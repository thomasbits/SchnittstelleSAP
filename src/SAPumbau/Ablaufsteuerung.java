package SAPumbau;

import com.mysql.jdbc.Statement;



public class Ablaufsteuerung extends Thread {

	public Ablaufsteuerung() {
		// TODO Auto-generated constructor stub
	}
	
	public void run()
	{
		//Datenbankverbindung aufbauen
		DatenbankVerbindung verbindung = new DatenbankVerbindung();
		//Statement von der Datenbank holen
		java.sql.Statement stmt = verbindung.getStatement();
		
		while(true)
		{
		 
		}
		
	}
}
