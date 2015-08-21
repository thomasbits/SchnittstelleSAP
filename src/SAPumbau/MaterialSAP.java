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

			//JCoStructure personalData = func.getImportParameterList().getStructure("CATALOG");
			//personalData.setValue("PRODCAT","K01");
			//JCoStructure referenceData = func.getImportParameterList().getStructure("VARIANT");
			//referenceData.setValue("VARIANT", "DN00");

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
			//System.out.println(func.getTableParameterList().getTable("ITEMS"));
			anzahlMat = table.getNumRows();
			for(int i = 0; i<anzahlMat; i++)
			{
				table.setRow(i);
				materialliste.add(table.getString("MATERIAL"));
			}

			for(int i = 0; i<materialliste.size();i++)
			{
				System.out.println(materialliste.get(i));
				//############# Überprüfen ob Neuerungen überhautp vorhanden sind müsste hier eingefügt werden

				//Daten aus SAP System holen und in Class.Material schreiben
				holeMaterialSAP(materialliste.get(i));

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

	
	public void ermittlePreise()
	{

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

			System.out.println(func.getExportParameterList().getValue("RETURN"));
			System.out.println(func.getTableParameterList().getTable("ITEMS"));
			System.out.println(func.getTableParameterList().getTable("TEXTS"));
			System.out.println(func.getTableParameterList().getTable("PRICES"));
			

		}catch (JCoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Verbindung konnte nicht aufgebaut werden.");

		}
	}
	
	
	
	
	public Material holeMaterialSAP(String materialid)
	{
		if (materialWEB == null) {
			//Instanz KundeSAP holen
			materialWEB = ablaufsteuerung.getInstanceMaterialWEB();
		}
		
		material = new Material();
		
		try {

			//Abfragen ob ein Ziel(Das SAP System vorhanden ist)
			JCoDestination dest = JCoDestinationManager.getDestination("");
			//Repository holen
			JCoRepository repo = dest.getRepository();
			//BAPI auswählen
			JCoFunction func = repo.getFunction("BAPI_MATERIAL_GET_ALL");
			//Import Parameter festlegen
			func.getImportParameterList().setValue("MATERIAL","M01");
			//Daten an das SAP System übergeben
			JCoContext.begin(dest);
			func.execute(dest);
			JCoFunction funcCommit = dest.getRepository().getFunction("BAPI_TRANSACTION_COMMIT");
			funcCommit.execute(dest);
			JCoContext.end(dest);

			//Material Bezeichnung filtern
			JCoTable table = func.getTableParameterList().getTable("MATERIALDESCRIPTION");
			String bezeichnung = func.getTableParameterList().getTable("MATERIALDESCRIPTION").getString("MATL_DESC");

			//Material  Grunddatentext / Materiallongtext
			table = func.getTableParameterList().getTable("MATERIALLONGTEXT");
	
			String beschreibung = table.getString("TEXT_LINE");
		
			
			
			//PiD
			material.setmID(materialid);	

			//Artikel des Tages

			//Beschreibung
			material.setBeschreibung(beschreibung);
			//bauart

			//Preis

			//Stand

			//Farbe

			//Bezeichnung
			material.setBezeichnung(bezeichnung);
			
			//Verfuegbare Menge
			
			//geloescht
			
			//produktkategorie
			
			//preis_alt
			
			//groesse
			
			//Bauvariante
			
			//Marke
			
			//Eigenschaften
			
			
		
			

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		
		return material;
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

			System.out.println(func1.getTableParameterList().getTable("I_CDHDR"));


			//boolean ret = materialWEB.datensatzAbfrage("2");
			//System.out.println("Ergebnis1: "+ String.valueOf(ret));


		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
