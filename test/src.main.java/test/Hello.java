package test;
//my sql connection Test 
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class Hello {
	
    public static void main(String[] args) {
 
    	try {
    	Class.forName("com.mysql.jdbc.Driver");
    	Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/new_schema","root","toor");
    	String sql = "SELECT sum(LOOP_CNT) as total_count FROM table1";
    	Statement st = con.createStatement();
    	ResultSet rs = st.executeQuery(sql);
    	
    	while(rs.next()) {
    	int total_count = rs.getInt("total_count");
    	System.out.println(total_count);
    	}
    	rs.close();
    	

    	}catch(Exception e) {
    	    // System.err.format("SQL State: %s\n", e.getMessage());
             System.out.print("Error SQL Message: " + e.getMessage());
    	}
    }

}
