package SAPumbau;

import java.util.Scanner;

/**
 *	Programmstart: Startet beim Programmstart alle Threads(Kunde, Auftrag, Material) und nimmt Benutzereingaben entgegen, um die Synchronisierung zu starten, beenden sowie um das Programms zu beenden.
 * @author Thomas & Robin
 */
public class Programmeinstieg{

	private Report report = new Report(this.getClass().toString()); //Report instanziieren (Logger)
	public static int status = 1;	//Status des Programms
	private Ablaufsteuerung_Kunde kunde;
	private Ablaufsteuerung_Kundenauftrag auftrag;
	private Ablaufsteuerung_Material material;
	private Thread t_kunde;
	private Thread t_material;
	private Thread t_auftrag;
	private static VerbindungSAP verbindungSAP;
	private static Programmeinstieg einstieg;

	/**
	 * Konstruktor Programmeinstieg bildes Instanzen der Klassen(Ablaufsteuerung_Kunde, Ablaufsteuerung_Kundenauftrag, Ablaufsteuerung_Material)
	 * Erstellt Threads der Klassen. Erstellt eine Instanz der Klasse (Verbindung SAP)
	 */
	
	//Hallo
	public Programmeinstieg() {

		kunde = new Ablaufsteuerung_Kunde();
		auftrag = new Ablaufsteuerung_Kundenauftrag();
		material = new Ablaufsteuerung_Material();

		//Threads instanziieren
		t_kunde = new  Thread(kunde);
		t_material = new Thread(material);
		t_auftrag = new Thread(auftrag);

		verbindungSAP = new VerbindungSAP();
	}

	/**
	 * Main - Methode: Instanziierung der Klasse Programmeinstieg und ausführen der Methode "synchonisiere".
	 * @param args
	 */
	public static void main(String[] args) {

		einstieg = new Programmeinstieg();	//Programeinstieg instanziieren
		einstieg.synchonisiere();			//Synchronisierung starten (Threads starten)
	}

	/**
	 * Diese Methode steuert die einzelen Threads. Ob diese gestartet oder beendet werden sollen. Diese Entscheidung werden von Benutzereingaben entgegen genommen.
	 */
	public void synchonisiere()
	{
		report.set("Programm wurde gestartet!");	//In den Report/Logger schreiben
		boolean durchlauf = true;
		do
		{
			switch (status) {

			case 1:
				//Synchronisierung starten - wird automatisch gestartet beim Programmstart
				//SAP Verbindung
				verbindungSAP.connect();

				//Abfragen je Thread ob dieser läuft. Wenn nicht wird er gestartet.
				if(t_kunde.isAlive())
				{

				}else{
					t_kunde.start();
					report.set("Thread Kunde gestartet");
				}
				if(t_material.isAlive())
				{

				}else{
					t_material.start();
					report.set("Thread Material gestartet");
				}
				if(t_auftrag.isAlive())
				{

				}else{
					t_auftrag.start();
					report.set("Thread Kundenauftrag gestartet");
				}
				break;

			case 2:
				//Synchronisierung beenden
				verbindungSAP.connect();
				sync_ende();
				break;

			case 3:
				//Programm beenden
				sync_ende();
				System.exit(0);
				report.set("Programm wurde beendet");
				break;

			case 4:
				report.set("Testcase wurde ausgeführt");
				break;
			}

			//Scanner für das auslesen der Eingabe erstellen.
			Scanner scanner = new Scanner(System.in);
			//Möglichkeiten der Auswahl dem Benutzer anzeigen
			System.out.print("--------\n0-Programm gestartet \n1-Synchronisierung starten \n2-Synchronisierung beenden \n3-Programm beenden\n4-Test \nLetzterStatus: " + status + "\nEingabe: ");
			//Nächste Zeile auslesen
			String eingabe = scanner.nextLine();
			try {
				status = Integer.valueOf(eingabe);
			} catch (Exception e) {
				report.set(e.toString());
			}
		}while(durchlauf);	//Das Programm läuft so lange bis der Nutzer es beendet. Der Benutzer kann jederzeit Eingaben tätigen.
	}

	/**
	 * Diese Methode ruft die "threadStop" Methoden der einzelnen Threads auf um diese zu beenden.
	 * Wird z.B. zum Programmende oder zum stoppen der Synchronisieung benötigt.
	 */
	private void sync_ende()
	{
		auftrag.threadStop();
		kunde.threadStop();
		material.threadStop();
	}
}
