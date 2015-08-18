package SAPumbau;

import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JTable;

import com.sap.conn.jco.JCo;
import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;


/**
 * @author Thomas and Robin
 *
 */
public class MaterialSAP {

	private int anzahlMat;
	private ArrayList<String> Materialliste = new ArrayList<String>();
	public MaterialSAP() {
		// TODO Auto-generated constructor stub
	}

	public void materialListeHolen()
	{
		
		try {
			//Abfragen ob ein Ziel(Das SAP System vorhanden ist)
			JCoDestination dest = JCoDestinationManager.getDestination("");
			//Repository holen
			JCoRepository repo = dest.getRepository();
			//BAPI auswählen
			JCoFunction func = repo.getFunction("BAPI_ADV_MED_GET_ITEMS");
			//Import Parameter festlegen
			func.getImportParameterList().setValue("CATALOG","K01");
			func.getImportParameterList().setValue("VARIANT","01");
			//JCoStructure personalData = func.getImportParameterList().getStructure("CATALOG");
			//personalData.setValue("PRODCAT","K01");
			
			//JCoStructure referenceData = func.getImportParameterList().getStructure("VARIANT");
			//referenceData.setValue("VARIANT", "DN00");
			
			//Daten an das SAP System übergeben
			JCoContext.begin(dest);
			func.execute(dest);

			JCoFunction funcCommit = dest.getRepository().getFunction("BAPI_TRANSACTION_COMMIT");
			funcCommit.execute(dest);
			JCoContext.end(dest);

			//Rückgabewert engegennehmen (SAP Kundennummer/Debitor)
			
			
			//Materialliste holen:
			Materialliste.clear();
			JCoTable table = func.getTableParameterList().getTable("ITEMS");
			anzahlMat = table.getNumRows();
			for(int i = 0; i<anzahlMat; i++)
			{
				table.setRow(i);
				Materialliste.add(table.getString("MATERIAL"));
			}
			
			for(int i = 0; i<Materialliste.size();i++)
			{
				System.out.println(Materialliste.get(i));
			}
			
			
			
			
			
		} catch (JCoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Katalog konnte nicht gefunden werden.");
		}
	}

}
