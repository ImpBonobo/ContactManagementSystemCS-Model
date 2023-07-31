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

import java.awt.*;
import java.awt.event.*;

/**
 * Klasse zur Erstellung der graphischen Oberfl�che f�r den Server. Au�erdem
 * Handhabung von mehreren Clients durch Threads.
 * 
 * @author Arianit Metaj, Beyza Kalinci, Hakan Celik
 * 
 */

@SuppressWarnings("serial")
public class Server_GUI extends Frame {

	static boolean start = false; // Server aktiv ?
	static String verzeichnis = ""; // adreli.csv Path
	static String datei = "adreli.csv"; // Dateiname
	static int port; // Port von Server
	static int client = 0; // Anzahl verbundene Clients
	static TextArea log; // TextArea Protokoll
	static Label anzahl_clients; // Label f�r Anzahl Clients
	static boolean datenbank = false; // Datenbankanbindung

	public Server_GUI() {

		this.setSize(600, 500); // Fenstergr��e
		this.setTitle("ADRELI - Server"); // Fenstertitel
		this.setLocationRelativeTo(null); // Fenster zentrieren
		this.setResizable(false); // Feste Fenstergr��e
		Font schrift = new Font("Calibri", Font.TRUETYPE_FONT, 11);
		this.setFont(schrift); // Schriftart
		this.setForeground(Color.gray); // Schriftfarbe
		this.setBackground(new Color(25, 25, 25));
		this.setLayout(null); // Layout deaktivieren
								// Schlie�enbutton
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				stop();
			}
		});

		// ---- Erstellung der Men�leiste ----

		// Men�leiste
		MenuBar menueLeiste = new MenuBar();

		// Men�
		Menu server = new Menu("Server");
		Menu basis = new Menu("Datenbank");
		Menu help = new Menu("Hilfe");

		// Eintr�ge
		MenuItem starten = new MenuItem("Starten");
		MenuItem stop = new MenuItem("Stoppen");
		MenuItem importieren = new MenuItem("Importieren");
		MenuItem exportieren = new MenuItem("Exportieren");
		MenuItem about = new MenuItem("Autoren");

		// Eintr�ge dem Men� hinzuf�gen
		server.add(starten);
		server.addSeparator();
		server.add(stop);
		basis.add(importieren);
		basis.addSeparator();
		basis.add(exportieren);
		help.add(about);

		// Men�s der Leiste hinzuf�gen
		menueLeiste.add(server);
		menueLeiste.add(basis);
		menueLeiste.add(help);

		// Funktionen sperren
		if (start == true) { // Server an
			starten.setEnabled(false);
		} else { // Server aus
			stop.setEnabled(false);
		}

		// Datenbank sperren
		if (datenbank == false) {
			basis.setEnabled(false);
		} else {
			basis.setEnabled(true);
		}

		// Men�leiste einf�gen
		this.setMenuBar(menueLeiste);

		// ActionListener Starten
		starten.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				starten();
			}
		});

		// ActionListener Stoppen
		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				stop();
			}
		});

		// ActionListener About
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				new OKDialog(null, "Autoren",
						"� Arianit Metaj, Beyza Kalinci, Hakan Celik");
			}
		});

		// ActionListener Import
		importieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				importieren();
			}
		});

		// ActionListener Export
		exportieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				exportieren();
			}
		});
	}

	// =============================================================================
	// ======================= MAINMETHODE ============================
	// =============================================================================

	/**
	 * Ein Objekt von der Server GUI wird erstellt und die Methode start()
	 * aufgerufen.
	 */

	public static void main(String[] args) {

		Server_GUI instanz = new Server_GUI();
		instanz.start();
	}

	// =============================================================================
	// ======================== START ========================
	// =============================================================================

	/**
	 * Diese Methode wird jeweils nach den Starten und Stoppen aufgerufen. Der
	 * alte Frame wird geschlossen und ein neuer erstellt. Dadurch lassen sich
	 * verschiedene Eigenschaften in Abh�ngigkeit von der Variable start in die
	 * GUI einbauen, wie beispielsweise das Sperren von Funktionen bei keiner
	 * Verbindung. Anschlie�end wird das Men� aufgerufen
	 */

	public void start() {

		this.dispose();
		Server_GUI start = new Server_GUI();
		start.setVisible(true);
		start.menu();
	}

	// =============================================================================
	// ======================== STOP ========================
	// =============================================================================

	/**
	 * Methode wird beim Stoppen des Servers aufgerufen. (Ebenfalls bei Beenden)
	 * Server beendet alle Threads. Sollten noch Clients mit dem Server
	 * verbunden sein, wird der Benutzer aufgefordert, das Beenden zu
	 * Best�tigen.
	 */

	public void stop() {

		if (client > 0) {
			// Falls noch Clients verbunden sind
			JaNein_Dialog dlg = new JaNein_Dialog(null, "Warnung",
					"Es sind noch " + client + " Client verbunden. "
							+ "Fortfahren?");
			if (dlg.getAntwort() == true) // Dr�cken von JA
				Server_Funktion.ende = true;
		} else {
			// Keine Clients verbunden
			JaNein_Dialog dlg = new JaNein_Dialog(null, "Programm beenden",
					"Wollen Sie den Server wirklich stoppen?");
			if (dlg.getAntwort() == true) // Dr�cken von JA
				System.exit(0);
		}
	}

	// =============================================================================
	// ======================= MENU ============================
	// =============================================================================

	/**
	 * Startseite der Server GUI. Meldung bei deaktiviertem Server. Andernfalls
	 * TextArea mit Protokolldaten.
	 */

	public void menu() {

		this.removeAll(); // Fenster leeren
		headline("Status"); // �berschrift

		if (start == true) {
			// Anzahl verbundener Clients
			anzahl_clients = new Label("Verbundene Clients: " + client);
			anzahl_clients.setBounds(50, 100, 150, 20);
			anzahl_clients.setVisible(true);
			this.add(anzahl_clients);
			// TextArea Protokoll
			log = new TextArea();
			log.setBounds(50, 130, 500, 300);
			Font schrift = new Font("Monospaced", Font.PLAIN, 11);
			log.setFont(schrift);
			log.setEditable(false);
			log.setForeground(Color.black);
			log.setBackground(new Color(240, 240, 240));
			log.setVisible(true);
			this.add(log);

			// Aktuelle Uhrzeit / Datum
			java.util.Date now = new java.util.Date();
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
					"dd.MM.yyyy HH:mm:ss");
			// Erster Eintrag
			log.setText(sdf.format(now) + "\tlocalhost\tServer gestartet\n");

		} else {
			Label text;
			text = new Label("Server ist im Moment deaktiviert.");
			text.setBounds(50, 80, 400, 50);
			text.setVisible(true);
			this.add(text);
		}
	}

	/**
	 * TextArea werden Aktivit�ten des Clients hinzugef�gt.
	 * 
	 * @param i
	 *            IP Adresse des Clients
	 * @param a
	 *            Von Client aufgerufene Funktion
	 */

	public static void addLog(String i, String a) {

		// Aktuelle Uhrzeit / Datum
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"dd.MM.yyyy HH.mm.ss");

		String bisher = log.getText();
		log.setText(bisher + sdf.format(now) + "\t" + i + "\t" + a + "\n");
	}

	// =============================================================================
	// ======================= PAINTMETHODE ============================
	// =============================================================================

	/**
	 * Methode f�r das Zeichen innerhalb des Frames. Notwendig f�r das Erstellen
	 * des Hintergrundbildes und des Textfooters.
	 * 
	 * @param bg
	 *            Hintergrundbild background.png
	 */

	public void paint(Graphics g) {

		// Hintergrundbild
		Image bg = this.getToolkit().getImage("./pic.png");
		g.drawImage(bg, (this.getWidth() - 600), (this.getHeight() - 400), this);

		// Footer
		g.drawString("ADRELI_4 � 2018", 500, this.getHeight() - 35);
		g.drawString("Arianit Metaj, Beyza Kalinci, Hakan Celik", 375,
				this.getHeight() - 20);
	}

	// =============================================================================
	// ======================= HEADLINE ============================
	// =============================================================================

	/**
	 * Methode f�r das Erstellen der �berschift.
	 * 
	 * @param s
	 *            �bergabe der �berschrift als String bei Aufruf
	 */

	public void headline(String s) {

		Label headline = new Label(s); // Label
		headline.setBounds(50, 70, this.getWidth(), 20); // Position
		Font headfont = new Font("Calibri", Font.TRUETYPE_FONT, 16);
		headline.setFont(headfont); // Schriftart
		headline.setForeground(new Color(0, 127, 222)); // Schriftfarbe
		headline.setVisible(true);
		this.add(headline);
	}

	// =============================================================================
	// ======================= STARTEN ============================
	// =============================================================================

	/**
	 * Server wird gestartet. Eingabe des Ports, au�erdem Wahl von adreli.csv
	 * und Datenbasis.
	 */

	public void starten() {

		this.removeAll(); // Fenster leeren
		headline("Server starten"); // �berschrift

		// Label Port
		Label port_l = new Label("Port:");
		port_l.setBounds(50, 100, 150, 20);
		port_l.setVisible(true);
		this.add(port_l);
		// TextField Port
		final TextField port_t = new TextField();
		port_t.setBounds(200, 100, 120, 20);
		port_t.setText("56789");
		port_t.setVisible(true);
		this.add(port_t);

		// Label datenbasis
		Label datenbasis_l = new Label("Datenbasis:");
		datenbasis_l.setBounds(50, 125, 150, 20);
		datenbasis_l.setVisible(true);
		this.add(datenbasis_l);
		// Ausklappbare Liste
		final Choice datenbasis = new Choice();
		datenbasis.add("Datei");
		datenbasis.add("Datenbank");
		datenbasis.setBounds(200, 125, 120, 20);
		datenbasis.setVisible(true);
		this.add(datenbasis);

		// Label File
		final Label file_l = new Label("Vorhandene Datei laden:");
		file_l.setBounds(50, 150, 150, 20);
		file_l.setVisible(true);
		this.add(file_l);
		// Knopf File
		final Button laden = new Button("Datei laden");
		laden.setBounds(200, 150, 120, 20);
		laden.setForeground(Color.black);
		laden.setVisible(true);
		this.add(laden);
		// Label Info
		final Label info_l = new Label("Alternativ wird Datei aus "
				+ "selben Verzeichnis verwendet und ggf. erstellt.");
		info_l.setBounds(200, 175, 380, 20);
		info_l.setBackground(new Color(203, 227, 255));
		info_l.setAlignment(Label.CENTER);
		info_l.setVisible(true);
		this.add(info_l);

		// ItemListener f�r Datenbasisauswahl
		datenbasis.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {

				if (datenbasis.getSelectedItem().equals("Datenbank")) {
					laden.setEnabled(false);
					info_l.setVisible(false);
				} else {
					laden.setEnabled(true);
					info_l.setVisible(true);
				}
			}
		});

		// FileDialog
		final FileDialog fd = new FileDialog(this, "�ffnen", FileDialog.LOAD);
		fd.setSize(300, 200);
		fd.setResizable(false);

		// ActionListener Laden
		laden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				fd.setVisible(true);

				String chosenDir = fd.getDirectory();
				String chosenFile = fd.getFile();

				if (!(chosenFile == null)) {
					setDatei(chosenDir, chosenFile);
					info_l.setText(chosenFile + " wurde gew�hlt.");
				}
			}
		});

		// Server starten
		Button knopf = new Button("Server starten");
		knopf.setBounds(50, 225, 125, 25);
		knopf.setForeground(Color.black);
		knopf.setVisible(true);
		this.add(knopf);

		// Fehlermeldungen
		final Label port_fehler; // Port falsch
		final Label file_fehler; // Falsche Datei
		port_fehler = new Label("Port darf nur aus 5 Zahlen bestehen.");
		file_fehler = new Label("Nur Dateien vom Typ CSV unterst�tzt.");

		// ActionListener Laden
		knopf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				boolean error = false; // Eingaben korrekt ?

				// Fehler sollen nach Knopfdruck zun�chst wieder
				// ausgeblendet werden
				port_fehler.setVisible(false);
				file_fehler.setVisible(false);

				// �berpr�fung des Ports
				if ((!port_t.getText().matches("[[0-9]*]{1,5}"))) {
					port_fehler.setBounds(330, 100, 250, 20);
					port_fehler.setBackground(new Color(255, 190, 190));
					port_fehler.setAlignment(Label.CENTER);
					port_fehler.setVisible(true);
					add(port_fehler);
					error = true;
				}

				// �berpr�fung der Datenbasis
				if (datenbasis.getSelectedItem().equals("Datenbank")) {
					datenbank = true;
				}

				// �berpr�fung der Datei
				if (!(datei.substring(datei.length() - 3, datei.length())
						.equals("csv"))) {
					file_fehler.setBounds(330, 150, 250, 20);
					file_fehler.setBackground(new Color(255, 190, 190));
					file_fehler.setAlignment(Label.CENTER);
					file_fehler.setVisible(true);
					add(file_fehler);
					error = true;
				}

				// �berpr�fung alle Eingaben korrekt ?
				if (error == false) {
					setPort(Integer.parseInt(port_t.getText()));
					Server_Controller control = new Server_Controller();
					control.start();

					if (Server_Controller.error == false) {
						start = true;
						start();
					} else {
						try {
							throw new Fehler_Exception();
						} catch (Fehler_Exception e) {
						}
					}
				}
			}
		});
	}

	/**
	 * Setter Methode f�r die Variablen verzeichnis und datei.
	 * 
	 * @param v
	 *            String Verzeichnis der csv Datei
	 * @param d
	 *            String Dateiname
	 */

	public void setDatei(String v, String d) {

		verzeichnis = v;
		datei = d;
	}

	/**
	 * Getter Methode f�r das Verzeichnis der zu verwendeten csv Datei. Sollte
	 * keine Datei gew�hlt worden sein, wird das selbe Verzeichnis wie die java
	 * Files gew�hlt.
	 */

	public static String getVerzeichnis() {

		return verzeichnis;
	}

	/**
	 * Getter Methode f�r den Dateinamen. Datei kann nur mit *.csv enden. Sollte
	 * keine Datei gew�hlt worden sein, wird adreli.csv verwendet.
	 */

	public static String getDatei() {

		return datei;
	}

	/**
	 * Setter Methode f�r Server Port
	 * 
	 * @param p
	 *            Port als Integer
	 */

	public static void setPort(int p) {

		port = p;
	}

	/**
	 * Getter Methode f�r Server Port
	 * 
	 * @return Port als Integer
	 */

	public static int getPort() {

		return port;
	}

	/**
	 * Variable Client wird um 1 erh�ht. client beschreibt die Anzahl der
	 * verbundenen Clients zum Server. Neuer Client verbunden.
	 */

	public static void addClient() {

		client++;
		anzahl_clients.setText("Verbundene Clients: " + client);
	}

	/**
	 * Variable Client wird um 1 minimiert. client beschreibt die Anzahl der
	 * verbundenen Clients zum Server. Client getrennt.
	 */

	public static void minClient() {

		client--;
		anzahl_clients.setText("Verbundene Clients: " + client);
	}

	// =============================================================================
	// ======================== IMPORT ========================
	// =============================================================================

	/**
	 * Methode soll es erm�glichen vorhandene Personendaten in die Datenbank zu
	 * schreiben. Hierf�r wird ein neues Fenster ge�ffnet, eine CSV Datei
	 * gew�hlt und der Inhalt der Datenbank angef�gt.
	 */

	public void importieren() {

		FileDialog fd = new FileDialog(this, "Importieren", FileDialog.LOAD);
		fd.setSize(300, 200);
		fd.setResizable(false);
		fd.setVisible(true);

		String chosenDir = fd.getDirectory();
		String chosenFile = fd.getFile();

		if (!(chosenFile == null)) {
			if (fd.getFile()
					.substring(fd.getFile().length() - 3, fd.getFile().length())
					.equals("csv")) {
				Server_Funktion.importieren(chosenDir, chosenFile);
			} else {
				new OKDialog(null, "Importieren",
						"Nur Dateien vom Typ csv erlaubt.");
			}
		}
	}

	// =============================================================================
	// ======================== EXPORT ========================
	// =============================================================================

	/**
	 * Methode soll es erm�glichen die Personeneintr�ge der Datenbank in eine
	 * CSV Datei zu exportieren. adreli.csv wird anschlie�end im gew�hlten
	 * Verzeichnis erstellt und bef�llt.
	 */

	public void exportieren() {

		FileDialog fd = new FileDialog(this, "Exportieren", FileDialog.LOAD);
		fd.setSize(300, 200);
		fd.setResizable(false);
		fd.setVisible(true);

		String chosenDir = fd.getDirectory();

		if (!(chosenDir == null)) {
			Server_Funktion.exportieren(chosenDir);
		} else {
			new OKDialog(null, "Exportieren", "Exportieren fehlgeschlagen.");
		}
	}
}