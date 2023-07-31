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

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Funktionen des Clients.
 * Personen aufnehmen / auflisten / sortieren
 * Außerdem Ausgabe von möglichen Fehlermeldungen.
 * @author Hakan Celik
 */

public class Client {

	/**
	 * @param serverIP also die IP-Adresse des Servers,
	 * Muss bei jedem Wechsel der IP angepasst werden.
	 * Firewall muss ggf. auch deaktiviert oder eine Ausnahmeregelung angefügt 
	 * werden.
	 */
	
	static String serverIP;
	
	// Deklaration Scanner / Menüeingabe / ArrayList
	
	static Scanner sc = new Scanner(System.in);
	static int eingabe;
	static ArrayList<Person> personen = new ArrayList<Person>();
	
	static boolean error;	// ist ein Fehler aufgetreten ?
	
	static InputStream in;
	static OutputStream out;
	
	/**
	 * @param dataIn Empfangen von Daten
	 * @param dataOut Senden von Daten
	 */
	
	static DataInputStream dataIn;
	static DataOutputStream dataOut;
	
	/**
	 * Main Methode
	 * Eingabe von IP Adresse des Servers von Benutzer.
	 * Anschließend Aufruf des Menüs
	 * 
	 */
	
	public static void main(String[] args) {
		
		// IP-Adresse des Servers 
		
		System.out.println("Bitte die IP-Adresse des Servers eingeben: "); 
		serverIP = sc.nextLine(); 
		
		System.out.println("\n\nBitte warten...");
		
		try {
			Socket socket = new Socket(serverIP, 5678);
			
			in = socket.getInputStream();
			out = socket.getOutputStream();
			
			dataIn = new DataInputStream(socket.getInputStream());
			dataOut = new DataOutputStream(socket.getOutputStream());	
		} catch (UnknownHostException ex) {
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			System.out.println("Verbindung fehlgeschlagen. " +
					"Programm wird beendet.");
			System.exit(0);
		} catch (IOException ex) {
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			System.out.println("Verbindung fehlgeschlagen. " +
					"Programm wird beendet.");
			System.exit(0);
		}
        
		menu();
	}
		
	/**
	 * 	Ausgabe des Menus.
	 * 	Eingabe wird eingelesen und an Server gesendet.
	 * 	Anschließend möglichkeit zum Aufruf der jeweiligen Methode. 
	 */
	
