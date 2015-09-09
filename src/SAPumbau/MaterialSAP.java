package SAPumbau;

import java.util.ArrayList;
import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoTable;

/**
 * Die Klasse MaterialSAP beinhaltet Methoden zur Synchronisierung von Materialen/Produkten auf der SAP Seite.
 * @author  Robin
 */
public class MaterialSAP {

	private Report report = new Report(this.getClass().toString());
	Material material;
	Ablaufsteuerung_Material ablaufsteuerung;
	MaterialWEB materialWEB;
	private int anzahlMat;
	private ArrayList<String> materialliste = new ArrayList<String>();
	private String itemNr;

	/**
	 * Konstruktor zum entgegennehmen der Instanz von Ablaufsteuerung_Material
	 * @param ablaufsteuerung
	 */
	public MaterialSAP(Ablaufsteuerung_Material ablaufsteuerung) {
		this.ablaufsteuerung = ablaufsteuerung;
	}

	/**
	 * Holt eine Liste aller Materialien aus dem SAP System. Anhand eines Produktkataloges.
	 */
	public void materialListeHolen()
	{
		if (materialWEB == null) {
			//Instanz KundeSAP holen
			materialWEB = ablaufsteuerung.getInstanceMaterialWEB();
		}

		try {
			//Abfragen ob ein Ziel(Das SAP System vorhanden ist)
			JCoDestination dest = JCoDestinationManager.getDestination("");
			//Repository holen
			JCoRepository repo = dest.getRepository();

			//BAPI auswählen
			JCoFunction func = repo.getFunction("BAPI_ADV_MED_GET_ITEMS");
			//Import Parameter festlegen
			func.getImportParameterList().setValue("CATALOG","K01");
			func.getImportParameterList().setValue("VARIANT","01");

			//Daten an das SAP System übergeben
			JCoContext.begin(dest);
			func.execute(dest);

			JCoFunction funcCommit = dest.getRepository().getFunction("BAPI_TRANSACTION_COMMIT");
			funcCommit.execute(dest);
			JCoContext.end(dest);

			//Materialliste holen:
			materialliste.clear();
			JCoTable table = func.getTableParameterList().getTable("ITEMS");
			anzahlMat = table.getNumRows();
			String materialtext = "Materialien: ";
			for(int i = 0; i<anzahlMat; i++)
			{
				table.setRow(i);
				materialliste.add(table.getString("MATERIAL"));
				materialtext = materialtext + table.getString("MATERIAL");
			}
			report.set(materialtext);
			for(int i = 0; i<materialliste.size();i++)
			{
				//############# Überprüfen ob Neuerungen überhautp vorhanden sind müsste hier eingefügt werden

				itemNr = holeItemNummer(materialliste.get(i));
				//Daten aus SAP System holen und in Class.Material schreiben
				holeMaterialSAP_Verteiler(materialliste.get(i));

				boolean rueck = materialWEB.datensatzAbfrage(materialliste.get(i));

				if(rueck)
				{
					material.setPreisAlt(materialWEB.holeMaterialpreisAlt(material));
					materialWEB.materialAktualisieren(material);
				}
				else
				{
					materialWEB.materialAnlegen(material);
				}
				//Überflüssige Datensätze im Webshop ermitteln
				materialWEB.DatenbankSortieren(materialliste);
			}
		} catch (JCoException e) {
			report.set(e.toString());
		}
	}

	/**
	 * Methode die das holen der einzelnen Informationen für ein Material aufteilt und auf weitere Methoden verteilt.
	 * @param materialid
	 * @return
	 */
	public Material holeMaterialSAP_Verteiler(String materialid)
	{
		if (materialWEB == null) {
			//Instanz KundeSAP holen
			materialWEB = ablaufsteuerung.getInstanceMaterialWEB();
		}
		material = new Material();

		//PiD
		material.setmID(materialid);	

		//Beschreibung, Bezeichnung

		holeMaterialSAP_Beschreibung_Bezeichnung(materialid,material);

		//Artikel des Tages, bauart, Farbe, groesse, bauvariante, marke, Eigenschaften

		holeMaterialSAP_PK(materialid, material);

		//Preis
		holeMaterialSAP_Preise(materialid, material);

		return material;
	}

