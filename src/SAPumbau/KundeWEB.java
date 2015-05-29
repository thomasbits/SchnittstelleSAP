package SAPumbau;

import java.sql.ResultSet;
import java.sql.SQLException;

public class KundeWEB {

	public KundeWEB() {
		// TODO Auto-generated constructor stub

	}
	Kunde kunde1 = new Kunde();
	java.sql.Statement stmt;
	
	public void setStatement(java.sql.Statement stmt)
	{
		this.stmt = stmt;
	}
	
	
	
	public Kunde abfrageNeueKunden()
	{


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
		return kunde1;
	}


	public void schreibeSAPNummer(String sapNummer)

	{
		//Muss in die Klasse KundeWEB
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
}
