package SAPumbau;

import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JTable;
import com.sap.conn.jco.JCo;
import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;

/**
 * @author Thomas and Robin
 *
 */

public class MaterialSAP {

	Material material;
	Ablaufsteuerung ablaufsteuerung;
	MaterialWEB materialWEB;
	private int anzahlMat;
	private ArrayList<String> materialliste = new ArrayList<String>();
	private String itemNr;

	public MaterialSAP(Ablaufsteuerung ablaufsteuerung) {
		// TODO Auto-generated constructor stub
		this.ablaufsteuerung = ablaufsteuerung;
	}

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

			//Rückgabewert engegennehmen (SAP Kundennummer/Debitor)


			//Materialliste holen:
			materialliste.clear();
			JCoTable table = func.getTableParameterList().getTable("ITEMS");
			anzahlMat = table.getNumRows();
			for(int i = 0; i<anzahlMat; i++)
			{
				table.setRow(i);
				materialliste.add(table.getString("MATERIAL"));
			}

			for(int i = 0; i<materialliste.size();i++)
			{
				//System.out.println(materialliste.get(i));
				//############# Überprüfen ob Neuerungen überhautp vorhanden sind müsste hier eingefügt werden

				itemNr = holeItemNummer(materialliste.get(i));
				//Daten aus SAP System holen und in Class.Material schreiben
				holeMaterialSAP_Verteiler(materialliste.get(i));

				boolean rueck = materialWEB.datensatzAbfrage(materialliste.get(i));

				if(rueck)
				{
					//überschreiben

				}
				else
				{
					materialWEB.materialAnlegen(material);
				}


			}

		} catch (JCoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

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

		//Stand
		//Fällt weg

		//Verfuegbare Menge

		//geloescht

		//produktkategorie

		//preis_alt


		return material;
	}

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

			String beschreibung = table.getString("TEXT_LINE");

			material.setBeschreibung(beschreibung);

		} catch (Exception e) {
			// TODO: handle exception
		}


	}

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

			
			int rows = func2.getTableParameterList().getTable("LINES").getNumRows();
			

			if(rows != 0)
			{
				System.out.println("Rows: " + rows );
				String ergeb = func2.getTableParameterList().getTable("LINES").getValue("LINE").toString();
				String[] new1 = ergeb.split("\n");


				int laenge = (new1.length -1);

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

				for(int i = 6; i<new1.length;i++) {
					if(i > 6)
					{
						eigenschaften = eigenschaften + ", ";
					}

					eigenschaften = eigenschaften + new1[i];

				}

				material.setEigenschaften(eigenschaften);

			}
			//System.out.println("-- \n"+ func2.getTableParameterList().getTable("LINES").getValue("LINE").toString() +"\n --");



		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

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
			// TODO Auto-generated catch block
			e.printStackTrace();
			//System.out.println("Verbindung konnte nicht aufgebaut werden.");

		}
	}










	public void neuAenderung()
	{
		try {
			//Abfragen ob ein Ziel(Das SAP System vorhanden ist)
			JCoDestination dest = JCoDestinationManager.getDestination("");
			//Repository holen
			JCoRepository repo = dest.getRepository();
			//BAPI auswählen

			JCoFunction func1 = repo.getFunction("CHANGEDOCUMENT_READ_HEADERS");

			func1.getImportParameterList().setValue("OBJECTCLASS","MATERIAL");
			func1.getImportParameterList().setValue("OBJECTID","M*");
			func1.getImportParameterList().setValue("USERNAME","GBI-613");

			/*
			JCoStructure referenceData = func1.getImportParameterList().getStructure("OBJECTCLASS");
			referenceData.setValue("OBJECTCLAS", "MATERIAL");
			JCoStructure referenceData1 = func1.getImportParameterList().getStructure("OBJECTID");
			referenceData1.setValue("OBJECTID", "M*");
			JCoStructure referenceData2 = func1.getImportParameterList().getStructure("USERNAME");
			referenceData2.setValue("USERNAME", "");
			 */
			JCoContext.begin(dest);
			func1.execute(dest);

			JCoFunction funcCommit = dest.getRepository().getFunction("BAPI_TRANSACTION_COMMIT");


			funcCommit.execute(dest);
			JCoContext.end(dest);

			//System.out.println(func1.getTableParameterList().getTable("I_CDHDR"));


			//boolean ret = materialWEB.datensatzAbfrage("2");
			//System.out.println("Ergebnis1: "+ String.valueOf(ret));


		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
