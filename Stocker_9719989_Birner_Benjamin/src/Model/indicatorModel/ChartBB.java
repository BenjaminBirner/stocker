package Model.indicatorModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import Model.chart.Candle;
import Model.chart.CandleConverter;
import Model.chart.CandleRequestData;
import View.chart.Chart;


/**
 * This class contains all the methods to calculate and update the Bollinger Bands and
 * to manage the {@link #BollingerBands} objects. Furthermore, an instance of this class 
 * contains all the close prices that are necessary update the Bollinger Bands.
 * Every instance belongs to exactly one chart. Only the class {@link #BBData} has access to this class.
 * 
 * @author Benjamin Birner
 *
 */
public class ChartBB implements Comparable<Integer> {

	private final Integer chartID;
	
	private LinkedList<Double> closeList = new LinkedList<Double>();
	private ArrayList<BollingerBands> bbObjList = new ArrayList<BollingerBands>(3);
	
	
	
	protected ChartBB(int chartID) {
		this.chartID = chartID;
	}
	
	/**
	 * creates the Bollinger Bands as a <code>String</code>-representation in the following shape:
	 * BB(period,factor) --> GD(param).
	 * This representation is used in the view to display it to the user
	 * 
	 * @return <code>String</code> Array with the representations
	 */
	protected String[] createAllBBsAsStringRep() {
		String[] bbs = new String[bbObjList.size()];
		for(int i = 0; i < bbObjList.size(); i++) {
			BollingerBands obj = bbObjList.get(i);
			Double fac = obj.getFactor();
			Integer per = obj.getPeriod();
			Integer gd = obj.getGDParameter();
			bbs[i] = "BB(" + fac.toString() + "," + gd.toString() + ")";
		}
		return bbs;
	}
	
	
	/**
	 * builds up the {@link #BBParameter} objects.
	 * This objects are required to check if there are enough close-prices by
	 * checking the parameters and in the store and restore process.
	 * 
	 * 
	 * @return array with the {@link #BBParameter} objects
	 */
	protected BBParameter[] createAllBBParaObjs() {
		BBParameter[] objs = new BBParameter[bbObjList.size()];
		int j = 0;
		for(int i = objs.length-1; i >= 0; i--) {
			BollingerBands bb = bbObjList.get(i);
			int gdPara = bb.getGDParameter();
			int per = bb.getPeriod();
			double fac = bb.getFactor();
			objs[j] = new BBParameter(gdPara, per, fac);
			j = j+1;
		}
		return objs;
	}
	
	
	/**
	 * gets all the persistence data.
	 * This method is important in the store-process.
	 * It builds up a {@link #PersistenceIndiParaData} object.
	 * 
	 * @return {@link #PersistenceIndiParaData}-object that contains all the required information
	 *         to rebuild the instance of this class.
	 */
	protected PersistenceIndiParaData getPersistenceData() {
		PersistenceIndiParaData per = new PersistenceIndiParaData("Bollinger Bands");
		for(int i = 0; i < bbObjList.size(); i++) {
			BollingerBands bb = bbObjList.get(i);
			BBParameter para = new BBParameter(bb.getGDParameter(),bb.getPeriod(),bb.getFactor());
			per.addFirst(para);
		}
		return per;
	}
	
	
	/**
	 * gets all the data that is required to paint the Bollinger Bands.
	 * It builds up a {@link #IndicatorPaint} object which contains all this data.
	 * The result of this method is required to paint the BBs.
	 * 
	 * @return the <code>IndicatorPaint</code> object
	 */
	protected IndicatorPaint getAllPaintData() {
		String[] disp = new String[bbObjList.size()];
		double[][] pointAr = new double[bbObjList.size() * 2][];
		int j = 0;
		for(int i = 0; i < bbObjList.size(); i++) {
			BollingerBands obj = bbObjList.get(i);
			disp[i] = obj.getDisplayDescription();
			pointAr[j] = obj.getAllUpperPoints();
			pointAr[j+1] = obj.getAllLowerPoints();
			j = j + 2;
		}
		IndicatorPaint indiO = new IndicatorPaint(disp,pointAr);
		return indiO;
	}
	
	
	/** 
	 * checks if there already exists a {@link #BollingerBands} object
	 * with these parameters.
	 * 
	 * @param gdPara the parameter of the moving average
	 * @param period the number of close prices to calculate a BB point
	 * @param factor the factor to calculate the BB-upper and BB-lower Band
	 * @return true if there exists an object with these parameters. Otherwise false.
	 */
	protected boolean contains(int gdPara, int period, double factor) {
		for(int i = 0; i < bbObjList.size(); i++) {
			BollingerBands obj = bbObjList.get(i);
			if(obj.getGDParameter() == gdPara && obj.getPeriod() == period && obj.getFactor() == factor) {
				return true;
			}
		}
		return false;
	}
	
	
	
