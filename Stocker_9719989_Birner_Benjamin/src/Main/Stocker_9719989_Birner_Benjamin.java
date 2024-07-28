package Main;



import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;




import Client.HttpRequest;
import Client.StocksRealTimeClient;

import Controller.ChartController;
import Controller.FrameManager;
import Controller.IndicatorController;
import Controller.StockerController;
import Controller.URLCandleRequest;
import Model.alarmModel.AlarmDataModel;
import Model.alarmModel.AlarmPersistence;
import Model.chart.CandleRequestData;
import Model.chart.ChartDataModel;
import Model.chart.ChartPersistence;
import Model.watchlist.FramePersistence;
import Model.indicatorModel.IndicatorModel;
import Model.indicatorModel.IndicatorParameterPersistence;
import Model.indicatorModel.PersistenceIndiParaData;
import Model.persistence.PropertiesCache;
import Model.chart.QuoteData;
import Model.searchModel.SearchResultModel;
import Model.persistence.StockerPersistenceData;
import Model.persistence.StockerPersistenceToPropertiesFile;
import Model.watchlist.WatchlistPersistence;
import Model.watchlist.WatchlistTableModel;
import View.chart.Chart;
import View.dialogsAndFrames.StockerMainFrame;



/**
 * This class is the starting-class with the main()-method.
 * The main()-method is the first method that is called up when the program starts.
 * So it is responsible for creating, initializing and connecting all required instances so that the program runs correctly.
 * 
 * @author Benjamin Birner
 *
 */
public class Stocker_9719989_Birner_Benjamin {

	


	/**
	 * this method is the first method that is called up when the program starts.
     * So it is responsible for creating, initializing and connecting all required instances so that the program runs correctly.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		//creates an instance of each XXXModel-class
		WatchlistTableModel watchlistModel = new WatchlistTableModel();
		ChartDataModel chartDataModel = new ChartDataModel();
		IndicatorModel indiModel = new IndicatorModel();
		SearchResultModel srModel = new SearchResultModel();
		AlarmDataModel alarmModel = new AlarmDataModel();
		
		//creates a HTTPRequest instance
		HttpRequest http = new HttpRequest(srModel);
		
	
		StockerPersistenceToPropertiesFile stockerPersistence = new StockerPersistenceToPropertiesFile();
		//loads all the data from the Properties-file which are required to restore the last state.
		//The returned StockerPersistenceData-Object contains all these data
		StockerPersistenceData  perData = stockerPersistence.loadStockerStatusFromProperties(indiModel);
		PropertiesCache prop = PropertiesCache.getInstance();
		
	    //gets the data from the StockerPersistenceData-Object for each single component
		LinkedList<WatchlistPersistence> wlPerList =  perData.getWLPersistenceList();
		LinkedList<AlarmPersistence> alPerList = perData.getAlarmList();
		LinkedList<ChartPersistence> cPerList = perData.getChartList();
		FramePersistence mainF = perData.getMainFramePersis();
		FramePersistence wlFPer = perData.getWlFramePersis();
		
		//creates a StockerMainFrame-Object either with the loaded data or with the default-data
		StockerMainFrame frame = null;
		if (mainF != null) {
		    frame = new StockerMainFrame(mainF);
		}else {
			frame = new StockerMainFrame();
		}
		
		

		/*
		 * This block greatest and initializes the StocksRealTimeClient-object. 
		 * In order to realize this it gets the push-URL from the Properties.
		 * If there is no push-URL default is used.
		 * Finally, it connects to the server
		 */
		
