package Model.indicatorModel;


/**
 *  An Object of this class contains all the data that the view requires to paint an indicator.
 *  It is usable for every individual indicator. So, every indicator has to hand over its data 
 *  to an Object of this class.
 *  This class makes it possible, that the <code>paint</code>-method is independent of the concrete
 *  indicator-type. So, if the program will be upgraded with a new indicator, it is not necessary to 
 *  adapt the <code>paint</code>-method.
 * 
 * @author Benjamin Birner
 *
 */
public class IndicatorPaint {

	private String description;
	private final String[] displayDescriptions;
	//contains all the indicator-points/entries for a defined indicator and chartID
	private final double[][] indiPoints;
	
	protected IndicatorPaint(String[] displayDescriptions, double[][] indiPoints) {
		this.displayDescriptions = displayDescriptions;
		this.indiPoints = indiPoints;
	}
	
	/**
	 * sets the description to define to which indicator the <code>this</code>-instance belongs to.
	 * 
	 * @param description the description that defines the indicator.
	 */
	protected void setDescription(String description) {
		this.description = description;
	}
	
	
	
	/**
	 * gets the description that defines the indicator.
	 * 
	 * @return the description of the indicator to which the <code>this</code>-instanse belongs to.
	 */
	public String getDescription() {
		return description;
	}
	
	
	
	/**
	 * gets the String-representations of the indicators that are represented by the <code>this</code>-object
	 * 
	 * @return the String-representations that are used to display  them in the chart
	 */
	public String[] getDisplayDescriptions() {
		return displayDescriptions;
	}
	
	
	
	/**
	 * gets all the calculated indicator-points that the view requires to paint the indicators.
	 * 
	 * @return the indicator-points
	 */
	public double[][] getIndiPoints(){
		return indiPoints;
	}

}
