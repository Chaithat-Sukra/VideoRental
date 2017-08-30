package utilities;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.PreparedStatement;

public class SQLAdapter {
	private static SQLAdapter instance = null;
//	protected SQLAdapter() {
//		//Exists only to defeat instantiation.
//	}
	public static SQLAdapter getInstance() {
		if(instance == null) {
			instance = new SQLAdapter();
		}
		return instance;	      
	}

	String dbName = "testDB";
	String dbUserName = "root";
	String dbPassword = "ch@mp2727";

	Connection connection = null;
	Statement stmt = null;
	ResultSet rs = null;

	public boolean sqlConnect() throws ClassNotFoundException {
		String connectionString = "jdbc:mysql://127.0.0.1:3307/" + dbName + "?user=" + dbUserName + "&password=" + dbPassword + "&useUnicode=true&characterEncoding=UTF-8";

		Class.forName("com.mysql.jdbc.Driver");
		try {
			connection = DriverManager.getConnection(connectionString);
			//System.out.println("connected");

		} catch (SQLException e) {
			//System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return false;
		}
		
		if (connection != null) {
			//System.out.println("You made it, take control your database now!");
			return true;
		} else {
			//System.out.println("Failed to make connection!");
			return false;
		}
	}
	
	public ArrayList<Map<String, String>> getData(String[] aMappingList, String aQueryString) {
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(aQueryString);
			
			Map<String, String> mappingData = new HashMap<String, String>();
			for (String aMappingKey : aMappingList) {
				mappingData.put(aMappingKey, "");
			}
			
			ArrayList<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
			while (rs.next()) {
				Map<String, String> tempData = new HashMap<String, String>();
				for (Map.Entry<String, String> entry : mappingData.entrySet()) {
					String key = entry.getKey();
					String value = rs.getString(key);
					tempData.put(key, value);
				}	
//				System.out.println(tempData);
				dataList.add(tempData);
			}
			return dataList;

		} catch (SQLException e) {
			System.out.println("invalid query statement or invalid mapping key");
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean insertIntoTable(String aQueryString, List<Object> aObjects) {
		try {
	        PreparedStatement statement = (PreparedStatement)connection.prepareStatement(aQueryString);
			for (int i = 0; i < aObjects.size(); i++) {
//				System.out.println(aObjects.get(i));
				statement.setObject(i + 1, aObjects.get(i));
			}
			statement.executeUpdate();	
			return true;
		
		} catch (SQLException aEx) {
			System.out.println(aEx.getMessage());
			aEx.printStackTrace();
			return false;
		}
	}
	
	public boolean updateTable(String aQueryString, List<Object> aObjects) {
		try {
	        PreparedStatement statement = (PreparedStatement)connection.prepareStatement(aQueryString);
			for (int i = 0; i < aObjects.size(); i++) {
//				System.out.println(aObjects.get(i));
				statement.setObject(i + 1, aObjects.get(i));
			}
			statement.executeUpdate();	
			return true;
		
		} catch (SQLException aEx) {
			System.out.println(aEx.getMessage());
			aEx.printStackTrace();
			return false;
		}
	}
}