	/**
	 * adds an new Bollinger Bands entry according to the defined parameters.
	 * It calls up the {@link #calculateBB(int period, double factor, int number, double[] data)} method 
	 * to calculate all the points for the upper and lower Band.
	 * It also calls up the {@link #setClose(int period, CandleRequestData data)}-method to set the new close prices.
	 * If the resolution is 10min or 240min, it converts the candles to the expected resolution.
	 * 
	 * @param period the number of close prices to calculate a BB point
	 * @param factor the factor to calculate the BB-upper and BB-lower Band
	 * @param data a {@link #CandleRequestData} object that contains all the required close prices
	 */
	protected void addBB(int period, double factor, String reso, CandleRequestData data) {
		
		int index = Collections.binarySearch(bbObjList, period);
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
				candleList = CandleConverter.convertCandlesToCandles(data,600,Chart.ELEM_NUMBER + period,"symb","reso");
			}else {
				candleList = CandleConverter.convertCandlesToCandles(data,14400,Chart.ELEM_NUMBER + period,"symb","reso");
			}
			close = new double[candleList.size()-1];
			Iterator<Candle> it = candleList.iterator();
			//extracts the close prices and saves them in an Array
			for(int i = 0; i < candleList.size()-1; i++) {
				close[i] = it.next().getClose();
			}
		}
		//calculates the BB
		BollingerBands bb = calculateBB(period, factor, Chart.ELEM_NUMBER, close);
		
		//adds the entry at the correct position
		if(index < 0) {
			bbObjList.add(-index-1,bb);
		}else {
			bbObjList.add(index,bb);
		}
		
	    //sets the close prices
		setClose( period, data);
	}
	
	
	
	/**
	 * removes an entry according to the parameter <code>index</code>.
	 * If there are close prices after the deletion that are no longer required, this method removes these elements. 
	 * 
	 * @param the index of the list <code>bbObjList</code> to delete the object.
	 */
	protected void removeBB(int index) {  
		//true means, that the object with the greatest parameter is going to be
		//deleted. So, there are close prices that are no longer required. This block deletes these prices.
		if( index == bbObjList.size()-1) {
			int per = bbObjList.get(bbObjList.size()-2).getPeriod();
			while(closeList.size() > per) {
				closeList.removeFirst();
			}
		}
		bbObjList.remove(index);
	}
	
	
	
	/**
	 * sets the number of close prices that are required to calculate the Bollinger Bands.
	 * This private method is only called up by the 
	 * {@link #addBB(int gdPara, int period, double factor, CandleRequestData data)} method.
	 * 
	 * @param period the number of close prices to calculate a BB point
	 * @param data all the data that includes the required close prices
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
	 * calculates the Bollinger Bands according to the parameters.
	 * This private method is only called up by the {@link #add(int gdPara, int period, double factor)} method.
	 * 
	 * @param period the number of close prices to calculate a BB point
	 * @param factor the factor to calculate the BB-upper and BB-lower Band
	 * @param number defines the number of MA-points that have to be calculated.
	 * @param data a double-Array that contains all the required close prices
	 * @return an {link #BollingerBands} object that contains all the relevant information.
	 */
	private BollingerBands calculateBB(int period, double factor, int number, double[] data){
		//initializes all the required variables
		LinkedList<Double> bbUpperList = new LinkedList<Double>();
		LinkedList<Double> bbLowerList = new LinkedList<Double>();
		
		double sumGD = 0;
		double sumBB = 0;
		int size = data.length;
		//this loop sums all the close prices respectively the squared close prices that
		//are required to calculate the MA and BB
		for(int i = size-1; i > size-period-1; i--) {
			double close = data[i];
			sumGD = sumGD + close;
			sumBB = sumBB + (close * close);
		}
		//saves the calculated values
		double latestGDSum = sumGD;
		double latestBBSum = sumBB;
		//calculates the Moving Average
		double gd = sumGD / period;                       
		//calculates the standard deviation
		double sig = Math.sqrt( (sumBB / period) - (gd * gd));
		//calculates and adds the latest BB point
		double bbUpp = gd + (factor * sig);
		double bbLow = gd - (factor * sig);
		//adds the calculated points to the corresponding list
		bbUpperList.addFirst(bbUpp);
		bbLowerList.addFirst(bbLow);
		//initializes the required variable
		int k = size - period - 1;
		//It iterates exactly until all required upper and lower BB-Points are calculated
		for(int j = size-1; j > size-number; j--) { 
			//calculates the sums for the point that is calculated
			sumGD = sumGD - data[j] + data[k];
			sumBB = sumBB - (data[j] * data[j]) + (data[k] * data[k]);
			//calculates the Moving Average
			gd = sumGD / period;
			//calculates the standard deviation
			sig = Math.sqrt( (sumBB / period) - (gd * gd));
			//calculates the latest BB-points
			bbUpp = gd + (factor * sig);
			bbLow = gd - (factor * sig);
			//adds the calculated BB-points to the corresponding list
			bbUpperList.addFirst(bbUpp);
			bbLowerList.addFirst(bbLow);
			//decrements the variable
			k = k - 1;
		}
		//a new BollingerBands object is created
		BollingerBands bbObj = new BollingerBands(period,period,factor,latestGDSum,latestBBSum,bbUpperList,bbLowerList);
		return bbObj;	
	}
		
	

	
	
	/**
	 * updates the Bollinger Bands entries if a new close price is available.
	 * This method calculates the new Bollinger Bands points and sets these points 
	 * to the correct {link #BollingerBands} object.
	 * It also updates the <code>closeList</code>.
	 * 
	 * @param newClose the latest close price that is available
	 */
	protected void update(double newClose) {
		
		for(int i = 0; i < bbObjList.size(); i++) {
			BollingerBands obj = bbObjList.get(i);
			//gets all the required data to update the point-list
			int per = obj.getPeriod();
			double fac = obj.getFactor();
			// initializes the required variables
			double sumGD = obj.getLatestGDSum();
			double sumBB = obj.getLatestBBSum();
			//gets the last close-price that must be subtracted
			double lastClose = closeList.get(closeList.size() - per);
			//adds the newClose-price and subtracts the lastClose-price to calculate the new sums
			sumGD = sumGD + newClose - lastClose;
			sumBB = sumBB + (newClose * newClose) - (lastClose * lastClose);
			sumBB = Math.rint(sumBB * 1000000) / 1000000;
			//calculates the GD
			double gd = sumGD/per;
			double sq = gd*gd;
			sq = Math.rint(sq * 1000000) / 1000000;
			//calculates the standard deviation;
			double sig = Math.sqrt((sumBB / per) - sq);
			//adds the new upper and lower points.
			obj.addNewUpperPoint(gd + (fac * sig));
			obj.addNewLowerPoint(gd - (fac * sig));
			//sets the new sums
			obj.setLatestGDSum(sumGD);
			obj.setLatestBBSum(sumBB);
		}
		
		//updates the closeList
		closeList.removeFirst();
		closeList.addLast(newClose);
	}
	
	
	/**
	 * gets the size of the <code>bbObjList</code> list
	 * 
	 * @return the size of the <code>bbObjList</code> list
	 */
	protected int size() {
		return bbObjList.size();
	}

	
	
	@Override
	public int compareTo(Integer i) {
		return chartID.compareTo(i);
	}
	
	
	
	/**
	 * calculates the upper-BB-points with the period <code>n</code> and the factor <code>f</code> for the close prices that are in the <code>stockData</code> array.
	 * This method is only used to test the BB-calculation.
	 * So, it calls up the {@link #calculateBB(int gdPara, int period, double factor, int number)} method.
	 * 
	 * @param n defines the period
	 * @param f defines the factor
	 * @param stockData contains the close prices to calculate the upper-BB-points
	 * @return <code>double</code> array with the calculated upper-BB-points
	 */
	protected double[] calculateUpperBBTest(double f, int n, double[] stockData) {
		int number = stockData.length - n + 1;
		BollingerBands obj= calculateBB(n,f,number,stockData);
		return obj.getAllUpperPoints();
	}

	
	
	/**
	 * calculates the lower-BB-points with the period <code>n</code> and the factor <code>f</code> for the close prices that are in the <code>stockData</code> array.
	 * This method is only used to test the BB-calculation.
	 * So, it calls up the {@link #calculateBB(int gdPara, int period, double factor, int number)} method.
	 * 
	 * @param n defines the period
	 * @param f defines the factor
	 * @param stockData contains the close prices to calculate the lower-BB-points
	 * @return <code>double</code> array with the calculated lower-BB-points
	 */
	protected double[] calculateLowerBBTest(double f, int n, double[] stockData) {
		int number = stockData.length - n + 1;
		BollingerBands obj= calculateBB(n,f,number, stockData);
		return obj.getAllLowerPoints();
	}
}
	

