package SAPumbau;



import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoStructure;
/*
 * 
 */
public class KundeSAP {

	private Ablaufsteuerung ablaufsteuerung;
	private KundeWEB kundeWEB;

	public KundeSAP(Ablaufsteuerung ablaufsteuerung) {
		// TODO Auto-generated constructor stub

		this.ablaufsteuerung = ablaufsteuerung;
	}						  




	public void createKunde(Kunde kunde1)
	{


		if (kundeWEB == null) {
			System.out.println("testas");
			//Instanz KundeWEB holen
			kundeWEB = ablaufsteuerung.getInstanceKundeWEB();
		}



		try {
			//Abfragen ob ein Ziel(Das SAP System vorhanden ist)
			JCoDestination dest = JCoDestinationManager.getDestination("");
			//Repository holen
			JCoRepository repo = dest.getRepository();
			//BAPI ausw�hlen
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
			//Referezkunde, wird f�r das anlegen eines neuen Kunden ben�tigt
			//muss im SAP System noch erstellt werden
			JCoStructure referenceData = func.getImportParameterList().getStructure("PI_COPYREFERENCE");
			referenceData.setValue("SALESORG", "DN00");
			referenceData.setValue("DISTR_CHAN", "IN");
			referenceData.setValue("DIVISION", "BI");			//Hier fehlen noch Werte!!!
			referenceData.setValue("REF_CUSTMR", "0000014000");

			//Daten an das SAP System �bergeben
			JCoContext.begin(dest);
			func.execute(dest);

			JCoFunction funcCommit = dest.getRepository().getFunction("BAPI_TRANSACTION_COMMIT");
			funcCommit.execute(dest);
			JCoContext.end(dest);

			//R�ckgabewert engegennehmen (SAP Kundennummer/Debitor)
			String sapNr = (String) func.getExportParameterList().getValue("CUSTOMERNO");



			kundeWEB.schreibeSAPNummer(sapNr);



		} catch (JCoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Verbindung konnte nicht aufgebaut werden.");

		}

	}

	//Gibt noch eine Fehlermeldung zur�ck
	public boolean changeKunde(Kunde kunde1)
	{

		try{
		//Abfragen ob ein Ziel(Das SAP System vorhanden ist)
		JCoDestination dest = JCoDestinationManager.getDestination("");
		//Repository holen
		JCoRepository repo = dest.getRepository();
		//BAPI ausw�hlen
		JCoFunction func = repo.getFunction("BAPI_CUSTOMER_CHANGEFROMDATA1");
		
		//Kundennummer, dessen Daten ge�ndert werden sollen
		func.getImportParameterList().setValue("CUSTOMERNO","25026");		//testdaten, sp�ter aus kunde1 holen.
		
		//die Daten, die ge�ndert werden sollen
		JCoStructure changeData = func.getImportParameterList().getStructure("PI_PERSONALDATA");
		changeData.setValue("TITLE_P","Herr");
		changeData.setValue("FIRSTNAME","Testkunde");
		changeData.setValue("LASTNAME","Testkunde");
		changeData.setValue("DATE_BIRTH","19920820");
		changeData.setValue("CITY","Buxdehude");
		changeData.setValue("POSTL_COD1","34389");				//testdaten, sp�ter aus kunde1 holen.
		changeData.setValue("STREET","Kleiner Weg");
		changeData.setValue("HOUSE_NO","5");
		changeData.setValue("E_MAIL","Testuser@user.com");
		changeData.setValue("COUNTRY","DE");
		changeData.setValue("CURRENCY","EUR");
		changeData.setValue("LANGU_P","DE");
		
		//Flag, welches bei den Werten, die ge�ndert werden sollen, gesetzt werden muss
		JCoStructure changeFlag = func.getImportParameterList().getStructure("PI_PERSONALDATAX");
		changeFlag.setValue("TITLE_P","X");
		changeFlag.setValue("FIRSTNAME","X");
		changeFlag.setValue("LASTNAME","X");
		changeFlag.setValue("DATE_BIRTH","X");
		changeFlag.setValue("CITY","X");
		changeFlag.setValue("POSTL_COD1","X");
		changeFlag.setValue("STREET","X");
		changeFlag.setValue("HOUSE_NO","X");
		changeFlag.setValue("E_MAIL","X");
		changeFlag.setValue("COUNTRY", "X");
		changeFlag.setValue("CURRENCY", "X");
		changeFlag.setValue("LANGU_P", "X");
				
		func.getImportParameterList().setValue("PI_SALESORG", "DN00");
		
		
		//Daten an das SAP System �bergeben
		JCoContext.begin(dest);
		func.execute(dest);
		JCoFunction funcCommit = dest.getRepository().getFunction("BAPI_TRANSACTION_COMMIT");
		funcCommit.execute(dest);
		JCoContext.end(dest);
		
		
		System.out.println(func.getExportParameterList().getValue("RETURN"));
		
		
		}catch (JCoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Verbindung konnte nicht aufgebaut werden.");
			return false;
		}

		return true;
	}

