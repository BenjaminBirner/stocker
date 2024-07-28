package Model.chart;


/**
 * This interface is part of the "observer pattern".
 * It is implemented by the <code>Chart</code>-class  so that the charts will be
 * informed if a new price or candle is available.
 * The listeners are added to the list in the {@link #StockCandleData}-class.
 * 
 * @author Benjamin Birner
 *
 */
public interface StockListener {
	
	
	/**
	 * this method is called up by an instance of the {@link #StockCandleData}-class if a new price is available.
	 * 
	 * @param price the latest price for the stock.
	 */
	public void newPriceAvailable(double price);
	
	
	
	/**
	 * this method is called up by an instance of the {@link #StockCandleData}-class if a new candle is available.
	 * 
	 */
	public void newCandleAvailable();
}
