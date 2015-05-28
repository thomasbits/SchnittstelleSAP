package test;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoStructure;

//Klasse die alle Kunden aus dem SAP System löscht die der Datenbank gelöscht werden sollen. (Merkmal Feld status = g(gelöscht) )
public class KundenSyncLOE extends Thread {

	private boolean sync = true;
	private int i = 0;
	private boolean datenVorhanden = true;
	private java.sql.Statement stmt;
	private String SAPDebitorNr;
	Kunde kunde1 = new Kunde();
	
	public KundenSyncLOE() {
		// TODO Auto-generated constructor stub
	}

	public void run()
	{
		while(sync)
		{
			
			//Datenbankverbindung aufbauen - Instanz
			DatenbankVerbindung dbconnection = new DatenbankVerbindung();
			//Nach 20 Abfragen Verbindung neu aufbauen!
			for(i = 0; i<20; i++)
			{
				//Variable ob Daten vorhanden sind
				datenVorhanden = true;
				//Statement abfragen
				stmt = dbconnection.getStatement();
				//Daten abfragen
				kundenLoeschenDatenbank();
				//Wenn Daten vorhanden, dann in das SAP System schreiben
				if(datenVorhanden)
				{
					System.out.println("Kunde gefunden.. wird gelöscht");
					//Verbindung zum SAP System
					VerbindungSAP verbindung = new VerbindungSAP();
					verbindung.connect();
					//Kunde erstellen
					deleteKunde();
					
					
					
				}
				
				try {
					sleep(20000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
			//DB Verbindung schließen
			dbconnection.schliesseVerbindung();
		}
	}

	public void setSyncFalse()
	{
		sync = false;
		i = 19;
	}
	
	public void kundenLoeschenDatenbank()
	{
		try {
			//Query ob Datensätze ohne SAP Nummer vorhanden sind?
			ResultSet results = stmt.executeQuery("SELECT SAP_KId FROM kunde WHERE status = 'l' AND SAP_KId IS NOT NULL;");
			//Abfragen ob Datensatz leer ist?
			if (!results.next()){
				//System.out.println("Result ist empty!!!!");
				datenVorhanden = false;
			}else
			{
				//Sonst Daten abfragen
				results.first();

				kunde1.setSapNummer(results.getString("SAP_KId"));
				
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean deleteKunde(/*Kunde k*/)
	{
		try {
			//Abfragen ob ein Ziel(Das SAP System vorhanden ist)
			JCoDestination dest = JCoDestinationManager.getDestination("");
			//Repository holen
			JCoRepository repo = dest.getRepository();
			//BAPI auswählen
			JCoFunction func = repo.getFunction("BAPI_CUSTOMER_DELETE");
			//Import Parameter festlegen
			
			
	
			//JCoStructure personalData = func.getImportParameterList().getStructure("CUSTOMERNO");
			func.getImportParameterList().setValue("CUSTOMERNO", kunde1.getSapNummer());
			//personalData.setValue("CUSTOMER",kunde1.getSapNummer());		
			

			//Daten an das SAP System übergeben
			JCoContext.begin(dest);
			func.execute(dest);

			JCoFunction funcCommit = dest.getRepository().getFunction("BAPI_TRANSACTION_COMMIT");
			funcCommit.execute(dest);
			JCoContext.end(dest);

			//Rückgabewert engegennehmen (SAP Kundennummer/Debitor)
			//String sapNr = (String) func.getExportParameterList().getValue("RETURN");
			System.out.println("Ergebnis: " + func.getExportParameterList().getValue("RETURN")); 

			//System.out.println(sapNr);
			//SAP Nummer in Datenbank schreiben
			//String query1 = "UPDATE kunde set SAP_KId = " + sapNr + " WHERE Email = \"" + Kunde1.getEmail() +"\";";

			//Query ausführen
			 
			
			/*
			try {
				stmt.execute(query1);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
		} catch (JCoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Verbindung konnte nicht aufgebaut werden.");
			return false;
		}
		return true;
	}
}
	
	
	

