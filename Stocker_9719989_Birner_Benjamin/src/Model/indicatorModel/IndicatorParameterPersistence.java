package Model.indicatorModel;



/**
 * this interface is important regarding the store and restore process and makes it 
 * possible to hold all the different data of the different indicators in the same list
 * and to work effectively.
 * Moreover, if the program will be upgraded with another indicator, this indicator merely
 * has to implement this interface and the store and restore process will work without any 
 * further adaptations.
 * The following classes implement this interface:
 * {@link #GDParameter}, {@link #BBParameter}.
 * 
 * @author Benjamin Birner
 *
 */
public interface IndicatorParameterPersistence {
	

	/**
	 * this method must convert the parameters of the instances which implements this interface 
	 * to a String representation in order to store the parameters in a properties file.
	 * This representation must have the following shape:
	 * param_0/param_1/..../param_n
	 * 
	 * @return the above mentioned String-representation.
	 */
	public String ParameterToString();
	
	
	/**
	 * this method must initialize the corresponding indicatorParameter-object with the values from
	 * the String representation.
	 * It is called up in the program´s restore process after starting the program.
	 * 
	 * @param parameter the String-representation that contains the required parameters to initialize 
	 *                  the corresponding object.
	 */
	public void StringToParameter(String parameter);
	
	
	/**
	 * gets the greatest parameter if this indicator-type has more than one parameter which influences
	 * the required number of candles.
	 * The result is needed to get the correct number of candles. 
	 * 
	 * @return the greatest parameter that influences the required number of candles.
	 */
	public int getParaMax();
}
