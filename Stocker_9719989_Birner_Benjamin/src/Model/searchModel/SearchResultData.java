package Model.searchModel;

/**
 * an instance of this class is the equivalent to the json-String of the search-request.
 * So, it is used to convert from json-format to a java-object.
 * 
 * @author Benjamin Birner
 *
 */
public class SearchResultData {

	private long count;
	private Stocks[] result;  
	
	private SearchResultData( long count, Stocks[] results) {
		this.count = count;
		this.result = results;
		
	}
	
	
	/**
	 *gets all the descriptions of the search-results that the <code>result</code>-array contains. 
	 * 
	 * @return <code>String</code>-array with the descriptions of the search-results that the <code>result</code>-array contains. 
	 */
	protected String[] getAllDescriptions() {
		String[] descriptions = new String[result.length];
		for(int i = 0; i < result.length; i++) {                    
			descriptions[i] = result[i].getDescription();               
		}
		return descriptions;
	}
	
	
	

	/**
	 *gets all the symbols of the search-results that the <code>result</code>-array contains. 
	 * 
	 * @return <code>String</code>-array with the symbols of the search-results that the <code>result</code>-array contains. 
	 */
	protected String[] getAllSymbols() {
		String[] symbols = new String[result.length];
		for(int i = 0; i < result.length; i++) {                   
			symbols[i] = result[i].getSymbol();          
		}
		return symbols;
	}
	
	
	
	/**
	 * gets the value of the attribute <code>count</code> that defines the number of stocks
	 * regarding the search-results.
	 * 
	 * @return
	 */
	protected long getCount() {
		return count;
	}
	
	private class Stocks {
		
		private String description;
		private String displaySymbol;
		private String symbol;
		private String type;
		
		private Stocks (String description, String displaySymbol, String symbol, String type) {
			this.description = description;
			this.displaySymbol = displaySymbol;
			this.symbol = symbol;
			this.type = type;
			
		}
		
		private String getDescription() {
			return description;
		}
		
		private String getSymbol() {
			return symbol;
		}
	}
}	
	

