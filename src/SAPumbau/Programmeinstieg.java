package SAPumbau;

import java.util.Scanner;

import com.mysql.jdbc.Statement;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;

/*
 * Klasse Programmeinstieg; nimmt die Benutzereingaben entgegen und soll dem Benutzer verscheidene Konfigurationsmöglichkeiten bieten.
 */

/**
 * @author Thomas
 *	Programmstart: Startet den Thread dieser Klasse, in dem dem Benutzer verschiedene Optionen zur Steuerung des Programms geboten werden.
 */
public class Programmeinstieg extends Thread {

	public Programmeinstieg() {
		// TODO Auto-generated constructor stub
	}
	//Status des Programms
	public int status = 0;
	private Ablaufsteuerung ablaufsteuerung = new Ablaufsteuerung();
	
	
	//Main Methode
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Programmeinstieg einstieg = new Programmeinstieg();
		einstieg.start();
		
	}
	
	public void run()
	{
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
				ablaufsteuerung.start();
				break;
			//Synchronisierung beenden
			case 2:
				//Mail mail = new Mail();
				//mail.senden();
				VerbindungSAP verbindungSAP = new VerbindungSAP();
				verbindungSAP.connect();
				
				
				MaterialSAP material = new MaterialSAP();
				material.materialListeHolen();
				
				break;
			//
			case 3:
				break;
			//Programm beenden
			case 4:
			ablaufsteuerung.threadStop();
			durchlauf = false;
				
			}
		}while(durchlauf);
	}
}
