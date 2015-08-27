package SAPumbau;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Thomas and Robin
 *	Stellt die benötigten Methoden bereit, um den Datensatz eines Kunden aus der Webshopdatenbank abzufragen und ihn um SAP System anzulegen, löschen oder ändern
 */
public class KundeWEB {

	private Report report = new Report(this.getClass().toString());
	Ablaufsteuerung_Kunde ablaufsteuerung;
	KundeSAP kundeSAP;
	KundeWEB kundeWEB;
	
	Kunde kunde1;
	java.sql.Statement stmt;
	boolean kundeGefunden = false;

	/**
	 * Konstruktor
	 * Instanz der Ablaufsteuerung entgegennehmen
	 * @param ablaufsteuerung
	 */
	public KundeWEB(Ablaufsteuerung_Kunde ablaufsteuerung) {
		// TODO Auto-generated constructor stub
		this.ablaufsteuerung = ablaufsteuerung;
		kundeSAP = new KundeSAP(ablaufsteuerung);
		kunde1 = new Kunde();
	}

	/**
	 * SQL Statement entgegennehmen um Datenbank Querys und Abfragen durchzuführen
	 * @param stmt
	 */
	public void setStatement(java.sql.Statement stmt)
	{
		this.stmt = stmt;
	}

	/**
	 * Kunde neu anlegen
	 * Fragt in der Webshopdatenbank ab, ob sich ein neuer Kunde Registriert hat.(Neu registrierte Kunden haben noch keine SAP-Kundennummer)
	 */
	public void abfrageNeueKunden()
	{
		
		if (kundeSAP == null) {
			//Instanz KundeSAP holen
			kundeSAP = ablaufsteuerung.getInstanceKundeSAP();
		}

		try {
			//Query ob Datensätze ohne SAP Nummer vorhanden sind?
			ResultSet results = stmt.executeQuery("SELECT * FROM kunde WHERE SAP_KId IS NULL;");
			//Abfragen ob Datensatz leer ist
			if (!results.next()){
				//new Logger("Kein neuer Kunde gefunden.");
			
			}else
			{
				
				//Sonst Daten abfragen und in Klasse Kunde1 schreiben	
				report.set("Neuer Kunde gefunden.");

				kundeGefunden = true;

				kunde1 = new Kunde();
				results.first();

				kunde1.setVorname(results.getString("vorname"));
				kunde1.setName(results.getString("name"));
				kunde1.setPLZ(results.getString("PLZ"));
				kunde1.setOrt(results.getString("Ort"));
				kunde1.setEmail(results.getString("Email"));
				kunde1.setGeburtstdatum(results.getString("Geburtsdatum"));
				kunde1.setGeschlecht(results.getString("Geschlecht"));
				kunde1.setStrasse(results.getString("Strasse"));
				kunde1.setHausNr(results.getString("Hausnummer"));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(kundeGefunden)
		{
			//Kunde in das SAP System schreiben
			report.set("Kunde anlegen wird durchgeführt.");
			kundeSAP.createKunde(kunde1);
			schreibeSAPNummer(kunde1.getSapNummer());
			kundeGefunden = false;
		}
	}

	/**
	 * Kunde löschen
	 * Es wird in der Datenbank geschaut ob ein Kunde gelöscht werden soll. (Hat sein Benutzerkonto gelöscht) 
	 * In diesem Fall wird eine Email an einen Mitarbeiter versendet mit der Information das sich dieser Kunde gelöscht und dies im SAP System vermerkt werden muss.
	 */
	public void kundenLoeschenDatenbank()
	{

		if (kundeSAP == null) {
			//Instanz KundeSAP holen
			kundeSAP = ablaufsteuerung.getInstanceKundeSAP();
		}
		kunde1 = new Kunde();
		try {
			//Query ob Datensätze ohne SAP Nummer vorhanden sind?
			ResultSet results = stmt.executeQuery("SELECT SAP_KId FROM kunde WHERE status = 'l' AND geloescht = 'nein' AND SAP_KId IS NOT NULL;");
			//Abfragen ob Datensatz leer ist?
			if (!results.next()){

			}else
			{
				//Sonst Daten abfragen
				results.first();
				kunde1.setSapNummer(results.getString("SAP_KId"));
				//Versende Email
				report.set("Kunde löschen wird durchgeführt.");
				schreibeGeloescht();
				//System.out.println("Kunde: " + kunde1.getSapNummer());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Kunde ändern
	 * Fragt in der Webshopdatenbank ab, ob es geänderte Kundendatensätze gibt und schreibt diese anschließend ins SAP System
	 */
	public void abfrageGeänderteKunden()
	{
		if (kundeSAP == null) {
			//Instanz KundeSAP holen
			kundeSAP = ablaufsteuerung.getInstanceKundeSAP();
		}

		try {
			//Query ob Datensätze mit Änderungen vorhanden sind?
			ResultSet results = stmt.executeQuery("SELECT * FROM kunde WHERE status = 'a' ;");
			//Abfragen ob Datensatz leer ist?
			if (!results.next()){
				//new Logger("Kein Kunde zum ändern gefunden!");
				kunde1 = null;
			}else
			{
				//Sonst Daten abfragen und in Klasse Kunde schreiben
				report.set("Kunde zum ändern gefunden!");

				results.first();

				kunde1.setSapNummer(results.getString("SAP_KId"));
				kunde1.setVorname(results.getString("vorname"));
				kunde1.setName(results.getString("name"));
				kunde1.setPLZ(results.getString("PLZ"));
				kunde1.setOrt(results.getString("Ort"));
				kunde1.setEmail(results.getString("Email"));
				kunde1.setGeburtstdatum(results.getString("Geburtsdatum"));
				kunde1.setGeschlecht(results.getString("Geschlecht"));
				kunde1.setStrasse(results.getString("Strasse"));
				kunde1.setHausNr(results.getString("Hausnummer"));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(kunde1 != null)
		{
			//Änderungen in das SAP System schreiben
			kundeSAP.changeKunde(kunde1);
			report.set("Kunde ändern wird durchgeführt.");
			//Status in Datenbank wieder auf Null setzen, da der Kunde erfolgreich geändert wurde.
			schreibeGeaendert();
		}
	}

	/**
	 * Schreibt die übergebene SAP-Nummer in die Webshopdatenbank
	 * @param sapNummer
	 */
	public void schreibeSAPNummer(String sapNummer)
	{
		//SAP Nummer in Datenbank schreiben
		String query1 = "UPDATE kunde set SAP_KId = " + sapNummer + " WHERE Email = \"" + kunde1.getEmail() +"\";";

		//Query ausführen
		try {
			stmt.execute(query1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	//Kann weg
	public void schreibeGeloescht()
	{
		//SAP Nummer in Datenbank schreiben
		String query1 = "UPDATE kunde set geloescht = 'ja' WHERE SAP_KId = \"" + kunde1.getSapNummer() +"\";";
		System.out.println("Kunde " + kunde1.getSapNummer() + " wird gelöscht.");
		//Query ausführen
		

		try {
			stmt.execute(query1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * Schreibt das Feld status nach Kundenänderung wieder auf "NULL"
	 */
	public void schreibeGeaendert()
	{
		//SAP Nummer in Datenbank schreiben
		String query1 = "UPDATE kunde set status = NULL WHERE SAP_KId = \"" + kunde1.getSapNummer() +"\";";

		//Query ausführen
		try {
			stmt.execute(query1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
