package View.chart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import Model.persistence.PropertiesCache;
import Model.chart.StockListener;
import View.dialogsAndFrames.StockerMainFrame;

/**
* This class extends the JInternalFrame-class and provides the frame
* and the JMenubar for the Chart.
* It also is the interface between the view and the model. So it implements
* the StockListener interface.
* The statusbar and the Panels that contain the canvas and depict the data
* are added to an instance of this class.
* 
* @author Benjamin Birner
*
*/
public class Chart extends JInternalFrame implements StockListener {
	
    
	/**
	 * the default configuration for the minimum height of the JInternalFrames
	 */
	public static final int MIN_HEIGHT = 300;

	/**
	 * the default configuration for the minimum width of the JInternalFrames
	 */
	public static final int MIN_WIDTH = 400;

	/**
	 * the default configuration for the resolution
	 */
	public static final String RESOLUTION = "D";
	
	/**
	 * the default configuration for the chart-type
	 */
	public static final String TYPE = "c";
	
	/**
	 * the default configuration for the alarm-color
	 */
	public static final Color ALARM_COLOR = Color.RED;
	
	/**
	 * defines the number of elements that are depicted in a chart
	 */
	public static final int ELEM_NUMBER = 90;
	
	
	
	/**
	 * defines the space between the canvas and the panel-margin (south)
	 */
	public static final int SPACE_SOUTH = 30;
	
	/**
	 * defines the space between the canvas and the panel-margin (north)
	 */
	public static final int SPACE_NORTH = 30;  
	
	/**
	 * defines the space between the canvas and the panel-margin (east)
	 */
	public static final int SPACE_EAST = 54;  
	
	/**
	 * defines the space between the canvas and the panel-margin (west)
	 */
	public static final int SPACE_WEST = 5;
	
	
	/**
	 * defines the number of dates that are depicted concerning the time-line
	 */
	public static final int NUMBER_OF_DATES_SMALL = 4;
	
	/**
	 * defines the number of dates that are depicted concerning the time-line
	 */
	public static final int NUMBER_OF_DATES_MIDDLE = 7;
	
	/**
	 * defines the number of dates that are depicted concerning the time-line
	 */
	public static final int NUMBER_OF_DATES_LARGE = 10;
	
