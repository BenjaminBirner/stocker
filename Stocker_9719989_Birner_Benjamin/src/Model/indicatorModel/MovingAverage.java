package Model.indicatorModel;

import java.util.Iterator;
import java.util.LinkedList;

import View.chart.Chart;



/**
 * This class models the Moving Average indicator.
 * It includes all data about Moving Averages like the parameters and calculated points.
 * 
 * @author Benjamin Birner
 *
 */
class MovingAverage implements Comparable<Integer> {

	private final Integer parameter;
	private LinkedList<Double> pointList;
	
	protected MovingAverage(int parameter, LinkedList<Double> pointList){
		this.parameter = parameter;
		this.pointList = pointList;
	}
	
	
	
	/**
	 * gets the parameter for the Moving Average. 
	 * 
	 * @return the value of the attribute <code>parameter</code> that defies the period.
	 */
	protected int getParameter() {
		return parameter;
	}
	
	
	
	
	/**
	 * sets a new MA-point.
	 * This method is called up if a new candle has just been generated and a new
	 * MA-point has just been calculated.
	 * 
	 * @param point the new calculated MA-point.
	 */
	protected void setNewPoint(double point) {
		pointList.removeFirst();
		pointList.addLast(point);
	}
	
	
	
	/**
	 * gets the latest MA-point.
	 * 
	 * @return the latest MA-point.
	 */
	protected double getLastPoint() {
		return pointList.getLast();
	}
	
	
	
	/**
	 * sets the calculated MA-point-list.
	 * 
	 * @param pointList the list that contains all MA-points.
	 */
	protected void setPointList(LinkedList<Double> pointList) {
		this.pointList = pointList;
	}
	
	
	
	/**
	 * gets all MA-points.
	 * 
	 * @return all MA-points in a <code>double</code>-array.
	 */
	protected double[] getAllPoints() {
		double[] points = new double[pointList.size()];
		Iterator<Double> it = pointList.iterator();
		for(int i = 0; i < pointList.size(); i++) {
			points[i] = it.next();
		}
		return points;
	}
	
	
	
	/**
	 * builds up a <code>String</code>-representation for an instance of this class
	 * in the following shape: GDparameter;
	 * 
	 * @return this <code>String</code>-representation
	 */
	protected String getDisplayDescription() {
		return "GD" + parameter;
	}
	
	
	@Override
	public int compareTo(Integer i) {
		return parameter.compareTo(i);
	}

}
