/**
 * 
 */
package service.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import httpUtil.HttpReqUtil;
import model.DatabaseManager;
import model.objects.AppParams;
import model.objects.EventId;
import model.objects.ResultEmpty;
import model.objects.UserId;

/**
 * @author anuragjha
 *
 */
public class UserCreateServlet extends HttpServlet {

	/**
	 * 
	 */
	public UserCreateServlet() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected synchronized void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String result = "";
		int userid = 0;

		System.out.println("in doPost of UserCreateServlet");
		System.out.println(req.getPathInfo() +"***"+ req.getRequestURI());

		String[] subPaths = req.getRequestURI().split("/");
		System.out.println("length: " + subPaths.length);
		for(String path : subPaths) {
			System.out.println("subpaths: " + path);
		}

		if((subPaths.length == 2) && (subPaths[1].equals("create"))) {

			AppParams appParams = new HttpReqUtil().reqParamsFromJsonBody(req);

			//TODO : add new user in database
			//result = "Add a new User";
			if(appParams.getUsername()!= null && (appParams.getUsername().length() > 0)) {
				////
				userid = this.getResult(appParams.getUsername());       //////  //method !!!!!!
				////
				//				if(result.contains("User unsuccessfully created")) {
				//					resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				//					
				//					System.out.println(" 1 bad bad request");
				//				} else {
				//					resp.setStatus(HttpServletResponse.SC_OK);	
				//
				//				}
			} 
			//			else {
			//				ResultEmpty re = new ResultEmpty("User unsuccessfully created");
			//				result = new Gson().toJson(re, ResultEmpty.class);
			//				System.out.println(" 2 bad bad request");
			//				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			//			}
		}


		if(userid!=0) {
			UserId userId = new UserId(userid);
			Gson gson = new Gson();
			result = gson.toJson(userId, UserId.class);
			resp.setStatus(HttpServletResponse.SC_OK);
		} else {
			//result = new ResultEmpty("User unsuccessfully created");
			ResultEmpty errorJson = new ResultEmpty("User unsuccessfully created");
			Gson gson = new Gson();
			result = gson.toJson(errorJson, ResultEmpty.class);
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentLength(result.length());

		resp.getWriter().println(result);
		resp.getWriter().flush();

	}




	public int getResult(String username) {

		//String resultJson = "";
		int userid = 0;
		DatabaseManager dbm1 = new DatabaseManager();
		System.out.println("Connected to database");


		if(dbm1.userTableIfUsernameExist(username) == 0) {
			userid = dbm1.userTableAddEntry(username);
			dbm1.close();
		}

		return userid;


		//		if(userid!=0) {
		//			UserId userId = new UserId(userid);
		//			Gson gson = new Gson();
		//			resultJson = gson.toJson(userId, UserId.class);
		//		} else {
		//			//result = new ResultEmpty("User unsuccessfully created");
		//			ResultEmpty errorJson = new ResultEmpty("User unsuccessfully created");
		//			Gson gson = new Gson();
		//			resultJson = gson.toJson(errorJson, ResultEmpty.class);
		//		}
		//
		//		System.out.println("result:::::: " + /*resultJson*/ resultJson);
		//
		//		return resultJson;
	}

}
