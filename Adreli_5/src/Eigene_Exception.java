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

/**
 * Eigenes Eception Handling. Meldung ein Fehler ist aufgetreten.
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