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
public class Programmeinstieg extends Thread {

	public Programmeinstieg() {
		// TODO Auto-generated constructor stub

		ablaufsteuerung = new Ablaufsteuerung();
	}

	//Status des Programms
	public int status = 0;
	private Ablaufsteuerung ablaufsteuerung;

	//Main Methode
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Programmeinstieg einstieg = new Programmeinstieg();
		einstieg.start();
		
		Logger log = new Logger("Programmstart");
		log.loggerLeeren();
	}

	//Run Methode (Thread)
	public void run()
	{
		boolean durchlauf = true;
		do
		{
			Scanner scanner = new Scanner(System.in);

			System.out.print("--------\n0-Programm gestartet \n1-Synchronisierung starten \n2-Synchronisierung beenden \n4-Programm beenden\n5-Test \nLetzterStatus: " + status + " -Eingabe: ");

			String eingabe = scanner.nextLine();
			try {
				status = Integer.valueOf(eingabe);
			} catch (Exception e) {
				// TODO: handle exception
			}

			switch (status) {

			//Synchronisierung starten
			case 1:
				boolean help = true;
				Thread t = new Thread(ablaufsteuerung);
				while(help)
				{
					if(t.isAlive())
					{
						ablaufsteuerung.threadStop();
					}else
					{
						help = false;
						t.start();
					}
				}


				break;

				//Synchronisierung beenden
			case 2:
				ablaufsteuerung.threadStop();


				break;

				//Synchronisierung beenden
			case 3:
				break;

				//Programm beenden
			case 4:
				ablaufsteuerung.threadStop();
				durchlauf = false;
				break;

				//Testfunktion
			case 5:
				//Mail mail = new Mail();
				//mail.senden();
				//VerbindungSAP verbindungSAP = new VerbindungSAP();
				//verbindungSAP.connect();
				//MaterialSAP material = new MaterialSAP();
				//material.materialListeHolen();

				
				new Logger("test");
				
			
				
				break;
			}
		}while(durchlauf);
	}
}
