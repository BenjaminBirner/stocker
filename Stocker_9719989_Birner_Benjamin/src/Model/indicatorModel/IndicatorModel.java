package Model.indicatorModel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import org.junit.experimental.theories.Theories;

import Model.chart.Candle;
import Model.chart.CandleRequestData;


/**
 * This class manages the communication with the different <code>Indicator</code>-instances.
 * With regard to these instances, this class is the center of information because it is capable of
 * gathering all the required information and of giving it back in the required shape.
 * Consequently, the view and the controller merely have to know this class to receive all information
 * about the indicators.
 * 
 * @author Benjamin Birner
 *
 */
public class IndicatorModel {
	
	private ArrayList<Indicator> indiList = new ArrayList<>(4);
	
	public IndicatorModel() { 
		//here it is important that the indicators are sorted according to its description
		indiList.add(new BBData());
		indiList.add(new GDData());
	}

	
	
	/**
	 * this method is called up in the program´s starting process and restores the indicator
	 * <code>description</code> with the parameters that are defined by the {@link #IndicatorParameterPersistence}-object.
	 * 
	 * @param description the description that defines the indicator.
	 * @param chartID the id of the chart to which the entry belongs to.
	 * @param paras <code>IndicatorParameterPersistence</code>-object that contains the required parameters to calculate the indicator
	 * @param reso defines the resolution for the candles.
	 * @param data {@link #CandleRequestData}-object that contains the required 
	 *             close-prices to calculate the indicator.
	 */
	public void restore(String description,int chartID, IndicatorParameterPersistence paras, String reso, CandleRequestData data) {
		int index = Collections.binarySearch(indiList, description );
		Indicator indi = indiList.get(index);
		indi.restore(chartID, paras,reso, data);	
	}
	
	
	/** 
	 * gets all descriptions of the different <code>Indicator</code>-instances.
	 * The result is used in the view to display the descriptions.
	 * 
	 * @return descriptions <code>String</code>-array with all descriptions of the different <code>Indicator</code>-instances.
	 */
	public String[] getAllIndiDescriptions() {
		String[] descriptions = new String[indiList.size()];
		for(int i = 0; i < indiList.size(); i++) {
			descriptions[i] = indiList.get(i).getDescription();
		}
		return descriptions;
	}
	
	
	/**
	 * gets all existing entries concerning the different <code>Indicator</code>-instances to an 
	 * individual chart which is defined by the <code>chartID</code>.
	 * It calls up the <code>getAllDrawnIndisFor(int chartID)</code>-method on every <code>Indicator</code>-instance.
	 * The single Strings in the array are in a shape that is suitable to display them in the chart.
	 * 
	 * @param chartID the id of the chart to identify the entries that belong to it.
	 * @return <code>String</code>-array that contains all the String-representations. If there is no entry for this id,
	 *         it returns s1[0] = <code>null</code>.
	 */
	public String[] getAllDrawnIndis(int chartID) {
		String[] s1 = {null};
		String[] s2 = {null};                                       
		String[] s3 = {null};
		// this loop gets the existing entries to a defined chartId for every indicator.
		for(int i = 0; i < indiList.size(); i++) {
			s1 = indiList.get(i).getAllDrawnIndisFor(chartID);
			if(s1[0] != null) {
				if(s2[0] == null) {                          
					s2 = Arrays.copyOf(s1,  s1.length);
				}else {
					s3 = Arrays.copyOf(s2,  s2.length + s1.length);
					//fuses together the different result-arrays
					System.arraycopy(s1,  0,  s3,  s2.length,  s1.length);
				}
			}
		}
		//returns the correct array that contains the results
		if( s3[0] != null) { return s3;}
		if( s2[0] != null ) {return s2;}
		return s1;
	}
	
	
	
