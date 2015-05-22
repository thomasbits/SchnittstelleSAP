
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
	
	public boolean createKunde(/*Kunde k*/)
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
	

	
}
