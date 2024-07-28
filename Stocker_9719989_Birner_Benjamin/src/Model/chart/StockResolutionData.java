package Model.chart;

import java.util.LinkedList;

import Model.indicatorModel.IndicatorModel;


/**
 * This class manages the different resolutions for a stock.
 * Every stock requires separate data for each resolution. This data is represented by a
 * {@link #StockCandleData}-object. These objects are managed in the <code>resolutionList</code>-list
 * of  this class.
 * Only the {@link #ChartDataModel}-class has access to this one because it manages all the instances
 * of this class in its list.
 * 
 * @author Benjamin Birner
 *
 */
class StockResolutionData implements Comparable<String> {

	private final String symbol;
	private double price;                                               
	private long time;
	private long volume;
	private final LinkedList<StockCandleData> resolutionList = new LinkedList<>();
	
	protected StockResolutionData(String symbol) {
		this.symbol = symbol;
	}
	
	protected double getLastPrice() {
		return price;
	}
	
	

	/**
	 * gets the timestamp of the candle at position <code>index</code> for the resolution <code>resolution</code>.
	 * 
	 * @param index defines the position of the candle to which the required timestamp belongs to.
	 * @param resolution defines the resolution to which the timestamp is required.
	 * @return the timestamp of the candle at position <code>index</code>.
	 */
	protected long getTimeAt(int index, String resolution) {
		return getStock(resolution).getTimeAt(index);
	}
	
	
	
	/**
	 * gets all timestamps of the candles that belong to this stock and to the resolution which is defined by 
	 * the parameter <code>resolution</code>.
	 * 
	 * @param resolution defines the resolution to which the timestamps are required.
	 * @return <code>long</code>-array with all timestamps of the candles.
	 */
	protected long[] getAllTimestamps(String resolution) {
		return getStock(resolution).getAllTimestamps();
	}
	
	
	
	/**
	 * gets all close prices of the candles that belong to this stock and to the resolution which is defined by 
	 * the parameter <code>resolution</code>.
	 * 
	 * @param resolution defines the resolution to which the close prices are required.
	 * @return <code>double</code>-array with all close prices of the candles.
	 */
	protected double[] getAllClose(String resolution) {
		return getStock(resolution).getAllClose();
	}
	
	
	
	/**
	 * gets all high prices of the candles that belong to this stock and to the resolution which is defined by 
	 * the parameter <code>resolution</code>.
	 * 
	 * @param resolution defines the resolution to which the high prices are required.
	 * @return <code>double</code>-array with all high prices of the candles.
	 */
	protected double[] getAllHigh(String resolution) {
		return getStock(resolution).getAllHigh();
	}
	
	
	
	
	/**
	 * gets all low prices of the candles that belong to this stock and to the resolution which is defined by 
	 * the parameter <code>resolution</code>.
	 * 
	 * @param resolution defines the resolution to which the low prices are required.
	 * @return <code>double</code>-array with all low prices of the candles.
	 */
	protected double[] getAllLow(String resolution) {
		return getStock(resolution).getAllLow();
	}
	
	
	
	
	/**
	 * gets all open prices of the candles that belong to this stock and to the resolution which is defined by 
	 * the parameter <code>resolution</code>.
	 * 
	 * @param resolution defines the resolution to which the open prices are required.
	 * @return <code>double</code>-array with all open prices of the candles.
	 */
	protected double[] getAllOpen(String resolution) {
		return getStock(resolution).getAllOpen();
	}
	
	
	
	
	/**
	 * gets the minimum close price of all the candles that belong to this stock and to the resolution which is defined by 
	 * the parameter <code>resolution</code>.
	 * 
	 * @param resolution defines the resolution to which the minimum close price is required.
	 * @return the minimum close price of all the candles.
	 */
	protected double getMinClose(String resolution) {
		return getStock(resolution).getMinClose();
	}
	
	
	
	/**
	 * gets the maximum close price of all the candles that belong to this stock and to the resolution which is defined by 
	 * the parameter <code>resolution</code>.
	 * 
	 * @param resolution defines the resolution to which the maximum close price is required.
	 * @return the maximum close price of all the candles.
	 */
	protected double getMaxClose(String resolution) {
		return getStock(resolution).getMaxClose();
	}
	
	
	
	
	/**
	 * gets the minimum low price of all the candles that belong to this stock and to the resolution which is defined by 
	 * the parameter <code>resolution</code>.
	 * 
	 * @param resolution defines the resolution to which the minimum low price is required.
	 * @return the minimum low price of all the candles.
	 */
	protected double getMinLow(String resolution) {
		return getStock(resolution).getMinLow();
	}		
	
	
	
	
	
