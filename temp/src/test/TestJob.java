package test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.TimerTask;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TestJob implements Job {
	
	@Override
	public void execute(JobExecutionContext context)throws JobExecutionException  {
	//	 public  static void querycheck() {
	    
			    String driver = "oracle.jdbc.driver.OracleDriver";
			    String url = "jdbc:oracle:thin:@localhost:1521:orcl";
			    String id="SYSTEM";
			    String pass="SYSTEM";
			    Connection conn = null;
			    Statement stmt = null;
			    ResultSet rset = null;
			    StringBuilder sb = new StringBuilder();

			        try {
			            Class.forName(driver);
			            conn = DriverManager.getConnection(url,id,pass);
			            String query = "SELECT DISTINCT TRANS_DATE FROM EDI WHERE EDI_RESULT AND TRANS_DATE > 20190618000000000000 ";

			            stmt = conn.createStatement();
			            rset = stmt.executeQuery(query);
			            
			            //Extract data from resultset
			            if(rset!=null) {
			            	
			            
			            while(rset.next()) {
			                String trans_date = rset.getString("trans_date");
			                String year = trans_date.substring(0,4); //
			                
			                String month = trans_date.substring(4,6); //11
			                
			                String date = trans_date.substring(6,8); //29
			                String time = trans_date.substring(8,10);
			                String minute = trans_date.substring(10,12);
			                String second = trans_date.substring(12,14);

			       


			                sb.append(year+"-"+month+"-"+date+" "+time+":"+minute+":"+second);
			                sb.append("\n");
			            }//while()
			            }else {
			            	sb.append("system is running normally");
			            	mailsend(sb);
			            }
			            rset.close();
			            
			          
			            //Exception
			            if(conn != null) {
			                System.out.println("Connected");
			            }else {
			                System.out.println("Failed to make connection");
			            }
			        }catch(SQLException e) {
			             System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			             System.out.print("Error SQL State: " + e.getSQLState() + " \nMessage: " + e.getMessage());
			        }catch(Exception e) {
			             e.printStackTrace();
			             System.out.print(e.getStackTrace().toString());
			        }
			    }//queycheck()
			    
			    private static void mailsend(StringBuilder sb)throws AddressException, MessagingException{
			         String host = "smtp.gmail.com";
			          //sender
			         final String username = "******@gmail.com";
			         final String password = "**********";
			         //int port = 465;
			         
			         //content
			         String recipient = "hamny888@naver.com";
			         String subject = "Data interface Error"; //title
			         String body = "Error Trans_date \n \n"+sb.toString(); //content < query result
			         
			         //property - SMTP information
			         Properties props = new Properties();
			         props.put("mail.smtp.host",host);
			         props.put("mail.smtp.auth","true");
			         props.put("mail.smtp.port",465);   //465 g
			         props.put("mail.smtp.ssl.enable","true");
			         props.put("mail.smtp.ssl.trust",host);  //smtp.gmail.com
			         
			         //Session
			         Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			             
			             protected javax.mail.PasswordAuthentication getPasswordAuthentication(){
			                 return new javax.mail. PasswordAuthentication(username,password);
			                 
			             }
			         });
			         session.setDebug(true);
			         
			         Message mimeMessage = new MimeMessage(session);
			         mimeMessage.setFrom(new InternetAddress("hamny88@gmail.com"));
			         mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			         
			         mimeMessage.setSubject(subject); //title
			         mimeMessage.setText(body); // content
			         Transport.send(mimeMessage);
			         System.out.println("message sent successfuly");
			         
			     }//mailsend
		
	}	


