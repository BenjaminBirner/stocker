package Controller;

import Model.persistence.PropertiesCache;


/**
 * This class constructs the appropriate URL-String for the quote-request.
 * So, if a quote-request must be carried out, it is just necessary to build an
 * object of this class and call its method "getUrlString".
 * 
 * @author Benjamin Birner
 *
 */
public class URLQuoteRequest {

	private final String query = "/quote?symbol=";
	private final String symbol;
	private final String tokenQ = "&token=";
	private final String basisURL;
	private final String apiKey;  
	
	public URLQuoteRequest(String symbol) {
		
		this.symbol = symbol;
		PropertiesCache prop = PropertiesCache.getInstance();
		String apiKey = prop.getProperty("api.key");
		this.apiKey = apiKey;
		String pullUrl = prop.getProperty("pull.url");
		basisURL = (pullUrl == null ? URLCandleRequest.DEFAULT_BASIS_URL : pullUrl);
	}
	
	/**
	 *constructs the URL-String by getting all parts
	 *
	 *@return url URL-String
	 */
	public String getUrlString() {
		String url = basisURL+ query + symbol + tokenQ + apiKey;
		return url;
	}
	
}
