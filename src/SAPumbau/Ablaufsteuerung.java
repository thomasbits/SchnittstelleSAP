package SAPumbau;

import com.mysql.jdbc.Statement;



public class Ablaufsteuerung extends Thread {

	public Ablaufsteuerung() {
		// TODO Auto-generated constructor stub
	}
	
	private KundeWEB kundeWEB = new KundeWEB(this);
	private KundeSAP kundeSAP = new KundeSAP(this);
	
	public KundeWEB getInstanceKundeWEB()
	{
		return kundeWEB;
	}
	
	public KundeSAP getInstanceKundeSAP()
	{
		return kundeSAP;
	}
	
	public void run()
	{
		//Datenbankverbindung aufbauen
		DatenbankVerbindung verbindung = new DatenbankVerbindung();
		//Statement von der Datenbank holen
		java.sql.Statement stmt = verbindung.getStatement();
		
		//Überprüfen ob neuer Kunde vorhanden
		kundeWEB.setStatement(stmt);
		kundeWEB.abfrageNeueKunden();
		if(kunde1 != null)
		{
			//Kunde in das SAP System schreiben
		}
		
		
		
	}
}
