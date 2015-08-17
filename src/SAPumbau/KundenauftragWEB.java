package SAPumbau;

import java.sql.ResultSet;
import java.sql.SQLException;
/*
 * Holt die Daten zu einem Kundenauftrag aus der Datenbank des Webshops um sie anschließend an das SAP-System weiterzuleiten
 * Schreibt den aktuellen Stand eines Kundenauftrages in die Datenbank des Webshops
 */
import java.util.Iterator;
import java.util.Map;

/**
 *	Stellt die benötigten Methoden bereit, um einen Kundenauftrag aus der Webshopdatenbank abzufragen und den Status zu aktualiesieren
 * @author Thomas
 */
public class KundenauftragWEB {

	Ablaufsteuerung ablaufsteuerung;
	Kundenauftrag auftrag;
	KundenauftragSAP auftragSAP;
	java.sql.Statement stmt;
	
	public KundenauftragWEB(Ablaufsteuerung ablaufsteuerung) {
		this.ablaufsteuerung = ablaufsteuerung;
		auftrag = new Kundenauftrag();
	}
	
	public void setStatement(java.sql.Statement stmt)
	{
		this.stmt = stmt;
	}
	
	public void abfrageNeueBestellungen()
	{
		if (auftragSAP == null) {
			//Instanz KundeSAP holen
			auftragSAP = ablaufsteuerung.getInstanceKundenauftragSAP();
		}
		
		try {
			//Query ob Datensätze ohne SAP Nummer vorhanden sind?
			ResultSet results = stmt.executeQuery("SELECT * FROM bestellung WHERE SAP_BestID IS NULL;");
			//Abfragen ob Datensatz leer ist?
			if (!results.next()){
				System.out.println("Result ist empty!!!!");
				auftrag = null;
			}else
			{
				
//				System.out.println("BestellID:" + results.getString("BestId"));			//nur zum Testen
				
				//Sonst Daten abfragen und in die instanz auftrag der Klasse Kundenauftrag schreiben	
				results.last();
				
				
				auftrag.setDebitorennummer(results.getString("KId"));
				auftrag.setBestellNRWEB(results.getString("BestId"));
				auftrag.setBestellDatum(results.getString("Datum"));
				auftrag.setZahlungsart(results.getString("Zahlungsart"));
				
				
//				auftrag.setStatus(results.getString("Status"));
				
				String bestellID = results.getString("BestId");
				
				System.out.println(bestellID);
				
				
//------------------------Abfragen der Produkte der Bestellung
				ResultSet resultsprodukte = stmt.executeQuery("SELECT * FROM bestellprodukte WHERE BestId = " + bestellID + ";");
				resultsprodukte.first();
				
				do
				{
					auftrag.setPosition(resultsprodukte.getString("PId"), resultsprodukte.getString("Menge"));
						
					resultsprodukte.next();
					
				}while(!resultsprodukte.isAfterLast());
				
//-----------------Testausgabe von Position
				
				Iterator iterator = auftrag.getPosition().entrySet().iterator();
				

				
				while(iterator.hasNext())
				{	
					Map.Entry e = (Map.Entry)iterator.next();
					
					System.out.println(e.getKey());
					System.out.println(e.getValue());
					
					
				}
				
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(auftrag != null)
		{
			//Auftrag in das SAP System schreiben
//			auftragSAP.createKundenauftrag(auftrag);
		}
	}
	

}
