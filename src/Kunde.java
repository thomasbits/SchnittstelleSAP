/*
 * Repräsentiert einen Kunden
 */
public class Kunde {
	
	private String name, vorname, strasse, hasunr, ort, plz, tel, email, land, region, kundennr;
	
	
	
	public Kunde(String name, String vorname, String strasse, String hasunr,
			String ort, String plz, String tel, String email, String land,
			String region, String kundennr) {
		super(); //???
		this.name = name;
		this.vorname = vorname;
		this.strasse = strasse;
		this.hasunr = hasunr;
		this.ort = ort;
		this.plz = plz;
		this.tel = tel;
		this.email = email;
		this.land = land;
		this.region = region;
		this.kundennr = kundennr;
	}

	public String getKundennr() {
		return kundennr;
	}

	public void setKundennr(String kundennr) {
		this.kundennr = kundennr;
	}

	public String getLand() {
		return land;
	}

	public void setLand(String land) {
		this.land = land;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public String getHasunr() {
		return hasunr;
	}

	public void setHasunr(String hasunr) {
		this.hasunr = hasunr;
	}

	public String getOrt() {
		return ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}

	public String getPlz() {
		return plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
