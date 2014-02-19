package de.hjg.hugojunkersapp.SQLite;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

//new class Vertretung Data, used to save each Vertretung
public class VertretungData {

	private int ID;
	private String Klasse;
	private String Lehrer;
	private Date Datum;
	private String Stunde;
	private String Fach;
	private String Raum;
	private String Vertreter;
	private String Info;

	// Methods to set and get Values
	public int getID() {
		return ID;
	}

	public void setID(int id) {
		this.ID = id;
	}

	public String getKlasse() {
		return Klasse;
	}

	public void setKlasse(String s) {
		this.Klasse = s;
	}

	public String getLehrer() {
		return Lehrer;
	}

	public void setLehrer(String s) {
		this.Lehrer = s;
	}

	public Date getDatum() {
		return Datum;
	}

	public void setDatum(String s) {
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			this.Datum = df.parse(s);
		} catch (Exception ex) {
		}
	}

	public String getStunde() {
		return Stunde;
	}

	public void setStunde(String std) {
		this.Stunde = std;
	}

	public String getFach() {
		return Fach;
	}

	public void setFach(String f) {
		this.Fach = f;
	}

	public String getRaum() {
		return Raum;
	}

	public void setRaum(String r) {
		this.Raum = r;
	}

	public String getVertreter() {
		return Vertreter;
	}

	public void setVertreter(String V) {
		this.Vertreter = V;
	}

	public String getInfo() {
		return Info;
	}

	public void setInfo(String I) {
		this.Info = I;
	}

	public boolean equal(VertretungData vert) {
		if ((!this.getDatum().equals(vert.getDatum()))
				|| (!this.getFach().equals(vert.getFach()))
				|| (!this.getInfo().equals(vert.getInfo()))
				|| (!this.getKlasse().equals(vert.getKlasse()))
				|| (!this.getLehrer().equals(vert.getLehrer()))
				|| (!this.getRaum().equals(vert.getRaum()))
				|| (!this.getStunde().equals(vert.getStunde()))
				|| (!this.getVertreter().equals(vert.getVertreter()))) {
			return false;
		} else {
			return true;
		}
	}
}
