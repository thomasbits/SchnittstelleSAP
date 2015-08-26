package SAPumbau;

public class Ablaufsteuerung_Kunde implements Runnable{
	
	private KundeWEB kundeWEB;
	private KundeSAP kundeSAP;
	private boolean threadRun = true;
	private DatenbankVerbindung verbindung;
	
	public Ablaufsteuerung_Kunde() {
		// TODO Auto-generated constructor stub
		kundeWEB = new KundeWEB();
	}
	
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
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		threadRun = true;
		while(threadRun)
		{
			
			//Datenbankverbindung aufbauen
			verbindung = new DatenbankVerbindung();				//immer eine neue instanz inhalt von konstruktor in methode verschieben und die aufrufen???
			//Statement von der Datenbank holen
			java.sql.Statement stmt = verbindung.getStatement();
			
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
			//Kunde löschen
			kundeWEB.kundenLoeschenDatenbank();
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//Kunde ändern -> Fertig
			kundeWEB.abfrageGeänderteKunden();
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
