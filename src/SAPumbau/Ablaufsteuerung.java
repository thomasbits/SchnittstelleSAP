package SAPumbau;

import com.mysql.jdbc.Statement;



public class Ablaufsteuerung extends Thread {

	private KundeWEB kundeWEB; 
	private KundeSAP kundeSAP;
	
	public Ablaufsteuerung() {
		// TODO Auto-generated constructor stub
		kundeWEB = new KundeWEB(this);
		kundeSAP = new KundeSAP(this);
	}
	private VerbindungSAP verbindungSAP = new VerbindungSAP();
	
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
		i = 21;
	}

	public void run()
	{

		while(threadRun)
		{
			
			//SAP Verbindung
			verbindungSAP.connect();
			//Datenbankverbindung aufbauen
			DatenbankVerbindung verbindung = new DatenbankVerbindung();
			//Statement von der Datenbank holen
			java.sql.Statement stmt = verbindung.getStatement();

			for (i=0; i<20; i++)
			{
				//Überprüfen ob neuer Kunde vorhanden
				kundeWEB.setStatement(stmt);


				//kundeWEB.abfrageNeueKunden();
				
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
