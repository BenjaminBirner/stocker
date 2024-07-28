package View.dialogsAndFrames;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import Model.searchModel.SearchResultModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

/**
* This class models the Dialog-Frame regarding the stock-search.
* It is used to enter the symbol or description of the required stock
* and to select some of the search-results and open these stocks in
* a chart or add the stocks to the watchlist.
* It manages a list to display the search-results.
* 
* @author Benjamin Birner
*
*/
public class SearchJDialog extends JDialog {
	
	//to enter the symbol or description of the required stock
	private JTextField textField;
	
	private JButton searchB;
	private JButton chartB;
	private JButton watchlistB;
	private JButton exitB;
	
	//the model and the list for the searchresults
	private SearchResultModel model;                         
	private JList<String> list;
	
	
	public SearchJDialog(JFrame owner, SearchResultModel model) { 
		
		super(owner,true);             
    
		//creates the basic panel
		JPanel basic = new JPanel();
		basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
		add(basic);
		
		basic.add(Box.createRigidArea(new Dimension(0,10)));
		
		//creates label "Suchbegriff"
		JPanel searchLabelP = new JPanel(new FlowLayout(FlowLayout.LEFT,20,5));
		JLabel searchL = new JLabel("Suchbegriff");
		searchLabelP.add(searchL);
		basic.add(searchLabelP);
		
		//creates text field and button "Suche starten" 
		JPanel searchP = new JPanel();
		searchP.setLayout (new BoxLayout(searchP, BoxLayout.X_AXIS));
		JPanel textFieldP = new JPanel(new FlowLayout(FlowLayout.LEFT,20,5));
		textField = new JTextField(25);
		textField.setEditable(true);
		textFieldP.add(textField);
		JPanel searchButtonP = new JPanel(new FlowLayout(FlowLayout.LEFT,20,5));
		searchB = new JButton("Suche starten");
		searchButtonP.add(searchB);
		searchP.add(textFieldP);
		searchP.add(searchButtonP);
		basic.add(searchP);
		
		basic.add(Box.createRigidArea(new Dimension(0,10)));
		
		//creates label "Ergebnisse"
		JPanel resultLabelP = new JPanel(new FlowLayout(FlowLayout.LEFT,20,0));
		JLabel resultLabel = new JLabel("Ergebnisse");
		resultLabelP.add(resultLabel);
		basic.add(resultLabelP);
		
		//creates the list components
		JPanel listP = new JPanel(new BorderLayout());
		this.model = model;
		list = new JList<String>(model.getDescriptions());
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));  
		JScrollPane scrollP = new JScrollPane();
		scrollP.getViewport().add(list);
		scrollP.setPreferredSize(new Dimension(250, 200));
		listP.setBorder(BorderFactory.createEmptyBorder(10,20,20,20));
		
		listP.add(scrollP);
		
		basic.add(listP);
		
		//creates several buttons 
		JPanel buttonP = new JPanel();
		buttonP.setLayout(new BoxLayout(buttonP,BoxLayout.X_AXIS));
		buttonP.setBorder(BorderFactory.createEmptyBorder(10,20,20,20));
		chartB = new JButton("im Chart öffnen");
		watchlistB = new JButton("zur Watchlist hinzufügen");
		exitB = new JButton("Beenden");
		buttonP.add(chartB);
		buttonP.add(Box.createRigidArea(new Dimension(15,0)));
		buttonP.add(watchlistB);
		buttonP.add(Box.createRigidArea(new Dimension(15,0)));
		buttonP.add(exitB);
		basic.add(buttonP);
		
		//window settings
		setTitle ("Aktiensuche");
		setSize(600, 400);
		setResizable(false);
		setLocationRelativeTo(owner);
		pack();                                         
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);  
	
		}
	
	
	
	/**
	 * returns the selected indices concerning the list for the search-results.
	 * 
	 * @return <code>int</code>-array with the selected indices concerning the list for the search-results.
	 */
	public int[] getSelectedIndices() {
		return list.getSelectedIndices();
	}
	
	
	
	/**
	 * updates the list for the search-results.
	 * This method is called up just after the search to display the results immediately.
	 */
	public void refreshList() {
		list.setListData(model.getDescriptions());
	}
	
	
	
	
	/**
	 * gets the text-field-entry for the search.
	 * 
	 * @return the text-field-entry as String.
	 */
	public String getTextfieldEntry() {
		return textField.getText();
	}
	
	
	

	/**
	 * returns the selected index concerning the list for the search-results.
	 * 
	 * @return the selected index concerning the list for the search-results.
	 */
	public int getSelectedIndex() {
		return list.getSelectedIndex();
	}
	
	
	
	/**
	 * adds a listener to the <code>textField</code>-.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setSearchTFListener(ActionListener listener) {  
		textField.addActionListener(listener);
	}
	
	
	
	/**
	 * adds a listener to the <code>searchB</code>-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setSearchButtonListener(ActionListener listener) {
		searchB.addActionListener(listener);
	}
	
	
	
	/**
	 * adds a listener to the <code>chartB</code>-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setChartOpenButtonListener(ActionListener listener) {
		chartB.addActionListener(listener);
	}
	
	
	
	
	/**
	 * adds a listener to the <code>watchlistB</code>-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setWatchlistAddButtonListener(ActionListener listener) {
	    watchlistB.addActionListener(listener);
	}
	
	
	
	/**
	 * adds a listener to the <code>exitB</code>-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setExitButtonListener(ActionListener listener) {
		exitB.addActionListener(listener);
	}
	
	
	
	/**
	 * adds a MouseListener to the <code>list</code>-.
	 * 
	 * @param listener the MouseListener that should be added.
	 */
	public void setListMouseListener(MouseListener listener) {
		list.addMouseListener(listener);
	}

}
