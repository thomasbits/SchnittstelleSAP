package SAPumbau;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * Klasse Datenbankverbindung: Stellt die Verbindung zur Datenbank des Webshops her.
 */

public class DatenbankVerbindung {

	public static Statement stmt;

	private static Connection conn = null;

	// Hostname
	private static String dbHost = "127.0.0.1";

	// Port -- Standard: 3306
	private static String dbPort = "3306";

	// Datenbankname
	private static String database = "webshop";

	// Datenbankuser
	private static String dbUser = "root";

	// Datenbankpasswort
	private static String dbPassword = "test1234";

	public DatenbankVerbindung() {
		try {
			//Hallo
			// Datenbanktreiber für ODBC Schnittstellen laden.
			// Für verschiedene ODBC-Datenbanken muss dieser Treiber
			// nur einmal geladen werden.
			Class.forName("com.mysql.jdbc.Driver");

			// Verbindung zur ODBC-Datenbank 'sakila' herstellen.
			// Es wird die JDBC-ODBC-Brücke verwendet.
			conn = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":"
					+ dbPort + "/" + database + "?" + "user=" + dbUser + "&"
					+ "password=" + dbPassword);

			stmt = conn.createStatement();
		} catch (ClassNotFoundException e) {
			System.out.println("Treiber nicht gefunden");
			System.out.println(e);
		} catch (SQLException e) {
			System.out.println("Connect nicht moeglich");
			System.out.println(e);
		}
	}

	public static Connection getInstance()
	{
		if(conn == null)
			new DatenbankVerbindung();
		return conn;
	}

	public static Statement getStatement()
	{
		return stmt;
	}

	public void schliesseVerbindung()
	{
		try 
		{
			stmt.close();
			conn.close();
		}
		catch (SQLException sqle) 
		{
			System.out.println(sqle.toString());
		}
	}
}