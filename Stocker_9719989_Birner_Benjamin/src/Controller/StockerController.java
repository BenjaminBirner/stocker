package Controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.java_websocket.exceptions.WebsocketNotConnectedException;

import Client.HttpRequest;
import Client.StocksRealTimeClient;
import Client.exception.HTTPException;
import Model.alarmModel.AlarmDataModel;
import Model.alarmModel.AlarmPersistence;
import Model.chart.CandleRequestData;
import Model.chart.ChartDataModel;
import Model.chart.ChartPersistence;
import Model.watchlist.FramePersistence;
import Model.indicatorModel.IndicatorModel;
import Model.indicatorModel.PersistenceIndiParaData;
import Model.persistence.PropertiesCache;
import Model.chart.QuoteData;
import Model.searchModel.SearchResultModel;
import Model.persistence.StockerPersistenceData;
import Model.persistence.StockerPersistenceToPropertiesFile;
import Model.watchlist.WatchlistPersistence;
import Model.watchlist.WatchlistStock;
import Model.watchlist.WatchlistTableModel;
import View.chart.Chart;
import View.dialogsAndFrames.ConfigurationDialog;
import View.dialogsAndFrames.SearchJDialog;
import View.dialogsAndFrames.StockerMainFrame;
import View.dialogsAndFrames.Watchlist;



/**
 * this class organizes the event-handling concerning all <code>JMenuItem</code>s that the <code>JMenuBar</code>
 * in the class {@link #StockerMainFrame} contains as well as the Frames and Dialogs
 * that can be opened with these JMenuItems.
 * So, it is one of the three controller-classes and manages the view, analyzes the user input
 * and induces the suitable actions concerning the model and the client.
 * This class contains several inner classes that implement the <code>ActionListener</code> interface and
 * handle the occurred events.
 * 
 * @author Benjamin Birner
 *
 */
public class StockerController {
	
	private StockerMainFrame view;
	private ChartDataModel chartDataModel;
	private SearchResultModel searchModel;             
	private WatchlistTableModel watchlistModel;
	private IndicatorModel indiModel;
	private AlarmDataModel alarmModel;
	private HttpRequest request;
	private StocksRealTimeClient client;
	private ChartController chartController;
	private IndicatorController indiController;
	private FrameManager frameManager;
	
	public StockerController(StockerMainFrame view, ChartDataModel chartDataModel, SearchResultModel searchModel, WatchlistTableModel watchlistModel, AlarmDataModel alarmModel,
			          HttpRequest request, StocksRealTimeClient client, ChartController chartController,IndicatorController indiController, IndicatorModel indiModel, FrameManager frameManager) {
		this.view = view;
		this.chartDataModel = chartDataModel;
		this.searchModel = searchModel;
		this.watchlistModel = watchlistModel;
		this.alarmModel = alarmModel;
		this.request = request;
		this.client = client;
		this.chartController = chartController;
		this.indiController = indiController;
		this.indiModel = indiModel;
		this.frameManager = frameManager;
		//adds all listeners to the MainFrame
		addMainFrameListener();
	}
	//adds all listeners to the MainFrame
	private void addMainFrameListener() {
		view.setFileCloseMItemListener(new CloseStockerListener());
		view.setSearchMItemListener(new SearchDialogOpenListener());
		view.setWatchlistMItemListener(new WatchlistOpenListener());
		view.setConfigMItemListener(new ConfigDialogOpenListener());
		view.addMainFrameClosingListener(new MainFrameClosingListener());
	}
	
	
	private class MainFrameClosingListener extends WindowAdapter{
		
		@Override
		public void windowClosing(WindowEvent e) {
			closeAndStore();
		}
	}
	
