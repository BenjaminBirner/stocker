package Model.watchlist;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.table.AbstractTableModel;



/**
 * this class extends the {@link #AbstractTableModel}-class and serves as the model for the 
 * watchlist-table.
 * Moreover, it manages all the {@link #WatchlistStock}-instances in its <code>tablelist</code>.
 * 
 * @author Benjamin Birner
 *
 */
public class WatchlistTableModel extends AbstractTableModel {
	
	private String[] columnNames = {"Aktie", "Symbol", "Kurs", "Tagesänderung in %"};
	private ArrayList<WatchlistStock> tablelist = new ArrayList<>(20);
	private ColoredTableCellRenderer renderer = new ColoredTableCellRenderer(this);
	

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return tablelist.size();
	}

	@Override
	public String getColumnName(int index) {
		return columnNames[index];
	}
	
	@Override
	public Object getValueAt(int row, int col) {             
		if(col == 2) {return tablelist.get(row).getPrice();}
		if(col == 3) {return tablelist.get(row).getPercent();}
		if(col == 0) {return tablelist.get(row).getDescription();
		}else { return tablelist.get(row).getSymbol();}
		
	}

	
	
	/**
	 * gets the symbols from the <code>tablelist</code> at the positions that are defined by the entries
	 * in the <code>indices</code>-array.
	 * This method is called up after selecting watchlist-entries in order to open the selected stocks in a chart.
	 * 
	 * @param indices each entry defines the position in the <code>tablelist</code> for the required symbol.
	 * @return <code>String</code>-array with the selected symbols from the <code>tablelist</code>.
	 */
	public String[] getSymbolsAt(int[] indices) {
		String[] symbols = new String[indices.length];
		for(int i = 0; i < indices.length; i++) {
			symbols[i] = tablelist.get(indices[i]).getSymbol(); 
		}
		return symbols;
	}

	
	@Override
	public Class<?> getColumnClass(int colIndex){
		if(colIndex == 0 || colIndex == 1) { return String.class;}
		else {return Double.class;}                           
	}
	
	
	/**
	 * adds a stock which is represented by the <code>WatchlistStock</code>-object to the watchlist.
	 * 
	 * 
	 * @param stock the stock that should be added. It is represented by the <code>WatchlistStock</code>-object.
	 * @param index defines the position in the <code>tablelist</code> at which the entry must be added to maintain the order
	 */
	public void addStockToTable(WatchlistStock stock, int index) {
		if(index < 0) {
			tablelist.add(-index - 1, stock);
			fireTableRowsInserted(-index-1, -index-1);
		}
	}
	
	
	/**
	 * removes the stocks from the <code>tablelist</code>  according to the position that is defined by the
	 * <code>indices</code>-array. This array contains the selected indices concerning the table in the
	 * {@link #View.Watchlist}.
	 * So, this method is called up if the user selects some watchlist-entries that should be deleted.
	 * 
	 * @param indices each entry defines the position in the <code>tablelist</code> for the entry that should be deleted.
	 * @return <code>String</code>-array that contains all the symbols of the delete entries.
	 */
	public String[] removeStocks(int[] indices) { 
		 //In order that the watchlist works correctly, it is important here that the deletion is downwards and 
	     //that it begins with the greatest index.
		Arrays.sort(indices);                             
		String[] symbols = new String[indices.length];
		for(int i = indices.length-1; 0 <= i ; i--) {
			symbols[i] = tablelist.get(indices[i]).getSymbol();
			tablelist.remove(indices[i]);
			fireTableRowsDeleted(indices[i], indices[i]);	
		}
		return symbols;
	}
	
	
	/**
	 * deletes all entries from the <code>tablelist</code>.
	 */
	public void clearWL() {
		tablelist.clear();
	}
	
	
	
	/**
	 * gets the previous price for the stock which is defined by the parameter <code>row</code>.
	 * This method is only called up by the the {@link #ColoredTableCellRenderer}-class to change 
	 * the Background to the correct color.
	 * 
	 * @param row defines the row of the stock to which the previous price is required. 
	 * @return the previous price.
	 */
	public double getPreviousPriceAtRow(int row) {
		WatchlistStock stock = tablelist.get(row);
		return stock.getPreviousPrice();
	}
	
	
	/**
	 * gets the CellRenderer
	 * 
	 * @return the CellRenderer
	 */
	public ColoredTableCellRenderer getCellRenderer() {
		return renderer;
	}
	
	
	
	/**
	 * gets the <code>tablelist</code>-index to the stock that is defines by the parameter <code>symbol</code>.
	 * This method is often used to check if a stock is in the <code>tablelist</code> and to get the correct
	 * index in order maintain the list-order if an entry should be added.
	 * 
	 * @param symbol the symbol that should be checked if it is in the list
	 * @return the position of the entry in the <code>tablelist</code> if it contains this symbol.
	 *         Otherwise a minus integer that can be used to add this symbol at the correct position
	 *         in order to maintain the list-order.This minus integer must be used like this:
	 *         -integer-1  
	 */
	public int getIndex(String symbol) {
		return Collections.binarySearch(tablelist, symbol);
	}
	
	
	/**
	 * gets the symbols of all the existing watchlist-entries.
	 * 
	 * @return <code>String</code>-array that contains all the symbols of the existing watchlist-entries.
	 */
	public String[] getAllSymbols() {                     
		String[] symbols = new String[tablelist.size()];
		for(int i = 0; i < tablelist.size(); i++) {
			symbols[i] = tablelist.get(i).getSymbol();
		}
		return symbols;
	}
	
	
	
	/**
	 * gets the color-flag. This flag is required in the 
	 * ColoredTableCellRenderer to decide if a color or white is required.
	 * It also insures that the renderer works correct.
	 * 
	 * @return true if color is required else false
	 */
	public boolean getColorFlag(int row) {
		WatchlistStock stock = tablelist.get(row);
		return stock.getColorFlag();
	}
	
	
	
	
	/**
	 * sets the color-flag. This flag is required in the 
	 * ColoredTableCellRenderer to decide if a color or white is required.
	 * It also insures that the renderer works correct.
	 * 
	 * @param the value of the flag
	 */
	public void setColorFlag(int row, boolean flag) {
		tablelist.get(row).setColorFlag(flag);;
	}
	
	
	
	
	/**
	 * creates a list that contains all watchlist-entries represented by {@link #WatchlistPersistence}-objects.
	 * This method is only used in the store process to get the data that must be stored.
	 * 
	 * @return <code>LinkedList</code> that contains all watchlist-entries represented by {@link #WatchlistPersistence}-objects.
	 */
	public LinkedList<WatchlistPersistence> createWLPersistenceList(){
		LinkedList<WatchlistPersistence> list = new LinkedList<WatchlistPersistence>();
		for(int i = 0; i < tablelist.size(); i++) {
			list.addLast(new WatchlistPersistence(tablelist.get(i).getSymbol(), tablelist.get(i).getDescription()));
		}
		return list;
	}
	
	
	/**
	 * updates the previous close price for the stock that is defined by the parameter <code>symbol</code>.
	 * It is only called up by the {@link #TaskOpeningPrice}-class.    
	 * 
	 * @param symbol defines the symbol of which the previous price should be updated
	 * @param price the previous price
	 */
	public void updatePreviousClosePrice(String symbol, double price){
		int index = Collections.binarySearch(tablelist,  symbol);
		if(index >= 0) {
			tablelist.get(index).setPreviousClosePrice(price);
		}
	}
	
	
	/**
	 * updates the watchlist-entry that is defined by the parameter <code>symbol</code> if a 
	 * new price is available.
	 * It is only called up by the {@link #StocksRealTimeClient}-class and its <code>onMessage</code>-method.
	 * 
	 * @param symbol defines the stock that should be updated
	 * @param price the latest price that is available for this stock
	 * @param time the timestamp that belongs to this stock
	 */
	public void updateTablelist(String symbol, double price, long time) { 
		int index = Collections.binarySearch(tablelist, symbol);
		//checks if the tablelist contains the symbol
		if(index >= 0) {
			//gets the symbol and updates its values
			WatchlistStock stock = tablelist.get(index);
			stock.setColorFlag(true);
			stock.setPreviousPrice(stock.getPrice());
			stock.setPrice(price);
			stock.setPercent(price);
			stock.setTime(time); 
			//sets the color back after 800ms
			javax.swing.Timer timer = new javax.swing.Timer(999, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int index2 = Collections.binarySearch(tablelist, symbol);
					if(index2 >= 0) {
						setColorFlag(index2, false);
						fireTableCellUpdated(index2,2);
					}
				}
			});
			timer.setRepeats(false);
			timer.start();
			//fires table cell updated
			fireTableCellUpdated(index,2);
			fireTableCellUpdated(index,3);
		}
	}
}
