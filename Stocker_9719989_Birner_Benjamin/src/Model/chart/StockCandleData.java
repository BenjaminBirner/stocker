package Model.chart;

import java.util.Iterator;
import java.util.LinkedList;

import Model.indicatorModel.IndicatorModel;
import View.chart.Chart;


/**
 * This class manages all aspects concerning the candles that a chart requires to paint them and to work correct.
 * It contains all candles that belong to a specified symbol and resolution.
 * It also updates the candle which is in progress and generates a new one if this candle is complete.
 * Then, it informs the StockListeners that are managed in the listenersList.
 * 
 * @author Benjamin Birner
 *
 */
class StockCandleData implements Comparable<String> {
	
	private final String symbol;
	private final String resolution;
	private long resoInMilli;
	private double lastPrice;
	private long lastTime;
	private long lastVolume;
	
	//this reference is necessary in order to call the update-method in the IndicatorModel
	private IndicatorModel indiModel;
	private final LinkedList<Candle> candleList;
	
	//the chart ID´s are necessary in order that the IndicatorModel are able to identify the charts which have to be updated
	private final LinkedList<Integer> chartIDList = new LinkedList<>();
	private final LinkedList<StockListener> listenerList = new LinkedList<>();
	
	protected StockCandleData(String symbol, String resolution, int chartID, CandleRequestData data, 
			StockListener listener, IndicatorModel indiModel) {
		this.symbol = symbol;
		this.resolution = resolution;
		chartIDList.addFirst(chartID);
		listenerList.addFirst(listener);
		this.indiModel = indiModel;
		
		//if the resolution is 10 min or 240 min, the candles must be converted
		if(resolution.equals("10")) {
			LinkedList<Candle> candleList = CandleConverter.convertCandlesToCandles(data, 600, Chart.ELEM_NUMBER + 1, symbol, resolution);
			this.candleList = candleList;
		}else {
			if(resolution.equals("240")) {
				LinkedList<Candle> candleList = CandleConverter.convertCandlesToCandles(data, 14400, Chart.ELEM_NUMBER + 1, symbol, resolution);
				this.candleList = candleList;
		    }else {
		    	this.candleList = new LinkedList<Candle>();
		    	addCandles(data);
		    }
		}
	    setResolutionInMilli(resolution); 
	}                                                                                               
	/**
	 * creates all the required candles and adds them to the list.
	 * This private method is called up in the constructor of this class.
	 *                                                                                                                 
	 * @param data contains all the required data to create the candles.
	 */
	private void addCandles(CandleRequestData data) {                                                                     
		int index = data.size()-1;
		for(int i = index; i >= index - Chart.ELEM_NUMBER; i--) {
			candleList.addFirst(new Candle(symbol, resolution, data.getCloseAt(i), data.getHighAt(i), 
					data.getLowAt(i), data.getOpenAt(i), data.getTimeAt(i), data.getVolumeAt(i)));
		}
	}
	
	
	/**
	 * gets the resolution for this stock.
	 * 
	 * @return the resolution for this stock.
	 */
	protected String getResolution() {
		return resolution;
	}
	
	

	/**
	 * gets the timestamp of the candle at position <code>index</code>.
	 * 
	 * @param index defines the position of the candle to which the required timestamp belongs to.
	 * @return the timestamp of the candle at position <code>index</code>.
	 */
	protected long getTimeAt(int index) {
		return candleList.get(index).getTimeInSec();
	}
	
	
	/**
	 * gets all timestamps of the candles in the <code>candleList</code>.
	 * 
	 * @return <code>long</code>-array with all timestamps of the candles in the <code>candleList</code>.
	 */
	protected long[] getAllTimestamps() {
		long[] time = new long[Chart.ELEM_NUMBER];
		Iterator<Candle> iter = candleList.iterator();
		for(int i = 0; i < Chart.ELEM_NUMBER; i++) {
			time[i] = iter.next().getTimeInSec();
		}
		return time;
	}
	
	
	/**
	 * gets all close prices of the candles in the <code>candleList</code>.
	 * 
	 * @return <code>double</code>-array with all close prices of the candles in the <code>candleList</code>.
	 */
	protected double[] getAllClose() {
		double[] close = new double[Chart.ELEM_NUMBER];
		Iterator<Candle> iter = candleList.iterator();
		for(int i = 0; i < Chart.ELEM_NUMBER; i++) {
			close[i] = iter.next().getClose();
		}
		return close;
	}
	
	
	
