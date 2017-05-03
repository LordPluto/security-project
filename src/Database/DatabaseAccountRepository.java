package Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class DatabaseAccountRepository implements IAccountRepository {
	
	private static final String URL = "jdbc:mysql://";
	private static final String Port = "3306";
	private static final String DBName = "SecurityProject";
	private static final String Driver = "com.mysql.jdbc.Driver";
	private static final String Username = "root";
	private static final String Password = "swordfish";
	private static final String Host = "127.0.0.1";
	
	private Hashtable<String,PreparedStatement> pstatements = new Hashtable<String,PreparedStatement>();
	private static int UserId = -1;
	
	private static Connection conn;
	
	private static DatabaseAccountRepository instance;
	
	@Override
	public synchronized String getPasswordHash(String username) {
		String retVal = "";
		if(!connect()) {
			return retVal;
		}
		
		try {
			String query = "SELECT password FROM UserPass WHERE username = ?;";
			ArrayList<String> queryParams = new ArrayList<String>();
			queryParams.add(username);
			
			startTrans();
			ArrayList<ArrayList<String>> results = getData(query, queryParams);
			
			retVal = results.get(1).get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		while(!close()) {}
		return retVal; 
	}
	
	private DatabaseAccountRepository() {
		//Setup file
	}
	
	public static DatabaseAccountRepository getInstance() {
		if (instance == null) instance = new DatabaseAccountRepository();
		return instance;
	}
	
	/**
	 * Set up the prepared statement to be run.
	 * @param query
	 * @param params
	 * @return
	 */
	private PreparedStatement prepare(String query, ArrayList<String> params) {
		try {
			PreparedStatement pt;
			if(pstatements.containsKey(query)) {
				pt = pstatements.get(query);
			} else {
				//Allows for retrieving keys on account generation
				pt = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			}
			
			for(int arg = 0;arg<params.size();++arg) {
				pt.setString(arg+1, params.get(arg));
			}
			return pt;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Modifying data in the database
	 * @param query
	 * @param params
	 * @return
	 */
	private boolean setData(String query, ArrayList<String> params) {
		try {
			PreparedStatement ps = prepare(query, params);
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) {
				UserId = rs.getInt(1);
			}
			return true;
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Retrieve data from the database.
	 * @param query
	 * @param params
	 * @return
	 */
	private ArrayList<ArrayList<String>> getData(String query, ArrayList<String> params) {
		try {
			PreparedStatement ps = prepare(query, params);
			ResultSet rs = ps.executeQuery();
			ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int numCols = rsmd.getColumnCount();
			ArrayList<String> columnArray = new ArrayList<String>();
			for(int col = 1;col<=numCols;++col) {
				columnArray.add(rsmd.getColumnName(col));
			}
			results.add(columnArray);
			while(rs.next()) {
				ArrayList<String> tempArray = new ArrayList<String>();
				
				for(int col = 1;col<=numCols;++col) {
					tempArray.add(rs.getString(col));
				}
				results.add(tempArray);
			}
			return results;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Start a SQL command
	 * @return
	 */
	private boolean startTrans() {
		try {
			conn.setAutoCommit(false);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * End a SQL command
	 * @return
	 */
	private boolean endTrans() {
		try {
			conn.commit();conn.setAutoCommit(true);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Undo a SQL command
	 * @return
	 */
	private boolean rollbackTrans() {
		try {
			conn.rollback();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public synchronized void addAccount(String username, String hash, int balance) {
		if(!connect()) {
			return;
		}
		
		try{			
			String insertPass = "INSERT INTO UserPass(username,password) VALUES(?,?);";
			String insertBalance = "INSERT INTO UserFund(balance,client_id) VALUES(?,?);";
			
			ArrayList<String> insertParams = new ArrayList<String>();
			insertParams.add(username);
			insertParams.add(hash);
			
			startTrans();
			if(setData(insertPass, insertParams)) {
				if(UserId > 0) {
					insertParams.clear();
					insertParams.add(""+balance);
					insertParams.add(""+UserId);
					if(setData(insertBalance,insertParams)) {
						endTrans();
						UserId = -1;
					}
					else {
						rollbackTrans();
					}
				}
				else {
					rollbackTrans();
				}
			}
			else {
				rollbackTrans();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		while(!close()) {}
	}
	
	@Override
	public synchronized double getFundsAvailable(String username) {
		double balance = -1;
		
		if(!connect()) {
			return balance;
		}
		
		try {
			String query = "SELECT balance FROM UserFund JOIN UserPass ON UserFund.client_id=UserPass.id WHERE username=?;";
			ArrayList<String> queryParams = new ArrayList<String>();
			queryParams.add(username);
			
			startTrans();
			ArrayList<ArrayList<String>> results = getData(query, queryParams);
			endTrans();
			
			balance = Double.parseDouble(results.get(1).get(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		while(!close()) {}
		
		return balance;
	}
	
	@Override
	public synchronized void modifyFundsAvailable(String username, double amount) {
		if(!connect()) {
			return;
		}
		
		try {
			String query = "UPDATE UserFund join UserPass on UserPass.id = UserFund.client_id set balance = balance + ? WHERE username = ?;";
			ArrayList<String> queryParams = new ArrayList<String>();
			queryParams.add(""+amount);
			queryParams.add(username);
			
			startTrans();
			setData(query, queryParams);
			UserId = -1;
			endTrans();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		while(!close()) {}
	}
	
	@Override
	public synchronized void setFundsAvailable(String username, double balance) {
		if(!connect()) {
			return;
		}
		
		try {
			String query = "UPDATE UserFund join UserPass on UserPass.id = UserFund.client_id set balance = ? WHERE username = ?;";
			ArrayList<String> queryParams = new ArrayList<String>();
			queryParams.add(""+balance);
			queryParams.add(username);
			
			startTrans();
			setData(query, queryParams);
			UserId = -1;
			endTrans();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		while(!close()) {}
	}
	
	@Override
	public synchronized void setFundsPassword(String username, String hash) {
		if(!connect()) {
			return;
		}
		
		try{			
			String query = "UPDATE UserFund join UserPass on UserPass.id = UserFund.client_id set client_password = ? WHERE username = ?;";			
			ArrayList<String> queryParams = new ArrayList<String>();
			queryParams.add(username);
			queryParams.add(hash);
			
			startTrans();
			setData(query, queryParams);
			UserId = -1;
			endTrans();
		} catch (Exception e) {
		}
		
		while(!close()) {}
	}
	
	@Override
	public synchronized String getFundsPassword(String username) {
		String retVal = "";
		if(!connect()) {
			return retVal;
		}
		
		try {
			String query = "SELECT client_password FROM UserPass join UserFund on UserPass.id = UserFund.client_id WHERE username = ?;";
			ArrayList<String> queryParams = new ArrayList<String>();
			queryParams.add(username);
			
			startTrans();
			ArrayList<ArrayList<String>> results = getData(query, queryParams);
			
			retVal = results.get(1).get(0);
		} catch (Exception e) {
		}
		
		while(!close()) {}
		return retVal;
	}
	
	private boolean connect() {
		try{
			Class.forName(Driver).newInstance();
			conn = DriverManager.getConnection(URL+Host+":"+Port+"/"+DBName+"?verifyServerCertificate=false&useSSL=true", Username, Password);
			return true;
		} catch(Exception e) {
			System.err.println("Error while connection to SQL database");
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean close() {
		try {
			conn.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
