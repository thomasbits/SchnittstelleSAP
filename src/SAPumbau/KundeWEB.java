package SAPumbau;

import java.sql.ResultSet;
import java.sql.SQLException;
/*
 * Stellt dei ben�tigten Methoden bereit, um einen Kunden in der Webshopdatenbank zu erfassen und zu bearbeiten.
 */

/**
 * @author Thomas
 *	Stellt die ben�tigten Methoden bereit, um den Datensatz eines Kunden aus der Webshopdatenbank abzufragen 
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
			//Query ob Datens�tze ohne SAP Nummer vorhanden sind?
			ResultSet results = stmt.executeQuery("SELECT * FROM kunde WHERE SAP_KId IS NULL;");
			//Abfragen ob Datensatz leer ist?
			if (!results.next()){
				//System.out.println("Result ist empty!!!!");
				kunde1 = new Kunde();
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

	//Schreibt die �bergebene SAP-Nummer in die Webshopdatenbank
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
	
	
	//Schreibt die �bergebene SAP-Nummer in die Webshopdatenbank
		public void schreibeGeloescht()
		{
			//SAP Nummer in Datenbank schreiben
			String query1 = "UPDATE kunde set geloescht = 'ja' WHERE SAP_KId = \"" + kunde1.getSapNummer() +"\";";

			//Query ausf�hren
			try {
				stmt.execute(query1);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
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
	
	
	

	//Kunde l�schen
	public void kundenLoeschenDatenbank()	//Methode umbennenen in kundeL�schen() ???
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
//				kundeSAP.deleteKunde(kunde1);
				//Versende Email
				schreibeGeloescht();
				System.out.println("Kunde: " + kunde1.getSapNummer());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Fragt in der Webshopdatenbank ab, ob es ge�nderte Kundendatens�tze gibt und
	//schreibt diese anschlie�end ins SAP System
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
			//�nderungen in das SAP System schreiben
			kundeSAP.changeKunde(kunde1);
			//Status in Datenbank wieder auf Null setzen, da der Kunde erfolgreich ge�ndert wurde.
			schreibeGeaendert();
			
			System.out.println(kunde1.getVorname());		//nur zum testen
		}

	}


}
