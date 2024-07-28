package Model.watchlist;

import Model.chart.StockerCalculator;

/**
 * this class represents a watchlist-entry with all its data that are required to display it in the watchlist.
 * Moreover, if a new price is available for this stock, the <code>this</code>-instance is updated and calculates
 * the alteration of the day in percent.
 * 
 * @author Benjamin Birner
 *
 */
public class WatchlistStock implements Comparable<String> {

	private final String description;
	private final String symbol;
	private double price;
	//that´s the price before the price. It is required in the ColoredTableCellRenderer.
	private double previousPrice = 0;
	//this flag is required in the ColoredTableCellRenderer to decide if a color or white is required
	private boolean colorFlag = false;
	private double percent;
	private double previousClosePrice;  
	private long time;
	
	public WatchlistStock(String description, String symbol, double price,
			double percent, double previousClosePrice) {
		this.description = description;
		this.symbol = symbol;
		price = StockerCalculator.roundPrice(price);
		this.price = price;
		percent =  Math.rint(percent * 100.0) / 100.0;
		this.percent = percent;
		this.previousClosePrice = previousClosePrice;
		this.time = 0;               
	}
	
	
	/**
	 * gets the description that describes the stock.
	 * 
	 * @return the description
	 */
	protected String getDescription() {
		return description;
	}
	
	
	
	/**
	 * gets the symbol (id of the stock)
	 * 
	 * @return the symbol
	 */
	protected String getSymbol() {
		return symbol;
	}
	
	
	/**
	 * gets the current price for this stock.
	 * 
	 * @return the price
	 */
	protected double getPrice() {
		return price;
	}
	
	
	
	/**
	 * gets the previous price. Thats the price before the price and is only required 
	 * in the {@link #ColoredTableCellRenderer}-class.
	 * 
	 * @return the previous price of this stock.
	 */
	protected double getPreviousPrice() {
		return previousPrice;
	}
	
	
	
	/**
	 * gets the alteration of the day in percent.
	 * 
	 * @return the alteration of the day in percent.
	 */
	protected  double getPercent() {
		return percent;
	}
	
	
	/**
	 * gets the time that belongs to the latest price
	 * 
	 * @return the time that belongs to the latest price
	 */
	protected  long getTime() {
		return time;
	}
	
	
	/**
	 * gets the color-flag. This flag is required in the 
	 * ColoredTableCellRenderer to decide if a color or white is required.
	 * It also insures that the renderer works correct
	 * 
	 * @return true if color is required else false
	 */
	protected  boolean getColorFlag() {
		return colorFlag;
	}
	
	
	
	
	/**
	 * sets the price that belongs to this stock
	 * 
	 * @param price the price that should be set
	 */
	protected  void setPrice(double price) {
		price = StockerCalculator.roundPrice(price);
		this.price = price;
	}
	
	
	/**
	 * sets the previous price. Thats the price before the price. 
	 * It is only required in the {@link #ColoredTableCellRenderer}-class.
	 * 
	 * @param price the price that should be set
	 */
	protected  void setPreviousPrice(double price) {
		previousPrice = price;
	}
	
	
	
	
	/**
	 * sets the alteration of the day in percent
	 * 
	 * @param price the latest price to calculate the alteration of the day
	 */
	protected  void setPercent(double price) {
		percent =  ((price - previousClosePrice) * 100) / previousClosePrice;
		percent =  Math.rint(percent * 100.0) / 100.0;
	}
	
	
	
	/**
	 * sets the previous close price. Thats the close price of the day before.
	 * 
	 * @param price close price of the day before.
	 */
	protected  void setPreviousClosePrice(double price) {
		previousClosePrice = price;
	}
	
	
	
	/**
	 * sets the time that belongs to the latest price.
	 * 
	 * @param time the time that belongs to the latest price.
	 */
	protected  void setTime(long time) {
		this.time = time;
	}
	
	
	/**
	 * sets the color-flag. This flag is required in the 
	 * ColoredTableCellRenderer to decide if a color or white is required.
	 * It also insures that the renderer works correct
	 * 
	 * @param the value of the flag
	 */
	protected  void setColorFlag(boolean flag) {
		colorFlag = flag;
	}
	
	
	
	@Override
	public int compareTo(String s) {        
		
		return getSymbol().compareTo(s);
	}

} 
