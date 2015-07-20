package SAPumbau;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

/**
 *	Stellt die ben�tigten Methoden bereit, um einen Kundenauftrag im SAP-System anzulegen und den aktuellen Status abzufragen
 * @author Thomas
 */
public class KundenauftragSAP {
	
	int i = 0;
	

	public KundenauftragSAP() {
	}

/**
 * 	Legt den �bergebnenen Kundenauftrag im SAP-System an
 * @param auftrag Datensatz eines Kundenauftrages, der im SAP-System erstellt werden soll
 */
	public void createKundenauftrag(Kundenauftrag auftrag)
	{
		
		try {
			//Abfragen ob ein Ziel(Das SAP System vorhanden ist)
			JCoDestination dest = JCoDestinationManager.getDestination("");
			//Repository holen
			JCoRepository repo = dest.getRepository();
			//BAPI ausw�hlen
			JCoFunction func = repo.getFunction("BAPI_SALESORDER_CREATEFROMDAT2");	//Transaktion VA01
			
			//Pflichtfeld
			JCoStructure header = func.getImportParameterList().getStructure("ORDER_HEADER_IN");
			header.setValue("DOC_TYPE", "TA");			//Verkaufsbelegart: hier Termninauftrag
			header.setValue("SALES_ORG", "DN00");		//Verkaufsorganisation
			header.setValue("DISTR_CHAN", "IN");		//Vertriebsweg
			header.setValue("DIVISION", "BI");			//Sparte
			header.setValue("PURCH_DATE", new Date());
			
			
			JCoTable partner = func.getTableParameterList().getTable("ORDER_PARTNERS");
			partner.appendRow();
			partner.setValue("PARTN_ROLE", "AG");				//Partnerrolle: hier AuftragGeber
			partner.setValue("PARTN_NUMB", "0000025026");		//Debitorennummer
			
			//Nur zum testen
			JCoTable items = func.getTableParameterList().getTable("ORDER_ITEMS_IN");
			items.appendRow();
			items.setValue("ITM_NUMBER", "1");							//Verkaufsbelegposition
			items.setValue("MATERIAL", "M02");							//Materialnummer
			items.setValue("TARGET_QTY", "2");							//Zielmenge in Verkaufsmengeneinheit
			
			
			JCoTable shedules = func.getTableParameterList().getTable("ORDER_SCHEDULES_IN");
			shedules.appendRow();	
			shedules.setValue("ITM_NUMBER", "1");				//Verkaufsbelegposition
			shedules.setValue("SCHED_LINE", "1");				//Einteilungsnummer
			shedules.setValue("REQ_QTY", "2");					//Auftragsmenge des Kunden in VME
			
			/*
			Iterator iterator = auftrag.getPosition().entrySet().iterator();
			
			while(iterator.hasNext())
			{	
				Map.Entry e = (Map.Entry)iterator.next();
				
				JCoTable items = func.getTableParameterList().getTable("ORDER_ITEMS_IN");
				items.appendRow();
				items.setValue("ITM_NUMBER", i);									//Verkaufsbelegposition
				items.setValue("MATERIAL", e.getKey());								//Materialnummer
				items.setValue("TARGET_QTY", e.getValue());							//Zielmenge in Verkaufsmengeneinheit
			
			
				JCoTable shedules = func.getTableParameterList().getTable("ORDER_SCHEDULES_IN");
				shedules.appendRow();	
				shedules.setValue("ITM_NUMBER", i);							//Verkaufsbelegposition
				shedules.setValue("SCHED_LINE", "1");						//Einteilungsnummer
				shedules.setValue("REQ_QTY", e.getValue());					//Auftragsmenge des Kunden in VME
				
				System.out.println(e.getKey());
				System.out.println(e.getValue());
				
				i++;
			}
			*/
			func.getImportParameterList().setValue("TESTRUN", "");		//"X" zum Testen sonst "" oder auskommentieren
			
			
			//Funktion ausf�hren und commiten
			JCoContext.begin(dest);
			func.execute(dest);
			JCoFunction funcCommit = dest.getRepository().getFunction("BAPI_TRANSACTION_COMMIT");
			funcCommit.execute(dest);
			JCoContext.end(dest);
			
			
			System.out.println(func.getTableParameterList().getTable("RETURN"));
			
			
			System.out.println("Salesdocument: " + func.getExportParameterList().getValue("SALESDOCUMENT"));
			
			
			

		} catch (JCoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Verbindung konnte nicht aufgebaut werden.");

		}
		
	}
/**
 * Fragt den aktuellen Status eines Kundenauftrages im SAP-System ab
 * @param bestellNRSAP Auftragsnummer, des Auftrages, dessen Status abgefragt werden soll
 * @return Gibt den aktuellen Status in Form eines Strings zur�ck
 */
	/*
	 * Funktionalit�t
Mit dieser Methode k�nnen Sie sich zu einem bestimmten Kundenauftrag Informationen hinsichtlich der Verf�gbarkeitssituation, des Bearbeitungsstatus (z.B. Lieferstatus) und der 
Preise beschaffen.Dazu m�ssen Sie �ber den Parameter SALESDOCUMENT die entsprechende Belegnummer angeben. Sie erhalten dann �ber die Struktur STATUSINFO die oben geannten 
Detailinformationen. Eventuell aufgetretene Fehler werden �ber den Parameter RETURN zur�ckgegeben.
	 */
	public String getStatus(String bestellNRSAP)
	{
		// TODO Auftragsstatus abfragen
		try {
			//Abfragen ob ein Ziel(Das SAP System vorhanden ist)
			JCoDestination dest = JCoDestinationManager.getDestination("");
			//Repository holen
			JCoRepository repo = dest.getRepository();
			//BAPI ausw�hlen
			JCoFunction func = repo.getFunction("BAPI_SALESORDER_GETSTATUS");	//Transaktion
			
			func.getImportParameterList().setValue("SALESDOCUMENT", "0000000002");	//Auftragsnummer des Auftrages
		

			
			//Funktion ausf�hren und commiten
			JCoContext.begin(dest);
			func.execute(dest);
			JCoFunction funcCommit = dest.getRepository().getFunction("BAPI_TRANSACTION_COMMIT");
			funcCommit.execute(dest);
			JCoContext.end(dest);
			
			JCoTable statusinfo = func.getTableParameterList().getTable("STATUSINFO");
			
			if (statusinfo.isEmpty())
			{
				System.out.println("Auftrag nicht vorhanden!");
			}else
			{
				System.out.println("Doc_Date: " + func.getTableParameterList().getTable("STATUSINFO").getValue("DOC_DATE"));
			
				System.out.println("Doc_Number: " + func.getTableParameterList().getTable("STATUSINFO").getValue("DOC_NUMBER"));	
			
				System.out.println("Gesamtstatus: " + func.getTableParameterList().getTable("STATUSINFO").getValue("PRC_STAT_H"));
//			A     Not yet processed
//			B     Partially processed
//			C     Completely processed
			
			
				System.out.println("Lieferstatus: " + func.getTableParameterList().getTable("STATUSINFO").getValue("DLV_STAT_H"));
			//unvollst�ndig 
			}
			
			System.out.println(func.getExportParameterList().getValue("RETURN"));
			
			
		} catch (JCoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Verbindung konnte nicht aufgebaut werden.");

		}
		
		String status ="";
		
		return status;
	}
}