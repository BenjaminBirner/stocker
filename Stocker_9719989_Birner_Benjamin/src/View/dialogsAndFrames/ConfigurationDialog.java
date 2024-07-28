package View.dialogsAndFrames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import Controller.URLCandleRequest;
import Model.indicatorModel.IndicatorModel;
import Model.persistence.PropertiesCache;
import View.chart.Chart;



/**
 * This class models the dialog concerning the configurations an provides 
 * the required components. It also manages three lists regarding the 
 * connection data. The data for these lists are saved in the Properties.
 * This class also does the coding and the encoding concerning the Properties-String. 
 * 
 * @author Benjamin Birner
 *
 */
public class ConfigurationDialog extends JDialog {
	
	private JTextField heightTf;
	private JTextField widthTf;
	private JTextField apiTf;
	private JTextField pullTf;
	private JTextField pushTf;
	private JButton colorAb;
	private JButton resetB;
	private JButton exitB;
	private JButton colorIb;
	private JButton okB;
	private JButton addConnB;
	private JButton removeConnB;
	private JList<String> indiList;
	private JList<String> apiList;
	private JList<String> pullList;
	private JList<String> pushList;
    private JRadioButton lineR;
    private JRadioButton candleR;
	private JRadioButton m1R;
	private JRadioButton m5R;
	private JRadioButton m10R;
	private JRadioButton m15R;
	private JRadioButton m30R;
	private JRadioButton m60R;
	private JRadioButton m240R;
	private JRadioButton dayR;
	private JRadioButton weekR;
	private JRadioButton monthR;
	private IndicatorModel indiModel;
	
