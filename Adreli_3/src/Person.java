/**
 * 	PROGRAMMIEREN UND MODELLIEREN 2
 * 	==================================================
 * 	PROJEKT 3
 * 	ADRELI_3_NetCom
 * 	==================================================
 * 	AUTOREN
 * 	Arianit Metaj, Beyza Kalinci, Hakan Celik
 * 	==================================================
 * 	DATUM
 * 	29.11.2017
 */

import java.io.Serializable;

/**
 * Die Klasse Person ist notwendig für die Erstellung von Personen-Objekten.
 * @author Beyza Kalinci
 *
 */

@SuppressWarnings("serial")

public class Person implements Serializable {
	
	String name, vorname, anrede, strasse, plz, ort, telefon, fax, bemerkung;
	
	public Person(String n, String v, String a, String s, String p, 
			String o, String t, String f, String b) {
		
		this.name = n; this.vorname = v; this.anrede = a; 
		this.strasse = s; this.plz = p;	this.ort = o; 
		this.telefon = t; this.fax = f; this.bemerkung = b;
	}
}