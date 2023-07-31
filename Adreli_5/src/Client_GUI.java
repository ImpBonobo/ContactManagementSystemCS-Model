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

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

/**
 * Klasse zur Erstellung der graphischen Oberfläche für den Client.
 * 
 * @author Arianit Metaj, Beyza Kalinci, Hakan Celik
 * 
 */

@SuppressWarnings("serial")
public class Client_GUI extends Frame {

	static boolean verbindung; // Verbindung aktiv?
	int zaehler; // Notwendig für Records auflisten

	// =============================================================================
	// ======================== KONSTRUKTOR ========================
	// =============================================================================

	public Client_GUI() {

		this.setSize(600, 500); // Fesntergröße
		this.setTitle("ADRELI - Client"); // Fesntertitel
		this.setLocationRelativeTo(null); // Fesnter zentrieren
		Image icon = this.getToolkit().getImage("./icon.png");
		this.setIconImage(icon); // Icon hinzufügen
		this.setResizable(false); // Feste Fenstergröße
		Font schrift = new Font("Calibri", Font.TRUETYPE_FONT, 11);
		this.setFont(schrift); // Schriftart
		this.setBackground(new Color(25,25,112));
		this.setForeground(Color.gray); // Schriftfarbe
		this.setLayout(null); // Layout deaktivieren
								// Schließenbutton
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				beenden();
			}
		});

		// ---- Erstellung der Menüleiste ----

		// Menüleiste
		MenuBar menueLeiste = new MenuBar();

		// Menü
		Menu client = new Menu("Client");
		Menu funktion = new Menu("Funktionen");
		Menu help = new Menu("Hilfe");

		// Einträge
		MenuItem verbinden = new MenuItem("Verbinden");
		MenuItem trennen = new MenuItem("Trennen");
		MenuItem beenden = new MenuItem("Beenden");
		MenuItem person_aufnehmen = new MenuItem("Person aufnehmen");
		MenuItem records_auflisten = new MenuItem("Records auflisten");
		MenuItem records_sichern = new MenuItem("Records sichern");
		MenuItem records_laden = new MenuItem("Records laden");
		MenuItem datei_loeschen = new MenuItem("Daten löschen");
		MenuItem about = new MenuItem("Über Adreli");

		// Einträge dem Menü hinzufügen
		client.add(verbinden);
		client.add(trennen);
		client.addSeparator();
		client.add(beenden);
		funktion.add(person_aufnehmen);
		funktion.add(records_auflisten);
		funktion.add(records_sichern);
		funktion.add(records_laden);
		funktion.add(datei_loeschen);
		help.add(about);

		// Menüs der Leiste hinzufügen
		menueLeiste.add(client);
		menueLeiste.add(funktion);
		menueLeiste.add(help);

		// Funktionen sperren
		if (verbindung == false) { // Verbindung inaktiv
			funktion.setEnabled(false);
			trennen.setEnabled(false);
		} else { // Verbindung aktiv
			funktion.setEnabled(true);
			trennen.setEnabled(true);
			verbinden.setEnabled(false);
		}

		// Menüleiste einfügen
		this.setMenuBar(menueLeiste);

		// ActionListener Beenden
		beenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				beenden();
			}
		});

		// ActionListener About
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				new OKDialog(null, "Über Adreli",
						"© Arianit Metaj, Beyza Kalinci, Hakan Celik");
			}
		});

		// ActionListener Verbinden
		verbinden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				verbinden();
			}
		});

		// ActionListener Trennen
		trennen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				trennen();
			}
		});

		// ActionListener Person_Aufnehmen
		person_aufnehmen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				person_aufnehmen();
			}
		});

		// ActionListener Records_Auflisten
		records_auflisten.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				records_auflisten();
			}
		});

		// ActionListener Records_Sichern
		records_sichern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					records_sichern();
				} catch (Fehler_Exception e) {
				}
			}
		});

		// ActionListener Records_Laden
		records_laden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					records_laden();
				} catch (Fehler_Exception e) {
				}
			}
		});

		// ActionListener Datei_Löschen
		datei_loeschen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					datei_loeschen();
				} catch (Fehler_Exception e) {
				}
			}
		});
	}

	// =============================================================================
	// ======================= MAINMETHODE ============================
	// =============================================================================

	/**
	 * Ein Objekt von der Client GUI wird erstellt und die Methode start()
	 * aufgerufen.
	 */

	public static void main(String[] args) {

		Client_GUI instanz = new Client_GUI();
		instanz.start();
	}

	// =============================================================================
	// ======================== START ========================
	// =============================================================================

	/**
	 * Diese Methode wird jeweils nach den Verbinden uns trennen aufgerufen. Der
	 * alte Frame wird geschlossen und ein neuer erstellt. Dadurch lassen sich
	 * verschiedene Eigenschaften in Abhängigkeit von der Variable verbindung in
	 * die GUI einbauen, wie beispielsweise das Sperren von Funktionen bei
	 * keiner Verbindung. Anschließend wird das Menü aufgerufen
	 */

	public void start() {

		this.dispose();
		Client_GUI start = new Client_GUI();
		start.setVisible(true);
		start.menu();
	}

	// =============================================================================
	// ======================= MENU ============================
	// =============================================================================

	/**
	 * Startseite der Client GUI mit ensprechender Meldung, je nach bestehender
	 * Verbindung. Desweiteren Erstellung eines PopUpMenüs bei aktiver
	 * Verbindung.
	 */

	public void menu() {

		this.removeAll(); // Fenster leeren
		headline("Willkommen"); // Überschrift

		Label text; // Label für Textausgabe

		if (verbindung == true) {

			text = new Label(" ");
			Label server = new Label("Server: " + Client_Funktion.ip);
			Label port = new Label("Port: " + Client_Funktion.port);
			server.setBounds(75, 120, 350, 10);
			port.setBounds(75, 135, 350, 10);
			server.setVisible(true);
			port.setVisible(true);
			this.add(server);
			this.add(port);

			// ---- Erstellung des PopUpMenüs ----

			final JPopupMenu popmenu = new JPopupMenu();

			// Einträge
			JMenuItem person_aufnehmen = new JMenuItem("Person aufnehmen");
			JMenuItem records_auflisten = new JMenuItem("Records auflisten");
			JMenuItem records_sichern = new JMenuItem("Records sichern");
			JMenuItem records_laden = new JMenuItem("Records laden");
			JMenuItem datei_loeschen = new JMenuItem("Daten loeschen");

			// Einträge hinzufügen
			popmenu.add(person_aufnehmen);
			popmenu.add(records_auflisten);
			popmenu.add(records_sichern);
			popmenu.add(records_laden);
			popmenu.add(datei_loeschen);

			// MouseListener für PopUpMenu
			addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent me) {
					if (me.isPopupTrigger())
						popmenu.show(me.getComponent(), me.getX(), me.getY());
				}
			});

			// ActionListener Person_Aufnehmen
			person_aufnehmen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					person_aufnehmen();
				}
			});

			// ActionListener Records_Auflisten
			records_auflisten.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					records_auflisten();
				}
			});

			// ActionListener Records_Sichern
			records_sichern.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					try {
						records_sichern();
					} catch (Fehler_Exception e) {
					}
				}
			});

			// ActionListener Records_Laden
			records_laden.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					try {
						records_laden();
					} catch (Fehler_Exception e) {
					}
				}
			});

			// ActionListener Datei_Löschen
			datei_loeschen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					try {
						datei_loeschen();
					} catch (Fehler_Exception e) {
					}
				}
			});

		} else {
			text = new Label("Bitte stellen Sie zunächst "
					+ "eine Verbindung zu einem Server her.");
		}

		text.setBounds(75, 80, 350, 50);
		text.setVisible(true);
		this.add(text);
	}

	// =============================================================================
	// ======================= PAINTMETHODE ============================
	// =============================================================================

	/**
	 * Methode für das Zeichen innerhalb des Frames. Notwendig für das Erstellen
	 * des Hintergrundbildes und des Textfooters.
	 * 
	 * @param bg
	 *            Hintergrundbild background.png
	 */

	public void paint(Graphics g) {

		// Footer
		g.drawString("ADRELI", 375, this.getHeight() - 50);
		g.drawString("Gruppe 10:", 375,
				this.getHeight() - 35);
		g.drawString("Arianit Metaj, Beyza Kalinci, Hakan Celik", 375,
				this.getHeight() - 20);
	}

	// =============================================================================
	// ======================= HEADLINE ============================
	// =============================================================================

	/**
	 * Methode für das Erstellen der Überschift.
	 * 
	 * @param s
	 *            Übergabe der Überschrift als String bei Aufruf
	 */

	public void headline(String s) {

		Label headline = new Label(s); // Label
		headline.setBounds(75, 70, this.getWidth(), 20); // Position
		Font headfont = new Font("Calibri", Font.TRUETYPE_FONT, 25);
		headline.setFont(headfont); // Schriftart
		headline.setForeground(new Color(240,230,140)); // Schriftfarbe
		headline.setVisible(true);
		this.add(headline);
	}

	// =============================================================================
	// ======================= VERBINDEN ============================
	// =============================================================================

	/**
	 * Methode für die Herstellung einer Verbindung zu einem bekannten Server.
	 * Notwendig sind IP und Port.
	 */

	public void verbinden() {

		this.removeAll(); // Fenster leeren
		headline("Verbinden"); // Überschrift

		// Label IP-Adresse
		Label ip_l = new Label("IP-Adresse des Servers:");
		ip_l.setBounds(75, 100, 150, 20);
		ip_l.setVisible(true);
		this.add(ip_l);
		// TextField IP-Adresse
		final TextField ip_t = new TextField();
		ip_t.setBounds(225, 100, 120, 20);
		ip_t.setText("localhost");
		ip_t.setVisible(true);
		this.add(ip_t);

		// Label Port
		Label port_l = new Label("Port:");
		port_l.setBounds(75, 125, 150, 20);
		port_l.setVisible(true);
		this.add(port_l);
		// TextField Port
		final TextField port_t = new TextField();
		port_t.setBounds(225, 125, 120, 20);
		port_t.setText("56789");
		port_t.setVisible(true);
		this.add(port_t);

		// Knopf Verbinden
		Button knopf = new Button("Verbinden");
		knopf.setBounds(225, 225, 100, 25);
		knopf.setForeground(Color.black);
		knopf.setVisible(true);
		this.add(knopf);

		knopf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				boolean error = false; // Eingaben korrekt ?

				final Label ip_fehler; // IP-Adresse falsch
				ip_fehler = new Label("IP-Adresse darf nicht leer sein.");
				final Label port_fehler; // Port falsch
				port_fehler = new Label("Port darf nur aus Zahlen bestehen.");

				// Fehler sollen nach Knopfdruck zunächst wieder
				// ausgeblendet werden
				ip_fehler.setVisible(false);
				port_fehler.setVisible(false);

				// Überprüfung der IP Adresse
				if (ip_t.getText().equals("")) {
					ip_fehler.setBounds(330, 100, 250, 20);
					ip_fehler.setBackground(new Color(255, 190, 190));
					ip_fehler.setAlignment(Label.CENTER);
					ip_fehler.setVisible(true);
					add(ip_fehler);
					error = true;
				}

				// Überprüfung des Ports
				if ((!port_t.getText().matches("[0-9]*") || (port_t.getText()
						.equals("")))) {
					port_fehler.setBounds(330, 125, 250, 20);
					port_fehler.setBackground(new Color(255, 190, 190));
					port_fehler.setAlignment(Label.CENTER);
					port_fehler.setVisible(true);
					add(port_fehler);
					error = true;
				}

				// Überprüfung alle Eingaben korrekt ?
				if (error == false) {
					// Aufruf CLIENT_FUNKTION verbinden()
					Client_Funktion.verbinden(ip_t.getText(), port_t.getText());
					// Fehler beim Verbinden
					if (Client_Funktion.error == false) {
						verbindung = true;
						start();
					} else {
						new OKDialog(null, "Verbindungsfehler",
								"Verbindung konnte nicht aufgebaut werden.");
						verbinden();
					}
				}
			}
		});

	}

	// =============================================================================
	// ======================= TRENNEN ============================
	// =============================================================================

	/**
	 * Trennen der Verbindung zum Server. Server wird hierfür außerdem ein Wert
	 * übermittelt um das Trennen des Clients mitzuteilen. Methode wird beim
	 * Beenden des Client ebenfalls aufgerufen. Funktionen befinden sich
	 * innerhalb von CLIENT_FUNKTION. Variable verbindung wird außerdem auf
	 * false gesetzt.
	 */

	public void trennen() {

		checkConnection();
		Client_Funktion.senden(7);
		Client_Funktion.trennen();
		verbindung = false;
		start();
	}

	// =============================================================================
	// ======================= BEENDEN ============================
	// =============================================================================

	/**
	 * Methode wird durch das Drücken von X oder Beenden aufgerufen. Zunächst
	 * wird ein JA_NEIN_DIALOG erstellt. Anschließend evtl. Aufruf von trennen()
	 * und schließen des Frames + Programmes.
	 */

	public void beenden() {

		JaNein_Dialog dlg = new JaNein_Dialog(null, "Programm beenden",
				"Wollen Sie das Programm wirklich beenden?");

		if (dlg.getAntwort() == true) { // Drücken von JA
			if (verbindung == true) // Bestehende Verbindung ?
				trennen();
			dispose(); // Frame schließen
			System.exit(0); // Programm beenden
		}
	}

	// =============================================================================
	// ==================== PERSON AUFNEHMEN ====================
	// =============================================================================

	/**
	 * Aufnehmen einer neuen Person. Ausgabe von Label und TextField für jede
	 * benötigte Eingabe. Anschließend Prüfen der Eingaben, ggf. Ausgabe von
	 * Fehlermeldung(en).
	 */

	public void person_aufnehmen() {

		checkConnection(); // Ist Server noch aktiv ?

		Client_Funktion.senden(1); // Eingabe an Server weiterleiten

		this.removeAll(); // Fesnter leeren
		headline("Person aufnehmen"); // Überschrift

		// Label Name
		Label name_l = new Label("Name:");
		name_l.setBounds(75, 100, 150, 20);
		name_l.setVisible(true);
		this.add(name_l);

		// Label Vorname
		Label vorname_l = new Label("Vorname:");
		vorname_l.setBounds(75, 125, 150, 20);
		vorname_l.setVisible(true);
		this.add(vorname_l);

		// Label Anrede
		Label anrede_l = new Label("Anrede:");
		anrede_l.setBounds(75, 150, 150, 20);
		anrede_l.setVisible(true);
		this.add(anrede_l);

		// Label Strasse
		Label strasse_l = new Label("Straße:");
		strasse_l.setBounds(75, 175, 150, 20);
		strasse_l.setVisible(true);
		this.add(strasse_l);

		// Label PLZ
		Label plz_l = new Label("PLZ:");
		plz_l.setBounds(75, 200, 150, 20);
		plz_l.setVisible(true);
		this.add(plz_l);

		// Label Ort
		Label ort_l = new Label("Ort:");
		ort_l.setBounds(75, 225, 150, 20);
		ort_l.setVisible(true);
		this.add(ort_l);

		// Label Telefon
		Label telefon_l = new Label("Telefon:");
		telefon_l.setBounds(75, 250, 150, 20);
		telefon_l.setVisible(true);
		this.add(telefon_l);

		// Label Fax
		Label fax_l = new Label("Fax:");
		fax_l.setBounds(75, 275, 150, 20);
		fax_l.setVisible(true);
		this.add(fax_l);

		// Label Bemerkung
		Label bemerkung_l = new Label("Bemerkung:");
		bemerkung_l.setBounds(75, 300, 150, 20);
		bemerkung_l.setVisible(true);
		this.add(bemerkung_l);

		// TextField Name
		final TextField name_t = new TextField();
		name_t.setBounds(225, 100, 120, 20);
		name_t.setVisible(true);
		this.add(name_t);

		// TextField Vorname
		final TextField vorname_t = new TextField();
		vorname_t.setBounds(225, 125, 120, 20);
		vorname_t.setVisible(true);
		this.add(vorname_t);

		// TextField Anrede
		final TextField anrede_t = new TextField();
		anrede_t.setBounds(225, 150, 120, 20);
		anrede_t.setVisible(true);
		this.add(anrede_t);

		// TextField Strasse
		final TextField strasse_t = new TextField();
		strasse_t.setBounds(225, 175, 120, 20);
		strasse_t.setVisible(true);
		this.add(strasse_t);

		// TextField PLZ
		final TextField plz_t = new TextField();
		plz_t.setBounds(225, 200, 120, 20);
		plz_t.setVisible(true);
		this.add(plz_t);

		// TextField Ort
		final TextField ort_t = new TextField();
		ort_t.setBounds(225, 225, 120, 20);
		ort_t.setVisible(true);
		this.add(ort_t);

		// TextField Telefon
		final TextField telefon_t = new TextField();
		telefon_t.setBounds(225, 250, 120, 20);
		telefon_t.setVisible(true);
		this.add(telefon_t);

		// TextField Fax
		final TextField fax_t = new TextField();
		fax_t.setBounds(225, 275, 120, 20);
		fax_t.setVisible(true);
		this.add(fax_t);

		// TextField Bemerkung
		final TextField bemerkung_t = new TextField();
		bemerkung_t.setBounds(225, 300, 120, 20);
		bemerkung_t.setVisible(true);
		this.add(bemerkung_t);

		// Button
		Button knopf = new Button("Person aufnehmen");
		knopf.setBounds(75, 350, 125, 25);
		knopf.setForeground(Color.black);
		knopf.setVisible(true);
		this.add(knopf);

		// Fehler Name
		final Label name_fehler = new Label(
				"Ungültige Eingabe. Bsp.: Mustermann");

		// Fehler Vorname
		final Label vorname_fehler = new Label("Ungültige Eingabe. Bsp.: Max");

		// Fehler Anrede
		final Label anrede_fehler = new Label(
				"Ungültige Eingabe. Bsp.: Herr / Frau");

		// Fehler Strasse
		final Label strasse_fehler = new Label(
				"Ungültige Eingabe. Bsp.: Musterweg 3");

		// Fehler PLZ
		final Label plz_fehler = new Label("Ungültige Eingabe. Bsp.: D-12345");

		// Fehler Ort
		final Label ort_fehler = new Label("Ungültige Eingabe. Bsp.: Berlin");

		// Fehler Telefon
		final Label telefon_fehler = new Label(
				"Ungültige Eingabe. Bsp.:  0123-456789");

		// Fehler Fax
		final Label fax_fehler = new Label(
				"Ungültige Eingabe. Bsp.:  0123-456789");

		// Fehler Bemerkung
		final Label bemerkung_fehler = new Label(
				"Min. 4 / max. 300 Zeichen. ';' nicht erlaubt.");

		knopf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				boolean error = false; // Alle Eingaben korrekt ?

				// Fehler sollen nach Knopfdruck zunächst wieder
				// ausgeblendet werden
				name_fehler.setVisible(false);
				vorname_fehler.setVisible(false);
				anrede_fehler.setVisible(false);
				strasse_fehler.setVisible(false);
				plz_fehler.setVisible(false);
				ort_fehler.setVisible(false);
				telefon_fehler.setVisible(false);
				fax_fehler.setVisible(false);
				bemerkung_fehler.setVisible(false);

				// Überprüfung von Name
				if (!name_t.getText().matches("[A-Za-zäöüÄÖÜß\\-\\s]{3,40}")) {
					name_fehler.setBounds(330, 100, 250, 20);
					name_fehler.setBackground(new Color(255, 190, 190));
					name_fehler.setVisible(true);
					add(name_fehler);
					error = true;
				}

				// Überprüfung von Vorname
				if (!vorname_t.getText().matches("[A-Za-zäöüÄÖÜß\\-\\s]{3,40}")) {
					vorname_fehler.setBounds(330, 125, 250, 20);
					vorname_fehler.setBackground(new Color(255, 190, 190));
					vorname_fehler.setVisible(true);
					add(vorname_fehler);
					error = true;
				}

				// Überprüfung von Anrede
				if (!anrede_t.getText().matches("Herr|Frau")) {
					anrede_fehler.setBounds(330, 150, 250, 20);
					anrede_fehler.setBackground(new Color(255, 190, 190));
					anrede_fehler.setVisible(true);
					add(anrede_fehler);
					error = true;
				}

				// Überprüfung von Straße
				if (!strasse_t.getText().matches(
						"[[a-zA-ZäöüÄÖÜß\\-\\s\\.]+[1-9][0-9]*[a-z]?]{4,40}")) {
					strasse_fehler.setBounds(330, 175, 250, 20);
					strasse_fehler.setBackground(new Color(255, 190, 190));
					strasse_fehler.setVisible(true);
					add(strasse_fehler);
					error = true;
				}

				// Überprüfung von PLZ
				if (!plz_t.getText().matches("D-[0-9]{5}")) {
					plz_fehler.setBounds(330, 200, 250, 20);
					plz_fehler.setBackground(new Color(255, 190, 190));
					plz_fehler.setVisible(true);
					add(plz_fehler);
					error = true;
				}

				// Überprüfung von Ort
				if (!ort_t.getText().matches("[A-Za-zäöüÄÖÜß\\s\\-]{3,40}")) {
					ort_fehler.setBounds(330, 225, 250, 20);
					ort_fehler.setBackground(new Color(255, 190, 190));
					ort_fehler.setVisible(true);
					add(ort_fehler);
					error = true;
				}

				// Überprüfung von Telefon
				if (!telefon_t.getText().matches("[0-9\\.\\-]+")) {
					telefon_fehler.setBounds(330, 250, 250, 20);
					telefon_fehler.setBackground(new Color(255, 190, 190));
					telefon_fehler.setVisible(true);
					add(telefon_fehler);
					error = true;
				}

				// Überprüfung von Fax
				if (!fax_t.getText().matches("[0-9\\.\\-]+")) {
					fax_fehler.setBounds(330, 275, 250, 20);
					fax_fehler.setBackground(new Color(255, 190, 190));
					fax_fehler.setVisible(true);
					add(fax_fehler);
					error = true;
				}

				// Überprüfung von Bemerkung
				if (!bemerkung_t.getText().matches("[^;]{4,300}")) {
					bemerkung_fehler.setBounds(330, 300, 250, 20);
					bemerkung_fehler.setBackground(new Color(255, 190, 190));
					bemerkung_fehler.setVisible(true);
					add(bemerkung_fehler);
					error = true;
				}

				// Überprüfung alle Eingaben korrekt ?
				if (error == false) {

					// Eingaben prüfen
					JaNein_Dialog test = new JaNein_Dialog(null,
							"Eingaben prüfen", "Sind Ihre Eingaben korrekt?");
					if (test.getAntwort() == true) {

						Client_Funktion.person_aufnehmen(name_t.getText(),
								vorname_t.getText(), anrede_t.getText(),
								strasse_t.getText(), plz_t.getText(),
								ort_t.getText(), telefon_t.getText(),
								fax_t.getText(), bemerkung_t.getText());

						// Weitere Person aufnehmen ?
						JaNein_Dialog next = new JaNein_Dialog(null,
								"Noch eine Person",
								"Möchten Sie eine weitere Person aufnehmen?");

						if (next.getAntwort() == true) {
							person_aufnehmen();
						} else {
							menu();
						}
					}
				}
			}
		});
	}

	// =============================================================================
	// ==================== REOCRDS AUFLISTEN ====================
	// =============================================================================

	/**
	 * Auflisten der Personenobjekte, welche in ArrayList des Clients gerade
	 * gespeichert sind. Zunächst wird dem Server mitgeteilt, dass Funktion
	 * aufgerufen wurde. Anschließend Ausgabe aller Labels und TextFields. Beim
	 * Drücken des Buttons wird nächste Person in die TextFelder eingefügt.
	 * Sobald letzte Person erreicht ist, wird der Button gesperrt.
	 */

	public void records_auflisten() {

		checkConnection(); // Ist Server noch aktiv ?

		Client_Funktion.senden(2); // Eingabe an Server weiterleiten

		this.removeAll(); // Fenster leeren
		headline("Records auflisten"); // Überschrift

		zaehler = 0; // zaehler wird auf 0 gesetzt

		if (Client_Funktion.personen.isEmpty()) {
			// ArrayList leer
			Label leer1;
			leer1 = new Label("Zur Zeit sind keine Personendaten vorhanden.");
			leer1.setBounds(75, 100, 350, 20);
			leer1.setVisible(true);
			this.add(leer1);
			Label leer2 = new Label("Bitte neue Records anlegen oder laden.");
			leer2.setBounds(75, 120, 350, 20);
			leer2.setVisible(true);
			this.add(leer2);
		} else {
			// Einträge vorhanden

			// Label Name
			Label name_l = new Label("Name:");
			name_l.setBounds(75, 100, 150, 20);
			name_l.setVisible(true);
			this.add(name_l);

			// Label Vorname
			Label vorname_l = new Label("Vorname:");
			vorname_l.setBounds(75, 125, 150, 20);
			vorname_l.setVisible(true);
			this.add(vorname_l);

			// Label Anrede
			Label anrede_l = new Label("Anrede:");
			anrede_l.setBounds(75, 150, 150, 20);
			anrede_l.setVisible(true);
			this.add(anrede_l);

			// Label Strasse
			Label strasse_l = new Label("Straße:");
			strasse_l.setBounds(75, 175, 150, 20);
			strasse_l.setVisible(true);
			this.add(strasse_l);

			// Label PLZ
			Label plz_l = new Label("PLZ:");
			plz_l.setBounds(75, 200, 150, 20);
			plz_l.setVisible(true);
			this.add(plz_l);

			// Label Ort
			Label ort_l = new Label("Ort:");
			ort_l.setBounds(75, 225, 150, 20);
			ort_l.setVisible(true);
			this.add(ort_l);

			// Label Telefon
			Label telefon_l = new Label("Telefon:");
			telefon_l.setBounds(75, 250, 150, 20);
			telefon_l.setVisible(true);
			this.add(telefon_l);

			// Label Fax
			Label fax_l = new Label("Fax:");
			fax_l.setBounds(75, 275, 150, 20);
			fax_l.setVisible(true);
			this.add(fax_l);

			// Label Bemerkung
			Label bemerkung_l = new Label("Bemerkung:");
			bemerkung_l.setBounds(75, 300, 150, 20);
			bemerkung_l.setVisible(true);
			this.add(bemerkung_l);

			// TextField Name
			final TextField name_t = new TextField();
			name_t.setBounds(225, 100, 175, 20);
			name_t.setEditable(false);
			name_t.setVisible(true);
			this.add(name_t);

			// TextField Vorname
			final TextField vorname_t = new TextField();
			vorname_t.setBounds(225, 125, 175, 20);
			vorname_t.setEditable(false);
			vorname_t.setVisible(true);
			this.add(vorname_t);

			// TextField Anrede
			final TextField anrede_t = new TextField();
			anrede_t.setBounds(225, 150, 175, 20);
			anrede_t.setEditable(false);
			anrede_t.setVisible(true);
			this.add(anrede_t);

			// TextField Strasse
			final TextField strasse_t = new TextField();
			strasse_t.setBounds(225, 175, 175, 20);
			strasse_t.setEditable(false);
			strasse_t.setVisible(true);
			this.add(strasse_t);

			// TextField PLZ
			final TextField plz_t = new TextField();
			plz_t.setBounds(225, 200, 175, 20);
			plz_t.setEditable(false);
			plz_t.setVisible(true);
			this.add(plz_t);

			// TextField Ort
			final TextField ort_t = new TextField();
			ort_t.setBounds(225, 225, 175, 20);
			ort_t.setEditable(false);
			ort_t.setVisible(true);
			this.add(ort_t);

			// TextField Telefon
			final TextField telefon_t = new TextField();
			telefon_t.setBounds(225, 250, 175, 20);
			telefon_t.setEditable(false);
			telefon_t.setVisible(true);
			this.add(telefon_t);

			// TextField Fax
			final TextField fax_t = new TextField();
			fax_t.setBounds(225, 275, 175, 20);
			fax_t.setEditable(false);
			fax_t.setVisible(true);
			this.add(fax_t);

			// TextField Bemerkung
			final TextField bemerkung_t = new TextField();
			bemerkung_t.setBounds(225, 300, 175, 20);
			bemerkung_t.setEditable(false);
			bemerkung_t.setVisible(true);
			this.add(bemerkung_t);

			// Button
			final Button knopf = new Button("Nächste Person anzeigen");
			knopf.setBounds(75, 350, 175, 30);
			knopf.setForeground(Color.black);
			knopf.setVisible(true);
			this.add(knopf);

			// Button bei keinen weiteren Personen sperren
			if ((zaehler + 1) == Client_Funktion.personen.size()) {
				knopf.setEnabled(false);
			}

			// Label Person
			final Label person;
			person = new Label((zaehler + 1) + "/"
					+ Client_Funktion.personen.size());
			person.setBounds(355, 325, 50, 20);
			person.setVisible(true);
			this.add(person);

			// Erste Person bei Aufruf automatisch einfügen
			name_t.setText(Client_Funktion.personen.get(zaehler).name);
			vorname_t.setText(Client_Funktion.personen.get(zaehler).vorname);
			anrede_t.setText(Client_Funktion.personen.get(zaehler).anrede);
			strasse_t.setText(Client_Funktion.personen.get(zaehler).strasse);
			plz_t.setText(Client_Funktion.personen.get(zaehler).plz);
			ort_t.setText(Client_Funktion.personen.get(zaehler).ort);
			telefon_t.setText(Client_Funktion.personen.get(zaehler).telefon);
			fax_t.setText(Client_Funktion.personen.get(zaehler).fax);
			bemerkung_t
					.setText(Client_Funktion.personen.get(zaehler).bemerkung);

			knopf.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					if ((zaehler + 1) < Client_Funktion.personen.size()) {
						addzaehler(); // zaehler um 1 erhöhen
						person.setText((zaehler + 1) + "/"
								+ Client_Funktion.personen.size());
					}

					// TextFields mit Personendaten füllen
					name_t.setText(Client_Funktion.personen.get(zaehler).name);
					vorname_t.setText(Client_Funktion.personen.get(zaehler).vorname);
					anrede_t.setText(Client_Funktion.personen.get(zaehler).anrede);
					strasse_t.setText(Client_Funktion.personen.get(zaehler).strasse);
					plz_t.setText(Client_Funktion.personen.get(zaehler).plz);
					ort_t.setText(Client_Funktion.personen.get(zaehler).ort);
					telefon_t.setText(Client_Funktion.personen.get(zaehler).telefon);
					fax_t.setText(Client_Funktion.personen.get(zaehler).fax);
					bemerkung_t.setText(Client_Funktion.personen.get(zaehler).bemerkung);

					// Button bei keinen weiteren Personen sperren
					if ((zaehler + 1) == Client_Funktion.personen.size())
						knopf.setEnabled(false);
				}
			});
		}
	}

	/**
	 * Methode wird für das Auflisten der Personen benötigt. Bei Aufruf wird die
	 * Variable zaehler um 1 erhöht.
	 */

	public void addzaehler() {

		zaehler++;
	}

	// =============================================================================
	// =================== RECORDS SICHERN ====================
	// =============================================================================

	/**
	 * Personeneinträge, welche sich im Moment in der ArrayList des Clients
	 * befinden, sollen in eine csv Datei auf dem Server gespeichert werden. Bei
	 * Aufruf wird Methode in Client_Funktion gestartet und anschließend im Gui
	 * eine Meldung ausgegeben.
	 * 
	 * @throws Fehler_Exception
	 */

	public void records_sichern() throws Fehler_Exception {

		checkConnection(); // Ist Server noch aktiv ?

		Client_Funktion.senden(3); // Eingabe an Server weiterleiten

		Client_Funktion.records_sichern();

		this.removeAll(); // Fesnter leeren
		headline("Records speichern"); // Überschrift

		if (Client_Funktion.error == false) {
			Label text1;
			text1 = new Label("Personendaten wurden erfolgreich gespeichert.");
			text1.setBounds(75, 100, 350, 20);
			text1.setVisible(true);
			this.add(text1);
		} else {
			throw new Fehler_Exception();
		}
	}

	// =============================================================================
	// =================== RECORDS LADEN ====================
	// =============================================================================

	/**
	 * Personendaten werden aus der csv Datei der Servers gelesen und an den
	 * Client geschickt. Diese werden daraufhin in die ArrayList desjenigen
	 * gespeichert und können verwendet werden.
	 * 
	 * @throws Fehler_Exception
	 */

	public void records_laden() throws Fehler_Exception {

		checkConnection(); // Ist Server noch aktiv ?

		Client_Funktion.senden(4); // Eingabe an Server weiterleiten

		Client_Funktion.records_laden();

		this.removeAll(); // Fesnter leeren
		headline("Records laden"); // Überschrift

		if (Client_Funktion.error == false) {
			Label text1;
			text1 = new Label("Personendaten wurden erfolgreich geladen.");
			text1.setBounds(75, 100, 350, 20);
			text1.setVisible(true);
			this.add(text1);
		} else {
			throw new Fehler_Exception();
		}
	}

	// =============================================================================
	// =================== DATEI LOESCHEN ====================
	// =============================================================================

	/**
	 * CSV Datei auf dem Server soll gelöscht werden. Hierfür wird zunächst ein
	 * JA NEIN Dialog ausgegeben. Wird das löschen der Datei bestätigt, wird die
	 * jeweilige Funktion des Servers aufgerufen.
	 * 
	 * @throws Fehler_Exception
	 */

	public void datei_loeschen() throws Fehler_Exception {

		this.removeAll(); // Fesnter leeren
		headline("Daten löschen"); // Überschrift

		checkConnection(); // Ist Server noch aktiv ?

		JaNein_Dialog dlg = new JaNein_Dialog(null, "Daten löschen",
				"Wollen Sie die Daten wirklich löschen?");

		if (dlg.getAntwort() == true) { // Drücken von JA
			Client_Funktion.senden(6); // Eingabe an Server weiterleiten
			Client_Funktion.datei_loeschen();

			if (Client_Funktion.error == false) {
				Label text1;
				text1 = new Label("Daten wurde erfolgreich von dem "
						+ "Server gelöscht.");
				text1.setBounds(75, 100, 350, 20);
				text1.setVisible(true);
				this.add(text1);
			} else {
				throw new Fehler_Exception();
			}
		} else {
			Label text1;
			text1 = new Label("Daten wurden nicht gelöscht.");
			text1.setBounds(75, 100, 350, 20);
			text1.setVisible(true);
			this.add(text1);
		}
	}

	// =============================================================================
	// =================== CHECKCONNECTION ====================
	// =============================================================================

	/**
	 * Prüft ob der Server noch aktiv ist. Sollte von dem Server den Buchstaben
	 * 's' verschickt worden sein, so ist dieser beendet. Die Methode prüft
	 * zunächst, ob noch Werte zum Lesen vorhanden sind. Falls nein, ist der
	 * Server folglich noch aktiv. Andernfalls wirrd geprüft ob Server gestoppt
	 * / beendet wurde. Anschlißend wird Verbindung getrennt und Client wird in
	 * das Hauptmenü geleitet.
	 */

	public void checkConnection() {

		try {
			// Sind Daten zum Lesen vorhanden ?
			if (Client_Funktion.dataIn.available() > 0) {
				try {
					// String lesen
					char ende = Client_Funktion.dataIn.readChar();
					// Server ist gestoppt / beendet
					if (ende == 's') {
						new OKDialog(null, "Information",
								"Verbindung zum Server ist unterbrochen.");
						Client_Funktion.trennen();
						verbindung = false;
						start();
					}
				} catch (IOException e) {
				}
			}
		} catch (IOException e) {
		}
	}
}