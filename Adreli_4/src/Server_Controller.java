/**
 * 	PROGRAMMIEREN UND MODELLIEREN 2
 * 	==================================================
 * 	PROJEKT 4
 * 	ADRELI_4_GUI
 * 	==================================================
 * 	AUTOREN
 * 	Arianit Metaj, Beyza Kalinci, Hakan Celik
 * 	==================================================
 * 	DATUM
 * 	19.12.2017
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
	boolean datenbank = false; // Welche Dateibasis wurde gewählt
	Connection conn = null; // Datenbankverbindung

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
			final String port = "3306";
			final String dbname = "adreli_5";
			final String user = "root";
			final String password = "";

			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (Exception e) {
				try {
					throw new Fehler_Exception();
				} catch (Fehler_Exception ex) {
				}
			}

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

			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("CREATE TABLE OBST (NAME VARCHAR(32),PREIS FLOAT)");
				stmt.executeUpdate("INSERT INTO OBST VALUES ('Adreli', 2.50)");
			} catch (SQLException e) {
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
}