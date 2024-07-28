package View.dialogsAndFrames;


import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;



import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import Model.watchlist.FramePersistence;
import Model.watchlist.WatchlistTableModel;
import View.chart.Chart;
import Model.persistence.PropertiesCache;
import Model.watchlist.ColoredTableCellRenderer;


/**
* This class models the watchlist.
* It displays all watchlist-entries in a table.
* 
* @author Benjamin Birner
*
*/
public class Watchlist extends JInternalFrame {
	
	private JMenuItem chart;
	private JMenuItem delete;
	private JMenuItem search;
	
	//the model that contains all entries and data
	private WatchlistTableModel model;
	
	private JTable table;
	private StockerMainFrame frame;
	
	
	
	//is called up if there is no persistence data
	public Watchlist(WatchlistTableModel model , StockerMainFrame frame) {    
		super("Watchlist", true,true,true);
		
		initialize(model,frame);
		setVisible(true);
	}	
	
	//is called up if there is persistence data
	public Watchlist(WatchlistTableModel model , StockerMainFrame frame, FramePersistence frameData) {
		
		super("Watchlist", true,true,true);
		initialize(model,frame);

		
		/*
		 * the check in the following block is in the restore-process of importance.
		 * It avoids a false behavior regarding one special case
		 */
		
		PropertiesCache prop = PropertiesCache.getInstance();
		int height;
		int width;
		String minH = prop.getProperty("height");
		String minW = prop.getProperty("width");
		if(minH != null) {
			height = Integer.parseInt(minH);
			height = (frameData.getHeight() < height ? height : frameData.getHeight());
		}else {
			height = frameData.getHeight();
		}
		if(minW != null) {
			width = Integer.parseInt(minW);
			width = (frameData.getWidth() < width ? width : frameData.getWidth());
		}else {
			width = frameData.getWidth();
		}
		
		setSize(width,height);
		setLocation(frameData.getLocX(),frameData.getLocY());
		frame.getDesktopPane().setComponentZOrder(this,frameData.getZOrder());
		setVisible(true);
	}
	
	
	private void initialize(WatchlistTableModel model , StockerMainFrame frame) {

		
		this.model = model;
		this.frame = frame;
		
		
		// creates the menu "Aktie hinzufügen"
		JMenuBar menubar = new JMenuBar();
		JMenu add = new JMenu("Aktie hinzufügen");
		search = new JMenuItem("Aktiensuche");
		search.setToolTipText("Aktie suchen");
		add.add(search);
		menubar.add(add);
		setJMenuBar(menubar);
	
		//sets the row-sorter and the model to the table
		final TableRowSorter<AbstractTableModel> rowSorter = new TableRowSorter<>(model); 
		table = new JTable(this.model);
		table.setRowSorter(rowSorter);
		
		//creates the popup-menu to open entries in a chart or to delete entries
		JPopupMenu popup = new JPopupMenu();
		JMenuItem title = new JMenuItem("markierte Einträge");
		chart = new JMenuItem("in Chart öffnen");
		delete = new JMenuItem("löschen");
		popup.add(title);
		popup.addSeparator();
		popup.add(chart);
		popup.add(delete);
		table.add(popup);
		table.addMouseListener(new MouseAdapter() {                           
			@Override public void mousePressed( MouseEvent me) {              
				if(me.isPopupTrigger())											
					popup.show( me.getComponent(), me.getX(), me.getY());
			}
		});
		
		//sets the ColoredTableCellRenderer
		ColoredTableCellRenderer renderer = model.getCellRenderer();
		table.setDefaultRenderer(Double.class, renderer);
		JScrollPane scrollP = new JScrollPane();
		scrollP.getViewport().add(table);
		add(scrollP);   
		
		
		//gets and sets the correct size regarding the frame
	    PropertiesCache prop = PropertiesCache.getInstance();
	    String height = prop.getProperty("height");
	    String width = prop.getProperty("width");
	    int heightInt ;
	    int widthInt ;
	    if(height == null) {
	    	heightInt = Chart.MIN_HEIGHT;
	    }else {
	    	heightInt = Integer.parseInt(height);
	    }
	    if(width == null) {
	    	widthInt = Chart.MIN_WIDTH;
	    }else {
	    	widthInt = Integer.parseInt(width);
	    }
	    setMinimumSize(new Dimension(widthInt, heightInt));
	    setSize(widthInt ,heightInt);	
		
		
		this.frame.addJInternalFrame(this);
	
	}
	
	
	
	

	/**
	 * returns the selected rows concerning the <code>table</code>.
	 * 
	 * @return <code>int</code>-array with the selected rows concerning the <code>table</code>.
	 */
	public int[] getSelectedRows() {
		int[] selection = table.getSelectedRows();
		//as the rows can be sorted by several, the row-index must be converted 
		for(int i = 0; i < selection.length; i++) {
			selection[i] = table.convertRowIndexToModel(selection[i]);
		}
		return selection;
	}
	
	
	
	/**
	 * adds a listener to the <code>search</code>-JMenuItem.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setWLSearchMenueListener(ActionListener listener) {
		search.addActionListener(listener);
	}

	
	
	/**
	 * adds a listener to the <code>chart</code>-JMenuItem.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setWLChartOpenListener(ActionListener listener) {
		chart.addActionListener(listener);
	}
	
	
	
	/**
	 * adds a listener to the <code>delete</code>-JMenuItem.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setWLDeleteListener(ActionListener listener) {
		delete.addActionListener(listener);
	}

}
