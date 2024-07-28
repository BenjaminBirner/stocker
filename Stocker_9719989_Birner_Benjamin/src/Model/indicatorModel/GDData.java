package Model.indicatorModel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import Model.chart.Candle;
import Model.chart.CandleRequestData;
import View.chart.Chart;



/**
 * This class belongs to the indicator "Moving Average" and manages the communication
 * with all the {@link #ChartGD} objects.                                                  
 * This class implements the {@link #Indicator} interface so that the methods in the class
 * {@link #IndicatorModel} work correctly.
 * Furthermore, there exists no direct reference from any other class to this one apart from
 * the class {@link #IndicatorModel}.
 * 
 * @author Benjamin Birner
 *
 */
class GDData implements Indicator, Comparable<String> {
	
	private Color defaultColor = Color.BLUE;
	private ArrayList<ChartGD> gdList = new ArrayList<>(20);
	private String description = "Moving Average";
	
	
	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public Color getDefaultColor(){
		return defaultColor;
	}
	
	@Override
	public String[] getAllDrawnIndisFor(int chartID) {
		int index = Collections.binarySearch(gdList,  chartID);
		if(index < 0) {
			String[] indis = {null};                          
			return indis;
		}
		ChartGD objs = gdList.get(index);
		return objs.createAllGDsAsStringRep();
		
	}
	
	@Override
	public PersistenceIndiParaData getPersistenceDataFor(int chartID) {
		int index = Collections.binarySearch(gdList, chartID);
		if(index < 0) {
			return null;
		}
		return gdList.get(index).getPersistenceData();
	}
	
	@Override
	public IndicatorParameterPersistence createParameterObj(String parameters) {
		return new GDParameter(parameters);
	}
	

	@Override
	public IndicatorPaint getAllIndisToPaint(int chartID) {
		int index = Collections.binarySearch(gdList, chartID);
		if(index < 0) {
			return null;
		}
		return gdList.get(index).getAllPaintData();
	}
	
	
	
	
	/**
	 *adds a new Moving Average entry.
	 *It gets the correct {@link #ChartGD} object according to the chartID and calls up
	 *its <code>addGD(int para, String reso, CandleRequestData data)</code> method.
	 *If there is no {@link #ChartGD}-object for this ID, it creates a new one.
	 * 
	 * @param chartID the ID of the chart for which the information is required.
	 * @param reso defines the resolution for the candles.
	 * @param para the period of the Moving Average.
	 * @param data the required candles to calculate the MA.
	 */
	protected void addGD(int chartID, String reso, int para, CandleRequestData data) {
		int index = Collections.binarySearch(gdList,  chartID);
		if(index < 0) {
			gdList.add(-index-1, new ChartGD(chartID));
			index = -index-1;
		}
		gdList.get(index).addGD(para,reso, data);
	}
	
	
	/** 
	 * checks if there already exists a {@link #MovingAverage} object
	 * with the period <code>period</code>.
	 * 
	 * @param chartID the ID of the chart for which the information is required.
	 * @param period the period of the Moving Average
	 * @return true if there exists an object with this parameter. Otherwise false.
	 */
	protected boolean contains(int chartID, int period) {
		int index = Collections.binarySearch(gdList,  chartID);
		if(index < 0) {
			return false;
		}
		return gdList.get(index).contains(period);
	}
	
	
	
	@Override
	public void update(int[] chartIDs, Candle candle) {
		int index;
		for(int i = 0; i < chartIDs.length; i++) {
			index = Collections.binarySearch(gdList,  chartIDs[i]);
			if(index >= 0) {
				gdList.get(index).update(candle.getClose());
			}
		}
	}
	
	
	
	/**
	 * gets the period for each entry to the id <code>chartID</code>.
	 * 
	 * @param chartID the ID of the chart for which the information is required.
	 * @return <code>int</code>-array that contains the periods of all entries.
	 */
	protected int[] getAllGDs(int chartID) {
		int index = Collections.binarySearch(gdList, chartID);
		if(index < 0) {
			int[] gds = {-1}; 
			return gds;
		}
		return gdList.get(index).getAllGDs();
	}
	
	@Override
	public int getNumberOfDrawnIndis(int chartID) {
		int index = Collections.binarySearch(gdList, chartID);
		if(index < 0) {
			return 0;
		}
		return gdList.get(index).size();
	}
	
	@Override
	public void removeIndi(int chartID, int index) {
		int i = Collections.binarySearch(gdList,  chartID);
		if(gdList.get(i).size() <= 1 ) {                     
			gdList.remove(i);                               
		}else {
			gdList.get(i).removeGD(index);
		}	
	}
	
	@Override
	public void removeChart(int chartID) {
		int index = Collections.binarySearch(gdList,  chartID);
		if(index >= 0) {
			gdList.remove(index);
		}
	}
	
	
	
	@Override
	public int compareTo(String s) {
		return  description.compareTo(s);
	}

	@Override
	public void restore(int chartID, IndicatorParameterPersistence paras, String reso, CandleRequestData data) {
		GDParameter para = (GDParameter) paras;
		addGD(chartID,reso, para.getParameter(),data);
	}
}
