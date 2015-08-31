package SAPumbau;

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
	private String adt; //"Artikel des Tages"
	private String beschreibung; //"Beschreibung" Ausführliche Beschreibung
	private int bauart = 1; //"bauart" -> Mountainbike, Crossrad, ...
	private float preis; //"Preis"
	private String farbe; //"Farbe"
	private String bezeichnung; //"Bezeichnung"
	private int vMenge; //Verfuegbare Menge
	private boolean geloescht; //"geloescht"
	private int produktkategorie; //"produktkategorie"
	private float preisAlt; //"preis_alt"
	private int groesse; //"groesse"
	private String bauvariante; //"bauvariante" -> Herrenrad h Damenrad d
	private String marke; //"marke"
	private String eigenschaften; //"Eigenschaften"


	public Material() {
		// TODO Auto-generated constructor stub
	}


	public String getmID() {

		return "'"+mID+"'";
	}
	public void setmID(String mID) {
		this.mID = mID;
	}
	public String getAdt() {

		return "'"+adt+"'";
	}
	public void setAdt(String adt) {
		
		if(adt == "")
		{
			adt = "nein";
		}
		this.adt = adt;
	}
	public String getBeschreibung() {
		return "'"+beschreibung+"'";
	}
	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}
	public int getBauart() {
		System.out.println("Bauart: " + bauart);
		return bauart;
	}
	public void setBauart(String bauart) {
		
		
		int bauartSize = bauart.length();
		if (bauartSize > 1)
		{
			char a = bauart.charAt(bauartSize-1);
			String b = String.valueOf(a);
			if(!b.matches("[0-9]"))
			{
				this.bauart = Integer.valueOf(bauart.substring(0,bauartSize-1));
			}
		}else
		{
			if(!bauart.matches("[0-9]"))
			{
				this.bauart = 14;
			}
			else
			{
				this.bauart = Integer.valueOf(bauart);
			}
			
		}
		
		
	}
	public float getPreis() {
		return preis;
	}
	public void setPreis(float preis) {
		this.preis = preis;
	}
	public String getFarbe() {
		return "'"+farbe+"'";
	}
	public void setFarbe(String farbe) {
		this.farbe = farbe;
	}
	public String getBezeichnung() {
		return "'"+bezeichnung+"'";
	}
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
	public int getvMenge() {
		return (vMenge +20);
	}
	public void setvMenge(int vMenge) {
		//Damit erstmal Produkte da sind.
		this.vMenge = vMenge ;
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
	public void setGroesse(String groesse) {
		groesse = groesse.substring(0,2);

		if(groesse.length()<1)
		{
			this.groesse = 10;
		}
		else
		{

			//this.groesse = Integer.valueOf(groesse);

			String test = groesse;
			System.out.println("Grosse:---"+ test +"---");
			this.groesse = Integer.valueOf(test);
		}

	}
	public String getBauvariante() {
		return bauvariante;
	}
	public void setBauvariante(String bauvariante) {
		bauvariante = bauvariante.substring(0, 1);
		if(bauvariante.equals("d"))
		{
			this.bauvariante = "'Damenrad'";
		}
		else if(bauvariante.equals("h"))
		{
			this.bauvariante = "'Herrenrad'";
		}else
		{
			this.bauvariante = null;
		}
	}
	public String getMarke() {
		return marke;
	}
	public void setMarke(String marke) {
		if(marke == "")
		{
			this.marke = null;
		}else
		{
			this.marke = "'"+ marke + "'";
		}
		
	}
	public String getEigenschaften() {
		return "'"+eigenschaften+"'";
	}
	public void setEigenschaften(String eigenschaften) {
		this.eigenschaften = eigenschaften;
		System.out.println("Eig:" + eigenschaften);
	}

}
