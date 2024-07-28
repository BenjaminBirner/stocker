package View.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

import Model.alarmModel.AlarmDataModel;

import Model.chart.ChartDataModel;
import Model.indicatorModel.IndicatorModel;
import Model.indicatorModel.IndicatorPaint;
import Model.persistence.PropertiesCache;
import Model.chart.StockerCalculator;


/**
 * This class paints all the candles that should be depicted in a suitable coordinate system.
 * So, its paintComponent()-method is the main part of this class.
 * An Instance of this class can be added to a {@link #Chart}-object.
 * 
 * @author Benjamin Birner
 *
 */
public class CandleChartPanel extends JPanel {
	
	//each candle has a defined area at the canvas at its disposal. Within this area the candle must be drawn
	//and little space between the candles is required. To each area belongs such a space. When the area is 
	//multiplied with CANDLE_SPACE_FAC, the result is the width of the candle --> area = 85% + 15% space
	private static final double CANDLE_SPACE_FAC = 0.85;
	
	private String symbol;
	private String resolution;
	private int chartID;
	private ChartDataModel chartDataModel;
	private AlarmDataModel alarmModel;
	private IndicatorModel indiModel;
	private Double[] scale = {0.0};
	private boolean cursorIn;
	
	public CandleChartPanel(ChartDataModel chartDataModel, AlarmDataModel alarmModel, IndicatorModel indiModel, String symbol, String resolution) {
		this.chartDataModel = chartDataModel;
		this.alarmModel = alarmModel;
		this.indiModel = indiModel;
		this.symbol = symbol;
		this.resolution = resolution;
		cursorIn = false;
		setBackground(new Color(153,153,153));
		setForeground(Color.BLACK);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);                  
		Graphics2D g2 = (Graphics2D) g;
		
		
		/*
		 * The following section does some preparatory calculations and settings
		 */
		

	    //the coordinate origin is placed from the left top corner to 
		//the left bottom corner
		g2.translate(0, getHeight());
		//determines and sets font size
		int fontSize = (getWidth() < 500 && getHeight() < 300 ? 10 : 14);
		g2.setFont(new Font(Font.SERIF,Font.PLAIN,fontSize));    
		
		//gets the minimum low and the maximum high of all candles apart 
		//from that on that is in progress
		double min = chartDataModel.getMinLow(symbol, resolution);
		double max = chartDataModel.getMaxHigh(symbol, resolution);
		//checks if the min low or the max high is out of range regarding the scale. 
		//If so, the scale must be calculated with the new values.
		if( min < scale[0] || max > scale[scale.length-1]) {
			scale = StockerCalculator.scaleChart(min,max);
		}
		
		
		//define the width of the canvas
		int canvasW = getWidth() - Chart.SPACE_EAST -16 - Chart.SPACE_WEST; 
		//define the height of the canvas
		int canvasH = getHeight() - Chart.SPACE_SOUTH - Chart.SPACE_NORTH; 
		//the canvas-width is divided into ELEM_NUMBER parts. stepW saves the size of such a part in pixel. 
		//That defines the area for a candle --> part = candle-width + space between candles
		double stepW =(double) canvasW / Chart.ELEM_NUMBER; 
		//calculates the candle-width without the space. 
		//The value is saved in candleW
		int candleW = (int) (stepW * CANDLE_SPACE_FAC);  
		//insures that the width of each candle is odd so that the wick can be placed in the middle
		if(candleW % 2 == 0) {
			candleW = candleW - 1;
		}
		//the first scale-value just over the x-axis
		double scale0 =  scale[0];
		//calculates the value-range between the first scale-value and the last scale-value
		double stepH = scale[scale.length - 1] - scale0;
		//calculates the value that one pixel within the canvas concerning the y-axis represents.
		//The result is saved in stepH
		stepH = 1 / (canvasH / stepH);                     
		
		
		/*
		 * The following section draws the scale and the time-line
		 */
		
	
		//The variable steps saves the number of pixel that must be set between the different scale-values.
		//That means that e.g. the first scale-value is drawn at position x and the second one at position x + step and so on.
		double steps = (double)(getHeight() - Chart.SPACE_SOUTH - Chart.SPACE_NORTH) /  (scale.length - 1);
		//x defines the value at the x-axis at that the scale-value should be drawn.
		int x = getWidth() - Chart.SPACE_EAST;
		int y;
		//this loop draws all the scale-values at the correct position with the help of the calculated values x,y and step.
		for(int i = 0; i < scale.length; i++) {
			y = (int) Math.round(i * steps);
			String s = scale[i].toString();
			if(scale[i] >= 10000) {
				if(scale[i] >= 100000) {
					s = scale[i].toString();
					s = s.substring(0,6);
				}else {
					s = scale[i].toString();
					s = s.substring(0,5);	
				}
			}
			g2.drawString(s, x, -Chart.SPACE_SOUTH - y + 5);
		}
		
		
		
