package SAPumbau;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter; 
import java.io.BufferedWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	
	PrintWriter pWriter = null;
	DateFormat df;
	public Logger(String text) {
		// TODO Auto-generated constructor stub
		
		//Aktuelle Zeit auslesen.
		SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy.MM.dd - HH:mm:ss ");
        Date currentTime = new Date();
		
		
		
	        try {
	            pWriter = new PrintWriter(new BufferedWriter(new FileWriter("Log_SAP_Schnittstelle.txt",true)));
	            pWriter.println(formatter.format(currentTime) + ": " + text);
	        } catch (IOException ioe) {
	            ioe.printStackTrace();
	        } finally {
	            if (pWriter != null){
	                pWriter.flush();
	                pWriter.close();
	            }
	        }
	}
	
	
	
	public void loggerLeeren()
	{
		
		//Aktuelle Zeit auslesen.
				SimpleDateFormat formatter = new SimpleDateFormat(
		                "yyyy.MM.dd - HH:mm:ss ");
		        Date currentTime = new Date();
		
		
	    try {
            pWriter = new PrintWriter(new BufferedWriter(new FileWriter("Log_SAP_Schnittstelle.txt",false)));
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
