import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Hashtable;

public class DatabaseAccountRepository /*implements IAccountRepository*/ {
	
	private static String URL = "jdbc:mysql://";
	private static String Port = "3306";
	private static String DBName = "SecurityProject";
	private static String Driver = "com.mysql.jdbc.Driver";
	private static String Username = "root";
	private static String Password = "swordfish";
	private static String Host = "127.0.0.1";
	
	private static Hashtable<String,PreparedStatement> pstatements = new Hashtable<String,PreparedStatement>();
	private static int UserId = -1;
	
	private static Connection conn;
	
	private static DatabaseAccountRepository instance;
	
	//@Override
	public synchronized static String getPasswordHash(String username) {
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
	
	private static PreparedStatement prepare(String query, ArrayList<String> params) {
		try {
			PreparedStatement pt;
			if(pstatements.containsKey(query)) {
				pt = pstatements.get(query);
			} else {
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

	private static boolean setData(String query, ArrayList<String> params) {
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
	
	private static ArrayList<ArrayList<String>> getData(String query, ArrayList<String> params) {
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
	
	private static boolean startTrans() {
		try {
			conn.setAutoCommit(false);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean endTrans() {
		try {
			conn.commit();conn.setAutoCommit(true);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean rollbackTrans() {
		try {
			conn.rollback();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//@Override
	public synchronized static void addAccount(String username, String hash, int balance) {
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
	
	public synchronized static int getFundsAvailable(String username) {
		//TODO: Implements
		int balance = -1;
		
		if(!connect()) {
			return balance;
		}
		
		try {
			String query = "SELECT balance FROM UserFund JOIN UserPass ON UserFund.client_id=UserPass.id WHERE username=?;";
			ArrayList<String> queryParams = new ArrayList<String>();
			queryParams.add(username);
			
			startTrans();
			ArrayList<ArrayList<String>> results = getData(query, queryParams);
			
			balance = Integer.parseInt(results.get(1).get(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		while(!close()) {}
		
		return balance;
	}
	
	public static boolean connect() {
		try{
			Class.forName(Driver).newInstance();
			conn = DriverManager.getConnection(URL+Host+":"+Port+"/"+DBName, Username, Password);
			return true;
		} catch(Exception e) {
			System.err.println("Error while connection to SQL database");
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean close() {
		try {
			conn.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public static void main(String[] args) {
		/*addAccount("shannon_weir",
				Base64.getEncoder().encodeToString(Security.hash("swordfish")),
				400);*/
		
		//System.out.println(Security.verifyPassword("swordfish", Base64.getDecoder().decode(getPasswordHash("shannon_weir"))));
		System.out.println(getFundsAvailable("shannon_weir"));
		/*
		
		if(!connect()) {
			return;
		}
		
		try {
			String query = "SELECT * FROM UserPass WHERE username='test2';";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();
			
			int row=0;
			while(rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int numCols = rsmd.getColumnCount();
				ArrayList<String> tempArray = new ArrayList<String>();
				for(int i = 1;i<=numCols;++i) {
					tempArray.add(rs.getString(i));
				}
				System.out.println(tempArray.toString());
				row++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		while(!close()) {}
		*/
	}
}
