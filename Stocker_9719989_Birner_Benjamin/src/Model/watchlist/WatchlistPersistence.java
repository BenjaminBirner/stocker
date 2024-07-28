package Model.watchlist;



/**
 * This class represents a watchlist-entry by its symbol and description.
 * An instance of this class is only used in the store and restore process. 
 * 
 * @author Benjamin Birner
 *
 */
public class WatchlistPersistence {

	private final String symbol;
	private final String description;
	
	public WatchlistPersistence(String symbol, String description) {
		this.symbol = symbol;
		this.description = description;
	}
	
	
	
	/**
	 * gets the symbol
	 * 
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}
	
	
	/**
	 * gets the description
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
}
