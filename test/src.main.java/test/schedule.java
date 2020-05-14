package test;

import javax.mail.internet.*;
import javax.mail.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.time.*;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;
import java.io.FileInputStream;
import java.util.Date;
import java.io.IOException;
//velocity
import java.io.StringWriter;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;


public class schedule {
   
    public static void main(String[] args) throws SQLException  {

       String sched1 = "120000";
       String sched2 = "180000";
       Timer t = new Timer();
       
          TimerTask timecheck = new TimerTask() {
       	   @Override
                 public void run() {
       		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HHmmss");
       		LocalDateTime now = LocalDateTime.now();
       		String today = dtf.format(now);
       		if(today.equals(sched1)|| today.contentEquals(sched2)) {
       			try {
					querycheck();
					try {
						Thread.sleep(3000);
					}catch(InterruptedException e) {
						System.out.println(e.getMessage());
					}
				} catch (IOException | MessagingException | SQLException e) {
					e.printStackTrace();
				} 
       		}
                 };//RUN   
             };//TIMERTASK
             t.scheduleAtFixedRate(timecheck, new Date(), 1000); 
    /*	try {
			querycheck();
		} catch (IOException | MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	*/
    } //main

    public  static void querycheck() throws IOException, AddressException, MessagingException, SQLException {
    System.out.println("querycheck begin");
    String driver = "oracle.jdbc.driver.OracleDriver";
    String url = "jdbc:oracle:thin:@localhost:1521:orcl";
    String id="SYSTEM";
    String pass="SYSTEM";
    Connection conn = null;
    Statement stmt = null;
  //  Statement stmt2= null;
    Statement stmt3 = null; 
    Statement stmt4 = null;       
    
    ResultSet rset1 = null;
 //   ResultSet rset2 = null;
    ResultSet rset3 = null;
    ResultSet rset4 = null; 
    
    StringBuilder sb = new StringBuilder();
    StringBuilder code = new StringBuilder();
    StringBuilder sql_code = new StringBuilder();
    StringBuilder sql_discription = new StringBuilder(); 
    StringBuilder discription = new StringBuilder();
    
    int sql_total=0;
    int oracle_total=0;   
    int n = 0;
    int s =0;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url,id,pass);
           
           // String query = "SELECT DISTINCT TRANS_DATE FROM EDI WHERE EDI_RESULT is null ";
            String query = "SELECT DISTINCT TRANS_DATE FROM EDI WHERE EDI_RESULT is null AND TRANS_DATE > 20190618000000000000 ";

         //   String query2 = "SELECT LOT_NO FROM EDI WHERE company = null OR TRANS_DATE = null OR TRANS_TYPE = null OR IF_TYPE = null OR BODY_NO=null OR XML_ID=null";
            String query3 = "SELECT COUNT(*) as oracle_count FROM EDI WHERE EDI_RESULT is null";
            
            stmt = conn.createStatement();    
      //    stmt2 = conn.createStatement();
            stmt3=conn.createStatement();            
            rset1 = stmt.executeQuery(query);
        //    rset2 = stmt2.executeQuery(query2);
            rset3 = stmt3.executeQuery(query3);
    
            System.out.println("Extracting rset1....................");
            
            //Extract data from resultset
            if(rset1.next() == false) { 
                n=1;
                sb.append(" ");
                System.out.println("nothing in rset1");
            }else {
            	System.out.println("There is a null");
                while(rset1.next()) {            //trans_Date extract 
                String trans_date = rset1.getString("trans_date");
                String year = trans_date.substring(0,4); //
                String month = trans_date.substring(4,6); //11                
                String date = trans_date.substring(6,8); //29
                String time = trans_date.substring(8,10);
                String minute = trans_date.substring(10,12);
                String second = trans_date.substring(12,14);
       
                sb.append(year+"-"+month+"-"+date+" "+time+":"+minute+":"+second);
                sb.append("\n");
            }//while
              sb.append("These data's EDI_RESULT are still null \n");
                n=3;
            }//else
           rset1.close();
           
            System.out.println("Extracting rset2....................");
         
