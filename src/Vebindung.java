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
		
		
		
		
			Provider prov = new Provider();
			prov.setLoginData("GBI-612", "tho123");
			com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(prov);
		
			
			
			try
			{
				JCoDestinationManager.getDestination("").ping();
				
			}
			catch (JCoException e)
			{
				e.printStackTrace();
				System.out.println("test");
			}
			
	}

}