		int dateNumb;
		int k;
		//this block determines the number of dates that should be depicted. It depends on
		//the width of the Panel. 
		//The variable k points to the candle of which the time-stamp is required.
		if(getWidth() < 500) {
			dateNumb = Chart.NUMBER_OF_DATES_SMALL;
			k = 3;
		}else {
			if(getWidth() > 1000) {
				dateNumb = Chart.NUMBER_OF_DATES_LARGE;
				k = 1;
			}else {
				dateNumb = Chart.NUMBER_OF_DATES_MIDDLE;
				k = 2;
			}
		}
		//defines the number of candles between each drawn date
		int dist = Chart.ELEM_NUMBER / dateNumb;
		//The timeScale-Array saves at each position the number of pixel that will be
		//used in order to start drawing the corresponding date.
		int[] timeScale = new int[dateNumb];
		//this loop calculates the number of pixel for the remaining positions in the timeScale-Array
		for(int i = 0; i < dateNumb; i++) {
			double xD = Chart.SPACE_WEST + (i * dist) * stepW;
			timeScale[i] = (int) Math.round(xD);
		}
		
		
		
		
		long time;
		Date date;
		DateFormat df;
		String s;
		//gets all time-stamps to the stock that is depicted
		long[] timestamps = chartDataModel.getAllTimestamps(symbol, resolution);
		//This if-instruction checks the resolution because for the resolutions month, week and day the 
		//date is depicted whereas for the intra-day-resolutions the time is depicted
		if(resolution.equals("M") || resolution.equals("D") || resolution.equals("W")) {
			//This loop gets all the required time-stamps according to the variable k.
			//Then, the time-stamp is translated into the date and the this date is drawn.
			for (int i = 0 ; i < dateNumb; i++) {
				time = TimeUnit.SECONDS.toMillis(timestamps[k]);
				date = new Date(time);
			    df = DateFormat.getDateInstance(DateFormat.SHORT);
			    s = df.format(date);
			    s= s.substring(0,5);
			    g2.drawString(s, timeScale[i], -10);
			    k = k + dist;
			}
		}else {
			//this loop does the same as the loop above but it translates to time
			for (int i = 0; i < dateNumb; i++) {
				time = TimeUnit.SECONDS.toMillis(timestamps[k]);
				date = new Date(time);
			    df = DateFormat.getTimeInstance(DateFormat.SHORT);
			    g2.drawString(df.format(date), timeScale[i], -10);	
			    k = k + dist;
			}
		}
		
		
		/*
		 * This block draws the resolution, the current high an low
		 */
		
		
		//sets the font
		g2.setFont(new Font(Font.SERIF, Font.BOLD,fontSize+1));
		//calculate the position for the resolution
		int resoY = getHeight() - fontSize-2;
		int resoX = (getWidth() - Chart.SPACE_WEST - Chart.SPACE_EAST) / 2 -40;
		//draws the resolution at the calculated position
		g2.drawString("Resolution: " + resolution, resoX, -resoY);
		
		//sets the font for the current high and low
		g2.setFont(new Font(Font.SERIF, Font.PLAIN,fontSize));
		//sets the color for the current high
		g2.setColor(Color.GREEN);
		//gets the current high from the model
		Double currentH = chartDataModel.getCurrentHigh(symbol,resolution);
		currentH = StockerCalculator.roundPrice(currentH);
		//draws the current high
		g2.drawString(currentH.toString() , getWidth() - Chart.SPACE_EAST, 
				-(getHeight() - Chart.SPACE_NORTH + 10));
		//sets the color for the current low
		g2.setColor(Color.RED);
		//gets the current low from the model
		Double currentL = chartDataModel.getCurrentLow(symbol,resolution);
		currentL = StockerCalculator.roundPrice(currentL);
		//draws the current low
		g2.drawString(currentL.toString(), getWidth()-Chart.SPACE_EAST, -10);
		
		
		
