package SAPumbau;

/**
 * Klasse Ablaufsteuerung_Kundenauftrag, implementiert die Schnittstelle Runnable. Steuert die Synchronisierung der Kundendatenauftragsdaten zwischen dem SAP-System und der Webshop-DB
 * @author Thomas
 */
public class Ablaufsteuerung_Kundenauftrag implements Runnable {

	private Report report = new Report(this.getClass().toString());
	private KundenauftragSAP auftragSAP;
	private KundenauftragWEB auftragWEB;
	private boolean threadRun = true;
	private Programmeinstieg programm;

	/**
	 * Konstruktor: erstellet Instanzen von KundenauftragWEB und KundenauftragSAP
	 */
	public Ablaufsteuerung_Kundenauftrag(Programmeinstieg einstieg) {
		// TODO Auto-generated constructor stub
		auftragSAP = new KundenauftragSAP(this);
		auftragWEB = new KundenauftragWEB(this);
		this.programm = einstieg;
	}

	/**
	 * 
	 * @return Gibt die Instanz der Klasse KundenauftragSAP zurück.
	 */
	public KundenauftragSAP getInstanceKundenauftragSAP()
	{
		return auftragSAP;
	}

	/**
	 * 
	 * @return Gibt die Instanz der Klasse KundenauftragWEB zurück.
	 */
	public KundenauftragWEB getInstanceKundenauftragWEB()
	{
		return auftragWEB;
	}

	/**
	 * Stoppt den Thread. Setzt dazu die Variable threadRun auf false, was die while Schleife in der Runmethode nach dem aktuellen Durchlauf beendet.
	 */
	public void threadStop()
	{
		threadRun = false;
	}

	@Override
	public void run() {

		threadRun = true;

		while(threadRun)			//Synchronisiere solange die Variable threadRun ture ist. Notwendig um zu verhinder das die Synchronisierung unvollständig beendet wird.
		{

			for(int i = 0;i<15;i++)		//Führe die Schritte zur Synchronisierung 15 mal aus, anschließend baue eine neue Verbindung zur WebDB auf
			{

				auftragWEB.abfrageNeueBestellungen();

				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					report.set(e.toString());
				}
			}
			auftragWEB.neueVerbindungDB();
			if(!programm.prüferSAPVerbindung())
			{
				System.out.println("Programm beendet!");
				System.exit(1);			//Fehler 1: in die Doku: Keine Verbindung zum SAP-System
			}
		}
	}
}
