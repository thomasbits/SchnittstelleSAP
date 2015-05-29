package SAPumbau;



import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoStructure;

public class KundeSAP {
	
	

	public KundeSAP() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean createKunde(Kunde kunde1)
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
			personalData.setValue("TITLE_P",kunde1.getTitel());		
			personalData.setValue("FIRSTNAME", kunde1.getVorname());
			personalData.setValue("LASTNAME", kunde1.getName());	
			personalData.setValue("DATE_BIRTH", kunde1.getGeburtstdatum());	
			personalData.setValue("CITY", kunde1.getOrt());
			personalData.setValue("POSTL_COD1", kunde1.getPLZ());
			personalData.setValue("STREET",kunde1.getStrasse());
			personalData.setValue("HOUSE_NO",kunde1.getHausNr());
			personalData.setValue("E_MAIL", kunde1.getEmail());
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
			
			kunde1.setSapNummer(sapNr);
			
			KundeWEB.schreibeSAPNummer(sapNr);
			
			/*
			//Muss in die Klasse KundeWEB
			//SAP Nummer in Datenbank schreiben
			String query1 = "UPDATE kunde set SAP_KId = " + sapNr + " WHERE Email = \"" + kunde1.getEmail() +"\";";

			//Query ausführen
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
	
	
	
	
	
	
	private boolean changeKunde()
	{
		//muss noch implementiert werden
		
		return true;
	}
	
	
	public boolean deleteKunde(Kunde kunde1)
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

			/*
			personalData.setValue("SALESORG", "DN00");
			personalData.setValue("DISTR_CHAN", "IN");
			personalData.setValue("DIVISION", "BI");
			*/

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
