package SAPumbau;
import com.sun.jmx.snmp.Timestamp;
/*
 * Repräsentiert ein Material/Produkt, mit den zugehörigen getter und setter Methoden
 */

/**
 *	Enthält alle Attribute eines Materials, wird zum Austausch zwischen MaterialSAP und MaterialWEB benutzt
 *  Getter und Setter Klasse
 * @author Robin
 */



public class Material {

	//In Anführungszeichen stehen die Feldnamen der Datenbank
	private String mID; //"PId"  MaterialID oder ProdukutID
	private boolean adt; //"Artikel des Tages"
	private String beschreibung; //"Beschreibung" Ausführliche Beschreibung
	private int bauart; //"bauart" -> Mountainbike, Crossrad, ...
	private float preis; //"Preis"
	private Timestamp stand; //"Stand" -> Stand der letzten Aktualisierung
	private String farbe; //"Farbe"
	private String bezeichnung; //"Bezeichnung"
	private int vMenge; //Verfuegbare Menge
	private boolean geloescht; //"geloescht"
	private int produktkategorie; //"produktkategorie"
	private float preisAlt; //"preis_alt"
	private int groesse; //"groesse"
	private char bauvariante; //"bauvariante" -> Herrenrad h Damenrad d
	private String marke; //"marke"
	private String eigenschaften; //"Eigenschaften"

	
	public Material() {
		// TODO Auto-generated constructor stub
	}
	

	public String getmID() {
		return mID;
	}
	public void setmID(String mID) {
		this.mID = mID;
	}
	public String getAdt() {
		String ret;
		if(adt)
		{
			ret = "ja";
		}
		else
		{
			ret = "nein";
		}
		return ret;
	}
	public void setAdt(boolean adt) {
		this.adt = adt;
	}
	public String getBeschreibung() {
		return beschreibung;
	}
	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}
	public int getBauart() {
		return bauart;
	}
	public void setBauart(int bauart) {
		this.bauart = bauart;
	}
	public float getPreis() {
		return preis;
	}
	public void setPreis(float preis) {
		this.preis = preis;
	}
	public Timestamp getStand() {
		return stand;
	}
	public void setStand(Timestamp stand) {
		this.stand = stand;
	}
	public String getFarbe() {
		return farbe;
	}
	public void setFarbe(String farbe) {
		this.farbe = farbe;
	}
	public String getBezeichnung() {
		return bezeichnung;
	}
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
	public int getvMenge() {
		return vMenge;
	}
	public void setvMenge(int vMenge) {
		this.vMenge = vMenge;
	}
	public boolean getGeloescht() {
		return geloescht;
	}
	public void setGeloescht(boolean geloescht) {
		this.geloescht = geloescht;
	}
	public int getProduktkategorie() {
		return produktkategorie;
	}
	public void setProduktkategorie(int produktkategorie) {
		this.produktkategorie = produktkategorie;
	}
	public float getPreisAlt() {
		return preisAlt;
	}
	public void setPreisAlt(float preisAlt) {
		this.preisAlt = preisAlt;
	}
	public int getGroesse() {
		return groesse;
	}
	public void setGroesse(int groesse) {
		this.groesse = groesse;
	}
	public String getBauvariante() {
		String ret = "";
		if(bauvariante == 'm')
		{
			ret = "Herrenrad";
		}
		else if(bauvariante == 'd')
		{
			ret = "Damenrad";
		}
		
		return ret;
	}
	public void setBauvariante(char bauvariante) {
		this.bauvariante = bauvariante;
	}
	public String getMarke() {
		return marke;
	}
	public void setMarke(String marke) {
		this.marke = marke;
	}
	public String getEigenschaften() {
		return eigenschaften;
	}
	public void setEigenschaften(String eigenschaften) {
		this.eigenschaften = eigenschaften;
	}

}
