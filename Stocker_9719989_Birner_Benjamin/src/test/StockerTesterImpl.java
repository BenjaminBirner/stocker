package test;

import java.util.Set;

import Model.alarmModel.AlarmDataModel;
import Model.indicatorModel.IndicatorModel;
import Model.watchlist.WatchlistStock;
import Model.watchlist.WatchlistTableModel;
import stocker.IStockerTester;

public class StockerTesterImpl implements IStockerTester {
	
	AlarmDataModel alarmModel = new AlarmDataModel();
	WatchlistTableModel watchlistModel = new WatchlistTableModel();
	IndicatorModel indiModel = new IndicatorModel();

	@Override
	public String getMatrNr() {
	
		return "9719989";
	}

	@Override
	public String getName() {

		return "Benjamin Birner";
	}

	@Override
	public String getEmail() {
		
		return "martina.meng@jvafreiburg.justiz.bwl.de";
	}

	@Override
	public void clearWatchlist() {
		watchlistModel.clearWL();

	}

	@Override
	public void addWatchlistEntry(String stockId) {
		WatchlistStock stock = new WatchlistStock(stockId,stockId,0.0,0.0,0.0);
		int index = watchlistModel.getIndex(stockId);
		watchlistModel.addStockToTable(stock, index);
	}

	@Override
	public void removeWatchlistEntry(String stockId) {
		int index = watchlistModel.getIndex(stockId);
		if(index >= 0) {
			int[] indices = {index};
			watchlistModel.removeStocks(indices);
		}

	}

	@Override
	public String[] getWatchlistStockIds() {
		
		return watchlistModel.getAllSymbols();
	}

	@Override
	public void clearAlarms(String stockId) {
		alarmModel.deleteEntry(stockId);

	}

	@Override
	public void clearAllAlarms() {
		alarmModel.clearAlarmListTest();

	}

	@Override
	public void addAlarm(String stockId, double threshold) {
		//evtl. quote-req
		
		alarmModel.addAlarm(stockId, -1, threshold);

	}

	@Override
	public void removeAlarm(String stockId, double threshold) {
		alarmModel.deleteAlarmTest(stockId, threshold);

	}

	@Override
	public double[] getAlarms(String stockId) {
		
		return alarmModel.getAllAlarmsInDouble(stockId);
	}

	@Override
	public Set<String> getAlarmStockIds() {
		
		return alarmModel.getAllSymbolsTest();
	}

	@Override
	public double[] getMovingAverage(int n, double[] stockData) {
		
		return indiModel.calculateGDTest(n,stockData);
	}

	@Override
	public double[] getUpperBollingerBand(double f, int n, double[] stockData) {
		
		return indiModel.calculateUpperBBTest(f,n,stockData);
	}

	@Override
	public double[] getLowerBollingerBand(double f, int n, double[] stockData) {
		
		return indiModel.calculateLowerBBTest(f,n,stockData);
	}

}
