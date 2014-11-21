package nano.util;

import java.io.UnsupportedEncodingException;
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
import javax.mail.internet.MimeUtility;
import nano.*;

 /**
 * @description : 메일 전송 class 
 *
 *  1. 화일 이름  :    MailUtil.java
 *  2. 작성자     :    정수현
 *  3. 프로그램 변경 내역
 *      1) 2013. 11. 01 / 최초 작성
 *
 *  추가설명 :
 *
 *  </pre>
 */
public class MailUtil {

	/**
	 * 가격 책정시
	 */
	public void setCompanyEmail(String[] email_list, Record rec, Properties props){
        try {
        	String[] emailList = email_list;// 메일 보낼사람 리스트 
        	String emailFromAddress = "glory@nanodms.co.kr";// 메일 보내는 사람
        	String fromName = "BizTK";// 메일 보내는 명
            
        	// 내용
            //String emailMsgTxt = props.getProperty("product_detail","").toString();
        	String emailMsgTxt = rec.get("email_content", "") + "<br />사이즈 : " + rec.get("size_list", "") + "<br /></br />"  
        			+ "<a href='" +  props.getProperty("link_url","").toString() + "' _target='blank'>"
        			+ "<img src='" + props.getProperty("email_image_name","").toString() + "' style='width:100%;height:auto;'>"
        			+ "</a>";
        System.out.println(emailMsgTxt);	
            String emailSubjectTxt = props.getProperty("product_title").toString();// 제목
             
            // 메일보내기 
            postGMail(emailList, emailSubjectTxt, emailMsgTxt, emailFromAddress, fromName);
            //System.out.println("모든 사용자에게 메일이 성공적으로 보내졌음~~");
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }	
	/**
	 * 비번 변경시
	 */
	public void temporaryPW(Properties props){
        try {
        	String[] emailList = { props.getProperty("find_email").toString() };// 메일 보낼사람 리스트 
        	String emailFromAddress = "contactus@kbgkorea.com";// 메일 보내는 사람
        	String fromName = "KBGKorea";// 메일 보내는 명
            
        	// 내용
            String emailMsgTxt = "Dear "+props.getProperty("user_firstname").toString()+" "+props.getProperty("user_lastname").toString()+"<br/><br/>"	
            		+"We have reset your password. Your new password is"+"<br/><br/>"	
    				+"Your new password is: " + props.getProperty("temporary_pw")+"<br/><br/>"
    				+"You can sign in here <a href=\"http://www.kbgkorea.com/jsp/user/aboutus/mypages/user_login.jsp\" target=\"_blank\">www.kbgkorea.com</a>"+"<br/><br/>"
			        +"Kind Regards"+"<br/><br/>"
			        +"Customer Service Team"+"<br/><br/><br/>"
			        
			        //서명
			        +"<table border=\"0\" cellpadding=\"0\" style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; line-height: normal; width: 440pt;\" width=\"440\">"
			        +"<tbody>"
			        +"<tr>"
			        +"<td style=\"margin: 0px; width: 97.5pt; border-style: none solid none none; border-right-color: rgb(62, 72, 201); border-right-width: 1pt; padding: 0.75pt 6pt 0.75pt 0.75pt;\" valign=\"top\" width=\"130\">"
			        +"<p style=\"margin-bottom: 0.0001pt;\"><span lang=\"EN-US\" style=\"font-size: 9pt; font-family: Arial, sans-serif;\"><img src=\"https://s3.amazonaws.com/uploads.wisestamp.com/2a05b9dde59f427410469acbbe80a96b/1385084629.png?chaching=none\" /></span></p>"
			        +"</td>"
			        +"<td style=\"margin: 0px; width: 215.25pt; padding: 0.75pt 0.75pt 0.75pt 4.5pt;\" valign=\"top\" width=\"287\">"
			        +"<p style=\"margin-bottom: 0.0001pt;\"><b><span lang=\"EN-US\" style=\"font-size: 9pt; font-family: Arial, sans-serif; color: rgb(62, 72, 201); border: 1pt none windowtext; padding: 0cm;\">Pirya Budhani</span></b><br />"
			        +"<span lang=\"EN-US\" style=\"font-size: 10pt; font-family: Arial, sans-serif; color: rgb(100, 100, 100); border: 1pt none windowtext; padding: 0cm;\">KBGKorea</span></p>"
			        +"<p style=\"margin-bottom: 0.0001pt;\"><span lang=\"EN-US\" style=\"font-size: 8.5pt; font-family: Arial, sans-serif; color: rgb(141, 141, 141); border: 1pt none windowtext; padding: 0cm;\">t:</span><span lang=\"EN-US\" style=\"font-size: 8.5pt; font-family: Arial, sans-serif; color: rgb(141, 141, 141);\">&nbsp;82.2.465.5974&nbsp;<span style=\"border: 1pt none windowtext; padding: 0cm;\">|&nbsp;</span></span><span lang=\"EN-US\" style=\"font-size: 8.5pt; font-family: Arial, sans-serif; color: rgb(62, 72, 201); border: 1pt none windowtext; padding: 0cm;\">m:</span><span lang=\"EN-US\" style=\"font-size: 8.5pt; font-family: Arial, sans-serif; color: rgb(141, 141, 141); border: 1pt none windowtext; padding: 0cm;\">&nbsp;82.10.5778.2509</span><span lang=\"EN-US\" style=\"font-size: 8.5pt; font-family: Arial, sans-serif; color: rgb(141, 141, 141);\">&nbsp;<span style=\"border: 1pt none windowtext; padding: 0cm;\">|</span></span><br />"
			        +"<span lang=\"EN-US\" style=\"font-size: 8.5pt; font-family: Arial, sans-serif; color: rgb(62, 72, 201); border: 1pt none windowtext; padding: 0cm;\">e:</span><span lang=\"EN-US\" style=\"font-size: 8.5pt; font-family: Arial, sans-serif; color: rgb(141, 141, 141); border: 1pt none windowtext; padding: 0cm;\"><a href=\"mailto:pdbudhani@kbgkorea.com\" target=\"_blank\">pdbudhani@kbgkorea.com</a>&nbsp;|&nbsp;</span><span lang=\"EN-US\" style=\"font-size: 8.5pt; font-family: Arial, sans-serif; color: rgb(62, 72, 201); border: 1pt none windowtext; padding: 0cm;\">w:</span><span lang=\"EN-US\" style=\"font-size: 8.5pt; font-family: Arial, sans-serif; color: rgb(141, 141, 141); border: 1pt none windowtext; padding: 0cm;\">&nbsp;<a href=\"http://www.kbgkorea.com/\" target=\"_blank\">www.kbgkorea.com</a></span></p>"
			        +"</td>"
			        +"</tr>"
			        +"</tbody>"
			        +"</table>";
            
		    String emailSubjectTxt = "Your new KBGKorea password";// 제목
             
            // 메일보내기 
            postGMail(emailList, emailSubjectTxt, emailMsgTxt, emailFromAddress, fromName);
            //System.out.println("모든 사용자에게 메일이 성공적으로 보내졌음~~");
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * 가격 책정시
	 */
	public void setPricing(Properties props, String product_num){
        try {
        	String[] emailList = { props.getProperty("user_email").toString() };// 메일 보낼사람 리스트 
        	String emailFromAddress = "contactus@kbgkorea.com";// 메일 보내는 사람
        	String fromName = "KBGKorea";// 메일 보내는 명
            
        	// 내용
            String emailMsgTxt = "Dear "+props.getProperty("user_firstname","").toString()+" "+props.getProperty("user_lastname","").toString()+"<br/><br/>"
    				+props.getProperty("product_content").toString().replace("\n", "<br/>")+"<br/><br/>"
    				+"US$"+props.getProperty("product_price").toString()+"<br/>";
            
            if(props.getProperty("menu_name").equals("subscription")){
            	emailMsgTxt +="Terms : "+props.getProperty("product_month","").toString()+" months"+"<br/><br/>";
	    	}
            
            emailMsgTxt +=props.getProperty("mail_greetings","").toString()+"<br/><br/>"
    				+props.getProperty("mail_sender","").toString()+"<br/><br/>"
    				+"<a href=\"http://www.kbgkorea.com/jsp/user/purchase/purchase_cart.jsp\" target=\"_blank\">Purchase Subscription</a><br/><br/><br/>"
            
		          	//서명
			        +"<table border=\"0\" cellpadding=\"0\" style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; line-height: normal; width: 440pt;\" width=\"440\">"
			        +"<tbody>"
			        +"<tr>"
			        +"<td style=\"margin: 0px; width: 97.5pt; border-style: none solid none none; border-right-color: rgb(62, 72, 201); border-right-width: 1pt; padding: 0.75pt 6pt 0.75pt 0.75pt;\" valign=\"top\" width=\"130\">"
			        +"<p style=\"margin-bottom: 0.0001pt;\"><span lang=\"EN-US\" style=\"font-size: 9pt; font-family: Arial, sans-serif;\"><img src=\"https://s3.amazonaws.com/uploads.wisestamp.com/2a05b9dde59f427410469acbbe80a96b/1385084629.png?chaching=none\" /></span></p>"
			        +"</td>"
			        +"<td style=\"margin: 0px; width: 215.25pt; padding: 0.75pt 0.75pt 0.75pt 4.5pt;\" valign=\"top\" width=\"287\">"
			        +"<p style=\"margin-bottom: 0.0001pt;\"><b><span lang=\"EN-US\" style=\"font-size: 9pt; font-family: Arial, sans-serif; color: rgb(62, 72, 201); border: 1pt none windowtext; padding: 0cm;\">Pirya Budhani</span></b><br />"
			        +"<span lang=\"EN-US\" style=\"font-size: 10pt; font-family: Arial, sans-serif; color: rgb(100, 100, 100); border: 1pt none windowtext; padding: 0cm;\">KBGKorea</span></p>"
			        +"<p style=\"margin-bottom: 0.0001pt;\"><span lang=\"EN-US\" style=\"font-size: 8.5pt; font-family: Arial, sans-serif; color: rgb(141, 141, 141); border: 1pt none windowtext; padding: 0cm;\">t:</span><span lang=\"EN-US\" style=\"font-size: 8.5pt; font-family: Arial, sans-serif; color: rgb(141, 141, 141);\">&nbsp;82.2.465.5974&nbsp;<span style=\"border: 1pt none windowtext; padding: 0cm;\">|&nbsp;</span></span><span lang=\"EN-US\" style=\"font-size: 8.5pt; font-family: Arial, sans-serif; color: rgb(62, 72, 201); border: 1pt none windowtext; padding: 0cm;\">m:</span><span lang=\"EN-US\" style=\"font-size: 8.5pt; font-family: Arial, sans-serif; color: rgb(141, 141, 141); border: 1pt none windowtext; padding: 0cm;\">&nbsp;82.10.5778.2509</span><span lang=\"EN-US\" style=\"font-size: 8.5pt; font-family: Arial, sans-serif; color: rgb(141, 141, 141);\">&nbsp;<span style=\"border: 1pt none windowtext; padding: 0cm;\">|</span></span><br />"
			        +"<span lang=\"EN-US\" style=\"font-size: 8.5pt; font-family: Arial, sans-serif; color: rgb(62, 72, 201); border: 1pt none windowtext; padding: 0cm;\">e:</span><span lang=\"EN-US\" style=\"font-size: 8.5pt; font-family: Arial, sans-serif; color: rgb(141, 141, 141); border: 1pt none windowtext; padding: 0cm;\"><a href=\"mailto:pdbudhani@kbgkorea.com\" target=\"_blank\">pdbudhani@kbgkorea.com</a>&nbsp;|&nbsp;</span><span lang=\"EN-US\" style=\"font-size: 8.5pt; font-family: Arial, sans-serif; color: rgb(62, 72, 201); border: 1pt none windowtext; padding: 0cm;\">w:</span><span lang=\"EN-US\" style=\"font-size: 8.5pt; font-family: Arial, sans-serif; color: rgb(141, 141, 141); border: 1pt none windowtext; padding: 0cm;\">&nbsp;<a href=\"http://www.kbgkorea.com/\" target=\"_blank\">www.kbgkorea.com</a></span></p>"
			        +"</td>"
			        +"</tr>"
			        +"</tbody>"
			        +"</table>";
            
            
            String emailSubjectTxt = "KBGKorea - "+props.getProperty("product_title").toString();// 제목
             
            // 메일보내기 
            postGMail(emailList, emailSubjectTxt, emailMsgTxt, emailFromAddress, fromName);
            //System.out.println("모든 사용자에게 메일이 성공적으로 보내졌음~~");
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
	
	
	/**
	 * 답변 시
	 */
	public void replySend(Properties props){
        try {
        	String[] emailList = { props.getProperty("user_email").toString() };// 메일 보낼사람 리스트 
        	String emailFromAddress = "contactus@kbgkorea.com";// 메일 보내는 사람
        	String fromName = "KBGKorea";// 메일 보내는 명
            
        	// 내용
            String emailMsgTxt = "Dear "+props.getProperty("user_firstname","").toString()+" "+props.getProperty("user_lastname","").toString()+"<br/><br/>"
    				+props.getProperty("reply_content","").toString().replace("\n", "<br/>")+"<br/><br/>"
    				+props.getProperty("reply_writer","").toString()+"<br/><br/><br/>"
            
		          	//서명
			        +"<table border=\"0\" cellpadding=\"0\" style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; line-height: normal; width: 440pt;\" width=\"440\">"
			        +"<tbody>"
			        +"<tr>"
			        +"<td style=\"margin: 0px; width: 97.5pt; border-style: none solid none none; border-right-color: rgb(62, 72, 201); border-right-width: 1pt; padding: 0.75pt 6pt 0.75pt 0.75pt;\" valign=\"top\" width=\"130\">"
			        +"<p style=\"margin-bottom: 0.0001pt;\"><span lang=\"EN-US\" style=\"font-size: 9pt; font-family: Arial, sans-serif;\"><img src=\"https://s3.amazonaws.com/uploads.wisestamp.com/2a05b9dde59f427410469acbbe80a96b/1385084629.png?chaching=none\" /></span></p>"
			        +"</td>"
			        +"<td style=\"margin: 0px; width: 215.25pt; padding: 0.75pt 0.75pt 0.75pt 4.5pt;\" valign=\"top\" width=\"287\">"
			        +"<p style=\"margin-bottom: 0.0001pt;\"><b><span lang=\"EN-US\" style=\"font-size: 9pt; font-family: Arial, sans-serif; color: rgb(62, 72, 201); border: 1pt none windowtext; padding: 0cm;\">Pirya Budhani</span></b><br />"
			        +"<span lang=\"EN-US\" style=\"font-size: 10pt; font-family: Arial, sans-serif; color: rgb(100, 100, 100); border: 1pt none windowtext; padding: 0cm;\">KBGKorea</span></p>"
			        +"<p style=\"margin-bottom: 0.0001pt;\"><span lang=\"EN-US\" style=\"font-size: 8.5pt; font-family: Arial, sans-serif; color: rgb(141, 141, 141); border: 1pt none windowtext; padding: 0cm;\">t:</span><span lang=\"EN-US\" style=\"font-size: 8.5pt; font-family: Arial, sans-serif; color: rgb(141, 141, 141);\">&nbsp;82.2.465.5974&nbsp;<span style=\"border: 1pt none windowtext; padding: 0cm;\">|&nbsp;</span></span><span lang=\"EN-US\" style=\"font-size: 8.5pt; font-family: Arial, sans-serif; color: rgb(62, 72, 201); border: 1pt none windowtext; padding: 0cm;\">m:</span><span lang=\"EN-US\" style=\"font-size: 8.5pt; font-family: Arial, sans-serif; color: rgb(141, 141, 141); border: 1pt none windowtext; padding: 0cm;\">&nbsp;82.10.5778.2509</span><span lang=\"EN-US\" style=\"font-size: 8.5pt; font-family: Arial, sans-serif; color: rgb(141, 141, 141);\">&nbsp;<span style=\"border: 1pt none windowtext; padding: 0cm;\">|</span></span><br />"
			        +"<span lang=\"EN-US\" style=\"font-size: 8.5pt; font-family: Arial, sans-serif; color: rgb(62, 72, 201); border: 1pt none windowtext; padding: 0cm;\">e:</span><span lang=\"EN-US\" style=\"font-size: 8.5pt; font-family: Arial, sans-serif; color: rgb(141, 141, 141); border: 1pt none windowtext; padding: 0cm;\"><a href=\"mailto:pdbudhani@kbgkorea.com\" target=\"_blank\">pdbudhani@kbgkorea.com</a>&nbsp;|&nbsp;</span><span lang=\"EN-US\" style=\"font-size: 8.5pt; font-family: Arial, sans-serif; color: rgb(62, 72, 201); border: 1pt none windowtext; padding: 0cm;\">w:</span><span lang=\"EN-US\" style=\"font-size: 8.5pt; font-family: Arial, sans-serif; color: rgb(141, 141, 141); border: 1pt none windowtext; padding: 0cm;\">&nbsp;<a href=\"http://www.kbgkorea.com/\" target=\"_blank\">www.kbgkorea.com</a></span></p>"
			        +"</td>"
			        +"</tr>"
			        +"</tbody>"
			        +"</table>";
		    
            String emailSubjectTxt = "KBGKorea - Reply";// 제목
             
            // 메일보내기 
            postGMail(emailList, emailSubjectTxt, emailMsgTxt, emailFromAddress, fromName);
            //System.out.println("모든 사용자에게 메일이 성공적으로 보내졌음~~");
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
	
	
	
	//구글 메일 서버 이용
    private void postGMail(String recipients[], String subject, String message, String from, String fromName) throws MessagingException, UnsupportedEncodingException {
        boolean debug = false;
        java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
  
        String SMTP_HOST_NAME = "gmail-smtp.l.google.com";
         
        // Properties 설정
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
  
        Authenticator auth = new SMTPAuthenticator();
        Session session = Session.getInstance(props, auth);
  
        session.setDebug(debug);
  
        // create a message
        Message msg = new MimeMessage(session);
        Multipart multipart = new MimeMultipart();
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(message,"text/html;charset=UTF-8");// html형식
        multipart.addBodyPart(messageBodyPart);
        // set the from and to address
        //InternetAddress addressFrom = new InternetAddress(from);
        //msg.setFrom(addressFrom);
        msg.setFrom(new InternetAddress(from, MimeUtility.encodeText(fromName,"UTF-8","B")));//보내는 사람 설정
        
        InternetAddress[] addressTo = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++) {
            addressTo[i] = new InternetAddress(recipients[i]);
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);
  
        // Setting the Subject and Content Type
        msg.setSubject(subject);
        msg.setContent(multipart);
        Transport.send(msg);
    }
  
    
    
    /**
     * 구글 사용자 메일 계정 아이디/패스 정보
     */
    private class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            String username =  "nanodms2014@gmail.com"; // gmail 사용자;
            String password = "nano5858"; // 패스워드;
            return new PasswordAuthentication(username, password);
        }
    }
    
		
}

