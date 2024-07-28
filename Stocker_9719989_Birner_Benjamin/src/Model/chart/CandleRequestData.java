package Model.chart;

import com.google.gson.annotations.SerializedName;

import View.chart.Chart;


/**
 * this class is the equivalence to the json string from the candle request.
 * It is only used to convert the json string to an java instance and to build up
 * the required candles.
 * 
 * @author Benjamin Birner
 *
 */
public class CandleRequestData {
	
	@SerializedName("c") private double[] close;
	@SerializedName("h") private double[] high;
	@SerializedName("l") private double[] low;
	@SerializedName("o") private double[] open;
	@SerializedName("s") private String status;
	@SerializedName("t") private long[] time;
	@SerializedName("v") private long[] volume;                  
	
	
	private CandleRequestData(double[] c, double[] h, double[] l, double[] o, String s, long[] t, long[] v) {
		close = c;
		high = h;
		low = l;
		open = o;
		status = s;
		time = t;
		volume = v;
	}
	
	/**
	 * gets the close price in the array <code>close</code> at position <code>index</code>
	 * 
	 * @param index defines the position in the array <code>close</code> with the required close price
	 * @return the close price at position <code>index</code> in the array <code>close</code>
	 */
	public double getCloseAt(int index) {
		return close[index];
	}
	
	
	/**
	 * gets the open price in the array <code>open</code> at position <code>index</code>
	 * 
	 * @param index defines the position in the array <code>open</code> with the required open price
	 * @return the open price at position <code>index</code> in the array <code>open</code>
	 */
	public double getOpenAt(int index) {
		return open[index];
	}
	
	
	/**
	 * gets the high price in the array <code>high</code> at position <code>index</code>
	 * 
	 * @param index defines the position in the array <code>high</code> with the required high price
	 * @return the high price at position <code>index</code> in the array <code>high</code>
	 */
	public double getHighAt(int index) {
		return high[index];
	}
	
	
	/**
	 * gets the low price in the array <code>low</code> at position <code>index</code>
	 * 
	 * @param index defines the position in the array <code>low</code> with the required low price
	 * @return the low price at position <code>index</code> in the array <code>low</code>
	 */
	public double getLowAt(int index) {
		return low[index];
	}
	
	
	/**
	 * gets the timestamp in the array <code>time</code> at position <code>index</code>
	 * 
	 * @param index defines the position in the array <code>time</code> with the required timestamp
	 * @return the timestamp at position <code>index</code> in the array <code>time</code>
	 */
	public long getTimeAt(int index) {
		return time[index];
	}
	
	
	/**
	 * gets the volume in the array <code>volume</code> at position <code>index</code>
	 * 
	 * @param index defines the position in the array <code>volume</code> with the required volume
	 * @return the volume at position <code>index</code> in the array <code>volume</code>
	 */
	public long getVolumeAt(int index) {
		return volume[index];
	}
	
	
	
	/**
	 * gets the status of the request.
	 * 
	 * @return the status of the request.                    
	 */
	public String getState() {
		return status;
	}
	
	
    /**
     * gets the size of the array <code>close</code>
     * 
     * @return the size of the array <code>close</code>
     */
	public int size() {
		return close.length;
	}
	
}
