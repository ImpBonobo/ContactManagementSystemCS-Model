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

/**
 * Eigenes Exception Handling. Meldung ein Fehler ist aufgetreten.
 * 
 * @author Arianit Metaj, Beyza Kalinci, Hakan Celik
 *
 */

@SuppressWarnings("serial")
class Fehler_Exception extends Exception {

	Fehler_Exception() {

		new OKDialog(null, "Fehler", "Ein Fehler ist aufgetreten.");
	}
}