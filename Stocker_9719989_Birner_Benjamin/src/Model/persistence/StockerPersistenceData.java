package Model.persistence;

import java.util.LinkedList;

import Model.alarmModel.AlarmPersistence;
import Model.chart.ChartPersistence;
import Model.watchlist.FramePersistence;
import Model.watchlist.WatchlistPersistence;



/**
 * This class is the center of all persistence data. 
 * It contains all data that has to be stored.
 * The controller collects all <code>xxxPersistence</code>-objects and creates
 * an instance of this class. 
 * Then, the <code>StockerPersistenceToPropertiesFile.storeToPropertiesFile(StockerPersistenceData data)</code>-method
 * is called up with this instance and the status is stored.
 * When the program starts, the <code>StockerPersistenceToPropertiesFile.loadStockerStatusFromProperties(IndicatorModel indiModel)</code>-method
 * reconstructs this object so that the former status can be restored.
 * 
 * @author Benjamin Birner
 *
 */
public class StockerPersistenceData {  //Vorteil: Erweiterung der zu persistierenden Daten; wechsel zu anderen form der persis
	
	
	//contains all the data to restore the watchlist-entrys
	private  LinkedList<WatchlistPersistence> wlPerList;
	
	//contains all the data to restore the alarms
	private  LinkedList<AlarmPersistence> alarmList;
	
	//contains all the data to restore the charts
	private  LinkedList<ChartPersistence> chartList;
	
	//contains all the data to restore the watchlist-frame
	private  FramePersistence wlFrame;
	
	//contains all the data to restore the main-frame
	private  FramePersistence mainFrame;
	
	
	
	/**
	 * adds the <code>list</code> to this object
	 * 
	 * @param list <code>LinkedList</code> that contains all the data to restore the watchlist-entries
	 */
	public void addWLPersistenceList(LinkedList<WatchlistPersistence> list) {
		wlPerList = list;
	}
	
	
	/**
	 * adds the <code>list</code> to this object
	 * 
	 * @param list <code>LinkedList</code> that contains all the data to restore the alarms
	 */
	public void addAlarmList(LinkedList<AlarmPersistence> list) {
		alarmList = list;
	}
	
	
	
	/**
	 * adds the <code>list</code> to this object
	 * 
	 * @param list <code>LinkedList</code> that contains all the data to restore the charts
	 */
	public void addChartList(LinkedList<ChartPersistence> list) {
		chartList = list;
	}
	
	
	
	/**
	 * sets the <code>frame</code> that contains all the data to restore the watchlist-frame.
	 * 
	 * @param frame contains all the data to restore the watchlist-frame.
	 */
	public void setWLFrame(FramePersistence frame) {
		wlFrame = frame;
	}
	
	
	

	/**
	 * sets the <code>frame</code> that contains all the data to restore the main-frame.
	 * 
	 * @param frame contains all the data to restore the main-frame.
	 */
	public void setMainFrame(FramePersistence frame) {
		mainFrame = frame;
	}
	
	
	
	/**
	 * gets the <code>wlPerList</code> that contains all the data to restore the watchlist-entries.
	 * 
	 * @return the <code>wlPerList</code> that contains all the data to restore the watchlist-entries.
	 */
	public LinkedList<WatchlistPersistence> getWLPersistenceList(){
		return wlPerList;
	}
	
	
	
	/**
	 * gets the <code>alarmList</code> that contains all the data to restore the alarms.
	 * 
	 * @return the <code>alarmList</code> that contains all the data to restore the alarms.
	 */
	public LinkedList<AlarmPersistence> getAlarmList(){
		return alarmList;
	}
	
	
	
	
	/**
	 * gets the <code>chartList</code> that contains all the data to restore the charts.
	 * 
	 * @return the <code>chartList</code> that contains all the data to restore the charts.
	 */
	public LinkedList<ChartPersistence> getChartList(){
		return chartList;
	}
	
	
	/**
	 * gets the <code>wlFrame</code> that contains all the data to restore the watchlist-frame.
	 * 
	 * @return the <code>wlFrame</code>-object that contains all the data to restore the watchlist-frame.
	 */
	public FramePersistence getWlFramePersis() {
		return wlFrame;
	}
	
	
	
	/**
	 * gets the <code>mainFrame</code> that contains all the data to restore the main-frame.
	 * 
	 * @return the <code>mainFrame</code>-object that contains all the data to restore the main-frame.
	 */
	public FramePersistence getMainFramePersis() {
		return mainFrame;
	}

}
