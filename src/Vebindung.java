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

			
				Provider des = new Provider(); 
				des.setLoginData("GBI-612", "tho123"); 
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

			



