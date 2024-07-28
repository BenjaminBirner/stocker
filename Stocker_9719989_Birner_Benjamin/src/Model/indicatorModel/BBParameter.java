package Model.indicatorModel;


/**
 * This class represents the parameters of a Bollinger Bands entry.
 * It implements the {@link #IndicatorParameterPersistence} interface and
 * is mainly used in the store and restore process.
 * 
 * @author Benjamin Birner
 *
 */
public class BBParameter implements IndicatorParameterPersistence{
	
	private int gdParameter;
	private int period;
	private double factor;
	
	protected BBParameter(int gdParameter, int period, double factor) {
		this.gdParameter = gdParameter;
		this.period = period;
		this.factor = factor;
	}
	
	protected BBParameter(String parameters) {
		StringToParameter(parameters);
	}

	
	/**
	 * gets the parameter for this Moving Average
	 * 
	 * @return the value of the attribute <code>gdParameter</code>
	 */
	public int getGDPara() {
		return gdParameter;
	}
	
	
	/**
	 * gets the period.
	 * 
	 * @return the value of the attribute <code>period</code>.
	 */
	public int getPeriod() {
		return period;
	}
	
	
	/**
	 * gets the factor.
	 * 
	 * @return the value of the attribute <code>factor</code>.
	 */
	public double getFactor() {
		return factor;
	}
	
	
	/**
	 * creates a <code>String</code> representation for the parameters of the  <code>this</code> object.
	 * 
	 * @return this <code>String</code> representation.
	 */
	@Override
	public String ParameterToString() {
		return gdParameter + "/" + period + "/" + factor;
		
	}
	
	/**
	 * extracts the parameters for the <code>this</code> object from a
	 * <code>String</code> representation and sets these parameters.
	 * 
	 *@param <code>String</code> representation that contains all parameters
	 */
	@Override
	public void StringToParameter(String parameters){
		int index1 = parameters.indexOf('/');
		int gdPara = Integer.parseInt(parameters.substring(0, index1));
		int index2 = parameters.indexOf('/', index1 + 1);
		int period = Integer.parseInt(parameters.substring(index1 + 1, index2));
		double factor = Double.parseDouble(parameters.substring(index2 + 1, parameters.length() - 1));
		this.gdParameter = gdPara;
		this.period = period;
		this.factor = factor;
	}

	
	/**
	 * gets the greatest value of the attribute <code>period</code> and <code>gdParameter</code>
	 * 
	 * @return the greatest value
	 */
	@Override
	public int getParaMax() {
		
		return (gdParameter >= period ? gdParameter : period);
	}

}
