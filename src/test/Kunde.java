package test;

public class Kunde {
	
	private String vorname;
	private String name;
	private String titel;

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
		
		Geburtsdatum = Geburtsdatum.replace( "-", "" );
	}
	
	public void setOrt()
	{
		
	}
	public void setPLZ()
	{
		
	}
	
	public void setStrasse()
	{
		
	}
	
	public void setHausnr()
	{
		
	}
	
	public void setEmail()
	{
		
	}
	
}
