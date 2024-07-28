package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.java_websocket.exceptions.WebsocketNotConnectedException;

import Client.HttpRequest;
import Client.StocksRealTimeClient;
import Client.exception.HTTPException;
import Model.alarmModel.AlarmDataModel;
import Model.chart.CandleRequestData;
import Model.chart.ChartDataModel;

import Model.indicatorModel.IndicatorModel;
import Model.chart.QuoteData;
import Model.watchlist.WatchlistTableModel;
import View.dialogsAndFrames.AlarmDialog;
import View.chart.CandleChartPanel;
import View.chart.Chart;
import View.chart.LineChartPanel;
import View.chart.Statusbar;
import View.dialogsAndFrames.StockerMainFrame;



/**
 * this class organizes the event-handling concerning all <code>JMenuItem</code>s that the <code>JMenuBar</code>
 * in the class {@link #Chart} contains (except for the JMenuItem "Indikatoren") and further events concerning charts.
 * So, it is one of the three controller-classes and manages the view, analyzes the user input
 * and induces the suitable actions concerning the model and the client.
 * This class contains several inner classes that implement the <code>ActionListener</code> interface and
 * handle the occurred events.
 * 
 * @author Benjamin Birner
 *
 */
public class ChartController {
	
	
	//the time for the Timer to update the statusbar if the cursor is "in"
	private int statusbarUpdateTime = 12;
	
	private StockerMainFrame mFrame;
	private WatchlistTableModel wLModel;
	private ChartDataModel chartDataModel;
	private IndicatorModel indiModel;
	private AlarmDataModel alarmModel;
	private IndicatorController indiController;
	private FrameManager frameManager;
	private HttpRequest request;
	private StocksRealTimeClient client;
	
	public ChartController(StockerMainFrame mFrame, WatchlistTableModel wLModel, ChartDataModel chartDataModel,
			IndicatorModel indiModel, AlarmDataModel alarmModel, IndicatorController indiController,FrameManager frameManager, HttpRequest request, StocksRealTimeClient client) {
		this.mFrame = mFrame;
		this.wLModel = wLModel;
		this.chartDataModel = chartDataModel;
		this.indiModel = indiModel;
		this.alarmModel = alarmModel;
		this.indiController = indiController;
		this.frameManager = frameManager;
		this.request = request;
		this.client = client;
	}
	
	
	/*
	 * The method in this class is used if a chart-frame will be closed.
	 */
	private class InternalFrameClosingListener extends InternalFrameAdapter{
		
		private JMenuItem menuItem;
		private Chart chart;
		
		private InternalFrameClosingListener(JMenuItem menuItem, Chart chart) {
			this.menuItem = menuItem;
			this.chart = chart;
		}
		
		@Override
		public void internalFrameClosing(InternalFrameEvent e) {
			//removes the data from the corresponding components
			mFrame.removeMenuItem(menuItem);
			String symbol = chart.getSymbol();
			indiModel.removeChart(chart.getID()); 
			frameManager.removeChartPersistence(chart);
			int index = chartDataModel.getIndex(symbol);
			int i = chartDataModel.remove(index,chart.getResolution(), chart.getID(), chart);
			//true means that there is no more entry to this symbol in the chart Model
			if( i == 1) {
				//checks if the symbol must be unsubscribed
				if( wLModel.getIndex(symbol) < 0) {
					try {
						client.unsubscribeStock(symbol);
					}catch(WebsocketNotConnectedException ex) {
						System.err.println("Keine Verbindung!");
						ex.printStackTrace();
					}
					alarmModel.deleteEntry(symbol);
				}
			}
		}
	}
	
	
	/*
	 * The method in this class is used if the depiction in a chart changes from candle to line or form line to candle.
	 * So, the corresponding JMenuItem has been pushed.
	 */
	private class ChartChangeListener implements ActionListener{
		
		private Chart chart;
		private String to;
		
		public ChartChangeListener(Chart chart, String to) {
			this.chart = chart;
			this.to = to;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (to.equals("line")) {
				chart.lineToCandle();
			}else {
				chart.candleToLine();
			}
			chart.setTitle(chart.getSymbol()+"    "+chart.getTypeAsStringRep()+"    "+chart.getResoAsStringRep());
			chart.repaint();
		
		}
	}
	
	
	/*
	 * The method in this class is used if a chart changes the resolution.
	 * So, the corresponding JMenuItem has just been pushed.
	 */
	private class ResolutionMenuListener implements ActionListener{
		
		private Chart chart;
		private String newResolution;
		
