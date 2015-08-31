package SAPumbau;

public class Ablaufsteuerung_Kundenauftrag implements Runnable {

	private KundenauftragSAP auftragSAP;
	private KundenauftragWEB auftragWEB;
	private boolean threadRun = true;
	private DatenbankVerbindung verbindung;
	private java.sql.Statement stmt;
	
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
					System.out.println("Thread Auftrag l�uft!!!!");
					//Datenbankverbindung aufbauen
//					verbindung = new DatenbankVerbindung();
//					//Statement von der Datenbank holen
//					java.sql.Statement stmt = verbindung.getStatement();

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