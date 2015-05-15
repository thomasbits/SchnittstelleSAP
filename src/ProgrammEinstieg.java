import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;

//import de.hsowl.sap.einfuehrung.model.JCoDestinationProvider;


public class ProgrammEinstieg {

	public ProgrammEinstieg() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hallo Welt");
		
		ProgrammEinstieg einstieg = new ProgrammEinstieg();
		boolean help = einstieg.login();
		System.out.println(help);
	}

	
	public boolean login()
	{
		
		
		MyDestinationDataProvider provider = new MyDestinationDataProvider(); 

		
		
		if(com.sap.conn.jco.ext.Environment.isDestinationDataProviderRegistered())
		{
			com.sap.conn.jco.ext.Environment.unregisterDestinationDataProvider(provider);
		}
		com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(provider);
		try
		{
			JCoDestinationManager.getDestination("").ping();
			return true;
		}
		catch (JCoException e)
		{
			e.printStackTrace();
			
			return false;
		}
		
	}
}