	/**
	 * gets all high prices of the candles in the <code>candleList</code>.
	 * 
	 * @return <code>double</code>-array with all high prices of the candles in the <code>candleList</code>.
	 */
	protected double[] getAllHigh() {
		double[] high = new double[Chart.ELEM_NUMBER];
		Iterator<Candle> iter = candleList.iterator();
		for(int i = 0; i < Chart.ELEM_NUMBER; i++) {
			high[i] = iter.next().getHigh();
		}
		return high;
	}
	
	
	
	
	/**
	 * gets all low prices of the candles in the <code>candleList</code>.
	 * 
	 * @return <code>double</code>-array with all low prices of the candles in the <code>candleList</code>.
	 */
	protected double[] getAllLow() {
		double[] low = new double[Chart.ELEM_NUMBER];
		Iterator<Candle> iter = candleList.iterator();
		for(int i = 0; i < Chart.ELEM_NUMBER; i++) {
			low[i] = iter.next().getLow();
		}
		return low;
	}
	
	
	
	
	/**
	 * gets all open prices of the candles in the <code>candleList</code>.
	 * 
	 * @return <code>double</code>-array with all open prices of the candles in the <code>candleList</code>.
	 */
	protected double[] getAllOpen() {
		double[] open = new double[Chart.ELEM_NUMBER];
		Iterator<Candle> iter = candleList.iterator();
		for(int i = 0; i < Chart.ELEM_NUMBER; i++) {
			open[i] = iter.next().getOpen();
		}
		return open;
	}
	
	
	/**
	 * gets the minimum close price of all the candles in the <code>candleList</code>.
	 * 
	 * @return the minimum close price of all the candles in the <code>candleList</code>.
	 */
	protected double getMinClose() {
		double min = candleList.getFirst().getClose();
		Iterator<Candle> iter = candleList.iterator();
		for(int i = 0; i < Chart.ELEM_NUMBER; i++) {
			double next = iter.next().getClose();
			if(next < min) {
				min = next;
			}
		}
		return min;
	}
	
	
	
	/**
	 * gets the maximum close price of all the candles in the <code>candleList</code>.
	 * 
	 * @return the maximum close price of all the candles in the <code>candleList</code>.
	 */
	protected double getMaxClose() {
		double max = candleList.getFirst().getClose();
		Iterator<Candle> iter = candleList.iterator();
		for(int i = 0; i < Chart.ELEM_NUMBER; i++) {
			double next = iter.next().getClose();
			if(next > max) {
				max = next;
			}
		}
		return max;
	}
	
	
	
	/**
	 * gets the minimum low price of all the candles in the <code>candleList</code>.
	 * 
	 * @return the minimum low price of all the candles in the <code>candleList</code>.
	 */
	protected double getMinLow() {
		double min = candleList.getFirst().getLow();
		Iterator<Candle> iter = candleList.iterator();
		for(int i = 0; i < Chart.ELEM_NUMBER; i++) {
			double next = iter.next().getLow();
			if(next < min) {
				min = next;
			}
		}
		return min;
	}		
	
	
	
	
	/**
	 * gets the maximum high price of all the candles in the <code>candleList</code>.
	 * 
	 * @return the maximum high price of all the candles in the <code>candleList</code>.
	 */
	protected double getMaxHigh() {
		double max = candleList.getFirst().getHigh();
		Iterator<Candle> iter = candleList.iterator();
		for(int i = 0; i < Chart.ELEM_NUMBER; i++) {
			double next = iter.next().getHigh();
			if(next > max) {
				max = next;
			}
		}
		return max;
	}
	
	
	
