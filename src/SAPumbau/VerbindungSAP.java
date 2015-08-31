package SAPumbau;

import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;


/**
 * Stellt die Methoden bereit, um eine Verbindung zum SAP-System aufzubauen
 * @author Thomas und Robin
 */
public class VerbindungSAP {

	private Report report = new Report(this.getClass().toString());
	private Provider des;
	
	/**
	 * Konstruktor Erstellt eine neue Instanz der Klasse Provider
	 */
	public VerbindungSAP() {
		des = new Provider();
	}
	/**
	 * Stellt eine Verbindung mit dem SAP-System her
	 */
	public void connect() {

		//Logindaten setzen
		des.setLoginData(); 
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
			report.set("Verbindung mit dem SAP-System hergestellt");
		}
		catch (JCoException e)
		{
			e.printStackTrace();
			System.out.println("Keine Verbindung zum SAP System!");
		}
	}
}

