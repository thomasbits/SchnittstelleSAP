package SAPumbau;

import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;
/*
 * Schreibt die Daten zu einem Kundenauftrag in das SAP-System.
 * Holt den aktuellen Stand eines Kundenauftrages aus dem SAP-System um ihn an den Webshop weiterzuleiten
 */
public class KundenauftragSAP {

	public KundenauftragSAP() {
		// TODO Auto-generated constructor stub
	}

	public void createKundenauftrag(Kundenauftrag auftrag)
	{
		try {
			//Abfragen ob ein Ziel(Das SAP System vorhanden ist)
			JCoDestination dest = JCoDestinationManager.getDestination("");
			//Repository holen
			JCoRepository repo = dest.getRepository();
			//BAPI auswählen
			JCoFunction func = repo.getFunction("BAPI_SALESORDER_CREATEFROMDAT2");	//Transaktion VA01
			
			//Pflichtfeld
			JCoStructure header = func.getImportParameterList().getStructure("ORDER_HEADER_IN");
			header.setValue("DOC_TYPE", "TA");			//Verkaufsbelegart: hier Termninauftrag
			header.setValue("SALES_ORG", "DN00");		//Verkaufsorganisation
			header.setValue("DISTR_CHAN", "IN");		//Vertriebsweg
			header.setValue("DIVISION", "BI");			//Sparte
			
			JCoTable partner = func.getTableParameterList().getTable("ORDER_PARTNERS");
			partner.appendRow();
			partner.setValue("PARTN_ROLE", "AG");								//Partnerrolle: hier AuftragGeber
			partner.setValue("PARTN_NUMB", "25009"/*k.getKundennummer()*/);		//Debitorennummer
			
			
		
			//Daten aus Klasse Kunden übernehmen
			//muss in eine Schleife um alle Werte setzen zu können
			JCoTable items = func.getTableParameterList().getTable("ORDER_ITEMS_IN");
			items.appendRow();
			items.setValue("ITM_NUMBER", "1");								//Verkaufsbelegposition
			items.setValue("MATERIAL", ""	/*k.getMaterialnummer()*/);		//Materialnummer
			items.setValue("TARGET_QTY", ""	/*k.getMenge()*/);				//Zielmenge in Verkaufsmengeneinheit
			
			
			JCoTable shedules = func.getTableParameterList().getTable("ORDER_SCHEDULES_IN");
			shedules.appendRow();	
			shedules.setValue("ITM_NUMBER", "1");						//Verkaufsbelegposition
			shedules.setValue("SCHED_LINE", "1");						//Einteilungsnummer
			shedules.setValue("REQ_QTY", ""	/*k.getMenge()*/);			//Auftragsmenge des Kunden in VME
			
			
			func.getImportParameterList().setValue("TESTRUN", "X");		//"X" zum Testen sonst "" oder auskommentieren
			
			
			//Funktion ausführen und commiten
			JCoContext.begin(dest);
			func.execute(dest);
			JCoFunction funcCommit = dest.getRepository().getFunction("BAPI_TRANSACTION_COMMIT");
			funcCommit.execute(dest);
			JCoContext.end(dest);
			
			
			
			System.out.println(func.getTableParameterList().getTable("RETURN"));
			

		} catch (JCoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Verbindung konnte nicht aufgebaut werden.");

		}
	}
}
