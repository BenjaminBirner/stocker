package View.dialogsAndFrames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;





/**
 * This class models the Dialog-Frame regarding the Moving Average.
 * It is used to parameterize a new MA.
 * Listeners can be added to the different components.
 * 
 * @author Benjamin Birner
 *
 */
public class GDDialog extends JDialog {
	
	//to enter the parameter for the period
	private JTextField textfield;
	private JButton ok;
	
	
	
	public GDDialog() {
		setModal(true);
		
		//creates the basic panel
		JPanel basic = new JPanel();
		basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
		add(basic);
		
		basic.add(Box.createRigidArea(new Dimension(0,10)));
		
		
		//creates the label regarding the period
		JPanel numberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,20,5));
		JLabel numberLabel = new JLabel("Bitte Anzahl der Schlusskurse eingeben:");
		numberPanel.add(numberLabel);
		basic.add(numberPanel);
		
		
		//creates the panel for the textfield and the textfield itself
		JPanel tfPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,20,5));
		textfield = new JTextField(26);
		textfield.setEditable(true);
		tfPanel.add(textfield);
		basic.add(tfPanel);
		
		basic.add(Box.createRigidArea(new Dimension(0,10)));
		
		
		//creates the ok-Button
		JPanel okPanel = new JPanel();
		ok = new JButton("ok");
		okPanel.add(ok,BorderLayout.CENTER);
		basic.add(okPanel);
		basic.add(Box.createRigidArea(new Dimension(0,10)));
		
		
		//frame settings
		setTitle ("Gleitender Durchschnitt");
		setSize(350,150);
		setResizable(false);
		setLocationRelativeTo(null);                   
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	
	
	
	/**
	 * gets the text-field-entry concerning the period for the MA.
	 * 
	 * @return the text-field-entry as String.
	 */
	public String getParameter() {
		return textfield.getText();
	}
	
	
	
	
	/**
	 * adds a listener to the <code>ok</code>-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void addGDOkButtonListener(ActionListener listener) {
		ok.addActionListener(listener);
	}
	
	
	/**
	 * clears the <code>JTextField</code>.
	 */
	public void clearTF() {
		textfield.setText("");
	}

	
	/**
	 * adds a listener to the <code>JTextField</code>.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setGDTFListener(ActionListener listener) {  
		textfield.addActionListener(listener);
	}
}
