package Model.watchlist;

import com.google.gson.annotations.SerializedName;


/**
 * an instance of this class is the equivalent to the json-String of the push-mechanism.
 * So, it is used to convert from json-format to a java-object.
 * 
 * @author Benjamin Birner
 *
 */
public class RealTimeData {
	
	private RealTimeStock[] data;
	private String type;
	
	public RealTimeData(RealTimeStock[] data, String type) {
		this.data = data;
		this.type = type;
	}
	
	
	/**
	 * gets the symbol that defines the stock to which the data of this object belongs to.
	 * 
	 * @return the symbol that defines the stock to which the data of this object belongs to.
	 */
	public String getSymbol() {   
		return data[0].getSymbol();
	}
	
	
	/**
	 * gets the current price that belongs to this stock.
	 * 
	 * @return the price that belongs to this stock.
	 */
	public double getPrice() {    
		return data[0].getPrice();
	}
	
	
	
	/**
	 * gets the timestamp which belongs to the price if this object.
	 * 
	 * @return the timestamp which belongs to the price if this object.
	 */
	public long getTime() {   
		return data[0].getTime();
	}
	
	
	
	/**
	 * gets the volume that was traded during the last period.
	 * 
	 * @return the volume that was traded during the last period.
	 */
	public long getVolume() {
		return data[0].getVolume();
	}
	
	private static class RealTimeStock{
		@SerializedName("c") private String[] condition;
		@SerializedName("p") private double price;
		@SerializedName("s") private String symbol;
		@SerializedName("t") private long time;
		@SerializedName("v") private long volume;
		
		private RealTimeStock(String[] c, double p, String s, long t, long v) {
			condition = c;
			price = p;
			symbol = s;
			time = t;
			volume = v;
		}
		
		private String getSymbol() {
			return symbol;
		}
		
		private double getPrice() {
			return price;
		}
		
		private long getTime() {
			return time;
		}
		
		private long getVolume() {
			return volume;
		}
	}

	
}


