package SAPumbau;

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
		// TODO Auto-generated constructor stub
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
	
	public void setGeschlecht(String Geschlecht)
	{
		if(Geschlecht.charAt(0)=='m')
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
	
	public void setGeburtstdatum(String Geburtsdatum)
	{
		
		geburtsdatum = Geburtsdatum.replace( "-", "" );
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
		return sapNummer;
	}


	public void setSapNummer(String sapNummer) {
		this.sapNummer = sapNummer;
	}
	
}
