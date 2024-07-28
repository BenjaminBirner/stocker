package Main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.TimerTask;

import Client.HttpRequest;
import Client.exception.HTTPException;
import Controller.URLQuoteRequest;
import Model.chart.QuoteData;
import Model.watchlist.WatchlistTableModel;


/**
 * This class is responsible for updating the watchlist with  
 * the previous close price when the stock exchange opens.
 * 
 * @author Benjamin Birner
 *
 */
public class TaskClosePriceUpdater extends TimerTask {  

	private WatchlistTableModel watchlistModel;
	private HttpRequest request;
	
	public TaskClosePriceUpdater(WatchlistTableModel watchlistModel, HttpRequest request) {
		this.watchlistModel = watchlistModel;
		this.request = request;
	}
	
	@Override
	public void run() {
		//gets all symbols of the entries in the watchlist
		String[] symbols = watchlistModel.getAllSymbols();
		//this loop fetches the previous close price for each stock by carrying out a quote-request
		for(String s : symbols) {
			URLQuoteRequest urlQR = new URLQuoteRequest(s);
			try {
				QuoteData quote = request.quoteRequest(urlQR.getUrlString());
				//updates the entries
				watchlistModel.updatePreviousClosePrice(s, quote.getPreviousClose()); 
			} catch (MalformedURLException e) {
				System.err.println("URL ist falsch aufgebaut!");
				e.printStackTrace();
				break;
			} catch (IOException e) {
				System.err.println("URL konnte nicht geöffnet werden!");
				e.printStackTrace();
				break;
			} catch (HTTPException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				break;
			}
			  
		}
	}
}
