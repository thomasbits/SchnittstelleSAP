package robin1;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoStructure;

//Klasse die die Kundensynchronisierung verwaltet
public class KundenSyncNEU extends Thread{

	private boolean datenVorhanden = true;
	private java.sql.Statement stmt;
	private Kunde Kunde1 = new Kunde();
	private boolean sync = true;
	private int i = 0;

	public KundenSyncNEU() {
		//Konstruktor
	}

	public void setSyncFalse()
	{
		sync = false;
		i = 19;
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
				kundendatenDatenbank();
				//Wenn Daten vorhanden, dann in das SAP System schreiben
				if(datenVorhanden)
				{
					//Verbindung zum SAP System
					VerbindungSAP verbindung = new VerbindungSAP();
					verbindung.connect();
					//Kunde erstellen
					createKunde();
					
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

	public void kundendatenDatenbank()
	{
		try {
			//Query ob Datensätze ohne SAP Nummer vorhanden sind?
			ResultSet results = stmt.executeQuery("SELECT * FROM kunde WHERE SAP_KId IS NULL;");
			//Abfragen ob Datensatz leer ist?
			if (!results.next()){
				//System.out.println("Result ist empty!!!!");
				datenVorhanden = false;
			}else
			{
				//Sonst Daten abfragen und in Klasse Kunde1 schreiben	
				results.first();

				Kunde1.setVorname(results.getString("vorname"));
				Kunde1.setName(results.getString("name"));
				Kunde1.setPLZ(results.getString("PLZ"));
				Kunde1.setOrt(results.getString("Ort"));
				Kunde1.setEmail(results.getString("Email"));
				Kunde1.setGeburtstdatum(results.getString("Geburtsdatum"));
				Kunde1.setGeschlecht(results.getString("Geschlecht"));
				Kunde1.setStrasse(results.getString("Strasse"));
				Kunde1.setHausNr(results.getString("Hausnummer"));

				/*
				System.out.println(Kunde1.getTitel());
				System.out.println(Kunde1.getVorname());
				System.out.println(Kunde1.getName());
				System.out.println(Kunde1.getStrasse());
				System.out.println(Kunde1.getHausNr());
				System.out.println(Kunde1.getPLZ());
				System.out.println(Kunde1.getOrt());
				System.out.println(Kunde1.getGeburtstdatum());
				System.out.println(Kunde1.getEmail());
				 */
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean createKunde(/*Kunde k*/)
	{
		try {
			//Abfragen ob ein Ziel(Das SAP System vorhanden ist)
			JCoDestination dest = JCoDestinationManager.getDestination("");
			//Repository holen
			JCoRepository repo = dest.getRepository();
			//BAPI auswählen
			JCoFunction func = repo.getFunction("BAPI_CUSTOMER_CREATEFROMDATA1");
			//Import Parameter festlegen
			JCoStructure personalData = func.getImportParameterList().getStructure("PI_PERSONALDATA");
			personalData.setValue("TITLE_P",Kunde1.getTitel());		
			personalData.setValue("FIRSTNAME", Kunde1.getVorname());
			personalData.setValue("LASTNAME", Kunde1.getName());	
			personalData.setValue("DATE_BIRTH", Kunde1.getGeburtstdatum());	
			personalData.setValue("CITY", Kunde1.getOrt());
			personalData.setValue("POSTL_COD1", Kunde1.getPLZ());
			personalData.setValue("STREET",Kunde1.getStrasse());
			personalData.setValue("HOUSE_NO",Kunde1.getHausNr());
			personalData.setValue("E_MAIL", Kunde1.getEmail());
			personalData.setValue("LANGU_P","DE");
			personalData.setValue("CURRENCY","EUR");
			personalData.setValue("COUNTRY","DE");

			/*
			 *The reference customer should be created bearing in mind that its sole
			 *purpose is to provide data for customers that are created via the BAPI.
			 *That is, the reference customer is not an operative customer in the
			 *business sense. As a result, the customer should be created with a
			 *separate account group with internal number assignment. In addition, 
			 *the reference customer must exist in the organizational data that is transferred 
			 *to the BAPI.
			 */
			//Referezkunde, wird für das anlegen eines neuen Kunden benötigt
			//muss im SAP System noch erstellt werden
			JCoStructure referenceData = func.getImportParameterList().getStructure("PI_COPYREFERENCE");
			referenceData.setValue("SALESORG", "DN00");
			referenceData.setValue("DISTR_CHAN", "IN");
			referenceData.setValue("DIVISION", "BI");			//Hier fehlen noch Werte!!!
			referenceData.setValue("REF_CUSTMR", "0000014000");

			//Daten an das SAP System übergeben
			JCoContext.begin(dest);
			func.execute(dest);

			JCoFunction funcCommit = dest.getRepository().getFunction("BAPI_TRANSACTION_COMMIT");
			funcCommit.execute(dest);
			JCoContext.end(dest);

			//Rückgabewert engegennehmen (SAP Kundennummer/Debitor)
			String sapNr = (String) func.getExportParameterList().getValue("CUSTOMERNO");

			//SAP Nummer in Datenbank schreiben
			String query1 = "UPDATE kunde set SAP_KId = " + sapNr + " WHERE Email = \"" + Kunde1.getEmail() +"\";";

			//Query ausführen
			try {
				stmt.execute(query1);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (JCoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Verbindung konnte nicht aufgebaut werden.");
			return false;
		}
		return true;
	}
}
