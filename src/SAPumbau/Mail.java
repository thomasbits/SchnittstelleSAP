package SAPumbau;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

/** 
 * Diese Klasse sollte dazu verwendet werden eine Email zu versenden wenn ein Kunde sein Account gelöscht hat. Wenn ein Kunde im SAP System gelsöcht werden soll, kann er nur zum löschen vermerkt werden. Da dies durch ein Mitarbeiter durchgeführt werden muss, weil es durch die Schnittstelle nicht Realiserbar ist, sollte eine Email an einen Mitarbeiter versendet werden.
 * Da es im Webshop keine Möglichkeit zum löschen eines Kundenaccount gibt ist diese Klasse stillgelegt.
 * @author Robin & Thomas
 */

public class Mail {

	/**
	 * Die Methode "senden" versendet eine Email
	 */
	public void senden(){
		String msgTitle = "Title";
		String msgBody = "msgBody";
		String userEmail = ""; //Empfängermailadresse
		final String username = "sapservice@felixbe.de"; //Absendermailadresse
		final String password = "";

		Properties prop = new Properties();
		//Die unterschiedlichen Mailserver einstellungen (Verschlüsselung, Ports)
		//		prop.put("mail.smtp.auth", "true");
		//		prop.put("mail.smtp.starttls.enable", "false");
		//		prop.put("mail.smtp.ssl.enable", "false");
		//		prop.put("mail.smtp.host", "");
		//		prop.put("mail.smtp.port", "25");

		Session session = Session.getInstance(prop, new Authenticator(){
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);

			}
		});

		try{
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(""));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
			message.setSubject(msgTitle);
			message.setText(msgBody);

			Transport.send(message);

		} catch (MessagingException ex){
			//Error
		}
	}
}