	private static int ID = 0;
	
	
	private StockerMainFrame frame;
	private CandleChartPanel cPanel;
	private LineChartPanel lPanel;
	private Statusbar statusbar;
	private String symbol;
	private String resolution;
	private String type;
	private int id;
	private JMenuItem windowMenuI;
	private JMenuItem candle;
	private JMenuItem line;
	private JMenuItem min1;
	private JMenuItem min5;
	private JMenuItem min10;
	private JMenuItem min15;
	private JMenuItem min30;
	private JMenuItem h1;
	private JMenuItem h4;
	private JMenuItem day;
	private JMenuItem week;
	private JMenuItem month;
	private JMenuItem alarm;
	private JMenuItem indicators;
	
	
	public Chart(StockerMainFrame frame, LineChartPanel lPanel, CandleChartPanel cPanel,
			String symbol, String resolution, JMenuItem windowMenuI) {
		
		super(symbol,true,true,true);
		ID = ID + 1;
		id = ID;
		
		this.symbol = symbol;
		this.resolution = resolution;
		this.frame = frame;
		this.lPanel = lPanel;
		this.cPanel = cPanel;
	    this.windowMenuI = windowMenuI;
		
	
		//creates all the JMenus
		JMenuBar menubar = new JMenuBar();
		JMenu type = new JMenu("Charttyp");
		JMenu intervall = new JMenu("Intervall");
		JMenu func = new JMenu("Funktionen");
		
		
		//creates all the JMenuItems
		candle = new JMenuItem("Kerzendiagramm");
		line = new JMenuItem("Liniengraph");
		min1 = new JMenuItem("1 Minute");
		min5 = new JMenuItem("5 Minuten");
		min10 = new JMenuItem("10 Minuten");
		min15 = new JMenuItem("15 Minuten");
		min30 = new JMenuItem("30 Minuten");
		h1 = new JMenuItem("Stunde");
		h4 = new JMenuItem("4 Stunden");
		day = new JMenuItem("Tag");
		week = new JMenuItem("Woche");
		month = new JMenuItem("Monat");
		alarm = new JMenuItem("Alarm");
		indicators = new JMenuItem("Indikatoren");
		
		//sets the different ToolTips
		type.setToolTipText("Charttyp ändern");
		intervall.setToolTipText("Zeitintervall ändern");		
		func.setToolTipText("Funktion auswählen");
		indicators.setToolTipText("Indikator hinzufügen/löschen");
	    alarm.setToolTipText("Alarme hinzufügen/löschen");
	    
	    
	    //adds the JMenuItems to the corresponding components
	    type.add(candle);
	    type.add(line);
	    intervall.add(min1);
	    intervall.add(min5);
	    intervall.add(min10);
	    intervall.add(min15);
	    intervall.add(min30);
	    intervall.add(h1);
	    intervall.add(h4);
	    intervall.add(day);
	    intervall.add(week);
	    intervall.add(month);
	    func.add(alarm);
	    func.add(indicators);
	    menubar.add(type);
	    menubar.add(intervall);
	    menubar.add(func);
	    setJMenuBar(menubar);
	    //adds the chart-object to the MainFrame
	    this.frame.addJInternalFrame(this);
	    
	  //disables the current resolution-menuItem
	    setDisabled(resolution);
	    
	    //gets and sets the correct size regarding the frame
	    PropertiesCache prop = PropertiesCache.getInstance();
	    String height = prop.getProperty("height");
	    String width = prop.getProperty("width");
	    int heightInt ;
	    int widthInt ;
	    if(height == null) {
	    	heightInt = Chart.MIN_HEIGHT;
	    }else {
	    	heightInt = Integer.parseInt(height);
	    }
	    if(width == null) {
	    	widthInt = Chart.MIN_WIDTH;
	    }else {
	    	widthInt = Integer.parseInt(width);
	    }
	    setMinimumSize(new Dimension(widthInt, heightInt));
	    setSize(widthInt ,heightInt);	
	     
	}
	
	
	
	/**
	 * adds the {@link #CandleChartPanel} to this  <code>JInternalFrame</code>.
	 */
	public void addCandlePanel() {
		add(cPanel);
		this.type = "c";
		setVisible(true);
		
	}
	
	
	/**
	 * adds the {@link #LineChartPanel} to this  <code>JInternalFrame</code>.
	 */
	public void addLinePanel() {
		add(lPanel);	
		this.type = "l";
		setVisible(true);
	}

	@Override
	public void newPriceAvailable(double price) {
		statusbar.setLastPrice(price);
		if(statusbar.getCursorIn()) {
			statusbar.updateStatusCursorIn();  
		}else {
			statusbar.updateStatusCursorOut();
		}
	}
	
	@Override
	public void newCandleAvailable() {
		repaintPanel();
	}
	
	
	/**
	 * gets the resolution that this chart depicts.
	 * 
	 * @return the resolution that this chart depicts.
	 */
	public String getResolution() {
		return resolution;
	}
	
	
	
	/**
	 * gets the chart ID that belongs to this chart.
	 * 
	 * @return the chart ID that belongs to this chart.
	 */
	public int getID() {
		return id;
	}
	
	
	/**
	 * gets the "Fenster"-MenuItem that belongs to this chart.
	 * 
	 * @return the the "Fenster" that belongs to this chart.
	 */
	public JMenuItem getWindowMenuItem() {
		return windowMenuI;
	}
	
	
	
