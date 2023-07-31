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
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.io.Serializable;

/**
 * Client_Funktion beinhaltet vor allem die Kommunikation zwischen Client und
 * Server. Außerdem Verwalung der Personenobjekte in der ArrayList.
 * 
 * @author Arianit Metaj, Beyza Kalinci, Hakan Celik
 * 
 */

public class Client_Funktion {

	static InputStream in;
	static OutputStream out;
	static DataInputStream dataIn;
	static DataOutputStream dataOut;

	static ArrayList<Person> personen = new ArrayList<Person>();
	static Socket socket;

	protected static boolean error;
	protected static String ip;
	protected static String port;

	// =============================================================================
	// ======================== SENDEN ========================
	// =============================================================================

	/**
	 * Methode leitet Aufruf von Funktion aus Client_GUI an Server weiter.
	 * 
	 * @param i
	 *            Nummer von Funktion (vgl aus ARELI 1)
	 */

	public static void senden(int i) {

		int eingabe = i; // 1-7 vgl. ADRELI 1

		try {
			dataOut.writeInt(eingabe);
		} catch (IOException e) {
		}
	}

	// =============================================================================
	// ====================== VERBINDEN ========================
	// =============================================================================

	/**
	 * Verbindung zu Server herstellen.
	 * 
	 * @param i
	 *            IP-Adresse des Servers
	 * @param p
	 *            Port
	 */

	public static void verbinden(String i, String p) {

		ip = i; // IP Adresse Server
		port = p; // Port
		error = false; // Fehler aufgetreten ?

		try {
			socket = new Socket(ip, Integer.parseInt(port));
			in = socket.getInputStream();
			out = socket.getOutputStream();
			dataIn = new DataInputStream(socket.getInputStream());
			dataOut = new DataOutputStream(socket.getOutputStream());
		} catch (UnknownHostException ex) {
			error = true;
		} catch (IOException ex) {
			error = true;
		}
	}

	// =============================================================================
	// ====================== TRENNEN ========================
	// =============================================================================

	/**
	 * Beenden der Verbindung zu dem Server.
	 */

	public static void trennen() {

		try {
			socket.close();
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// =============================================================================
	// ==================== PERSON AUFNEHMEN ====================
	// =============================================================================

	/**
	 * Neues Personenobjekt erstellen und der ArrayList anfügen.
	 * 
	 * @param n
	 *            String Name
	 * @param v
	 *            String Vorname
	 * @param a
	 *            String Anrede
	 * @param s
	 *            String Straße
	 * @param p
	 *            String PLZ
	 * @param o
	 *            String Ort
	 * @param t
	 *            String Telefon
	 * @param f
	 *            String Fax
	 * @param b
	 *            String Bemerkung
	 * 
	 */

	public static void person_aufnehmen(String n, String v, String a, String s,
			String p, String o, String t, String f, String b) {

		String name = n;
		String vorname = v;
		String anrede = a;
		String strasse = s;
		String plz = p;
		String ort = o;
		String telefon = t;
		String fax = f;
		String bemerkung = b;

		// Neues Objekt von Person erstellen
		Person pers = new Person(name, vorname, anrede, strasse, plz, ort,
				telefon, fax, bemerkung);
		// ArrayList anfügen
		personen.add(pers);
	}

	// =============================================================================
	// ==================== REOCRDS SICHERN ====================
	// =============================================================================

	/**
	 * Personeneinträge, welche sich im Moment in der ArrayList des Clients
	 * befinden, sollen in eine csv Datei auf dem Server gespeichert werden.
	 */

	public static void records_sichern() {

		error = false;

		// Senden der ArrayList
		try {
			ObjectOutputStream ois = new ObjectOutputStream(out);
			ois.writeObject(personen);
		} catch (IOException e) {
			error = true;
		}

		// Ist ein Fehler aufgetreten ?
		try {
			error = dataIn.readBoolean();
		} catch (IOException e) {
		}
	}

	// =============================================================================
	// =================== RECORDS LADEN ====================
	// =============================================================================

	/**
	 * Personendaten werden aus der csv Datei der Servers gelesen und an den
	 * Client geschickt. Diese werden daraufhin in die ArrayList desjenigen
	 * gespeichert und können verwendet werden.
	 */

	@SuppressWarnings("unchecked")
	public static void records_laden() {

		error = false;

		// Empfangen der ArrayList von Server
		try {
			ObjectInputStream ois = new ObjectInputStream(in);
			personen = (ArrayList<Person>) ois.readObject();
		} catch (IOException e) {
			error = true;
		} catch (ClassNotFoundException e) {
			error = true;
		}

		// Ist ein Fehler aufgetreten ?
		try {
			error = dataIn.readBoolean();
		} catch (IOException e) {
		}
	}

	// =============================================================================
	// =================== DATEI LOESCHEN ====================
	// =============================================================================

	/**
	 * CSV Datei auf dem Server soll gelöscht werden. Empfangen von möglichen
	 * Fehlern.
	 */

	public static void datei_loeschen() {

		// Ist ein Fehler aufgetreten ?
		try {
			error = dataIn.readBoolean();
		} catch (IOException e) {
		}
	}
}

/**
 * Klasse Person notwendig für die Erstellung von Personen-Objekten.
 * 
 * @author Arianit Metaj, Beyza Kalinci, Hakan Celik
 *
 */

@SuppressWarnings("serial")
class Person implements Serializable {

	String name, vorname, anrede, strasse, plz, ort, telefon, fax, bemerkung;

	public Person(String n, String v, String a, String s, String p, String o,
			String t, String f, String b) {

		this.name = n;
		this.vorname = v;
		this.anrede = a;
		this.strasse = s;
		this.plz = p;
		this.ort = o;
		this.telefon = t;
		this.fax = f;
		this.bemerkung = b;
	}
}