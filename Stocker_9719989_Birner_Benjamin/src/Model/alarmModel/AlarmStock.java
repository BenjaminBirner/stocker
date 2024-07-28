package Model.alarmModel;

import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JOptionPane;


/**
 * this class manages all existing alarms which belongs to an individual stock.
 * These alarms are modeled as {@link #Alarm} objects. This class manages the objects in a list.
 * Furthermore, it checks if an alarm must be triggered. If so, it informs the user by showing a
 * message dialog.
 * Nearly all the modifiers  in this class are package or private, because it receives all the 
 * instructions from the {@link #AlarmDataModel} class.
 * 
 * @author Benjamin Birner
 *
 */
class AlarmStock implements Comparable<String> {
	
	private String symbol;
	private double lastPrice;
	private LinkedList<Alarm> alarmList = new LinkedList<>();
	
	AlarmStock(String symbol, double lastPrice) {
		this.symbol = symbol;
		this.lastPrice = lastPrice;
	}
	
	
	/**
	 *adds an {@link #Alarm} object to its list.
	 *More precisely, it checks if there exists already an alarm with this value.
	 *if not, it creates an {@link #Alarm} object with all the relevant information.
	 * 
	 * @param value the price that triggers the alarm.
	 */
	protected void addAlarm(double value) {  
		boolean contains = false;
		for(Alarm o : alarmList) {
			if(o.price == value) {
				contains = true;
			}
		}
		if(!contains) {
			Alarm obj;
			if(value < lastPrice) {
				obj = new Alarm(true,value);
			}else {
				obj = new Alarm(false,value);
			}
			alarmList.addFirst(obj);
		}
	}
	
	
	/**
	 * gets the symbol of the this-object.
	 * 
	 * @return the symbol of the this-object.
	 */
	protected String getSymbol() {
		return symbol;
	}
	
	
	/**
	 * restores the alarms whereas the program starts.
	 * 
	 * @param value the value that defines when the alarm should be triggered.
	 * @param typ true if the price is under the value when the alarm was set.
	 *            Otherwise false.
	 */
	protected void restoreAlarm(double value, boolean typ) {
		alarmList.addFirst(new Alarm(typ, value));
	}
	
	
	
	/**
	 * gets all existing alarms of this stock.
	 * The results are required to display the alarms.
	 * 
	 * @return <code>-Array</code> with all prices.
	 */
	protected String[] getAllAlarmsInString() {
		String[] alarm = new String[alarmList.size()];
		int i = 0;
		Double d;
		String s;
		for(Alarm o : alarmList) {
			d = o.price;
			s = d.toString();
			alarm[i] = s;
			i = i + 1;
		}
		return alarm;
	}
	
	
	/**
	 * gets all existing alarms of this stock.
	 * The results are required to paint the alarms.
	 * 
	 * @return <code>double[]</code>-Array with all prices.
	 */
	protected double[] getAllAlarmsInDouble() {
		double[] alarm = new double[alarmList.size()];
		int i = 0;
		for(Alarm o : alarmList) {
			alarm[i] = o.price;
			i = i + 1;
		}
		return alarm;
	}
	
	
	/**
	 * creates an {@link #AlarmPersistence} object for every  {@link #Alarm} object of this stock.
	 * The results of this method are required to store and restore the alarms when the program is closed
	 * or started.
	 * 
	 * @return Array with all {@link #AlarmPersistence} objects.
	 */
	protected AlarmPersistence[] getAlarmPersistenceData() {
		AlarmPersistence[] data = new AlarmPersistence[alarmList.size()];
		Iterator<Alarm> it = alarmList.iterator();
		for(int i = 0; i < alarmList.size(); i++) {
			Alarm obj = it.next();
			data[i] = new AlarmPersistence(symbol,obj.price,obj.typ);
		}
		return data;
	}
	
	
	/**
	 * deletes for every index in the array <code>indices</code> the corresponding {@link #Alarm} object.
	 * 
	 * @param indices the indices for the <code>Alarm</code> objects in the list which must be deleted.
	 * @return if there is no remaining <code>Alarm</code> object after the deletion for this stock,
	 *         the method returns -1. Otherwise 1.
	 */
	protected int deleteAlarm(int[] indices) {
		for( int i : indices) {
			alarmList.remove(i);
		}
		if( alarmList.size() == 0) {
			return -1;
		}
		return 1;
	}
	
	
	
	/**
	 * sets the latest price.
	 * 
	 * @param price latest price
	 */
	protected void setLastPrice(double price) {
		lastPrice = price;	
	}
	
	
	
	/**
	 * checks for every {@link #Alarm} object if the alarm must be triggered.
	 * If so, the method calls up the {@link #fireAlarm(double value)} method and removes the
	 * object. 
	 * This method is called up every time when the price for this stock is updated.
	 * 
	 * @return the number of remaining {@link #Alarm} objects.
	 */
	protected int checkAlarm() {
		for (Alarm o : alarmList) {
			if( o.typ == true) {
				if(lastPrice <= o.price) {
					fireAlarm(o.price);
					alarmList.remove(o);
					return alarmList.size();
				}
				
			}else {
				if(lastPrice >= o.price) {
					fireAlarm(o.price);
					alarmList.remove(o);
					return alarmList.size();
				}
			}
		}
		return alarmList.size();
	}
	

	private void fireAlarm(double value) {
		String message = "ALARM WURDE AUSGELÖST!!!\n" + 
					"Symbol:               " + symbol + "\n"+
					"Alarmwert:          " + value + "\n"+
					"Aktueller Wert:  " + lastPrice ;
		JOptionPane.showMessageDialog(null, message,"ALARM!!!",JOptionPane.INFORMATION_MESSAGE );
	}
	
	
	
	
	/**
	 * deletes an alarm for the alarm price <code>threshold</code>.
	 * This method is only used to test the alarm-Model.
	 * 
	 * @param threshold the alarm price of this stock that should be deleted
	 * @return -1 if the <code>alarmList</code> is empty. Otherwise 1.
	 */

	protected int deleteAlarm(double threshold) {
		Iterator<Alarm> it = alarmList.iterator();
		for(int i = 0; i < alarmList.size(); i++) {
			Alarm obj = it.next();
			if(obj.price == threshold) {
				alarmList.remove(i);
			}
		}
		if (alarmList.size() == 0) {
			return -1;
		}
		return 1;
	}
	
	
	
	

	
	@Override
	public int compareTo(String s) {
		return  symbol.compareTo(s);
	}
	
	
    /**
     * this class models an Alarm.
     * 
     * @author Benjamin Birner
     *
     */
	private static class Alarm{
		private boolean typ;
		private double price;
		
		private Alarm(boolean typ, double price) {
			this.typ = typ;
			this.price = price;
		}
	}

}
