package SAPumbau;

import java.text.SimpleDateFormat;
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

	private String versandart, zahlungsart, bestellNRSAP, bestellNRWEB, verkaufsbelegart, partnerrolle, debitorennummer, status, bestellDatum;		//nicht alle notwendig weil immer gleich??

	private String lierferadresse_geschlecht, lierferadresse_vorname, lierferadresse_name, lierferadresse_strasse, lierferadresse_hausnummer, lierferadresse_plz, lierferadresse_ort;
	private Hashtable<String, String> position;	

	public void ausgabeKundenauftrag(){
		System.out.println("Versandart: " + versandart + "\n" + "Zahlungsart: " + zahlungsart + "\n" + "BestellNR-SAP: " + bestellNRSAP + "\n" + "BestellNR-WEB: " + bestellNRWEB + "\n" + "Debitorennummer: " + debitorennummer + "\n" + "Status: " + status + "\n" + "Datum: " + bestellDatum + "\n" + "Liefer_Geschlecht: " + lierferadresse_geschlecht + "\n" + "Liefer_Vorname: " + lierferadresse_vorname + "\n" + "Liefer_Name: " + lierferadresse_name + "\n" + "Liefer_Straße: " + lierferadresse_strasse + "\n" + "Liefer_Hausnummer: " + lierferadresse_hausnummer + "\n" + "Liefer_PLZ: " + lierferadresse_plz + "\n" + "Liefer_Ort: " + lierferadresse_ort);
	}

	public Kundenauftrag() {
		position = new Hashtable<String, String>();
	}


	public String getLierferadresse_geschlecht() {
		return lierferadresse_geschlecht;
	}


	public void setLierferadresse_geschlecht(String lierferadresse_geschlecht) {
		this.lierferadresse_geschlecht = lierferadresse_geschlecht;
	}


	public String getLierferadresse_vorname() {
		return lierferadresse_vorname;
	}


	public void setLierferadresse_vorname(String lierferadresse_vorname) {
		this.lierferadresse_vorname = lierferadresse_vorname;
	}


	public String getLierferadresse_name() {
		return lierferadresse_name;
	}


	public void setLierferadresse_name(String lierferadresse_name) {
		this.lierferadresse_name = lierferadresse_name;
	}


	public String getLierferadresse_strasse() {
		return lierferadresse_strasse;
	}


	public void setLierferadresse_strasse(String lierferadresse_strasse) {
		this.lierferadresse_strasse = lierferadresse_strasse;
	}


	public String getLierferadresse_hausnummer() {
		return lierferadresse_hausnummer;
	}


	public void setLierferadresse_hausnummer(String lierferadresse_hausnummer) {
		this.lierferadresse_hausnummer = lierferadresse_hausnummer;
	}


	public String getLierferadresse_plz() {
		return lierferadresse_plz;
	}


	public void setLierferadresse_plz(String lierferadresse_plz) {
		this.lierferadresse_plz = lierferadresse_plz;
	}


	public String getLierferadresse_ort() {
		return lierferadresse_ort;
	}


	public void setLierferadresse_ort(String lierferadresse_ort) {
		this.lierferadresse_ort = lierferadresse_ort;
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


	public String getBestellDatum() {
		return bestellDatum;
	}

	public void setBestellDatum(String bestellDatum) {		//Format im SAP-System:  JJJJMMDD
		
		this.bestellDatum = bestellDatum.replace("-", "");
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
