package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import Client.HttpRequest;
import Client.exception.HTTPException;
import Client.exception.NotEnoughCandlesException;
import Client.exception.StateNotOkException;
import Model.indicatorModel.BBParameter;
import Model.chart.CandleRequestData;
import Model.indicatorModel.IndicatorModel;
import View.dialogsAndFrames.BBDialog;
import View.chart.Chart;
import View.dialogsAndFrames.GDDialog;
import View.dialogsAndFrames.IndicatorDialog;



/**
 * this class organizes the event-handling concerning the indicators.
 * So, it is one of the three controller-classes and manages the view, analyzes the user input
 * and induces the suitable actions concerning the model and the client.
 * This class contains several inner classes that implement the <code>ActionListener</code> interface and
 * handle the occurred events.
 * 
 * @author Benjamin Birner
 *
 */
public class IndicatorController {
	
	private IndicatorModel indiModel;
	private HttpRequest request;
	
	public IndicatorController(IndicatorModel indiModel, HttpRequest request) {
		this.indiModel = indiModel;
		this.request = request;
	}

	
	/*
	 * The method in this class is called up if the JMenuItem "Funktionen" --> "Inikatoren" is pushed.
	 * It builds up a IndicatorDialog-Object and adds all listeners
	 */
	private class IndiMenuListener implements ActionListener{
		
		private Chart chart;
		
		private IndiMenuListener(Chart chart) {
			this.chart = chart;
		}
		
		@Override 
		public void actionPerformed(ActionEvent e) {
		IndicatorDialog dia = new IndicatorDialog(chart, indiModel);	
		dia.addAddIndiListener(new AddIndiListener(dia,chart));
		dia.addDeleteIndiListener(new DeleteIndiListener(dia, chart));
		dia.addIndiOkButtonListener(new ExitButtonListener(dia));
		dia.setVisible(true);
		}
	}
	
	
	/*
	 * The method in this class is called up if a indicator in the Indicator-Dialog is selected 
	 * and the "hinzufügen"-Button is pushed.
	 * It builds up the corresponding Dialog to parameterize the indicator.
	 */
	private class AddIndiListener implements ActionListener{
		
		private IndicatorDialog dia;
		private Chart chart;
		
		private AddIndiListener(IndicatorDialog dia, Chart chart) {
			this.dia = dia;
			this.chart = chart;
		}
		
		@Override 
		public void actionPerformed(ActionEvent e) {
			int index = dia.getSelectedIndi();
			String description = indiModel.getDescriptionAt(index);
			switch(description) {
			case "Moving Average":
				GDDialog gdDia = new GDDialog();
				gdDia.addGDOkButtonListener( new GDOkButtonListener(gdDia,chart)); 
				gdDia.setGDTFListener(new GDOkButtonListener(gdDia,chart));
				gdDia.setVisible(true);
				break;
			case "Bollinger Bands":
				BBDialog bbDia = new BBDialog(indiModel, chart.getID());
				bbDia.addBBaddGDListener(new AddBBaddGDListener(chart,bbDia,dia));
				bbDia.addBBOkButtonListener( new BBOkButtonListener(bbDia,chart));
				bbDia.setFactorTFListener(new BBOkButtonListener(bbDia,chart));
				bbDia.addExitButtonListener(new ExitButtonListener(bbDia));
				bbDia.setVisible(true);
				break;
			}
			dia.refreshList();
			chart.repaintPanel();
			dia.setSelectedIndiListIndex(index);
		}
	}
	
	
	/*
	 * The method in this class is called up if a drawn indicator in the indicator dialog is selected and the
	 * "entfernen" -Button is pushed.
	 * It removes the selected entry from the model/chart
	 */
	private class DeleteIndiListener implements ActionListener{
		
		private IndicatorDialog dia;
		private Chart chart;
		
