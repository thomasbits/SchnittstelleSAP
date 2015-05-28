package robin1;

import java.util.Scanner;

import outsort.NeuerKundeSAP;

import com.mysql.jdbc.Statement;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;


public class ProgrammRoutine extends Thread {

	public ProgrammRoutine() {
		// TODO Auto-generated constructor stub
	}
	public int status = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ProgrammRoutine einstieg = new ProgrammRoutine();
		einstieg.start();
	}
	
	public void run()
	{
		VerbindungSAP verbindung = new VerbindungSAP();
		KundenSyncNEU Sync = new KundenSyncNEU();
		
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
			case 4:
				Sync.setSyncFalse();
				durchlauf = false;
				System.out.println("Programm beendet");
				break;
			case 5:
				verbindung.connect();
				break;
			case 6:
				neuerKunde.speichereKunden();
				break;
			case 7:
				DatenbankVerbindung dbconnection = new DatenbankVerbindung();
				break;
			case 8:
				System.out.println("Hallo");
				break;
			case 9:
				//Kundensynchronisierung
				//Vorher Datenbankverbindung aufbauen
				
				
				Sync.start();

				break;
			}

		}while(durchlauf);
	}

}