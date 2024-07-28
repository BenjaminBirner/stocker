package Model.chart;


/**
 * This class is only required to unsubscribe a stock from the provider.
 * To realize this, an Instance of this class must be converted to an equivalent json-object. 
 * 
 * @author Benjamin Birner
 *
 */
public class ToJsonStock {
	
	private String type;
	private String symbol;
	
	public ToJsonStock(String symbol, String type) {
		this.symbol = symbol;
		this.type = type;
	}

}