	public ConfigurationDialog(IndicatorModel indiModel) {
		setModal(true);
		this.indiModel = indiModel;
		//creates the basic Panel
		JPanel basic = new JPanel();
		basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
		
		
		
		
		
		/*
		 * creates all the Panels that are required regarding the first page 
		 * in the tabbedPane
		 */
		
		JPanel chartP = new JPanel();
		chartP.setLayout(new BoxLayout(chartP, BoxLayout.Y_AXIS));
		
		JPanel typP = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		typP.setBorder(BorderFactory.createEmptyBorder(12,60,0,60));
		
		JPanel typRP = new JPanel();
		typRP.setLayout(new BoxLayout(typRP, BoxLayout.Y_AXIS));
		
		JPanel widthP = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		widthP.setBorder(BorderFactory.createEmptyBorder(42,60,5,60));
		
		JPanel heightP = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		heightP.setBorder(BorderFactory.createEmptyBorder(5,60,10,60));  
		
		
		
		
		/*
		 *creates the labels and textfields for the min height and min width
		 */
		
		JLabel heightL = new JLabel("Mindesthöhe:");
		heightTf = new JTextField(10);
		heightTf.setEditable(true);
		
		JLabel widthL = new JLabel("Mindestlänge:");
		widthTf = new JTextField(10);
		widthTf.setEditable(true);
		
		
		/*
		 * sets the current configuration regarding the min height and 
		 * min width to the textfield
		 */
		
		PropertiesCache prop = PropertiesCache.getInstance();
		String valueW = prop.getProperty("width");
		String valueH = prop.getProperty("height");
		
		if(valueW != null) {
			widthTf.setText(valueW);
		}else {
			Integer intW = Integer.valueOf(Chart.MIN_WIDTH);
			widthTf.setText(intW.toString());
		}
		
		if(valueH != null) {
			heightTf.setText(valueH);
		}else {
			Integer intH = Integer.valueOf(Chart.MIN_HEIGHT);
			heightTf.setText(intH.toString());
		}
		
		
		
		/*
		 * adds the labels and textfields to the panel
		 */
		
		heightP.add(heightL);
		heightP.add(Box.createRigidArea(new Dimension(90,0)));	
		heightP.add(heightTf);
		widthP.add(widthL);
		widthP.add(Box.createRigidArea(new Dimension(87,0)));	
		widthP.add(widthTf);
		
		
		
		/*
		 *creates the label and the radioButtons for the chart-type 
		 *configuration 
		 */
		
		JLabel typL = new JLabel("Standard-Charttyp:");
		candleR = new JRadioButton("Kerzenchart");
		lineR = new JRadioButton("Linienchart");
		ButtonGroup group1 = new ButtonGroup();
		group1.add(candleR);
		group1.add(lineR);
		
		
		/*
		 * adds the label and the radioButtons to the Panel
		 */
		
		typP.add(typL, BorderLayout.WEST);
		typRP.add(candleR);
		typRP.add(lineR);
		typP.add(Box.createRigidArea(new Dimension(44,0)));	
		typP.add(typRP, BorderLayout.EAST);
		
		
		
		/*
		 * creates the required Panels for the second page regarding the tabbedPane
		 */
		
		JPanel periodRP = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		periodRP.setBorder(BorderFactory.createEmptyBorder(7,55,10,60));
		
		JPanel periodR2P = new JPanel();
		periodR2P.setLayout(new BoxLayout(periodR2P, BoxLayout.Y_AXIS));
		
		JPanel periodR3P = new JPanel();
		periodR3P.setLayout(new BoxLayout(periodR3P, BoxLayout.Y_AXIS));
		
		JPanel periodRRP = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
	
		
		
		
		/*
		 * creates the labels and the RadioButtons for the period configuration
		 */
		
		
		JLabel periodL = new JLabel("Standard-Zeitintervall:");
		periodL.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		m1R = new JRadioButton("1 Minute");
		m5R = new JRadioButton("5 Minuten");
		m10R = new JRadioButton("10 Minuten");
		m15R = new JRadioButton("15 Minuten");
		m30R = new JRadioButton("30 Minuten");
		m60R = new JRadioButton("60 Minute");
		m240R = new JRadioButton("240 Minuten");
		dayR = new JRadioButton("1 Tag");
		weekR = new JRadioButton("1 Woche");
		monthR = new JRadioButton("1 Monat");
		ButtonGroup g3 = new ButtonGroup();
		g3.add(m1R);
		g3.add(m5R);
		g3.add(m10R);
		g3.add(m15R);
		g3.add(m30R);
		g3.add(m60R);
		g3.add(m240R);
		g3.add(dayR);
		g3.add(weekR);
		g3.add(monthR);
		
		
		
       /*
        * adds the RadioButtons and the label to the Pane
        */
		
		periodR2P.add(m1R);
		periodR2P.add(m5R);
		periodR2P.add(m10R);
		periodR2P.add(m15R);
		periodR2P.add(m30R);
		
		periodR3P.add(m60R);
		periodR3P.add(m240R);
		periodR3P.add(dayR);
		periodR3P.add(weekR);
		periodR3P.add(monthR);
		
		JPanel ll = new JPanel();
		ll.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
		ll.add(periodL);
		periodRP.add(ll);
		periodRRP.add(periodR2P);
		periodRRP.add(Box.createRigidArea(new Dimension(85,0)));	
		periodRRP.add(periodR3P);
		periodRP.add(periodRRP);
        
		
		
		/*
		 * adds the components to the Panel for the first page regarding the tabbedPane
		 */
		
		chartP.add(widthP);
		chartP.add(heightP);
		chartP.add(typP);
		chartP.add(Box.createRigidArea(new Dimension(0,10)));		
	    periodRP.setPreferredSize(new Dimension(200,200));
		chartP.add(periodRP);
	
		
		
		
		/*
		 * creates all Panes for the color configuration
		 */
		
		JPanel farbP = new JPanel();
		farbP.setLayout(new BoxLayout(farbP, BoxLayout.Y_AXIS));
		
		JPanel alarmP = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		alarmP.setBorder(BorderFactory.createEmptyBorder(50,40,10,40));
		
		JPanel indiP = new JPanel();
		indiP.setLayout(new BoxLayout(indiP, BoxLayout.X_AXIS));
		indiP.setBorder(BorderFactory.createEmptyBorder(10,40,50,40));
		
		JPanel indi2P = new JPanel();
		indi2P.setLayout(new BoxLayout(indi2P, BoxLayout.Y_AXIS));
		
		
		
		
		
		/*
		 * creates the labels, Buttons and a List that are required for the color configuration
		 */
		
		JLabel alarmL = new JLabel("Alarme:");
		colorAb = new JButton("Farbe wählen");
		
		JLabel indiL = new JLabel("Indikatoren:");
		colorIb = new JButton("Farbe wählen");
		
		String[] indi = this.indiModel.getAllIndiDescriptions();
		indiList = new JList<String>(indi);
		indiList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		indiList.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		indiList.setSelectedIndex(0);
		JScrollPane scrollP = new JScrollPane();
		scrollP.getViewport().add(indiList);
		
		
		
		
		/*
		 * adds the components for the color configuration to the Panes
		 */
		
		alarmP.add(alarmL);
		alarmP.add(Box.createRigidArea(new Dimension(90,0)));	
		alarmP.add(colorAb);
		
		indi2P.add(indiL);
		indi2P.add(Box.createRigidArea(new Dimension(0,50)));
		indi2P.add(colorIb);
		
		indiP.add(indi2P);
		indiP.add(Box.createRigidArea(new Dimension(12,0)));
		indiP.add(scrollP);
		
		farbP.add(alarmP);
		farbP.add(indiP);
		
		
		
		
		
		/*
		 * creates all Panes that are required for the connection configurations
		 */
		
		JPanel connecP = new JPanel();
		connecP.setLayout(new BoxLayout(connecP, BoxLayout.Y_AXIS));
		
		JPanel apiP1 = new JPanel();
		apiP1.setLayout(new BorderLayout());
		apiP1.setBorder(BorderFactory.createEmptyBorder(25,20,10,20));  
		
		JPanel pullP1 = new JPanel();
		pullP1.setLayout(new BorderLayout());
		pullP1.setBorder(BorderFactory.createEmptyBorder(25,20,10,20));  
		
		JPanel pushP1 = new JPanel();
		pushP1.setLayout(new BorderLayout());
		pushP1.setBorder(BorderFactory.createEmptyBorder(25,20,10,20));  
		
		
		JPanel apiP2 = new JPanel();
		apiP2.setLayout(new BoxLayout(apiP2, BoxLayout.Y_AXIS));
		apiP2.setBorder(BorderFactory.createEmptyBorder(20,0,10,20));  
		
		JPanel pullP2 = new JPanel();
		pullP2.setLayout(new BoxLayout(pullP2, BoxLayout.Y_AXIS));
		pullP2.setBorder(BorderFactory.createEmptyBorder(10,0,10,20));
		
		JPanel pushP2 = new JPanel();
		pushP2.setLayout(new BoxLayout(pushP2, BoxLayout.Y_AXIS));
		pushP2.setBorder(BorderFactory.createEmptyBorder(10,0,10,20));
		
		
		
		/*
		 * creates the labels for the connection configurations
		 */
		
		JLabel apiL = new JLabel("API-Key:");
		apiTf = new JTextField(15);
		apiTf.setEditable(true);
		
		JLabel pullL = new JLabel("Pull-URL:");
		pullTf = new JTextField(15);
		pullTf.setEditable(true);
		
		JLabel pushL = new JLabel("Push-URL:");
		pushTf = new JTextField(15);
		pushTf.setEditable(true);
		
		
		
		/*
		 * adds the labels to the Panels
		 */
		
		apiP2.add(apiL);
		apiP2.add(Box.createRigidArea(new Dimension(0,15)));
		apiP2.add(apiTf);
		
		pullP2.add(pullL);
		pullP2.add(Box.createRigidArea(new Dimension(0,15)));
		pullP2.add(pullTf);

		pushP2.add(pushL);
		pushP2.add(Box.createRigidArea(new Dimension(0,15)));
		pushP2.add(pushTf);

		
		
		
		
		/*
		 * creates the required lists for the connection configuration
		 */
		 
		apiList = new JList<String>();
		apiList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		apiList.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		JScrollPane scrollApi = new JScrollPane();
		scrollApi.getViewport().add(apiList);
		scrollApi.setPreferredSize(new Dimension(160,80));
	
		pullList = new JList<String>();
		pullList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pullList.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		JScrollPane scrollPull = new JScrollPane();
		scrollPull.getViewport().add(pullList);
		scrollPull.setPreferredSize(new Dimension(160,80));
	
		pushList = new JList<String>();
		pushList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pushList.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		JScrollPane scrollPush = new JScrollPane();
		scrollPush.getViewport().add(pushList);
		scrollPush.setPreferredSize(new Dimension(160,80));
		
		
		//loads the list-entries
		refreshConnectionList();
		
		
		
		
		/*
		 * adds the Panes and scrollPanes to the Panes
		 */
		
		apiP1.add(apiP2, BorderLayout.WEST);
		apiP1.add(Box.createRigidArea(new Dimension(0,20)));
		apiP1.add(scrollApi, BorderLayout.EAST);
		
		pullP1.add(pullP2,BorderLayout.WEST);
		pullP1.add(Box.createRigidArea(new Dimension(0,20)));
		pullP1.add(scrollPull,BorderLayout.EAST);
		
		pushP1.add(pushP2,BorderLayout.WEST);
		pushP1.add(Box.createRigidArea(new Dimension(0,20)));
		pushP1.add(scrollPush,BorderLayout.EAST);
		
		
		
		
		/*
		 * creates and adds the components that are required for the add and delete Buttons 
		 * regarding the connection configurations
		 */
		
		JPanel buttonConnP = new JPanel();
		buttonConnP.setLayout(new BoxLayout(buttonConnP, BoxLayout.X_AXIS));
		buttonConnP.setBorder(BorderFactory.createEmptyBorder(10,20,20,20));
		
		addConnB = new JButton("hinzufügen");
		removeConnB = new JButton("löschen");
		
		buttonConnP.add(addConnB);
		buttonConnP.add(Box.createRigidArea(new Dimension(20,0)));	
		buttonConnP.add(removeConnB);
		
		connecP.add(apiP1);
		connecP.add(pullP1);
		connecP.add(pushP1);
		connecP.add(buttonConnP);
		
		
		
		
		/*
		 * creates and adds all the components that are required for the ok, exit, and reset Buttons
		 * regarding all the configurations
		 */
		
		JPanel buttonP = new JPanel();
		buttonP.setLayout(new BoxLayout(buttonP, BoxLayout.X_AXIS));
		buttonP.setBorder(BorderFactory.createEmptyBorder(10,20,20,20));
		
		okB = new JButton("OK");
		resetB = new JButton("Zurücksetzen");
		exitB = new JButton("Abbruch");
		
		buttonP.add(okB);
		buttonP.add(Box.createRigidArea(new Dimension(20,0)));	
		buttonP.add(resetB);
		buttonP.add(Box.createRigidArea(new Dimension(20,0)));	
		buttonP.add(exitB);
		
		
		
		
		/*
		 * creates the tabbedPane and adds the components from above
		 */
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setBorder(BorderFactory.createEmptyBorder(20,20,10,20));
		tabbedPane.addTab("Charts", chartP);
		tabbedPane.addTab("Farbauswahl", farbP);
		tabbedPane.addTab("Verbindung", connecP);
		basic.add(tabbedPane);
		basic.add(buttonP);
		
		
		//adds the basic-Panel to the JDialog
		add(basic);
		
		
		/*
		 *sets the Dialog settings 
		 */
		
		setSelected();
		setTitle ("Einstellungen");
		setSize(475,520);
		setResizable(false);
		setLocationRelativeTo(null);                    
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	
	
	
	

	/**
	 * gets the text-field-entry concerning the height-textfield.
	 * 
	 * @return the text-field-entry as String.
	 */
	public String getTFHeight() {
		return heightTf.getText();
	}
	
	
	
	

	/**
	 * gets the text-field-entry concerning the width-textfield.
	 * 
	 * @return the text-field-entry as String.
	 */
	public String getTFWidth() {
		return widthTf.getText();
	}
	
	
	
	
	
	/**
	 * gets the selected chart-type
	 * 
	 * @return "c" if candle-chart is selected and "l" for the line-chart
	 */
	public String getChartTyp() {
		if(lineR.isSelected()) {
			return "l";
		}
		if(candleR.isSelected()) {
			return "c";
		}
		return null;
	}
	
	
	
	
	
	

	/**
	 * gets the resolution.
	 * 
	 * @return the selected resolution.
	 */
	public String getResolution() {
		if(m1R.isSelected()) {
			return "1";
		}
		if(m5R.isSelected()) {
			return "5";
		}
		if(m10R.isSelected()) {
			return "10";
		}
		if(m15R.isSelected()) {
			return "15";
		}
		if(m30R.isSelected()) {
			return "30";
		}
		if(m60R.isSelected()) {
			return "60";
		}
		if(m240R.isSelected()) {
			return "240";
		}
		if(dayR.isSelected()) {
			return "D";
		}
		if(weekR.isSelected()) {
			return "W";
		}
		if(monthR.isSelected()) {
			return "M";
		}
		return null;
	}
	
	


	/**
	 * gets the selected index regarding the indicator-color-list 
	 * 
	 * @return the selected index.
	 */
	public int getSelectedIndi() {
		return indiList.getSelectedIndex();
	}
	
	

	/*
	 * sets the two RadioButtons to selected that represent the current configuration regarding
	 * the chart-type and standard resolution
	 */
	private void setSelected() {
		PropertiesCache prop = PropertiesCache.getInstance();
		String type = prop.getProperty("chartType");
		
		if(type == null || type.equals("c")) {
			candleR.setSelected(true);
		}else {
			lineR.setSelected(true);
		}
		String reso = prop.getProperty("resolution");
		if(reso == null) {
			dayR.setSelected(true);
		}else {
			switch(reso) {
			case "1": m1R.setSelected(true);
					break;
			case "5": m5R.setSelected(true);
					break;
			case "10": m10R.setSelected(true);
					break;
			case "15": m15R.setSelected(true);
			 		break;
			case "30": m30R.setSelected(true);
					break;
			case "60": m60R.setSelected(true);
					break;
			case "240": m240R.setSelected(true);
					break;
			case "D": dayR.setSelected(true);
					break;
			case "W": weekR.setSelected(true);
					break;
			case "M": monthR.setSelected(true);
					break; 
			}
		}
	}
	
	
	
	
	
	/**
	 * clears all textfields
	 */
	public void clearTF() {
		apiTf.setText("");
		pullTf.setText("");
		pushTf.setText("");
	}
	
	
	
	
	
	
	
	/**
	 * this method updates the api- pull and push-list.
	 * It gets the entries that belong to the lists from the properties to display them
	 */
	public void refreshConnectionList() {
		//gets all the data regarding the api-key, pull- and push URL
		PropertiesCache prop = PropertiesCache.getInstance();
		String apiString = prop.getProperty("api.key.list");
		String pullString = prop.getProperty("pull.list");
		String pushString = prop.getProperty("push.list");
		String apiValue = prop.getProperty("api.key");
		String pullValue = prop.getProperty("pull.url");
		String pushValue = prop.getProperty("push.url");
		String[] elems;
		if(apiString != null) {
			//encodes the api-String and sets the data to the list
			elems = getSinglePropStrings(apiString);
			apiList.setListData(elems);
			if(apiValue != null) {
				apiList.setSelectedValue(apiValue, true);
			}
		}else {
			String[] empty = {};
			apiList.setListData(empty);
		}
		if(pullString != null) {
			//encodes the pull-String and sets the data to the list
			elems = getSinglePropStrings(pullString);
			elems[elems.length-1] = URLCandleRequest.DEFAULT_BASIS_URL;
			pullList.setListData(elems);
			//sets the current configuration to selected
			if(pullValue != null) {
				pullList.setSelectedValue(pullValue, true);
			}else {
				pullList.setSelectedValue(URLCandleRequest.DEFAULT_BASIS_URL, true);
			}
		}else {
			//if there are no entries in the Properties, default is used
			String[] defPull = {URLCandleRequest.DEFAULT_BASIS_URL};
			pullList.setListData(defPull);
			pullList.setSelectedValue(URLCandleRequest.DEFAULT_BASIS_URL, true);
		}
		if(pushString != null) {
			//encodes the push-String and sets the data to the list
			elems = getSinglePropStrings(pushString);
			elems[elems.length-1] = URLCandleRequest.DEFAULT_WS_URL;
			pushList.setListData(elems);
			if(pushValue != null) {
				pushList.setSelectedValue(pushValue, true);
			}else {
				pushList.setSelectedValue(URLCandleRequest.DEFAULT_WS_URL, true);
			}
		}else {
			String[] defPush = {URLCandleRequest.DEFAULT_WS_URL};
			pushList.setListData(defPush);
			pullList.setSelectedValue(URLCandleRequest.DEFAULT_WS_URL, true);
		}
	}
	
	
	
	
	/*
	 * this method encodes the parameter propString. If there are several api-keys- pull- or push basis-URLs 
	 * stored in the Properties, they must be encoded. This method encodes them so that they can be displayed 
	 * in the list. It is only called up in the refreshConnectionList() method.	 
     */
	private String[] getSinglePropStrings(String propString) {
	    //encodes the first value
		LinkedList<String> list = new LinkedList<String>();
		int index1 = propString.indexOf('$');
		int index2 = propString.indexOf('$', index1 + 1);
		String s;
		//encodes all the other values and adds them to the list
		while(index2 != -1) {
			s = propString.substring(index1 + 1, index2);
			list.addLast(s);
			index1 = index2;
			index2 = propString.indexOf('$', index1 + 1);
		}
		//copies the generated from the list in an array
		String[] arr = new String[list.size()+1];
		Iterator<String> it = list.iterator();
		for(int i = 0; i < list.size(); i++) {
			arr[i] = it.next();
		}
		return arr;
	}
	
	
	
	/**
	 * sets the <code>entry</code> to the Properties. 
	 * 
	 * @param key the key that is used to set and get the <code>entry</code>
	 * @param entry the value that should be set
	 */
	public void setEntryToProp(String key, String entry) {
		//gets the current String to the key and adds the entry
		PropertiesCache prop = PropertiesCache.getInstance();
		String elemString = prop.getProperty(key);
		if(elemString == null) {
			prop.setProperty(key, "$" + entry + "$.");
		}else {
			prop.setProperty(key, "$" + entry + elemString);
		}
	}
	
	
	
	
	
	/**
	 * removes the entry at position <code>index</code> concerning the Properties-Value
	 * that is reachable by the <code>key</code>.
	 * This method is called up by the {@link #StockerController} if in the api- pull- or pushlist 
	 * an entry is selected and the "löschen" Button is pushed
	 * 
	 * @param key the key that refers to the Properties-value
	 * @param index defines the position regarding the Properties-String
	 */
	public void removeConnection(String key, int index) {
		PropertiesCache prop = PropertiesCache.getInstance();
		String elemString = prop.getProperty(key);
		//true means that there is only the defaultURL in the JList and it is selected
		if(elemString != null) {
			//surrounds the entry at position index with the variables index1 and index2
			int index1 = elemString.indexOf('$');
			int index2 = elemString.indexOf('$', index1 + 1);
			for(int i = 0; i < index; i++) {
				index1 = index2;
				index2 = elemString.indexOf('$', index1 + 1);
			}
			//true means that there are several URLs in the JList and that the defaultURL is selected
			//checks if the entry is at the beginning, at the middle or at the end of the String
			//and carries out the appropriate action
			if(index2 != -1) {
				String newValue;
				int size = elemString.length();
				//true --> there is only one entry
				if(index == 0 && index2 == size-2) {
					prop.removeProperty(key);
				}else {
					//true --> the entry is at the beginning
					if(index == 0 && index2 != size-2) {
						newValue = elemString.substring(index2, size);
					}else {
						//true --> the entry is at the end
						if( index2 == size-2) {
							newValue = elemString.substring(0, index1 + 1) + ".";
						}else {
							//the entry is in the middle
							String s = elemString.substring(0, index1 + 1);
							newValue = s + elemString.substring(index2 + 1, size);
						}
					}
					//sets the new value without the entry
					prop.setProperty(key, newValue);
				}
			}
		}
	}
	
	
	
	
	/**
	 * gets the text-field-entry concerning the api-textfield.
	 * 
	 * @return the text-field-entry as String.
	 */
	public String getApiEntry() {    
		return apiTf.getText();
	}
	
	
	
	
	/**
	 * gets the text-field-entry concerning the pull-textfield.
	 * 
	 * @return the text-field-entry as String.
	 */
	public String getPullEntry() {
		return pullTf.getText();
	}
	
	
	
	
	/**
	 * gets the text-field-entry concerning the push-textfield.
	 * 
	 * @return the text-field-entry as String.
	 */
	public String getPushEntry() {
		return pushTf.getText();
	}
	
	
	
	
	
	/**
	 * returns the selected index concerning the api-list.
	 * 
	 * @return the selected index concerning the api-list.
	 */
	public int getSelectedApiIndex() {
		return apiList.getSelectedIndex();
	}
	
	
	
	

	/**
	 * returns the selected index concerning the pull-list.
	 * 
	 * @return the selected index concerning the pull-list.
	 */
	public int getSelectedPullIndex() {
		return pullList.getSelectedIndex();
	}
	
	
	
	

	/**
	 * returns the selected index concerning the push-list.
	 * 
	 * @return the selected index concerning the push-list.
	 */
	public int getSelectedPushIndex() {
		return pushList.getSelectedIndex();
	}
	
	
	
	
	/**
	 * gets the selected api-key from the api-list
	 * 
	 * @return the selected api-value
	 */
	public String getSelectedApiKey() {
		return apiList.getSelectedValue();
	}
	
	
	

	/**
	 * gets the selected pull-URL from the pull-list
	 * 
	 * @return the selected pull-URL
	 */
	public String getSelectedPullUrl() {
		return pullList.getSelectedValue();
	}
	
	
	
	

	/**
	 * gets the selected push-URL from the push-list
	 * 
	 * @return the selected push-URL
	 */
	public String getSelectedPushUrl() {
		return pushList.getSelectedValue();
	}
	
	
	
	/**
	 * clears the selection regarding the api-list
	 */
	public void clearApiSelection() {
		apiList.clearSelection();
	}
	
	
	
	
	/**
	 * clears the selection regarding the pull-list
	 */
	public void clearPullSelection() {
		pullList.clearSelection();
	}
	
	
	
	
	/**
	 * clears the selection regarding the push-list
	 */
	public void clearPushSelection() {
		pushList.clearSelection();
	}
	
	
	
	
	/**
	 * adds a listener to the ok-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setConfigurationOkButtonListener(ActionListener listener) {  
		okB.addActionListener(listener);
	}
	
	
	
	/**
	 * adds a listener to the exit-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setExitButtonListener(ActionListener listener) {  
		exitB.addActionListener(listener);
	}
	
	
	
	
	/**
	 * adds a listener to the reset-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setResetButtonListener(ActionListener listener) {  
		resetB.addActionListener(listener);
	}
	
	
	
	
	/**
	 * adds a listener to the alarm-color-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setAlarmColorButtonListener(ActionListener listener) {  
		colorAb.addActionListener(listener);
	}
	
	
	
	
	/**
	 * adds a listener to the indicator-color-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setIndiColorButtonListener(ActionListener listener) {  
		colorIb.addActionListener(listener);
	}
	
	
	
	
	/**
	 * adds a listener to the connection-add-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setConnectionAddListener(ActionListener listener) {  
		addConnB.addActionListener(listener);
	}
	
	
	
	/**
	 * adds a listener to the remove-connection-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setConnectionRemoveListener(ActionListener listener) {  
		removeConnB.addActionListener(listener);
	}

	
	
	
	/**
	 * adds a listener to the api-<code>JTextField</code>.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setApiTFListener(ActionListener listener) {  
		apiTf.addActionListener(listener);
	}
	
	
	
	
	/**
	 * adds a listener to the pull-<code>JTextField</code>.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setPullTFListener(ActionListener listener) {  
		pullTf.addActionListener(listener);
	}
	
	
	
	
	/**
	 * adds a listener to the push-<code>JTextField</code>.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setPushTFListener(ActionListener listener) {  
		pushTf.addActionListener(listener);
	}
	
	
	
	
	
	/**
	 * adds a Mouse-listener to the api-list.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setApiListMouseListener(MouseListener listener) {
		apiList.addMouseListener(listener);
	}
	
	
	
	
	/**
	 * adds a Mouse-listener to the pull-list.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setPullListMouseListener(MouseListener listener) {
		pullList.addMouseListener(listener);
	}
	
	
	
	
	/**
	 * adds a Mouse-listener to the push-list.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setPushListMouseListener(MouseListener listener) {
		pushList.addMouseListener(listener);
	}
}
