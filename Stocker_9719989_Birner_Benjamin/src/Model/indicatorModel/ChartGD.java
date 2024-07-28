package Model.indicatorModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import Model.chart.Candle;
import Model.chart.CandleConverter;
import Model.chart.CandleRequestData;
import View.chart.Chart;



/**
 * This class contains all the methods to calculate and update the Moving Average and
 * to manage the {@link #MovingAverage} objects. Furthermore, an instance of this class 
 * contains all the close prices that are necessary to calculate and update the Moving Average.
 * Every instance belongs to exactly one chart. Only the class {@link #GDData} has access to this class.
 * It is also important to know that the required close prices are held in an array and if a new 
 * price is available, this price overwrites the oldest one in the array so that the array must never be resized.
 * 
 * @author Benjamin Birner
 *
 */
class ChartGD implements Comparable<Integer> {

	private final Integer chartID;
	
	private LinkedList<Double> closeList = new LinkedList<Double>();
	private ArrayList<MovingAverage> gdObjList = new ArrayList<>(5);
	
	protected ChartGD(int chartID) {
		this.chartID = chartID;
	}
	
	
	/**
	 * creates the Moving Averages as a <code>String</code>-representation in the following shape:
	 * "GD" + "param".
	 * This representation is used in the view to display it to the user
	 * 
	 * @return gds <code>String</code> Array with the representations
	 */
	protected String[] createAllGDsAsStringRep() {
		String[] gds = new String[gdObjList.size()];
		Integer gd;
		for(int i = 0; i < gdObjList.size(); i++) {
			gd = gdObjList.get(i).getParameter();
			gds[i] = "GD" + gd.toString();
		}
		return gds;
	}
	
	
	
	/**
	 * gets all the persistence data.
	 * This method is important in the store-process.
	 * It builds up a {@link #PersistenceIndiParaData} object
	 * 
	 * @return {@link #PersistenceIndiParaData} object that contains all the required information
	 *         to rebuild the instance of this class.
	 */
	protected PersistenceIndiParaData getPersistenceData() {
		PersistenceIndiParaData per = new PersistenceIndiParaData("Moving Average");
		for(int i = 0; i < gdObjList.size(); i++) {
			MovingAverage gd = gdObjList.get(i);
			GDParameter para = new GDParameter(gd.getParameter());
			per.addFirst(para);
		}
		return per;
	}
	
	
	
	/**
	 * adds an new Moving Average entry according to the parameter.
	 * It calls up the {@link #calculateGD(int para, int number, double[] data)}
	 * method to calculate all the MA-points.
	 * It also calls up the {@link #setClose(int para, CandleRequestData data)}
	 * method to set the new close prices.
	 * If the resolution is 10min or 240min, it converts the candles to the expected resolution.
	 * 
	 * @param para the number of close prices to calculate a MA point.
	 * @param reso defines the resolution for the candles.
	 * @param data a {@link #CandleRequestData} object that contains all the required close prices
	 */
	protected void addGD(int para, String reso, CandleRequestData data) {
		
		int index = Collections.binarySearch(gdObjList, para);
		double[] close = null;
		//checks the resolution. If the resolution is 10min or 240min,
		//it is necessary to convert the candles.
		if( !(reso.equals("10")) && !(reso.equals("240"))){
			close = new double[data.size()-1];
			//extracts the close prices and saves them in an Array
			for(int i = 0; i < data.size()-1; i++) {
				close[i] = data.getCloseAt(i);
			}
		}else {
			LinkedList<Candle> candleList;
			if(reso.equals("10")) {
				candleList = CandleConverter.convertCandlesToCandles(data,600,Chart.ELEM_NUMBER + para -1, "symb","reso");
			}else {
				candleList = CandleConverter.convertCandlesToCandles(data,14400, Chart.ELEM_NUMBER + para -1,"symb","reso");
			}
			close = new double[candleList.size()-1];
			Iterator<Candle> it = candleList.iterator();
			//extracts the close prices and saves them in an Array
			for(int i = 0; i < candleList.size()-1; i++) {
				close[i] = it.next().getClose();
			}
		}
		LinkedList<Double> list = calculateGD(para, Chart.ELEM_NUMBER,close);
		//sets the new pointlist to an existing object or creates a new one
		if(index >= 0) {
			gdObjList.get(index).setPointList(list);
		}else {
			gdObjList.add(-index-1, new MovingAverage(para,list));
		}
		setClose(para,data);
	}
	
	
	
	/**
	 * gets all Moving Averages respectively the parameters.
	 * 
	 * @return <code>int</code> array that contains all the parameters.
	 */
	protected int[] getAllGDs() {                   
		int[] gds = new int[gdObjList.size()];
		
		for(int i = 0; i < gdObjList.size(); i++) {
			gds[i] = gdObjList.get(i).getParameter();
		}
		return gds;
	}
	
	
	
