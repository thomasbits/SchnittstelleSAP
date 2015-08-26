package SAPumbau;

public class Ablaufsteuerung_Material implements Runnable{

	private MaterialSAP materialSAP;
	private MaterialWEB materialWEB;
	private boolean threadRun = true;
	private DatenbankVerbindung verbindung;

	public Ablaufsteuerung_Material() {
		// TODO Auto-generated constructor stub
		materialSAP = new MaterialSAP(this);
		materialWEB = new MaterialWEB(this);
	}

	public MaterialWEB getInstanceMaterialWEB()
	{
		return materialWEB;
	}

	public MaterialSAP getInstanceMaterialSAP()
	{
		return materialSAP;
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
				//Material Statement setzten für Datenbankverbindungen
				materialWEB.setStatement(stmt);
				//Materialliste holen
				materialSAP.materialListeHolen();
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
