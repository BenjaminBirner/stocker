package Model.indicatorModel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import Model.chart.Candle;
import Model.chart.CandleRequestData;



/**
 * This class belongs to the indicator "Bollinger Bands" and manages the communication
 * with all the {@link #ChartBB} objects.                                                  
 * This class implements the {@link #Indicator} interface so that the methods in the class
 * {@link #IndicatorModel} work correctly.
 * Furthermore, there exists no direct reference from any other class to this one apart from
 * the class {@link #IndicatorModel}.
 * 
 * @author Benjamin Birner
 *
 */
class BBData implements Comparable<String>, Indicator {
	
	private Color defaultColor = Color.ORANGE;
	private ArrayList<ChartBB> bbList = new ArrayList<>(20);
	private String description = "Bollinger Bands";

	
	
	
	@Override
	public String getDescription() {
		return description;
	}
	
   
	@Override
	public Color getDefaultColor() {
		return defaultColor;
	}

	/**
	 * gets all drawn Bollinger Bands for a chart ID to display them to the user.
	 * So, the shape is like this: BB(period,factor) --> GD(param)
	 * The method gets the correct {@link #ChartBB} object according to the chartID.
	 * If there exists no entry, it returns <code>null</code>. Otherwise it calls up its <code>ChartBB.createAllBBsAsStringRep()</code> method.
	 * 
	 * @param chartID the ID of the chart for which the information is required
	 * @return <code>String</code>-Array with all Bollinger Bands representations or <code>null</code> if there exists 
	 *         Bollinger Bands for this ID.
	 */
	@Override
	public String[] getAllDrawnIndisFor(int chartID) {                 
		int index = Collections.binarySearch(bbList,  chartID);
		if(index < 0) {
			String[] indis = {null};                                 
			return indis;
		}
		ChartBB objs = bbList.get(index);
		return objs.createAllBBsAsStringRep();
	}
	
	
	@Override
	public PersistenceIndiParaData getPersistenceDataFor(int chartID) {
		int index = Collections.binarySearch(bbList, chartID);
		if(index < 0) {
			return null;
		}
		return bbList.get(index).getPersistenceData();
	}
	
	
	/**
	 * creates a {@link #BBParameter} object with the parameters from a <code>String</code>-representation.
	 * This method is called up during the restore process.
	 * 
	 * @param parameters the parameters (MA-param, period, factor) that are necessary to create the  
	 * 					     {@link #BBParameter}-object in a <code>String</code>-representation in the following
	 *                       shape: MA-param/period/factor
	 *@return {@link #BBParameter} object that represents all parameters.                     
	 */             
	@Override
	public IndicatorParameterPersistence createParameterObj(String parameters) {
		return new BBParameter(parameters);
	}

	
	
	/**
	 *adds a new Bollinger Bands entry.
	 *It gets the correct {@link #ChartBB} object according to the chartID and calls up
	 *its <code>addBB(int period, double factor,  String reso, CandleRequestData data)</code> method.
	 *If there is no {@link #ChartBB} object for this ID, it creates a new one.
	 * 
	 * @param chartID the ID of the chart for which the information is required.
	 * @param reso defines the resolution for the candles.
	 * @param period the number of close prices to calculate a BB point
	 * @param factor the factor to calculate the BB-upper and BB-lower Band
	 * @param data the required candles to calculate the Bollinger Bands
	 */
	protected void addBB(int chartID, String reso, int period, double factor, CandleRequestData data) {
		int index = Collections.binarySearch(bbList,  chartID);
		if(index < 0) {
			bbList.add(-index-1, new ChartBB(chartID));
			index = -index-1;
		}
		bbList.get(index).addBB(period,factor, reso, data);
	}
	
	
	@Override
	public void update(int[] chartIDs, Candle candle) {
		int index;
		for(int i = 0; i < chartIDs.length; i++) {
			index = Collections.binarySearch(bbList,  chartIDs[i]);
			if(index >= 0) {
				bbList.get(index).update(candle.getClose());
			}
		}
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
	protected BBParameter[] getAllBBParaObjs(int chartID) {
		int index = Collections.binarySearch(bbList, chartID);
		if(index < 0) { 
			return null;
		}
		return bbList.get(index).createAllBBParaObjs();
	}
	
	@Override
	public int getNumberOfDrawnIndis(int chartID) {
		int index = Collections.binarySearch(bbList, chartID);
		if(index < 0) {
			return 0;
		}
		return bbList.get(index).size();
	}
	
	@Override
	public IndicatorPaint getAllIndisToPaint(int chartID) {
		int index = Collections.binarySearch(bbList, chartID);
		if(index < 0) {
			return null;
		}
		return bbList.get(index).getAllPaintData();
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
	protected boolean contains(int chartID, int gdPara, int period, double factor) {
		int index = Collections.binarySearch(bbList, chartID);
		if(index < 0) {
			return false;
		}
		return bbList.get(index).contains(gdPara,period,factor);
	}
	
	@Override
	public void removeIndi(int chartID, int index) {
		int i = Collections.binarySearch(bbList,  chartID);
		if(bbList.get(i).size() <= 1 ) {                                                               
			bbList.remove(i);                                                                                
		}else {
			bbList.get(i).removeBB(index);
		}	
	}
	
	@Override
	public void removeChart(int chartID) {
		int index = Collections.binarySearch(bbList,  chartID);
		if(index >= 0) {
			bbList.remove(index);
		}
	}
	
	
	
	@Override
	public int compareTo(String s) {
		return  description.compareTo(s);
	}
	
	@Override
	public void restore(int chartID, IndicatorParameterPersistence paras,String reso, CandleRequestData data) {
		BBParameter paraObj = (BBParameter) paras;
		addBB(chartID,reso, paraObj.getPeriod(), paraObj.getFactor(), data);
	}
	
}
