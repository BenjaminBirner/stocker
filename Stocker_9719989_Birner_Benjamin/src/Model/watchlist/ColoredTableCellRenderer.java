package Model.watchlist;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


/**
 * This class is responsible for the watchlist-table to color the cell for the price.
 * If the price is fallen, the cell gets red.
 * If the price is raised, the cell gets green.
 * 
 * @author Benjamin Birner
 *
 */
public class ColoredTableCellRenderer extends DefaultTableCellRenderer {
	
	private WatchlistTableModel watchlistModel;
	
	public ColoredTableCellRenderer( WatchlistTableModel watchlistModel){
		this.watchlistModel = watchlistModel;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column) {
		
		if( column == 2) {
			int convertedRow = table.convertRowIndexToModel(row);
			boolean flag = watchlistModel.getColorFlag(convertedRow);
			
			if(flag == false) {
				setBackground(Color.WHITE);
				setValue(value);
			}else {
				double previousPrice = watchlistModel.getPreviousPriceAtRow(convertedRow); 
				if( previousPrice  < (double) value) {
					setBackground(new Color(0,188,0));
					setValue(value);
				}else {
					if( previousPrice  > (double) value) {
						setBackground(new Color(198,11,0));
						setValue(value);
					}else {
						setBackground(Color.WHITE);
						setValue(value);
					}
				}
			}
		}else {
			setBackground(Color.WHITE);
			setValue(value);
		}
		return this;
	}
}