	/**
	 * gets the current high prices regarding the candles that is in progress.
	 * 
	 * @return the current high price
	 */
	protected double getCurrentHigh() {
		return candleList.getLast().getHigh();
	}
	
	
	/**
	 * gets the current low prices regarding the candles that is in progress.
	 * 
	 * @return the current low price
	 */
	protected double getCurrentLow() {
		return candleList.getLast().getLow();
	}
	
	
	
	
	//sets the resolution in milliseconds
	private void setResolutionInMilli(String reso) {
		long milli = 60000l;
		switch(reso) {
		case "1": milli = 60000l;
				break;
		case "5": milli = 300000l;
				break;
		case "10": milli = 600000l;
				break;
		case "15": milli = 900000l;
		 		break;
		case "30": milli = 1800000l;
				break;
		case "60": milli = 3600000l;
				break;
		case "240": milli = 14400000l ;
				break;
		case "D": milli = 86400000l;
				break;
		case "W": milli = 604800000l;
				break;
		case "M": milli = 2628000000l;  
				break; 
		}
		resoInMilli = milli;
	}
	
	
	/**
	 * adds a new chart to this stock.
	 * It is called up if the required data is already available which means that there is already another chart
	 * that depicts the candles for this symbol and resolution.
	 * 
	 * @param chartID the id of the chart to identify the entry to which the listener must be added
	 * @param listener StockListener (chart) that the data requires
	 */
	protected void addChart(int chartID, StockListener listener) {
		chartIDList.addFirst(chartID);
		listenerList.addFirst(listener);
	}
	
	
	
	/**
	 * removes the chart ID <code>chatID</code> from the <code>chartIDList</code> and the
	 * <code>listener</code> from the <code>listenerList</code>.
	 * 
	 * @param chartID the ID that should be removed
	 * @param listener the listener that should be removed
	 */
	protected void removeListener(int chartID, StockListener listener) { 
		chartIDList.removeFirstOccurrence(chartID);  
		listenerList.remove(listener);
	}
	
	
	
	
	/**
	 * gets the number of listeners that are added to this stock and resolution.
	 * 
	 * @return the number of listeners that are added to this stock and resolution.
	 */
	protected int getNumberOfListeners() {
		return listenerList.size();                 
	}
	
	
	/**
	 * updates the candle that is in progress and informs all listeners that a new price is available.
	 * This method is only called up by the <code>updateStock(double price, long time, long vol)</code>-method 
	 * in the {@link #StockResolutionData}-class.
	 * 
	 * @param price the latest price that belongs to this stock.
	 * @param time the timestamp that belongs to the <code>price</code>.
	 * @param vol the volume that was traded in this period
	 */
	protected void update(double price, long time, long vol) {
		lastPrice = price;
		lastTime = time;
		lastVolume = vol;
		updateLastCandle();
		firePriceChanged();
	}
	//informs all listeners if a new price is available
	private void firePriceChanged() {
		for(StockListener listener : listenerList) {
			listener.newPriceAvailable(lastPrice);
		}
	}
	//updates the candle that is in progress
	private void updateLastCandle() {
		//gets the last candle that is in progress
		Candle candle = candleList.getLast();
		//true means that this candle is complete
		if(lastTime >= candle.getTimeInMilli() + resoInMilli) {
			//creates a new candle which is in progress
			Candle newCandle = new Candle(symbol, resolution, lastPrice, lastPrice, lastPrice, lastPrice, lastTime/1000, lastVolume);
			//removes the oldest candle from the list an adds the completed one
			candleList.removeFirst();
			candleList.addLast(newCandle);
			fireNewCandleAvailable(candle);
		}else {
			//the candle is not complete
			//the candle is updated with the latest data
			if(candle.getHigh() < lastPrice) {
				candle.setHigh(lastPrice);
			}
			if(candle.getLow() > lastPrice) {
				candle.setLow(lastPrice);
			}
			candle.setClose(lastPrice);
			candle.setVolume(lastVolume + candle.getVolume());
		}	
	}
	
	
	//informs all the listeners as well as the IndicatorModel if a new candle is generated
	private void fireNewCandleAvailable(Candle newCandle) {
		int[] ids = new int[chartIDList.size()];
		Iterator<Integer> iter = chartIDList.iterator();
		for(int i = 0; i < chartIDList.size(); i++) {
			ids[i] = iter.next();
	
		}
		indiModel.updateIndi(ids, newCandle);
		for(StockListener listener : listenerList) {
			listener.newCandleAvailable();
		}
	}
	
	@Override
	public int compareTo(String s) {
		return  resolution.compareTo(s);
	}
	

}