		private DeleteIndiListener(IndicatorDialog dia, Chart chart) {
			this.dia = dia;
			this.chart = chart;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int index = dia.getSelectedDrawnIndi();
			if(dia.getSelectedValue() != null) {
				indiModel.removeIndiAt(index,  chart.getID());
				
			}
			dia.refreshList();
			chart.repaintPanel();
			dia.setSelectedIndiListIndex(0);
		}
	}
	
	
	/*
	 * The method in this class is called up if in a dialog the "Abbrechen"-Button is pushed.
	 * It closes the dialog.
	 */
	private class ExitButtonListener implements ActionListener{
		
		private JDialog dia;
		
		private ExitButtonListener(JDialog dia) {
			this.dia = dia;
		}
		
		@Override 
		public void actionPerformed(ActionEvent e) {
			dia.dispose();
		}
	}
	
	
	/*
	 * The method in this class is called up if the "Ok"-Button in the dialog to parameterize the 
	 * MA is pushed. It checks the entry and adds the MA to the Model.
	 */
	private class GDOkButtonListener implements ActionListener{
		
		private Chart chart;
		private GDDialog dia;
		
		private GDOkButtonListener(GDDialog dia,Chart chart) {
			this.dia = dia;
			this.chart = chart;
		}
		
		@Override 
		public void actionPerformed(ActionEvent e) {
			int para;
			try {
				//gets the entry
				String paraS = dia.getParameter();
				para = Integer.parseInt(paraS);
				//checks if the entry is practical
				if( para <= 0 || para > 400) {
					throw new IllegalArgumentException("inkorrekte Eingabe! Wert kleiner gleich 0 oder geößer 400!");
				}else {
					boolean cont = indiModel.containsGD(chart.getID(), para);
					//checks if there already exists an identical entry
					if(!cont) {
						//the number of required close-prices for the request/MA
						int number = para + 1 + Chart.ELEM_NUMBER;
						CandleRequestData data = candleRequest(chart.getSymbol(), chart.getResolution(), number);
						if(data != null) {
							indiModel.addGD(chart.getID(),chart.getResolution(), para, data);
						}
						
					}
				}
				dia.dispose();

			}catch(IllegalArgumentException ae) {
				JOptionPane.showMessageDialog(null,  "Bitte Zahl größer 0 kleiner gleich 400 eingeben!", "Inkorrekte Eingabe!",JOptionPane.ERROR_MESSAGE);
				dia.clearTF();
			}	
		}	
	}
	
	
	
	/*
	 * The method in this class is called up if a JMenuItem in the JMenu "Resolution" is pushed.
	 * It removes the MA entry regarding the old resolution and adds the new one.
	 */
	private class ResolutionGDListener implements ActionListener{
		
		private Chart chart;
		private String newResolution;
		
		private ResolutionGDListener(Chart chart, String newResolution) {
			this.chart = chart;
			this.newResolution = newResolution;
		}
		
		@Override 
		public void actionPerformed(ActionEvent e) {
			// gets all MAs (parameter) to this chart
			int[] allPara = indiModel.getAllGDs(chart.getID());
			//if there is a MA
			if(allPara[0] != -1) {
				//Deletes the old entries
				indiModel.removeChartForIndi("Moving Average", chart.getID());    
				//carries out a request for each parameter with the new resolution and adds the MAs 
				for(int i = 0; i < allPara.length; i++) {	
					int number = allPara[i] + 1 + Chart.ELEM_NUMBER;                                        
					CandleRequestData data = candleRequest(chart.getSymbol(), newResolution, number); 
					if(data != null) {
						indiModel.addGD(chart.getID(),newResolution, allPara[i], data);
					}
					
				}
				chart.repaintPanel();	
			}	
		}	
	}
	
	
	/*
	 * The method in this class is called up if the "Ok"-Button in the dialog to parameterize the BB is pushed.
	 *  It checks the entry and adds the BB to the Model.
	 */
	private class BBOkButtonListener implements ActionListener{
		
		private BBDialog dia;
		private Chart chart;
		
