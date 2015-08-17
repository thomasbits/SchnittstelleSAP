package SAPumbau;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
/*
 * Repräsentiert einen Kundenauftrag mit den zugehörigen Methoden
 */

/**
 * Enthält alle Attribute eines Kundenauftrages, dient zum Austausch der Daten zwischen KundenauftragSAP und KundenauftragWEB
 * @author Thomas
 */
public class Kundenauftrag {

	private String versandart, zahlungsart, bestellNRSAP, bestellNRWEB, verkaufsbelegart, partnerrolle, debitorennummer, status;		//nicht alle notwendig weil immer gleich??
	private Date bestellDatum;
	private Hashtable<String, String> position;	


	public Kundenauftrag() {
		position = new Hashtable<String, String>();
	}


	public String getZahlungsart() {
		return zahlungsart;
	}

	public String getVersandart() {
		return versandart;
	}

	public void setVersandart(String versandart) {
		this.versandart = versandart;
	}

	public void setZahlungsart(String zahlungsart) {
		this.zahlungsart = zahlungsart;
	}


	public Date getBestellDatum() {
		return bestellDatum;
	}

	public void setBestellDatum(String bestellDatum) {
		// TODO String auf Date Casten!!
		this.bestellDatum = bestellDatum;
	}

	public Hashtable<String, String> getPosition() {

		return position;
	}

	public void setPosition(String produkt, String menge) {
		this.position.put(produkt, menge);
	}

	public String getBestellNRSAP() {
		return bestellNRSAP;
	}

	public void setBestellNRSAP(String bestellNRSAP) {
		this.bestellNRSAP = bestellNRSAP;
	}

	public String getBestellNRWEB() {
		return bestellNRWEB;
	}

	public void setBestellNRWEB(String bestellNRWEB) {
		this.bestellNRWEB = bestellNRWEB;
	}

	public String getDebitorennummer() {
		return debitorennummer;
	}

	public void setDebitorennummer(String debitorennummer) {
		this.debitorennummer = debitorennummer;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
