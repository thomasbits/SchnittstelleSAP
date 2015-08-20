package SAPumbau;

import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoStructure;

public class Produktpreise {

	public Produktpreise() {
		// TODO Auto-generated constructor stub
	}


	public void ermittlePreise()
	{

		//Unvollständigkeitsprotokoll unter v.01 und v.02		
		try {
			//Abfragen ob ein Ziel(Das SAP System vorhanden ist)
			JCoDestination dest = JCoDestinationManager.getDestination("");
			//Repository holen
			JCoRepository repo = dest.getRepository();
			//BAPI auswählen
			
			//Materialpreise
			JCoFunction func = repo.getFunction("BAPI_ADV_MED_GET_ITEMS");

			func.getImportParameterList().setValue("CATALOG", "K01");
			func.getImportParameterList().setValue("VARIANT", "01");
			func.getImportParameterList().setValue("WITH_PRICES", "X");
			
			//Daten an das SAP System übergeben
			JCoContext.begin(dest);
			func.execute(dest);
			JCoFunction funcCommit = dest.getRepository().getFunction("BAPI_TRANSACTION_COMMIT");
			funcCommit.execute(dest);
			JCoContext.end(dest);

			System.out.println(func.getExportParameterList().getValue("RETURN"));
			System.out.println(func.getTableParameterList().getTable("ITEMS").getValue("MATERIAL"));
			System.out.println(func.getTableParameterList().getTable("TEXTS").getValue("NAME"));
			System.out.println(func.getTableParameterList().getTable("TEXTS").getValue("TITLE"));
			System.out.println(func.getTableParameterList().getTable("PRICES").getValue("CONDVAL"));
			
			
			
			
			//Matarialbeschreibung
			JCoFunction func2 = repo.getFunction("BAPI_ADV_MED_GET_LAYOBJ_DESCR");
			func2.getImportParameterList().setValue("CATALOG", "K01");
			func2.getImportParameterList().setValue("VARIANT", "01");
			func2.getImportParameterList().setValue("AREA", "1");
			func2.getImportParameterList().setValue("ITEM", "1");
			
			//Daten an das SAP System übergeben
			JCoContext.begin(dest);
			func2.execute(dest);
			JCoFunction funcCommit2 = dest.getRepository().getFunction("BAPI_TRANSACTION_COMMIT");
			funcCommit2.execute(dest);
			JCoContext.end(dest);			
			
			//Auslesen der Materialbeschreibung des Produnktkataloges
			System.out.println(func2.getTableParameterList().getTable("LINES").getValue("LINE"));
			

		}catch (JCoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Verbindung konnte nicht aufgebaut werden.");

		}
	}
}
