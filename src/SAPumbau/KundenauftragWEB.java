package SAPumbau;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *	Stellt die benötigten Methoden bereit, um einen Kundenauftrag aus der Webshopdatenbank abzufragen und den Status zu aktualiesieren
 * @author Thomas
 */
public class KundenauftragWEB {

	private Report report = new Report(this.getClass().toString());
	private Ablaufsteuerung_Kundenauftrag ablaufsteuerung;
	private Kundenauftrag auftrag;
	private KundenauftragSAP auftragSAP;
	private String bestellID;
	private DatenbankVerbindung verbindung;

	/**
	 * Erstellt eine Instanz der Klasse Kundenauftrag
	 * @param ablaufsteuerung Instanz der Klasse Ablaufsteuerung_Kundenauftrag
	 */
	public KundenauftragWEB(Ablaufsteuerung_Kundenauftrag ablaufsteuerung) {
		this.ablaufsteuerung = ablaufsteuerung;
		auftrag = new Kundenauftrag();

		verbindung = new DatenbankVerbindung();	
	}
	/**
	 * Stellt eine neue Verbindung zur WebDB her
	 */
	public void neueVerbindungDB()
	{
		verbindung.schliesseVerbindung();
		verbindung = new DatenbankVerbindung();
	}

/**
 * Fragt ab, ob sich in der WebDB ein neuer Kundenauftrag befindet. Neue Aufträge werden anschließend ins SAP-System übertragen
 */
	public void abfrageNeueBestellungen()
	{

		if (auftragSAP == null) {
			//Instanz KundeSAP holen
			auftragSAP = ablaufsteuerung.getInstanceKundenauftragSAP();
		}

		try {
			//Query ob Datensätze ohne SAP Nummer vorhanden sind
			ResultSet results = verbindung.getInstance().createStatement().executeQuery("SELECT * FROM bestellung WHERE SAP_BestID IS NULL;");
			//Abfragen ob Datensatz leer ist?
			if (!results.next()){
				report.set("Keine neue Bestellung");
				auftrag = null;
			}else
			{

				report.set("Neuer Auftrag:" + results.getString("BestId"));
				
				//Sonst Daten abfragen und in die instanz auftrag der Klasse Kundenauftrag schreiben	
				results.last();


				auftrag.setDebitorennummer(results.getString("KId"));
				auftrag.setBestellNRWEB(results.getString("BestId"));
				auftrag.setBestellDatum(results.getString("Datum"));
				auftrag.setZahlungsart(results.getString("Zahlungsart"));
				auftrag.setVersandart(results.getString("versandart"));
				auftrag.setStatus(results.getString("Status"));
				auftrag.setLierferadresse_geschlecht(results.getString("lieferadresse_geschlecht"));
				auftrag.setLierferadresse_hausnummer(results.getString("lieferadresse_hausnummer"));
				auftrag.setLierferadresse_vorname(results.getString("lieferadresse_vorname"));
				auftrag.setLierferadresse_name(results.getString("lieferadresse_name"));
				auftrag.setLierferadresse_strasse(results.getString("lieferadresse_strasse"));
				auftrag.setLierferadresse_plz(results.getString("lieferadresse_plz"));
				auftrag.setLierferadresse_ort(results.getString("lieferadresse_ort"));

				String SAPNr = results.getString("KId");
				bestellID = results.getString("BestId");

				//Ermitteln der SAP-Kundennummer des Auftraggebers

				ResultSet kunde = verbindung.getInstance().createStatement().executeQuery("SELECT SAP_KId from kunde Where KId = " + SAPNr + ";");

				//Abfragen ob Datensatz leer ist?
				if (!kunde.next()){
					System.out.println("Result ist empty!!!!");
					auftrag = null;
				}else
				{

					auftrag.setDebitorennummer(kunde.getString("SAP_KId"));

					//------------------------Abfragen der Produkte der Bestellung
					ResultSet resultsprodukte = verbindung.getInstance().createStatement().executeQuery("SELECT * FROM bestellprodukte WHERE BestId = " + bestellID + ";");
					resultsprodukte.first();

					do
					{
						auftrag.setPosition(resultsprodukte.getString("PId"), resultsprodukte.getString("Menge"));

						resultsprodukte.next();

					}while(!resultsprodukte.isAfterLast());
				}

			}

			//			auftrag.ausgabeKundenauftrag();



			//-----------------Testausgabe von Position
			/*				
				Iterator iterator = auftrag.getPosition().entrySet().iterator();



				while(iterator.hasNext())
				{	
					Map.Entry e = (Map.Entry)iterator.next();

					System.out.println(e.getKey());
					System.out.println(e.getValue());


				}
			 */				


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(auftrag != null)
		{
			//Auftrag in das SAP System schreiben
			auftragSAP.createKundenauftrag(auftrag);
		}
	}

	public void setSAPNr(int SAPNr, int WSNr)
	{

		//SAP Nummer in Datenbank schreiben
		String query1 = "UPDATE bestellung set SAP_BestId = '" + SAPNr + "' WHERE BestId = '" + WSNr +"';";
		//		String query1 = "UPDATE kunde set SAP_KId = " + sapNummer + " WHERE Email = \"" + kunde1.getEmail() +"\";";
		//Query ausführen
		try {
			verbindung.getInstance().createStatement().execute(query1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getAuftragsNr()
	{
		if (auftragSAP == null) {
			//Instanz KundeSAP holen
			auftragSAP = ablaufsteuerung.getInstanceKundenauftragSAP();
		}

		try {
			ResultSet auftragsnr = verbindung.getInstance().createStatement().executeQuery("SELECT SAP_BestId from bestellung Where Status !='Auftrag erhalten';");

			while(auftragsnr.next())
			{
				auftragSAP.getStatus(auftragsnr.getString("SAP_BestId"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}



	public void setAuftragsStatus(String bestellNR, String status)
	{
		//Status in Datenbank schreiben
		String query1 = "UPDATE bestellung set Status = " + status + " WHERE SAP_BestId = \"" + bestellNR +"\";";

		//Query ausführen
		try {
			verbindung.getInstance().createStatement().execute(query1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