	/*
	 * The method in this class is called up if the JMenuItem "Beenden" or the closeIcon in the MainFrame is pushed.
	 */
	private class CloseStockerListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) { 
			closeAndStore();
		}
	}
	
	/*
	 * It collects all data that must be stored and stores this data. It builds up the required StockerPersistenceData-Object.
	 */
	private void closeAndStore() {
		//The user is asked if he would like to store.
		//This choice is important if the stocker starts an there are problems concerning the connection
		//so that the old storage can not be restored. The user must change the connection configurations
		//and restart the stocker without storing.
		//without this choice the old storage would get lost because it would be overwritten.
		int store = JOptionPane.showConfirmDialog(null,
	             "Möchten Sie speichern und damit den alten\n"
				+"Speicherstand überschreiben?", "Speichern?", JOptionPane.YES_NO_OPTION);
		if( store ==1) {
			PropertiesCache prop = PropertiesCache.getInstance();
			try {
				prop.store();
			} catch (FileNotFoundException e1) {
				System.err.println("Die Datei konnte nicht gefunden werden!");
				e1.printStackTrace();
			} catch (IOException e1) {
				System.err.println("Ein- Ausgabefehler!");
				e1.printStackTrace();
			}
			System.exit(0);
		}
		
		StockerPersistenceData spd = new StockerPersistenceData();
		//gets all the frame-data regarding the Main-Frame
		FramePersistence main = new FramePersistence(view.getHeight(),view.getWidth(),(int) view.getLocation().getX(),
				(int) view.getLocation().getY(),0);
		//sets the MainFrame-data
		spd.setMainFrame(main);
		//gets the data concerning the watchlist
		FramePersistence wl = frameManager.getWLPersistenceObj();
		//sets this data if there is one
		if(wl != null) {
			spd.setWLFrame(wl);
		}
		//gets  all the remaining data (watchlist-entries, alarms, charts, indicators).
		LinkedList<WatchlistPersistence> wlPerList = watchlistModel.createWLPersistenceList();
		LinkedList<AlarmPersistence> alarmPer = alarmModel.getAlarmPersistenceData();
		LinkedList<ChartPersistence> chartPer = frameManager.getChartPersistenceObjs();
		Iterator<ChartPersistence> it = chartPer.iterator();
		for(int i = 0; i < chartPer.size(); i++) {
			ChartPersistence chart = it.next();
			LinkedList<PersistenceIndiParaData> indiList = indiModel.getPersistenceDataFor(chart.getID());
			chart.setPersistenceIndicator(indiList);
		}
		//sets this data
		spd.addWLPersistenceList(wlPerList);
		spd.addAlarmList(alarmPer);
		spd.addChartList(chartPer);
		StockerPersistenceToPropertiesFile stockerPersistence = new StockerPersistenceToPropertiesFile();  // change here to attribute --> only for test
		
		try {
			stockerPersistence.storeToPropertiesFile(spd);
			System.exit(0);
		} catch (FileNotFoundException e1) {
			System.err.println("Die Datei konnte nicht gefunden werden");
			e1.printStackTrace();
		} catch (IOException e1) {
			System.err.println("Die Datei konnte nicht gefunden werden");
			e1.printStackTrace();
		}
		int error = JOptionPane.showConfirmDialog(null,
	             "Fehler beim Speichervorgang!\n"
				+"\n"
	            +"Stocker ohne Speichern beenden? ", "Fehlermeldung" , JOptionPane.YES_NO_OPTION);
		if(error == 0) {
			System.exit(0);
		}
		
	}
	
	/*
	 * The method in this class is called up if the JMenuItem "Aktiensuche" is pushed.
	 * It creates a SearchJDialog-Object and adds all listeners.
	 */
	private class SearchDialogOpenListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {     
			SearchJDialog searchDia = new SearchJDialog(view, searchModel);
			searchDia.setSearchTFListener(new StartSearchListener(searchDia));
			searchDia.setSearchButtonListener(new StartSearchListener(searchDia));
			searchDia.setChartOpenButtonListener(new ChartOpenListener(searchDia));
			searchDia.setWatchlistAddButtonListener(new WatchlistAddListener(searchDia));
			searchDia.setExitButtonListener(new ExitButtonListener(searchDia));
			searchDia.setListMouseListener(new SearchListMouseListener(searchDia));
			searchDia.setVisible(true);
		}	
	}
	
	/*
	 * The method in this class is called up if the JMenuItem "Watchlist" is pushed.
	 */
	private class WatchlistOpenListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) { 
			if(!frameManager.getWLStatus()) {
				initializeWatchlist(null);
			}
		}
	}
	
	/**
	 * This Method creates a {@link #View.Watchlist}-object and adds all listeners.
	 * It is called up in the inner class WatchlistOpenListener and when the stocker
	 * starts.
	 */
	public void initializeWatchlist(FramePersistence fp) {
		Watchlist watchlist;
		if(fp != null) {
			watchlist = new Watchlist(watchlistModel, view, fp);
		}else {
			watchlist = new Watchlist(watchlistModel, view);
		}
		watchlist.setWLSearchMenueListener(new SearchDialogOpenListener());
		watchlist.setWLChartOpenListener(new ChartOpenListener(watchlist));
		watchlist.setWLDeleteListener(new WLDeleteListener(watchlist));
		JMenuItem menuItem = new JMenuItem("Watchlist");
		watchlist.addInternalFrameListener(new InternalFrameClosingListener(menuItem));
		//creates a new JMenuItem in the JMenu "Fenster"
		view.addOpenWindowToFenster(menuItem, new MenuFensterItemListener(watchlist));
		frameManager.addWLPersistence(watchlist);
	}
	
	
	/*
	 * The method in this class is called up if the JMenuItem "Einstellungen ändern" is pushed. It creates a
	 * ConfigurationDialog-Object and sets all listeners.
	 */
	private class ConfigDialogOpenListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			ConfigurationDialog dia = new ConfigurationDialog(indiModel);
			dia.setConfigurationOkButtonListener(new ConfigurationOkButtonListener(dia));
			dia.setExitButtonListener(new ExitButtonListener(dia));
			dia.setResetButtonListener(new ResetButtonListener(dia));
			dia.setIndiColorButtonListener(new IndiColorButtonListener(dia));
			dia.setAlarmColorButtonListener(new AlarmColorButtonListener());
			dia.setConnectionAddListener(new ConnectionAddListener(dia));
			dia.setApiTFListener(new ConnectionAddListener(dia));
			dia.setPullTFListener(new ConnectionAddListener(dia));
			dia.setPushTFListener(new ConnectionAddListener(dia));
			dia.setConnectionRemoveListener(new ConnectionRemoveListener(dia));
			dia.setApiListMouseListener(new apiListMouseListener(dia));
			dia.setPullListMouseListener(new pullListMouseListener(dia));
			dia.setPushListMouseListener(new pushListMouseListener(dia));
			dia.setVisible(true);
		}
	}
	
	
	/*
	 * The method in this class is called up if the "OK"-Button in the Configuration-Dialog is pushed.
	 * It takes over the new settings and stores them in the Properties.
	 */
	private class ConfigurationOkButtonListener implements ActionListener{
		
		private ConfigurationDialog dia;
	
		private ConfigurationOkButtonListener(ConfigurationDialog dia) {
			this.dia = dia;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//gets all entries regarding the dialog
			String height = dia.getTFHeight();
			String width = dia.getTFWidth();
			String chartTyp = dia.getChartTyp();
			String resolution = dia.getResolution();
			String api = dia.getSelectedApiKey();
			String pull = dia.getSelectedPullUrl();
			String push = dia.getSelectedPushUrl();
			PropertiesCache prop = PropertiesCache.getInstance();
			//sets all configurations to the Properties
			prop.setProperty("chartType", chartTyp);
			prop.setProperty("resolution", resolution);
			
			if(api != null) {
				prop.setProperty("api.key", api);
			}
		    if(pull != null) {
		    	prop.setProperty("pull.url", pull);
		    }
			if(push != null) {
				prop.setProperty("push.url", push);
				//gets all the symbols regarding the watchlist and the charts
				String[] symbWL = watchlistModel.getAllSymbols();
				Set<String> symbols = frameManager.getAllSymbols();
				for(int i = 0; i < symbWL.length; i++) {
					symbols.add(symbWL[i]);
				}
				//unsubscribes all stocks and closes the connection
				Iterator<String> it = symbols.iterator();
				if(client.isConnected() && client != null) {
					for(int i = 0; i < symbols.size(); i++) {
						
						try {
							client.unsubscribeStock(it.next());
						}catch(WebsocketNotConnectedException ex) {
							System.err.println("Keine Verbindung!");
							ex.printStackTrace();
							break;
						}
						
					}
					client.close();
				}
				//creates a new StocksRealTimeClient-Object the URI "push" and builds up the connection
				client = new StocksRealTimeClient(push, watchlistModel, chartDataModel, alarmModel);
				client.start();
				//subscribes all symbols from above
				Iterator<String> iter = symbols.iterator();
				for(int i = 0; i < symbols.size(); i++) {
					
					try {
						client.subscribeStock(iter.next());
					}catch(WebsocketNotConnectedException ex) {
						System.err.println("Keine Verbindung!");
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null,  "Es besteht derzeit keine Websocket Vebindung!", "Verbindungsaufbau Fehlgeschlagen!!!",JOptionPane.ERROR_MESSAGE);
						break;
					}
					
				}
				
			}
			//gets the configuration for the JInternalFrame-Min-Size and sets it to the Properties
			int height2 = -1;
			int width2 = -1;
			try {
				if(!height.isEmpty()) {
					height2 = Integer.parseInt(height);
					if(height2 < 140 || height2 > 1800) {
						throw new IllegalArgumentException("inkorrekte Eingabe! Wert kleiner 140 oder größer 1800!");
					}
					prop.setProperty("height", height);
				}
				if(!width.isEmpty()) {
					width2 = Integer.parseInt(width);
					if(width2 < 240 || width2 > 2400) {
						throw new IllegalArgumentException("inkorrekte Eingabe! Wert kleiner 240 oder größer 2400!");
					}
					prop.setProperty("width", width);
				}
				if(height2 == -1) {
					height = prop.getProperty("height");
					if(height == null) {
						height2 = Chart.MIN_HEIGHT;
					}else {
						height2 = Integer.parseInt(height);
					}
				}
				if(width2 == -1) {
					width = prop.getProperty("width");
					if(width == null) {
						width2 = Chart.MIN_WIDTH;
					}else {
						width2 = Integer.parseInt(width);
					}
				}
				//checks the size of all JInternalFrames and resize them if necessary
				frameManager.checkMinSize(width2,height2);
				dia.dispose();
			}catch(IllegalArgumentException ae) {
				JOptionPane.showMessageDialog(null,  "Höhe und Breite konnten nicht geändert werden,\n"
					                               + "da inkorrekte Eingabe!\n"
					                               + "\n"
					                               + "Bitte  140 <= Höhe <= 1800\n"
					                               + "und 240 <= Breite <= 2400\n"
					                               + "eingeben.",
					                               "Inkorrekte Eingabe!",JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/*
	 * The method in this class is called up if the "Zurücksetzen"-Button in the Configuration-Dialog is pushed.
	 * It removes all settings from the Properties so that the default configurations are going to be used.
	 */
	private class ResetButtonListener implements ActionListener{
		
		private ConfigurationDialog dia;
		
		private ResetButtonListener(ConfigurationDialog dia) {
			this.dia = dia;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			PropertiesCache prop = PropertiesCache.getInstance();
			//the entries regarding the indicator-color are deleted
			String[] descs = indiModel.getAllIndiDescriptions();
			for(int i = 0; i < descs.length; i++) {
				prop.removeProperty(descs[i]);
			}
			prop.removeProperty("chartType");
			prop.removeProperty("resolution");
			prop.removeProperty("height");
			prop.removeProperty("width");
			prop.removeProperty("api.key");
			prop.removeProperty("pull.url");
			prop.removeProperty("push.url");
			prop.removeProperty("api.key.list");
			prop.removeProperty("pull.list");
			prop.removeProperty("push.list");
			
			dia.dispose();
		}
	}
	
	
	/*
	 * The method in this class is called up if the "Farbe wählen"-Button in the Configuration-Dialog concerning the indicators is pushed.
	 * It opens the JColorChooser-Dialog, gets the selected color and sets it to the Properties.
	 */
	private class IndiColorButtonListener implements ActionListener{
		
		private ConfigurationDialog dia;
		
		private IndiColorButtonListener(ConfigurationDialog dia) {
			this.dia = dia;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {  
			int index = dia.getSelectedIndi();
			if(index != -1) {
				String desc = indiModel.getDescriptionAt(index);
				Color color = JColorChooser.showDialog(null, "Choose" + desc + "Color", Color.BLACK);  // adapt black (maybe)
				if(color != null) {
					int rgb = color.getRGB();
					PropertiesCache prop = PropertiesCache.getInstance();
					prop.setProperty(desc+".rgb", rgb+"");
					frameManager.repaint();
				}	
			}
			
		}
	}
	
	/*
	 * The method in this class is called up if the "Farbe wählen"-Button  in the Configuration-Dialog concerning the alarms is pushed.
	 * It opens the JColorChooser-Dialog, gets the selected color and sets it to the Properties.
	 */
	private class AlarmColorButtonListener implements ActionListener{
	
		@Override
		public void actionPerformed(ActionEvent e) {  
			Color color = JColorChooser.showDialog(null, "Choose Alarm Color", Color.BLACK);  // adapt black (maybe)
			if(color != null) {
				int rgb = color.getRGB();
				PropertiesCache prop = PropertiesCache.getInstance();
				prop.setProperty("alarm.rgb", rgb+"");
				frameManager.repaint();
			}	
		}
	}
	
	
	/*
	 * The method in this class is called up if the "hinzufügen"-Button in the Configuration-Dialog regarding the connections is pushed.
	 * It opens the JColorChooser-Dialog, gets the selected color and sets it to the Properties.
	 */
	private class ConnectionAddListener implements ActionListener{
		
		private ConfigurationDialog dia;
		
		private ConnectionAddListener(ConfigurationDialog dia) {
			this.dia = dia;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {  
			
			//gets the textfield-entries and sets them to the Properties
			String api = dia.getApiEntry();
			String pull = dia.getPullEntry();
			String push = dia.getPushEntry();
			if(!api.isEmpty()) {
				dia.setEntryToProp("api.key.list", api);
				dia.clearTF();
			}
			if(!pull.isEmpty()) {
				dia.setEntryToProp("pull.list", pull);
				dia.clearTF();
			}
			if(!push.isEmpty()) {
				dia.setEntryToProp("push.list", push);
				dia.clearTF();
			}
			dia.refreshConnectionList();
		}
	}
	
	
	/*
	 * The method in this class is called up if the "löschen"-Button in the Configuration-Dialog regarding the connections is pushed.
	 * It gets the selected entries and removes them from the Properties
	 */
	private class ConnectionRemoveListener implements ActionListener{
		
		private ConfigurationDialog dia;
		
		private ConnectionRemoveListener(ConfigurationDialog dia) {
			this.dia = dia;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {  

			int apiIndex = dia.getSelectedApiIndex();
			int pullIndex = dia.getSelectedPullIndex();
			int pushIndex = dia.getSelectedPushIndex();
			if(apiIndex != -1) {
				dia.removeConnection("api.key.list", apiIndex);
			}
			if(pullIndex != -1) {
				dia.removeConnection("pull.list", pullIndex);
			}
			if(pushIndex != -1) {
				dia.removeConnection("push.list", pushIndex);
			}
			dia.refreshConnectionList();
		}
	}
	
	/*
	 * This method is called up if there is mouse click concerning an entry in the api-list.
	 * It clears the selection if there is one.
	 */
	private class apiListMouseListener extends MouseAdapter{
		
	    private ConfigurationDialog dia;
			
		private apiListMouseListener(ConfigurationDialog dia){
			this.dia = dia;
		}
			
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() > 1) {
				dia.clearApiSelection();
			}
		}
	}
	
	/*
	 * This method is called up if there is mouse click concerning an entry in the pull-list.
	 * It clears the selection if there is one.
	 */
	private class pullListMouseListener extends MouseAdapter{
		
	    private ConfigurationDialog dia;
			
	    private pullListMouseListener(ConfigurationDialog dia){
	    	this.dia = dia;
	    }
			
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() > 1) {
				dia.clearPullSelection();
			}
		}
	}

	/*
	 * This method is called up if there is mouse click concerning an entry in the push-list.
	 * It clears the selection if there is one.
	 */
	private class pushListMouseListener extends MouseAdapter{
	
		private ConfigurationDialog dia;
		
		private pushListMouseListener(ConfigurationDialog dia){
			this.dia = dia;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() > 1) {
				dia.clearPushSelection();
			}
		}
	}
	
	/*
	 * The method in this class is called up if the "Suche starten"-Button in the Search-Dialog is pushed.
	 * It gets the entry and carries out a request.
	 */
	private class StartSearchListener implements ActionListener{
		
        private SearchJDialog searchDia;
		
		private StartSearchListener(SearchJDialog searchDia){
			this.searchDia = searchDia;
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String entry = searchDia.getTextfieldEntry();   
			URLSearchRequest url = new URLSearchRequest(entry);
			try {
				
				request.searchRequest(url.getUrlString());
				
			} catch (MalformedURLException e1) {
				System.err.println("URL ist falsch aufgebaut!");
				e1.printStackTrace();
			} catch (IOException e1) {
				System.err.println("URL konnte nicht geöffnet werden!");
				e1.printStackTrace();
			} catch (HTTPException e1) {
				System.err.println(e1.getMessage());
				e1.printStackTrace();
			}
			
			searchDia.refreshList();
		}
	}
	
	
	/*
	 * This method is called up if a entry in the watchlist or in the Search-Dialog is selected in order
	 * to open it in chart. The corresponding button must be pushed. It creates and initializes the 
	 * corresponding chart-object.
	 */
	private class ChartOpenListener implements ActionListener{  
		                                                
		private Watchlist watchlist;
		private SearchJDialog searchDia;
		
		private ChartOpenListener(Watchlist watchlist) {
			this.watchlist = watchlist;
		}
		
		private ChartOpenListener(SearchJDialog searchDia) {
			this.searchDia = searchDia;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String[] symbols;   
			//gets the resolution from the Properties or uses the default resolution if there is non
			PropertiesCache prop = PropertiesCache.getInstance();
			String reso = prop.getProperty("resolution");
			if(reso == null) {
				reso = Chart.RESOLUTION;
			}
			//gets the chart-type from the Properties
			String type = prop.getProperty("chartType");
			if(type == null) {
				type = Chart.TYPE;
			}
			//checks if the instruction comes from the watchlist or the Search-Dialog and gets the selected symbols.
			if(watchlist != null) {
				int[] rows = watchlist.getSelectedRows();
				symbols = watchlistModel.getSymbolsAt(rows);
			}else{
				int[] selectedIndices = searchDia.getSelectedIndices();
				symbols = searchModel.getSymbolsAt(selectedIndices);
			}
			if(symbols != null) {
				for(int i = 0; i < symbols.length; i++) {
					//builds up the Chart-Object with the data above
					initializeNewChart(symbols[i],reso,type);
				}
			}
			
		}
	}
	
	
	/**
	 * creates and initializes a Chart-Object with all its parts. So, it also carries out a
	 * candle-request and subscribes a stock if necessary. Finally, it adds this stock to 
	 * the {@link #ChartDataModel}-instance.
	 * This method is called up if a chart will be opened.
	 * 
	 * @param symbol the id that defines the stock
	 * @param reso the resolution that belongs to this stock
	 * @param type defines the chart-type --> candle/line
	 * @return the initialized {@link #Chart}-object
	 */
	public Chart initializeNewChart(String symbol, String reso, String type) {
		CandleRequestData data = indiController.candleRequest(symbol, reso, Chart.ELEM_NUMBER + 1);
		//true means the request was successful
		if( data != null) {
			JMenuItem menuI = new JMenuItem(symbol+" ~ "+reso);
			//creates a new chart and initializes the chart with the main parts
			Chart chart = chartController.createNewChart(symbol,menuI,reso);
			//adds the JMenuItem to the JMenu "Fenster"
			view.addOpenWindowToFenster(menuI, new MenuFensterItemListener(chart));
			int listIndex = chartDataModel.getIndex(symbol);
			//checks if there already exists an entry to this stock
			if(listIndex < 0) {
				chartDataModel.createNewEntry(-listIndex-1, symbol);
				//checks if it is necessary to subscribe this stock
				if(watchlistModel.getIndex(symbol) < 0) {
					try {
						client.subscribeStock(symbol);
					}catch(WebsocketNotConnectedException ex) {
						System.err.println("Keine Verbindung!");
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null,  "Es besteht keine Websocket Vebindung!", "Keine Verbindung!",JOptionPane.ERROR_MESSAGE);
					}
				}
			}                                                                               
			listIndex = chartDataModel.getIndex(symbol);
			boolean contains = chartDataModel.containsResolution(listIndex, reso);  
			//checks if there exists an entry to this stock with the same resolution
			if(!contains) {           
				chartDataModel.addStock(listIndex,chart.getID(), reso, data, chart,indiModel);                      
			}else {
				//it is only necessary to add this chart as a listener
				chartDataModel.addChart(listIndex,chart.getID(), reso, chart);                          
			}
			chart.calcScale();
			if(type.equals("c")) {
				chart.addCandlePanel();
			}else {
				chart.addLinePanel();
			}
			chart.setTitle(symbol+"    "+chart.getTypeAsStringRep()+"    "+chart.getResoAsStringRep());
			chart.repaint();
			return chart;
		}
		return null;
	}
		


	/*
	 * The method in this class is called up if an entry in the Search-Dialog is selected and the "zur Watchlist hinzufügen"-Button 
	 * is pushed. It adds this entry to the Watchlist.
	 */
	private class WatchlistAddListener implements ActionListener{
		
		private SearchJDialog searchDia;
		
		private WatchlistAddListener(SearchJDialog searchDia){
			this.searchDia = searchDia;
		}
		
		@Override
		public void actionPerformed(ActionEvent e){
			//gets the selected indices
			int[] selectedIndices = searchDia.getSelectedIndices();
			//gets the symbols to these indices
			String[] symbols = searchModel.getSymbolsAt(selectedIndices);
			if(symbols != null) {
				//adds all entries
				for(int i = 0; i < symbols.length; i++) {
					int tableIndex = watchlistModel.getIndex(symbols[i]);
					if(tableIndex < 0) {
						String description = searchModel.getDescriptionAt(selectedIndices[i]);
						addNewEntryToWL(symbols[i],description);
					}
				}
			}
			
		
	    }
	}
	
	
	
	/**
	 * adds a new entry to the watchlist. It also subscribes the stocks with the symbol<code>symbol</code>
	 * if it has not subscribed yet.
	 * 
	 * @param symbol
	 * @param description
	 */
	public void addNewEntryToWL(String symbol, String description) {
		//carries out the quote-request
		QuoteData qData = chartController.getQuoteData(symbol);
		if(qData != null) {
			double percent = ((qData.getLastClose() - qData.getPreviousClose()) * 100) / qData.getPreviousClose();
			//creates a new watchlist-stock/entry
			WatchlistStock stock = new WatchlistStock(description, symbol, qData.getLastClose(),percent, qData.getPreviousClose());
			int tableIndex = watchlistModel.getIndex(symbol);
			watchlistModel.addStockToTable(stock, tableIndex);  
			//checks if it is necessary to subscribe this stock
			if(!(chartDataModel.contains(symbol))) {
				try {
					client.subscribeStock(symbol);
				}catch(WebsocketNotConnectedException ex) {
					System.err.println("Keine Verbindung!");
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null,  "Es besteht keine Websocket Vebindung!", "Keine Verbindung!",JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
	
	

	
	
	

	/*
	 * The method in this class is called up if there are two mouse clicks concerning an entry in the Search-Dialog.
	 * It creates a Chart-Object to this entry.
	 */
	private class SearchListMouseListener extends MouseAdapter{
		
    private SearchJDialog searchDia;
		
		private SearchListMouseListener(SearchJDialog searchDia){
			this.searchDia = searchDia;
		}
		
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() > 1) { 
				//gets the symbol
				int selectedIndex = searchDia.getSelectedIndex();
				String symbol = searchModel.getSymbolAt(selectedIndex);
				//gets the required data from the Properties
				PropertiesCache prop = PropertiesCache.getInstance();
				String reso = prop.getProperty("resolution");
				if(reso == null) {
					reso = Chart.RESOLUTION;
				}
				String type = prop.getProperty("chartType");
				if(type == null) {
					type = Chart.TYPE;
				}
				if(symbol != null) {
					initializeNewChart(symbol,reso,type);
				}
			}
		}
	}
	
	
	/*
	 * The method in this class is called up if in a dialog the "Abbrechen"-Button is pushed.
	 * It closes this dialog.
	 */
	private class ExitButtonListener implements ActionListener{
		
		private JDialog dia;
		
		private ExitButtonListener(JDialog dia) {
			this.dia = dia;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			dia.dispose();                        
		}
	}
	

	/*
	 * The method in this class is called up if in the JMenu "Fenster" a JMenuItem is selected.
	 * It places the corresponding JInternalFrame-Object in the foreground.
	 */
	private class MenuFensterItemListener implements ActionListener{
		
			private JInternalFrame frame;
			
			private MenuFensterItemListener(JInternalFrame frame) {
				this.frame = frame;
			}
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.toFront();
				
		}
	}
	
	
	/*
	 * The method in this class is called up if there are selected rows in the watchlist and in the pop-up-menu the 
	 * JMenuItem "löschen" is pushed. The method deletes the selected entries.
	 */
	private class WLDeleteListener implements ActionListener{
		
		private Watchlist watchlist;
		
		private WLDeleteListener(Watchlist watchlist) {
			this.watchlist = watchlist;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int[] rows = watchlist.getSelectedRows();
			String[] symbols = watchlistModel.removeStocks(rows);
			for(String s : symbols) {
				// unsubscribes the stocks and deletes the existing alarms if it is no longer required.
				// this means that there is no open chart to this stock
				if(!(chartDataModel.contains(s))) {
					try {
						client.unsubscribeStock(s);
					}catch(WebsocketNotConnectedException ex) {
						System.err.println("Keine Verbindung!");
						ex.printStackTrace();
					}
					alarmModel.deleteEntry(s);
				}
			}
		}
	}
	
	
	
	/*
	 * The method in this class is called up if the closing icon of a JInternalFrame is pushed.
	 * It removes the corresponding JMenuItem in the Menu "Fenster".
	 */
	private class InternalFrameClosingListener extends InternalFrameAdapter{
		
		private JMenuItem menuItem;
		
		private InternalFrameClosingListener(JMenuItem menuItem) {
			this.menuItem = menuItem;
		}
		
		@Override
		public void internalFrameClosing(InternalFrameEvent e) {
			view.removeMenuItem(menuItem);
			frameManager.removeWLPersistence();
		}
	}

}
