package Model.indicatorModel;

import java.awt.Color;

import Model.chart.Candle;
import Model.chart.CandleRequestData;


/**
 * Every indicator must implement this interface in order that the program works correctly.
 * If the program is going to be upgraded with another indicator, this interface makes it
 * possible that no alterations or adaptations in the source code are necessary except for one.
 * Thats in the <code>Controller.IndicatorController.AddIndiListener</code>-class where another
 * case concerning this indicator must be added to the switch-instruction.
 * Only the {@link #IndicatorModel}-class calls up the methods of this interface. 
 * It enables the <code>IndicatorModel</code>-class to collect all the required data regarding 
 * the different indicators so that the view and the controller work correctly.
 * 
 * @author Benjamin Birner
 *
 */
interface Indicator extends Comparable<String>{
	
	
	/**
	 * must get the description of the indicator
	 * 
	 * @return the description of the indicator
	 */
	public String getDescription();
	
	
	
	/**
	 * must get all entries for the indicator to the chart ID <code>chartID</code> in a String-representation.
	 * This representation is displayed to the user.
	 * If there exists no entry, it must return <code>null</code>.
	 * 
	 * @param chartID the ID of the chart for which the information is required
	 * @return <code>String</code>-Array with all indicator entries as  a String-representation. <code>null</code> if there exists 
	 *         no entry for this ID.
	 */
	public String[] getAllDrawnIndisFor(int chartID);
	
	
	
	/**
	 * must updates all entries if a new candle is available.
	 * This method is used by the {@link #StockCandleData}. It calls up this method if a new
	 * candle is available.
	 * 
	 * @param chartIDs the ids of the charts to identify the entries that have to be updated.
	 * @param candle the latest candle that has just been generated.
	 */
	public void update(int[] chartIDs, Candle candle);
	
	
	/**
	 * must get the number of entries to the chart ID <code>chartID</code>.
	 * 
	 * @param chartID the ID of the chart to identify the entries.
	 * @return the number of entries.
	 */
	public int getNumberOfDrawnIndis(int chartID);
	
	
	
	/**
	 *must remove an single entry for the chart with the ID <code>chartID</code>.
	 *<code>index</code> defines the list-position of this entry that should be deleted.
	 * 
	 * @param index defines the list-position of the entry that should be deleted.
	 * @param chartID the ID of the chart to identify the entry.
	 */
	public void removeIndi(int chartID, int index);
	
	
	
	/**
	 *must remove the whole entry/all entries for the chart with the id <code>chartID</code>.
	 *This method is called up if a chart is closed to remove all entries that belong to the chart with
	 *the id <code>chartID</code>
	 * 
	 * @param chartID the id of the chart to identify the entries that belong to it.
	 */
	public void removeChart(int chartID);
	
	
	
	/**
	 * must get all data that belongs to the chart with the id <code>chartID</code> that are required to paint the indicators.
	 * This method is required in the <code>paint</code>-method.
	 * 
	 * @param chartID the id of the chart to identify the entries that belong to it.
	 * @return a {@link #IndicatorPaint}-object that are required to paint the indicators.
	 *         
	 */
	public IndicatorPaint getAllIndisToPaint(int chartID);
	
	
	
	/**
	 * must get all persistence data for the indicator concerning the chart ID that is defined by <code>chartID</code>.
	 * This method is called up in the store process.
	 * If there exists no entry it must return <code>null</code>.
	 * 
	 * @param chartID the ID of the chart for which the information is required.
	 * @return the parameters for all entries regarding this indicator in a {@link #PersistenceIndiParaData}-object
	 */
	public PersistenceIndiParaData getPersistenceDataFor(int chartID);
	
	
	/**
	 * must create the corresponding parameter-object to the indicator with the parameters that the 
	 * <code>String</code>-representation <code>parameters</code> contains.
	 * This method is called up during the restore process.
	 * 
	 * @param parameters the parameters as a <code>String</code>-representation that are necessary 
	 *                   to create the corresponding parameter-object.
	 *@return parameter-object that represents all parameters.                     
	 */             
	public IndicatorParameterPersistence createParameterObj(String parameters);
	
	
	
	/**
	 * must create and add a new entry according to the parameters that are defined by the
	 * {@link #IndicatorParameterPersistence}-object.
	 * It is called up in the program´s starting process and restores the indicator
	 * with this parameters.
	 * 
	 * @param chartID the id of the chart to which the entry belongs to.
	 * @param paras <code>IndicatorParameterPersistence</code>-object that contains the required parameters to calculate the indicator
	 * @param reso defines the resolution for the candles.
	 * @param data {@link #CandleRequestData}-object that contains the required 
	 *             close-prices to calculate the indicator.
	 */
	public void restore(int chartID, IndicatorParameterPersistence paras, String reso, CandleRequestData data);
	
	
	
	 /**
     * must get the default color for the indicator
     * 
     * @return defaultColor <code>Color</code>-object that defines the default color which 
     *                      is used in the chart to paint the indicator.
     */
	public Color getDefaultColor();
}
