package SAPumbau;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *	Stellt die benötigten Methoden bereit, um den Datensatz eines Kunden aus der Webshopdatenbank abzufragen
 *  @author Thomas and Robin
 */
public class KundeWEB {

	private Report report = new Report(this.getClass().toString());
	private Ablaufsteuerung_Kunde ablaufsteuerung;
	private KundeSAP kundeSAP;
	private DatenbankVerbindung verbindung;

	private Kunde kunde1;
	boolean kundeGefunden = false;

	/**
	 * Konstruktor: Ertellt Instanzen der Klassen Kunde und DatenbankVerbindung
	 * @param ablaufsteuerung Instanz der Klasse Ablaufsteuerung_Kunde
	 */
	public KundeWEB(Ablaufsteuerung_Kunde ablaufsteuerung) {

		this.ablaufsteuerung = ablaufsteuerung;

		kunde1 = new Kunde();

		verbindung = new DatenbankVerbindung();	
	}

	/**
	 * Stellt eine neue Verbindung zur WebDB her
	 */
	public void neueVerbindungDB()
	{
//		verbindung.schliesseVerbindung();
		verbindung = new DatenbankVerbindung();
	}

	/**
	 * Fragt in der Webshopdatenbank ab, ob sich ein neuer Kunde Registriert hat.(Neu registrierte Kunden haben noch keine SAP-Kundennummer)
	 */
	public void abfrageNeueKunden()
	{

		if (kundeSAP == null) {
			//Instanz KundeSAP holen
			kundeSAP = ablaufsteuerung.getInstanceKundeSAP();
		}

		try {
			//Query ob Datensätze ohne SAP Nummer vorhanden sind
			ResultSet results = verbindung.getInstance().createStatement().executeQuery("SELECT * FROM kunde WHERE SAP_KId IS NULL;");
			//Abfragen ob Datensatz leer ist
			if (!results.next()){
				//				report.set("Kein neuer Kunde gefunden!");

			}else
			{
				//Sonst Daten abfragen und in die Instanz kunde1 der Klasse Kunde schreiben	
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
			report.set(e.toString());
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
	 * Es wird in der Datenbank geschaut ob ein Kunde gelöscht werden soll. (Hat sein Benutzerkonto gelöscht) 
	 * In diesem Fall wird eine Email an einen Mitarbeiter versendet mit der Information das sich dieser Kunde gelöscht hat und dies im SAP System vermerkt werden muss.
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
			ResultSet results = verbindung.getInstance().createStatement().executeQuery("SELECT SAP_KId FROM kunde WHERE status = 'l' AND geloescht = 'nein' AND SAP_KId IS NOT NULL;");
			//Abfragen ob Datensatz leer ist?
			if (!results.next()){

			}else
			{
				//Sonst Daten abfragen
				results.first();
				kunde1.setSapNummer(results.getString("SAP_KId"));
				//Versende Email
				report.set("Kunde löschen wird durchgeführt." + results.getString("SAP_KId"));
				schreibeGeloescht();
			}

		} catch (SQLException e) {
			report.set(e.toString());
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
			//Query ob Datensätze mit Änderungen vorhanden sind
			ResultSet results = verbindung.getInstance().createStatement().executeQuery("SELECT * FROM kunde WHERE status = 'a' ;");
			//Abfragen ob Datensatz leer ist?
			if (!results.next()){
				//				report.set("Kein Kunde zum ändern gefunden!");
				kunde1 = null;
			}else
			{
				//Sonst Daten abfragen und in Klasse Kunde schreiben
				results.first();

				report.set("Kunde zum ändern gefunden!" + results.getString("SAP_KId"));

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
			report.set(e.toString());
		}
		if(kunde1 != null)
		{
			//Änderungen in das SAP System schreiben
			kundeSAP.changeKunde(kunde1);
			report.set("Kunde ändern wird durchgeführt." + kunde1.getSapNummer());
			//Status in Datenbank wieder auf Null setzen, da der Kunde erfolgreich geändert wurde.
			schreibeGeaendert();
		}
	}

	/**
	 * Schreibt die übergebene SAP-Nummer in die Webshopdatenbank
	 * @param sapNummer SAP-Nummer
	 */
	public void schreibeSAPNummer(String sapNummer)
	{
		//SAP Nummer in Datenbank schreiben
		String query1 = "UPDATE kunde set SAP_KId = " + sapNummer + " WHERE Email = \"" + kunde1.getEmail() +"\";";

		//Query ausführen
		try {
			verbindung.getInstance().createStatement().execute(query1);
		} catch (SQLException e) {
			report.set(e.toString());
		}
	}


	/**
	 * Kennzeichnet den Kunden in der WebDB als gelöscht
	 */
	public void schreibeGeloescht()
	{
		//SAP Nummer in Datenbank schreiben
		String query1 = "UPDATE kunde set geloescht = 'ja' WHERE SAP_KId = \"" + kunde1.getSapNummer() +"\";";
		report.set("Kunde " + kunde1.getSapNummer() + " wird gelöscht.");
		
		//Query ausführen
		try {
			verbindung.getInstance().createStatement().execute(query1);
		} catch (SQLException e) {
			report.set(e.toString());
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
			verbindung.getInstance().createStatement().execute(query1);
		} catch (SQLException e) {
			report.set(e.toString());
		}
	}
}
