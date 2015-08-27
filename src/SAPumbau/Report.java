package SAPumbau;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter; 
import java.io.BufferedWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Logger Klasse
 * Sie stellt eine LogDatei anbindung zur Verfügung, in die jederzeit über den Konstruktor ein String übergeben werden kann.
 * Dieser String wird über den Konstruktor mit Datum und Uhrzeit in die Logdatei geschrieben.
 * @author Robin
 *
 */
public class Report {

	PrintWriter pWriter = null;
	DateFormat df;
	String klasse;
	public Report(String klasse)
	{
		this.klasse = klasse;
	}
	
	public void set(String text) {
		// TODO Auto-generated constructor stub

		//Aktuelle Zeit auslesen.
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy.MM.dd - HH:mm:ss ");
		Date currentTime = new Date();

		try {
			pWriter = new PrintWriter(new BufferedWriter(new FileWriter("Log_SAP_Schnittstelle.txt",true)));
			pWriter.println(formatter.format(currentTime) + "("+ klasse +"): " + text);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (pWriter != null){
				pWriter.flush();
				pWriter.close();
			}
		}
	}


	/**
	 * Logdatei wird geleert.
	 * Zu beginn des Programms muss die Logdatei einmal geleert werden.
	 */
	public void setEmpty()
	{
		//Aktuelle Zeit auslesen.
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy.MM.dd - HH:mm:ss ");
		Date currentTime = new Date();

		try {
			pWriter = new PrintWriter(new BufferedWriter(new FileWriter("LOG_SAP_Schnittstelle.txt",false)));
			pWriter.println("Programmstart(" +formatter.format(currentTime) + "): ");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (pWriter != null){
				pWriter.flush();
				pWriter.close();
			}
		}
	}
}