		private BBOkButtonListener(BBDialog dia, Chart chart) {
			this.dia = dia;
			this.chart = chart;
		}
		
		@Override 
		public void actionPerformed(ActionEvent e) {
			double fac;
			try {
				//gets the entry
				String facS = dia.getFactor();
				fac = Double.parseDouble(facS);
				//checks if the entry is practical
				if( fac <= 0 || fac > 5) {
					throw new IllegalArgumentException("inkorrekte Eingabe! Wert kleiner gleich 0 oder größer 5!");
				}else {
					//gets the index concerning the selected MA to which the BB should be added.
					//This MA defines the period/parameter n
					int index = dia.getSelectedIndex();
					if (index == -1) {
						JOptionPane.showMessageDialog(null,  "Bitte Gleitenden Durchschnitt auswählen!", "Inkorrekte Eingabe!",JOptionPane.ERROR_MESSAGE);
					}else {
						//gets the MA-Period n to the selected MA.
						int[] gdPara = indiModel.getAllGDs(chart.getID());
						int per = gdPara[index];
						boolean cont = indiModel.containsBB( chart.getID(),per, per, fac);  
						//checks if there already exists an identical entry
						if(!cont) {
							//number of required close-prices for the BB/request
							int numb = Chart.ELEM_NUMBER + 1 + per;
							CandleRequestData data = candleRequest(chart.getSymbol(), chart.getResolution(), numb);
							if( data != null) {
								indiModel.addBB(chart.getID(),chart.getResolution(), per, fac, data);
							}
						
						}
					}
				}
				dia.dispose();
				
			}catch(IllegalArgumentException ae) {
				JOptionPane.showMessageDialog(null,  "Bitte Zahl größer 0 kleiner gleich 5 eingeben!", "Inkorrekte Eingabe!",JOptionPane.ERROR_MESSAGE);
				dia.clearTF();
			}
		}	
	}
	
	
	/*
	 *  The method in this class is called up if in the BB-Dialog to parameterize the BB the "hinzufügen"-Button concerning
	 *  the MA is pushed. It builds up the MA-Dialog in order to parameterize and add a MA.
	 */
	private class AddBBaddGDListener implements ActionListener{
		
		private Chart chart;
		private BBDialog bbDia;
		private IndicatorDialog indiDia;
		
		private AddBBaddGDListener(Chart chart, BBDialog bbDia, IndicatorDialog indiDia) {
			this.chart = chart;
			this.bbDia = bbDia;
			this.indiDia = indiDia;
		}
		
		
		@Override 
		public void actionPerformed(ActionEvent e) {
			GDDialog gdDia = new GDDialog();
			gdDia.addGDOkButtonListener( new GDOkButtonListener(gdDia,chart)); 
			gdDia.setVisible(true);
			indiDia.refreshList();
			bbDia.refreshList();
			chart.repaintPanel();
			bbDia.setSelectedGDListIndex(0);
		}
	}
	

	/*
	 * The method in this class is called up if a JMenuItem in the JMenu "Resolution" is pushed.
	 * It removes the BB entry regarding the old resolution and adds the new one.
	 */
	private class ResolutionBBListener implements ActionListener{
		private Chart chart;
		private String newResolution;
		
		private ResolutionBBListener(Chart chart, String newResolution) {
			this.chart = chart;
			this.newResolution = newResolution;
		}
		
		@Override 
		public void actionPerformed(ActionEvent e) {
			//gets all BB (parameter) to this chart
			BBParameter[] objs = indiModel.getAllBBParaObjs(chart.getID());
			//if there is a BB to this chart
			if(objs != null) {
				indiModel.removeChartForIndi("Bollinger Bands", chart.getID());  
				//carries out a request for each BB with the new resolution and adds them to the model
				for(int i = 0; i < objs.length; i++) {
					//the 1 is for the candle that is in progress, it can not be used for the calculation
					int numb = Chart.ELEM_NUMBER + 1 + objs[i].getPeriod();
					CandleRequestData data = candleRequest(chart.getSymbol(),newResolution, numb);
					if(data != null) {
						indiModel.addBB(chart.getID(),newResolution, objs[i].getPeriod(), objs[i].getFactor(), data);  
					}
					
				}
				chart.repaintPanel();	
			}
		}
	}
	
	
	
