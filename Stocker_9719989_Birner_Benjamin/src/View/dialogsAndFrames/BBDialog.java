package View.dialogsAndFrames;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import Model.indicatorModel.IndicatorModel;



/**
 * This class models the Dialog-Frame regarding the Bollinger Bands.
 * It is used to parameterize a new BB.
 * It manages a list to display the existing Moving Averages.
 * Listeners can be added to the different components.
 * 
 * @author Benjamin Birner
 *
 */
public class BBDialog extends JDialog {
	
	private JList<String> list;
	private JTextField factorTf;
	private JButton addB;
	private JButton okB;
	private JButton exitB;
	private IndicatorModel indiModel;
	private int chartID;

	public BBDialog( IndicatorModel indiModel, int chartID) {
		this.indiModel = indiModel;
		this.chartID = chartID;
		setModal(true);
		
		//creates the basic panel
		JPanel basic = new JPanel();
		basic.setLayout(new BoxLayout(basic,BoxLayout.Y_AXIS));
		add(basic);
		
		//creates the different panels concerning the Moving Average
		JPanel gdPanel = new JPanel();
		gdPanel.setLayout(new BoxLayout(gdPanel, BoxLayout.X_AXIS));
		JPanel gdLabButP = new JPanel();
		gdLabButP.setLayout(new BoxLayout(gdLabButP, BoxLayout.Y_AXIS));
		JPanel listP = new JPanel();
		
		//creates all the components concerning the Moving Average (the n for the BBs)
		JLabel gdLabel = new JLabel("Gleitenden Durchschnitt wählen:");
		addB = new JButton("GD hinzufügen");
		gdLabButP.add(gdLabel);
		gdLabButP.add(Box.createRigidArea(new Dimension(0,25)));
		gdLabButP.add(addB);
		gdPanel.add(gdLabButP);
		gdPanel.add(Box.createRigidArea(new Dimension(20,0)));
		String[] drawnGDs = indiModel.getAllDrawnGDs(chartID);
		list = new JList<String>(drawnGDs);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		list.setSelectedIndex(0);
		JScrollPane scrollP = new JScrollPane();
		scrollP.getViewport().add(list);
		scrollP.setPreferredSize(new Dimension(100,57));
		listP.add(scrollP);
		gdPanel.add(listP);
		gdPanel.setBorder(BorderFactory.createEmptyBorder(10,20,20,20));
		basic.add(gdPanel);
		
		//creates the components concerning the factor for the BBs
		JPanel facP = new JPanel();
		facP.setLayout(new BoxLayout(facP, BoxLayout.X_AXIS));
		JLabel fac = new JLabel("Factor eingeben:");
		facP.add(fac);
		facP.add(Box.createRigidArea( new Dimension(89,0)));
		factorTf = new JTextField(20);
		factorTf.setEditable(true);
		facP.add(factorTf);
		facP.setBorder(BorderFactory.createEmptyBorder(0,20,20,20));
		basic.add(facP);
		
		//creates the different JButtons
		JPanel buttonP = new JPanel();
		buttonP.setLayout(new BoxLayout (buttonP, BoxLayout.X_AXIS));
		buttonP.setBorder(BorderFactory.createEmptyBorder(0,20,20,20));
		okB = new JButton("ok");
		exitB = new JButton("Abbruch");
		buttonP.add(okB);
		buttonP.add(Box.createRigidArea(new Dimension(15,0)));
		buttonP.add(exitB);
		basic.add(buttonP);
		
		//the frame settings
		setTitle("BollingerBänder");
		setResizable(false);
		setSize(400,220);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	
	
	/**
	 * returns the selected index concerning the list for the MAs.
	 * 
	 * @return the selected index concerning the list for the MAs.
	 */
	public int getSelectedIndex() {
		return list.getSelectedIndex();
	}
	
	
	
	/**
	 * updates the list for the MAs.
	 * This method is called up if the user adds
	 * a MA to display it immediately in the list.
	 */
	public void refreshList() {
		list.setListData(indiModel.getAllDrawnGDs(chartID));
		
		}
	
	
	
	/**
	 * gets the text-field-entry concerning the factor for the BBs.
	 * 
	 * @return the text-field-entry as String.
	 */
	public String getFactor() {
		return factorTf.getText();
	}
	
	
	/**
	 * adds a listener to the <code>okB</code>-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void addBBOkButtonListener(ActionListener listener) {
		okB.addActionListener(listener);
	}
	
	
	
	/**
	 * adds a listener to the <code>addB</code>-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void addBBaddGDListener(ActionListener listener) {
		addB.addActionListener(listener);
	}
	
	
	
	
	/**
	 * adds a listener to the <code>exitB</code>-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void addExitButtonListener(ActionListener listener) {
		exitB.addActionListener(listener);
	}
	
	
	/**
	 * clears the <code>JTextField</code>.
	 */
	public void clearTF() {
		factorTf.setText("");
	}
	
	
	/**
	 * sets the list-index regarding the <code>list</code>.
	 * 
	 * @param index defines the index that should be set.
	 */
	public void setSelectedGDListIndex(int index) {
		list.setSelectedIndex(index);
	}
	
	

	/**
	 * adds a listener to the <code>JTextField</code>.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setFactorTFListener(ActionListener listener) {  
		factorTf.addActionListener(listener);
	}
}
