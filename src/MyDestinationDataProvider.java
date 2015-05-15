import java.util.Properties;

import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;


public class MyDestinationDataProvider implements DestinationDataProvider
{
	private final Properties properties;
	public MyDestinationDataProvider()
	{
		properties = new Properties();
		properties.setProperty(DestinationDataProvider.JCO_ASHOST,"/H/remote.hcc.uni-magdeburg.de/S/3299/H/R52Z ");
		properties.setProperty(DestinationDataProvider.JCO_R3NAME, "R40");
		properties.setProperty(DestinationDataProvider.JCO_SYSNR, "52");
		properties.setProperty(DestinationDataProvider.JCO_CLIENT, "204");
		properties.setProperty(DestinationDataProvider.JCO_LANG, "de");
		properties.setProperty(DestinationDataProvider.JCO_USER, "JCO_BITS");
		properties.setProperty(DestinationDataProvider.JCO_PASSWD, "init123");
	}
	
	public Properties getDestinationProperties( String destinationName )
	{
		return properties;
	}
	public void setDestinationDataEventListener( DestinationDataEventListener eventListener )
	{
	}
	public boolean supportsEvents()
	{
		return false;
	}
}