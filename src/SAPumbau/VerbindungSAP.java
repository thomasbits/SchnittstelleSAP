package SAPumbau;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;

//Klasse für die SAP Verbindung
public class VerbindungSAP {

	Provider des;
	
	public VerbindungSAP() {
	//Konstruktor
	// Neue Instanz des Providers (Verbindungsdaten)
		des = new Provider();
	}

	private JCoDestination dest;
	public void connect() {
		// TODO Auto-generated method stub

		//Logindaten setzen
		des.setLoginData("JCO_BITS", "init1234"); 
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

