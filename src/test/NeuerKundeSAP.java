package test;

import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoStructure;

public class NeuerKundeSAP {

	public NeuerKundeSAP() {
		// TODO Auto-generated constructor stub
	}
	
	//Methode wird aufgerufen um einen Kundendatensatz zu speichern.
	//Gibt es bereits einen Datensatz mit der selben E-Mail gibt es die passende Rückmeldung
	public boolean speichereKunden(/*Übergabe des Kunden der erstellt werden soll*/)		
	{
		if (kundeIstNeu("kunde@email.de"/*email des zu erstellenden Kunden*/))
		{
			createKunde();
			return true;
		}else{
			System.out.println("Kunde bereits vorhanden");
			return false;
		}
	}
	
	private boolean createKunde(/*Kunde k*/)
	{
		
		try {
			JCoDestination dest = JCoDestinationManager.getDestination("");
			
			JCoRepository repo = dest.getRepository();
			
			JCoFunction func = repo.getFunction("BAPI_CUSTOMER_CREATEFROMDATA1");
			
			JCoStructure personalData = func.getImportParameterList().getStructure("PI_PERSONALDATA");
			//werte zum testen, sollen später aus der klasse Kunde genommen werden
			personalData.setValue("TITLE_P", "Herr");		//Webshop verwendet hier Geschlecht, muss hier noch geändert werden
			personalData.setValue("FIRSTNAME", "Testuser");
			personalData.setValue("LASTNAME", "Testuser");	
			personalData.setValue("DATE_BIRTH", "19901230");	
			personalData.setValue("CITY", "Bad Driburg");
			personalData.setValue("POSTL_COD1", "33014");
			personalData.setValue("STREET","Musterstrasse");
			personalData.setValue("HOUSE_NO", "5");
			personalData.setValue("E_MAIL", "Max.Musterman@gmail.de");
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
			
			JCoContext.begin(dest);
			
			func.execute(dest);
			JCoFunction funcCommit = dest.getRepository().getFunction("BAPI_TRANSACTION_COMMIT");
			funcCommit.execute(dest);
			JCoContext.end(dest);
			
			System.out.println(func.getExportParameterList().getValue("RETURN"));
			System.out.println(func.getExportParameterList().getValue("CUSTOMERNO"));
			
			
			
			
		} catch (JCoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Verbindung konnte nicht aufgebaut werden.");
			return false;
		}
		
		return true;
	}
	
	private boolean kundeIstNeu(String email)		//E-Mail und Verkaufsorgansisation des zu prüfenden Kunden
	{
		try{
		JCoDestination dest = JCoDestinationManager.getDestination("");
		
		JCoRepository repo = dest.getRepository();
		
		
		JCoFunction func = repo.getFunction("BAPI_CUSTOMER_SEARCH1");
		

		func.getImportParameterList().setValue("PI_E_MAIL","Unique@mail.com");		//E-Mail des Kunden	->	später durch den Übergabeparameter email ersetzen
		
		//Möglicherweise sind nicht alle Pflicht
//		emailData.setValue("E_MAIL", "Max.Musterman@gmail.de");		//E-Mail des zu gesuchten Kunden
//		emailData.setValue("LANGU_P", "");		//Sprachschlüssel
//		emailData.setValue("CURRENCY", "");		//Währung
//		emailData.setValue("TITLE_KEY", "");	//Anredeschlüssel
		
		
		
		func.getImportParameterList().setValue("PI_SALESORG","DN00");		//Verkaufsorganisation ->	später durch den Übergabeparameter verkaufsOrg ersetzen
		//Möglicherweise sind nicht alle pflicht
		//beste wahl wahrscheinlich der Referenzkunde
//		salesOrgData.setValue("SALESORG","");		//Pflicht
//		salesOrgData.setValue("DISTR_CHAN","");			
//		salesOrgData.setValue("DIVISION","");			
//		salesOrgData.setValue("REF_CUSTMR","");
		
		
		//Ausführen der Funktionen mit anschließenden Commit
		JCoContext.begin(dest);
		func.execute(dest);
		JCoFunction funcCommit = dest.getRepository().getFunction("BAPI_TRANSACTION_COMMIT");
		funcCommit.execute(dest);
		JCoContext.end(dest);
		
		
		
		System.out.println(func.getExportParameterList().getValue("RETURN"));
		System.out.println(func.getExportParameterList().getValue("CUSTOMERNO"));					//Ausgaben nur zum Testen
		System.out.println(func.getExportParameterList().getValue("CUSTOMERNO") == null);
		return func.getExportParameterList().getValue("CUSTOMERNO") == null;			//Gibt true zurück, wenn die abfrage ans SAP-System keine Kundennummer zurück gibt
		
		
		} catch (JCoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Verbindung konnte nicht aufgebaut werden.");
			return false;
		}
		
	}
	
/*	//wird nicht benötigt
	public void getKunde()
	{
		try{
			JCoDestination dest = JCoDestinationManager.getDestination("");
			
			JCoRepository repo = dest.getRepository();
			
			JCoFunction func = repo.getFunction("BAPI_CUSTOMER_GETDETAIL2");
			
			
			JCoStructure gesuchterKunde = func.getImportParameterList().getStructure("CUSTOMERNO");
			gesuchterKunde.setValue("CUSTOMER", "");		//Kundennummer
			gesuchterKunde.setValue("COMP_CODE", "");	//Buchungskreis
			gesuchterKunde.setValue("SALES_ORG", "");	//Verkaufsorganisation
			gesuchterKunde.setValue("DISTR_CHAN", "");	//Vertriebsweg
			gesuchterKunde.setValue("DIVISION", "");		//Sparte
			
			JCoStructure gefundenerKunde = func.getExportParameterList().getStructure("CUSTOMERADDRESS");
			//Neuer Kunde wird erstellt und mit den Daten "gefüllt" kann dann anschließend in die SQL Datenbank geschrieben werden
			Kunde ergebnisKunde = new Kunde();
			ergebnisKunde.setName(gefundenerKunde.getValue("NAME").toString());
			ergebnisKunde.setOrt(gefundenerKunde.getValue("CITY").toString());
			
			
			
			
		}catch(JCoException e){
		
		}
		
	}

	*/
}
