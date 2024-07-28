package Model.alarmModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * This class manages all {@link #AlarmStock} objects in an ArrayList.
 * These objects are sorted according to there symbol.
 * Every action that is done or information that is required in the
 * controller and view concerning alarms, an instance of this class must be called up
 * and it carries out the instructions. So, it is the only class to get access to 
 * alarm information.
 * 
 * @author Benjamin Birner
 *
 */

public class AlarmDataModel {
	
	private ArrayList<AlarmStock> stockList = new ArrayList<>(20);
    
    /**
     * adds a new alarm to the corresponding stock.
     * It creates an {@link #AlarmStock} object if it is the first alarm for this stock
     * and calls up its <code>addAlarm(double value)</code> method. 
     * If there is already one, it merely calls up this method.
     * 
     * @param symbol the symbol of the corresponding stock.
     * @param lastPrice the latest price that is available for this stock.
     * @param value the alarm value.
     */
	public void addAlarm(String symbol, double lastPrice, double value) {
		int index = Collections.binarySearch(stockList, symbol);
		if(index < 0) {
			stockList.add(-index-1, new AlarmStock(symbol, lastPrice));
			stockList.get(-index-1).addAlarm(value);
		}else {
			stockList.get(index).addAlarm(value);
		}
	}
	
	
	/**
	 * restores the alarms whereas the program starts.
	 * 
	 * @param symbol symbol of the stock
	 * @param lastPrice the latest price that is available for this stock.
	 * @param value the value that defines when the alarm should be triggered.
	 * @param typ true if the price is under the value when the alarm was set.
	 *            Otherwise false.
	 */
	public void restoreAlarm(String symbol, double lastPrice, double value, boolean typ) {
		int index = Collections.binarySearch(stockList, symbol);
		if(index < 0) {
			stockList.add(-index-1, new AlarmStock(symbol, lastPrice));
			stockList.get(-index-1).restoreAlarm(value,typ);
		}else {
			stockList.get(index).restoreAlarm(value,typ);
		}
	}
	
	
	/**
	 * gets all existing alarms for a stock/symbol.
	 * If there is an alarm, it calls up the <code>AlarmStock.getAllAlarmsInString()</code> method 
	 * on the corresponding Object.
	 * This method is called up by the view to display all the alarms.
	 * 
	 * @param symbol the symbol of the stock.
	 * @return <code>String</code>-Array with all alarm values.                              
	 */
	public String[] getAllAlarmsInString(String symbol) {
		int index = Collections.binarySearch(stockList, symbol);
		if(index < 0) {
			String[] s = new String[1];
			s[0] = "kein Alarm gesetzt";                       
			return s;                                     
		}
		return stockList.get(index).getAllAlarmsInString();
	}
	
	
	/**
	 * gets all existing alarms for a stock/symbol.
	 * If there is an alarm, it calls up the <code>AlarmStock.getAllAlarmsInDouble()</code> method 
	 * on the corresponding Object.
	 * This method is called up by the view to draw all the alarms.
	 * 
	 * @param symbol the symbol of the stock.
	 * @return <code>double[]</code>-Array with all alarm values. <code>Null</code> if there is no alarm.    
	 */
	public double[] getAllAlarmsInDouble(String symbol) {
		int index = Collections.binarySearch(stockList, symbol);
		if(index < 0) {
			
			return null;                                     
		}
		return stockList.get(index).getAllAlarmsInDouble();
	}
	
	
	/**
	 * gets all AlarmPersistence-Objects for every existing alarm.
	 * On every AlarmStock-Object it calls up the  <code>AlarmStock.getAlarmPersistenceData()</code> method.
	 * This method is used to get all the relevant information to store the alarms when the program is
	 * in the closing-process. 
	 * 
	 * @return LinkedList with all {@link #AlarmPersistence}-Objects.
	 */
	public LinkedList<AlarmPersistence> getAlarmPersistenceData(){
		LinkedList<AlarmPersistence> list = new LinkedList<>();
		for(int i = 0; i < stockList.size(); i++) {
			AlarmPersistence[] data = stockList.get(i).getAlarmPersistenceData();
			for(int j = 0; j < data.length; j++) {
				list.addFirst(data[j]);
			}
		}
		return list;
	}

	
	/**
	 * removes alarms from a stock.
	 * It calls up the <code>AlarmStock.deleteAlarm(int[] indices)</code> method on the corresponding {@link #AlarmStock}-Object
	 * according to the values in the <code>indices</code>-Array.
	 * If there is no more alarm after the deletion, this object is going to be deleted.
	 * 
	 * @param symbol the symbol of the stock.
	 * @param indices Array including all indices of the alarms that should be deleted.
	 */
	public void deleteAlarm(String symbol, int[] indices) {
		int index = Collections.binarySearch(stockList, symbol);
		if (index >= 0) {
			int i = stockList.get(index).deleteAlarm(indices);
			if( i == -1) {
				stockList.remove(index);
			}
		}
		
	}
	
	
	
	/**
	 * deletes the complete {@link #AlarmStock}-Object with all its alarms.
	 * This method is called up if there is no entry in the watchlist for
	 * this symbol and if the last open chart for this symbol has just been
	 * closed. And in the reverse case.
	 * 
	 * @param symbol the symbol of the stock.
	 */
	public void deleteEntry(String symbol) {
		int index = Collections.binarySearch(stockList,  symbol);
		if(index >= 0) {
			stockList.remove(index);
		}
	}
	
	
	
	/**
	 * updates the last price and checks if an alarm must be triggered.
	 * It gets the corresponding {@link #AlarmStock}-Object and calls up its <code>AlarmStock.setLastPrice(double price)</code>
	 * and <code>AlarmStock.checkAlarm()</code> method. If an alarm has triggered and under the condition that there is
	 * no more remaining alarm, it removes the object.
	 * This method is used by the {@link #StocksRealTimeClient} class and its <code>onMessage(String json)</code>
	 * method.
	 * Consequently, every time when there is a new price available, this method is called up.
	 * 
	 * @param symbol the symbol of the stock.
	 * @param price the latest price for the stock.
	 */
	public void updateLastPrice(String symbol, double price) {
		int index = Collections.binarySearch(stockList,  symbol);
		if(index >= 0) {
			AlarmStock stock = stockList.get(index);
			stock.setLastPrice(price);
			int i = stock.checkAlarm();
			if ( i == 0 ) {
				stockList.remove(index);
				
			}
		}	
	}
	
	
	
	
	
	
	
	
	/**
	 * removes all entries in the <code>AlarmModel</code>
	 * This method is only called up in the {@link #StockerTesterImpl} class.
	 */
	public void clearAlarmListTest() {
		stockList.clear();
	}
	
	
	/**
	 * deletes an alarm for the stock with the symbol <code>symbol</code> and the alarm price <code>threshold</code>.
	 * This method is only called up in the {@link #StockerTesterImpl} class.
	 * 
	 * @param symbol the symbol for the stock of which the alarm should be deleted
	 * @param threshold the alarm price of this stock that should be deleted
	 */
	public void deleteAlarmTest(String symbol, double threshold) {
		int index = Collections.binarySearch(stockList,  symbol);
		int i = stockList.get(index).deleteAlarm(threshold);
		if( i == -1) {
			stockList.remove(index);
		}
	}
	
	/**
	 * gets all the symbols of the stocks to which an alarm exists.
	 * This method is only called up in the {@link #StockerTesterImpl} class.
	 * 
	 * @return symb HashSet that contains all the symbols to which an alarm exists
	 */
	public Set<String> getAllSymbolsTest(){
		Set<String> symb = new HashSet<>();
		for(int i = 0; i < stockList.size(); i++) {
			AlarmStock stock = stockList.get(i);
			symb.add(stock.getSymbol());
		}
		return symb;
	}
	
	
	
}
