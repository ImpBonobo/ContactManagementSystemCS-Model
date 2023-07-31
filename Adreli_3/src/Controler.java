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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Handhabung von mehreren Clients implementiert. 
 * verwendet wird Jeweils ein Thread vom Server,
 * für jeweils einen Client. PORT: 5678
 * @author Arianit Metaj
 */

public class Controler {
	public static void main(String[]args) {

		System.out.println("Server gestartet...");
		
		// Endlosschleife um jeden Client empfangen zu können.
		
		while (true) {

			ServerSocket serverSocket;
	
			try {
					// Erstellung des ServerSockets
				serverSocket = new ServerSocket(5678);
				
					// Warten auf Client
				Socket clientSocket = serverSocket.accept();
				
					// IP Adresse des Clients
				InetAddress ip_adress = clientSocket.getInetAddress();
				
				InputStream in = clientSocket.getInputStream();
				OutputStream out = clientSocket.getOutputStream();
				
					// Thread erstellen
				Server server = new Server(in, out);
					
					// Thread starten
				server.start();
				
				/**
				 * Protokoll der Aktivitäten
				 */
				
				// Falls Datei nicht vorhanden
				
				File log = new File ("log.csv");
				
				if (!log.exists()) {
					try	{
						log.createNewFile();
					}
					catch (IOException e) {
						System.out.println("Logfile konnte nicht erstellt " +
								"werden.");
					}
				}
				server.ip_adress(ip_adress);
	
			} catch (IOException e) {	}
		}
	}
}