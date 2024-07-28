package View.dialogsAndFrames;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.ListSelectionModel;

import Model.indicatorModel.IndicatorModel;
import View.chart.Chart;



/**
 * This class models the Dialog-Frame regarding the different Indicators.
 * It is used to select an indicator in order to add or remove an entry.
 * It manages a list to display the existing Indicators.
 * And it manages another list to display the drawn indicators to the corresponding chart.
 * 
 * @author Benjamin Birner
 *
 */
public class IndicatorDialog extends JDialog  {
	
	//contains all the different indicator-types
	private JList<String> indiList;
	
	//contains all the drawn indicators to the corresponding chart
	private JList<String> drawnList;
	
	private JButton addB;
	private JButton deleteB;
	private JButton okB;
	
	//the chart to which this dialog belongs to
	private Chart chart;
	private IndicatorModel indiModel;
	
	public IndicatorDialog(Chart chart, IndicatorModel indiModel) {   
		
        this.indiModel = indiModel;
        this.chart = chart;
        setModal(true);
		
		//creates the basic panel
		JPanel basic = new JPanel();
		basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
		add(basic);
		
		basic.add(Box.createRigidArea(new Dimension(0,10)));
		
		//creates the label for the indicator-types
		JPanel availableLabelP = new JPanel(new FlowLayout(FlowLayout.LEFT,20,5));
		JLabel availableL = new JLabel("Verfügbare Indikatortypen");
		availableLabelP.add(availableL);
		basic.add(availableLabelP);
		
		//creates the list for the different indicator-types
		JPanel addP = new JPanel();
		addP.setLayout(new BoxLayout(addP, BoxLayout.X_AXIS));
		String[] indi = indiModel.getAllIndiDescriptions();
		indiList = new JList<String>(indi);
		indiList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		indiList.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		indiList.setSelectedIndex(0);
		JScrollPane scrollP = new JScrollPane();
		scrollP.getViewport().add(indiList);
		scrollP.setPreferredSize(new Dimension(250,200));
		addP.setBorder(BorderFactory.createEmptyBorder(10,20,20,20));
		addP.add(scrollP);
		
		addP.add(Box.createRigidArea( new Dimension(20,0)));
		
		
		//creates the add-Button
		addB = new JButton("Hinzufügen");
		addP.add(addB);
		basic.add(addP);
		
		basic.add(Box.createRigidArea( new Dimension(0,20)));
		
		
		//creates the label for the drawn indicators
		JPanel drawnIndiLabelP = new JPanel(new FlowLayout(FlowLayout.LEFT,20,5));
		JLabel drawnIndiL = new JLabel("Eingezeichnete Indikatoren");
		drawnIndiLabelP.add(drawnIndiL);
		basic.add(drawnIndiLabelP);
		
		//creates the list for the drawn indicators
		JPanel drawnIndiP = new JPanel();
		drawnIndiP.setLayout(new BoxLayout(drawnIndiP, BoxLayout.X_AXIS));
		String[] drawnIndi = indiModel.getAllDrawnIndis(chart.getID());
		drawnList = new JList<String>(drawnIndi);
		drawnList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		drawnList.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		JScrollPane scrollP2 = new JScrollPane();
		scrollP2.getViewport().add(drawnList);
		scrollP2.setPreferredSize(new Dimension(250,200));
		drawnIndiP.setBorder(BorderFactory.createEmptyBorder(10,20,20,20));
		drawnIndiP.add(scrollP2);
		
        drawnIndiP.add(Box.createRigidArea( new Dimension(20,0)));
		
        
        //creates the delete-Button
		deleteB = new JButton(" Entfernen ");
		drawnIndiP.add(deleteB);
		basic.add(drawnIndiP);
		
		
		//creates the ok-Button
		JPanel okP = new JPanel();
		okB = new JButton("ok");
		okP.add(okB, BorderLayout.CENTER);
		basic.add(okP);
		
		basic.add(Box.createRigidArea(new Dimension(0,10)));
		
		//frame settings
		pack();
		setTitle("Indikatoren");
		setSize(500,400);
		setResizable(false);
		setLocationRelativeTo(null);   
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}
	
	
	
	/**
	 * returns the selected indices concerning the list for the indicator-types.
	 * 
	 * @return the selected indices concerning the list for the indicator-types.
	 */
	public int getSelectedIndi() {
		return indiList.getSelectedIndex();
	}
	
	
	
	
	/**
	 * returns the selected indices concerning the list for the drawn indicators.
	 * 
	 * @return the selected indices concerning the list for the drawn indicators.
	 */
	public int getSelectedDrawnIndi() {
		return drawnList.getSelectedIndex();
	}
	
	
	/**
	 * returns the selected value concerning the list for the drawn indicators.
	 * 
	 * @return the selected value concerning the list for the drawn indicators.
	 */
	public String getSelectedValue() {
		return drawnList.getSelectedValue();
	}
	
	
	
	/**
	 * updates the lists for the indicator-types and the drawn indicators.
	 * This method is called up if the user adds or deletes a indicator-entry
	 * display it immediately in the list.
	 */
	public void refreshList() {
		indiList.setListData(indiModel.getAllIndiDescriptions());
		drawnList.setListData(indiModel.getAllDrawnIndis(chart.getID()));
	}
	
	
	
	
	/**
	 * adds a listener to the <code>addB</code>-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void addAddIndiListener(ActionListener listener) {
		addB.addActionListener(listener);
	}
	
	
	

	/**
	 * adds a listener to the <code>deleteB</code>-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void addDeleteIndiListener(ActionListener listener) {
		deleteB.addActionListener(listener);
	}
	
	
	
	/**
	 * adds a listener to the <code>okB</code>-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void addIndiOkButtonListener(ActionListener listener) {
		okB.addActionListener(listener);
	}
	
	
	/**
	 * sets the list-index regarding the <code>indiList</code>.
	 * 
	 * @param index defines the index that should be set.
	 */
	public void setSelectedIndiListIndex(int index) {
		indiList.setSelectedIndex(index);
	}

}
