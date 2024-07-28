package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Client.exception.HTTPException;
import Client.exception.StateNotOkException;
import Model.chart.CandleRequestData;
import Model.chart.QuoteData;
import Model.searchModel.SearchResultData;
import Model.searchModel.SearchResultModel;


/**
 * This class provides all methods regarding the HTTP-requests.
 * So, it is responsible for the search-request, the quote-request and the candle-request.
 * 
 * @author Benjamin Birner
 *
 */
public class HttpRequest {
	
	private SearchResultModel searchModel;
	
	public HttpRequest(SearchResultModel searchModel) {
		this.searchModel = searchModel;
	}
	
	
	
	/**
	 * This method carries out the search-request.
	 * On the basis of the parameter<code>url</code> which defines the url-string for the search-request,
	 * it carries out the request.
	 * It also uses the result to set the descriptions and symbols of the result-stocks in the {@link #SearchResultModel} 
	 * by calling up its <code>setDescriptions</code>-method and  <code>setSymblos</code>-method.
	 * 
	 * @param url defines the url-string for the request.
	 * @throws IOException  if there is something wrong regarding the IO   
	 * @throws MalformedURLException  if the URL-String wrong build up                                    
	 * @throws HTTPException if a HTTP-Code >= 400 was sent
	 */
	public void searchRequest(String url) throws IOException, MalformedURLException, HTTPException {
		
		String json = request(url);
		Gson gson = new GsonBuilder().create();
		SearchResultData searchResults = gson.fromJson(json, SearchResultData.class);
		
		searchModel.setDescriptions(searchResults);
		searchModel.setSymbols(searchResults);
	}
	
	
	/**
	 * This method carries out the quote-request.
	 * On the basis of the parameter<code>url</code> which defines the url-string for the quote-request,
	 * it carries out the request.
	 * It is called up if the last open or the last close price is required e.g. if a new stock is added to
	 * the watchlist or a chart is opend.
	 * 
	 * @param url defines the url-string for the request.
	 * @throws IOException  if there is something wrong regarding the IO   
	 * @throws MalformedURLException  if the URL-String wrong build up                                    
	 * @throws HTTPException if a HTTP-Code >= 400 was sent
	 */
	public QuoteData quoteRequest(String url)throws  IOException, MalformedURLException, HTTPException {
		String json = request(url);
		Gson gson = new GsonBuilder().create();
		QuoteData qData = gson.fromJson(json, QuoteData.class);
		return qData;
	}
	
	
	
	/**
	 * This method carries out the candle-request.
	 * On the basis of the parameter<code>url</code> which defines the url-string for the candle-request,
	 * it carries out the request.
	 * It is called up by the controller if candles are required to calculate an indicator or to depict 
	 * the candles in a chart.
	 * 
	 * @param url defines the url-string for the request.
	 * @throws IOException  if there is something wrong regarding the IO   
	 * @throws MalformedURLException  if the URL-String wrong build up                                    
	 * @throws HTTPException if a HTTP-Code >= 400 was sent
	 */
	public CandleRequestData candleRequest(String url) throws IOException, MalformedURLException, HTTPException, StateNotOkException {
		String json = request(url);
		Gson gson = new GsonBuilder().create();
		CandleRequestData data = gson.fromJson(json, CandleRequestData.class); 
		if (!data.getState().equals("ok")) {
			throw new StateNotOkException(data.getState());
		}
		return data;

	}
	
	/*
	 * This method is only called up by all the methods above in order to build up the connection
	 * and to do the request
	 */
	private String request(String url) throws  MalformedURLException, HTTPException, IOException {
		
		String json = "";
		//builds up the connection
		    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			int code = conn.getResponseCode();
			//checks the HTTP-Code
			if(code >= 400) {
				throw new HTTPException(code);  
			}
			//builds up the InputStream and reads the data
			try(BufferedReader in = new BufferedReader(
					new InputStreamReader(
					conn.getInputStream()))){
				json = in.readLine(); 
			}
		return json;
	}	
}


