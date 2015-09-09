package SAPumbau;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import com.mysql.jdbc.Statement;

/**
 *	Stellt die Methoden zur Verbindung zur Webshopdatenbank bereit.
 * @author Thomas und Robin
 */
public class DatenbankVerbindung {

	private Report report = new Report(this.getClass().toString());

	private static Connection conn = null;

	// Hostname
	private static String dbHost;
	// Port -- Standard: 3306
	private static String dbPort;
	// Datenbankname
	private static String database;
	// Datenbankuser
	private static String dbUser;
	// Datenbankpasswort
	private static String dbPassword;

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
			System.out.println("error");
		} catch (SQLException e) {
			report.set("Connect nicht moeglich");
			report.set(e.toString());
			System.out.println("Datenbankenverbindung verloren. Das Programm wird beendet. Bitte neu starten!");
			System.exit(0);
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

	/**
	 * Mit dieser Methode kann gestestet werden ob die Verbindung zur Datenbank noch steht
	 * Wenn keine Verbindung zur Datenbank besteht wird das Programm beendet
	 */
	public void isDbConnected() {
		final String CHECK_SQL_QUERY = "SELECT 1";
		boolean isConnected = false;
		try {
			Statement statement = (Statement) conn.prepareStatement(CHECK_SQL_QUERY);
			isConnected = true;
		} catch (SQLException | NullPointerException e) {
			// handle SQL error here!
			System.out.println("\nDatenbankverbindung verloren! Das Programm wurde beendet. Bitte neustarten!");
			System.exit(0);
		}
	}
}