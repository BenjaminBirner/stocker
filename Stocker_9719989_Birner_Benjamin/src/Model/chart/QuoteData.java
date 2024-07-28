package Model.chart;

import com.google.gson.annotations.SerializedName;


/**
 * an instance of this class is the equivalent to the json-String of the quote-request.
 * It contains various prices that belongs to a stock which is defined by the quote-request.
 * It is also used to convert from json-format to a java-object.
 * 
 * @author Benjamin Birner
 *
 */
public class QuoteData { 
	
	@SerializedName("c") private final double lastClose;
	@SerializedName("h") private final double lastHigh;
	@SerializedName("l") private final double lastLow;
	@SerializedName("o") private final double lastOpen;
	@SerializedName("pc") private final double previousClose;
	@SerializedName("t") private final long time;
	
	private QuoteData(double c, double h, double l, double o, double pc, long t) {
		lastClose = c;
		lastHigh = h;
		lastLow = l;
		lastOpen = o;
		previousClose = pc;
		time = t;
	}
	
	
	/**
	 * gets the last close price
	 * 
	 * @return the last close price
	 */
	public double getLastClose() {
		return lastClose;
	}
	
	
	
	/**
	 * gets the last high price
	 * 
	 * @return the last high price
	 */
	public double getLastHigh() {
		return lastHigh;
	}
	
	
	
	
	/**
	 * gets the last low price
	 * 
	 * @return the last low price
	 */
	public double getLastLow() {
		return lastLow;
	}
	
	
	
	
	/**
	 * gets the last open price
	 * 
	 * @return the last open price
	 */
	public double getLastOpen() {
		return lastOpen;
	}
	
	
	
	
	
	/**
	 * gets the last previous close price. Thats the close price of the day before.
	 * 
	 * @return the last previous close  price
	 */
	public double getPreviousClose() {
		return previousClose;
	}
	
	

}