		/*
		 * The following section draws the candles
		 */
		
		
		
		//creates and sets the stroke
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  
		BasicStroke stroke = new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
		g2.setStroke(stroke);
		
	
		double ydoub;
		double xdoub;
		double candleH;
		double wickH;
		double wickL;
		int candleHR;
		int y2;
		int yTop;
		int yBottom;
		//gets all the required data/prices to the stock that is depicted
		double[] close = chartDataModel.getAllClose(symbol, resolution);
		double[] open = chartDataModel.getAllOpen(symbol, resolution);
		double[] high = chartDataModel.getAllHigh(symbol,resolution);
		double [] low = chartDataModel.getAllLow(symbol,resolution);
		//in this loop all the calculations are done in order to draw the
		//candles and it draws the candles
		for(int i = 0; i < Chart.ELEM_NUMBER; i++) {
			//xdoub/x defines the beginning of the area concerning the x-axis for the i-the candle
			xdoub = Chart.SPACE_WEST + stepW * i;
			x = (int) Math.round(xdoub);
			//This if-instruction checks if the open-price is lower then the close-price or if the
			//close-price is lower than the open-price concerning this candle.
			//Depending on the result the corresponding calculations follow.
			if(open[i] <= close[i]) {
				//In this block the open-price is smaller or equal than the close-price
				//It draws the rectangle of the i-th candle.
				
				//ydoub/yBottom defines the number of pixel from the bottom of the Panel to the lower-edge of the i-th candle.
				ydoub = (open[i] - scale0) / stepH + Chart.SPACE_SOUTH;  
				yBottom = (int) Math.round(ydoub);
				//candleH/candleHR defines the height of the i-th candle in pixel
				candleH = (close[i] - open[i]) / stepH; 
				candleHR = (int) Math.round(candleH);
				//calculates the height of the lower and higher wick in Pixel
				wickH = (high[i] - close[i]) / stepH;
				wickL = (open[i] - low[i]) / stepH;
				//sets the color to green
				g2.setColor(new Color(0,225,25));
			}else {
				//In this block the close-price is smaller  than the open-price
				//It draws the rectangle of the i-th candle.
				
				//ydoub/yBottom defines the number of pixel from the bottom of the Panel to the lower-edge of the i-th candle.
				ydoub = (close[i] - scale0) / stepH + Chart.SPACE_SOUTH;
				yBottom = (int) Math.round(ydoub);  
				//candleH/candleHR defines the height of the i-th candle in pixel
				candleH = (open[i] - close[i]) / stepH;  
				candleHR = (int) Math.round(candleH);
				//calculates the height of the lower and higher wick in Pixel
				wickH = (high[i] - open[i]) / stepH;
				wickL = (close[i] -  low[i]) / stepH;
				//sets the color to red
				g2.setPaint(new Color(225,0,25));		
			}
			//draws the rectangle with the calculated values
			g2.fillRect(x, -yBottom-candleHR, candleW, candleHR);  
			//sets the color to black and draws the "frame" around the rectangle
			g2.setColor(Color.BLACK);
			g2.drawRect(x-1, -yBottom-candleHR-1, candleW+1, candleHR+1);
			
			//yTop defines the number of pixel from the bottom of the Panel to the top edge of the i-th candle
			yTop = yBottom + candleHR;                                              
			//calculates the pixel-value of the peak of the higher wick
			y2 = (int) Math.round(wickH) + yTop; 
			//calculates the x-position for the wick
			x = (int) (Chart.SPACE_WEST + stepW * i + candleW/2 + 1); 
			//draws the higher wick
			g2.drawLine(x, -yTop-1, x, -y2-1);  
			
			y = (int) (Math.round(wickL));
			//calculates the pixel-value of the down of the lower wick
			y = yBottom - y ;
			//draws the wick
			g2.drawLine(x, -y, x, -yBottom);
		
		}
		
		