	/**
	 * gets the description that belongs to the indicator at position <code>index</code>.
	 * 
	 * @param index the position of this indicator in the <code>indiList</code>
	 * @return the description of this indicator
	 */
	public String getDescriptionAt(int index) {
		return indiList.get(index).getDescription();
	}
	
	
	/**
	 * gets the default color for the indicator <code>description</code>.
	 * This color is required to paint the indicator in the chart.
	 * 
	 * @param description defines the indicator of which the color is required.
	 * @return an <code>color</code>-object
	 */
	public Color getDefaultColorFor(String description) {
		int index = Collections.binarySearch(indiList, description);
		return indiList.get(index).getDefaultColor();
	}
	
	
	/**
	 * creates for the indicator <code>description</code> a {@link #IndicatorParameterPersistence}-object with
	 * the parameters that are defined in the String-representation <code>parameters</code>. 
	 * This method is used in the restore-process to create the corresponding <code>IndicatorParameterPersistence</code>-object
	 * from the String-representation from the properties-file.
	 * 
	 * @param description the description that defines the indicator.
	 * @param parameters String-representation that contains the parameters.
	 * @return the corresponding object to the indicator <code>description</code>. This object is of the interface-type
     *         {@link #IndicatorParameterPersistence}.
	 */
	public IndicatorParameterPersistence createParameterObj(String description, String parameters) {
		int index = Collections.binarySearch(indiList, description);
		return indiList.get(index).createParameterObj(parameters);
	}
	
	
	
	/**
	 * gets all data that belongs to the chart with the id <code>chartID</code> that are required to paint the indicators.
	 * This method calls up the <code>getAllIndisToPaint(int chartID)</code>-method on every <code>indicator</code>-object
	 * and stores the different returned {@link #IndicatorPaint}-objects in an array.
	 * This method is required in the <code>paint</code>-method.
	 * 
	 * @param chartID the id of the chart to identify the entries that belong to it.
	 * @return an array that contains the different {@link #IndicatorPaint}-objects that are required to paint the indicators.
	 *         The length of the array is always corresponding to the number of <code>indicator</code>-objects so that each has its 
	 *         fixed position. If there is no data available, the field of this position is <code>null</code>.
	 */
	public IndicatorPaint[] getAllIndisToPaintFor(int chartID) {
		IndicatorPaint[] objs = new IndicatorPaint[indiList.size()];
		for(int i = 0; i < indiList.size(); i++) {
			objs[i] = indiList.get(i).getAllIndisToPaint(chartID);
			if(objs[i] != null) {
				objs[i].setDescription(getDescriptionAt(i));
			}
		}
		return objs;
	}
	
	
	/**
	 * gets the relevant data that are required to restore the indicators for the chart with the id <code>chartID</code>.
	 * This data are represented by an object of the type {@link #PersistenceIndiParaData}.
	 * So, this method is called up in the store-process.
	 * 
	 * @param chartID the id of the chart to identify the entries that belong to it.
	 * @return list that contains the {@link #PersistenceIndiParaData}-objects for the indicators to which an entry to the
	 *         id <code>chartID</code> exists.
	 */
	public LinkedList<PersistenceIndiParaData> getPersistenceDataFor( int chartID){
		 LinkedList<PersistenceIndiParaData> paraList = new LinkedList<>();
		 for(int i = 0; i < indiList.size(); i++) {
			 PersistenceIndiParaData para = indiList.get(i).getPersistenceDataFor(chartID);
			 if(para != null) {
				 paraList.addLast(para);
			 }
		 }
		 return paraList;
	}
	
	
	
	/**
	 * updates all <code>indicator</code>-objects if a new candle is available.
	 * It calls up the <code>update(int[] chartIDs, Candle candle)</code>-method on every 
	 * <code>indicator</code>-object. 
	 * This method is used by the {@link #StockCandleData}. It calls up this method if a new
	 * candle is available.
	 * 
	 * @param chartIDs the ids of the charts to identify the entries that have to be updated.
	 * @param candle the latest candle that has just been generated.
	 */
	public void updateIndi(int[] chartIDs, Candle candle) { 
		for(Indicator indi : indiList) {
			indi.update(chartIDs, candle);
		}
	}
	
	
	
