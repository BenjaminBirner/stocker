package Controller;

import java.time.DayOfWeek;
import java.time.LocalDateTime;                                //here we can create several constructors. The one without reso is the one with the standard reso
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import Model.persistence.PropertiesCache;


/**
 * This class constructs the appropriate URL-String for the candle-request.
 * It calculates the exact time for the from-query-String and builds the correct
 * URL-Sting with all its aspects by handing over the symbol, the resolution and 
 * the required number of candles,
 * So, if a candle request must be carried out, it is just necessary to build an
 * object of this class and call its method "getUrlString".
 */
public class URLCandleRequest {
	
	
	/**
	 * defines the basis URL for the HTTP-Requests (pull-mechanism)
	 */
	public static final String DEFAULT_BASIS_URL = "http://localhost:8080";
	
	/**
	 * defines the basis URL for the webSocket (push-mechanism)
	 */
	public static final String DEFAULT_WS_URL = "ws://localhost:8090";
	
	                         
	private final String query = "/stock/candle?symbol=";
	private final String symbol;
	private final String resolutionQ = "&resolution=";
	private final String resolution;
	private final String fromQ = "&from=";
	private final String toQ = "&to=";
	private final String tokenQ = "&token=";
	private final String basisURL;
	private final String apiKey;  
	
	private String from;
	private String to;
	
	
	protected URLCandleRequest(String symbol, String resolution, int number) {
		this.symbol = symbol;
		//checks the resolution. The resolution-value of the attribute is the one that
		//is used for the request. The resolution-parameter is used for the calculation.
		if(resolution.equals("10")) {
			this.resolution = "5";
		}else {
			if(resolution.equals("240")) {
				this.resolution = "60";
		    }else {
		    	this.resolution = resolution;
		    }
		}
		PropertiesCache prop = PropertiesCache.getInstance();
		String apiKey = prop.getProperty("api.key");
		this.apiKey = apiKey;
		String pullUrl = prop.getProperty("pull.url");
		basisURL = (pullUrl == null ? DEFAULT_BASIS_URL : pullUrl);
		calculateFromAndTo(resolution, number);
	}
	/**
	 *constructs the URL-String by getting all parts
	 *
	 *@return url URL-String
	 */
	protected String getUrlString() {
		String url = basisURL+ query + symbol + resolutionQ + resolution + fromQ + from + toQ + to + tokenQ + apiKey;
		return url;
	}
	
	/*
	 * calculates the "from" and "to" time
	 */
	private void getFromAndToTime(long min) { 
		//gets the current date and time
		LocalDateTime now = LocalDateTime.now();
		//gets the date and time of the required time in the past
		LocalDateTime past = LocalDateTime.now().minusMinutes(min);
		//gets the single values to create a GregorianCalendar
		int nowDay = now.getDayOfMonth();                  
		int nowMonth = now.getMonth().getValue()-1;
		int nowYear = now.getYear();
		int nowHour = now.getHour();
		int nowMin = now.getMinute();
		int nowSec = now.getSecond();
		int pastDay = past.getDayOfMonth();                 
		int pastMonth = past.getMonth().getValue()-1;
		int pastYear = past.getYear();
		int pastHour = past.getHour();
		int pastMin = past.getMinute();
		int pastSec = past.getSecond();
		//creates the GregorianCalendar to get the required time in milliseconds
		long msTo = new GregorianCalendar(nowYear, nowMonth, nowDay, nowHour, nowMin, nowSec).getTimeInMillis();
		long msFrom = new GregorianCalendar(pastYear, pastMonth, pastDay, pastHour, pastMin, pastSec).getTimeInMillis(); 
		//the required time for the request in seconds 
		from = 	Long.toString(TimeUnit.MILLISECONDS.toSeconds(msFrom));
		to = Long.toString(TimeUnit.MILLISECONDS.toSeconds(msTo));
	
	}
	
	private void calculateFromAndTo(String resolution, int number) {
		int min = 0;
		number = number + 2;
		switch(resolution) {
		case "1": min = calculatePeriod(number+3);
				break;
		case "5": min =  calculatePeriod(number * 5);
				break;
		case "10": min = calculatePeriod((number+2) * 10);
				break;
		case "15": min = calculatePeriod(number * 15);
		 		break;
		case "30": min = calculatePeriod(number * 30);
				break;
		case "60": min = calculatePeriod(number * 60);
				break;
		case "240": min = calculatePeriod((number) * 240);
				break;
		case "D": min = (int) (60 * 24 * number * 1.4 + 60 * 24 *4);
				break;
		case "W": min = (int) (60 * 24 * 7* (number + 4));
				break;
		case "M": min = (int) (60 * 24 * 31 * (number + 4));
				break;
		}
		
		getFromAndToTime(min) ;                                                             
	}
	
	/**
	 * calculates the period of minutes which is required to get the adequate number of candles by considering the open times of the stock exchange.
	 * This method is used by the method "calculateFromAndTo" for the resolutions "1" to "240".
	 * Moreover, the method is easily adaptable to other opening times by adjusting the variables midnightToOpen and midnightToClose.
	 *  
	 * @param requMin the required period of opening time.
	 * @return the period if minutes to calculate the correct point of times for the candle request.
	 */
	private int calculatePeriod(int requMin) {
		
		// these two variables are adaptable to other open times
		int midnightToOpen = 480;
		int midnightToClose = 1320;
		
		LocalDateTime now = LocalDateTime.now();
		DayOfWeek day = now.getDayOfWeek();
		
		// the passed time of today
		int passedMin = now.getHour() * 60 + now.getMinute() +1;
		int closeMin = 0;
		int openMin = 0;
		
		// case: today is no opening time
		if( day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY || passedMin <= midnightToOpen) {
			closeMin = passedMin;
		}else {
			
			// case: there is opening time and the stock exchange is still opened
			if(passedMin <= midnightToClose) {
				closeMin = midnightToOpen;
				openMin = passedMin - closeMin - 2;
				if(openMin > requMin) {
					return requMin;
				}	
			}else {
				//case: there is opening time and the stock exchange has already closed
				closeMin = midnightToOpen + passedMin - midnightToClose;
				openMin = midnightToClose - midnightToOpen;
				if( openMin > requMin) {
					return requMin + closeMin - midnightToOpen;
				}
			}
		}
		day = day.minus(1);
		
		//this loop adds all close and open time until there is enough open time for the request
		while(openMin < requMin) {
			if(day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY) {
				closeMin = closeMin + 1440;
			}else {
				closeMin = closeMin + midnightToOpen + (1440 - midnightToClose);
				openMin = openMin + midnightToClose - midnightToOpen;
			}
			day = day.minus(1);
		}
		return closeMin - midnightToOpen + requMin;
	}
}
