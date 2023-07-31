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
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Server legt eine LogFile an um alle Aktivitäten der Clients zu 
 * dokumentieren. 
 * @author Beyza Kalinci
 * @param DataInputStream Empfangen von Daten des Clients.
 * @param DataOutputStream Senden von Daten an Client.
 */

public class Server extends Thread {
	
	InputStream in;
	OutputStream out;
	
	DataInputStream dataIn;
	DataOutputStream dataOut;
	
	ArrayList<Person> personen = new ArrayList<Person>();
	int eingabe; 	// Menüeingabe
	boolean error;	// Fehler aufgetretten ?
	Scanner sc = new Scanner(System.in);
	
	InetAddress ip;
	
	/**
	 * Bei jedem Client, wird ein Thread erstellt und den Aufruf der Methode ermöglicht.
	 * DatenInput und DatenOutput wird für Client erstellt und 
	 * anschließend Methode empfangen() gestartet.
	 */
	
	
	public void run() {
		
		System.out.println("Client " + ip + " gestartet...");
		
		dataIn = new DataInputStream(in);
		dataOut = new DataOutputStream(out);

		empfangen();
	}
	
	/**
	 * Konstruktor
	 * @param in InputStream
	 * @param out OuputStream
	 */
	
	
	public Server (InputStream in, OutputStream out) {
		this.out = out;
		this.in = in;
	}
	
	
	public void ip_adress(InetAddress i) {
		ip = i;
	}
	
	
	/**
	 * Eingabe des Clients im Menü wird an den Server übertragen,
	 * und anschließend die jweilige Methode gestartet.
	 */
	
	
	public void empfangen() {
		
		// Empfangen der Eingabe von Client
		
		try {
			eingabe = dataIn.readInt();
		} catch (IOException e) { }
		
		protokoll();
		
		/* 
		 * Jeweils immer Aufruf von empfangen(), um auf neue
		 * Eingabe des Menus zu warten.
		*/
		
		switch (eingabe) {
		case 1:		empfangen(); break;
		case 2:		empfangen(); break;
		case 3: 	recordsSichern(); empfangen(); break;
		case 4:		recordsLaden(); empfangen(); break;
		case 5:		empfangen(); break;
		case 6:		Server.interrupted(); break;
		}
		
	}
	
	/**
	 * Protokollierung der Aktivitäten.
	 * Speicherung von Client im Format: IP, Uhrzeit und Funktion.
	 */
	
	public void protokoll() {
		
		// Aktuelle Uhrzeit / Datum
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat
				("dd.MM.yyyy HH.mm.ss");

		FileWriter out;
		
		try {
			out = new FileWriter("log.csv", true);
			out.write(sdf.format(now) + ";" + ip + ";" + eingabe); 
			out.append(System.getProperty("line.separator") );
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println((sdf.format(now) + ";" + ip + ";" + eingabe));
	}
	
	/**
	 * ArrayList von Client empfangen und in adreli.csv hinzufügen.
	 * Falls Fehler auftritt error = true;
	 */
	
	@SuppressWarnings("unchecked")
	
	public void recordsSichern() {
	
		error = false;
		
		// Empfangen der ArrayList von Client
		
		try {
			ObjectInputStream ois = new ObjectInputStream(in);
			personen = (ArrayList<Person>) ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// Datei anlegen
		
		File adreli = new File ("adreli.csv");
		
		if (!adreli.exists()) {
			try	{
				adreli.createNewFile();
			}
			catch (IOException e) {
				error = true;
			}
		}
		
		// Records in Datei sichern
		
		if (!adreli.canRead()) {
			error = true;
		}
		else {
			FileWriter out;
			
			for (int p = 0; p < personen.size(); p++) {
				
				try {		
					out = new FileWriter("adreli.csv", true);
					out.write(personen.get(p).name + ";" + 
							personen.get(p).vorname + ";" + 
							personen.get(p).anrede + ";" + 
							personen.get(p).strasse + ";" + 
							personen.get(p).plz + ";" + 
							personen.get(p).ort + ";" + 
							personen.get(p).telefon + ";" + 
							personen.get(p).fax + ";" + 
							personen.get(p).bemerkung);
					out.append(System.getProperty("line.separator") );
					out.close();
					
				} catch (IOException e1) {
					error = true;
				}
			}
		}
		
		/* 
		 * Senden von error. Falls Fehler aufgetreten: 
		 * error  = true
		 */
		
		try {
			dataOut.writeBoolean(error);
		} catch (IOException e) { }
	}
	
	/**
	 * Records aus adreli.csv auslesen und an Client senden.
	 */
	
	public void recordsLaden() {
		
		error = false;
		
		String zeile = null;
		File adreli = new File ("adreli.csv");

		if (!adreli.exists()) {		// Existiert Datei ?
			error = true;
		}
		else {
			try	{
				RandomAccessFile datensatz = new RandomAccessFile 
						("adreli.csv","rw");
				datensatz.seek (0);		// Auf Anfang setzten 
				while ((zeile = datensatz.readLine()) != null) {
					String[]daten = zeile.split(";");
					Person pers = new Person(daten[0], daten[1], daten[2], 
							daten[3], daten[4], daten[5], 
							daten[6], daten[7], daten[8]);
					personen.add(pers);		// ArrayList anfügen
				}
				datensatz.close();
			}
			catch (FileNotFoundException fnfex)	{
				error = true;
			}
			catch (IOException ioex) {
				error = true;
			}
		}
		
		// ArrayList an Client übergeben
		
		try {
			ObjectOutputStream ois = new ObjectOutputStream(out);
			ois.writeObject(personen);
		} catch (IOException e) {
			error = true;
		}
		
		/* 
		 * Senden von error. Falls Fehler aufgetreten: 
		 * error  = true
		 */
		
		try {
			dataOut.writeBoolean(error);
		} catch (IOException e) { }
	}
	
	/**
	 * Löschen von adreli.csv.  
	 */
	
	public void dateiLoeschen() {
		
		error = false; 
		
		File adreli = new File ("adreli.csv");

		if (!adreli.exists()) {
			error = true;
		}
		else {
			adreli.delete();
		}
		
		/* 
		 * Senden von error. Falls Fehler aufgetreten: 
		 * error  = true
		 */
		
		try {
			dataOut.writeBoolean(error);
		} catch (IOException e) { }
	}
}