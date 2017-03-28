package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Dao {
	
//  TODO:	
//	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
//	static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/scraper?user=root&useSSL=false";
//	static final String USER = "root";
//	static final String PASS ="cs160";
//
//	public Dao() {
//	}
//	
//	public static void saveSearch(String phrase, String loc, int site){
//		Connection conn = null;
//		Statement stmt = null;
//		try{
//			Class.forName("com.mysql.jdbc.Driver");
//			System.out.println("Connecting to a database...");
//			conn = DriverManager.getConnection(DB_URL, USER, PASS);
//			System.out.println("Connected Successfully");
//			System.out.println("Creating statement...");
//			stmt = conn.createStatement();
//			String sql = "INSERT INTO save-srch (phrase, loc, site)"
//					+ "VALUES ('" + phrase + "','" + loc + "','" + Integer.toString(site) + "')";
//			stmt.executeUpdate(sql);
//		   }catch(SQLException se){
//		      se.printStackTrace();
//		   }catch(Exception e){
//		      e.printStackTrace();
//		   }finally{
//		      try{
//		         if(stmt!=null)
//		            conn.close();
//		      }catch(SQLException se){
//		      }
//		      try{
//		         if(conn!=null)
//		            conn.close();
//		      }catch(SQLException se){
//		         se.printStackTrace();
//		      }
//		   }
//	}

}
