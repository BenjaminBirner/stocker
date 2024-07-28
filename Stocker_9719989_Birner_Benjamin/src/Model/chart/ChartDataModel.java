package Model.chart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import Model.indicatorModel.IndicatorModel;
import View.chart.Chart;
import View.dialogsAndFrames.Watchlist;


/**
 * This class manages the communication with all the different instances of the class
 * {@link #StockResolutionData} which represents a stock with a particular resolution.
 * Therefore, the view, the controller and the client merely have a reference to this
 * class to exchange data. It provides access to all the data a chart requires.
 * 
 * @author Benjamin Birner
 *
 */
public class ChartDataModel {
	
	
	
	private ArrayList<StockResolutionData> stockList = new ArrayList<>(20);

	/**
	 *gets the index in the ArrayList <code>stockList</code> for the parameter <code>symbol</code>.
	 *This method is often used in the program to check if there exists already a entry for this symbol.
	 * 
	 * @param symbol the symbol of a stock 
	 * @return the index if there is an entry. Otherwise it returns a negative integer that can be used to
	 * add a new entry concerning this symbol. in order to maintain the order, the negative index must be used
	 * like this: -index-1
 	 */
	public int getIndex(String symbol) {
		return Collections.binarySearch(stockList, symbol);
	}
	
	
	/**
	 * creates a new entry in the ArrayList <code>stockList</code>
	 * 
	 * @param index the position at which the entry should be added
	 * @param symbol the symbol of this stock/entry
	 */
	public void createNewEntry(int index, String symbol) {
		stockList.add(index, new StockResolutionData(symbol));
	}
	
	
	/**
	 * checks if the stock in the ArrayList <code>stockList</code> at the position which is referenced by
	 * the parameter <code>index</code> already contains the data for the resolution that is referenced by 
	 * the parameter <code>resolution</code>.
	 * 
	 * @param index the index for the required stock in the ArrayList <code>stockList</code>.
	 * @param resolution the resolution that should be checked
	 * @return true if this stock contains the data for this resolution
	 */
	public boolean containsResolution(int index, String resolution) {
		return stockList.get(index).contains(resolution);
	}
	
	
	/**
	 * checks if there exists an entry in the ArrayList <code>stockList</code> for the symbol which
	 *  is referenced by the parameter <code>symbol</code>.
	 * 
	 * @param symbol of a stock that should be checked
	 * @return true if this stock is in the ArrayList <code>stockList</code>. Otherwise false.
	 */
	public boolean contains(String symbol) {
		int index = getIndex(symbol);
		return (index < 0 ? false : true);
	}
	
	
	/**
	 *adds a new stock to the the model.
	 *To do so, it calls up the <code>addStock(int chartID, String resolution, CandleRequestData data, StockListener listener, IndicatorModel indiModel)</code>
	 *method on the {@link #StockResolutionData} object according to the index that is referenced by the parameter <code>index</code>.
	 *This method is called up if a complete new stock with all its data must be added which means that a candle request was necessary.
	 * 
	 * @param index the index to get the correct object from the ArrayList <code>stockList</code>.
	 * @param chartID the ID of the chart that represents this stock.
	 * @param resolution the resolution of this stock
	 * @param data the data from the candle request
	 * @param listener an instance of the type {@link #StockListener} which must be updated if a new candle is available.
	 * @param indiModel reference to the {@link #IndicatorModel} which must also be updated.
	 */
	public void addStock ( int index, int chartID, String resolution,CandleRequestData data, StockListener listener, IndicatorModel indiModel) {
		stockList.get(index).addStock(chartID, resolution, data, listener, indiModel);
	}
	
	
	/**
	 *adds a chart to a already existing stock and resolution.
	 *This method is called up if the data that the chart requires is already in the model.
	 * 
	 * @param index the index to get the correct object from the ArrayList <code>stockList</code>
	 * @param chartID the ID of the chart that represents this stock.
	 * @param resolution the resolution of this stock
	 * @param listener an instance of the type {@link #StockListener} which must be updated if a new candle is available.
	 */
	public void addChart(int index, int chartID, String resolution, StockListener listener) {
		stockList.get(index).addChart(chartID, resolution, listener);
	}
	
	
	
	/**
	 * removes the entry concerning the chart ID that is referenced by the parameter <code>chartID</code>.
	 * 	
	 * @param index the index to get the correct object from the ArrayList <code>stockList</code>
	 * @param resolution the resolution that this chart represents concerning this stock
	 * @param chartID the ID of the chart that should be removed
	 * @param listener an instance of the type {@link #StockListener} which must also be removed.
	 * @return 1 if this method deletes the complete {@link #StockResolutionData} object due to the fact that 
	 *         there is no chart that represents this stock. Otherwise 0
	 */
	public int remove(int index, String resolution, int chartID, StockListener listener) {
		int i = stockList.get(index).remove(resolution,  chartID, listener);
		if( i == 1) {
			stockList.remove(index);
			return 1;
		}
		return 0;
	}
	
	
	/**
	 *updates the stock which is defined by the symbol that is referenced by the parameter <code>symbol</code>
	 *if new data is available.
	 *This method is called up by the {@link #StocksRealTimeClient} and its <code>onMessage(String json)</code> method.
	 * 
	 * @param symbol the symbol of the stock that should be updated
	 * @param price the latest price to update the stock
	 * @param time the timestamp to this price
	 * @param vol the volume that was traded since the last timestamp and this one
	 */
	public void updateStock(String symbol, double price, long time, long vol) {
		int index = getIndex(symbol);
		if(index >= 0) {
			stockList.get(index).updateStock(price, time, vol);
		}
	}
	
