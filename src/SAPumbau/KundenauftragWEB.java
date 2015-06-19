package SAPumbau;

import java.sql.ResultSet;
import java.sql.SQLException;
/*
 * Holt die Daten zu einem Kundenauftrag aus der Datenbank des Webshops um sie anschließend an das SAP-System weiterzuleiten
 * Schreibt den aktuellen Stand eines Kundenauftrages in die Datenbank des Webshops
 */

/**
 *	Stellt die benötigten Methoden bereit, um einen Kundenauftrag aus der Webshopdatenbank abzufragen und den Status zu aktualiesieren
 * @author Thomas
 */
public class KundenauftragWEB {

	Ablaufsteuerung ablaufsteuerung;
	Kundenauftrag auftrag = new Kundenauftrag();
	KundenauftragSAP auftragSAP;
	java.sql.Statement stmt;
	
	public KundenauftragWEB(Ablaufsteuerung ablaufsteuerung) {
		this.ablaufsteuerung = ablaufsteuerung;
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
				
				System.out.println("BestellID:" + results.getString("BestId"));			//nur zum Testen
				System.out.println(results);
				//Sonst Daten abfragen und in instanz auftrag der Klasse Kundenauftrag schreiben	
				results.first();

				auftrag.setDebitorennummer(results.getString("KId"));
				auftrag.setBestellNRWEB(results.getString("BestId"));
//				auftrag.setStatus(results.getString("Status"));
				
				String bestellID = results.getString("BestId");
				
				System.out.println(results.getString("PId"));
				
				while(results.next())
				{
					if(results.getString("BestId").equals(bestellID))
					{			
						auftrag.setPosition(results.getString("PId"), results.getString("Menge"));
					}
		
					results.next();
	
				}
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(auftrag != null)
		{
			//Auftrag in das SAP System schreiben
			//auftragSAP.createKundenauftrag(auftrag);
		}
	}
	

}
