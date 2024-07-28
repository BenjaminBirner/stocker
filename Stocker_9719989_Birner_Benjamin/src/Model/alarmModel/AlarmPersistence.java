package Model.alarmModel;


/**
 * this class concentrates all the relevant informations in order to store and restore a single alarm.
 * In the program`s closing process, it is necessary to build up an instance of this class for every
 * single alarm to store the state.
 * In the program`s opening process these instances are rebuild to restore the last state.
 * 
 * @author Benjamin Birner
 *
 */
public class AlarmPersistence {
	
	private final String symbol;
	private final double price;
	private final boolean typ;
	
	public AlarmPersistence( String symbol, double price, boolean typ) {
		this.symbol = symbol;
		this.price = price;
		this.typ = typ;
	}
	
	/**
	 * gets the symbol.
	 * 
	 * @return the symbol to that the alarm belongs to.
	 */
	public String getSymbol() {
		return symbol;
	}
	
	
	/**
	 * gets the price.
	 * 
	 * @return the value to which the alarm should be triggered.
	 */
	public double getPrice() {
		return price;
	}
	
	
	/**
	 * gets the type.
	 * 
	 * @return true if the price was higher as the value of the alarm when the alarm was set. Otherwise false.
	 */
	public boolean getTyp() {
		return typ;
	}

}