   /*         if(rset2.next() == false) {      
                n=1;
            }else {                  
                while(rset2.next()) {
                    String LOT_NO = rset2. getString("LOT_NO");
                    sb.append(LOT_NO);
                    sb.append("\n");
                }
                n=2;
            }         
            rset2.close();*/
            System.out.println("Extracting rset3....................");
            	while(rset3.next()) {
            		System.out.println("rset3 data poop poop poop");
                	int oracle_count = rset3.getInt("oracle_count"); //oracle-> count    
                	System.out.println("Oracle_count is "+ oracle_count);
                	oracle_total = oracle_count;
                }    
            rset3.close(); 
            
        } catch(SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            System.out.print("Error SQL State: " + e.getSQLState() + " \nMessage: " + e.getMessage());
       }catch(Exception e) {
            e.printStackTrace();
            System.out.print(e.getStackTrace().toString());
       }

        //mysql connection
        try {
        	System.out.println("\n sql connection start ....");
        	Class.forName("com.mysql.jdbc.Driver");
        	Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/new_schema?autoReconnect=true&useSSL=false","root","toor");
        	String sql = "SELECT sum(LOOP_CNT) as sql_count FROM table1";
            stmt4 = con.createStatement();
        	rset4 = stmt4.executeQuery(sql);
        	
        	System.out.println("Extracting rset4....................");
        //extract mysql count
        	while(rset4.next()) {
        		int sql_count = rset4.getInt("sql_count");
        		System.out.println("This is sql_count "+ sql_count);
        		sql_total = sql_count;
        		}
        		
        	rset4.close();
        }catch(Exception e) {
    		System.err.println("Got an exception!");
    		System.err.println(e.getMessage());
    	}
    //compare sql_count and oracle_count
        if(sql_total == oracle_total) {
        	s=2;
        }else {
        	s=1;
        }
        
            Properties properties = new Properties();
            
            //0805 get the config file 
            FileInputStream ip = new FileInputStream("C:\\Users\\nayou\\eclipse-workspace\\test\\config.properties");
            properties.load(ip);
            
            FileInputStream ip2 = new FileInputStream("C:\\Users\\nayou\\eclipse-workspace\\test\\sql.properties");
            properties.load(ip2);
            
            
            //oracle case
            switch(n) {
            case 1: discription.append(properties.getProperty("discription_normall"));
            		code.append(properties.getProperty("normal_code"));
            		break;
            case 2: discription.append(properties.getProperty("discription_ora_key"));           //Case2 not working
            		code.append(properties.getProperty("ora_key_code"));
            		break;
            case 3: discription.append(properties.getProperty("discription_ora_null"));
            		code.append(properties.getProperty("ora_null_code"));
            		break;

            }
            
            //sql case
            switch(s) {
            case 1: sql_discription.append(properties.getProperty("discription_sql_interfaced"));
					sql_code.append(properties.getProperty("sql_interfaced_code"));
					break;
            case 2: sql_discription.append(properties.getProperty("sql_normal"));
					sql_code.append(properties.getProperty("sql_normal_code"));
					break;
					
            }
          
            mailsend(sb,code,discription,sql_discription,sql_code);
            
            //Exception
            if(conn != null) {
                System.out.println("Connected");
            }else {
                System.out.println("Failed to make connection");
            }
      
    }//queycheck()
    
    private static void mailsend(StringBuilder sb,StringBuilder code,StringBuilder discription,StringBuilder sql_discription, StringBuilder sql_code)throws AddressException, MessagingException, IOException{
         System.out.println("mailsend begin");
         String host = "smtp.gmail.com";
        
         //senderf
         final String username = "***************";
         final String password = "******************";
         //int port = 465;
         
         //content
         String recipient = "***************";
         String subject = "Data interface Report"; //title
         
         String discript = discription.toString();
         String body = sb.toString(); //content < query result
         String body2 = code.toString(); //code put 
         String sql_discrip = sql_discription.toString();
         String body3 = sql_code.toString();
         
         StringBuffer messageBuffer = new StringBuffer();
         StringBuffer messageBuffer2 = new StringBuffer();
         StringBuffer messageBuffer3 = new StringBuffer();
         StringBuffer messageBuffer4 = new StringBuffer();
         StringBuffer messageBuffer5 = new StringBuffer();
         
         messageBuffer.append(body.toString());
         messageBuffer2.append(body2.toString());
         messageBuffer3.append(discript.toString());
         //sql
         messageBuffer4.append(sql_discrip.toString());
         messageBuffer5.append(body3.toString());
         
         //get and initialize an engine 
         VelocityEngine ve = new VelocityEngine();
         ve.init();
         
        //get the Template  
         System.out.println("get the Template");
         Template t = ve.getTemplate("src.main.java/report_html.vm ");  //call the template 
        
         //create a context and add data 
         System.out.println("create a context ");
         VelocityContext context = new VelocityContext();
         context.put("discription",discript);
         context.put("trans_date", body);  //body named as "trans_date"
         context.put("code",body2);
         context.put("sql_discription",sql_discrip);
         context.put("sql_code",body3);
         
         //render the template into a StringWriter 
       
         StringWriter writer = new StringWriter();
         t.merge(context,writer);

         
       //property - SMTP information
         Properties props = new Properties();
         props.put("mail.smtp.host",host);
         props.put("mail.smtp.auth","true");         
         props.put("mail.smtp.port",465);   //465 google 
         props.put("mail.smtp.ssl.enable","true");
         props.put("mail.smtp.ssl.trust",host);  //smtp.gmail.com
         
         //Session
         Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
             
             protected javax.mail.PasswordAuthentication getPasswordAuthentication(){
                 return new javax.mail. PasswordAuthentication(username,password);
                 
             }
         });
         session.setDebug(true);
         System.out.println("Session");
         Message mimeMessage = new MimeMessage(session);
         mimeMessage.setFrom(new InternetAddress("hamny88@gmail.com"));
         mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
    
         
         mimeMessage.setSubject(subject); //title
      
         mimeMessage.setContent(writer.toString(), "text/html");
   
         mimeMessage.setSentDate(new java.util.Date());
         
         //connect to transport
         Transport.send(mimeMessage);
         System.out.println("message sent successfuly");
                  
     }//mailsend
         
        
    }//public class

