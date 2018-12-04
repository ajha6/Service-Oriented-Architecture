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

import cs601.project4.AppParams;
import httpUtil.HttpReqUtil;
import model.DatabaseManager;
import model.objects.Event;

/**
 * @author anuragjha
 *
 */
public class EventCreateServlet extends HttpServlet {

	/**
	 * 
	 */
	public EventCreateServlet() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected synchronized void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println("in doPost of EventCreateServlet");
		
		System.out.println(req.getPathInfo() +"***"+ req.getRequestURI());
		
		//System.out.println("req body to EvenCreateServlet: " + new Proj4HTTPReader().httpBody(req));
		
		
		String[] subPaths = req.getRequestURI().split("/");
		System.out.println("length: " + subPaths.length);
		for(String path : subPaths) {
			System.out.println("subpaths: " + path);
		}
		
		if((subPaths.length == 2) && (subPaths[1].equals("create"))) {
			
			AppParams appParams = new HttpReqUtil().reqParamsFromJsonBody(req);
			String respResult = "Response Result: params in webservice req body\n" 
			+ "userid: " + appParams.getUserid() + "\n"
			+ "eventName: " + appParams.getEventname() + "\n"
			+ "numTickets: " + appParams.getNumtickets();
			System.out.println("respResult: " + respResult);
			
			//TODO : add a event in database
			String result = this.getResult(appParams.getEventname(), 
					appParams.getUserid(), 
					appParams.getNumtickets(),
					appParams.getNumtickets(), 0);
			
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.setContentLength(respResult.length());
			
			resp.getWriter().println(result);
			resp.getWriter().flush();
			
		} else {
			System.out.println("bad bad request");
		}
		
	}
	
	
	private String getResult(String eventname, int userid, int numtickets, int avail, int purchased) {
		DatabaseManager dbm1 = new DatabaseManager();
		System.out.println("Connected to database");

		int result = dbm1.eventsTableAddEntry(eventname, userid, numtickets, avail, purchased); 

		dbm1.close();

		Gson gson = new Gson();
		String resultJson = gson.toJson(result, Integer.class);
		System.out.println("result:::::: " + resultJson);


		return resultJson;
	}

}