		StocksRealTimeClient client = null;
		String pushURI = prop.getProperty("push.url");
		if(pushURI == null) {
			client = new StocksRealTimeClient(URLCandleRequest.DEFAULT_WS_URL ,watchlistModel,chartDataModel,alarmModel);
		}else {
			client = new StocksRealTimeClient(pushURI,watchlistModel,chartDataModel,alarmModel);
		}
		client.start();
		
		
		//creates a FrameManager-Object
		FrameManager frameManager = new FrameManager(frame);
		//creates and initializes all Controller-Instances
		IndicatorController indiController = new IndicatorController(indiModel,http);
		ChartController chartController = new ChartController(frame,watchlistModel,chartDataModel,indiModel,alarmModel,indiController,frameManager,http,client);
		StockerController stockController = new StockerController(frame,chartDataModel,
				srModel,watchlistModel,alarmModel,http, client,chartController,indiController,indiModel,frameManager);
	
		
		//checks if the loading-process was successful. False means that there are data to restore the last state.
		//if true, only an empty watchlist is created
		if(!prop.getLoadStatus()) {
			stockController.initializeWatchlist(null);
		}else {
			//checks if the webSocket-client is connected. If no, nothing will be restored because it is useless to restore the program with no data.
			//But the Stocker stays opened so that the connection-configurations can be adapted.
			//if true, the last state is restored.
			if(client.isConnected()) {
				//restores the watchlistModel
				Iterator<WatchlistPersistence> wlIt = wlPerList.iterator();
				for(int i = 0; i < wlPerList.size(); i++) {
					WatchlistPersistence wlPerObj = wlIt.next();
					stockController.addNewEntryToWL(wlPerObj.getSymbol(), wlPerObj.getDescription());
				}
				
				/*
				 * In this block the ChartPersistence-objects are copied in an Array and sorted according to its ZOrder.
				 * This is necessary to restore the ZOrder according to the last state.
				 */
				
		        ChartPersistence[] cPerArr = new ChartPersistence[cPerList.size()+1];
		        Iterator<ChartPersistence> itC = cPerList.iterator();
		        for(int i = 0; i < cPerList.size(); i++) {
		        	ChartPersistence cPer = itC.next();
		        	cPerArr[cPer.getZOrder()] = cPer;
		        }
		        
		        //in this loop all charts are restored completely
		    	for(int i = cPerArr.length-1; i >= 0; i--) {
		    		//this check is necessary because there always is one position with null. 
		    		//Thats the ZOrder position for the watchlist.
					if(cPerArr[i] != null) {
						ChartPersistence cPerObj = cPerArr[i];
						//creates and initializes a Chart-Object
						Chart chart = stockController.initializeNewChart(cPerObj.getSymbol(),cPerObj.getResolution(), cPerObj.getType());
						//null means, that there was a problem regarding the candle-request
						if( chart != null) {
							
							
							/*
							 * the check in the following block is in the restore-process of importance.
							 * It avoids a false behavior regarding one special case
							 */
							
							int height;
							int width;
							String minH = prop.getProperty("height");
							String minW = prop.getProperty("width");
							if(minH != null) {
								height = Integer.parseInt(minH);
								height = (cPerObj.getHeight() < height ? height : cPerObj.getHeight());
							}else {
								height = cPerObj.getHeight();
							}
							if(minW != null) {
								width = Integer.parseInt(minW);
								width = (cPerObj.getWidth() < width ? width : cPerObj.getWidth());
							}else {
								width = cPerObj.getWidth();
							}
							chart.setSize(width,height);
							
							
							//sets the size and location
							chart.setSize(width, height);
							chart.setLocation(cPerObj.getLocationX(), cPerObj.getLocationY());
							//gets all indicators that belongs to this chart
							LinkedList<PersistenceIndiParaData> indiList = cPerObj.getIndicatorList();
							if(indiList != null) {
								Iterator<PersistenceIndiParaData> itI = indiList.iterator();
								//this loop iterates once for each indicator-type
								for(int j = 0; j < indiList.size(); j++) {
									PersistenceIndiParaData indiPerData = itI.next();
									String desc = indiPerData.getDescription();
									//gets the greatest parameter. In this way one candle-request is sufficient, even if there are
									//several entries to this indicator-type.
									int max = indiPerData.getParaMax();
									LinkedList<IndicatorParameterPersistence> paraList = indiPerData.getParameterList();
									//carries out the candle-request
									CandleRequestData data = indiController.candleRequest(chart.getSymbol(), chart.getResolution(), max + Chart.ELEM_NUMBER + 1);
									Iterator<IndicatorParameterPersistence> itIPP = paraList.iterator();
									//adds all indicator-entries to the indicator-model
									for(int k = 0; k < paraList.size(); k++) {
										IndicatorParameterPersistence indiPer = itIPP.next();
										indiModel.restore(desc, chart.getID(), indiPer,chart.getResolution(), data); 
									}
								}
							}
							chart.repaintPanel();
						}else {
							System.err.println("Chart konnte nicht wiederhergestellt werden!");
						}
					}
				}
		    	
		    	
		    	
		    	/*
		    	 * This block restores all all alarms that belongs to this chart
		    	 */
		    	
		    	Iterator<AlarmPersistence> itA = alPerList.iterator();
				for(int j = 0; j < alPerList.size(); j++) {
					AlarmPersistence alarm = itA.next();
					String symb = alarm.getSymbol();
					QuoteData qData = chartController.getQuoteData(symb);
					if(qData != null) {
						alarmModel.restoreAlarm(symb, qData.getLastClose(), alarm.getPrice(), alarm.getTyp());
					}else {
						System.err.println("Alarme konnten nicht wiederhergestellt werden");
					}
				}
				
				//restores th watchlist
				if (wlFPer != null) {
				    stockController.initializeWatchlist(wlFPer);;
				    
				}
			}
		}
		
		/*
		 * This block starts the Timer that is responsible to update the watchlist 
		 * with the previous price when the stock exchange opens.
		 */
		
		LocalDate date = LocalDate.now();                
		int day = date.getDayOfMonth();                 
		int month = date.getMonth().getValue()-1;
		int year = date.getYear();
		long time = new GregorianCalendar(year, month, day, 8, 0, 0).getTimeInMillis(); 
		
		LocalTime locTime = LocalTime.now();
		int hour = locTime.getHour();
		if(hour >= 8) {
			time = time + 86400000;
		}
		
		Date dateObj = new Date(time);
		Timer timer = new Timer();
		TaskClosePriceUpdater top = new TaskClosePriceUpdater(watchlistModel, http);
		timer.scheduleAtFixedRate(top,  dateObj,  86400000);
			
	}	
}