	/**
	 * sets the "Fenster"-MenuItem.
	 * 
	 * @param the "Fenster"-MenuItem that belongs to this chart.
	 */
	public void setWindowMenuItem(JMenuItem m) {
		windowMenuI = m;
	}
	
	/**
	 * removes the {@link #LineChartPanel} and adds the {@link #CandleChartPanel} to this  <code>JInternalFrame</code>.
	 * This method is called up if the user changes the view from line-chart to candle-chart.
	 */
	public void lineToCandle() {
		setVisible(false);
		remove(lPanel);
		add(cPanel);
		setVisible(true);
		type = "c";
	}
	
	
	
	/**
	 * removes the {@link #CandleChartPanel} and adds the {@link #LineChartPanel} to this  <code>JInternalFrame</code>.
	 * This method is called up if the user changes the view from candle-chart to line-chart.
	 */
	public void candleToLine() {
		setVisible(false);
		remove(cPanel);
		add(lPanel);
		setVisible(true);
		type = "l";
	}
	
	/**
	 * enables and disables a JMenuItem regarding the resolution.
	 * It is used to disable the JMenuItem concerning the resolution that this chart depicts currently and
	 * to enable the JMenuItem if the resolution change to another one.
	 * It also sets the new resolution to <code>reso</code>.
	 * It is called up if the user changes the resolution.
	 * 
	 * @param reso the resolution that should be enabled
	 * @param b defines enable (true) or disable(false)
	 */
	public void setDisabled(String reso) {
		
		switch(reso) {
		case "1": min1.setEnabled(false);
				break;
		case "5": min5.setEnabled(false);
				break;
		case "10": min10.setEnabled(false);
				break;
		case "15": min15.setEnabled(false);
		 		break;
		case "30": min30.setEnabled(false);
				break;
		case "60": h1.setEnabled(false);
				break;
		case "240":h4.setEnabled(false);
				break;
		case "D": day.setEnabled(false);
				break;
		case "W": week.setEnabled(false);
				break;
		case "M": month.setEnabled(false);
				break;
		}
		
		if(reso != resolution) {
			switch(resolution) {
			case "1": min1.setEnabled(true);
					break;
			case "5": min5.setEnabled(true);
					break;
			case "10": min10.setEnabled(true);
					break;
			case "15": min15.setEnabled(true);
			 		break;
			case "30": min30.setEnabled(true);
					break;
			case "60": h1.setEnabled(true);
					break;
			case "240":h4.setEnabled(true);
					break;
			case "D": day.setEnabled(true);
					break;
			case "W": week.setEnabled(true);
					break;
			case "M": month.setEnabled(true);
					break;
			}
			resolution = reso;
			setResolution(reso);
		}
		
	}
	
	
	
