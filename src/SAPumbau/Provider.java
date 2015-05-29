package SAPumbau;

import java.util.Properties;

import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;


//Stellt die Daten für eine Verbindung bereit
public class Provider implements DestinationDataProvider{

	private String JCO_ASHOST;
	private String JCO_SYSNR;		
	private String JCO_CLIENT;
	
	private String JCO_USER;
	private String JCO_PASSWD;
	private String JCO_LANG;
	
	private final Properties properties;
	
	public Provider() {
		// TODO Auto-generated constructor stub
		
		properties = new Properties();
		properties.setProperty(DestinationDataProvider.JCO_ASHOST, "/H/remote.hcc.uni-magdeburg.de/S/3299/H/R52Z");
		properties.setProperty(DestinationDataProvider.JCO_R3NAME, "R52");
		properties.setProperty(DestinationDataProvider.JCO_SYSNR, "52");
		properties.setProperty(DestinationDataProvider.JCO_CLIENT, "204");
		properties.setProperty(DestinationDataProvider.JCO_LANG, "de");
		
	}

	public void setLoginData(String username, String pwd)
	{
		properties.setProperty(DestinationDataProvider.JCO_USER, username);
		properties.setProperty(DestinationDataProvider.JCO_PASSWD, pwd);
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
