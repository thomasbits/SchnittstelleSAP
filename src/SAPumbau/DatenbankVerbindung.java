package SAPumbau;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


/**
 *	Stellt die Methoden zur Verbindung zur Webshopdatenbank bereit.
 * @author Thomas und Robin
 */
public class DatenbankVerbindung {

	private Report report = new Report(this.getClass().toString());
	
	private static Connection conn = null;

	// Hostname
	private static String dbHost = "127.0.0.1";

	// Port -- Standard: 3306
	private static String dbPort = "3306";

	// Datenbankname
//	private static String database = "Webshop";					//Server
	private static String database = "webshop";					//Benedikt

	// Datenbankuser
//	private static String dbUser = "webshop";					//Server
	private static String dbUser = "root";						//Benedikt

	// Datenbankpasswort
//	private static String dbPassword = "TestAcc123";			//Server
//	private static String dbPassword = "Dagobert";				//Benedikt
	private static String dbPassword = "test1234";				//lokal

	/**
	 * Konstruktor Lädt den Datenbanktreiber und erstellt eine Verbindung zur Datenbank
	 */
	public DatenbankVerbindung() {
		try {
			// Datenbanktreiber für ODBC Schnittstellen laden.
			// Für verschiedene ODBC-Datenbanken muss dieser Treiber
			// nur einmal geladen werden.
			Class.forName("com.mysql.jdbc.Driver");
			
			//Verbindungsdaten aus der Konfigurationsdatei lesen
			getConnectionProperties();

			// Verbindung zur ODBC-Datenbank 'sakila' herstellen.
			// Es wird die JDBC-ODBC-Brücke verwendet.
			conn = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":"
					+ dbPort + "/" + database + "?" + "user=" + dbUser + "&"
					+ "password=" + dbPassword);

		} catch (ClassNotFoundException e) {
			report.set("Treiber nicht gefunden");
			report.set(e.toString());
		} catch (SQLException e) {
			report.set("Connect nicht moeglich");
			report.set(e.toString());
		}
	}
	/**
	 * Liest die Verbindungs- und Zugangsdaten aus der Konfigurationsdatei
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

		dbHost = properties.getProperty("dbHost");
		dbPort = properties.getProperty("dbPort");
		database = properties.getProperty("database");
		dbUser = properties.getProperty("dbUser");
		dbPassword = properties.getProperty("dbPassword");
		
//		System.out.println(dbHost + dbPort + database + dbUser + dbPassword);			//Zum Testen
		
	}
	/**
	 * Gibt eine Verbindung zur Datenbak zurück
	 * @return
	 */
	public Connection getInstance()
	{
		if(conn == null)
			new DatenbankVerbindung();
		return conn;
	}
	/**
	 * Schließt die Datenbankverbindung
	 */
	public void schliesseVerbindung()
	{
		try 
		{
			conn.close();
		}
		catch (SQLException sqle) 
		{
			report.set(sqle.toString());
		}
	}
}