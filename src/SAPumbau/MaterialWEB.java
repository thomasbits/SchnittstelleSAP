package SAPumbau;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MaterialWEB {

	Ablaufsteuerung ablaufsteuerung;
	MaterialSAP materialSAP;
	MaterialWEB materialWEB;
	Material material;
	java.sql.Statement stmt;
	


	public MaterialWEB(Ablaufsteuerung ablaufsteuerung) {
		// TODO Auto-generated constructor stub
		this.ablaufsteuerung = ablaufsteuerung;
	}

	public void setStatement(java.sql.Statement stmt)
	{
		this.stmt = stmt;
	}

	public boolean datensatzAbfrage(String id)
	{
		boolean ret = false;
		
		//Query ob Datensätze ohne SAP Nummer vorhanden sind?
		ResultSet results;
		try {
			results = stmt.executeQuery("SELECT * FROM produkte WHERE PId = '"+id+"';");

			//Abfragen ob Datensatz leer ist
			if (!results.next())
			{
				ret = false;
			}
			else
			{
				ret = true;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}





}
