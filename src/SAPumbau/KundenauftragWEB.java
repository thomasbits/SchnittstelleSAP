package SAPumbau;

import java.sql.ResultSet;
import java.sql.SQLException;
/*
 * Holt die Daten zu einem Kundenauftrag aus der Datenbank des Webshops um sie anschlieﬂend an das SAP-System weiterzuleiten
 * Schreibt den aktuellen Stand eines Kundenauftrages in die Datenbank des Webshops
 */
public class KundenauftragWEB {

	Ablaufsteuerung ablaufsteuerung;
	Kundenauftrag auftrag;
	KundenauftragSAP auftragSAP;
	java.sql.Statement stmt;
	
	public KundenauftragWEB(Ablaufsteuerung ablaufsteuerung) {
		this.ablaufsteuerung = ablaufsteuerung;
	}
	
	public void abfrageNeueBestellungen()
	{
		if (auftragSAP == null) {
			//Instanz KundeSAP holen
			auftragSAP = ablaufsteuerung.getInstanceKundenauftragSAP();
		}
		
		try {
			//Query ob Datens‰tze ohne SAP Nummer vorhanden sind?
			ResultSet results = stmt.executeQuery("SELECT * FROM bestellung WHERE SAP_BestID IS NULL;");
			//Abfragen ob Datensatz leer ist?
			if (!results.next()){
				//System.out.println("Result ist empty!!!!");
				auftrag = null;
			}else
			{
				//Sonst Daten abfragen und in Klasse Kundenauftrag schreiben	
				results.first();

				auftrag.setDebitorennummer(results.getString("KId"));
				auftrag.setBestellNRWEB(results.getString("BestId"));
//				auftrag.setStatus(results.getString("Status"));
				
				String bestellID = results.getString("BestId");
				
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
			auftragSAP.createKundenauftrag(auftrag);
		}
	}
	

}
