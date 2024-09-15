import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

class RetirementEmail
{
   public static void main(String [] args)
   {
 		try
 	    {
 			
 			Class.forName("com.mysql.jdbc.Driver").newInstance();

 		    Connection m_Connection = DriverManager.getConnection("jdbc:mysql://115.112.32.93/myden", "oltuser", "D3nav3@123");

 		    Statement m_Statement = m_Connection.createStatement();
 		    String query = "SELECT e.employeecode,e.employeename,p.project,e.doj AS 'Date Of Joining',"
 		    		+" DATE_ADD(DATE(e.birthdate),INTERVAL 58 YEAR) AS 'Date Of Retirement',"
 		    +" er.employeename AS 'Reporting Manager',e.employeeType"
 		    		+" FROM empinfootherinfo e"
 		    		+" JOIN empinfootherinfo er ON e.reportingmanager=er.emp_id"
 		    		+" JOIN projectmast p ON e.project=p.projectid"
 		    		+" WHERE e.workingstatus IN ('working') "
 		    		+" AND FLOOR(DATEDIFF(DATE_ADD(DATE(NOW()), INTERVAL 60 DAY),e.birthdate)/365)>=58"
 		    		+" AND DATE(e.birthdate)<>'1900-01-01'"
 		    		+ "#AND e.companyid=1 ";
 		    		
 		    System.out.format("query>>>>>>>."+query);
 		   
 		    ResultSet rs = m_Statement.executeQuery(query);
 	      
 	      // iterate through the java resultset
 	      while (rs.next())
 	      {
 	    	 String empCode = rs.getString("employeecode");
 	        String empName = rs.getString("employeename");
 	        Date dateOfJoining = rs.getDate("Date Of Joining");
 	       Date dateOfRetirement = rs.getDate("Date Of Retirement");
 	      String reportingManager = rs.getString("Reporting Manager");
 	        String employeeType= rs.getString("employeeType");
 	        String project=rs.getString("project");
 	        
 	        // print the results
 	        System.out.format("%s, %s, %s, %s, %s, %s\n", empCode, empName, dateOfJoining, dateOfRetirement, reportingManager, employeeType);
 	        
 	        sendEmail(empCode, empName, project, dateOfJoining, dateOfRetirement, reportingManager, employeeType);
 	        
 	       	      }
 	     m_Statement.close();
 	    }
 	    catch (Exception e)
 	    {
 	      System.err.println("Got an exception! ");
 	      System.err.println(e.getMessage());
 	    }
 		
      
   }