		public ResolutionMenuListener(Chart chart, String newResolution) {
			this.chart = chart;
			this.newResolution = newResolution;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) { 
			
			int index = chartDataModel.getIndex(chart.getSymbol());
			boolean contains = chartDataModel.containsResolution(index, newResolution);
			//checks if the data is already available in the Model
			if(!contains) {
				//carries out a candle request
				CandleRequestData data = indiController.candleRequest(chart.getSymbol(), newResolution, Chart.ELEM_NUMBER + 1);  
				//false means that there was a problem concrning the request
				if(data != null) {
					//adds the entry to the Model
					chartDataModel.addStock(index,chart.getID(),newResolution,data,chart,indiModel);
					//removes the old entry
					chartDataModel.remove(index,chart.getResolution(),chart.getID(), chart);
					chart.setDisabled(newResolution);
					//removes the old entry and adds the current one("Fenster"-menu)
					JMenuItem menuI = new JMenuItem(chart.getSymbol()+" ~ "+newResolution);
					mFrame.addOpenWindowToFenster(menuI, new MenuFensterItemListener(chart));
					mFrame.removeMenuItem(chart.getWindowMenuItem());
					chart.setWindowMenuItem(menuI);
				}
			//no request is necessary
			}else {
				//adds the entry to the Model
				chartDataModel.addChart(index, chart.getID(), newResolution,chart);
				//removes the old entry
				chartDataModel.remove(index,chart.getResolution(),chart.getID(), chart);
				chart.setDisabled(newResolution);
				//removes the old entry and adds the current one("Fenster"-menu)
				JMenuItem menuI = new JMenuItem(chart.getSymbol()+" ~ "+newResolution);
				mFrame.addOpenWindowToFenster(menuI, new MenuFensterItemListener(chart));
				mFrame.removeMenuItem(chart.getWindowMenuItem());
				chart.setWindowMenuItem(menuI);
			} 
			chart.setTitle(chart.getSymbol()+"    "+chart.getTypeAsStringRep()+"    "+chart.getResoAsStringRep());
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
	 * This class is responsible for updating the statusbar and the canvas 
	 * if the mouse enters the canvas.
	 * When the mouse enters the timer starts the updating
	 * When the mouse exits the timer stops updating
	 */
	private class CursorInOutListener extends MouseAdapter{
		private LineChartPanel lPanel;
		private CandleChartPanel cPanel;
		private Statusbar statusbar;
		private Timer timer;
		
		public CursorInOutListener(CandleChartPanel cPanel, LineChartPanel lPanel, Statusbar statusbar, Timer timer) {
			this.cPanel = cPanel;
			this.lPanel = lPanel;
			this.statusbar = statusbar;
			this.timer = timer;
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			lPanel.setCursorIn(true);
			cPanel.setCursorIn(true);
			statusbar.setCursorIn(true);
			timer.start();
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			lPanel.setCursorIn(false);
			cPanel.setCursorIn(false);
			statusbar.setCursorIn(false);
			timer.stop();
		}
	}
	
	/*
	 * The timerTask that updates the statusbar and the canvas
	 */
	private class TimerTask implements ActionListener{
		private Statusbar statusbar;
		private LineChartPanel lPanel;
		private CandleChartPanel cPanel;
		
		private TimerTask(Statusbar statusbar, LineChartPanel lPanel) {
			this.statusbar = statusbar;
			this.lPanel = lPanel;
		}
		
		private TimerTask(Statusbar statusbar, CandleChartPanel cPanel) {
			this.statusbar = statusbar;
			this.cPanel = cPanel;
		}
		
		@Override public void actionPerformed(ActionEvent e) {
			statusbar.updateCursorStatus();
			if(!(lPanel == null)) {
				lPanel.repaint();
			}else {
				cPanel.repaint();
			}
		}
	}
	
	
	/*
	 * The method in this class is called up if the Menu "Funktionen" --> "Alarm" is selected.
	 */
	private class AlarmOpenListener implements ActionListener{
		
		private String symbol;
		private Chart chart;
		
		private AlarmOpenListener(String symbol, Chart chart) {
			this.symbol = symbol;
			this.chart = chart;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			AlarmDialog dia = new AlarmDialog(alarmModel, symbol);
			dia.addAddAlarmListener(new AddAlarmListener(dia,symbol,chart));
			dia.addDeleteAlarmListener(new DeleteAlarmListener(symbol,dia,chart));
			dia.addAlarmOkButtonListener(new AlarmOkButtonListener(dia));
			dia.setAlarmTFListener(new AddAlarmListener(dia,symbol,chart));
			dia.setVisible(true);
		}
	}
	
	
	/*
	 * The method in this class is called up if in the alarm dialog the "hinzufügen"-button is pushed
	 */
	private class AddAlarmListener implements ActionListener{
		
		private AlarmDialog dia;
		private String symbol;
		private Chart chart;
		
		private AddAlarmListener(AlarmDialog dia, String symbol, Chart chart) {
			this.dia = dia;
			this.symbol = symbol;
			this.chart = chart;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			double d = 0;
			try {
				//gets the entry for the textfield
				String entry = dia.getTextfieldEntry();
				d= Double.parseDouble(entry);
				//checks if the entry is greater than 0
				if( d <= 0) {
					throw new IllegalArgumentException("inkorrekte Eingabe! Wert <= 0!");
				}else {
					//adds the alarm to the alarm-model
					alarmModel.addAlarm(symbol, chartDataModel.getLastPrice(symbol), d);
					dia.refreshList();
					chart.repaintPanel();
				}
			}catch(IllegalArgumentException ae) {
				JOptionPane.showMessageDialog(null,  "Bitte Zahl größer 0 eingeben!", "Inkorrekte Eingabe!",JOptionPane.ERROR_MESSAGE);
				dia.clearTF();
			}
		}
	}                                                               
	
	
	/*
	 * The method in this class is called up if in the alarm dialog the "löschen"-button is pushed.
	 */
	private class DeleteAlarmListener implements ActionListener{
		
		private AlarmDialog dia;
		private String symbol;
		private Chart chart;
		
		private DeleteAlarmListener(String symbol,AlarmDialog dia, Chart chart) {
			this.dia = dia;
			this.symbol = symbol;
			this.chart = chart;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int[] indices = dia.getSelectedIndices();
			alarmModel.deleteAlarm(symbol, indices);
			dia.refreshList();
			chart.repaintPanel();
		}                                      
	}
	
	
	/*
	 * The method in this class is called up if in the alarm dialog the "ok"-button is pushed.
	 */
	private class AlarmOkButtonListener implements ActionListener{
		
		private AlarmDialog dia;
		
		private AlarmOkButtonListener(AlarmDialog dia) {
			this.dia = dia;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			dia.dispose();
		}
	}
	
	
	
	/**
	 * This method realizes an essential part to create a new chart.
	 * It builds up several elements that are required and initializes the chart.
	 * It is only called up from the <code>initializeNewChart(String symbol, String reso, String type)</code> 
	 * in the StockerController.
	 * 
	 * @param symbol defines the stock that should be depicted in the chart.
	 * @param menuI the JMenuItem that should be added to the menu "Fenster".
	 * @param reso defines the resolution to the stock that should be depicted.
	 * @return
	 */
	protected Chart createNewChart(String symbol, JMenuItem menuI, String reso) {
		QuoteData qData = getQuoteData(symbol);
		LineChartPanel lPanel = new LineChartPanel(chartDataModel,alarmModel,indiModel,symbol, reso);
		CandleChartPanel cPanel = new CandleChartPanel(chartDataModel,alarmModel,indiModel,symbol, reso);
		Chart chart = new Chart(mFrame,lPanel,cPanel, symbol, reso, menuI); 
		Statusbar sBar = null;
		if( qData == null) {
			sBar = new Statusbar(symbol,chart,cPanel, lPanel,chartDataModel,0.0);
		}else {
			sBar = new Statusbar(symbol,chart,cPanel, lPanel,chartDataModel,qData.getLastClose());
		}
		chart.addStatusbar(sBar);
		lPanel.setChartID(chart.getID());
		cPanel.setChartID(chart.getID());
		frameManager.addChartPersistence(chart);
		Timer cTimer = new Timer(statusbarUpdateTime, new TimerTask(sBar,cPanel));
		Timer lTimer = new Timer(statusbarUpdateTime, new TimerTask(sBar, lPanel));
		lPanel.addCursorInOutListener(new CursorInOutListener(cPanel, lPanel, sBar, lTimer) );
		cPanel.addCursorInOutListener(new CursorInOutListener(cPanel, lPanel, sBar, cTimer) );
		chart.setMenuAlarmListener(new AlarmOpenListener(symbol,chart));
		chart.setMenuLineChartListener(new ChartChangeListener(chart, "candle"));
		chart.setMenuCandleChartListener(new ChartChangeListener(chart, "line"));
		chart.addInternalFrameListener(new InternalFrameClosingListener(menuI, chart));
		chart.setMenuMin1Listener(new ResolutionMenuListener(chart, "1"));
		chart.setMenuMin5Listener(new ResolutionMenuListener(chart, "5"));
		chart.setMenuMin10Listener(new ResolutionMenuListener(chart, "10"));
		chart.setMenuMin15Listener(new ResolutionMenuListener(chart, "15"));
		chart.setMenuMin30Listener(new ResolutionMenuListener(chart, "30"));
		chart.setMenuH1Listener(new ResolutionMenuListener(chart, "60"));
		chart.setMenuH4Listener(new ResolutionMenuListener(chart, "240"));
		chart.setMenuDayListener(new ResolutionMenuListener(chart, "D"));
		chart.setMenuWeekListener(new ResolutionMenuListener(chart, "W"));
		chart.setMenuMonthListener(new ResolutionMenuListener(chart, "M"));
		indiController.setAllIndiResolutionListener(chart);
		return chart;
		
	}
	
	
	/**
	 * this method carries out a quote-request to a given symbol.
	 * It contains the following data:
	 * last close, last open, last low, last high, previous close and the timestamp.
	 * 
	 * @param symbol the the id for the stock to which the data is required
	 * @return the data from the request
	 */
	public QuoteData getQuoteData(String symbol) {
		URLQuoteRequest url = new URLQuoteRequest(symbol);
		QuoteData quoteData = null;                                                                                    
		try {
			quoteData= request.quoteRequest(url.getUrlString());
		} catch (MalformedURLException e) {
			System.err.println("URL ist falsch aufgebaut!");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("URL konnte nicht geöffnet werden!");
			e.printStackTrace();
		} catch (HTTPException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		return quoteData;
	}
}
