package SAPumbau;
/**
 * Klasse Ablaufsteuerung_Kunde, implementiert die Schnittstelle Runnable. Steuert die Synchronisierung der Kundendaten zwischen dem SAP-System und der Webshop-DB
 * @author Thomas & Robin
 */
public class Ablaufsteuerung_Kunde implements Runnable{

	private Report report = new Report(this.getClass().toString());
	private KundeWEB kundeWEB;
	private KundeSAP kundeSAP;
	private boolean threadRun = true;

	/**
	 * Konstruktor: erstellet eine Instanz von KundeWEB
	 */
	public Ablaufsteuerung_Kunde() {
		kundeWEB = new KundeWEB(this);
		kundeSAP = new KundeSAP(this);
	}
	/**
	 * 
	 * @return Gibt die Instanz der Klasse KundeWEB zurück
	 */
	public KundeWEB getInstanceKundeWEB()
	{
		return kundeWEB;
	}
	/**
	 * 
	 * @return Gibt die Instanz der Klasse KundeSAP zurück
	 */
	public KundeSAP getInstanceKundeSAP()
	{
		return kundeSAP;
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
	@Override
	public void run() {

		threadRun = true;
		while(threadRun)		//Synchronisiere solange die Variable threadRun ture ist. Notwendig um zu verhinder das die Synchronisierung unvollständig beendet wird.
		{

			for (int i=0; i<15; i++)		//Führe die Schritte zur Synchronisierung 15 mal aus, anschließend baue eine neue Verbindung zur WebDB auf
			{

				//Neuer Kunde
				kundeWEB.abfrageNeueKunden();

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					report.set(e.toString());
				}
				//Kunde löschen
				kundeWEB.kundenLoeschenDatenbank();

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					report.set(e.toString());
				}

				//Kunde ändern
				kundeWEB.abfrageGeänderteKunden();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					report.set(e.toString());
				}
			}
			kundeWEB.neueVerbindungDB();		
		}
	

	}

}