	/**
	 *removes an entry from the corresponding <code>indicator</code>-object for the chart with the id <code>chartID</code>.
	 *<code>index</code> defines the position of the entry that should be deleted considering all <code>indicator</code>-objects
	 *because <code>index</code> refers to the selected list-index in the {@link #IndicatorDialog}-class.
	 * 
	 * @param index defines the position of the entry that should be deleted considering all <code>indicator</code>-objects.
	 * @param chartID the id of the chart to identify the entries that belong to it.
	 */
	public void removeIndiAt(int index, int chartID) {
		int i = index;
		int j = 0;
		int k = index;
		Indicator indi = null;
		//this loop searches in the indiList for the Indicator-Object that contains the entry that should be deleted
		while( i >= 0 ) {
			indi = indiList.get(j);
			int size = indi.getNumberOfDrawnIndis(chartID);
			k = i;
			i = i - size;
			j = j + 1;
		}
		if (indi != null) {
			indi.removeIndi(chartID, k);
		}
	}
	
	
	/**
	 *removes the entry for the chart with the id <code>chartID</code> in every <code>indicator</code>-object.
	 *This method is called up if a chart is closed to remove all entries that belong to the chart with
	 *the id <code>chartID</code>
	 * 
	 * @param chartID the id of the chart to identify the entries that belong to it.
	 */
	public void removeChart(int chartID) {
		for(Indicator indi : indiList) {
			indi.removeChart(chartID);
		}
	}
	
	
	
	/**
	 *removes the entry concerning the <code>description</code>-indicator for the chart that is defined by
	 *<code>chartID</code>.
	 *This method is called up if the resolution in a chart has just changed to delete the old entry.
	 * 
	 * @param description defines the indicator.
	 * @param chartID the id of the chart to identify the entries that belong to it.
	 */
	public void removeChartForIndi(String description, int chartID) {
		int index = Collections.binarySearch(indiList, description);
		indiList.get(index).removeChart(chartID);
	}
	
	
	
	
	
	private GDData getGDData() {
		int index = Collections.binarySearch(indiList, "Moving Average");
		return (GDData) indiList.get(index);
	}
	
	
	/**
	 * adds a MA-entry concerning the chart with the id <code>chartID</code>.
	 * 
	 * @param chartID chartID the id of the chart to which a MA should be added.
	 * @param reso defines the resolution for the candles.
	 * @param para the parameter for the period of the MA.
	 * @param data {@link #CandleRequestData}-object that contains the required 
	 *             close-prices to calculate the MA
	 */
	public void addGD(int chartID, String reso, int para, CandleRequestData data) {
		getGDData().addGD(chartID,reso, para, data);
	}

	
	/** 
	 * checks if there already exists a {@link #MovingAverage} object
	 * with the period <code>period</code>.
	 * 
	 * @param chartID the ID of the chart for which the information is required.
	 * @param period the period of the Moving Average
	 * @return true if there exists an object with this parameter. Otherwise false.
	 */
	public boolean containsGD(int chartID, int period) {
		return getGDData().contains(chartID, period);
	}
	
	
	/**
	 * gets all MA-parameters for the chart that is defined by the id <code>chartID</code>.
	 * 
	 * @param chartID the id of the chart to identify the entries that belong to it.
	 * @return <code>int</code>-array with the periods.
	 */
	public int[] getAllGDs(int chartID) {
		return getGDData().getAllGDs(chartID);
	}
	
	
	/**
	 * gets all MA´s for the chart that is defined by the id <code>chartID</code> as a String-representation
	 * which has the following shape: GD + parameter.
	 * The results of this method are used in the {@link #BBDialog} to display it.
	 * 
	 * @param chartID the id of the chart to identify the entries that belong to it.
	 * @return <code>String</code>-array with the String-representations.
	 */
	public String[] getAllDrawnGDs(int chartID) {
		String[] s = getGDData().getAllDrawnIndisFor(chartID);
		if( s[0] == null) {
			String[] empty = {};
			return empty;
		}
		return s;
	}
	
	
	/**
	 * calculates the MA with the period <code>n</code> for the close prices that are in the <code>stockData</code> array.
	 * This method is only called up in the {@link #StockerTesterImpl} class.
	 * 
	 * @param n defines the period
	 * @param stockData contains the close prices to calculate the MA
	 * @return <code>double</code> array with the calculated MA-points
	 */
	public double[] calculateGDTest(int n, double[] stockData) {
		ChartGD chartGD = new ChartGD(-1);
		return chartGD.calculateGDTest(n,stockData);
	}
	
	
	
	
	private BBData getBBData() {
		int index = Collections.binarySearch(indiList, "Bollinger Bands");
		return (BBData) indiList.get(index);
	}
	

	
	/**
	 *adds a Bollinger-Bands-entry for the chart with the id <code>chartID</code>.
	 * 
	 * @param chartID the id of the chart to which a BBs should be added.
	 * @param period the parameter for the period of the BBs.
	 * @param factor the factor to calculate the upper- and lower Bands
	 * @param data {@link #CandleRequestData}-object that contains the required 
	 *             close-prices to calculate the BBs.
	 */
	public void addBB(int chartID,String reso, int period, double factor, CandleRequestData data) {
		getBBData().addBB(chartID,reso, period, factor, data);
	}
	
	
	
