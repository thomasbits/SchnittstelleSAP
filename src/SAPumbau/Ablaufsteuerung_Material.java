package SAPumbau;

/**
 * Klasse Ablaufsteuerung_Material, implementiert die Schnittstelle Runnable. Steuert die Synchronisierung der Materialdaten zwischen dem SAP-System und der Webshop-DB
 * @author Thomas & Robin
 */
public class Ablaufsteuerung_Material implements Runnable{

	private Report report = new Report(this.getClass().toString());
	private MaterialSAP materialSAP;
	private MaterialWEB materialWEB;
	private boolean threadRun = true;
	private Programmeinstieg programm;

	/**
	 * Konstruktor: erstellet eine Instanz von MateriaSAP und MaterialWEB
	 */
	public Ablaufsteuerung_Material(Programmeinstieg einstieg) {
		materialSAP = new MaterialSAP(this);
		materialWEB = new MaterialWEB(this);
		this.programm = einstieg;
	}

	/**
	 * @return Gibt die Instanz der Klasse MaterialWEB zurück
	 */
	public MaterialWEB getInstanceMaterialWEB()
	{
		return materialWEB;
	}

	/**
	 * @return Gibt die Instanz der Klasse MaterialSAP zurück
	 */
	public MaterialSAP getInstanceMaterialSAP()
	{
		return materialSAP;
	}
	
	/**
	 * Stoppt den Thread. Setzt dazu die Variable threadRun auf false, was die while Schleife in der Runmethode nach dem aktuellen Durchlauf beendet.
	 */
	public void threadStop()
	{
		threadRun = false;
	}

	/**
	 * Runmethode dieser Klasse. Hier werden die notwendigen Schritte bei der Synchronisierung ausgeführt
	 */
	public void run() {
		threadRun = true;

		while(threadRun)
		{
			//15 mal die Materialsynchronisierung durchlaufen, dannach Verbindung neu aufbauen
			for(int i = 0;i<15;i++)
			{
				//Materialliste holen
				materialSAP.materialListeHolen();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					report.set(e.toString());
				}
			}
			//Verbindung zur Datenbank neu aufbauen
			materialWEB.neueVerbindungDB();
			if(!programm.prüferSAPVerbindung())
			{
				System.out.println("Programm beendet!");
				System.exit(1);			//Fehler 1: in die Doku: Keine Verbindung zum SAP-System
			}
		}
	}
}