	/**
	 *gets the latest price to a symbol that is defined by the parameter <code>symbol</code>.
	 * 
	 * @param symbol represents the symbol to the stock of that the latest price is required
	 * @return the latest price of this stock
	 */
	public double getLastPrice(String symbol) {
		int index = getIndex(symbol);
		return stockList.get(index).getLastPrice();
	}

	/**
	 *gets the timestamp to a specified symbol and resolution at the position which is defined by 
	 *the parameter <code>timeIndex</code>.
	 * 
	 * @param listIndex defines the position for the <code>stockList</code> to get the required stock
	 * @param timeIndex defines the position for the required timestamp.
	 * @param resolution defines the resolution to which the timestamp belongs to.
	 * @return the timestamp
	 */
	public long getTimeAt(int listIndex, int timeIndex, String resolution) {
		return stockList.get(listIndex).getTimeAt(timeIndex,resolution);
		
	}
	
	
	/**
	 *gets all timestamps to a specified symbol and resolution 
	 * 
	 * @param symbol defines the symbol to which the timestamps are required
	 * @param resolution defines the resolution to which the timestamps belongs to.
	 * @return the timestamps in a <code>long</code> array
	 */
	public long[] getAllTimestamps(String symbol, String resolution) {
		int index = getIndex(symbol);
		return stockList.get(index).getAllTimestamps(resolution);
	}
	
	
	/**
	 *gets all close prices to a specified symbol and resolution 
	 * 
	 * @param symbol defines the symbol to which the close prices are required
	 * @param resolution defines the resolution to which the close prices belongs to.
	 * @return the close prices in a <code>double</code> array
	 */
	public double[] getAllClose(String symbol, String resolution) {
		int index = getIndex(symbol);
	    return stockList.get(index).getAllClose(resolution);
	}
	
	
	/**
	 *gets all high prices to a specified symbol and resolution 
	 * 
	 * @param symbol defines the symbol to which the high prices are required
	 * @param resolution defines the resolution to which the high prices belongs to.
	 * @return the high prices in a <code>double</code> array
	 */
	public double[] getAllHigh(String symbol, String resolution) {
		int index = getIndex(symbol);
		return stockList.get(index).getAllHigh(resolution);
	}
	
	
	/**
	 *gets all timestamps to a specified symbol and resolution 
	 * 
	 * @param symbol defines the symbol to which the low prices are required
	 * @param resolution defines the resolution to which the low prices belongs to.
	 * @return the low prices in a <code>double</code> array
	 */
	public double[] getAllLow(String symbol, String resolution) {
		int index = getIndex(symbol);
		return stockList.get(index).getAllLow(resolution);
	}
	
	
	/**
	 *gets all open prices to a specified symbol and resolution 
	 * 
	 * @param symbol defines the symbol to which the open prices are required
	 * @param resolution defines the resolution to which the open prices belongs to.
	 * @return the open prices in a <code>double</code> array
	 */
	public double[] getAllOpen(String symbol, String resolution) {
		int index = getIndex(symbol);
		return stockList.get(index).getAllOpen(resolution);
	}
	
	
	/**
	 *gets the lowest close prices for a specified symbol and resolution 
	 * 
	 * @param symbol defines the symbol to which the lowest close prices is required
	 * @param resolution defines the resolution to which the lowest close prices belongs to.
	 * @return the lowest close prices
	 */
	public double getMinClose(String symbol, String resolution) {
		int index = getIndex(symbol);
		return stockList.get(index).getMinClose(resolution);
	}
	
	
	/**
	 *gets the highest close prices for a specified symbol and resolution 
	 * 
	 * @param symbol defines the symbol to which the highest close prices is required
	 * @param resolution defines the resolution to which the highest close prices belongs to.
	 * @return the highest close prices
	 */
	public double getMaxClose(String symbol, String resolution) {
		int index = getIndex(symbol);
		return stockList.get(index).getMaxClose(resolution);
	}
	
	
	/**
	 *gets the lowest low prices for a specified symbol and resolution 
	 * 
	 * @param symbol defines the symbol to which the lowest low prices is required
	 * @param resolution defines the resolution to which the lowest low prices belongs to.
	 * @return the lowest low prices
	 */
	public double getMinLow(String symbol, String resolution) {
		int index = getIndex(symbol);
		return stockList.get(index).getMinLow(resolution);
	}
	
	
	/**
	 *gets the highest high prices for a specified symbol and resolution 
	 * 
	 * @param symbol defines the symbol to which the highest high prices is required
	 * @param resolution defines the resolution to which the highest high prices belongs to.
	 * @return the highest high prices
	 */
	public double getMaxHigh(String symbol, String resolution) {
		int index = getIndex(symbol);
		return stockList.get(index).getMaxHigh(resolution);
	}
	
	

	/**
	 * gets the current high prices regarding the candles that is in progress.
	 * 
	 * @param symbol defines the symbol to which the current high prices is required
	 * @param resolution defines the resolution to which the current high price is required.
	 * @return the current high price
	 */
	public double getCurrentHigh(String symbol, String resolution) {
		int index = getIndex(symbol);
		return stockList.get(index).getCurrentHigh(resolution);
	}
	
	
	/**
	 * gets the current low prices regarding the candles that is in progress.
	 * 
	 * @param symbol defines the symbol to which the current low prices is required 
	 * @param resolution defines the resolution to which the current low price is required.
	 * @return the current low price
	 */
	public double getCurrentLow(String symbol, String resolution) {
		int index = getIndex(symbol);
		return stockList.get(index).getCurrentLow(resolution);
	}

}
