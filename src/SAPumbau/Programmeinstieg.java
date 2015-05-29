package SAPumbau;

import java.util.Scanner;
import com.mysql.jdbc.Statement;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;

/*
 * Klasse Programm
 */

public class Programmeinstieg extends Thread {

	public Programmeinstieg() {
		// TODO Auto-generated constructor stub
	}
	//Status des Programms
	public int status = 0;
	
	//Main Methode
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Programmeinstieg einstieg = new Programmeinstieg();
		einstieg.start();
	}
	
	public void run()
	{
		VerbindungSAP verbindung = new VerbindungSAP();
		KundenSyncNEU SyncKundeNeu = new KundenSyncNEU();
		KundenSyncLOE SyncKundeLoeschen = new KundenSyncLOE();
		boolean durchlauf = true;
		do
		{
			Scanner scanner = new Scanner(System.in);

			System.out.print("--------\n0-Programm gestartet \n1-Synchronisierung starten \n2-Synchronisierung beenden \n4-Programm beenden\n5-Verbindungaufbauen \n6-Kundeanlegen \n7-Datenbankverbindung \n8-Funktion testen\nLetzterStatus: " + status + " -Eingabe: ");

			String eingabe = scanner.nextLine();
			try {
				status = Integer.valueOf(eingabe);
			} catch (Exception e) {
				// TODO: handle exception
			}

			switch (status) {
			//Synchronisierung starten
			case 1:
				SyncKundeNeu.start();
				break;
			//Synchronisierung beenden
			case 2:
				break;
			//
			case 3:
				break;
			//Programm beenden
			case 4:
				SyncKundeNeu.setSyncFalse();
				durchlauf = false;
				System.out.println("Programm beendet");
				break;
			}
		}while(durchlauf);
	}
}
