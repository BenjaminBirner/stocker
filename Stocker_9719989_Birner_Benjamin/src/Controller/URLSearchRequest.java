package Controller;

import Model.persistence.PropertiesCache;


/**
 * This class constructs the appropriate URL-String for the search-request.
 * So, if a search-request must be carried out, it is just necessary to build an
 * object of this class and call its method "getUrlString".
 * 
 * @author Benjamin Birner
 *
 */
class URLSearchRequest {

	private final String query = "/search?q=";
	private final String entry;
	private final String tokenQ = "&token=";
	private final String basisURL;
	private final String apiKey;  
	
	protected URLSearchRequest(String entry) {
		
		this.entry = entry;
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
	protected String getUrlString() {
		String url = basisURL+ query + entry + tokenQ + apiKey;
		return url;
	}
	
}
