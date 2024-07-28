package Model.chart;

import java.util.concurrent.TimeUnit;


/**
 * This class models a candle with all its data.
 * Instances of this class are used in the {@link #StockCandleData} class.
 * 
 * @author Benjamin Birner
 *
 */
public class Candle {
	
	private final String symbol;
	private final String resolution;
	private double close;
	private double high;
	private double low;
	private double open;
	// in seconds
	private long time;
	private long volume;
	
	protected Candle(String symbol, String resolution, double close,
			double high, double low, double open, long time, long volume) {
		this.symbol = symbol;
		this.resolution = resolution;
		this.close = close;
		this.high = high;
		this.low = low;
		this.open = open;
		this.time = time;
		this.volume = volume;
	}
	
	/**
	 * gets the close price of this candle.
	 * 
	 * @return the last price which was available for this candle.
	 */
	public double getClose() {
		return close;
	}
	
	/**
	 * gets the highest price of this candles.
	 * 
	 * @return the highest price that was available in the period of this candle.
	 */
	protected double getHigh() {
		return high;
	}
	
	/**
	 * gets the lowest price of this candles.
	 * 
	 * @return the lowest price that was available in the period of this candle.
	 */
	protected double getLow() {
		return low;
	}
	
	/**
	 * gets the open price of this candle.
	 * 
	 * @return the first price which was available for this candle.
	 */
	protected double getOpen() {
		return open;
	}
	
	
	/**
	 * gets the timestamp of this candle.
	 * 
	 * @return the timestamp  in seconds which defines the starting point.
	 */
	protected long getTimeInSec() {
		return time;
	}
	
	/**
	 * gets the timestamp of this candle.
	 * 
	 * @return the timestamp in milliseconds which defines the starting point.
	 */
	protected long getTimeInMilli(){
		return TimeUnit.SECONDS.toMillis(time);
	}
	
	
	/**
	 * gets the volume of this candle.
	 * 
	 * @return the number of stocks that was traded whereas the period of this candle
	 */
	protected long getVolume() {
		return volume;
	}
	
	/**
	 * sets the close price for this candle.
	 * this method is called up if a new candle is generated
	 * 
	 * @param close the last price which was available for this candle.
	 */
	protected void setClose(double close) {
		this.close = close;
	}
	
	/**
	 * sets the highest price for this candle.
	 * this method is called up if a new candle is generated
	 * 
	 * @param high the highest price that was available in the period of this candle.
	 */
	protected void setHigh(double high) {
		this.high = high;
	}

	/**
	 * sets the lowest price for this candle.
	 * this method is called up if a new candle is generated
	 * 
	 * @param low the lowest price that was available in the period of this candle.
	 */
	protected void setLow(double low) {
		this.low = low;
	}
	

	/**
	 * sets the open price for this candle.
	 * this method is called up if a new candle is generated
	 * 
	 * @param open the first price which was available for this candle.
	 */
	protected void setOpen(double open) {
		this.open = open;
	}
	
	/**
	 * sets the timestamp of this candle
	 * 
	 * @param time the timestamp in seconds which defines  the starting point
	 */
	protected void setTime(long time) {
		this.time = time;
	}
	
	
	/**
	 * sets the volume 
	 * 
	 * @param volume the number of stocks that was traded whereas the period of this candle
	 */
	protected void setVolume(long volume) {
		this.volume = volume;
	}
}
