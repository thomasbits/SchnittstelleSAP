package SAPumbau;


import com.mysql.jdbc.Statement;

/*
 * Klasse Ablaufsteuerung: Steuert den Ablauf der Synchronisierung. Ruft in bestimmten intervallen die n�tigen Methoden auf
 */


/**
 *	Im Thread der Klasse Ablaufsteuerung wird die Synchronisation gesteuert
 * @author Thomas
 */
public class Ablaufsteuerung implements Runnable {

	private KundeWEB kundeWEB; 
	private KundeSAP kundeSAP;
	private MaterialSAP materialSAP;
	private KundenauftragSAP auftragSAP;
	private KundenauftragWEB auftragWEB;
	private VerbindungSAP verbindungSAP;
	private boolean threadRun = true;
	private	int i = 0;
	private DatenbankVerbindung verbindung;


	public Ablaufsteuerung() {
		// TODO Auto-generated constructor stub
		kundeWEB = new KundeWEB(this);
		kundeSAP = new KundeSAP(this);

		materialSAP = new MaterialSAP();
		verbindungSAP = new VerbindungSAP();
		auftragSAP = new KundenauftragSAP();
		auftragWEB = new KundenauftragWEB(this);
		
	}

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
		threadRun = true;
		while(threadRun)
		{

			//SAP Verbindung
			verbindungSAP.connect();

			//Datenbankverbindung aufbauen
			verbindung = new DatenbankVerbindung();
			//Statement von der Datenbank holen
			java.sql.Statement stmt = verbindung.getStatement();


			//Testelement

			for (i=0; i<10; i++)
			{
//				kundeWEB.setStatement(stmt);
//				kundeWEB.abfrageNeueKunden();
//				kundeSAP.changeKunde(new Kunde());
//				kundeSAP.createKunde(new Kunde());
//				auftragSAP.createKundenauftrag(new Kundenauftrag());
//				auftragSAP.getStatus("5");
//				auftragWEB.setStatement(stmt);
				
				auftragWEB.setStatement(stmt);
				auftragWEB.abfrageNeueBestellungen();
				threadStop();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			 //test

			/*
			for (i=0; i<10; i++)

			{
				
				kundeWEB.setStatement(stmt);

				//�berpr�fen ob neuer Kunde vorhanden
				
				
				kundeWEB.abfrageNeueKunden();

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				kundeWEB.kundenLoeschenDatenbank();
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				kundeWEB.abfrageGe�nderteKunden();
				
				
				
				
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				materialSAP.materialListeHolen();
				
				try {
					Thread.sleep(10000);

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/


		}
		
		verbindung.schliesseVerbindung();
		System.out.println("Sync beendet");
	}
}
