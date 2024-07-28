package Model.searchModel;


/**
 * This class manages the data concerning the search-results.
 * It contains the data to display the results in the view and to open the
 * correct chart or to add the correct stock to the watchlist.
 * 
 * @author Benjamin Birner
 *
 */
public class SearchResultModel {
	
	//the descriptions/symbols of the current search-results
	private String[] descriptions; 
	private String[] symbols;      
	
	
	
	/**
	 * sets the descriptions that are displayed to the user.
	 * So, this method is called up by the {@link #Client.HttpRequest}-class after the search-request.
	 * If the search was unsuccessful, the method creates a String-array with the following String on
	 * position 0: "Suche erfolglos".
	 * 
	 * @param results the object that contains the search-result data. 
	 */
	public void setDescriptions(SearchResultData results) {
		if(results.getCount() == 0) {
			descriptions = new String[1];
			descriptions[0] = "Suche erfolglos" ;
		}else{
		    descriptions = results.getAllDescriptions();
		}    
	}
	
	
	
	/**
	 * this method is called up by the {@link #Client.HttpRequest}-class 
	 * after the search-request to set the symbols.
	 * 
	 * @param results the object that contains the search-result data.
	 */
	public void setSymbols(SearchResultData results) {
		symbols = results.getAllSymbols();
	}
	
	
	
	
	/**
	 * gets the descriptions that are displayed to the user.
	 * This method is called up by the view to get the descriptions.
	 * 
	 * @return <code>String</code>-array that contains the descriptions that are displayed to
	 *         the user.
	 */
	public String[] getDescriptions() {
		if (descriptions == null) {                           
			descriptions = new String[1];
			descriptions[0] = "bitte Suchbegriff eingeben" ;
		}
		return descriptions;
	}
	
	
	
	/**
	 * gets the symbols from the <code>symbols</code>-array.
	 * This method is called up if the user selects some stocks of the search results and wants
	 * to add them to the watchlist or open them in the chart.
	 * 
	 * @param indices defines the positions in the <code>symbols</code>-array to get the correct symbols.
	 * @return <code>String</code>-array that contains the symbols. <code>null</code> if there are no symbols available.  
	 */
	public String[] getSymbolsAt(int[] indices) {
		String[] symbols = new String[indices.length];
		for(int i = 0; i < indices.length; i++) {
			if(this.symbols != null && this.symbols.length > 0 ) {
				symbols[i] = this.symbols[indices[i]];
			}else {
				return null;
			}
		}
		return symbols;
	}
	
	
	
	/**
	 * gets the symbol from the <code>symbols</code>-array.
	 * This method is called up if the user selects a stock of the search results with a mouse click.
	 * 
	 * @param index defines the position in the <code>symbols</code>-array to get the correct symbol.
	 * @return <code>String</code> that represents the symbol. <code>null</code> if there are no symbols available.  
	 */
	public String getSymbolAt(int index) {
		if(this.symbols != null && this.symbols.length > 0 ) {
			return symbols[index];
		}else {
			return null;
		}	
	}
	
	
	/**
	 * gets the description at position <code>index</code> in the <code>descriptions</code>-array
	 * 
	 * @param index defines the position in the <code>descriptions</code>-array.
	 * @return this description
	 */
	public String getDescriptionAt(int index) {
		return descriptions[index];
	}
}
