package SAPumbau;

import java.util.*;
import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.*;
import javax.mail.Provider;
import javax.mail.internet.*;
/*
 * Stellt 
 */

/**
 * @author Thomas
 *
 */
public class Mail {


	public void senden(){

		String msgTitle = "Title";
		String msgBody = "msgBody";
		String userEmail = "robin@kljb-alhausen.de";
		final String username = "sapservice@felixbe.de";
		final String password = "";

		Properties prop = new Properties();
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "false");
		//prop.put("mail.smtp.ssl.enable", "false");
		prop.put("mail.smtp.host", "mail.felixbe.de");
		prop.put("mail.smtp.port", "25");

		Session session = Session.getInstance(prop, new Authenticator(){
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try{
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("sapservice@felixbe.de"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
			message.setSubject(msgTitle);
			message.setText(msgBody);

			Transport.send(message);

		} catch (MessagingException ex){
			//Error
			System.out.println(ex);
		}
	}

}
/*
import java.util.*;
import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.*;
import javax.mail.Provider;
import javax.mail.internet.*;

public class Mail {

    public static void senden() {
            final String username="sap5";
            final String password="sap12345";
            Properties prop=new Properties();
            prop.put("mail.smtp.auth", "false");
            prop.put("mail.smtp.host", "smtp.web.de");
            prop.put("mail.smtp.port", "587");
            prop.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getDefaultInstance(prop,
          new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
          }
        });
          try {
                 String body="Dear Renish Khunt Welcome";
                 String htmlBody = "<strong>This is an HTML Message</strong>";
                 String textBody = "This is a Text Message.";
         Message message = new MimeMessage(session);
         message.setFrom(new InternetAddress("sap5@web.de"));
                 message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("robinhake@web.de"));
        message.setSubject("Testing Subject");
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);
            message.setText(htmlBody);
                        message.setContent(textBody, "text/html");
            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

}

 */