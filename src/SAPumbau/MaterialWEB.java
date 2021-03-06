package SAPumbau;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Die Klasse MaterialWEB beinhaltet Methoden zur Synchronisierung von Materialen/Produkten auf der WEBSHOP Datenbank Seite.
 * @author Robin
 */
public class MaterialWEB {

	private Report report = new Report(this.getClass().toString());
	Ablaufsteuerung_Material ablaufsteuerung;
	MaterialSAP materialSAP;
	MaterialWEB materialWEB;
	java.sql.Statement stmt;
	private DatenbankVerbindung verbindung;

	/**
	 * Im Konstruktor der Klasse Material wird die aktuelle Instanz der Ablaufsteuerung_Material �bergeben und eine neue Datenbankverbindung instanziiert
	 * @param ablaufsteuerung
	 */
	public MaterialWEB(Ablaufsteuerung_Material ablaufsteuerung) {
		this.ablaufsteuerung = ablaufsteuerung;
		verbindung = new DatenbankVerbindung();
	}

	/**
	 * Stellt eine neue Verbindung zur WebDB her
	 */
	public void neueVerbindungDB()
	{
		//		verbindung.schliesseVerbindung();
		verbindung = new DatenbankVerbindung();
	}

	/**
	 * Abfrage ob ein Datensatz zur �bergebenen id in der Datenbank steht
	 * @param id id die �bergeben wird
	 * @return Gibt true oder false zur�ck ob ein Datensatz zu der id in der Datenbank vorhanden ist
	 */
	public boolean datensatzAbfrage(String id)
	{
		boolean ret = false;

		//Query ob Datens�tze ohne SAP Nummer vorhanden sind?
		ResultSet results;
		try {
			verbindung.isDbConnected();
			results = verbindung.getInstance().createStatement().executeQuery("SELECT * FROM produkte WHERE PId = '"+id+"';");

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
			report.set(e.toString());
		}
		return ret;
	}

	/**
	 * Methode die zwei Strings im Detial vergleicht
	 * @param a �bergabe String1
	 * @param b �bergabe String2
	 * @return Gibt true oder false zur�ck ob gleich oder ungleich
	 */
	public boolean StringVergleich(String a, String b)
	{
		boolean ergebnis = true;
		char [] achar;
		char [] bchar;

		achar=a.toCharArray();
		bchar=b.toCharArray();

		if(achar.length != bchar.length)
		{
			ergebnis = false;
		}else
		{
			for(int i = 0;i<achar.length;i++)
			{
				if(!(achar[i] == bchar[i]))
				{
					ergebnis = false;
				}
			}
		}
		return ergebnis;
	}

	/**
	 * Methode die in der Webshopdatenbank unter Produkte das attribut geloescht auf ja setzt wenn diese im SAP System nicht vorhanden sind
	 * @param materialliste �bergabe der Liste der Materialien aus dem SAP System
	 */
	public void DatenbankSortieren(ArrayList<String> materialliste)
	{
		try {
			verbindung.isDbConnected();
			//Abfragen aller Produkte
			ResultSet results = verbindung.getInstance().createStatement().executeQuery("SELECT PId FROM `produkte` WHERE 1 AND geloescht = 'nein';");

			results.first();
			while(! results.isAfterLast()) // as long as valid data is in the result set
			{

				boolean stat = false;
				for(int i = 0; i < materialliste.size();i++)
				{
					if(StringVergleich(results.getString("PId"),materialliste.get(i)))
					{
						stat = true;
						i = materialliste.size()+5;
					}
				}
				if(!stat)
				{
					verbindung.isDbConnected();
					report.set("Material in der Datenbank zum l�schen makiert." + results.getString("PId"));
					String query = "UPDATE `produkte` SET geloescht = 'ja' WHERE PId= '"+results.getString("PId")+"';";
					verbindung.getInstance().createStatement().execute(query);

				}
				results.next();
			}
		} catch (SQLException e) {
			report.set(e.toString());
		}
	}

	/**
	 * Aktualisiert ein Material in der Datenbank ausgehend von der �bergebenen Materialinstanz
	 * @param material
	 */
	public void materialAktualisieren(Material material)
	{
		String query1 = "UPDATE `produkte` SET `Artikel des Tages`="+material.getAdt()+",`Beschreibung`="+material.getBeschreibung()+",`bauart`="+material.getBauart()+",`Preis`="+material.getPreis()+",`Farbe`="+material.getFarbe()+",`Bezeichnung`="+material.getBezeichnung()+",`Verfuegbare Menge`="+material.getvMenge()+",`preis_alt`="+material.getPreisAlt()+",`groesse`="+material.getGroesse()+",`bauvariante`="+material.getBauvariante()+",`marke`="+material.getMarke()+",`Eigenschaften`="+material.getEigenschaften()+" WHERE PId = "+material.getmID()+";";
		//Query ausf�hren
		try {
			verbindung.isDbConnected();
			verbindung.getInstance().createStatement().execute(query1);
		} catch (SQLException e) {
			report.set(e.toString());
		}
	}

	/**
	 * Legt ein neues Material in der Datenbank an, ausgehend von der �bergebenen Materialinstanz
	 * @param material
	 */
	public void materialAnlegen(Material material)
	{
		String query1 = "INSERT INTO produkte(`PId`, `Artikel des Tages`, `Beschreibung`, `bauart`, `Preis`, `Stand`, `Farbe`, `Bezeichnung`, `Verfuegbare Menge`, `geloescht`, `produktkategorie`, `preis_alt`, `groesse`, `bauvariante`, `marke`, `Eigenschaften`) VALUES ("+material.getmID()+","+material.getAdt()+","+material.getBeschreibung()+","+material.getBauart()+","+material.getPreis()+",null,"+material.getFarbe()+","+material.getBezeichnung()+","+material.getvMenge()+",'nein',1,NULL,"+material.getGroesse()+","+material.getBauvariante()+","+material.getMarke()+","+material.getEigenschaften()+");";
		//Query ausf�hren
		try {
			verbindung.isDbConnected();
			verbindung.getInstance().createStatement().execute(query1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Methode die den Materialpreis auslie�t
	 * Wird vor dem aktuallisiern eines Datensatzes aufgerufen um den vorheringen Preis zu ermitteln.
	 * @param material
	 * @return
	 */
	public float holeMaterialpreisAlt(Material material)
	{
		float preisAlt = 0;
		try {
			verbindung.isDbConnected();
			//Query ob Datens�tze ohne SAP Nummer vorhanden sind?
			ResultSet results = verbindung.getInstance().createStatement().executeQuery("SELECT Preis FROM produkte WHERE PId = "+material.getmID()+";");
			//Abfragen ob Datensatz leer ist
			if (results.next())
			{
				preisAlt = Float.valueOf(results.getString("Preis"));
			}
		} catch (SQLException e) {
			report.set(e.toString());
		}
		return preisAlt;
	}
}
