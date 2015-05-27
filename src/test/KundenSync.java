package test;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;



public class KundenSync extends Thread{

	private java.sql.Statement stmt;
	
	public KundenSync() {
		// TODO Auto-generated constructor stub
		DatenbankVerbindung dbconnection = new DatenbankVerbindung();
		 stmt = dbconnection.getStatement();
	}
	
	
	
	public void test()
	{
	
		try {
			ResultSet results = stmt.executeQuery("SELECT * FROM kunde WHERE SAP_KId IS NULL;");
			while (results.next()) 
			{
				String vorname = results.getString("vorname");
				String name = results.getString("name");
				String kid = results.getString("KId");
				String PLZ = results.getString("PLZ");
				String ort = results.getString("Ort");
				String email = results.getString("Email");
				String gebdat = results.getString("Geburtsdatum");
				System.out.println("Mein Name:" + vorname + " " +gebdat );
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	public void run()
	{
		
		
		
		
	}

}
