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

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

/**
 * Klasse benötigt für die Handhabung von mehreren Clients. Für jeden Client
 * wird ein Thread von Server_Funktion erstellt.
 * 
 * @author Arianit Metaj, Beyza Kalinci, Hakan Celik
 *
 */

public class Server_Controller extends Thread {

	static boolean error = false; // Ist ein Fehler aufgetreten ?
	static boolean datenbank = false; // Welche Dateibasis wurde gewählt
	static Connection conn = null; // Datenbankverbindung

	/**
	 * Automatischer Aufruf, sobald Server_GUI den Server startet. Für jeden
	 * Client wird ein Thread von Server_Funktion erstellt. Außerdem wird ggf.
	 * log.csv erstellt.
	 */

	public void run() {

		datenbank = Server_GUI.datenbank;

		if (datenbank == true) {

			// Variablen für Datenbankanbindung
			final String hostname = "localhost";
			final String port = "3307";
			final String dbname = "adreli";
			final String user = "root";
			final String password = "Labinot5?";

			// Datenbank Treiber laden
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (Exception e) {
				try {
					throw new Fehler_Exception();
				} catch (Fehler_Exception ex) {
				}
			}

			// Verbindung zur Datenbank aufbauen
			try {
				String url = "jdbc:mysql://" + hostname + ":" + port + "/"
						+ dbname;
				conn = DriverManager.getConnection(url, user, password);
			} catch (SQLException sqle) {
				try {
					throw new Fehler_Exception();
				} catch (Fehler_Exception ex) {
				}
			}
		}

		ServerSocket serverSocket = null;

		try {
			// Erstellung des ServerSockets
			serverSocket = new ServerSocket(Server_GUI.getPort());
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Endlosschleife, da Programm ständig auf Clients warten soll
		while (true) {
			try {
				// Warten auf Client
				Socket clientSocket = serverSocket.accept();
				// IP Adresse des Clients
				InetAddress ip_adresse = clientSocket.getInetAddress();
				String ip_str = ip_adresse.toString();
				ip_str = ip_str.substring(1);
				// Kommunikation
				InputStream in = clientSocket.getInputStream();
				OutputStream out = clientSocket.getOutputStream();
				// Thread erstellen
				Server_Funktion funktion = new Server_Funktion(in, out, conn);
				// Thread starten
				funktion.start();
				// IP Adresse des Clients übergeben
				funktion.setIP(ip_str);
			} catch (IOException e) {
				error = true; // Fehler ist aufgetreten
			}
		}
	}

	// =============================================================================
	// =================== IMPORTIEREN ====================
	// =============================================================================

	/**
	 * Inhalt einer vorhandenen CSV Datei wird in die Datenbank geschrieben.
	 * Verzeichnis und Dateiname werden bei Methodenaufruf mitübergeben.
	 */

	public static void importieren(String d, String f) {

		boolean error = false;
		String zeile = null;
		Statement stmt = null;

		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			error = true;
		}

		try {
			RandomAccessFile datensatz = new RandomAccessFile(d + f, "rw");
			datensatz.seek(0);
			while ((zeile = datensatz.readLine()) != null) {
				String[] daten = zeile.split(";");

				try {
					stmt.executeUpdate("INSERT INTO adreli5_gr10 VALUES ('"
							+ daten[0] + "', '" + daten[1] + "', '" + daten[2]
							+ "', '" + daten[3] + "', '" + daten[4] + "', '"
							+ daten[5] + "', '" + daten[6] + "', '" + daten[7]
							+ "', '" + daten[8] + "')");
				} catch (SQLException e) {
					error = true;
				}
			}
			datensatz.close();
		} catch (FileNotFoundException fnfex) {
			error = true;
		} catch (IOException ioex) {
			error = true;
		}

		if (error == false) {
			new OKDialog(null, "Importieren", "" + f
					+ " erfolgreich importiert.");
		} else {
			try {
				throw new Fehler_Exception();
			} catch (Fehler_Exception ex) {
			}
		}
	}

	// =============================================================================
	// =================== EXPORTIEREN ====================
	// =============================================================================

	/**
	 * Alle Personen der Datenbank werden in eine CSV Datei exportiert. Hierfür
	 * wird im gewählten Verzeichnis die Datei adreli.csv erstellt und befüllt.
	 */

	public static void exportieren() {

		Statement stmt = null;
		ResultSet p_result = null;
		boolean error = false;
		FileWriter out;

		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			error = true;
		}

		try {
			p_result = stmt.executeQuery("SELECT * FROM adreli5_gr10");
		} catch (SQLException e) {
			error = true;
		}

		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"ddMMyyyy_HHmmss");

		String dateiname = sdf.format(now) + ".csv";

		try {
			out = new FileWriter(dateiname, true);
			while (p_result.next()) {
				out.write(p_result.getString("Name") + ";"
						+ p_result.getString("Vorname") + ";"
						+ p_result.getString("Anrede") + ";"
						+ p_result.getString("Strasse") + ";"
						+ p_result.getString("PLZ") + ";"
						+ p_result.getString("Ort") + ";"
						+ p_result.getString("Telefon") + ";"
						+ p_result.getString("Fax") + ";"
						+ p_result.getString("Bemerkung"));
				out.append(System.getProperty("line.separator"));
			}
			out.close();
		} catch (SQLException e) {
			error = true;
		} catch (IOException e1) {
			error = true;
		}

		if (error == false) {
			new OKDialog(null, "Exportieren", "Erfolgreich in " + dateiname
					+ " exportiert.");
		} 
else {
			try {
				throw new Fehler_Exception();
			} catch (Fehler_Exception ex) {
			}
		}
	}
}