/**
 * 
 */
package service.event;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cs601.project4.AppConstants;
import httpUtil.HttpConnection;
import httpUtil.HttpReqUtil;
import httpUtil.HttpRespUtil;
import model.DatabaseManager;
import model.objects.AppParams;
import model.objects.Event;
import model.objects.ResultEmpty;
import model.objects.ResultOK;

/**
 * @author anuragjha
 *
 */
public class EventTicketsPurchaseServlet extends HttpServlet {

	private DatabaseManager dbm1;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		dbm1 = new DatabaseManager();  // close it

		boolean isSuccess = false;
		String result = "";

		System.out.println("in doPost of EventTicketsPurchaseServlet");
		System.out.println(req.getRequestURI());

		String[] subPaths = req.getRequestURI().split("/");

		if((subPaths.length == 3) && (subPaths[1].equals("purchase")) &&
				(subPaths[2].matches("[0-9]+")) && (Integer.parseInt(subPaths[2]) > 0)) {
			
			AppParams appParams = new HttpReqUtil().reqParamsFromJsonBody(req);
			
			if(Integer.parseInt(subPaths[2]) == appParams.getEventid()) {
				isSuccess = this.getResult(appParams.getUserid(), appParams.getEventid(), 
						appParams.getTickets());
			}

		} 
		
		result = this.setResponse(resp, isSuccess);
		
		new HttpRespUtil().writeResponse(resp, result); 

		dbm1.close(); // closing dbm 
	}

	
	private String setResponse(HttpServletResponse resp, boolean isSuccess) {
		if(isSuccess) {
			resp.setStatus(HttpServletResponse.SC_OK);
			ResultOK ro = new ResultOK("Event tickets purchased");
			return new Gson().toJson(ro, ResultOK.class);
		} else {

			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			ResultEmpty re = new ResultEmpty("Tickets could not be purchased");
			return new Gson().toJson(re, ResultEmpty.class);
		}
	}

	private boolean getResult(int userid, int eventid, int tickets) {

		if(this.checkAndUpdateEventTable(eventid, tickets)) {
			if(this.addUserTickets(userid, eventid, tickets)) {
				return true;
			} else {
				//this.updateEventTable(eventid, tickets);
				this.checkAndUpdateEventTable(eventid, -tickets);
				return false;
			}
		}

		return false;
	}


	private boolean checkAndUpdateEventTable(int eventid, int tickets) {

		ResultSet result = dbm1.eventsTableGetEventDetails(eventid);  //get event detail
		Event event = new Event(result);

		int tableUpdate = 0;

		if((tickets > 0) && (eventid > 0) && 
				(event.getEventid() == eventid) && (event.getAvail() >= tickets)) {

			tableUpdate = dbm1.eventsTableUpdateForTickets(event.getAvail() - tickets,
					event.getPurchased() + tickets, eventid);

		} else if((tickets < 0) && (eventid > 0) && (event.getEventid() == eventid)) {
			System.out.println("roll back to : " + (event.getAvail() - tickets) + " : " + (event.getPurchased() + tickets) + " : " + eventid);

			tableUpdate =  dbm1.eventsTableUpdateForTickets((event.getAvail() - tickets),
					(event.getPurchased() + tickets), eventid);
		}

		if(tableUpdate > 0) {
			return true;
		}
		return false;
	}




	private boolean addUserTickets(int userid, int eventid, int tickets) {

		HttpConnection httpCon = null;
//		httpCon = new HttpConnection("http://localhost:7072/"+userid+"/tickets/add");
		httpCon = new HttpConnection(AppConstants.getInit().
				getBasepathUserService()+"/"+userid+"/tickets/add");

		httpCon.setRequestMethod("POST");
		httpCon.setRequestProperty("Content-Type", "application/json");
		httpCon.setDoOutput(true);
		try {
			httpCon.getConn().connect();
			httpCon.writeRequestBody(
					"{ \"eventid\": "+eventid+", \"tickets\": "+tickets+" }");

			String respStatus = httpCon.readResponseHeader().get(null).get(0);
			System.out.println("EventsTicketsPurchase : addUserTickets : "
					+ "respCode :" + httpCon.readResponseCode() +" : "+respStatus);
			if(httpCon.readResponseCode() == 200) {
				return true;

			}
		} catch (IOException e) {
			System.out.println("Cannot connect to User Service");
			return false;
		}
		return false;
	}




}
