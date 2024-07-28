package Model.chart;

import java.util.LinkedList;

import View.chart.Chart;


/**
 * This class only contains a static method that converts candles with a specific resolution n to candles
 * with the resolution m with m > n;
 * 
 * @author Benjamin Birner
 *
 */
public class CandleConverter {

	
		/**
		 * This method converts candles with a specific resolution n to candles
		 * with the resolution <code>resoInSec</code> with <code>resoInSec</code> > n;
		 * 
		 * @param data the candles that should be converted
		 * @param resoInSec the resolution to that the candles should be converted in seconds
		 * @param symbol the symbol to which the candles belongs to
		 * @param reso the resolution a as String-representation
		 * @return <code>LinkedList</code> that contains all the converted candles
		 */
		public static LinkedList<Candle> convertCandlesToCandles(CandleRequestData data, long resoInSec, int number, String symbol, String reso) {
			
			LinkedList<Candle> candleList = new LinkedList<Candle>();
			
			int index = data.size()-1;
			int index2 = data.size()-2;
			double close;
			double high;
		    double low;
		    double open;
		    long time;
		    long vol;
		    //if the resolution is 10 min it is important that each candle is generated with two 5 min candles which belongs to
		    // the same day. This insures the if-instruction. Without this if-instruction it could be possible that the first
		    // 5 min candle is from Friday and the second on is from Monday.
		    if(resoInSec == 600 && (data.getTimeAt(index) % 200) != 0) {   
		    	//in this block the latest 10 min candle is initialized with the data of the latest 5 min candle (the one which is in progress)
		    	//and the 5min candle before this one (the latest one which is completed).
		    	//So, the result is the 10 min candle that is in progress.
		    	high = (data.getHighAt(index) >= data.getHighAt(index2) ? data.getHighAt(index) : data.getHighAt(index2) );
		    	low = (data.getLowAt(index) <= data.getLowAt(index2) ? data.getLowAt(index) : data.getLowAt(index2) );
		    	vol = data.getVolumeAt(index) + data.getVolumeAt(index2);
		    	open = data.getOpenAt(index2);
		    	time = data.getTimeAt(index2);
		    	close = data.getCloseAt(index);
		    	Candle candle = new Candle(symbol,reso,close,high,low,open,time,vol);
		    	candleList.addFirst(candle);
		    	index = data.size()-3;
		    }else {
		    	// in this case the 10/240 min candle is only initialized with the latest 5/60 min candle(the one that is in progress).
		    	close = data.getCloseAt(index);
		    	high = data.getHighAt(index);
		    	low = data.getLowAt(index);
		    	open = data.getOpenAt(index);
		    	time = data.getTimeAt(index);
		    	vol = data.getVolumeAt(index);
		    	Candle candle = new Candle(symbol, reso,close,high,low,open,time,vol);
		    	candleList.addFirst(candle);
		    	index = data.size()-2;
		    }
		    //initializes the variables with the data of the latest candle that was not used in the last block.
		    close = data.getCloseAt(index);
	    	high = data.getHighAt(index);
	    	low = data.getLowAt(index);
	    	open = data.getOpenAt(index);
	    	time = data.getTimeAt(index);
	    	vol = data.getVolumeAt(index);
	    	index = index-1;
	    	int i = 0;
	    	//this loop generates all the required candles
	    	while( i < number) { 
	    		//checks if the candle that is in progress is complete
	    		if(data.getTimeAt(index) <= time - resoInSec) {
	    			//creates a new candle with the corresponding data, adds the candle to the list and initializes the
	    			//variables with the data of the last unused candle.
	    			Candle candle = new Candle(symbol,reso,close,high,low,open,data.getTimeAt(index + 1),vol);
	    			candleList.addFirst(candle);
	    			close = data.getCloseAt(index);
	    	    	high = data.getHighAt(index);
	    	    	low = data.getLowAt(index);
	    	    	open = data.getOpenAt(index);
	    	    	time = data.getTimeAt(index);
	    	    	vol = data.getVolumeAt(index);
	    	    	index = index-1;
	    	    	i = i+1;
	    		}else {
	    			//this block compares the data that are saved in the variables to the data of the latest unused
	    			//candle and initializes the variables with a new value if necessary.
	    			if(data.getHighAt(index) > high) {
	    				high = data.getHighAt(index);
	    			}
	    			if(data.getLowAt(index) < low) {
	    				low = data.getLowAt(index);
	    			}
	    			open = data.getOpenAt(index);
	    			vol = vol + data.getVolumeAt(index);
	    			index = index-1;
	    		}
	    	}
	    	return candleList;
		}
}
