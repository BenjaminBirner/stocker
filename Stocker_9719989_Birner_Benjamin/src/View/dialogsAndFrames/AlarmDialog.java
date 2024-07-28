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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import Model.alarmModel.AlarmDataModel;


/**
 * This class models the Dialog-Frame regarding the alarms.
 * It manages a list to display the existing alarms.
 * Listeners can be added to the different components.
 * 
 * @author Benjamin Birner
 *
 */
public class AlarmDialog extends JDialog {      
    
	//to enter the value to define the alarm
	private JTextField textField;
	private JButton addB;
	private JButton deleteB;
	private JButton okB;
	                   
    private String symbol; 
    //contains the existing alarms
	private JList<String> list;
	private AlarmDataModel alarmModel;
	
	public AlarmDialog(AlarmDataModel alarmModel, String symbol) {
		
		setModal(true);
		this.alarmModel = alarmModel;
		this.symbol = symbol;
		
		//creates the basic panel
		JPanel basic = new JPanel();
		basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
		add(basic);
		
		basic.add(Box.createRigidArea(new Dimension(0,10)));
		
		//creates label "Neuer Alarm"
		JPanel newAlarmLabelP = new JPanel(new FlowLayout(FlowLayout.LEFT,20,5));
		JLabel alarmL = new JLabel("Neuer Alarm");
		newAlarmLabelP.add(alarmL);
		basic.add(newAlarmLabelP);
		
		//creates text field und button "Hinzufügen"
		JPanel newP = new JPanel();
		newP.setLayout (new BoxLayout(newP, BoxLayout.X_AXIS));
		JPanel textFieldP = new JPanel(new FlowLayout(FlowLayout.LEFT,20,5));
		textField = new JTextField(29);
		textField.setEditable(true);
		textFieldP.add(textField);
		JPanel addButtonP = new JPanel(new FlowLayout(FlowLayout.LEFT,20,5));
		addB = new JButton("Hinzufügen");
		addButtonP.add(addB);
		newP.add(textFieldP);
		newP.add(addButtonP);
		basic.add(newP);
		
		basic.add(Box.createRigidArea(new Dimension(0,10)));
		
		//creates label "Eingezeichnete Alarme"
		JPanel drawnAlarmLabelP = new JPanel(new FlowLayout(FlowLayout.LEFT,20,5));
		JLabel drawnAlarmL = new JLabel("Eingezeichnete Alarme");
		drawnAlarmLabelP.add(drawnAlarmL);
		basic.add(drawnAlarmLabelP);
		
		//creates the list
		JPanel drawnAlarmP = new JPanel();
		drawnAlarmP.setLayout(new BoxLayout(drawnAlarmP, BoxLayout.X_AXIS));
		String[] alarms = alarmModel.getAllAlarmsInString(symbol);
		list = new JList<String>(alarms);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		JScrollPane scrollP = new JScrollPane();
		scrollP.getViewport().add(list);
		scrollP.setPreferredSize(new Dimension(250,200));
		drawnAlarmP.setBorder(BorderFactory.createEmptyBorder(10,20,20,20));
		drawnAlarmP.add(scrollP);
		
        drawnAlarmP.add(Box.createRigidArea( new Dimension(20,0)));
		
        //creates button "Entfernen" and "ok"
		deleteB = new JButton(" Entfernen ");
		drawnAlarmP.add(deleteB);
		basic.add(drawnAlarmP);
		JPanel okP = new JPanel();
		okB = new JButton("ok");
		okP.add(okB, BorderLayout.CENTER);
		basic.add(okP);
		
		basic.add(Box.createRigidArea(new Dimension(0,10)));
		
		
		//window settings
		setTitle ("Alarme");
		setResizable(false);
		setSize(500,400);
		setLocationRelativeTo(null);  
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		
		
	}
	
	/**
	 * returns the selected indices concerning the list.
	 * 
	 * @return the selected indices concerning the list.
	 */
	public int[] getSelectedIndices() {
		return list.getSelectedIndices();
	}
	
	
	
	/**
	 * updates the list.
	 * This method is called up while the dialog is opened e.g. if the user enters
	 * a new alarm to display it immediately in the list.
	 */
	public void refreshList() {
		list.setListData(alarmModel.getAllAlarmsInString(symbol));
		
		}
	
	
	
	/**
	 * gets the text-field-entry.
	 * 
	 * @return the text-field-entry as String.
	 */
	public String getTextfieldEntry() {
		return textField.getText();
	}
	
	
	
	/**
	 * adds a listener to the <code>addB</code>-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void addAddAlarmListener(ActionListener listener) {
		addB.addActionListener(listener);
	}
	
	
	
	/**
	 * adds a listener to the <code>deleteB</code>-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void addDeleteAlarmListener(ActionListener listener) {
		deleteB.addActionListener(listener);
	}
	
	
	
	/**
	 * adds a listener to the <code>okB</code>-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void addAlarmOkButtonListener(ActionListener listener) {
		okB.addActionListener(listener);
	}
	
	
	/**
	 * adds a listener to the <code>JTextField</code>.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setAlarmTFListener(ActionListener listener) {  
		textField.addActionListener(listener);
	}
	
	
	
	/**
	 * clears the <code>JTextField</code>.
	 * 
	 */
	public void clearTF() {
		textField.setText("");
	}

}
