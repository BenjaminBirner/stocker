package Client.exception;




/**
 * An object of this Exception class is created if after a candle-request
 * the number of candles is smaller than the delivered number of candles.
 * The instance of this class contains the delivered number of candles
 * 
 * @author Benjamin Birner
 *
 */
public class NotEnoughCandlesException extends Exception {

	private int number;
	
	public NotEnoughCandlesException(int number){
		this.number = number;
	}
	
	
	
	/**
	 * returns the delivered number of candles.
	 * 
	 * @return the delivered number of candles.
	 */
	public String getMessage() {
		return "Zu wenig Kerzen! Anzahl: " + number;
	}
}
