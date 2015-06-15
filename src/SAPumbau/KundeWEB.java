package SAPumbau;

import java.sql.ResultSet;
import java.sql.SQLException;
/*
 * Stellt dei benötigten Methoden bereit, um einen Kunden in der Webshopdatenbank zu erfassen und zu bearbeiten.
 */

/**
 * @author Thomas
 *	Stellt die benötigten Methoden bereit, um den Datensatz eines Kunden aus der Webshopdatenbank abzufragen 
 */
public class KundeWEB {

	Ablaufsteuerung ablaufsteuerung;
	KundeSAP kundeSAP;
	KundeWEB kundeWEB;
	Kunde kunde1;
	java.sql.Statement stmt;
	
	
	public KundeWEB(Ablaufsteuerung ablaufsteuerung) {
		// TODO Auto-generated constructor stub
		this.ablaufsteuerung = ablaufsteuerung;
		kunde1 = new Kunde();
	}
	
	
	public void setStatement(java.sql.Statement stmt)
	{
		this.stmt = stmt;
	}

	
	/*
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
			//Abfragen ob Datensatz leer ist?
			if (!results.next()){
				//System.out.println("Result ist empty!!!!");
				kunde1 = null;
			}else
			{
				//Sonst Daten abfragen und in Klasse Kunde1 schreiben	
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
		if(kunde1 != null)
		{
			//Kunde in das SAP System schreiben
			kundeSAP.createKunde(kunde1);
		}


	}

	//Schreibt die Übergebene SAP-Nummer in die Webshopdatenbank
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

	//Kunde löschen
	public void kundenLoeschenDatenbank()
	{
		
		if (kundeSAP == null) {
			//Instanz KundeSAP holen
			kundeSAP = ablaufsteuerung.getInstanceKundeSAP();
		}
		kunde1 = new Kunde();
		try {
			//Query ob Datensätze ohne SAP Nummer vorhanden sind?
			ResultSet results = stmt.executeQuery("SELECT SAP_KId FROM kunde WHERE status = 'l' AND SAP_KId IS NOT NULL;");
			//Abfragen ob Datensatz leer ist?
			if (!results.next()){
				System.out.println("Result ist empty!!!!");
			}else
			{
				//Sonst Daten abfragen
				results.first();
				kunde1.setSapNummer(results.getString("SAP_KId"));
//				kundeSAP.deleteKunde(kunde1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Fragt in der Webshopdatenbank ab, ob es geänderte Kundendatensätze gibt und
	//schreibt diese anschließend ins SAP System
	public void abfrageGeänderteKunden()
	{
		if (kundeSAP == null) {
			//Instanz KundeSAP holen
			kundeSAP = ablaufsteuerung.getInstanceKundeSAP();
		}

		try {
			//Query ob Datensätze mit Änderungen vorhanden sind?
			ResultSet results = stmt.executeQuery("SELECT * FROM kunde WHERE status = 'a';");
			//Abfragen ob Datensatz leer ist?
			if (!results.next()){
				System.out.println("Result ist empty!!!!");			//nur zum Testen
				kunde1 = null;
			}else
			{
				//Sonst Daten abfragen und in Klasse Kunde schreiben	
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
			System.out.println(kunde1.getVorname());		//nur zum testen
		}

	}


}