   	private static void sendEmail(String empCode, String empName, String project, Date dateOfJoining, Date dateOfRetirement,
		String reportingManager, String employeeType) {
	
	 final String user="myden";//change accordingly
     final String password="tks123";//change accordingly

  
  
	class Authenticator extends javax.mail.Authenticator 
	 {
	       protected PasswordAuthentication getPasswordAuthentication (){
	           return new PasswordAuthentication (user,password);
	       }
	 }
	
	 String to="hitesh.kandpal@denave.com";
	    
      final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
      Properties props = new Properties();
      props.put("mail.smtp.host", "smtp.denave.com");
      props.put ("mail.smtp.auth", "true");
      props.put ("mail.smtp.port", "25");

     Session session = javax.mail.Session.getInstance(props, new Authenticator());
     try{
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(user));
        message.addRecipient(Message.RecipientType.TO,
                                 new InternetAddress(to));
      MimeBodyPart p1 = new MimeBodyPart();
		String body="<!DOCTYPE html PUBLIC -//W3C//DTD XHTML 1.0 Transitional//EN http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd>\n"+
		"<html xmlns=http://www.w3.org/1999/xhtml>\n"+
		"<head>\n"+
		"<meta http-equiv=Content-Type content=text/html; charset=iso-8859-1 />\n"+
		"<title>Retirement Email</title>\n"+
		"</head>\n"+
		"\n"+
		"<body>\n"+
		"<p> This is to inform you that below mentioned employee will be attaining the age of retirement as per the clause mentioned in the offer letter. \n"+
		"Please get the needful done for timely update and smooth processing"+
		"</p>"+
		"<table width=600 style='border:2px solid #000; align=center cellpadding=1 cellspacing=1' bgcolor=#FFFFFF>\n"+
		"  <tr>\n"+
		"   <td width=30px height=25 align=left valign=middle bgcolor=#00C8FA style= 'padding-left:10px; font:bold 12PX  Arial, Helvetica, sans-serif; color:#000;'>EMP Code </td>\n"+
		"	<td width=30px height=25 align=left valign=middle bgcolor=#00C8FA style= 'padding-left:10px; font:bold 12PX  Arial, Helvetica, sans-serif; color:#000;'>Employee Name </td>\n"+
		"	<td width=48px height=25 align=left valign=middle bgcolor=#00C8FA style= 'padding-left:0px; font:bold 12PX  Arial, Helvetica, sans-serif; color:#000;'>Project Name </td>\n"+
		"	<td width=80px height=25 align=left valign=middle bgcolor=#00C8FA style= 'padding-left:10px; font:bold 12PX  Arial, Helvetica, sans-serif; color:#000;'>Date Of Joining </td>\n"+
		"   <td width=80px height=25 align=left valign=middle bgcolor=#00C8FA style= 'padding-left:10px; font:bold 12PX  Arial, Helvetica, sans-serif; color:#000;'>Date Of Retirement </td>\n"+
		"	<td width=48px height=25 align=left valign=middle bgcolor=#00C8FA style= 'padding-left:10px; font:bold 12PX  Arial, Helvetica, sans-serif; color:#000;'>Reporting Manager </td>\n"+
		"   <td width=48px height=25 align=left valign=middle bgcolor=#00C8FA style= 'padding-left:10px; font:bold 12PX  Arial, Helvetica, sans-serif; color:#000;'>Employee Type</td>\n"+
		"   </tr>\n"+
		"  <tr>\n"+
		"        <td width=30px height=25 align=left valign=middle bgcolor=#ffffff style= 'padding-left:20px; font:normal 12PX  Arial, Helvetica, sans-serif; color:#000;'>"+empCode+"</td>\n"+
		"        <td width=30px height=25 align=left valign=middle bgcolor=#ffffff style= 'padding-left:20px; font:normal 12PX  Arial, Helvetica, sans-serif; color:#000;'>"+empName+"</td>\n"+
		"        <td width=30px height=25 align=left valign=middle bgcolor=#ffffff style= 'padding-left:20px; font:normal 12PX  Arial, Helvetica, sans-serif; color:#000;'>"+project+"</td>\n"+
		"        <td width=80px height=25 align=left valign=middle bgcolor=#ffffff style= 'padding-left:20px; font:normal 12PX  Arial, Helvetica, sans-serif; color:#000;'>"+dateOfJoining+"</td>\n"+
		"        <td width=80px height=25 align=left valign=middle bgcolor=#ffffff style= 'padding-left:20px; font:normal 12PX  Arial, Helvetica, sans-serif; color:#000;'>"+dateOfRetirement+"</td>\n"+
		"        <td width=48px height=25 align=left valign=middle bgcolor=#ffffff style= 'padding-left:20px; font:normal 12PX  Arial, Helvetica, sans-serif; color:#000;'>"+reportingManager+"</td>\n"+
		"        <td width=48px height=25 align=left valign=middle bgcolor=#ffffff style= 'padding-left:20px; font:normal 12PX  Arial, Helvetica, sans-serif; color:#000;'>"+employeeType+"</td>\n"+
		"  </tr>\n"+
		"</table>\n"+
		"</body>\n"+
		"</html>\n";
		p1.setContent(body,"text/html");
		Multipart mp = new MimeMultipart();
	    mp.addBodyPart(p1);
	    message.setContent(mp);

     Transport.send(message);
      
      
        System.out.println("message sent....");
     }catch (MessagingException ex) {ex.printStackTrace();}
  

	// TODO Auto-generated method stub
	
}
}