	/**
	 * Methode die die Materialbeschreibung und Bezeichnung aus dem SAP System holt
	 * @param materialid Übergabe der Material ID
	 * @param material Übergabe der Getter/Setter Klasse material
	 */
	public void holeMaterialSAP_Beschreibung_Bezeichnung(String materialid, Material material)
	{

		//Hier wird folgendes aus dem SAP System geholt:
		//Bezeichnung, Beschreibung
		try {

			//Abfragen ob ein Ziel(Das SAP System vorhanden ist)
			JCoDestination dest = JCoDestinationManager.getDestination("");
			//Repository holen
			JCoRepository repo = dest.getRepository();

			//BAPI auswählen
			JCoFunction func = repo.getFunction("BAPI_MATERIAL_GET_ALL");

			//Import Parameter festlegen
			func.getImportParameterList().setValue("MATERIAL",materialid);

			//Daten an das SAP System übergeben
			JCoContext.begin(dest);
			func.execute(dest);
			JCoFunction funcCommit = dest.getRepository().getFunction("BAPI_TRANSACTION_COMMIT");
			funcCommit.execute(dest);
			JCoContext.end(dest);

			//Material Bezeichnung filtern
			JCoTable table = func.getTableParameterList().getTable("MATERIALDESCRIPTION");
			String bezeichnung = func.getTableParameterList().getTable("MATERIALDESCRIPTION").getString("MATL_DESC");

			material.setBezeichnung(bezeichnung);

			//Material  Grunddatentext / Materiallongtext
			table = func.getTableParameterList().getTable("MATERIALLONGTEXT");

			int anzahlSpalten = table.getNumRows();
			String beschreibung = "";
			for (int i = 0;i<anzahlSpalten; i++)
			{

				beschreibung = beschreibung + table.getString("TEXT_LINE");
				table.nextRow();
			}

			material.setBeschreibung(beschreibung);

		} catch (Exception e) {
			report.set("holeMaterialSAP_Beschreibung_Bezeichnung: " + e.toString());
		}


	}

