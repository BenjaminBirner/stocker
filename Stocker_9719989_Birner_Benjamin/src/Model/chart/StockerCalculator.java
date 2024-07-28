package Model.chart;



/**
 * This calculator-class makes two static methods available.
 * The first one calculates the scale for a chart and the second one rounds
 * the prices according to its value.
 * 
 * @author Benjamin Birner
 *
 */
public class StockerCalculator {
	
	//the maximum number of prices to scale the y-axis of the chart
	private static final int MAX_SCALE = 10;
	
	//this array contains the values which define the steps between the different prices at the y-axis of the chart
	private static final double[] measure = {0.0001,0.0002,0.0004,0.0005,0.0006,0.0008,
			0.001,0.002,0.004,0.005,0.006,0.008,0.01,0.02,0.04,0.05,0.06,0.08,0.1,0.2,
			0.3,0.4,0.5,0.6,0.8,1.0,2.0,3.0,4.0,5.0,6.0,8.0,10.0,20.0,30.0,40.0,50.0,
			60.0,80.0,100.0,200.0,300.0,400.0,500.0,600.0,800.0,1000.0,2000.0,3000.0,
			4000.0,5000.0,6000.0,8000.0,10000.0,20000.0,30000.0,40000.0,50000.0,
			60000.0,80000.0};
	
	/**
	 * This static method calculates the scale for a chart.
	 * It calculates the number of values and the values itself.
	 * To realize this, it uses the <code>measure</code>-array to get the 
	 * suitable distance between the values.
	 * 
	 * 
	 * @param min the minimum close price of all the candles that should be depicted in the chart.
	 * @param max the maximum close price of all the candles that should be depicted in the chart.
	 * @return <code>double</code>-array that contains all the scale-values.
	 */
	public static Double[] scaleChart(double min, double max) {  
		// the quotient of the range and MAX_SCALE-2
		double quot = (max - min) / (MAX_SCALE-2);
		double measure = 0.0;
		//this loop looks for a suitable step-size in the array measure
		for(int i = 0; i < StockerCalculator.measure.length; i++) {
			if(quot <= StockerCalculator.measure[i]) {
				measure = StockerCalculator.measure[i];
				break;
			}
		}
		//the lowest scale-vale determined by the value from the measure-array 
		//that is just under the lowest low of all candles
		min = min - (min % measure);
		//the highest scale-vale determined by the value from the measure-array 
		//that is just over the highest high of all candles
		max = max + (measure - (max % measure));
		//calculates the prices to scale the y-axis of the chart
		int size =  (int) Math.rint(((max - min) / measure +1 )); 
		Double[] scale = new Double[size];
		for(int i = 0; i < size; i++) {
			scale[i] = Math.rint((min + i * measure)*10000) / 10000;                
		}
		return scale;
	}
	
	
	
	
	/**
	 * this static method rounds a price according to its value.
	 * It is mainly used in order to display this price by the view.
	 * 
	 * @param price the price that should be rounded
	 * @return the rounded price
	 */
	public static double roundPrice(double price) {
		if(price >= 10000) {
			return  Math.round(price);
		}else {
			if(price >= 1000) {
				return Math.rint(price * 10) / 10;
			}else {
				if(price >= 10) {
					return Math.rint(price * 100) / 100;
				}else {
					if(price >= 0.1) {
						return Math.rint(price * 1000) / 1000;
					}else {
						if(price >= 0.01) {
							return Math.rint(price * 10000) / 10000;
						}else {
							return Math.rint(price * 100000) / 100000;
						}
					}
				}
			}
		}
	}
	
}
	
