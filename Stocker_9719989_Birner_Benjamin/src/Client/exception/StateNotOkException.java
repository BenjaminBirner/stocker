package Client.exception;



/**
 * An object of this Exception class is created if the state of the data concerning 
 * a candle-request is a other value than "ok".
 * The corresponding object contains the error-code.
 * 
 * @author Benjamin Birner
 *
 */
public class StateNotOkException extends Exception {

	private String state;
	
	public StateNotOkException(String state){
		this.state = state;
	}
	
	/**
	 * returns the error-code of the candle-request object
	 * 
	 * @return the error-code of the candle-request object
	 */
	public String getMessage() {
		return "Daten sind Fehlerhaft. Status: " + state;
	}

}
