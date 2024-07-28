package Model.indicatorModel;



/**
 * This class represents the parameter/period of a Moving Average entry.
 * It implements the {@link #IndicatorParameterPersistence} interface and
 * is mainly used in the store and restore process.
 * 
 * @author Benjamin Birner
 *
 */
public class GDParameter implements IndicatorParameterPersistence {

	public int parameter;
	
	public GDParameter(int parameter) {
		this.parameter = parameter;
	}
	
	public GDParameter(String parameter) {
		StringToParameter(parameter);
	}
	
	
	/**
	 * gets the parameter/period of the MA
	 * 
	 * @return the parameter/period of the MA
	 */
	public int getParameter() {
		return parameter;
	}
	
	
	@Override
	public String ParameterToString() {
	
		return parameter + "";
	}

	
	@Override
	public void StringToParameter(String parameter){
		this.parameter = Integer.parseInt(parameter);
	}

	@Override
	public int getParaMax() {
		
		return parameter;
	}
	
	

}
