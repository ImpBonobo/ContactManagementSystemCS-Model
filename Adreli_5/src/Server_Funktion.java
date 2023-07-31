/**
 * 	PROGRAMMIEREN UND MODELLIEREN 2
 * 	==================================================
 * 	PROJEKT 5
 * 	ADRELI_5_JDBC
 * 	==================================================
 * 	AUTOREN
 * 	Arianit Metaj, Beyza Kalinci, Hakan Celik
 * 	==================================================
 * 	DATUM
 * 	17.01.2018
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.sql.*;
import java.util.ArrayList;

/**
 * Klasse Server Funktion beinhaltet alle Funktionen, welche der Client aufrufen
 * kann. Außerdem werden die Aktivitäten desselbigen protokolliert.
 * 
 * @author Arianit Metaj, Beyza Kalinci, Hakan Celik
 *
 */

public class Server_Funktion extends Thread {

	ArrayList<Person> personen = new ArrayList<Person>();
	int eingabe; // Von Client gestartete Funktion
	boolean error; // Ist ein Fehler aufgetreten ?
	String verzeichnis; // adreli.csv Path
	String datei; // Dateiname
	File adreli; // Dateiobjekt
	String ip; // IP Adresse des Clients
	static boolean ende; // Server gestoppt ?
	boolean datenbank; // Welche Dateibasis ist aktiv ?
	static Statement stmt; // SQL Statements
	static Connection conn; // Datenbank Connection
	long time; // Messung der Serverfunktionen

	InputStream in;
	OutputStream out;
	DataInputStream dataIn;
	DataOutputStream dataOut;

	// =============================================================================
	// ======================== RUN ========================
	// =============================================================================

	/**
	 * Automatischer Aufruf bei dem Verbinden eines neues Clients. Streams für
	 * die Kommunikation zu dem Client werden erstellt und die Methode empfangen
	 * aufgerufen.
	 */

	public void run() {

		dataIn = new DataInputStream(in);
		dataOut = new DataOutputStream(out);

		// Verzeichnis und Dateiname der gewählten csv Datei empfangen
		verzeichnis = Server_GUI.getVerzeichnis();
		datei = Server_GUI.getDatei();

		// Dateiobjekt adreli
		adreli = new File(verzeichnis + datei);

		// Dateibasis
		datenbank = Server_GUI.datenbank;

		// Anzahl verbundener Clients um 1 erhöhen
		Server_GUI.addClient();
		// neue Verbindung protokollieren
		protokollieren(0);

		// Auf Start einer Funktion von Client warten
		empfangen();
	}

	// =============================================================================
	// ===================== KONSTRUKTOR ====================
	// =============================================================================

	/**
	 * Konstruktor der Klasse Server_Funktion. Wird bei dem Verbinden eines
	 * Clients zum Server aufgerufen. Erstellung von InputStream / OutputStream.
	 * 
	 * @param in
	 *            InputStream für Kommunikation
	 * @param out
	 *            OutputStream für Kommuniktaion
	 */

	public Server_Funktion(InputStream in, OutputStream out, Connection c) {

		this.out = out;
		this.in = in;
		conn = c;
	}

	// =============================================================================
	// ===================== EMPFANGEN ====================
	// =============================================================================

	/**
	 * Methode empfängt die Funktion, welche vom Client gestartet wurde. Werte
	 * liegen zwischen 1 und 7. Funktionen entsprechend ADRELI 1. Hierbei wird
	 * in Abstand von 200ms geprüft, ob eine Funktion getätigt wurde. Außerdem
	 * wird bei dieser Prüfung falls notwendig eine Meldung an alle Clients
	 * verschickt, dass der Server beendet wurde. Anschließend wird der Thread
	 * geschlossen.
	 */

	public void empfangen() {

		// Falls Server deaktiviert
		if (ende == true) {
			try {
				dataOut.writeChar('s'); // Meldung an Client senden
			} catch (IOException e) {
			}

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}

			try {
				in.close();
				out.close();
			} catch (IOException e) {
			}

			System.exit(0); // Schließen aller Threads, Streams,...

		}

