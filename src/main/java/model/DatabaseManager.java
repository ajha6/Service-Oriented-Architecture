/**
 * 
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author anuragjha
 *
 */
public class DatabaseManager {

	private static final String username = "user20";
	private static final String password = "user20"; 
	private static final String db = "user20"; 
	private Connection con;// = DatabaseConnector.connectToDataBase(username, password, db);
	private PreparedStmts preparedStmts;// = new PreparedStmts(con);

	/**
	 * constructor
	 */
	public DatabaseManager() {
		con = DatabaseConnector.connectToDataBase(username, password, db);
		preparedStmts = new PreparedStmts(con);

	}


	/**
	 * @return the con
	 */
	public Connection getCon() {
		return con;
	}

	/**
	 * @return the preparedstmts
	 */
	public PreparedStmts getPreparedstmts() {
		return preparedStmts;
	}

	public int eventsTableAddEntry(String eventname, int userid, int numtickets, 
			int avail, int purchased) {
		return this.getEventsTableAddEntry(eventname, userid, numtickets, avail, purchased);
	}


	public ResultSet eventsTableGetEventList() {
		return this.getEventsTableGetEventList();
	}

	public ResultSet eventsTableGetEventDetails(int eventid) {
		return this.getEventsTableGetEventDetails(eventid);
	}


	public int transactionTableAddEntry(int eventid, int userid, int numtickets, int numtransfer, int targetuserid) {
		return this.getTransactionTableAddEntry(eventid, userid, numtickets, numtransfer, targetuserid);
	}


	public ResultSet eventsTableUpdateForTickets(int avail, int purchased, int eventid) {
		return this.getEventsTableUpdateForTickets(avail, purchased, eventid);
	}


	public ResultSet ticketsTableUpdateForTickets(int numtickets, int eventid, int userid) {
		return this.getTicketsTableUpdateForTickets(numtickets, eventid, userid);
	}


	public int userTableAddEntry(String username) {
		return this.getUserTableAddEntry(username);
	}


	public ResultSet userTableGetUserDetails(int userid) {
		return this.getUserTableGetUserDetails(userid);
	}

	public ResultSet ticketsTableGetEventidForUser(int userid) {
		return this.getTicketsTableGetEventidForUser(userid);
	}


	public int ticketsTableAddEntry(int eventid, int userid, int numtickets) {
		return getTicketsTableAddEntry(eventid, userid, numtickets);
	}


	///////////////////////TODO: dividing marker


