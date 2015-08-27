package SAPumbau;

import java.util.Scanner;

import com.mysql.jdbc.Statement;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;

/*
 * Klasse Programmeinstieg; nimmt die Benutzereingaben entgegen und soll dem Benutzer verscheidene Konfigurationsmöglichkeiten bieten.
 */

/**
 *	Programmstart: Startet den Thread dieser Klasse, in dem dem Benutzer verschiedene Optionen zur Steuerung des Programms geboten werden.
 * @author Thomas
 */
public class Programmeinstieg{
	
	//Status des Programms
	private Report report = new Report(this.getClass().toString());
	public static int status = 0;
//	private Ablaufsteuerung ablaufsteuerung;			//Kann weg
	private Ablaufsteuerung_Kunde kunde;
	private Ablaufsteuerung_Kundenauftrag auftrag;
	private Ablaufsteuerung_Material material;
	private Thread t_kunde;
	private Thread t_material;
	private Thread t_auftrag;
	private static VerbindungSAP verbindungSAP;
	private static Programmeinstieg einstieg;
	
	public Programmeinstieg() {
		// TODO Auto-generated constructor stub

		kunde = new Ablaufsteuerung_Kunde();
		auftrag = new Ablaufsteuerung_Kundenauftrag();
		material = new Ablaufsteuerung_Material();
		t_kunde = new  Thread(kunde);
		t_material = new Thread(material);
		t_auftrag = new Thread(auftrag);
		verbindungSAP = new VerbindungSAP();

		
	}

	//Main Methode
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		einstieg = new Programmeinstieg();
	
		einstieg.synchonisiere();
	}

	//Run Methode (Thread)
	public void synchonisiere()
	{
		report.setEmpty();
		report.set("Programm wurde gestartet!");
		boolean durchlauf = true;
		do
		{

			switch (status) {

			//Synchronisierung starten
			case 1:
				//SAP Verbindung
				verbindungSAP.connect();
				
				if(t_kunde.isAlive())
				{
					System.out.println("Thread Kunde läuft");
				}else{
					t_kunde.start();
					System.out.println("Thread Kunde gestartet");
				}
//				if(t_material.isAlive())
//				{
//
//				}else{
//					t_material.start();
//				}
				if(t_auftrag.isAlive())
				{

				}else{
					t_auftrag.start();
				}



				break;

				//Synchronisierung beenden
			case 2:
				//SAP Verbindung
				verbindungSAP.connect();
				sync_ende();
				
				break;

				//Programm beenden
			case 3:
				sync_ende();
				System.exit(0);

				break;

				
			case 4:
				DatenbankVerbindung verbindung = new DatenbankVerbindung();

				break;
			}

			Scanner scanner = new Scanner(System.in);

			System.out.print("--------\n0-Programm gestartet \n1-Synchronisierung starten \n2-Synchronisierung beenden \n3-Programm beenden\n4-Test \nLetzterStatus: " + status + "\nEingabe: ");

			String eingabe = scanner.nextLine();
			try {
				status = Integer.valueOf(eingabe);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}while(durchlauf);
	}
	
	private void sync_ende()
	{
		auftrag.threadStop();
		kunde.threadStop();
		material.threadStop();
	}
}