		try {
			// Daten zum lesen vorhanden ?
			if (dataIn.available() > 0) {
				// Eingabe lesen
				try {
					eingabe = dataIn.readInt();
				} catch (IOException e) {
				}
				// Aktivität protokollieren
				protokollieren(eingabe);
				// Entsprechend der aufgerufenen Funktion wird dementsprechende
				// Methode gestartet. Anschließend erneut empfangen().
				switch (eingabe) {
				case 1:
					empfangen();
					break;
				case 2:
					empfangen();
					break;
				case 3:
					records_sichern();
					empfangen();
					break;
				case 4:
					records_laden();
					empfangen();
					break;
				case 5:
					empfangen();
					break;
				case 6:
					datei_loeschen();
					empfangen();
					break;
				case 7:
					Server_GUI.minClient();
					Server_Funktion.interrupted();
					break;
				}
			}
		} catch (IOException e1) {
		}

		try {
			Server_Funktion.sleep(200); // Stop bis neuer Check
		} catch (InterruptedException e) {
		}

		empfangen(); // Rekursion

	}

	// =============================================================================
	// =================== PROTOKOLL ====================
	// =============================================================================

	/**
	 * Aktivitäten des Clients werden protokolliert. Sowohl in log.csv, als auch
	 * auf der Startseite des Servers. Format: Zeit IP Funktion
	 */

	public void protokollieren(int e) {

		String aktivitaet = null;

		switch (e) {
		case 0:
			aktivitaet = "Verbindung hergestellt";
			break;
		case 1:
			aktivitaet = "Person aufnehmen";
			break;
		case 2:
			aktivitaet = "Records auflisten";
			break;
		case 3:
			aktivitaet = "Records sichern";
			break;
		case 4:
			aktivitaet = "Records laden";
			break;
		case 5:
			aktivitaet = "Daten löschen";
			break;
		case 6:
			aktivitaet = "Verbindung beendet";
			break;
		}

		Server_GUI.addLog(ip, aktivitaet);

		// Aktuelle Uhrzeit / Datum
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"dd.MM.yyyy HH:mm:ss");

		// Schreiben in Datei log.csv
		FileWriter out;
		try {
			out = new FileWriter("log.csv", true);
			out.write(sdf.format(now) + ";" + ip + ";" + aktivitaet);
			out.append(System.getProperty("line.separator"));
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Setter Methode für die IP Adresse des Clients
	 * 
	 * @param i
	 *            IP Adresse des Clients vom Typ InetAddress
	 */

	public void setIP(String i) {

		ip = i;
	}

	// =============================================================================
	// =================== RECORDS SICHERN ====================
	// =============================================================================

	@SuppressWarnings("unchecked")
	public void records_sichern() {

		time = -System.currentTimeMillis();

		error = false;

		// Empfangen der ArrayList
		try {
			ObjectInputStream ois = new ObjectInputStream(in);
			personen = (ArrayList<Person>) ois.readObject();
		} catch (IOException e) {
			error = true;
		} catch (ClassNotFoundException e) {
			error = true;
		}

		if (datenbank == true) {

			try {
				stmt = conn.createStatement();
			} catch (SQLException e) {
				error = true;
			}

			for (int p = 0; p < personen.size(); p++) {
				try {
					stmt.executeUpdate("INSERT INTO person VALUES ('"
							+ personen.get(p).name + "', '"
							+ personen.get(p).vorname + "', '"
							+ personen.get(p).anrede + "', '"
							+ personen.get(p).strasse + "', '"
							+ personen.get(p).plz + "', '"
							+ personen.get(p).ort + "', '"
							+ personen.get(p).telefon + "', '"
							+ personen.get(p).fax + "', '"
							+ personen.get(p).bemerkung + "')");
				} catch (SQLException e) {
					error = true;
				}
			}

		} else {

			FileWriter out;
			for (int p = 0; p < personen.size(); p++) {
				try {
					out = new FileWriter(verzeichnis + datei, true);
					out.write(personen.get(p).name + ";"
							+ personen.get(p).vorname + ";"
							+ personen.get(p).anrede + ";"
							+ personen.get(p).strasse + ";"
							+ personen.get(p).plz + ";" + personen.get(p).ort
							+ ";" + personen.get(p).telefon + ";"
							+ personen.get(p).fax + ";"
							+ personen.get(p).bemerkung);
					out.append(System.getProperty("line.separator"));
					out.close();
				} catch (IOException e1) {
					error = true;
				}
			}
		}

		// Mögliche Fehler werden an Client gesendet
		try {
			dataOut.writeBoolean(error);
		} catch (IOException e) {
		}

		Server_GUI.addTime(time + System.currentTimeMillis());
	}

	// =============================================================================
	// =================== RECORDS LADEN ====================
	// =============================================================================

	/**
	 * Personendaten sollen aus der vorhandenen csv Datei gelesen und in die
	 * ArrayList der Servers hinzugefügt werden. Anschließend wird die ArrayList
	 * an den Client gesendet und mit dessen ArrayList überschrieben.
	 */

	public void records_laden() {

		time = -System.currentTimeMillis();

		error = false;

		if (datenbank == true) {

			try {
				stmt = conn.createStatement();
			} catch (SQLException e) {
				error = true;
			}

			ResultSet p_result = null;
			try {
				p_result = stmt.executeQuery("SELECT * FROM person");
			} catch (SQLException e) {
				error = true;
			}

			try {
				while (p_result.next()) {
					Person pers = new Person(p_result.getString("Name"),
							p_result.getString("Vorname"),
							p_result.getString("Anrede"),
							p_result.getString("Strasse"),
							p_result.getString("PLZ"),
							p_result.getString("Ort"),
							p_result.getString("Telefon"),
							p_result.getString("Fax"),
							p_result.getString("Bemerkung"));
					personen.add(pers);
				}
			} catch (SQLException e) {
				error = true;
			}

		} else {

			String zeile = null;

			try {
				RandomAccessFile datensatz = new RandomAccessFile(verzeichnis
						+ datei, "rw");
				datensatz.seek(0); // Auf Anfang setzten
				while ((zeile = datensatz.readLine()) != null) {
					String[] daten = zeile.split(";");
					try {
						Person pers = new Person(daten[0], daten[1], daten[2],
								daten[3], daten[4], daten[5], daten[6],
								daten[7], daten[8]);
						personen.add(pers); // ArrayList anfügen
					} catch (ArrayIndexOutOfBoundsException e) {
						error = true; // CSV falsch
					}
				}
				datensatz.close();
			} catch (FileNotFoundException fnfex) {
				error = true;
			} catch (IOException ioex) {
				error = true;
			}
		}

		// ArrayList an Client übergeben
		try {
			ObjectOutputStream ois = new ObjectOutputStream(out);
			ois.writeObject(personen);
		} catch (IOException e) {
			error = true;
		} catch (NullPointerException e) {
			error = true;
		}

		// Ist ein Fehler aufgetreten ?
		try {
			dataOut.writeBoolean(error);
		} catch (IOException e) {
		}

		Server_GUI.addTime(time + System.currentTimeMillis());
	}

	// =============================================================================
	// =================== DATEI LOESCHEN ====================
	// =============================================================================

	/**
	 * Datei wird von Server gelöscht. Mögliche Fehlermeldungen werden an den
	 * Client weitergeleitet.
	 */

	public void datei_loeschen() {

		time = -System.currentTimeMillis();

		error = false;

		if (datenbank == true) {

			try {
				stmt = conn.createStatement();
			} catch (SQLException e) {
				error = true;
			}

			try {
				stmt.executeUpdate("DELETE FROM person;");
			} catch (SQLException e) {
				error = true;
			}

		} else {

			// Delete falls Datei vorhanden
			if (!adreli.exists()) {
				error = true;
			} else {
				adreli.delete();
			}
		}

		// Ist ein Fehler aufgetreten ?
		try {
			dataOut.writeBoolean(error);
		} catch (IOException e) {
		}

		Server_GUI.addTime(time + System.currentTimeMillis());
	}
}