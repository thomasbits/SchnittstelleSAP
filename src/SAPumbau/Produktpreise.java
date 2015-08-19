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
			System.out.println(func.getTableParameterList().getTable("ITEMS"));
			System.out.println(func.getTableParameterList().getTable("TEXTS"));
			System.out.println(func.getTableParameterList().getTable("PRICES"));
			

		}catch (JCoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Verbindung konnte nicht aufgebaut werden.");

		}
	}
}
