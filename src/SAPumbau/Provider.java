package SAPumbau;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;

/*
 * Stellt die Daten für eine Verbindung bereit
 */

/**
 * Stellt die Daten für die Verbindung zum SAP-System bereit, enthält außerdem die Methode zum setzen der Logindaten.
 * @author Thomas
 */
public class Provider implements DestinationDataProvider{

	private Report report = new Report(this.getClass().toString()); //Report instanziieren (Logger)
	private String JCO_ashost;
	private String JCO_r3name;		
	private String JCO_sysnr;
	private String JCO_client;
	private String JCO_lang;
	private String JCO_user;
	private String JCO_passwd;
	private final Properties properties;

	/**
	 * Im Konstruktor werden die Properties gesetzt die vorher über getConnectionProperties() aus einer extra Datei geholt werden
	 */
	public Provider() {
		getConnectionProperties();

		properties = new Properties();
		properties.setProperty(DestinationDataProvider.JCO_ASHOST, JCO_ashost);
		properties.setProperty(DestinationDataProvider.JCO_R3NAME, JCO_r3name);
		properties.setProperty(DestinationDataProvider.JCO_SYSNR, JCO_sysnr);
		properties.setProperty(DestinationDataProvider.JCO_CLIENT, JCO_client);
		properties.setProperty(DestinationDataProvider.JCO_LANG,JCO_lang);
	}

	/**
	 * getConnectionProperties holt die Logindaten aus einer Extra Datei
	 */
	private void getConnectionProperties()
	{
		File propertiesFile = new File("./resources/connection.properties");
		Properties properties = new Properties();

		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(propertiesFile))) {
			properties.load(bis);
		} catch (Exception ex) {
			report.set(ex.toString());
		}
		JCO_ashost = properties.getProperty("JCO_ashost");
		JCO_r3name = properties.getProperty("JCO_r3name");
		JCO_sysnr = properties.getProperty("JCO_sysnr");
		JCO_client = properties.getProperty("JCO_client");
		JCO_lang = properties.getProperty("JCO_lang");
		JCO_user = properties.getProperty("JCO_user");
		JCO_passwd = properties.getProperty("JCO_passwd");
	}

	/**
	 * Setzen der Logindaten
	 */
	public void setLoginData()
	{
		properties.setProperty(DestinationDataProvider.JCO_USER, JCO_user);
		properties.setProperty(DestinationDataProvider.JCO_PASSWD, JCO_passwd);
	}

	@Override
	public Properties getDestinationProperties(String destinationName) {
		// TODO Auto-generated method stub
		return properties;
	}

	@Override
	public void setDestinationDataEventListener(
			DestinationDataEventListener eventListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean supportsEvents() {
		// TODO Auto-generated method stub
		return false;
	}
}
