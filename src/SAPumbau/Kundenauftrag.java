package SAPumbau;

import java.util.Hashtable;
/*
 * Repräsentiert einen Kundenauftrag. 
 */
public class Kundenauftrag {
	
	private String bestellNRSAP, bestellNRWEB, verkaufsbelegart, partnerrolle, debitorennummer, status;		//nicht alle notwendig weil immer gleich??
	private Hashtable<String, String> position;	//

	public Kundenauftrag() {
		
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
