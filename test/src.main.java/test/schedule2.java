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

import java.io.IOException;
//velocity
import java.io.StringWriter;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.quartz.*;

public class schedule2 {
   //0806
    public static void main(String[] args) {
    	
        //  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HHmmss");  
        //  LocalDateTime now = LocalDateTime.now();   
          String sched1= "145100";
          String sched2 = "180000";
     
         // Timer timer = new Timer();
          System.out.println("Timer Task");
          Timer timer = new Timer(); 
          
        	  
          TimerTask task = new TimerTask() {
              @Override
              public void run() {
            	  	System.out.println("run start");
            	  	
            	    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HHmmss");  
                    LocalDateTime now = LocalDateTime.now();
            	  	System.out.println(now);

                    String today = dtf.format(now);
                    String hour = today.substring(0,6);
            	  	System.out.println("hour is"+hour);

                  if(hour.contentEquals(sched1)) //am 09
                  {
                	  System.out.println("now!");
                     try {
						querycheck();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                  }//IF
                  else
                  {
                      if(hour.contentEquals(sched2)) //pm 18
                      {
                          try {
							querycheck();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                      }
                  }//else    
              }//RUN
          };//TIMERTASK
          timer.schedule(task, 5000, 32400000);
    
          
        }//main

    public  static void querycheck() throws IOException {
    System.out.println("querycheck begin");
    String driver = "oracle.jdbc.driver.OracleDriver";
    String url = "jdbc:oracle:thin:@localhost:1521:orcl";
    String id="SYSTEM";
    String pass="SYSTEM";
    Connection conn = null;
    Statement stmt = null;
    Statement stmt2 = null;
    
    ResultSet rset1 = null;
    ResultSet rset2 = null;
    
    StringBuilder sb = new StringBuilder();
    StringBuilder code = new StringBuilder();
    int n = 0;
    
    
    
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url,id,pass);
           
            String query = "SELECT DISTINCT TRANS_DATE FROM EDI WHERE EDI_RESULT is null AND TRANS_DATE > 20190618000000000000 ";
            String query2 = "SELECT LOT_NO FROM EDI WHERE company = null OR TRANS_DATE = null OR TRANS_TYPE = null OR IF_TYPE = null OR BODY_NO=null OR XML_ID=null";
            
            System.out.println("test1");
            stmt = conn.createStatement();
            stmt2 = conn.createStatement();
            
            rset1 = stmt.executeQuery(query);
            rset2 = stmt2.executeQuery(query2);

            
            //System.out.println("test2");
         
            //Extract data from resultset
            if(rset1.next() == false) { //rset에 결과가 없으면 정상으로 판별 
              //  System.out.println("normal");  
              //sb.append("System is normally working");
                n=1;
            }else {

                while(rset1.next()) {
                String trans_date = rset1.getString("trans_date");
                String year = trans_date.substring(0,4); //
                String month = trans_date.substring(4,6); //11                
                String date = trans_date.substring(6,8); //29
                String time = trans_date.substring(8,10);
                String minute = trans_date.substring(10,12);
                String second = trans_date.substring(12,14);
       
                sb.append(year+"-"+month+"-"+date+" "+time+":"+minute+":"+second);
                sb.append("\n");
            }//while()
                sb.append("These data's EDI_RESULT are still NULL");
                n=3;
            }//else
            rset1.close();
            
            //키값이 누락된 경우
            if(rset2.next() == false) {
                //sb.append("System is normaly working");
                n=1;
            }else {
                while(rset2.next()) {
                    String LOT_NO = rset2. getString("LOT_NO");
                    sb.append(LOT_NO);
                    sb.append("\n");
                }
                n=2;
            }
            rset2.close();
            Properties properties = new Properties();
            
            //0805 get the config file 
            FileInputStream ip = new FileInputStream("C:\\Users\\nayou\\eclipse-workspace\\test\\config.properties");
            properties.load(ip);
            switch(n) {
            case 1: sb.append(properties.getProperty("discription_normall"));
            		code.append(properties.getProperty("normal_code"));
            		break;
            case 2: sb.append(properties.getProperty("discription_ora_key"));
            		code.append(properties.getProperty("ora_key_code"));
            		break;
            case 3: sb.append(properties.getProperty("discription_ora_null"));
            		code.append(properties.getProperty("ora_null_code"));
            		break;
            case 4: sb.append(properties.getProperty("discription_sql_interfaced"));
    				code.append(properties.getProperty("sql_interfaced_code"));
    				break;
            case 5: sb.append(properties.getProperty("discription_sql_notinterfaced"));
    				code.append(properties.getProperty("sql_notinterfaced_code"));
    				break;
            }
            // rset1.close();
            System.out.println("mailsend");
            mailsend(sb,code);
            
            //Exception
            if(conn != null) {
                System.out.println("Connected");
            }else {
                System.out.println("Failed to make connection");
            }
        }//try
        
        catch(SQLException e) {
             System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
             System.out.print("Error SQL State: " + e.getSQLState() + " \nMessage: " + e.getMessage());
        }catch(Exception e) {
             e.printStackTrace();
             System.out.print(e.getStackTrace().toString());
        }
    }//queycheck()
    
    private static void mailsend(StringBuilder sb,StringBuilder code)throws AddressException, MessagingException, IOException{
         System.out.println("mailsend begin");
         String host = "smtp.gmail.com";
        
         //sender
         final String username = "************";
         final String password = "**********";
         //int port = 465;
         
         //content
         String recipient = "hamny888@naver.com";
         String subject = "Data interface Report"; //title
         
         String body = sb.toString(); //content < query result
         String body2 = code.toString(); //code put 
         StringBuffer messageBuffer = new StringBuffer();
         StringBuffer messageBuffer2 = new StringBuffer();
         messageBuffer.append(body.toString());
         messageBuffer2.append(body2.toString());
         //get and initialize an engine 
         VelocityEngine ve = new VelocityEngine();
         ve.init();
         
        //get the Template  
         System.out.println("get the Template");
         Template t = ve.getTemplate("src.main.java/report_html.vm ");  //call the template 
        
         //create a context and add data 
         System.out.println("create a context ");
         VelocityContext context = new VelocityContext();
         context.put("discription", body);  //body named as "discription"
         context.put("code",body2);
         //render the template into a StringWriter 
         System.out.println("Rendering");
         StringWriter writer = new StringWriter();
         t.merge(context,writer);
         System.out.println(writer.toString());
         
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

