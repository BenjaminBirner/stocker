package Controller;

import java.awt.Dimension;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import Model.chart.ChartPersistence;
import Model.watchlist.FramePersistence;
import View.chart.Chart;
import View.dialogsAndFrames.StockerMainFrame;
import View.dialogsAndFrames.Watchlist;




/**
 * This class manages all frames that are opened and provides access.
 * While the program is running, there are several situations in which
 * access to all open frames is required. That is e.g. in the storing
 * process or if the minimum-size has changed. 
 * 
 * @author Benjamin Birner
 *
 */
public class FrameManager {
	
	private LinkedList<Chart> chartList = new LinkedList<>();
	private Watchlist wl;
	private StockerMainFrame mainFrame;
	
	public FrameManager(StockerMainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}
	
	
	
	
	/**
	 * This method is called up in the program`s storing process and returns a
	 * LinkedList that contains all {@link ChartPersistence}-objects to all opened 
	 * {@link Chart}-objects.
	 * A {@link ChartPersistence}-object contains all the data that are required to 
	 * restore the {@link Chart}-object when the program starts
	 * 
	 * @return LinkedList that contains all {@link ChartPersistence}-objects
	 */
	public LinkedList<ChartPersistence> getChartPersistenceObjs(){
		LinkedList<ChartPersistence> perList = new LinkedList<>();
		for(int i = 0; i < chartList.size(); i++) {
			Chart chart = chartList.get(i);
			//creates the ChartPersistence-object
			ChartPersistence obj = new ChartPersistence(chart.getID(),chart.getType(), chart.getSymbol(),
					chart.getResolution(), chart.getHeight(), chart.getWidth(),
					(int) chart.getLocation().getX(),(int) chart.getLocation().getY(),
					mainFrame.getDesktopPane().getComponentZOrder(chart));
			perList.addLast(obj);
		}
		return perList;
	}
	
	
	
    /**
     * This method is called up in the program`s storing process and returns a {@link FramePersistence}-object
     * which contains all the data that is required to restore the {@link #View.dialogsAndFrames.Watchlist}-object (frame)
     * 
     * @return FramePersistence object that contains the required data to restore the watchlist
     */
	public FramePersistence getWLPersistenceObj() {
		if(wl != null) {
			//creates the FramePersistence object
			FramePersistence fp = new FramePersistence(wl.getHeight(),wl.getWidth(),(int) wl.getLocation().getX(),
					(int) wl.getLocation().getY(), mainFrame.getDesktopPane().getComponentZOrder(wl));
			return fp;
		}
		return null;
	}
	
	
	
	/**
	 * adds a {@link #View.dialogsAndFrames.Watchlist}-object to the Frame-Manager.
	 * This method is called up when a {@link #View.dialogsAndFrames.Watchlist}-object is created.
	 * 
	 * @param wll the {@link #View.dialogsAndFrames.Watchlist}-object that is created.
	 */
	public void addWLPersistence(Watchlist wll) {
		wl = wll;
	}
	
	
	

	/**
	 * removes a {@link #View.dialogsAndFrames.Watchlist}-object from the Frame-Manager.
	 * This method is called up when the watchlist will be closed.
	 * 
	 */
	public void removeWLPersistence() {
		wl = null;             
	}
	
	
	
	/**
	 * gets the information if the FM holds a {@link #View.dialogsAndFrames.Watchlist}-object.
	 * 
	 * @return true if the Frame-Manager holds a {@link #View.dialogsAndFrames.Watchlist}-object, else false.
	 */
	protected boolean getWLStatus() {
		return (wl == null ? false : true);
	}
	
	
	
	

	/**
	 * adds a {@link #Chart}-object to the Frame-Manager.
	 * This method is called up when a {@link #Chart}-object is created.
	 * 
	 * @param chart the {@link #Chart}-object that is created.
	 */
	public void addChartPersistence(Chart chart) {
		chartList.add(chart);
	}
	
	
	
	/**
	 * removes a {@link #Chart}-object from the Frame-Manager.
	 * This method is called up when a Chart will be closed.
	 * 
	 */
	public void removeChartPersistence(Chart chart) {
		chartList.remove(chart);           
	}
	
	
	
	
	
	/**
	 * This method checks the size of all frames if the size is smaller than the minimum size.
	 * If so, the frame must be resized to the minimum size.
	 * This method is called up if the minimum-size in the configuration-dialog has changed.
	 * 
	 * @param minWidth defines the minimum width
	 * @param minHeight defines the minimum height
	 */
	protected void checkMinSize(int minWidth, int minHeight) { 
		Iterator<Chart> it = chartList.iterator();
		//this loop iterates over all chart-objects
		for(int i = 0; i < chartList.size(); i++) {
			Chart chart = it.next();
			int width = minWidth;
			int height = minHeight;
			//checks if the width of the chart is greater than the minimum-width
			if(chart.getWidth() > minWidth) {
				width = chart.getWidth();
			}
			//checks if the height of the chart is greater than the minimum-height
			if(chart.getHeight() > minHeight) {
				height = chart.getHeight();
			}
			//sets the new minimum size
			chart.setMinimumSize(new Dimension(minWidth, minHeight));
			//resizes the chart if necessary
			chart.setSize(width, height);	
		}
		//checks the size for the watchlist if there is a opened one
		if(wl != null) {
			wl.setMinimumSize(new Dimension(minWidth, minHeight));
			if(wl.getWidth() > minWidth) {
				minWidth = wl.getWidth();
			}
			if(wl.getHeight() > minHeight) {
				minHeight = wl.getHeight();
			}
			//resizes the watchlist if necessary
			wl.setSize(minWidth, minHeight);
		}
	}
	
	
	
	/**
	 * this method calls up the repaint()-method for all opened {@link #Chart}-objects.
	 * This method is called up after changing the alarm- or indicator-color
	 */
	protected void repaint() {
		Iterator<Chart> it = chartList.iterator();
		for(int i = 0; i < chartList.size(); i++) {
			Chart chart = it.next();
			chart.repaint();
		}
	}

	protected Set<String> getAllSymbols(){
		Set<String> symbols = new HashSet<>();
		Iterator<Chart> it = chartList.iterator();
		for(int i = 0; i < chartList.size(); i++){
			symbols.add(it.next().getSymbol());
		}
		return symbols;
	}
	
}



