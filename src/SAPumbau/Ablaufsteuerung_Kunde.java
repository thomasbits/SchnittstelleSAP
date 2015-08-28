package SAPumbau;

import com.mysql.jdbc.Statement;

public class Ablaufsteuerung_Kunde implements Runnable{
	
	private KundeWEB kundeWEB;
	private KundeSAP kundeSAP;
	private boolean threadRun = true;
	private DatenbankVerbindung verbindung;
	private java.sql.Statement stmt;
	
	public Ablaufsteuerung_Kunde() {
		// TODO Auto-generated constructor stub
		kundeWEB = new KundeWEB(this);
	}
	
	public KundeWEB getInstanceKundeWEB()
	{
		return kundeWEB;
	}

	public KundeSAP getInstanceKundeSAP()
	{
		return kundeSAP;
	}

	public void setStatement(java.sql.Statement stmt2)
	{
		this.stmt = stmt2;
	}
	
	public void threadStop()
	{
		threadRun = false;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		threadRun = true;
		while(threadRun)
		{
			System.out.println("Thread Kunde l�uft!!!!");
			//Datenbankverbindung aufbauen
//			verbindung = new DatenbankVerbindung();	
//			//Statement von der Datenbank holen
//			java.sql.Statement stmt = verbindung.getStatement();
			
		for (int i=0; i<15; i++)
		{
			
			//Neuer Kunde -> Fertig
			kundeWEB.setStatement(stmt);
			kundeWEB.abfrageNeueKunden();

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Kunde l�schen
			kundeWEB.kundenLoeschenDatenbank();
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//Kunde �ndern -> Fertig
			kundeWEB.abfrageGe�nderteKunden();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
		verbindung.schliesseVerbindung();
		
	}

}
