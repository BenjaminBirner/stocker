package Model.chart;

import java.awt.Point;
import java.util.LinkedList;

import Model.indicatorModel.PersistenceIndiParaData;



/**
 * This class contains all the data for an individual chart that is required to restore
 * the chart according to its last state when the program starts.
 * This chart is defined by the <code>chartID</code>.
 * 
 * @author Benjamin Birner
 *
 */
public class ChartPersistence{
	private final int chartID;
	//defines if the chart depicts a line chart or a candle chart 
	private final String type;
	private final String symbol;
	private final String resolution;
	private final int height;
	private final int width;
	private final Integer zOrder;
	private final int locX;
	private final int locY;
	//contains all data concerning the indicators to this chartID
	private LinkedList<PersistenceIndiParaData> indicators;
	
	
	public ChartPersistence(int chartID, String type, String symbol, String resolution, int height, int width, int locX, int locY, int zOrder) {
		this.chartID = chartID;
		this.type = type;
		this.symbol = symbol;
		this.resolution = resolution;
		this.height = height;
		this.width = width;
		this.zOrder = zOrder;
		this.locX = locX;
		this.locY = locY;
	}
	
	
	
	/**
	 * adds the <code>pIndi</code>-object to the <code>indicators</code>-list.
	 * 
	 * @param pIndi the object that should be added to the <code>indicators</code>-list.
	 */
	public void addPersistenceIndi(PersistenceIndiParaData pIndi) {
		if(indicators == null) {
			indicators = new LinkedList<PersistenceIndiParaData>();
		}
		indicators.addLast(pIndi);
		
	}
	
	
	
	/**
	 * gets the <code>indicators</code>-list
	 * 
	 * @return the <code>indicators</code>-list
	 */
	public LinkedList<PersistenceIndiParaData> getIndicatorList(){
		return indicators;
	}
	
	
	
	/**
	 * gets the <code>type</code> that defines if the chart depicts a line chart or a candle chart 
	 * 
	 * @return "l" for line chart and "c" for candle chart.
	 */
	public String getType() {
		return type;
	}
	
	
	
	/**
	 * gets the <code>symbol</code> that defines the stock that the chart depicts.
 	 * 
	 * @return the <code>symbol</code> that defines the stock that the chart depicts.
	 */
	public String getSymbol() {
		return symbol;
	}
	
	
	
	/**
	 * gets the <code>resolution</code> concerning the stock that the chart depicts.
	 * 
	 * @return the <code>resolution</code> concerning the stock that the chart depicts.
	 */
	public String getResolution() {
		return resolution;
	}
	
	
	
	/**
	 * gets the <code>height</code> of the chart-frame.
	 * 
	 * @return the <code>height</code> of the chart-frame.
	 */
	public int getHeight() {
		return height;
	}
	
	
	
	/**
	 * gets the <code>width</code> of the chart-frame.
	 * 
	 * @return the <code>width</code> of the chart-frame.
	 */
	public int getWidth() {
		return width;
	}
	
	
	
	/**
	 * gets the <code>zOrder</code> of the chart-frame.
	 * 
	 * @return the <code>zOrder</code> of the chart-frame.
	 */
	public int getZOrder() {  
		return zOrder;
	}
	
	
	
	/**
	 * gets the X-Location of the chart-frame.
	 * 
	 * @return the X-Location of the chart-frame.
	 */
	public int getLocationX() {          
		return locX;
	}
	
	
	
	/**
	 * gets the Y-Location of the chart-frame.
	 * 
	 * @return the Y-Location of the chart-frame.
	 */
	public int getLocationY() {
		return locY;
	}
	

	
	
	/**
	 * gets the <code>chartID</code> that defines the chart to which the data of the
	 * <code>this</code>-object belongs to.
	 * 
	 * @return
	 */
	public int getID() {
	
		return chartID;
	}
	
	

	/**
	 * sets the <code>indicators</code>-List.
	 * 
	 * @param list the list that contains all {@link #PersistenceIndiParaData}-objects.
	 */
	public void  setPersistenceIndicator(LinkedList<PersistenceIndiParaData> list){
		indicators = list;
	}

	
}