	/**
	 * gets all the data that is required to paint the Moving Average.
	 * It builds up a {@link #IndicatorPaint} object which contains all this data.
	 * The result of this method is required to paint the MA.
	 * 
	 * @return the <code>IndicatorPaint</code> object
	 */
	protected IndicatorPaint getAllPaintData() {
		String[] disp = new String[gdObjList.size()];
		double[][] pointAr = new double[gdObjList.size()][];
		for(int i = 0; i < gdObjList.size(); i++) {
			MovingAverage obj = gdObjList.get(i);
			disp[i] = obj.getDisplayDescription();
			pointAr[i] = obj.getAllPoints();
		}
		IndicatorPaint indiO = new IndicatorPaint(disp,pointAr);
		return indiO;
	}
	
	
	/**
	 * sets the number of close prices that are required to calculate the Moving Average.
	 * This private method is only called up by the 
	 * {@link #addGD(int para, CandleRequestData data)} method.
	 * 
	 * @param period the number of close prices to calculate a MA point.
	 * @param data the data that contains the required close prices.
	 */
	private void setClose(int period, CandleRequestData data){
		if(period > closeList.size()) {
			closeList.clear();
			for (int i = data.size()-2; i > data.size()-2-period; i--) {
				closeList.addFirst(data.getCloseAt(i));
			}
		}
	}
	
	
	
	/**
	 * removes an entry according to the parameter <code>index</code> and maintains the order in the list.
	 * If there are close prices after the deletion that are no longer required, this method removes these elements. 
	 * 
	 * @param the index of the list <code>gdObjList</code> to delete the object.
	 */
	protected void removeGD(int index) {  
		//true means, that the object with the greatest parameter is going to be
		//deleted. So, there are close prices that are no longer required. This block deletes these prices.
		if( index == gdObjList.size()-1) {
			int para = gdObjList.get(gdObjList.size()-2).getParameter();
			while(closeList.size() > para) {
				closeList.removeFirst();
			}
		}
		//removes the entry
		gdObjList.remove(index);
	}
	
	
	/**
	 * updates the Moving Average entries if a new close price is available.
	 * This method calculates the new MA point and sets this point.
	 * 
	 * @param newClose the latest close price that is available.
	 */
	protected void update(double newClose) {                       
		//this loop gets all MovingAverage-objects from the gdObjList and updates them
		for(int i = 0; i < gdObjList.size(); i++) {
			MovingAverage obj = gdObjList.get(i);
			int para = obj.getParameter();
			double lastPoint = obj.getLastPoint();
			//reconstructs the required sum with the help of the last GD-point
			double sum = lastPoint * para - closeList.get(closeList.size()-para) + newClose;
			obj.setNewPoint(sum / para);
		}
		//updates the closeList that contains all the close prices
		closeList.removeFirst();
		closeList.addLast(newClose);
	}
	
	
	
	/**
	 * calculates the MA-points according to the parameter.
	 * This private method is only called up by the {@link #addGD(int para, String reso, CandleRequestData data)} method.
	 * 
	 * @param para the parameter of the period for the Moving Average.
	 * @param number defines the number of MA-points that have to be calculated.
	 * @param data the data that contains the required close prices.
	 * @return <code>LinkedList</code> that contains the GD-points.
	 */
	private LinkedList<Double> calculateGD(int para, int number, double[] data){    
		LinkedList<Double> list = new LinkedList<Double>();
		double sum = 0;
		int size = data.length;
		if(!(size < para)) {
			//this loop sums all the close prices that are required to calculate the Moving Average
			for(int i = size-1; i > size-para-1; i--) {
				sum = sum + data[i];
			}
			//adds the latest MA-point
			list.addFirst(sum/para);
			//initializes the required variables to calculate the remaining MA-points
			int k = size - para -1;
			int l = size - 1;
			//this loop calculates the remaining MA-points and adds these points to the list.
			for(int j = 1; j < number; j++) {                                                                           
				sum = sum - data[l] + data[k];
				list.addFirst(sum/para);
				k = k-1;
				l = l-1;
			}
		}
		return list;
	}
	
	
	
	/**
	 * gets the size of the <code>gdObjList</code> list
	 * 
	 * @return the size of the <code>gdObjList</code> list
	 */
	protected int size() {
		return gdObjList.size();
	}
	
	
	
	/** 
	 * checks if there already exists a {@link #MovingAverage} object
	 * with the period <code>period</code>.
	 * 
	 * @param period the period of the Moving Average
	 * @return true if there exists an object with this parameter. Otherwise false.
	 */
	protected boolean contains(int period) {
		for(MovingAverage o : gdObjList) {
			if(o.getParameter() == period) {
				return true;
			}
		}
		return false;
	}
	
	
	@Override
	public int compareTo(Integer i) {
		return chartID.compareTo(i);
	}
	
	/**
	 * calculates the MA with the period <code>n</code> for the close prices that are in the <code>stockData</code> array.
	 * This method is only used to test the MA-calculation.
	 * It calls up the {@link #calculateGD(int para)} method to test it.
	 * 
	 * @param n defines the period
	 * @param stockData contains the close prices to calculate the MA
	 * @return <code>double</code> array with the calculated MA-points
	 */
	protected double[] calculateGDTest(int n, double[] stockData) {
		int number = stockData.length - n + 1;
		double[] gds = new double[number];
		LinkedList<Double> gdList = calculateGD(n,number,stockData);
		Iterator<Double> it = gdList.iterator();
		if(gds.length > 0) {
			for(int i = 0; i < gdList.size(); i++) {
				gds[i] = it.next();
			}
		}
		return gds;
	}

}
