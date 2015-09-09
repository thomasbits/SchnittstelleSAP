package SAPumbau;

import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoStructure;

/**
 *	Die Klasse KundeSAP stellt Methoden bereit, um Kunden im SAP-System zu Erstellen und zu Ändern
 * @author Thomas
 */
public class KundeSAP {

	private Report report = new Report(this.getClass().toString());
	private Ablaufsteuerung_Kunde ablaufsteuerung;
	private KundeWEB kundeWEB;

	/**
	 * Konstruktor 
	 * @param ablaufsteuerung
	 */
	public KundeSAP(Ablaufsteuerung_Kunde ablaufsteuerung) {
		// TODO Auto-generated constructor stub
		this.ablaufsteuerung = ablaufsteuerung;
	}						  

	/**
	 * Erstellt den übergebenen Kunden im SAP-System und schreibt die zugeteilte SAP-Numer in die WebDB
	 * @param kunde1 Kunde der im SAP-System erstellt werden soll
	 */
	public void createKunde(Kunde kunde1)
	{
		if (kundeWEB == null) {
			//Instanz KundeWEB holen
			kundeWEB = ablaufsteuerung.getInstanceKundeWEB();
		}

		try {
			//Abfragen ob ein Ziel(Das SAP System vorhanden ist)
			JCoDestination dest = JCoDestinationManager.getDestination("");
			//Repository holen
			JCoRepository repo = dest.getRepository();
			//BAPI auswählen
			JCoFunction func = repo.getFunction("BAPI_CUSTOMER_CREATEFROMDATA1");
			//Import Parameter festlegen
			JCoStructure personalData = func.getImportParameterList().getStructure("PI_PERSONALDATA");
			personalData.setValue("TITLE_P",kunde1.getTitel());		
			personalData.setValue("FIRSTNAME", kunde1.getVorname());
			personalData.setValue("LASTNAME", kunde1.getName());	
			personalData.setValue("DATE_BIRTH", kunde1.getGeburtstdatum());	
			personalData.setValue("CITY", kunde1.getOrt());
			personalData.setValue("POSTL_COD1", kunde1.getPLZ());
			personalData.setValue("STREET",kunde1.getStrasse());
			personalData.setValue("HOUSE_NO",kunde1.getHausNr());
			personalData.setValue("E_MAIL", kunde1.getEmail());
			personalData.setValue("LANGU_P","DE");
			personalData.setValue("CURRENCY","EUR");
			personalData.setValue("COUNTRY","DE");

			//Referezkunde, wird für das anlegen eines neuen Kunden benötigt
			JCoStructure referenceData = func.getImportParameterList().getStructure("PI_COPYREFERENCE");
			referenceData.setValue("SALESORG", "DN00");
			referenceData.setValue("DISTR_CHAN", "IN");
			referenceData.setValue("DIVISION", "BI");
			referenceData.setValue("REF_CUSTMR", "0000025075");

			//Daten an das SAP System übergeben
			JCoContext.begin(dest);
			func.execute(dest);
			JCoFunction funcCommit = dest.getRepository().getFunction("BAPI_TRANSACTION_COMMIT");
			funcCommit.execute(dest);
			JCoContext.end(dest);

			//Rückgabewert engegennehmen (SAP Kundennummer/Debitor)
			String sapNr = (String) func.getExportParameterList().getValue("CUSTOMERNO");
			kunde1.setSapNummer(sapNr);

		} catch (JCoException e) {
			report.set("Verbindung zum SAP System konnte nicht aufgebaut werden.");
			report.set(e.toString());
		}
	}

	/**
	 * Ändert die Daten des übergebenen Kunden im SAP-System
	 * @param kunde1 Kunde der im SAP-System geändert werden soll
	 * @return Returns true, wenn das ändern Erfolgreich war
	 */
	public boolean changeKunde(Kunde kunde1)
	{
		try{
			//Abfragen ob ein Ziel(Das SAP System vorhanden ist)
			JCoDestination dest = JCoDestinationManager.getDestination("");
			//Repository holen
			JCoRepository repo = dest.getRepository();
			//BAPI auswählen
			JCoFunction func = repo.getFunction("BAPI_CUSTOMER_CHANGEFROMDATA1");

			//Kundennummer, dessen Daten geändert werden sollen
			func.getImportParameterList().setValue("CUSTOMERNO",kunde1.getSapNummer());

			//die Daten, die geändert werden sollen
			JCoStructure changeData = func.getImportParameterList().getStructure("PI_PERSONALDATA");
			changeData.setValue("TITLE_P",kunde1.getTitel()); 
			changeData.setValue("FIRSTNAME",kunde1.getVorname());
			changeData.setValue("LASTNAME",kunde1.getName());
			changeData.setValue("DATE_BIRTH",kunde1.getGeburtstdatum());
			changeData.setValue("CITY",kunde1.getOrt());
			changeData.setValue("POSTL_COD1",kunde1.getPLZ());				
			changeData.setValue("STREET",kunde1.getStrasse());
			changeData.setValue("HOUSE_NO",kunde1.getHausNr());
			changeData.setValue("E_MAIL",kunde1.getEmail());
			changeData.setValue("COUNTRY","DE");
			changeData.setValue("CURRENCY","EUR");
			changeData.setValue("LANGU_P","DE");

			//Flag, welches bei den Werten, die geändert werden sollen, gesetzt werden muss
			JCoStructure changeFlag = func.getImportParameterList().getStructure("PI_PERSONALDATAX");
			changeFlag.setValue("TITLE_P","X");
			changeFlag.setValue("FIRSTNAME","X");
			changeFlag.setValue("LASTNAME","X");
			changeFlag.setValue("DATE_BIRTH","X");
			changeFlag.setValue("CITY","X");
			changeFlag.setValue("POSTL_COD1","X");
			changeFlag.setValue("STREET","X");
			changeFlag.setValue("HOUSE_NO","X");
			changeFlag.setValue("E_MAIL","X");
			changeFlag.setValue("COUNTRY", "X");
			changeFlag.setValue("CURRENCY", "X");
			changeFlag.setValue("LANGU_P", "X");

			func.getImportParameterList().setValue("PI_SALESORG", "DN00");
			func.getImportParameterList().setValue("PI_DISTR_CHAN", "IN");
			func.getImportParameterList().setValue("PI_DIVISION", "BI");


			//Daten an das SAP System übergeben
			JCoContext.begin(dest);
			func.execute(dest);
			JCoFunction funcCommit = dest.getRepository().getFunction("BAPI_TRANSACTION_COMMIT");
			funcCommit.execute(dest);
			JCoContext.end(dest);

			report.set(func.getExportParameterList().getValue("RETURN").toString());

		}catch (JCoException e) {
			report.set("Verbindung konnte nicht aufgebaut werden.");
			report.set(e.toString());
			return false;
		}
		return true;
	}
}
