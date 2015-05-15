import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;

public class NeuerKundeSAP {

	public NeuerKundeSAP() {
		// TODO Auto-generated constructor stub
	}

	private boolean createKunde(Kunde k)
	{
		
		JCoDestination dest;
		try {
			dest = JCoDestinationManager.getDestination("");
			
			JCoRepository repo = dest.getRepository();
			
			JCoFunction func = repo.getFunction("BAPI_CUSTOMER_CREATEFROMDATA1");
			
			JCoStructure personalData = func.getImportParameterList().getStructure("PI_PERSONALDATA");
			personalData.setValue("", "");
			
			
			
		} catch (JCoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
	}
	
}
