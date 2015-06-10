package SAPumbau;


import com.mysql.jdbc.Statement;

/*
 * 
 */
public class Ablaufsteuerung extends Thread {

	private KundeWEB kundeWEB; 
	private KundeSAP kundeSAP;
	private KundenauftragSAP auftragSAP;
	private KundenauftragWEB auftragWEB;
	private VerbindungSAP verbindungSAP;

	public Ablaufsteuerung() {
		// TODO Auto-generated constructor stub
		kundeWEB = new KundeWEB(this);
		kundeSAP = new KundeSAP(this);
		verbindungSAP = new VerbindungSAP();
		auftragSAP = new KundenauftragSAP();
		auftragWEB = new KundenauftragWEB(this);
	}

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
	
	public KundenauftragSAP getInstanceKundenauftragSAP()
	{
		return auftragSAP;
	}

	public void threadStop()
	{
		threadRun = false;
		i = 21;
	}

	public void run()
	{
		//SAP Verbindung
		verbindungSAP.connect();
		while(threadRun)
		{
			
			//SAP Verbindung
			verbindungSAP.connect();
			
			//Datenbankverbindung aufbauen
			DatenbankVerbindung verbindung = new DatenbankVerbindung();
			//Statement von der Datenbank holen
			java.sql.Statement stmt = verbindung.getStatement();
			
			
			
			
			
			
			for (i=0; i<10; i++)
			{
//				kundeSAP.changeKunde(new Kunde());
				auftragSAP.createKundenauftrag(new Kundenauftrag());
//				auftragWEB.setStatement(stmt);
//				auftragWEB.abfrageNeueBestellungen();
				threadStop();
				try {
					sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			/*


			for (i=0; i<10; i++)

			{
				//Überprüfen ob neuer Kunde vorhanden
				kundeWEB.setStatement(stmt);


				kundeWEB.abfrageNeueKunden();
				
				try {
					sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				kundeWEB.kundenLoeschenDatenbank();

				try {
					sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			*/

		}
	}
}
