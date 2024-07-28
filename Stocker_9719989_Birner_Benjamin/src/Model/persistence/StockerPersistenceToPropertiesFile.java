package Model.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import Model.alarmModel.AlarmPersistence;
import Model.chart.ChartPersistence;
import Model.indicatorModel.IndicatorModel;
import Model.indicatorModel.PersistenceIndiParaData;
import Model.watchlist.FramePersistence;
import Model.watchlist.WatchlistPersistence;


/**
 * this class stores the data to the properties-file and loads the data from the properties-file.
 * To realize this, it converts all the data that a <code>StockerPersistenceData</code>-Object contains
 * to string-representations so that the data can be stored in the properties-file.
 * When the program starts it reconverts the string-representations to an instance of the 
 * <code>StockerPersistenceData</code>-class in order to restore the <code>StockerPersistenceData</code>-Object.
 * With the help of this instance the former state of the program can be restored.
 * 
 * @author Benjamin Birner
 *
 */
public class StockerPersistenceToPropertiesFile {
	
	
	/**
	 * this method converts all the data from the <code>StockerPersistenceData</code>-Object which is referenced  by the 
	 * parameter <code>data</code> to string-representations and stores the strings in the properties-file.
	 * This method is called up in the program´s closing process.
	 * 
	 * @param data <code>StockerPersistenceData</code>-Object that contains the data to restore the last state of the program.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public void storeToPropertiesFile(StockerPersistenceData data) throws FileNotFoundException, IOException {
		//deletes all the Properties-entries that are no longer required
		removeOldStorageFromPropFile();
		
		//this block gets all the different data form the StockerPersistenceData-object
		LinkedList<WatchlistPersistence> wlList = data.getWLPersistenceList();
		LinkedList<AlarmPersistence> aList = data.getAlarmList();
		LinkedList<ChartPersistence> cList = data.getChartList();
		FramePersistence wlFrame = data.getWlFramePersis();
		FramePersistence mainFrame = data.getMainFramePersis();
		PropertiesCache prop = PropertiesCache.getInstance();
		
		//true means there are watchlist-entries
		if( wlList.size() > 0) {
			//The watchlist-entries are converted to the following shape:
			//&symbol_0&symbol_1&...&symbol_n&
			String wlKey = "watchListEntrys";
			String wlValue = "&";
			Iterator<WatchlistPersistence> itW = wlList.iterator();
			for(int i = 0; i < wlList.size(); i++) {
				WatchlistPersistence wpObj = itW.next();
				wlValue = wlValue + wpObj.getSymbol()+ "/" + wpObj.getDescription() + "&";
			}
			wlValue = wlValue + ".";
			prop.setProperty(wlKey, wlValue);
		}
		//true means there are alarms
		if(aList.size() > 0) {
			//The alarms are converted to the following shape:
			//symbol_0/price_0/type_0&...&symbol_n/price_n/type_n&
			String alarmKey = "alarmList";
			String alarmValue = "&";
			Iterator<AlarmPersistence> itA = aList.iterator();
			for(int i = 0; i < aList.size(); i++) {
				AlarmPersistence alarm = itA.next();
				alarmValue = alarmValue + alarm.getSymbol() + "/" + alarm.getPrice() + "/" + alarm.getTyp() + "&";
			}
			alarmValue = alarmValue + ".";
			prop.setProperty(alarmKey, alarmValue);
		}
		//true means that the watchlist-frame is open
		if(wlFrame != null) {
			//the data concerning the watchlist-frame are converted to the following shape:
			///height/width/locationX/locationY/Zorder/
			String wlFKey = "wlFrame";
			String wlFValue = "/" + wlFrame.getHeight() + "/" + wlFrame.getWidth() + "/" + wlFrame.getLocX() + "/" + wlFrame.getLocY() + "/" + wlFrame.getZOrder() + "/";
			prop.setProperty(wlFKey, wlFValue);
		}
		//the data concerning the main-frame are converted to the following shape:
		///height/width/locationX/locationY/
		String mainFKey = "mainFrame";
		String mainFValue = "/" + mainFrame.getHeight() + "/" + mainFrame.getWidth() + "/" + mainFrame.getLocX() + "/" + mainFrame.getLocY() + "/" ;
		prop.setProperty(mainFKey, mainFValue);
		//true means that there are charts
		if( cList.size() > 0) {
			//the ID´s for the different charts:&id_0&id_1&...&id_n&
			//these ID´s are only required in the load-process to build up the correct
			//keys for the different charts in the properties file
			String idKey = "ids";
			String idValue = "&";
			Iterator<ChartPersistence> itC = cList.iterator();
			
			//this loop builds up the basis-String for each chart that contains all data concerning the frame of a chart.
			//the shape:  basis key:     id.basis
			//            basis value:  /symbol/resolution/height/width/locationX/locationY/ZOrder/.
			for(int i = 0; i < cList.size(); i++) {
				ChartPersistence chart = itC.next();
				idValue = idValue + chart.getID() + "&";
				String cBasisKey = chart.getID() + ".basis";
				String cBasisValue = "/" + chart.getType() + "/" + chart.getSymbol() + "/" + chart.getResolution() + "/" + chart.getHeight() + "/" + chart.getWidth() + "/" +
											chart.getLocationX() + "/" + chart.getLocationY() + "/" + chart.getZOrder() + "/" ;
				
				// shape for the indicator-key:  id.indicator
				String cIndiKey = chart.getID() + ".indicator";
				LinkedList<PersistenceIndiParaData> indiList = chart.getIndicatorList();
				cBasisValue = cBasisValue + ".";
				prop.setProperty(cBasisKey, cBasisValue);
				
				
				//true means that there are indicators to this chart
				if(indiList.size() > 0) {
					String cIndiValue = "";
					
					//this loop builds up the corresponding String in the following shape:
					// value: $description_0*parameterString_0*$...$description_n*parameterSring_n*
					for(int k = 0; k < indiList.size(); k++) {
						cIndiValue = cIndiValue + "$" + indiList.get(k).getDescription() + "*";
						for(int j = 0; j < indiList.get(k).size(); j++) {
							//gets the parameter-String
							cIndiValue = cIndiValue + indiList.get(k).getParameterStringAt(j) + "*";
						}
					}
					prop.setProperty(cIndiKey, cIndiValue);
				}
			}
			prop.setProperty(idKey, idValue);
		}
		//stores the Properties to the file                                                   
		
			prop.store();
		
	}
	
	
	
	/**
	 * this method builds up a new <code>StockerPersistenceData</code>-Object from the string-representation from the properties-file.
	 * It is called up in the program´s store process. The <code>StockerPersistenceData</code>-Object enables the program to restore 
	 * the former state.
	 * 
	 * @param indiModel reference to the IndicatorModel that is needed to get access to the
     *                  <code>createParameterObj(String description, String parameters)</code>-method
     *                   which creates from the string-representation the corresponding <code>IndicatorParameterPersistence</code>-object.
	 * @return the <code>StockerPersistenceData</code>-Object that contains the data that are required to restore the last state of the program.
	 */
	public StockerPersistenceData loadStockerStatusFromProperties(IndicatorModel indiModel) {
	
		// creates several lists that must be filled with data in the process of this method and
		//added to the StockerPersistenceData-Object
		LinkedList<WatchlistPersistence> wlPerList = new LinkedList<WatchlistPersistence>();
		LinkedList<AlarmPersistence> alarmList = new LinkedList<AlarmPersistence>();
		LinkedList<ChartPersistence> chartList = new LinkedList<ChartPersistence>();
		PropertiesCache prop = PropertiesCache.getInstance();
		//creates a new StockerPersistenceData-Object
		StockerPersistenceData spd = new StockerPersistenceData();
		//gets the string-representation that contains the data for the watchlistFrame and mainFrame
		String wlF = prop.getProperty("wlFrame");
		String mainF = prop.getProperty("mainFrame");
		int index1;
		int index2;
		int height;
		int width;
		int locX;
		int locY;
		int zOrder;
		//this block converts all mainFrame-data from the string-representation to the required format and
		//creates with this data a FramePersistence-Object and adds this object to the StockerPersistenceData-Object
		if( mainF != null) {
			index1 = mainF.indexOf('/');
			index2 = mainF.indexOf('/', index1 + 1);
			height = Integer.parseInt(mainF.substring(index1 + 1, index2));
			index1 = mainF.indexOf('/', index2 + 1);
			width = Integer.parseInt(mainF.substring(index2 + 1, index1));
			index2 = mainF.indexOf('/', index1 + 1);
			locX = Integer.parseInt(mainF.substring(index1 + 1, index2));
			index1 = mainF.indexOf('/', index2 + 1);
			locY = Integer.parseInt(mainF.substring(index2 + 1, index1));
			FramePersistence mainFP = new FramePersistence(height,width,locX,locY,0);
			spd.setMainFrame(mainFP);
		}
		//this block converts all watchlistFrame-data from the string-representation to the required format and
		//creates with this data a FramePersistence-Object and adds this object to the StockerPersistenceData-Object
		if(wlF != null) {
			index1 = wlF.indexOf('/');
			index2 = wlF.indexOf('/', index1 + 1);
			height = Integer.parseInt(wlF.substring(index1 + 1, index2));
			index1 = wlF.indexOf('/', index2 + 1);
			width = Integer.parseInt(wlF.substring(index2 + 1, index1));
			index2 = wlF.indexOf('/', index1 + 1);
			locX = Integer.parseInt(wlF.substring(index1 + 1, index2));
			index1 = wlF.indexOf('/', index2 + 1);
			locY = Integer.parseInt(wlF.substring(index2 + 1, index1));
			index2 = wlF.indexOf('/', index1 + 1);
			zOrder = Integer.parseInt(wlF.substring(index1 + 1, index2));
			FramePersistence wlFP = new FramePersistence(height,width,locX,locY,zOrder);
			spd.setWLFrame(wlFP);
		}
		//gets the watchlist-symbols-string
		String wlPer = prop.getProperty("watchListEntrys");
		String sym;
		String desc;
		//extracts all symbols from this string-representation and adds the symbols to the corresponding list
		if(wlPer != null) {
			index1 = wlPer.indexOf('&');
			index2 = wlPer.indexOf('/', index1 + 1);
			while(index2 != -1) {
				sym = wlPer.substring(index1 + 1, index2);
				index1 = index2;
				index2 = wlPer.indexOf('&', index1 + 1);
				desc = wlPer.substring(index1 + 1, index2);
				WatchlistPersistence perObj = new WatchlistPersistence(sym,desc);
				wlPerList.addLast(perObj);
				index1 = index2;
				index2 = wlPer.indexOf('/', index1 + 1);
				
			}
		}
		//gets the alarm-string
		String al = prop.getProperty("alarmList");
		//extracts all the data for the different alarms from this string and builds up an AlarmPersistence-Object for
		//each alarm
		if(al != null) {
			index1 = al.indexOf('&');
			index2 = al.indexOf('/', index1 + 1);
			while(index2 != -1) {
				sym = al.substring(index1 + 1, index2);
				index1 = index2;
				index2 = al.indexOf('/', index1 + 1);
				double price = Double.parseDouble(al.substring(index1 + 1, index2));
				index1 = index2;
				index2 = al.indexOf('&', index1 + 1);
				boolean type = Boolean.parseBoolean(al.substring(index1 + 1, index2));
				//adds the AlarmPersistence-Object to the corresponding list
				alarmList.addLast(new AlarmPersistence(sym,price,type));
				index1 = index2;
				index2 = al.indexOf('/', index1 + 1);
			}
		}
		String ids = prop.getProperty("ids");
		String type;
		String reso;
		//gets the id-string to build up the keys
		if(ids != null) {
			index1 = ids.indexOf('&');
			index2 = ids.indexOf('&',index1 + 1);
			//this block is carried out once for  each id
			while(index2 != -1) { 
				//this block converts all chart-basis data from the string-representation to the required format and
				//creates with this data a ChartPersistence-Object
				String id = ids.substring(index1 + 1, index2);
				String basis = prop.getProperty(id + ".basis");
				int index3 = basis.indexOf('/');
				int index4 = basis.indexOf("/", index3 + 1);
				type = basis.substring(index3 + 1, index4);
				index3 = index4;
				index4 = basis.indexOf("/", index3 + 1);
				sym = basis.substring(index3 + 1, index4);
				index3 = index4;
				index4 = basis.indexOf("/", index3 + 1);
				reso = basis.substring(index3 + 1, index4);
				index3 = index4;
				index4 = basis.indexOf("/", index3 + 1);
				height = Integer.parseInt(basis.substring(index3 + 1, index4));
				index3 = index4;
				index4 = basis.indexOf("/", index3 + 1);
				width = Integer.parseInt(basis.substring(index3 + 1, index4));
				index3 = index4;
				index4 = basis.indexOf("/", index3 + 1);
				locX = Integer.parseInt(basis.substring(index3 + 1, index4));
				index3 = index4;
				index4 = basis.indexOf("/", index3 + 1);
				locY = Integer.parseInt(basis.substring(index3 + 1, index4));
				index3 = index4;
				index4 = basis.indexOf("/", index3 + 1);
				zOrder = Integer.parseInt(basis.substring(index3 + 1, index4));
				index1 = index2;
				index2 = ids.indexOf('&', index1 + 1);
				ChartPersistence chart = new ChartPersistence(-1,type, sym, reso, height, width, locX, locY, zOrder);
				//gets the indicator-string
				String indiS = prop.getProperty(id + ".indicator");
				if(indiS != null) {
					int index5 = indiS.indexOf('$');
					//this loop is carried out once for each indicator-type
					//it converts all indicator data from the string-representation to the required format and creates a
					//PersistenceIndiParaData-Object
					while(index5 != -1) {     
						index3 = index5;
						index4 = indiS.indexOf('*', index3 + 1);
						index5 = indiS.indexOf('$', index3 + 1);
						desc = indiS.substring(index3 + 1, index4);
						index3 = index4;
						index4 = indiS.indexOf('*', index3 + 1);
						String para = indiS.substring(index3 + 1, index4);
						//creates the PersistenceIndiParaData-Object and adds this object to the corresponding list
						PersistenceIndiParaData pIndi = new PersistenceIndiParaData(desc);
						pIndi.addFirst(indiModel.createParameterObj(desc,para));
						index3 = index4;
						index4 = indiS.indexOf('*', index3 + 1);
						//If there are several entries to this indicator-type, this loop creates the corresponding Parameter-Object for each entry 
						while(index4 < index5 || (index4 != -1 && index5 == -1)) {  
							para = indiS.substring(index3 + 1, index4);
							pIndi.addFirst(indiModel.createParameterObj(desc,para));
							index3 = index4;
							index4 = indiS.indexOf('*', index3 + 1);
						}
						chart.addPersistenceIndi(pIndi);
					}
				}
				chartList.addLast(chart);
			}
		}
		//adds all the generated data to the StockerPersistenceData-Object
		spd.addWLPersistenceList(wlPerList);
		spd.addAlarmList(alarmList);
		spd.addChartList(chartList);
		return spd;
	}
	
	private void removeOldStorageFromPropFile() {
		PropertiesCache prop = PropertiesCache.getInstance();
		prop.removeProperty("watchListEntrys");
		prop.removeProperty("alarmList");
		prop.removeProperty("wlFrame");
		prop.removeProperty("mainFrame");
		//this block deletes the chart entries with the different id´s. This is absolutely necessary because otherwise the id-string is
		//going to be overwritten with the consequence that it is not possible to get access to the old entries because the keys can not be rebuilt
		String ids = prop.getProperty("ids");
		if(ids != null) {
			int index1 = ids.indexOf('&');
			int index2 = ids.indexOf('&', index1 + 1);
			while(index2 != -1) {
				String id = ids.substring(index1 + 1, index2);
				prop.removeProperty(id + ".basis");
				prop.removeProperty(id + ".indicator");
				index1 = index2;
				index2 = ids.indexOf('&', index1 + 1);
			}
		}
		prop.removeProperty("ids");
	}
}
