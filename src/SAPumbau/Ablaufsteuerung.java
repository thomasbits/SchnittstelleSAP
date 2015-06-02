package SAPumbau;

<<<<<<< HEAD

=======
import com.mysql.jdbc.Statement;
>>>>>>> refs/remotes/origin/master

public class Ablaufsteuerung extends Thread {

	private KundeWEB kundeWEB; 
	private KundeSAP kundeSAP;
	private VerbindungSAP verbindungSAP;

	public Ablaufsteuerung() {
		// TODO Auto-generated constructor stub
		kundeWEB = new KundeWEB(this);
		kundeSAP = new KundeSAP(this);
		verbindungSAP= new VerbindungSAP();
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
			
			//Datenbankverbindung aufbauen
			DatenbankVerbindung verbindung = new DatenbankVerbindung();
			//Statement von der Datenbank holen
			java.sql.Statement stmt = verbindung.getStatement();

			
			
			kundeSAP.changeKunde(new Kunde());
			
			
			
			
			/*
			for (i=0; i<20; i++)
=======
			for (i=0; i<10; i++)
>>>>>>> refs/remotes/origin/master
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
<<<<<<< HEAD
			*/

		}
	}
}
