package view.view.other;

import dataconverter.writersandreaders.FileHandler;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Plot extends JFrame {

    public static String CREATE_PLOT_ACTION = "CREATE_PLOT_ACTION";
    Random random = new Random();
    Day[] domainRange;
    TimeSeriesCollection dataset;
    JFreeChart chart;
    Stroke stroke = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    ChartPanel chartPanel;
    int numOfSeries;
    JMenuBar bar = new JMenuBar();
    FileHandler handler = new FileHandler("png", "jpeg");

    public Plot(){

        dataset = new TimeSeriesCollection();

        chart = ChartFactory.createTimeSeriesChart(
                "Plot", "Date", "Values",
                dataset, true, true, false);

        chart.getLegend().setItemFont(new Font("Roboto", Font.PLAIN, 20));
        DeviationRenderer renderer = new DeviationRenderer(true, false);
        renderer.setAlpha(0.12f);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setRenderer(renderer);

        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 400));

        setLayout(new BorderLayout());
        add(chartPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(700, 700);

        setJMenuBar(bar);

        JMenu subMenu = new JMenu("Save As");

        JMenuItem itemPNG = new JMenuItem(new HandlePlotSave("png"));
        JMenuItem itemJPEG = new JMenuItem(new HandlePlotSave("jpeg"));

        subMenu.add(itemJPEG);
        subMenu.addSeparator();
        subMenu.add(itemPNG);

        bar.add(subMenu);
    }

    public class HandlePlotSave extends AbstractAction{

        String directory = "plots";

        public HandlePlotSave(String extension){
            putValue(Action.NAME, extension);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            Path path = Path.of(directory + "\\" + chart.getTitle().getText().replaceAll("\\W+", "_") + "." + getValue(Action.NAME));

            try {
                handler.parseFilePath(path.toString());
                handler.createFile();
                ChartUtils.saveChartAsPNG(path.toFile(),
                        chart, chartPanel.getWidth(), chartPanel.getHeight());
            } catch (IOException ioException) {
                ioException.printStackTrace();
                //JOptionPane.showMessageDialog(Plot.this, "Couldn't Save The Plot",
                     //   "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void setTitle(String title){
        chart.setTitle(title);
    }

    // If we want to reuse the plot we need to get rid of current displayed values
    public void clear(){
        dataset.removeAllSeries();
    }

    // Prepare domain values (as Day objects) : this must be called before adding series values
    public void setDomainRange(Day[] arr){
        domainRange = arr;
    }

    // Assumes that each series has the same domain
    public void addSeries(String title, double[] values){

        TimeSeries series;
        // This is the 1st time that data is added
        if (dataset.getSeries(title) == null){
            series = new TimeSeries(title);
            addOrUpdateValues(values, series);
            configSeries(numOfSeries);
            dataset.addSeries(series);
            numOfSeries++;
        } else {
            addOrUpdateValues(values, dataset.getSeries(title));
        }

    }

    private void addOrUpdateValues(double[] values, TimeSeries series){
        for (int i = 0; i < values.length; i++) {
            // Add data point to time series if the date is not already there, or puts new value
            // if the date already exists
            //System.out.println("Length: " + values.length);
            //System.out.println("Num of Series: " + numOfSeries);
            series.addOrUpdate(domainRange[i], values[i]);
        }
    }

    private void configSeries(int index){
        int RGB = 256;
        Color c = new Color(random.nextInt(RGB), random.nextInt(RGB), random.nextInt(RGB));
        DeviationRenderer r = (DeviationRenderer) chart.getXYPlot().getRenderer();
        //Color c = index == 0 ? new Color(58, 177, 177) : new Color(65, 186, 25);
        r.setSeriesPaint(index, c);
        r.setSeriesStroke(index, stroke);
    }

}
