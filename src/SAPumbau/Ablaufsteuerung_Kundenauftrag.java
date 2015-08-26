package SAPumbau;

public class Ablaufsteuerung_Kundenauftrag implements Runnable {

	private KundenauftragSAP auftragSAP;
	private KundenauftragWEB auftragWEB;
	private boolean threadRun = true;
	private DatenbankVerbindung verbindung;
	
	public Ablaufsteuerung_Kundenauftrag() {
		// TODO Auto-generated constructor stub
		auftragSAP = new KundenauftragSAP(this);
		auftragWEB = new KundenauftragWEB(this);
		
	}

	public KundenauftragSAP getInstanceKundenauftragSAP()
	{
		return auftragSAP;
	}
	
	public KundenauftragWEB getInstanceKundenauftragWEB()
	{
		return auftragWEB;
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
					verbindung = new DatenbankVerbindung();
					//Statement von der Datenbank holen
					java.sql.Statement stmt = verbindung.getStatement();

					for(int i = 0;i<15;i++)
					{
						
						auftragWEB.setStatement(stmt);
						auftragWEB.abfrageNeueBestellungen();
						
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
	}
}