	public static void menu() {
		
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		System.out.println(" ____________________________________________");
		System.out.println("|\tADRELI - Adressverwaltung            |" );
		System.out.println("|          			      	     |");
		System.out.println("|--> Wollen Sie...		 	     |");
		System.out.println("|					     |");
		System.out.println("|     	  eine neue Person aufnehmen : > 1   |");
		System.out.println("|     	           Records auflisten : > 2   |");
		System.out.println("|      Records in eine Datei sichern : > 3   |");
		System.out.println("|      Records aus einer Datei laden : > 4   |");
		System.out.println("|                     Datei loeschen : > 5   |");
		System.out.println("|             das Programm verlassen : > 6   |");
		System.out.println(" ____________________________________________");
		System.out.println(" ");
		
		try {
		eingabe = sc.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("Fehlerhafte Eingabe. " +
					"Programm wird beendet.");
		}
		// Prüfen der Eingabe
		
		if ((eingabe < 0) && (eingabe > 7)) {
			menu();
		}
		
		//	Senden der Eingabe an den Server
		
		try {
			dataOut.writeInt(eingabe);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*
		 * 	Nach Beendigung der Methode immer erneuter Aufruf des
		 * 	Menüs. Gleichzeitig läuft empfangen() im Server.
		 */
			
			switch (eingabe) {
			case 1: 
				personAufnehmen(); break;
			case 2: 
				recordsAuflisten(); break;
			case 3:			
				recordsSichern(); break;
			case 4:
				recordsLaden(); break;
			case 5:
				dateiLoeschen(); break;
			case 6:
				try 
				{ 
					Thread.sleep(1000);
				} catch (InterruptedException e) {	} 
				
				System.exit(0); break;
			}
		}
	
	/**
	 * Eine neue Person aufnehmen
	 * Innerhalb dieser Methode können Personen angelegt, bzw. hinzugefügt 
	 * werden. Eingaben werden durch Reguläre Ausdrücke auf Richtigkeit 
	 * geprüft und anschließend in eine ArrayList gespeichert.
	 */
	
	public static void personAufnehmen() {
		
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		System.out.println("\nGeben Sie bitte die Daten ein:\n");
		sc.nextLine();
		
		// ABFRAGE Name
		
		boolean name_ok = false;
		String name;
		
		do {
			System.out.print("       Name: "); name = sc.nextLine();
			name_ok = name.matches("[A-Za-z\\-\\s]{2,40}");
			
			if (name_ok == false) {
				System.out.println("\t\tBitte Namen korrekt eingeben." +
						"\n\t\tBsp.: Mustermann");
			}
			
		} while (name_ok == false);
		
		// ABFRAGE Vorname
		
		boolean vorname_ok = false;
		String vorname;
		
		do {
			System.out.print("    Vorname: "); vorname = sc.nextLine();
			vorname_ok = vorname.matches("[A-Za-z\\-\\s]{2,40}");
			
			if (vorname_ok == false) {
				System.out.println("\t\tBitte Vornamen korrekt eingeben." +
						"\n\t\tBsp.: Sebastian");
			}
			
		} while (vorname_ok == false);	
		
		// ABFRAGE Anrede
		
		boolean anrede_ok = false;
		String anrede;
		
		do {
			System.out.print("     Anrede: "); anrede = sc.nextLine();
			anrede_ok = anrede.matches("Herr|Frau");
			
			if (anrede_ok == false) {
				System.out.println("\t\tBitte Herr oder Frau eingeben.");
			}
					
		} while (anrede_ok == false);
		
		// ABFRAGE Strasse
		
		boolean strasse_ok = false;
		String strasse;
		
		do {
			System.out.print("    Strasse: "); strasse = sc.nextLine();
			strasse_ok = strasse.matches
					("[a-zA-Z\\-\\s\\.]+[1-9][0-9]*[a-z]?");
			
			if (strasse_ok == false) {
				System.out.println("\t\tBitte Strasse korrekt eingeben." +
						"\n\t\tBsp.: Hauptstrasse 4");
			}
					
		} while (strasse_ok == false);
		
		// ABFRAGE PLZ
		
		boolean plz_ok = false;
		String plz;
		
		do {
			System.out.print("        PLZ: "); plz = sc.nextLine();
			plz_ok = plz.matches("D-[0-9]{5}");
			
			if (plz_ok == false) {
				System.out.println("\t\tBitte PLZ im Format D-11111 " +
						"eingeben.");
			}
					
		} while (plz_ok == false);
		
		// ABFRAGE Ort
		
		boolean ort_ok = false;
		String ort;
		
		do {
			System.out.print("        Ort: "); ort = sc.nextLine();
			ort_ok = ort.matches("[A-Za-z\\s\\-]{3,40}");
			
			if (ort_ok == false) {
				System.out.println("\t\tBitte Ort korrekt eingeben." +
						"\n\t\tBsp.: Balingen");
			}
					
		} while (ort_ok == false);
		
		// ABFRAGE Telefon
		
		boolean telefon_ok = false;
		String telefon;
		
		do {
			System.out.print("    Telefon: "); telefon = sc.nextLine();
			telefon_ok = telefon.matches("[0-9\\.\\-]*");
			
			if (telefon_ok == false) {
				System.out.println("\t\tBitte Telefonnummer korrekt " +
						"eingeben.\n\t\tBsp.: 0123-456789");
			}
					
		} while (telefon_ok == false);
		
		// ABFRAGE Fax
		
		boolean fax_ok = false;
		String fax;
		
		do {
			System.out.print("        Fax: "); fax = sc.nextLine();
			fax_ok = fax.matches("[0-9\\.\\-]*");
			
			if (fax_ok == false) {
				System.out.println("\t\tBitte Faxnummer korrekt " +
						"eingeben.\n\t\tBsp.: 0123-456789");
			}
					
		} while (fax_ok == false);
		
		// ABFRAGE Bemerkung
		
		boolean bemerkung_ok = false;
		String bemerkung;
		
		do {
			System.out.print("  Bemerkung: "); bemerkung = sc.nextLine();
			bemerkung_ok = bemerkung.matches("[^;]{4,300}");
			
			if (bemerkung_ok == false) {
				System.out.println("\t\tMin. 4 / max. 300 Zeichen. " +
						"';' nicht erlaubt.");
			}
					
		} while (bemerkung_ok == false);
			
		// ABFRAGE Stimmt's (J/N)
		
		System.out.println("\n\t\tStimmt's (J/N)");
		char jn1 = sc.next().charAt(0); jn1 = Character.toUpperCase(jn1);
		
		if (jn1 == 'J') {
			Person pers = new Person(name, vorname, anrede, strasse, plz, 
					ort, telefon, fax, bemerkung);
			personen.add(pers);
		} 
		else if (jn1 == 'N') {
			personAufnehmen();
		} 
		else {
			System.out.println("Ungültige Eingabe. ENTER drücken.");
			sc.nextLine(); sc.nextLine(); menu();
		}

		// ABFRAGE Noch eine Person aufnehmen (J/N)
		
		System.out.println("\n\t\tNoch eine Person aufnehmen? (J/N)");
		char jn2 = sc.next().charAt(0); jn2 = Character.toUpperCase(jn2);
		
		if (jn2 == 'J') {
			personAufnehmen();
		} 
		else if (jn2 == 'N') {
			menu();
		} 
		else {
			System.out.println("Ungültige Eingabe. ENTER druecken.");
			sc.nextLine(); menu();
		}		
	}
	
	/**
	 * 	Records auflisten
	 * 	Durch diese Methode können erstellte oder geladenen Datensätze
	 * 	ausgegeben werden. 
	 */
	
	public static void recordsAuflisten() {
		
		int zaehler = 1;
		
		if (personen.isEmpty()) {
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
					"\n\n\n\n\n\n");
			System.out.println("\nKeine Datensaetze vorhanden. " +
					"Personen aufnehmen oder laden.");
		}
		else {
			for (Person p : personen) {
				sc.nextLine();
				
				System.out.println("\n\n\n\n\n\n\n\n\n\n\n" +
						"\n\n\n\n\n\n\n\n\n");
				System.out.println("\t\t\tSatzinhalt ("+ zaehler +
						". Satz)\n");
	
				System.out.println("         Name : " + p.name);
				System.out.println("      Vorname : " + p.vorname);
				System.out.println("       Anrede : " + p.anrede);
				System.out.println("      Strasse : " + p.strasse);
				System.out.println("          PLZ : " + p.plz);
				System.out.println("          Ort : " + p.ort);
				System.out.println("      Telefon : " + p.telefon);
				System.out.println("          Fax : " + p.fax);
				System.out.println("    Bemerkung : " + p.bemerkung);
				
				zaehler++;
			}
		}
		
		// Zurück zum Menu
		
		sc.nextLine(); sc.nextLine(); menu();
	}
	
