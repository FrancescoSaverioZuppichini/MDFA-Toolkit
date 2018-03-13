package ch.imetrica.mdfatrading.plotutil;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Day;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.joda.time.DateTime;

import ch.imetrica.mdfatrader.series.SignalSeries;
import ch.imetrica.mdfatrader.series.TargetSeries;

public class TimeSeriesPlot extends ApplicationFrame {

	    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		/**
	     * Creates a new demo instance.
	     *
	     * @param title  the frame title.
	     */
	    public TimeSeriesPlot(final String title) {
	        
	        super(title);
	        final XYDataset dataset = createDataset();
	        final JFreeChart chart = createChart(dataset);
	        final ChartPanel chartPanel = new ChartPanel(chart);
	        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	        chartPanel.setMouseZoomable(true, false);
	        setContentPane(chartPanel);
	        
	    }
	    
	    public TimeSeriesPlot(final String title, TargetSeries target) {
	        
	        super(title);
	        final XYDataset dataset = createDataset(title, target);
	        final JFreeChart chart = createChart(dataset);
	        final ChartPanel chartPanel = new ChartPanel(chart);
	        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	        chartPanel.setMouseZoomable(true, false);
	        setContentPane(chartPanel);
	        
	    }
	    
	    public TimeSeriesPlot(final String title, SignalSeries signal) throws Exception {
	        
	        super(title);
	        final XYDataset dataset = createDataset(title, signal);
	        final JFreeChart chart = createChart(dataset);
	        final ChartPanel chartPanel = new ChartPanel(chart);
	        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	        chartPanel.setMouseZoomable(true, false);
	        setContentPane(chartPanel);
	        
	    }
	    
	       
	    private XYDataset createDataset(String title, SignalSeries signal) throws Exception {
			
	    	TimeSeriesCollection dataset = new TimeSeriesCollection();
	    	
	    	final TimeSeries mySeries = new TimeSeries("EURUSD series");
	    	final TimeSeries mySignal = new TimeSeries("Signal");
	    	
	    	for (int i = 0; i < signal.size(); i++) {
	        	
	            try {
	            	
	                double value = signal.getSignalValue(i);
	                String sigDate = signal.getSignalDate(i);
	                double tsvalue = signal.getTargetValue(signal.getTargetSeries().size() - signal.size() + i);
	                String tsDate = signal.getTargetDate(signal.getTargetSeries().size() - signal.size() + i);
	                
	                if(!sigDate.equals(tsDate)) {
	                	throw new Exception("Datest don't match");
	                }

	                DateTime sigDateTime = signal.getSignalDateTime(i);	                
	                Day current = new Day(sigDateTime.toDate());
	                
	                //Second current = new Second(sigDateTime.toDate());
	                
	                mySignal.add(current, value);
	                mySeries.add(current, tsvalue);
	            }
	            catch (SeriesException e) {
	                System.err.println("Error adding to series");
	            }
	        }
	    	dataset.addSeries(mySignal);
	    	dataset.addSeries(mySeries);
	    	
	    	return dataset;
		}

		public TimeSeriesPlot(String title, TargetSeries[] collection) {
	    	
	    	super(title);
	        final XYDataset dataset = createDataset(title, collection);
	        final JFreeChart chart = createChart(dataset);
	        final ChartPanel chartPanel = new ChartPanel(chart);
	        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	        chartPanel.setMouseZoomable(true, false);
	        setContentPane(chartPanel);
		}

		public TimeSeriesPlot(String title, double[] prdx) {
			
	    	super(title);
	        final XYDataset dataset = createDataset(title, prdx);
	        final JFreeChart chart = createChart(dataset);
	        final ChartPanel chartPanel = new ChartPanel(chart);
	        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	        chartPanel.setMouseZoomable(true, false);
	        setContentPane(chartPanel);			
		}

		private XYDataset createDataset(String title, double[] prdx) {
			
			final TimeSeries series = new TimeSeries("Periodogram");
	        Day current = new Day(5, 3, 2018);
	        double value = 100.0;
	        for (int i = 0; i < prdx.length; i++) {
	            try {
	                value = prdx[i];
	                series.add(current, value);
	                current = (Day) current.next();
	            }
	            catch (SeriesException e) {
	                System.err.println("Error adding to series");
	            }
	        }
	        return new TimeSeriesCollection(series);
		}

		private XYDataset createDataset(String title, TargetSeries[] collection) {
			
			TimeSeriesCollection dataset = new TimeSeriesCollection();
			
			for(int k = 0; k < collection.length; k++) {
				
				final TimeSeries series = new TimeSeries(title + " " + k);
		        for (int i = 0; i < collection[k].size(); i++) {
		        	
		            try {
		            	
		                double value = collection[k].getTargetValue(i);
		                
		                DateTime myDay = collection[k].getDateTime(i);       
		                Day current = new Day(myDay.toDate());	 
		                
		                series.add(current, value);
		            }
		            catch (SeriesException e) {
		                System.err.println("Error adding to series");
		            }
		        }
		        dataset.addSeries(series);
			}
			return dataset;
		}

		/**
	     * Creates a sample dataset.
	     * 
	     * @return A sample dataset.
	     */
	    private XYDataset createDataset() {
	        
	        final TimeSeries series = new TimeSeries("Random Data");
	        Day current = new Day(1, 1, 1990);
	        double value = 100.0;
	        for (int i = 0; i < 4000; i++) {
	            try {
	                value = value + Math.random() - 0.5;
	                series.add(current, new Double(value));
	                current = (Day) current.next();
	            }
	            catch (SeriesException e) {
	                System.err.println("Error adding to series");
	            }
	        }
	        return new TimeSeriesCollection(series);
	    }
	    
	    
	    public XYDataset createDataset(String title, TargetSeries target) {
	        
	        final TimeSeries series = new TimeSeries(title);

	        for (int i = 0; i < target.size(); i++) {
	        	
	            try {
	            	
	                double value = target.getTargetValue(i);
	                DateTime myDay = target.getDateTime(i);       
	                Day current = new Day(myDay.toDate());	                    
	                series.add(current, value);
	            }
	            catch (SeriesException e) {
	                System.err.println("Error adding to series");
	            }
	        }
	        return new TimeSeriesCollection(series);          
	    }
	    
	    
	    /**
	     * Creates a sample chart.
	     * 
	     * @param dataset  the dataset.
	     * 
	     * @return A sample chart.
	     */
	    private JFreeChart createChart(final XYDataset dataset) {
	        return ChartFactory.createTimeSeriesChart(
	            "Test", 
	            "Day", 
	            "Value", 
	            dataset,
	            false, 
	            false, 
	            false
	        );
	    }
	    
	    // ****************************************************************************
	    // * JFREECHART DEVELOPER GUIDE                                               *
	    // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
	    // * to purchase from Object Refinery Limited:                                *
	    // *                                                                          *
	    // * http://www.object-refinery.com/jfreechart/guide.html                     *
	    // *                                                                          *
	    // * Sales are used to provide funding for the JFreeChart project - please    * 
	    // * support us so that we can continue developing free software.             *
	    // ****************************************************************************
	    
	    /**
	     * Starting point for the application.
	     *
	     * @param args  ignored.
	     */
	    public static void main(final String[] args) {

	        final String title = "\u20A2\u20A2\u20A2\u20A3\u20A4\u20A5\u20A6\u20A7\u20A8\u20A9\u20AA";
	        final TimeSeriesPlot demo = new TimeSeriesPlot(title);
	        demo.pack();
	        RefineryUtilities.positionFrameRandomly(demo);
	        demo.setVisible(true);

	    }
	
	
}