	/**
	 * Methode die Eigenschaften aus den Texten des Produktkataloges je Material/Produkt holt
	 * @param materialid Übergabe der Material ID
	 * @param material Übergabe der Getter/Setter Klasse material
	 */
	public void holeMaterialSAP_PK(String materialid, Material material)
	{
		//Hier wird folgendes aus dem SAP System geholt:
		//Artikel des Tages, bauart, Farbe, grosse, bauvariante, marke, eigenschaften

		try {

			//Abfragen ob ein Ziel(Das SAP System vorhanden ist)
			JCoDestination dest = JCoDestinationManager.getDestination("");
			//Repository holen
			JCoRepository repo = dest.getRepository();

			//Matarialbeschreibung
			JCoFunction func2 = repo.getFunction("BAPI_ADV_MED_GET_LAYOBJ_DESCR");
			func2.getImportParameterList().setValue("CATALOG", "K01");
			func2.getImportParameterList().setValue("VARIANT", "01");
			func2.getImportParameterList().setValue("AREA", "1");
			func2.getImportParameterList().setValue("ITEM", itemNr);


			//Daten an das SAP System übergeben
			JCoContext.begin(dest);
			func2.execute(dest);
			JCoFunction funcCommit2 = dest.getRepository().getFunction("BAPI_TRANSACTION_COMMIT");
			funcCommit2.execute(dest);
			JCoContext.end(dest);			

			//Auslesen der Materialbeschreibung des Produnktkataloges
			JCoTable table1 = func2.getTableParameterList().getTable("LINES");

			if(table1.getNumRows() != 0)
			{
				String ergeb = "";
				table1.firstRow();
				for(int i = 0; i<table1.getNumRows();i++ )
				{
					ergeb = ergeb + table1.getValue("LINE").toString();
					table1.nextRow();
				}

				String[] new1 = ergeb.split("\n");


				int laenge = (new1.length-1);

				//Material des tages
				if(laenge>=0)
				{
					material.setAdt(new1[0]);
				}

				//Bauart
				if(laenge>=1)
				{
					material.setBauart(new1[1]);
				}

				//Farbe
				if(laenge>=2)
				{
					material.setFarbe(new1[2]);
				}

				//Größe

				if(laenge>=3)
				{
					material.setGroesse(new1[3]);
				}

				//Bauvariante
				if(laenge>=4)
				{

					material.setBauvariante(new1[4]);
				}

				//Marke
				if(laenge>=5)
				{
					material.setMarke(new1[5]);
				}

				//Eigenschaften
				String eigenschaften = "";


				for(int i = 6; i<(new1.length);i++) {
					if(i > 6)
					{
						eigenschaften = eigenschaften + new1[i] + ";";
					}
				}
				eigenschaften = eigenschaften.substring(0, eigenschaften.length());
				material.setEigenschaften(eigenschaften);
			}
		} catch (Exception e) {
			report.set("holeMaterialSAP_PK: " + e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * Methode zum holen der ITEM Nummer aus dem Produktkatalog (ITEM Nummer bezieht sich auf die Reihenfolge im Produktkatalog)
	 * Wird benötigt um spezielle Zeilen aus der Rückgabe aus dem Produktkatalog zu verwenden
	 * @param materialid Übergabe der Materialid
	 * @return Gibt die ITEM Nummer zurück
	 */
	public String holeItemNummer(String materialid)
	{
		String ret = null;
		for(int i = 0; i<materialliste.size();i++)
		{
			if(materialliste.get(i).equals(materialid))
			{
				ret = String.valueOf(i+1);
			}
		}
		return ret;
	}

	/**
	 * Methode zum holen der Preise aus dem SAP System über den Produktkatalog
	 * @param materialid Übergabe der Material ID
	 * @param material Übergabe der Getter/Setter Klasse material
	 */
	public void holeMaterialSAP_Preise(String materialid, Material material)
	{

		itemNr = holeItemNummer(materialid);
		//Unvollständigkeitsprotokoll unter v.01 und v.02		
		try {
			//Abfragen ob ein Ziel(Das SAP System vorhanden ist)
			JCoDestination dest = JCoDestinationManager.getDestination("");
			//Repository holen
			JCoRepository repo = dest.getRepository();
			//BAPI auswählen
			JCoFunction func = repo.getFunction("BAPI_ADV_MED_GET_ITEMS");

			func.getImportParameterList().setValue("CATALOG", "K01");
			func.getImportParameterList().setValue("VARIANT", "01");
			func.getImportParameterList().setValue("WITH_PRICES", "X");

			//Daten an das SAP System übergeben
			JCoContext.begin(dest);
			func.execute(dest);
			JCoFunction funcCommit = dest.getRepository().getFunction("BAPI_TRANSACTION_COMMIT");
			funcCommit.execute(dest);
			JCoContext.end(dest);

			//Material Bezeichnung filtern
			JCoTable table = func.getTableParameterList().getTable("PRICES");
			int anzahlRow = table.getNumRows();
			ArrayList<Float> preis = new ArrayList<Float>();

			for(int i = 0; i<=anzahlRow;i++)
			{
				preis.add(Float.valueOf(table.getString("CONDVAL")));
				table.nextRow();
			}

			material.setPreis(preis.get(Integer.valueOf(itemNr)-1));
		}catch (JCoException e) {
			report.set(e.toString());
		}
	}
}
