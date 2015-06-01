package SAPumbau;

import com.mysql.jdbc.Statement;



public class Ablaufsteuerung extends Thread {

	public Ablaufsteuerung() {
		// TODO Auto-generated constructor stub
	}

	private KundeWEB kundeWEB = new KundeWEB(this);
	private KundeSAP kundeSAP = new KundeSAP(this);
	private boolean threadRun = true;
	private	int i = 0;
	
	public KundeWEB getInstanceKundeWEB()
	{
		return kundeWEB;
	}

	public KundeSAP getInstanceKundeSAP()
	{
		return kundeSAP;
	}
	
	public void threadStop()
	{
		threadRun = false;
		i = 19;
	}

	public void run()
	{

		while(threadRun)
		{
			//Datenbankverbindung aufbauen
			DatenbankVerbindung verbindung = new DatenbankVerbindung();
			//Statement von der Datenbank holen
			java.sql.Statement stmt = verbindung.getStatement();

			for (i=0; i<20; i++)
			{
				//Überprüfen ob neuer Kunde vorhanden
				kundeWEB.setStatement(stmt);


				kundeWEB.abfrageNeueKunden();
				
				kundeWEB.kundenLoeschenDatenbank();
				
				try {
					sleep(4000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
		}


	}
}