		/*
		 * The following section draws the cross hairs
		 */
		
		
		//true means that the cursor is within the Panel
		if(cursorIn == true) {
			//gets the cursor location within the canvas
			Point loc = getCursorLocation();
			//xc defines the number of pixel from the left-Panel-edge to the cursor position
			int xc = loc.x + Chart.SPACE_WEST;
			//yc defines the number of pixel from the bottom of the Panel to the cursor position
			int yc = loc.y + Chart.SPACE_SOUTH;
			//checks if the cursor is within the canvas
			if( !(yc < Chart.SPACE_SOUTH ) && !(yc-Chart.SPACE_SOUTH > (getHeight()-Chart.SPACE_NORTH-Chart.SPACE_SOUTH))
					&& !(xc-Chart.SPACE_WEST < 0)&& !(xc > getWidth() - Chart.SPACE_EAST-5)) {
				//draws the vertical line
				g2.drawLine(xc, -Chart.SPACE_SOUTH, xc, -(getHeight()-Chart.SPACE_NORTH));
				//draws the horizontal line
				g2.drawLine(Chart.SPACE_WEST, -yc, getWidth()-Chart.SPACE_EAST-5, -yc);
			}
		}
		
		
		
		/*
		 * The following section draws the alarms
		 */
		
		
		
		//gets all alarms that belongs to this stock
		double[] alarm = alarmModel.getAllAlarmsInDouble(symbol);
		PropertiesCache prop = PropertiesCache.getInstance();
		//true means that there is a alarm for this stock
		if(alarm != null) {
			String rgb = prop.getProperty("alarm.rgb");
			Color color;
			//checks if in the Properties is a value that defines the alarm-color.
			//If not, the default-color is used
			if( rgb != null) {
				int rgbInt = Integer.parseInt(rgb);
				color = new Color(rgbInt);
			}else {
				color = Chart.ALARM_COLOR;
			}
			g2.setColor(color);
			//this loop draws all the alarms for this stock
			for( Double d : alarm) {
				//calculates the number of pixel from the bottom of the canvas to 
				//the alarm-value and draws the line for the alarm
				int a = (int) ((d - scale0) / stepH + Chart.SPACE_SOUTH); 
				g2.drawLine(Chart.SPACE_WEST, -a, getWidth() - Chart.SPACE_EAST, -a);
			}
		}
		
		
		

		/*
		 * The following section draws the indicators
		 */
		
		
		
		//determines and sets the strokeSize for the indicator-lines
		int strokeSize = (getWidth() < 600 && getHeight() < 350 ? 1 : 2);
		BasicStroke stroke2 = new BasicStroke(strokeSize);
		g2.setStroke(stroke2);
		
