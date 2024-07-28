package View.dialogsAndFrames;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.WindowListener;
import java.awt.BorderLayout;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import Model.watchlist.FramePersistence;

													
/**
 * This class models the programs´s main-frame.
 * It provides the Menu and manages all the different {@link #JInternalFrame}-instances.
 * 
 * @author Benjamin Birner
 *
 */
public class StockerMainFrame extends JFrame {
	
	//the different menu-points
	private JMenuItem fileClose;
	private JMenuItem search;
	private JMenuItem watchlist;
	private JMenuItem configItem;
	private JMenu window;
	
	private final JDesktopPane desktopPane = new JDesktopPane();
	
	//is called up if there exists persistence data
	public StockerMainFrame(FramePersistence frameData) {
		setSize(frameData.getWidth(),frameData.getHeight());
		setLocation(frameData.getLocX(),frameData.getLocY());
		addMenuBar();
		initalizeFrame();
	}
	//is called up if there exists no persistence data
	public StockerMainFrame() {
		setSize(1000, 600);
		setLocationRelativeTo(null);
		addMenuBar();
		initalizeFrame();

	}
	
	//initializes the frame with some general settings
	private void initalizeFrame() {
		setTitle("Stocker_Birner_Benjamin_q9719989");
		add(desktopPane); 
		setVisible(true);
	}
	
	//adds the menu-bar
	private void addMenuBar () {
		
		JMenuBar menubar = new JMenuBar();
		
		//creates the menu "Datei"
		JMenu file = new JMenu("Datei");
		fileClose = new JMenuItem("Beenden");
		fileClose.setToolTipText("Programm beenden");
		fileClose.setAccelerator(KeyStroke.getKeyStroke('C',InputEvent.CTRL_DOWN_MASK));
		file.add(fileClose);
		menubar.add(file);
		
		//creates the menu "Aktien"
		JMenu stocks = new JMenu("Aktien");
		search = new JMenuItem("Aktiensuche");
		search.setToolTipText("nach Aktien suchen");
		stocks.add(search);
		
		watchlist = new JMenuItem("Watchlist");
		watchlist.setToolTipText("Watchlist öffnen");
		stocks.add(watchlist);
		menubar.add(stocks);
		
		//creates the menu "Einstellungen"
		JMenu config = new JMenu("Einstellungen");
		configItem  = new JMenuItem("Einstellungen ändern");
		configItem.setToolTipText("Einstellungen ändern");
		config.add(configItem);
		menubar.add(config);
		
		//creates the menu "Fenster" which manages the open JInternalFrames
	    window = new JMenu("Fenster");
	    window.setToolTipText("geöffnete Fenster");
		menubar.add(window);
		
		setJMenuBar(menubar);
		setVisible(true);          
		
	}
	
	
	/**
	 * adds an open JInternaFrame to the menu "Fenster" which manages all open JInternalFrames.
	 * It is called up if a chart or the watchlist is opened.
	 * 
	 * @param menuItem the JMenuItem that should be added to the menu "Fenster".
	 * @param listener the listener for this JMenuItem.
	 */
	public void addOpenWindowToFenster(JMenuItem menuItem, ActionListener listener) {    
		window.add(menuItem);
		menuItem.addActionListener(listener);
		
	}
	
	
	
	/**
	 * removes the <code>menuItem</code> from the menu "Fenster".
	 * It is called up if a JInternalFrame is closed.
	 * 
	 * @param menuItem
	 */
	public void removeMenuItem(JMenuItem menuItem) {
		window.remove(menuItem);
	}
	
	
	
	/**
	 * gets the JDesktopPane that belongs to this frame.
	 * 
	 * @return the JDesktopPane that belongs to this frame.
	 */
	public JDesktopPane getDesktopPane() {  
		return desktopPane;
	}
	
	
	
	/**
	 * adds a listener to the <code>fileClose</code>-JMenuItem.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setFileCloseMItemListener(ActionListener listener) {
		fileClose.addActionListener(listener);
	}
	
	
	
	/**
	 * adds a listener to the <code>search</code>-JMenuItem.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setSearchMItemListener(ActionListener listener) {
		search.addActionListener(listener);
	}
	
	
	
	
	/**
	 * adds a listener to the <code>watchlist</code>-JMenuItem.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setWatchlistMItemListener(ActionListener listener) {
		watchlist.addActionListener(listener);
	}
	
	
	
	
	/**
	 * adds a listener to the <code>configItem</code>-JMenuItem.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setConfigMItemListener(ActionListener listener) {
		configItem.addActionListener(listener);
	}
	
	
	
	/**
	 * adds a JInternalFrame to the desktopPane.
	 * 
	 * @param internalFrame the JInternalFrame that should be added.
	 */
	public void addJInternalFrame(JInternalFrame internalFrame) {
		desktopPane.add(internalFrame);                       
	}                                                       
	                                                  
	
	/**
	 * adds a <code>WindowListener</code> to this MainFrame.
	 * 
	 * @param listener the WindowListener that should be added.
	 */
	public void addMainFrameClosingListener(WindowListener listener) {
		addWindowListener(listener);                       
	}                           
	
}
