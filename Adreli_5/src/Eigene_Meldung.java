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

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * JA_NEIN_DIALOG erstellt ein Dialogfenster mit Ja / Nein Buttons.
 * 
 * @author Arianit Metaj, Beyza Kalinci, Hakan Celik
 *
 */

@SuppressWarnings("serial")
class JaNein_Dialog extends Dialog implements ActionListener {

	Button btnJa, btnNein; // Buttons
	public boolean antwort = false; // Was wurde gedrückt ?

	public JaNein_Dialog(Frame owner, String titel, String mldg) {

		super(owner, titel, true);
		this.setSize(300, 130); // Fesntergröße
		this.setLocationRelativeTo(null); // Fenster zentrieren
		this.setResizable(false); // feste Fenstergröße
		this.setLayout(null); // Layout deaktivieren
		Font schrift = new Font("Calibri", Font.TRUETYPE_FONT, 11);
		this.setFont(schrift); // Schriftart
		this.setForeground(Color.black); // Schriftfarbe
		Image icon = this.getToolkit().getImage("./icon.png");
		this.setIconImage(icon); // Icon setzen

		Label lblMldg = new Label(mldg); // Meldung
		lblMldg.setSize(260, 25);
		lblMldg.setLocation(20, 40);
		lblMldg.setAlignment(Label.CENTER);
		add(lblMldg);

		btnJa = new Button("Ja"); // JA Button
		btnJa.setSize(70, 25);
		btnJa.setLocation(70, 80);
		this.add(btnJa);
		btnJa.addActionListener(this);

		btnNein = new Button("Nein"); // NEIN Button
		btnNein.setSize(70, 25);
		btnNein.setLocation(160, 80);
		this.add(btnNein);
		btnNein.addActionListener(this);

		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent ae) {

		Object object = ae.getSource(); // geerbt von Class EventObject
		if (object.equals(btnJa))
			antwort = true;
		setVisible(false);
		dispose();
	}

	public boolean getAntwort() {

		return antwort;
	}
}

/**
 * OK_DIALOG erstellt ein Dialogfenster mit OK Button.
 * 
 * @author Arianit Metaj, Beyza Kalinci, Hakan Celik
 *
 */

@SuppressWarnings("serial")
class OKDialog extends Dialog implements ActionListener {

	Button btnOK; // OK Button

	public OKDialog(Frame owner, String titel, String mldg) {

		super(owner, titel, true);
		this.setSize(300, 120); // Fesntergröße
		this.setLocationRelativeTo(null); // Fenster zentrieren
		this.setResizable(false); // feste Größe
		this.setLayout(null); // Layout deaktivieren
		Font schrift = new Font("Calibri", Font.TRUETYPE_FONT, 11);
		this.setFont(schrift); // Schriftart
		this.setForeground(Color.black); // Schriftarbe
		Image icon = this.getToolkit().getImage("./icon.png");
		this.setIconImage(icon); // Icon setzten

		Label lblMldg = new Label(mldg); // Meldung
		lblMldg.setSize(260, 25);
		lblMldg.setLocation(20, 40);
		lblMldg.setAlignment(Label.CENTER);
		add(lblMldg);

		btnOK = new Button("OK"); // Button
		btnOK.setSize(70, 25);
		btnOK.setLocation(115, 80);
		this.add(btnOK);
		btnOK.addActionListener(this);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent ae) {

		ae.getSource(); // geerbt von Class EventObject
		setVisible(false);
		dispose();
	}
}