		int height = getHeight() - 15; 
		//gets all data from the model that is required to draw the indicators
		IndicatorPaint[] indiObjs = indiModel.getAllIndisToPaintFor(chartID);
		//This loop iterates exactly once  for each indicator-type.This means without 
		//addition of further indicators two times. Once for the GD and once for the BB.
		for(int i = 0; i < indiObjs.length; i++) {
			//true means that there is a entry for this indicator-type
			if(indiObjs[i] != null) {
				//this block gets the rgb-color-code from the Properties. If there is non,
				//the default-color is used
				String rgb = prop.getProperty(indiObjs[i].getDescription()+".rgb");
				Color color;
				if( rgb != null) {
					int rgbInt = Integer.parseInt(rgb);
					color = new Color(rgbInt);
				}else {
					color =indiModel.getDefaultColorFor(indiObjs[i].getDescription());
				}
				g2.setColor(color);
				
				//gets all data to draw the different entries to a indicator-type.
				//Each Array in this Array represents one indicator-line. 
				//It contains ELEM_NUMBER points.
				double[][] indis = indiObjs[i].getIndiPoints();
				for(int h = 0; h < indis.length; h++) {
					Path2D indiPath = new GeneralPath();
					//calculates the number of pixel from the bottom of the Panel to the first indicator-point (y-value)
					double indiY = (indis[h][0] - scale0) / stepH + Chart.SPACE_SOUTH;
					//calculates the number of pixel from the left-edge of the Panel to the first indicator-point (x-value)
					x = (int) (Chart.SPACE_WEST + candleW/2 + 1);
					//sets this point
					indiPath.moveTo(x, -indiY);
					double indiX = 0;
					//this loop draws the remaining points and completes the line.
					for(int l = 1; l < Chart.ELEM_NUMBER; l++) {
						//calculates the number of pixel from the bottom of the Panel to the l+1-th indicator-point (y-value)
						indiY = (indis[h][l] - scale0) / stepH + Chart.SPACE_SOUTH;
						//calculates the number of pixel from the left-edge of the Panel to the l+1-th indicator-point (x-value)
						indiX =  (int) (stepW * l + candleW/2 + 1);
	
						indiPath.lineTo(Chart.SPACE_WEST + indiX, -indiY);
					}
					g2.draw(indiPath);
				}
				g2.setFont(new Font(Font.SERIF, Font.PLAIN, 10));
				//gets all display-descriptions (z.B BB(2,20)) that belongs to the drawn indicators.
				//To each indicator-entry belongs one description
				String[] disp = indiObjs[i].getDisplayDescriptions();
				//this loop draws all the display descriptions at the canvas
				for(int a = 0; a < disp.length; a++) {
					g2.drawString(disp[a], Chart.SPACE_WEST, -height);
					height = height - 8;   
				}                                     
			}
		}
	}
	
	/**
	 * calculates and returns the cursor position within the canvas.
	 * 0/0 is at the left bottom corner.
	 * It is used to calculate the cursor-value in the statusbar and
	 * to draw the cross hairs
	 * 
	 * @return point-object that contains the cursor-position within the canvas
	 */
	public Point getCursorLocation() {
		//calculates the height of the canvas
		int height = getHeight() - Chart.SPACE_NORTH - Chart.SPACE_SOUTH;
		//gets the cursor-location on the screen
		Point locM = MouseInfo.getPointerInfo().getLocation();
		//gets the Panel-location on the screen
	    Point locP = getLocationOnScreen();
	    //x defines the number of pixel from the left-canvas-edge to the cursor-position
	    int x = locM.x - locP.x - Chart.SPACE_WEST;
	  //y defines the number of pixel from the top of the canvas to the cursor-position
	    int y = locM.y - locP.y - Chart.SPACE_NORTH;
	    //y defines the number of pixel from the bottom of the canvas to the cursor-position
	    y = height - y;
	 
	    return new Point(x, y);   
	}
	
	
	
	/**
	 * calculates the scale. It gets the minimum low and the maximum high of all candles
	 * and calls up with these values the scaleChart()-method of the StockerCalculator-class
	 */
	public void calcScale() {
		double min = chartDataModel.getMinLow(symbol, resolution);
		double max = chartDataModel.getMaxHigh(symbol, resolution);
		scale = StockerCalculator.scaleChart(min,max); 
	}
	
	
	/**
	 * sets the resolution and calls up the <code>calcScale</code>-method.
	 * It is called up if the resolution has just changed.
	 * 
	 * @param resolution the new value for the resolution
	 */
	public void setResolution(String resolution) {
		this.resolution = resolution;
		calcScale();
		repaint();
	}
	

	/**
	 * sets the chart-ID
	 * 
	 * @param chartID the ID that defines the {@link #Chart}-object to which 
	 *                this Panel belongs to
	 */
	public void setChartID(int chartID) {
		this.chartID = chartID;
	}
	
	
	/**
	 * adds a Mouse-listener to this Panel.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void addCursorInOutListener(MouseListener ml) {
		addMouseListener(ml);
	}
	
	
	
	/**
	 * gets the range of the y-axis-values
	 * 
	 * @return the range
	 */
	public double getRange() {
		return scale[scale.length-1] - scale[0];
	}
	
	
	/**
	 * gets the lowest scale-value
	 * 
	 * @return the lowest scale-value
	 */
	public double getScale0() {
		return scale[0];
	}

	
	
	/**
	 * sets the cursor-status to in.
	 * This means that the cursor is within the Panel
	 * 
	 * @param b true if the cursor is within the Panel. Else false.
	 */
	public void setCursorIn(boolean b) {
		cursorIn = b;
	}

}