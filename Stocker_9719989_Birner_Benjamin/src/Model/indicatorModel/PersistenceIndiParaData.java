package Model.indicatorModel;

import java.util.LinkedList;


/**
 * this class collects all objects that implement the {@link #IndicatorParameterPersistence}-interface concerning
 * an individual indicator which is defined by the attribute <code>description</code> in the <code>list</code>.
 * It is important regarding the store and restore process and belongs to a defined chartID.
 * Each indicator that has data to store concerning a defined chartID must create an instance of this class.
 * Objects of this class are held in a list of the {@link #ChartPersistence}-class.
 * 
 * @author Benjamin Birner
 *
 */
public class PersistenceIndiParaData {

	//defines the indicator-type
	private final String description;
	private final LinkedList<IndicatorParameterPersistence> list = new LinkedList<>();
	
	public PersistenceIndiParaData(String description) {
		this.description = description;
	}
	
	
	/**
	 * adds the <code>para</code>-object to the <code>list</code> at the first position.
	 * 
	 * @param para the object to add
	 */
	public void addFirst(IndicatorParameterPersistence para) {                                                       // after an bug i changed from addLast to addFirst!!!!
		list.addFirst(para);
	}
	
	
	/**
	 * gets the description which defines the indicator-type of the  <code>this</code>-object.
	 * 
	 * @return the description which defines the indicator-type of the  <code>this</code>-object.
	 */
	public String getDescription() {
		return description;
	}
	
	
	
	/**
	 * gets the greatest parameter of all instances that the <code>list</code> contains.
	 * This method is important regarding the candle request to get the correct number of required candles
	 * to do the request.
	 * 
	 * @return the greatest parameter of all instances that the <code>list</code> contains.
	 */
	public int getParaMax() { 
		//the getParaMax()-method gets the greatest parameter if this indicator-type
		//has more than one parameter which influences the required number of candles.
		int max = list.get(0).getParaMax();
		for(int i = 1; i < list.size(); i++) {
			if(list.get(i).getParaMax() > max) {
				max = list.get(i).getParaMax();
			}
		}
		return max;
	}
	
	
	
	/**
	 * gets the <code>list</code>
	 * 
	 * @return the <code>list</code>
	 */
	public LinkedList<IndicatorParameterPersistence> getParameterList(){
		return list;
	}
	
	
	
	/**
	 * gets the size of the <code>list</code>.
	 * 
	 * @return the size of the <code>list</code>.
	 */
	public int size() {
		return list.size();
	}
	
	
	/**
	 * gets the String-representation regarding the parameters of the object at position <code>index</code> in
	 * the <code>list</code>.
	 * This method is used to get the String-representation for the parameters to store the data in the properties file
	 *   
	 * @param index defines the position in the <code>list</code>.
	 * @return the above mentioned String-representation.
	 */
	public String getParameterStringAt(int index) {
		return list.get(index).ParameterToString();
	}
}
