package Client;


import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Model.alarmModel.AlarmDataModel;
import Model.chart.ChartDataModel;

import Model.watchlist.RealTimeData;
import Model.chart.ToJsonStock;
import Model.watchlist.WatchlistTableModel;


/**
 * This class is asuclass of the {@link #WebSocketClient}-class and is responsible
 * for all aspects of the push-mechanism. It connects to the defined data-provider, 
 * subscribes and unsubscribes specified stocks and receives the data.
 * It also updates all the watchlist and the charts.
 * 
 * @author Benjamin Birner
 *
 */
public class StocksRealTimeClient extends WebSocketClient {
	
	private WatchlistTableModel tablemodel;
	private ChartDataModel chartDataModel;
	private AlarmDataModel alarmModel;
	private boolean isConnected = false;                    
	
	public StocksRealTimeClient(String serverUri, WatchlistTableModel tablemodel,
			ChartDataModel chartDataModel, AlarmDataModel alarmModel) {    
		super(URI.create(serverUri));
		this.tablemodel = tablemodel;
		this.chartDataModel = chartDataModel;
		this.alarmModel = alarmModel;
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		isConnected = true;                                

	}

	@Override
	public void onMessage(String json) {
		Gson gson = new GsonBuilder().create();
		RealTimeData stock  = gson.fromJson(json, RealTimeData.class);
		String symbol = stock.getSymbol();
		double price = stock.getPrice();
		long time = stock.getTime();
		long vol = stock.getVolume();
        tablemodel.updateTablelist(symbol,price,time );
        chartDataModel.updateStock(symbol,price,time,vol);
        alarmModel.updateLastPrice(symbol,price);      
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("Die Verbindung wurde beendet vom "
				+ (remote? "Server" : "Client")
				+ "/ Code: " + code
				+ "/ Ursache: " +reason);
		isConnected = false;

	}

	@Override
	public void onError(Exception ex) {
		if(ex != null) {
			System.err.println("Ein Fehler ist aufgetreten:"
					+ ex.getMessage());
			ex.printStackTrace();
		}else {
			System.out.println("Unbekannter Fehler!");
		}
	}
	
	public boolean isConnected() {
		return isConnected;
	}
	
	
	
	/**
	 * This method connects to the data-provider.
	 * It must be called up after creating an instance of this class
	 * to connect to this provider.
	 */
	public void start() {
		connect(); 
		int i = 0;
		while(!isConnected && i < 20) {
			i = i+1;
			try {
	
				Thread.sleep(100l);
			}catch(InterruptedException iex) {
				
			}
		}
	}
	
	
	/**
	 * subscribes the stock that is defined by the parameter <code>symbol</code>.
	 * After subscribing the stock the push-messages are received by the 
	 * {@link #onMessage(String)}-method.
	 * 
	 * @param stock the ticker-symbol of the stock that should be subscribed
	 * @throws WebsocketNotConnectedException if this method is called up but there is no connection
	 */
	public void subscribeStock(String stock) throws  WebsocketNotConnectedException{
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(new ToJsonStock(stock, "subscribe"));
		send(json);
	}
	
	

	/**
	 * unsubscribes the stock that is defined by the parameter <code>symbol</code>.
	 * After unsubscribing the stock no more information will be send for this stock.
	 * 
	 * @param stock the ticker-symbol of the stock that should be unsubscribed
	 * @throws WebsocketNotConnectedException if this method is called up but there is no connection
	 */
	public void unsubscribeStock(String stock) throws  WebsocketNotConnectedException {
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(new ToJsonStock(stock, "unsubscribe"));
		send(json);
	}

}
