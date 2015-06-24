package SAPumbau;
/*
 * Repräsentiert einen Kunden, mit den zugehörigen getter und setter Methoden
 */

/**
 *	Enthält alle Attribute eines Kunden, wird zum Austausch zwischen KundeSAP und KundeWEB benutzt
 * @author Thomas
 */
public class Kunde {
	
	private String vorname;
	private String name;
	private String titel;
	private String geburtsdatum;
	private String ort;
	private String strasse;
	private String hausNr;
	private String email;
	private String sapNummer;
	private String PLZ;

	public Kunde() {

	}
	

	public void setVorname(String Vorname)
	{
		vorname = Vorname;
	}
	
	public String getVorname()
	{
		return vorname;
	}
	
	public void setName(String Name)
	{
		name = Name;
	}
	
	public String getName()
	{
		return name;
	}
	
	//da es in der Webshopdatenbank keine Feld für die Anrede(Titel) gibt muss dies über das Geschlecht des Kunde ermittelt werden
	public void setGeschlecht(String Geschlecht)
	{
		if(Geschlecht.equals("männlich"))
		{
			titel = "Herr";
		}else
		{
			titel = "Frau";
		}
	}
	
	public String getTitel()
	{
		return titel;
	}
	
	//Geburtsdatum ist ein Pflichtfeld im SAP-System. Um einen Kunden anlegen zukönnen der noch kein Geburtsdatum angegeben hat wird hier ein Standardwert für das Geburtsdatum gesetzt
	public void setGeburtstdatum(String Geburtsdatum)
	{
		if(Geburtsdatum == null || Geburtsdatum.equals(""))
		{
		geburtsdatum = "19700101";
		}else
		{
		geburtsdatum = Geburtsdatum.replace( "-", "" );
		}
	}
	
	public String getGeburtstdatum()
	{
		return geburtsdatum;
	}


	public String getOrt() {
		return ort;
	}


	public void setOrt(String ort) {
		this.ort = ort;
	}


	public String getStrasse() {
		return strasse;
	}


	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}


	public String getHausNr() {
		return hausNr;
	}


	public void setHausNr(String hausNr) {
		this.hausNr = hausNr;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPLZ() {
		return PLZ;
	}


	public void setPLZ(String pLZ) {
		PLZ = pLZ;
	}


	public String getSapNummer() {
		
		String sapNum = sapNummer;
		
		for(int i = sapNum.length();i<10;i++ )
		{
			sapNum = "0" + sapNum;
		}
		
		
		
		return sapNum;
	}


	public void setSapNummer(String sapNummer) {
		
		String sapNum = sapNummer;
		
		for(int i = sapNum.length();i<10;i++ )
		{
			sapNum = "0" + sapNum;
		}
		
		this.sapNummer = sapNum;
		
	}
	
}
