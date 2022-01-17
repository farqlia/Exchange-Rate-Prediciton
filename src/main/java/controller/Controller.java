package controller;

import currencyparsing.currencyurlbuilders.*;
import currencyparsing.currencyurlworker.ExchangeRateLoader;
import currencyparsing.currencyurlworker.Loader;
import dataconverter.writersandreaders.TextFileWriter;
import dataconverter.writersandreaders.VectorToCSV;
import datasciencealgorithms.utils.point.Point;
import exchangerateclass.ExchangeRate;
import mathlibraries.Statistics;
import mathlibraries.TimeSeriesScienceLibrary;
import model.Model;
import model.ModelEvent;
import model.ModelObserver;
import org.jfree.data.time.Day;
import view.*;
import view.other.Menu;
import view.other.Plot;
import view.view.AbstractView;
import view.view.ViewEvent;
import view.view.ViewObserver;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Controller {

    Model model;
    DefaultTableModel modelA, modelS;
    AbstractView view;

    List<Point> realDataPoints = new ArrayList<>(100);

    public Controller(AbstractView view, Model model,
                      DefaultTableModel modelA, DefaultTableModel modelS){
        this.view = view;
        this.model = model;
        this.modelA = modelA;
        this.modelS = modelS;

        ListenForCreatePlot listener = new ListenForCreatePlot(new Plot());

        this.view.registerObserver(new ListenForView());
        this.view.registerObserver(listener);

        model.registerObserver(new ListenForModel1());
        model.registerObserver(new ListenForModel2());
        model.registerObserver(listener);
        model.registerObserver(new HandleViewAction());

        view.getJMenuBar().addPropertyChangeListener(Menu.SAVE_TO_FILE,
                        new ListenForFileSave(new TextFileWriter<>(new VectorToCSV())));

        view.getJMenuBar().addPropertyChangeListener(Menu.CREATE_PLOT,
                listener);

    }

    public class ListenForView implements ViewObserver {

        Loader<ExchangeRate> exchangeRateLoader = new ExchangeRateLoader();
        ConcreteCurrencyURL.Builder builder = new ConcreteCurrencyURL.Builder(MoneyType.CURRENCY);

        @Override
        public void update(ViewEvent e) {

            realDataPoints.clear();

            exchangeRateLoader.setCurrencyURL(builder
                    .reset()        // reuse the same object
                    // Subtract days to provide some start-up points for the latest dates
                    .addDate(e.getStartDate().minusMonths(1), e.getEndDate())
                    .addTable(Table.A)
                    .addCurrencyCode(e.getCurrencyCode())
                    .build()
            );

            List<ExchangeRate> exchangeRatesList = exchangeRateLoader.load();

            if (exchangeRatesList.isEmpty()){
                System.out.println("Something went wrong");
                JOptionPane.showMessageDialog(null,
                        "Error Occurred", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (ExchangeRate eR : exchangeRatesList){
                // Date in ExchangeRates class is 'Date' type so we convert it to LocalDate
                realDataPoints.add(new Point(LocalDate.ofInstant(eR.getEffectiveDate().toInstant(),
                        ZoneId.systemDefault()),
                        eR.getMid()));
            }
            model.setAlgorithm(e.getChosenAlgorithm());

            try {
                model.predict(realDataPoints, e.getStartDate(), e.getEndDate());

            } catch (IllegalStateException ex){
                JOptionPane.showMessageDialog(view,
                        "Error Occurred", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    public class HandleViewAction implements ModelObserver{

        @Override
        public void update(ModelEvent event) {
            if (event == ModelEvent.DATA_PROCESS_STARTED){
                view.disableActions();
            } else if (event == ModelEvent.DATA_PROCESSED){
                view.enableActions();
            }
        }
    }

    public class ListenForModel1 implements ModelObserver{

        @Override
        public void update(ModelEvent event) {

            if (event == ModelEvent.DATA_IN_PROCESS){
                BigDecimal[] absoluteError = TimeSeriesScienceLibrary
                        .calculateAbsoluteError(realDataPoints, model.getDataChunk());

                BigDecimal[] absolutePercentageError = TimeSeriesScienceLibrary
                        .calculatePercentageError(realDataPoints, model.getDataChunk());

                Vector<Vector> dataToSend = DataPointsFlattering.flatten(realDataPoints,
                        model.getDataChunk());

                DataPointsFlattering.concat(dataToSend, absoluteError, absolutePercentageError);

                for (Vector row : dataToSend){
                    modelA.addRow(row);
                }
            }

        }
    }

    public class ListenForModel2 implements ModelObserver{

        List<Point> predictedData = new ArrayList<>();

        @Override
        public void update(ModelEvent event) {
            if (event == ModelEvent.DATA_IN_PROCESS){
                predictedData.addAll(model.getDataChunk());
            } else if (event == ModelEvent.DATA_PROCESSED){
                predictedData.addAll(model.getDataChunk());

                Vector<Vector> statistics = new Vector<>(3);

                for (Statistics s : Statistics.values()){
                    statistics.add(new Vector<>(List.of(s.name(),
                            s.apply(realDataPoints, predictedData))));
                }

                modelS.setDataVector(statistics, getColumnNames(modelS));

                predictedData.clear();

            }
        }
    }

    public class ListenForFileSave implements PropertyChangeListener{

        TextFileWriter<Vector> textFileWriter;
        public ListenForFileSave(TextFileWriter<Vector> textFileWriter){
            this.textFileWriter = textFileWriter;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

             Vector<Vector> v = new Vector<>();
                v.add(getColumnNames(modelA));
                v.addAll(modelA.getDataVector());
                try {
                    textFileWriter.saveToFile(((File)evt.getNewValue()).getAbsolutePath(), v);
                    }
                catch (IOException e) {
                    JOptionPane.showMessageDialog(null,
                            "Error Occurred", "Error", JOptionPane.ERROR_MESSAGE);

            }
        }
    }
    private Vector getColumnNames(DefaultTableModel model){
        Vector v = new Vector<>(model.getColumnCount());
        for (int i = 0; i < model.getColumnCount(); i++){
            v.add(model.getColumnName(i));
        }
        return v;
    }

    public class ListenForCreatePlot implements PropertyChangeListener, ModelObserver, ViewObserver{

        Plot plot;
        public ListenForCreatePlot(Plot plot){
            this.plot = plot;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            // Shows frame with plot
            if (evt.getPropertyName().equals(Menu.CREATE_PLOT)){
                plot.setVisible(true);
            }
        }
        @Override
        public void update(ModelEvent event) {
            if (event == ModelEvent.DATA_PROCESS_STARTED){
                // Prepare plot for new data
                plot.clear();
            } else if (event == ModelEvent.DATA_IN_PROCESS){
                // real-time update plot
                plot.setDomainRange(getDates());
                plot.addSeries("Real", getValues(1));
                plot.addSeries("Predicted", getValues(2));
            }
        }

        @Override
        public void update(ViewEvent e) {
            plot.setTitle(e.getCurrencyCode() + ", " + e.getStartDate() + ":" + e.getEndDate());
        }

        private Day[] getDates(){
            Day[] arr = new Day[modelA.getRowCount()];
            for (int i = 0; i < modelA.getRowCount(); i++){
                LocalDate d = (LocalDate) modelA.getValueAt(i, 0);
                arr[i] = new Day(d.getDayOfMonth(), d.getMonthValue(), d.getYear());
            }
            return arr;
        }

        private double[] getValues(int col){
            double[] arr = new double[modelA.getRowCount()];
            for (int i = 0; i < modelA.getRowCount(); i++){
                arr[i] =((BigDecimal) modelA.getValueAt(i, col)).doubleValue();
            }
            return arr;
        }



    }

}