	/**
	 * This method carries out a candle-request. It also checks if the request has delivered enough data for the
	 * following calculations.
	 * If not, it throws a NotEnoughCandleException.
	 * 
	 * @param symbol the symbol that defines the stock for the request
	 * @param resolution defines the period for the required candles
	 * @param number defines the number of candles that are required
	 * @return a {@link #CandleRequestData}-object that contains all the data from the request
	 */
	public CandleRequestData candleRequest(String symbol, String resolution, int number) {
		
		URLCandleRequest url = new URLCandleRequest(symbol, resolution, number);  
		//necessary for the number-check due to the fact that the 10 min resolution must be converted
		// from 5 min candles and the 240 min resolution from 60 min candles.
		if(resolution.equals("10")) {number = number * 2 + 2;}
		if(resolution.equals("240")) {number = number * 4;}
		CandleRequestData data = null;
		try {
			//checks if there are enough data for the following calculations
			data = request.candleRequest(url.getUrlString());
			int size = data.size();
			if( size < number) {
				data = null;
				throw new NotEnoughCandlesException(size);
			}
			return data;
		} catch (MalformedURLException e) {
			System.err.println("URL ist falsch aufgebaut!");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("URL konnte nicht geöffnet werden!");
			e.printStackTrace();
		} catch (HTTPException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} catch (StateNotOkException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} catch (NotEnoughCandlesException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
		JOptionPane.showMessageDialog(null,  "Anfrage Fehlgeschlagen!", "ERROR!",JOptionPane.ERROR_MESSAGE);
		
		return data;
	}
	
	/**
	 * This method sets all the resolution-listeners to a Chart-Object concerning the indicators.
	 * It is only used to initialize the {@link #Chart}-Object.
	 * 
	 * @param chart {@link #Chart}-Object with the set listeners
	 */
	protected void setAllIndiResolutionListener(Chart chart) {
		chart.setMenuIndiListener(new IndiMenuListener(chart));
		chart.setMenuMin1Listener(new ResolutionGDListener(chart, "1"));
		chart.setMenuMin5Listener(new ResolutionGDListener(chart, "5"));
		chart.setMenuMin10Listener(new ResolutionGDListener(chart, "10"));
		chart.setMenuMin15Listener(new ResolutionGDListener(chart, "15"));
		chart.setMenuMin30Listener(new ResolutionGDListener(chart, "30"));
		chart.setMenuH1Listener(new ResolutionGDListener(chart, "60"));
		chart.setMenuH4Listener(new ResolutionGDListener(chart, "240"));
		chart.setMenuDayListener(new ResolutionGDListener(chart, "D"));
		chart.setMenuWeekListener(new ResolutionGDListener(chart, "W"));
		chart.setMenuMonthListener(new ResolutionGDListener(chart, "M"));
		
		chart.setMenuMin1Listener(new ResolutionBBListener(chart, "1"));
		chart.setMenuMin5Listener(new ResolutionBBListener(chart, "5"));
		chart.setMenuMin10Listener(new ResolutionBBListener(chart, "10"));
		chart.setMenuMin15Listener(new ResolutionBBListener(chart, "15"));
		chart.setMenuMin30Listener(new ResolutionBBListener(chart, "30"));
		chart.setMenuH1Listener(new ResolutionBBListener(chart, "60"));
		chart.setMenuH4Listener(new ResolutionBBListener(chart, "240"));
		chart.setMenuDayListener(new ResolutionBBListener(chart, "D"));
		chart.setMenuWeekListener(new ResolutionBBListener(chart, "W"));
		chart.setMenuMonthListener(new ResolutionBBListener(chart, "M"));
		
	}
}
