package robin1;

import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;

//Klasse f�r die SAP Verbindung
public class VerbindungSAP {

	public VerbindungSAP() {
	//Konstruktor
	}

	public void connect() {
		// TODO Auto-generated method stub
		// Neue Instanz des Providers (Verbindungsdaten)
		Provider des = new Provider();
		//Logindaten setzen
		des.setLoginData("JCO_BITS", "init123"); 
		//Abfragen ob der DestinationDataProvider registriert ist, wenn nicht wird neu registiert
		if(com.sap.conn.jco.ext.Environment.isDestinationDataProviderRegistered())
		{
			com.sap.conn.jco.ext.Environment.unregisterDestinationDataProvider(des);
		}
		com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(des);

		//Testen der Verbindung
		try
		{
			JCoDestinationManager.getDestination("").ping();
			//System.out.println("Erfolgreich verbunden");
		}
		catch (JCoException e)
		{
			e.printStackTrace();
			System.out.println("Keine erfolgreiche Verbindung zum SAP System!");
		}
	}
}





