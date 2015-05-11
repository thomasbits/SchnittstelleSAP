import java.util.logging.Level;

import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sun.media.jfxmedia.logging.Logger;


public class Vebindung {

	public Vebindung() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		JCoDestination dest;
		//try {
			//dest = JCoDestinationManager.getDestination("DES1");
			Provider prov = new Provider();
			prov.setLoginData("GBI-612", "tho123");
			com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(prov);
			/*dest = JCoDestinationManager.getDestination("");
			JCoFunction function = dest.getRepository().getFunction("BAPI_SALESORDER_CREATEFROMDAT2");
			if(function == null)
			{
				Logger.logMsg(1, "Funktionsbaustein nicht vorhanden");
				throw new RuntimeException("Funktionsbaustein nicht vorhanden");
			}*/
			System.out.println("test");
		/*} catch (JCoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
			
	}

}