	/**
	 * gets all BBs-entries for the chart that is defined by the id <code>chartID</code>.
	 * The parameters for each BBs-entry are represented by a {@link #BBParameter}-object.
	 * This method is called up to check if a candle-request is necessary.
	 * 
	 * @param chartID the id of the chart to identify the entries that belong to it.
	 * @return <code>BBParameter</code>-array that contains the parameters for each BBs-entry.
	 *         If there is no entry it returns <cod>null</code>.
	 */
	public BBParameter[] getAllBBParaObjs(int chartID) {
		return getBBData().getAllBBParaObjs(chartID);
	}
	
	
	/**
	 * checks if a Bollinger-Bands-entry which is defined by the parameters <code>gdPara</code>,
	 * <code>period</code> and <code>factor</code> already exists.
	 * 
	 * @param chartID the id of the chart to identify the entries that belong to it.
	 * @param gdPara the parameter for the period of the MA.
	 * @param period the parameter for the period of the BBs.
	 * @param factor the factor for the upper- and lower Bands
	 * @return true if such an entry exists. Otherwise false.
	 */
	public boolean containsBB(int chartID, int gdPara, int period, double factor) {
		return getBBData().contains(chartID,gdPara,period,factor);
	}
	
	
	
	
	/**
	 * calculates the upper-BB-points with the period <code>n</code> and the factor <code>f</code> for the close prices that are in the <code>stockData</code> array.
	 * This method is only called up in the {@link #StockerTesterImpl} class.
	 * 
	 * @param n defines the period
	 * @param f defines the factor
	 * @param stockData contains the close prices to calculate the upper-BB-points
	 * @return <code>double</code> array with the calculated upper-BB-points
	 */
	public double[] calculateUpperBBTest(double f, int n, double[] stockData) {
		ChartBB chartBB = new ChartBB(-1);
		return chartBB.calculateUpperBBTest(f,n,stockData);
	}
	
	
	/**
	 * calculates the lower-BB-points with the period <code>n</code> and the factor <code>f</code> for the close prices that are in the <code>stockData</code> array.
	 * This method is only called up in the {@link #StockerTesterImpl} class.
	 * 
	 * @param n defines the period
	 * @param f defines the factor
	 * @param stockData contains the close prices to calculate the lower-BB-points
	 * @return <code>double</code> array with the calculated lower-BB-points
	 */
	public double[] calculateLowerBBTest(double f, int n, double[] stockData) {
		ChartBB chartBB = new ChartBB(-1);
		return chartBB.calculateLowerBBTest(f,n,stockData);
	}
	
}
