package SAPumbau;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MaterialWEB {

	Ablaufsteuerung ablaufsteuerung;
	MaterialSAP materialSAP;
	MaterialWEB materialWEB;
	//Material material;
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
			System.out.println(id);
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
	
	public void materialAnlegen(Material material)
	{
		//SAP Nummer in Datenbank schreiben
		//String query1 = "INSERT INTO produkte(`PId`, `Artikel des Tages`, `Beschreibung`, `bauart`, `Preis`, `Stand`, `Farbe`, `Bezeichnung`, `Verfuegbare Menge`, `Menge Besuche`, `geloescht`, `produktkategorie`, `preis_alt`, `groesse`, `bauvariante`, `marke`, `Eigenschaften`) VALUES ('"+material.getmID()+"','"+material.getAdt()+"','"+material.getBeschreibung()+"',1,"+material.getPreis()+","+material.getStand()+",'"+material.getFarbe()+"','"+material.getBezeichnung()+"',"+material.getvMenge()+",0,'nein',1,NULL,"+material.getGroesse()+",'"+material.getBauvariante()+"','"+material.getMarke()+"','"+material.getEigenschaften()+"');";
		String query1 = "INSERT INTO produkte VALUES ('"+material.getmID()+"','"+material.getAdt()+"','"+material.getBeschreibung()+"',1,"+material.getPreis()+","+material.getStand()+",'"+material.getFarbe()+"','"+material.getBezeichnung()+"',"+material.getvMenge()+",0,'nein',1,NULL,"+material.getGroesse()+",'"+material.getBauvariante()+"','"+material.getMarke()+"','"+material.getEigenschaften()+"');";
		
		
		System.out.println(query1);
		//Query ausführen
		try {
			stmt.execute(query1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void materialUeberschreiben(Material material)
	{
		
	}




}
