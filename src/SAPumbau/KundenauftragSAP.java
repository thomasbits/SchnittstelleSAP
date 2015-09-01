package SAPumbau;

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


/**
 *	Stellt die benötigten Methoden bereit, um einen Kundenauftrag im SAP-System anzulegen und den aktuellen Status abzufragen
 * @author Thomas
 */
public class KundenauftragSAP {

	private Report report = new Report(this.getClass().toString());
	int i = 0;
	private Ablaufsteuerung_Kundenauftrag ablaufsteuerung;
	private KundenauftragWEB auftragWEB;


	public KundenauftragSAP(Ablaufsteuerung_Kundenauftrag ablaufsteuerung) {
		this.ablaufsteuerung = ablaufsteuerung;

	}

	/**
	 * 	Legt den übergebnenen Kundenauftrag im SAP-System an
	 * @param auftrag Datensatz eines Kundenauftrages, der im SAP-System erstellt werden soll
	 */
	public void createKundenauftrag(Kundenauftrag auftrag)				//!!!!!!!!!!!!!PN00 fehlt!!!!!!!!!!!!!!
	{

		if (auftragWEB == null) {
			//Instanz KundeWEB holen
			auftragWEB = ablaufsteuerung.getInstanceKundenauftragWEB();
		}

		//Unvollständigkeitsprotokoll unter v.01 und v.02		
		try {
			//Abfragen ob ein Ziel(Das SAP System vorhanden ist)
			JCoDestination dest = JCoDestinationManager.getDestination("");
			//Repository holen
			JCoRepository repo = dest.getRepository();
			//BAPI auswählen
			JCoFunction func = repo.getFunction("BAPI_SALESORDER_CREATEFROMDAT2");	//Transaktion VA01

			/*			
			//Testdaten
			JCoStructure header = func.getImportParameterList().getStructure("ORDER_HEADER_IN");
			header.setValue("DOC_TYPE", "TA");			//Verkaufsbelegart: hier Termninauftrag
			header.setValue("SALES_ORG", "DN00");		//Verkaufsorganisation
			header.setValue("DISTR_CHAN", "IN");		//Vertriebsweg
			header.setValue("DIVISION", "BI");			//Sparte
			header.setValue("PURCH_DATE", new Date());	//Bestelldatum, in WS DB
			header.setValue("PURCH_NO_C", "01");		//Bestellnummer des Kunden
			header.setValue("PYMT_METH",  "");			//Zahlungsmethode, in WS DB
//			header.setValue("INCOTERMS1", "CIP");
//			header.setValue("INCOTERMS2", "Höxter");
			 */			


			JCoStructure header = func.getImportParameterList().getStructure("ORDER_HEADER_IN");
			header.setValue("DOC_TYPE", "TA");									//Verkaufsbelegart: hier Termninauftrag
			header.setValue("SALES_ORG", "DN00");								//Verkaufsorganisation
			header.setValue("DISTR_CHAN", "IN");								//Vertriebsweg
			header.setValue("DIVISION", "BI");									//Sparte
			header.setValue("PURCH_DATE", auftrag.getBestellDatum());			//Bestelldatum, in WS DB
			header.setValue("PURCH_NO_C", "01");								//Bestellnummer des Kunden
			//			header.setValue("PYMT_METH", auftrag.getZahlungsart());				//Zahlungsmethode, in WS DB
			header.setValue("PMNTTRMS", "0001");								//Zahlungsbedingungen
			header.setValue("INCOTERMS1", "CIP");
			header.setValue("INCOTERMS2", "Höxter");

			JCoTable partner = func.getTableParameterList().getTable("ORDER_PARTNERS");
			partner.appendRow();
			partner.setValue("PARTN_ROLE", "AG");				//Partnerrolle: hier AuftragGeber. WE: Warenempfänger
			partner.setValue("PARTN_NUMB", "0000025076");		//Debitorennummer		Muss noch aus der instanz Auftrag ausgelesen werden!!!!

			/*			
			//Nur zum testen
			JCoTable items = func.getTableParameterList().getTable("ORDER_ITEMS_IN");
			items.appendRow();
			items.firstRow();
			items.setValue("ITM_NUMBER", "1");							//Verkaufsbelegposition
			items.setValue("MATERIAL", "M02");							//Materialnummer
//			items.setValue("TARGET_QTY", "2");							//Zielmenge in Verkaufsmengeneinheit
//			items.setValue("TARGET_QU", "ST");

			JCoTable itemsx = func.getTableParameterList().getTable("ORDER_ITEMS_INX");
			itemsx.appendRow();
			itemsx.firstRow();
			itemsx.setValue("ITM_NUMBER", "1");
			itemsx.setValue("MATERIAL", "X");
			itemsx.setValue("UPDATEFLAG", "X");


			JCoTable shedules = func.getTableParameterList().getTable("ORDER_SCHEDULES_IN");
			shedules.appendRow();	
			shedules.firstRow();
			shedules.setValue("ITM_NUMBER", "1");				//Verkaufsbelegposition
			shedules.setValue("SCHED_LINE", "1");				//Einteilungsnummer
			shedules.setValue("REQ_QTY", "2");					//Auftragsmenge des Kunden in VME
			shedules.setValue("REQ_DATE", new Date());

			JCoTable shedulesx = func.getTableParameterList().getTable("ORDER_SCHEDULES_INX");
			shedulesx.appendRow();	
			shedulesx.firstRow();
			shedulesx.setValue("ITM_NUMBER", "1");				//Verkaufsbelegposition
			shedulesx.setValue("SCHED_LINE", "1");				//Einteilungsnummer
			shedulesx.setValue("REQ_QTY", "X");					//Auftragsmenge des Kunden in VME
			shedulesx.setValue("REQ_DATE", "X");
			shedulesx.setValue("UPDATEFLAG", "X");
			 */			

			Iterator iterator = auftrag.getPosition().entrySet().iterator();

			while(iterator.hasNext())
			{	
				Map.Entry e = (Map.Entry)iterator.next();

				JCoTable items = func.getTableParameterList().getTable("ORDER_ITEMS_IN");
				items.appendRow();
				items.setValue("ITM_NUMBER", i);									//Verkaufsbelegposition
				items.setValue("MATERIAL", e.getKey());								//Materialnummer
				items.setValue("TARGET_QTY", e.getValue());							//Zielmenge in Verkaufsmengeneinheit
				items.setValue("TAX_CLASS1", "19");

				JCoTable shedules = func.getTableParameterList().getTable("ORDER_SCHEDULES_IN");
				shedules.appendRow();	
				shedules.setValue("ITM_NUMBER", i);							//Verkaufsbelegposition
				shedules.setValue("SCHED_LINE", "1");						//Einteilungsnummer
				shedules.setValue("REQ_QTY", e.getValue());					//Auftragsmenge des Kunden in VME

				//				System.out.println(e.getKey());
				//				System.out.println(e.getValue());

				i++;
			}

			//			func.getImportParameterList().setValue("TESTRUN", "X");		//"X" zum Testen sonst "" oder auskommentieren





			//Funktion ausführen und commiten
			JCoContext.begin(dest);
			func.execute(dest);
			JCoFunction funcCommit = dest.getRepository().getFunction("BAPI_TRANSACTION_COMMIT");
			funcCommit.execute(dest);
			JCoContext.end(dest);
			
			
//			System.out.println(func.getTableParameterList().getTable("RETURN"));

			report.set(func.getTableParameterList().getTable("RETURN").toString());
			report.set("Kundenauftrag angelegt: " + func.getExportParameterList().getValue("SALESDOCUMENT"));

			try{
				//Fehler, weil aktuell keine Auftrgsnummer zurückgegeben wird
				auftragWEB.setSAPNr( Integer.valueOf((String) func.getExportParameterList().getValue("SALESDOCUMENT")), Integer.valueOf(auftrag.getBestellNRWEB()));

			} catch (NumberFormatException e) {

			}

		} catch (JCoException e) {
			report.set(e.toString());
		}

	}
	/**
	 * Fragt den aktuellen Status eines Kundenauftrages im SAP-System ab
	 * @param bestellNRSAP Auftragsnummer, des Auftrages, dessen Status abgefragt werden soll
	 * @return Gibt den aktuellen Status in Form eines Strings zurück
	 */
	public void getStatus(String bestellNRSAP)
	{
		String statusSAP="";

		if (auftragWEB == null) {
			//Instanz KundeWEB holen
			auftragWEB = ablaufsteuerung.getInstanceKundenauftragWEB();
		}
		String status = "";



		// TODO Auftragsstatus abfragen
		try {
			//Abfragen ob ein Ziel(Das SAP System vorhanden ist)
			JCoDestination dest = JCoDestinationManager.getDestination("");
			//Repository holen
			JCoRepository repo = dest.getRepository();
			//BAPI auswählen
			JCoFunction func = repo.getFunction("BAPI_SALESORDER_GETSTATUS");	//Transaktion

			func.getImportParameterList().setValue("SALESDOCUMENT", bestellNRSAP);	//Auftragsnummer des Auftrages


			//Funktion ausführen und commiten
			JCoContext.begin(dest);
			func.execute(dest);
			JCoFunction funcCommit = dest.getRepository().getFunction("BAPI_TRANSACTION_COMMIT");
			funcCommit.execute(dest);
			JCoContext.end(dest);

			JCoTable statusinfo = func.getTableParameterList().getTable("STATUSINFO");

			if (statusinfo.isEmpty())
			{
				report.set("Auftrag " + bestellNRSAP + " nicht vorhanden!");
			}else
			{
				statusSAP = (String) func.getTableParameterList().getTable("STATUSINFO").getValue("PRC_STAT_H");

				if(statusSAP.equals("A"))
				{
					status = "Auftrag wird geprüft";
				}else if(statusSAP.equals("B"))
				{
					status = "Auftrag in Bearbeitung";
				}else if(statusSAP.equals("C"))
				{
					status = "Auftrag abgeschlossen";
				}else		//Was dann???
				{
					status = "Status unbekannt";
				}

				auftragWEB.setAuftragsStatus(bestellNRSAP, status);

			}






		} catch (JCoException e) {
			report.set(e.toString());
		}
	}
}
