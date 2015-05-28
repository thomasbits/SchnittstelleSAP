package robin1;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoStructure;

public class ProduktKatalogSAP {

	public ProduktKatalogSAP() {
		// TODO Auto-generated constructor stub
	}

	public void getProduktKatalog(/*KatalogNr*/)
	{
		try{
			
		
		JCoDestination dest = JCoDestinationManager.getDestination("");
		
		JCoRepository repo = dest.getRepository();
		
		JCoFunction func = repo.getFunction("BAPI_PRODCAT_GETITEM");
		
		func.getImportParameterList().setValue("CATALOG", "");	//Katalognummer
		func.getImportParameterList().setValue("VARIANT", "");	//Variante, bestimmt Währung usw.
		
		JCoStructure area = func.getImportParameterList().getStructure("AREA");
		area.setValue("AREA", "");		//Layoutbereichsnummer
		area.setValue("PARENT", "");	//übergeordneter Layoutbereich
		area.setValue("CHILD", "");		//relative Ordnungsnummer
		area.setValue("LEAF", "");		//
		area.setValue("", "");
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		}catch(JCoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Verbindung konnte nicht aufgebaut werden.");
		}
	}
}