	private synchronized int getEventsTableAddEntry(String eventname, int userid, int numtickets, 
			int avail, int purchased) {

		int eventid = 0;

		PreparedStatement sqlStmt = preparedStmts.getEventsTableAddEntry();
		try {
			sqlStmt.setString(1, eventname);
			sqlStmt.setInt(2, userid);
			sqlStmt.setInt(3, numtickets);
			sqlStmt.setInt(4, avail);
			sqlStmt.setInt(5, purchased);

			//boolean sqlStatus = sqlStmt.execute();
			//TODO://retrieving value
			if(sqlStmt.executeUpdate() > 0) {   //executing update
				ResultSet insertID = preparedStmts.getLastInsertID().executeQuery();
				if(insertID.next()) {
					eventid = insertID.getInt(1);
					System.out.println("last insert id: " + eventid);
					
				}
			}

			//this.getCon().close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return eventid;
	}


	private ResultSet getEventsTableGetEventList() {

		ResultSet result = null;

		PreparedStatement sqlStmt = preparedStmts.getEventsTableGetEventList();
		try {

			result = sqlStmt.executeQuery();

			//this.getCon().close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}



	private ResultSet getEventsTableGetEventDetails(int eventid) {
		ResultSet result = null;

		PreparedStatement sqlStmt = preparedStmts.getEventsTableGetEventDetails();
		try {

			sqlStmt.setInt(1, eventid);;

			result = sqlStmt.executeQuery();

			//this.getCon().close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}



	private synchronized int getTransactionTableAddEntry(int eventid, int userid, int numtickets, int numtransfer,
			int targetuserid) {
		int transactionid = 0;

		PreparedStatement sqlStmt = preparedStmts.getTransactionTableAddEntry();
		try {
			sqlStmt.setInt(1, eventid);
			sqlStmt.setInt(2, userid);
			sqlStmt.setInt(3, numtickets);
			sqlStmt.setInt(4, numtransfer);
			sqlStmt.setInt(5, targetuserid);

			//boolean sqlStatus = sqlStmt.execute();
			//TODO://retrieving value
			if(sqlStmt.executeUpdate() > 0) {   //executing update
				ResultSet insertID = preparedStmts.getLastInsertID().executeQuery();
				if(insertID.next()) {
					transactionid = insertID.getInt(1);
					System.out.println("last insert id: " + transactionid);
					
				}
			}

			//this.getCon().close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return transactionid;

	}


	private synchronized ResultSet getEventsTableUpdateForTickets(int avail, int purchased, int eventid) {

		ResultSet result = null;

		PreparedStatement sqlStmt = preparedStmts.getEventsTableAddEntry();
		try {
			sqlStmt.setInt(1, avail);
			sqlStmt.setInt(2, purchased);
			sqlStmt.setInt(3, eventid);

			result = sqlStmt.executeQuery();

			//this.getCon().close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}



	private synchronized ResultSet getTicketsTableUpdateForTickets(int numtickets, int eventid, int userid) {

		ResultSet result = null;

		PreparedStatement sqlStmt = preparedStmts.getEventsTableAddEntry();
		try {
			sqlStmt.setInt(1, numtickets);
			sqlStmt.setInt(2, eventid);
			sqlStmt.setInt(3, userid);

			result = sqlStmt.executeQuery();

			//this.getCon().close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}


	private synchronized int getUserTableAddEntry(String username) {

		int userid = 0;

		PreparedStatement sqlStmt = preparedStmts.getEventsTableAddEntry();
		try {
			sqlStmt.setString(1, username);

			//boolean sqlStatus = sqlStmt.execute();
			//TODO://retrieving value
			if(sqlStmt.executeUpdate() > 0) {   //executing update
				ResultSet insertID = preparedStmts.getLastInsertID().executeQuery();
				if(insertID.next()) {
					userid = insertID.getInt(1);
					System.out.println("last insert id: " + userid);
					
				}
			}

			//this.getCon().close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return userid;
	}


	private ResultSet getUserTableGetUserDetails(int userid) {

		ResultSet result = null;

		PreparedStatement sqlStmt = preparedStmts.getEventsTableGetEventDetails();
		try {

			sqlStmt.setInt(1, userid);

			result = sqlStmt.executeQuery();

			//this.getCon().close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}


	private ResultSet getTicketsTableGetEventidForUser(int userid) {
		ResultSet result = null;

		PreparedStatement sqlStmt = preparedStmts.getEventsTableGetEventDetails();
		try {

			sqlStmt.setInt(1, userid);

			result = sqlStmt.executeQuery();

			//this.getCon().close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}
	
	
	private synchronized int getTicketsTableAddEntry(int eventid, int userid, int numtickets) {

		int ticketid = 0;

		PreparedStatement sqlStmt = preparedStmts.getTicketsTableAddEntry();
		try {
			sqlStmt.setInt(1, eventid);
			sqlStmt.setInt(2, userid);
			sqlStmt.setInt(3, numtickets);

			//boolean sqlStatus = sqlStmt.execute();
			//TODO://retrieving value
			if(sqlStmt.executeUpdate() > 0) {   //executing update
				ResultSet insertID = preparedStmts.getLastInsertID().executeQuery();
				if(insertID.next()) {
					ticketid = insertID.getInt(1);
					System.out.println("last insert id: " + ticketid);
					
				}
			}

			//this.getCon().close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ticketid;
	}



	public void close() {
		try {
			con.close();
		} catch (SQLException e) {
			System.out.println("Error in closing connection");
			e.printStackTrace();
		}
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		DatabaseManager dbm1 = new DatabaseManager();
		System.out.println("Connected to database");
		
		int id = dbm1.eventsTableAddEntry("testEvent1", 4, 15, 14, 1);
		System.out.println("id ::::::: " + id);

		ResultSet result = dbm1.eventsTableGetEventList();  //get event list
		try {
			while(result.next()) {
				System.out.println("result ::::::: " + result.getInt(1) + " " + result.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
//		id = dbm1.eventsTableAddEntry("testEvent13", 4, 15, 14, 1);
//		System.out.println("id ::::::: " + id);
//		
		dbm1.close();

		


	}




}