	/**
	 * ArrayList vom Client wird an den Server gesendet und dort 
	 * als CSV Datei gespeichert.
	 */
	
	public static void recordsSichern() {

			error = false;
			
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
					"\n\n\n\n\n");
			
			// Senden der ArrayList
			
			try {
				ObjectOutputStream ois = new ObjectOutputStream(out);
				ois.writeObject(personen);
			} catch (IOException e) {
				System.out.println("Fehler beim Uebertragen.");
			}
			
			/* 
			 * Empfangen von error. Falls Fehler aufgetreten: 
			 * error  = true
			 */
			 
			try {
				error = dataIn.readBoolean();
			} catch (IOException e) { }
			
			// Ausgabe Erfolg / Fehler
			
			if (error == false) {
				System.out.println("Records erfolgreich gespeichert.");
			} else {
				System.out.println("Ein Fehler ist aufgetreten.");
			}
			
			sc.nextLine(); sc.nextLine(); menu(); 
			
	}
	
	/**
	 * Records laden
	 * ArrayList wird vom Server empfangen und der ArrayListe vom 
	 * Clients hinzugefügt.
	 */
	
	@SuppressWarnings("unchecked")
	
	public static void recordsLaden() {
		
		error = false;
		
		System.out.println("\n\n\n" +
				"\n\n\n");
		
		// ArrayList empfangen
		
		try {
			ObjectInputStream ois = new ObjectInputStream(in);
			personen = (ArrayList<Person>) ois.readObject();
		} catch (IOException e) {
			error = true;
		} catch (ClassNotFoundException e) {
			error = true;
		}
		
		/* 
		 * Empfangen von error. Falls Fehler aufgetreten: 
		 * error  = true
		 */
		
		try {
			error = dataIn.readBoolean();
		} catch (IOException e) { }
		
		// Ausgabe Fehler / Erfolg
		
		if (error == false) {
			System.out.println("Records erfolgreich geladen.");
		} else {
			System.out.println("Ein Fehler ist aufgetreten.");
		}
		
		// Zurück zum Menü
		
		sc.nextLine(); sc.nextLine(); menu(); 	
	}
	

	
	/**
	 * Datei löschen
	 * adreli.csv wird von Server gelöscht. 
	 * Anschließend Ausgabe von möglichen Fehlermeldungen
	 */
	
	public static void dateiLoeschen() {
		
		error = false;
		
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
				"\n\n\n\n\n");
		
		/* 
		 * Empfangen von error. Falls Fehler aufgetreten: 
		 * error = true
		 */
		
		/*
		try {
			error = dataIn.readBoolean();
		}
		 catch (IOException e) { }
		*/
		// Ausgabe Erfolg / Fehler
		
		if (error == false) {
			System.out.println("Datei erfolgreich von Server " +
					"geloescht.");
		} else {
			System.out.println("Ein Fehler ist aufgetreten.");
		}	
		
		//Zurück zum Menu
		
		sc.nextLine(); sc.nextLine(); menu();
	}
}