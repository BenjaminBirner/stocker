package Model.indicatorModel;

import java.util.Iterator;
import java.util.LinkedList;


/**
 * This class models the Bollinger Bands indicator.
 * It includes all data about Bollinger Bands like the parameters and
 * all upper and lower points.
 * 
 * @author Benjamin Birner
 *
 */
public class BollingerBands implements Comparable<Integer> {
	
	private final int gdParameter;
	private final Integer period;
	private final double factor;
	private final LinkedList<Double> upperPointList;
	private final LinkedList<Double> lowerPointList;
	
	private double latestGDSum;
	private double latestBBSum;
	
	protected BollingerBands(int gdParameter, int period, double factor,double latestGDSum,double latestBBSum,
			LinkedList<Double> upperPointList, LinkedList<Double> lowerPointList) {
		this.gdParameter = gdParameter;
		this.period = period;
		this.factor = factor;
		this.latestGDSum = latestGDSum;
		this.latestBBSum = latestBBSum;
		this.upperPointList = upperPointList;
		this.lowerPointList = lowerPointList;
	}
	
	
	/**
	 * gets the parameter for the Moving Average. 
	 * 
	 * @return the value of the attribute <code>gdParameter</code> that defines the period of the MA.
	 */
	protected int getGDParameter() {
		return gdParameter;
	}
	
	
	
	/**
	 * gets the period for this <code>BollingerBands</code> object
	 * 
	 * @return the value of the attribute <code>period</code>
	 */
	protected int getPeriod() {
		return period;
	}
	
	
	/**
	 * gets the factor for this <code>BollingerBands</code> object
	 * 
	 * @return the value of the attribute <code>factor</code>
	 */
	protected double getFactor() {
		return factor;
	}
	
	

	/**
	 * gets the latest GD-Sum for this <code>BollingerBands</code> object
	 * 
	 * @return the value of the attribute <code>latestGDSum</code>
	 */
	protected double getLatestGDSum() {
		return latestGDSum;
	}

	
	/**
	 * gets the latest BB-Sum for this <code>BollingerBands</code> object.
	 * 
	 * @return the value of the attribute <code>latestBBSum</code>
	 */
	protected double getLatestBBSum() {
		return latestBBSum;
	}
	
	
	/**
	 * adds the latest upper point to the <code>upperPointList</code> list.
	 * 
	 * @param point the latest upper point
	 */
	protected void addNewUpperPoint(double point) {
		upperPointList.addLast(point);
		upperPointList.removeFirst();
	}
	
	
	/**
	 * adds the latest lower point to the <code>lowerPointList</code> list.
	 * 
	 * @param point the latest lower point
	 */
	protected void addNewLowerPoint(double point) {
		lowerPointList.addLast(point);
		lowerPointList.removeFirst();
	}
	
	
	/**
	 * gets all points for the Upper-Band
	 * 
	 * @return <code>double</code> array that contains all upper points
	 */
	protected double[] getAllUpperPoints() {
		double[] points = new double[upperPointList.size()];
		Iterator<Double> it = upperPointList.iterator();
		for(int i = 0; i < upperPointList.size(); i++) {
			points[i] = it.next();
		}
		return points;
	}
	
	
	/**
	 * gets all points for the Lower-Band
	 * 
	 * @return <code>double</code> array that contains all lower points
	 */
	protected double[] getAllLowerPoints() {
		double[] points = new double[lowerPointList.size()];
		Iterator<Double> it = lowerPointList.iterator();
		for(int i = 0; i < lowerPointList.size(); i++) {
			points[i] = it.next();
		}
		return points;
	}
	
	
	/**
	 * builds up a <code>String</code> representation for an instance of this class
	 * in the following shape: BB(period, factor) --> GD(param);
	 * 
	 * @return this <code>String</code> representation
	 */
	protected String getDisplayDescription() {
		return "BB(" + factor +", " + period + ") ";
	}
	
	
	/**
	 * sets the latest BB-Sum for this <code>BollingerBands</code> object.
	 * 
	 * @param sum the latest BB-Sum
	 */
	protected void setLatestBBSum(double sum) {
		latestBBSum = sum;
	}
	
	
	/**
	 * sets the latest GD-Sum for this <code>BollingerBands</code> object.
	 * 
	 * @param sum the latest GD-Sum
	 */
	protected void setLatestGDSum(double sum) {
		latestGDSum = sum;
	}


	@Override
	public int compareTo(Integer i) {
		return period.compareTo(i);
	}
}