	/**
	 * adds a listener to the <code>candle</code>-JMenuItem.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setMenuCandleChartListener(ActionListener listener){
		candle.addActionListener(listener);
	}
	
	
	
	/**
	 * adds a listener to the <code>line</code>-JMenuItem.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setMenuLineChartListener(ActionListener listener){
		line.addActionListener(listener);
	}
	
	
	
	/**
	 * adds a listener to the <code>min1</code>-JMenuItem.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setMenuMin1Listener(ActionListener listener){
		min1.addActionListener(listener);
	}
	
	
	

	/**
	 * adds a listener to the <code>min5</code>-JMenuItem.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setMenuMin5Listener(ActionListener listener){
		min5.addActionListener(listener);
	}
	
	
	

	/**
	 * adds a listener to the <code>min10</code>-JMenuItem.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setMenuMin10Listener(ActionListener listener){
		min10.addActionListener(listener);
	}
	
	
	
	

	/**
	 * adds a listener to the <code>min15</code>-JMenuItem.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setMenuMin15Listener(ActionListener listener){
		min15.addActionListener(listener);
	}
	
	
	
	

	/**
	 * adds a listener to the <code>min30</code>-JMenuItem.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setMenuMin30Listener(ActionListener listener){
		min30.addActionListener(listener);
	}
	
	
	
	

	/**
	 * adds a listener to the <code>h1</code>-JMenuItem.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setMenuH1Listener(ActionListener listener){
		h1.addActionListener(listener);
	}
	
	
	
	

	/**
	 * adds a listener to the <code>h4</code>-JMenuItem.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setMenuH4Listener(ActionListener listener){
		h4.addActionListener(listener);
	}
	
	
	

	/**
	 * adds a listener to the <code>day</code>-JMenuItem.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setMenuDayListener(ActionListener listener){
		day.addActionListener(listener);
	}
	
	
	
	

	/**
	 * adds a listener to the <code>week</code>-JMenuItem.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setMenuWeekListener(ActionListener listener){
		week.addActionListener(listener);
	}
	
	
	
	

	/**
	 * adds a listener to the <code>month</code>-JMenuItem.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setMenuMonthListener(ActionListener listener){
		month.addActionListener(listener);
	}
	
	
	

	/**
	 * adds a listener to the <code>alarm</code>-JMenuItem.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setMenuAlarmListener(ActionListener listener){
		alarm.addActionListener(listener);
	}
	
	
	
	

	/**
	 * adds a listener to the <code>indicators</code>-JMenuItem.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setMenuIndiListener(ActionListener listener){
		indicators.addActionListener(listener);
	}
	
	
	
	/**
	 * gets the symbol of the stock that is depicted by this chart.
	 * 
	 * @return the symbol of the stock that is depicted by this chart.
	 */
	public String getSymbol() {
		return symbol;
	}
	
	
	
	
	/**
	 * gets the current type (line/candle) that this chart depicts.
	 * 
	 * @return the current type (line/candle) that this chart depicts.
	 */
	public String getType() {
		return type;
	}
	
	
	
	/**
	 * adds the statusbar to this <code>JInternalFrame</code>.
	 * 
	 * @param sBar
	 */
	public void addStatusbar(Statusbar sBar) {  
		statusbar = sBar;
		add(sBar, BorderLayout.SOUTH);
	}

	
	/**
	 * this method causes that the scale is calculated for the {@link #LineChartPanel} and  
	 * the {@link #CandleChartPanel}.
	 */
	public void calcScale() {
		cPanel.calcScale();
		lPanel.calcScale();
	}
	
	
	/**
	 * this method causes that the {@link #LineChartPanel} or the {@link #CandleChartPanel} is repainted.
	 */
	public void repaintPanel() {
		if(type.equals("c")) {
			cPanel.repaint();
		}else {
			lPanel.repaint();
		}
	}
	
	/**
	 * updates the resolution in the <code>CandleChartPanel</code> and the <code>LineChartPanel</code>.
	 * It is called up if the resolution changes.
	 * 
	 * @param resolution the new resolution
	 */
	public void setResolution(String resolution) {
		this.resolution = resolution;
		cPanel.setResolution(resolution);
		lPanel.setResolution(resolution);
		
	}
	
	
	/**
	 * returns the chart-type as String-representation.
	 * 
	 * @return the chart-type as String-representation.
	 */
	public String getTypeAsStringRep() {
		if(type.equals("c")) {
			return "Candle-Chart";
		}else {
			return "Line-Chart";
		}
	}
	
	
	/**
	 * returns the resolution as String-representation.
	 * 
	 * @return the resolution as String-representation.
	 */
	public String getResoAsStringRep() {
		
		switch(resolution) {
		case "1": return "1-Min";
			
		case "5": return "5-Min";
				
		case "10": return "10-Min";
				
		case "15": return "15-Min";
		 		
		case "30": return "30-Min";
				
		case "60": return "60-Min";
				
		case "240":return "240-Min";
				
		case "D": return "Tag";
				
		case "W": return "Woche";
				
		case "M": return "Monat";
				
		}
		return"";
	}
	

}