package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//oracle 
public class JDBCModel {
	
	
	public static void main(String[] args) {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:orcl";
		String id="SYSTEM";
		String pass="SYSTEM";

		
		System.out.print("Test Print \n");
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;	
		try { 
			Class.forName(driver);
			conn = DriverManager.getConnection(url,id,pass);

			String query = "SELECT COUNT(*) as oracle_count FROM EDI WHERE EDI_RESULT is null";
			stmt = conn.createStatement();
			rset = stmt.executeQuery(query);

			//Extract data from result set 
		
			while(rset.next()) {
				System.out.println("test2");
				int oracle_count = rset.getInt("oracle_count") ;
            	System.out.println(oracle_count);
			
			}

			System.out.println("test3");
			if (conn != null) {
                System.out.println("Connected");
            } else {
                System.out.println("Failed to make connection");
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            System.out.print("Error SQL State: " + e.getSQLState() + " \nMessage: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print(e.getStackTrace().toString());
        }
	}//public main 

}//public class