	/**
	 * gets the maximum high price of all the candles that belong to this stock and to the resolution which is defined by 
	 * the parameter <code>resolution</code>.
	 * 
	 * @param resolution defines the resolution to which the maximum high price is required.
	 * @return the maximum high price of all the candles.
	 */
	protected double getMaxHigh(String resolution) {
		return getStock(resolution).getMaxHigh();
	}
	
	
	
	/**
	 * gets the current high prices regarding the candles that is in progress.
	 * 
	 * @param resolution defines the resolution to which the current high price is required.
	 * @return the current high price
	 */
	protected double getCurrentHigh(String resolution) {
		return getStock(resolution).getCurrentHigh();
	}
	
	
	/**
	 * gets the current low prices regarding the candles that is in progress.
	 * 
	 * @param resolution defines the resolution to which the current low price is required.
	 * @return the current low price
	 */
	protected double getCurrentLow(String resolution) {
		return getStock(resolution).getCurrentLow();
	}
	
	
	
	/**
	 * checks if there exists an entry for the resolution <code>resolution</code> in the <code>resolutionList</code>.
	 * 
	 * @param resolution the resolution that should be checked.
	 * @return true if the entry exists. Otherwise false.
	 */
	protected boolean contains(String resolution) {
		for(StockCandleData data : resolutionList) {
			if( data.getResolution().equals(resolution)) {
				return true;
			}
		}
		return false;
	}
	
	
	
	private StockCandleData getStock(String resolution) {
		for(StockCandleData data : resolutionList) {
			if(data.getResolution().equals(resolution)) {
				return data;
			}
		}
		return null;
	}
	
	
	
	
	/**
	 * adds a new chart to this stock for the resolution that is defined by the parameter <code>resolution</code>..
	 * It is called up if the required data was not available which means that a candle-request was necessary.
	 * The candle-request data is referenced by the parameter <code>data</code>. 
	 * 
	 * @param chartID the ID of the chart that represents this stock and resolution
	 * @param resolution the resolution that is represented by this chart
	 * @param data the data from the candle request
	 * @param listener an instance of the type {@link #StockListener} which must be updated if a new candle is available.
	 * @param indiModel reference to the {@link #IndicatorModel} which must also be updated.
	 */
	protected void addStock(int chartID, String resolution, CandleRequestData data, StockListener listener, IndicatorModel indiModel) {
		StockCandleData cData= new StockCandleData(symbol, resolution, chartID, data, listener, indiModel);
		resolutionList.addFirst(cData);
	}
	
	
	
	/**
	 * adds a new chart to this stock for the resolution that is defined by the parameter <code>resolution</code>..
	 * It is called up if the required data is already available which means that there is already another chart
	 * that depicts the candles for this symbol and resolution.
	 * 
	 * @param chartID the id of the chart to identify the entry to which the listener must be added
	 * @param resolution defines the resolution to which the listener should be added.
	 * @param listener StockListener (chart) that the data requires
	 */
	protected void addChart(int chartID, String resolution, StockListener listener) {
		StockCandleData data = getStock(resolution);
		if(data != null){
			data.addChart(chartID,  listener);
		}
	}
	
	
	
	
	/**
	 * removes the entry concerning the chart ID that is referenced by the parameter <code>chartID</code>.
	 * 	
	 * @param resolution the resolution that this chart represents concerning this stock
	 * @param chartID the ID of the chart that should be removed
	 * @param listener an instance of the type {@link #StockListener} which must also be removed.
	 * @return 1 if there is no listener to this stock after the deletion. Otherwise 0.
	 */
	protected int remove(String resolution, int chartID, StockListener listener) {
		StockCandleData stock = getStock(resolution);
		int size = stock.getNumberOfListeners(); 
		/*
		 * true means that there is no listener to this stock after the deletion.
		 * So, it is no longer required and it is deleted by the remove-method 
		 * in the ChartDataModel-class.
		 */
		if(resolutionList.size() == 1 && size == 1) {
			return 1;
		}
		/*
		 * true means that there is no listener to this resolution after the deletion.
		 * So, it is no longer required and it deleted.
		 */
		if( size == 1) {
			resolutionList.remove(stock);
		/*
		 * else means that there are several charts which depicts the same stock and resolution.
		 * So, only the listener must be removed. 
		 */
 		}else {
 			stock.removeListener(chartID,  listener);
 		}
		return 0;
	}
	
	
	
	
	/**
	 * updates the stock if new data is available.
	 * 
	 * @param price the latest price to update the stock
	 * @param time the timestamp to this price
	 * @param vol the volume that was traded since the last timestamp and this one
	 */
	protected void updateStock(double price, long time, long vol) {
		this.price = price;                                             
		this.time = time;
		this.volume = vol;
		for( StockCandleData stock : resolutionList) {
			stock.update(price,  time,  vol);
		}
	}
	
	
	@Override
	public int compareTo(String s) {
		return  symbol.compareTo(s);
	}

}