	//Kunde l�schen
	//so nicht Umsetzbar. Anderen L�sungsweg finden.
	public boolean deleteKunde(Kunde kunde1)
	{
		try {

//			kundeIstNeu();
			System.out.println("test");

			//Abfragen ob ein Ziel(Das SAP System vorhanden ist)
			JCoDestination dest = JCoDestinationManager.getDestination("");
			//Repository holen
			JCoRepository repo = dest.getRepository();
			//BAPI ausw�hlen
			JCoFunction func = repo.getFunction("BAPI_CUSTOMER_DELETE");
			//Import Parameter festlegen

			System.out.println(func.getImportParameterList());

//			JCoStructure personalData = func.getImportParameterList().getStructure("CUSTOMERNO");
			func.getImportParameterList().setValue("CUSTOMERNO", "25026");//kunde1.getSapNummer());
//			personalData.setValue("CUSTOMER", "25009");//kunde1.getSapNummer());	
			


			
//			personalData.setValue("SALESORG", "DN00");
//			personalData.setValue("DISTR_CHAN", "IN");
//			personalData.setValue("DIVISION", "BI");
			

			//JCoStructure personalData = func.getImportParameterList().getStructure("CUSTOMERNO");
			func.getImportParameterList().setValue("CUSTOMERNO", kunde1.getSapNummer());
			//personalData.setValue("CUSTOMER",kunde1.getSapNummer());		

			/*
			personalData.setValue("SALESORG", "DN00");
			personalData.setValue("DISTR_CHAN", "IN");
			personalData.setValue("DIVISION", "BI");
			 */


			//Daten an das SAP System �bergeben
			JCoContext.begin(dest);
			func.execute(dest);

			JCoFunction funcCommit = dest.getRepository().getFunction("BAPI_TRANSACTION_COMMIT");
			funcCommit.execute(dest);
			JCoContext.end(dest);

			//R�ckgabewert engegennehmen (SAP Kundennummer/Debitor)
			//String sapNr = (String) func.getExportParameterList().getValue("RETURN");
			System.out.println("Ergebnis: " + func.getExportParameterList().getValue("RETURN")); 

			//System.out.println(sapNr);
			//SAP Nummer in Datenbank schreiben
			//String query1 = "UPDATE kunde set SAP_KId = " + sapNr + " WHERE Email = \"" + Kunde1.getEmail() +"\";";

			//Query ausf�hren


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

	
	private boolean kundeIstNeu(/*String email*/)		//E-Mail des zu pr�fenden Kunden
	{
		try{
			
			//Abfragen ob ein Ziel(Das SAP System vorhanden ist)
			JCoDestination dest = JCoDestinationManager.getDestination("");
			//Repository holen
			JCoRepository repo = dest.getRepository();
			
			
			
	
		JCoFunction func = repo.getFunction("BAPI_CUSTOMER_SEARCH1");
		

		func.getImportParameterList().setValue("PI_E_MAIL","Max.Musterman@gmail.de");		//E-Mail des Kunden	->	sp�ter durch den �bergabeparameter email ersetzen
		
		func.getImportParameterList().setValue("PI_SALESORG","DN00");				//Verkaufsorganisation

		//Ausf�hren der Funktionen mit anschlie�enden Commit
		JCoContext.begin(dest);
		func.execute(dest);
		JCoFunction funcCommit = dest.getRepository().getFunction("BAPI_TRANSACTION_COMMIT");
		funcCommit.execute(dest);
		JCoContext.end(dest);
		
		
		
		System.out.println(func.getExportParameterList().getValue("RETURN"));
		System.out.println(func.getExportParameterList().getValue("CUSTOMERNO"));					//Ausgaben nur zum Testen
		System.out.println(func.getExportParameterList().getValue("CUSTOMERNO") == null);
		return func.getExportParameterList().getValue("CUSTOMERNO") == null;						//Gibt true zur�ck, wenn die abfrage ans SAP-System keine Kundennummer zur�ck gibt
		
		
		} catch (JCoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Verbindung konnte nicht aufgebaut werden.");
			return false;
		}
		
	}

}
