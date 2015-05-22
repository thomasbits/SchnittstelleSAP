package robin;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;


public class VerbindungSAP {

	public VerbindungSAP() {
		// TODO Auto-generated constructor stub
	}

	public void connect() {
		// TODO Auto-generated method stub

		System.out.println("Hallo Welt");
		Provider des = new Provider(); 
		//des.setLoginData("GBI-612", "tho123"); 
		des.setLoginData("JCO_BITS", "init123"); 
		if(com.sap.conn.jco.ext.Environment.isDestinationDataProviderRegistered())
		{
			com.sap.conn.jco.ext.Environment.unregisterDestinationDataProvider(des);
		}
		com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(des);


		try
		{
			JCoDestinationManager.getDestination("").ping();
			System.out.println("Erfolg");
		}
		catch (JCoException e)
		{
			e.printStackTrace();
			System.out.println("Kein Erfolg");
		}
	}
}





