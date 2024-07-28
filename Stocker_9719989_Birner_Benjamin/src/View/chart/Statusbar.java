package View.chart;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;


import Model.chart.ChartDataModel;
import Model.chart.StockerCalculator;



/**
 * This class models the status-bar for a chart.
 * It displays the symbol of a stock, the latest price and the 
 * cursor-position (date/value) if the cursor is within the canvas.
 * 
 * @author Benjamin Birner
 *
 */
public class Statusbar extends JLabel {
	
	
	
	/*
	 * all these attributes are required to construct the String for the status-bar.
	 */
	private final String s1 = "Symbol: ";
	private final String s2 = "      ";
	private final String s3 = "Letzter Kurs: ";
	private final String[] spaces = {"            ","          ","        ","      ","    ","  "};
	private final String s5 = "Cursor: ";
	private final String s6 = "  |  ";
	private final String cursorOut = "                                                ";
	private String symbol;
	private String lastPrice = "     ";
	private String cursorPrice = "     ";
	private String date = "                 ";
	
	//is set by the inner class CursorInOutListener and its MouseEntered/MouseExited-method.
	//it is set to true if the cursor enters the Panel. Else false.
	private boolean cursorIn = false;
	
	private Chart chart;
	private CandleChartPanel cPanel;
	private LineChartPanel lPanel;
	private ChartDataModel chartDataModel;
	
	public Statusbar(String symbol, Chart chart, CandleChartPanel cPanel, LineChartPanel lPanel, ChartDataModel chartDataModel, double lastPrice) {
		this.chart = chart;
		this.cPanel = cPanel;
		this.lPanel = lPanel;
		this.symbol = symbol;
		this.chartDataModel = chartDataModel;
		setLastPrice(lastPrice);
		//some settings
		setHorizontalAlignment(RIGHT);
		setBackground(Color.DARK_GRAY);
		setForeground(Color.DARK_GRAY);
		//builds up the status-String (cursorOut)
		setText(s1 + symbol + s2 + s3 + this.lastPrice + spaces[this.lastPrice.length()-3]  + s5 + cursorOut); 
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
    }

	
	/**
	 * rounds the price that is referenced by the parameter <code>price</code>,
	 * builds up an equal String-representation and sets it.
	 * 
	 * @param price the price that should be set.
	 */
	public void setLastPrice(double price) {
		double rPrice = StockerCalculator.roundPrice(price);
		lastPrice = Double.toString(rPrice);
	}
		
	
	
	/**
	 * updates and builds up the status-String in the case that the cursor is without the canvas.
	 * So, it solely displays the symbol an the latest price.
	 * It is called up by the class {@link #chart Chart} if a new price is available and by the
	 * {@link #updateCursorStatus()} method.
	 */
	protected void updateStatusCursorOut(){
		setText(s1 + symbol + s2 + s3 + lastPrice + spaces[lastPrice.length()-3] + s5 + cursorOut); 
		repaint();
	}
	
	
	/**
	 * updates and builds up the status-String in the case that the cursor is within the canvas.
	 * So, it displays the symbol, the latest price, the date and price concerning the cursor-position.
	 * It is called up by the class {@link #chart Chart} if a new price is available and by the
	 * {@link #updateCursorStatus()} method.
	 */
	protected void updateStatusCursorIn(){  
		setText(s1 + symbol + s2 + s3 + lastPrice + spaces[lastPrice.length()-3] + s5 + date + s6 + cursorPrice + spaces[cursorPrice.length()-3]);
		repaint();
	}
	
	
	/**
	 * This method updates the cursor-status concerning the status-bar.
	 * It builds up the suitable String to display it in the bar.
	 * It is called up by the actionPerformed() method of the inner class TimerTask 
	 * in the {@link #ChartController} if the cursor is within this Panel.
	 */
	public void updateCursorStatus() {
		Point loc;
		int height;
		int width;
		double range;
		double scale0;
		//checks the current chart-Type to get the corresponding values to initialize the variables
		if (chart.getType().equals("c")){
			//returns the cursor-location within the canvas
			loc = cPanel.getCursorLocation();
			height = cPanel.getHeight();
			width = cPanel.getWidth();
			//the range of the scale
			range = cPanel.getRange();
			//the first/lowest scale value
			scale0 = cPanel.getScale0();
		}else {
			//returns the cursor-location within the canvas
			loc = lPanel.getCursorLocation();
			height = lPanel.getHeight();
			//the range of the scale
			range = lPanel.getRange();
			width = lPanel.getWidth();
			//the first/lowest scale value
			scale0 = lPanel.getScale0();
		}
		int y = loc.y;
		int x = loc.x;  
		//checks the cursor-position. True means that the cursor is within the Panel but outside the canvas.
		// true --> updateStatusCursorOut()
		if( (y < 0 || y > (height - Chart.SPACE_SOUTH - Chart.SPACE_NORTH) || x < 0 || x > width - Chart.SPACE_EAST-Chart.SPACE_WEST) || 
				(y < 0 || y > (height - Chart.SPACE_SOUTH - Chart.SPACE_NORTH) || x < 0 || x > width - Chart.SPACE_EAST-Chart.SPACE_WEST-20) && chart.getType().equals("c")  ) { 
			updateStatusCursorOut();
			//in this case the cursor is within the canvas
		}else {
			//insures that there is a candle available concerning this x-position regarding the cursor
			if( x < width-Chart.SPACE_EAST-20 ){ 
				//calculates the number of pixel in relation to the value of the scale
				double pix = (height - Chart.SPACE_SOUTH - Chart.SPACE_NORTH)/ range;   
				//translates the cursor-position to the corresponding value
				double price =  StockerCalculator.roundPrice((y / pix) + scale0);
				////sets the calculated price
				cursorPrice =  Double.toString(price);
				
				//calculates the width of the element-area (element = candle/line-point)
				width = width - Chart.SPACE_WEST -16- Chart.SPACE_EAST; 
				String s = chart.getType();
				double step;
				//initializes the variable steps with the area of a single element in dependency of the chart-type
				if(s.equals("c")) {
					step = (double)  width /  Chart.ELEM_NUMBER; 
				}else {
					step = (double)  width /  (Chart.ELEM_NUMBER-1); 
				}
			            
				/*
				 * gets the correct timestamp to the cursor-position from the model and translates 
				 * the timestamp to a date-representation and updates the status
				 */
				
				int candle = (int) (x / step);
				int index = chartDataModel.getIndex(symbol);  
				long time = chartDataModel.getTimeAt(index,candle, chart.getResolution());
				time = TimeUnit.SECONDS.toMillis(time);
				Date date = new Date(time);
				DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
				this.date = df.format(date);
				updateStatusCursorIn();
			}
		}
		repaint(); // do we need repaint here?

		
		
	}
	
	
	/**
	 * gets the information if the cursor is within the canvas
	 * 
	 * @return true if the cursor is within the canvas. Else false
	 */
	public boolean getCursorIn() {
		return cursorIn;
	}

	
	
	/**
	 * sets the information if the cursor is within the canvas
	 * 
	 * @param b true if the cursor is within the canvas. Else false
	 */
	public void setCursorIn(boolean b) {
		cursorIn = b;	
	}
	
	

}