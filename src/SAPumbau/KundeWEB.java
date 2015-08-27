package SAPumbau;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Thomas and Robin
 *	Stellt die ben�tigten Methoden bereit, um den Datensatz eines Kunden aus der Webshopdatenbank abzufragen und ihn um SAP System anzulegen, l�schen oder �ndern
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
	 * SQL Statement entgegennehmen um Datenbank Querys und Abfragen durchzuf�hren
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
			//Query ob Datens�tze ohne SAP Nummer vorhanden sind?
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
			report.set("Kunde anlegen wird durchgef�hrt.");
			kundeSAP.createKunde(kunde1);
			schreibeSAPNummer(kunde1.getSapNummer());
			kundeGefunden = false;
		}
	}

	/**
	 * Kunde l�schen
	 * Es wird in der Datenbank geschaut ob ein Kunde gel�scht werden soll. (Hat sein Benutzerkonto gel�scht) 
	 * In diesem Fall wird eine Email an einen Mitarbeiter versendet mit der Information das sich dieser Kunde gel�scht und dies im SAP System vermerkt werden muss.
	 */
	public void kundenLoeschenDatenbank()
	{

		if (kundeSAP == null) {
			//Instanz KundeSAP holen
			kundeSAP = ablaufsteuerung.getInstanceKundeSAP();
		}
		kunde1 = new Kunde();
		try {
			//Query ob Datens�tze ohne SAP Nummer vorhanden sind?
			ResultSet results = stmt.executeQuery("SELECT SAP_KId FROM kunde WHERE status = 'l' AND geloescht = 'nein' AND SAP_KId IS NOT NULL;");
			//Abfragen ob Datensatz leer ist?
			if (!results.next()){

			}else
			{
				//Sonst Daten abfragen
				results.first();
				kunde1.setSapNummer(results.getString("SAP_KId"));
				//Versende Email
				report.set("Kunde l�schen wird durchgef�hrt.");
				schreibeGeloescht();
				//System.out.println("Kunde: " + kunde1.getSapNummer());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Kunde �ndern
	 * Fragt in der Webshopdatenbank ab, ob es ge�nderte Kundendatens�tze gibt und schreibt diese anschlie�end ins SAP System
	 */
	public void abfrageGe�nderteKunden()
	{
		if (kundeSAP == null) {
			//Instanz KundeSAP holen
			kundeSAP = ablaufsteuerung.getInstanceKundeSAP();
		}

		try {
			//Query ob Datens�tze mit �nderungen vorhanden sind?
			ResultSet results = stmt.executeQuery("SELECT * FROM kunde WHERE status = 'a' ;");
			//Abfragen ob Datensatz leer ist?
			if (!results.next()){
				//new Logger("Kein Kunde zum �ndern gefunden!");
				kunde1 = null;
			}else
			{
				//Sonst Daten abfragen und in Klasse Kunde schreiben
				report.set("Kunde zum �ndern gefunden!");

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
			//�nderungen in das SAP System schreiben
			kundeSAP.changeKunde(kunde1);
			report.set("Kunde �ndern wird durchgef�hrt.");
			//Status in Datenbank wieder auf Null setzen, da der Kunde erfolgreich ge�ndert wurde.
			schreibeGeaendert();
		}
	}

	/**
	 * Schreibt die �bergebene SAP-Nummer in die Webshopdatenbank
	 * @param sapNummer
	 */
	public void schreibeSAPNummer(String sapNummer)
	{
		//SAP Nummer in Datenbank schreiben
		String query1 = "UPDATE kunde set SAP_KId = " + sapNummer + " WHERE Email = \"" + kunde1.getEmail() +"\";";

		//Query ausf�hren
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
		System.out.println("Kunde " + kunde1.getSapNummer() + " wird gel�scht.");
		//Query ausf�hren
		

		try {
			stmt.execute(query1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * Schreibt das Feld status nach Kunden�nderung wieder auf "NULL"
	 */
	public void schreibeGeaendert()
	{
		//SAP Nummer in Datenbank schreiben
		String query1 = "UPDATE kunde set status = NULL WHERE SAP_KId = \"" + kunde1.getSapNummer() +"\";";

		//Query ausf�hren
		try {
			stmt.execute(query1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
