package controller;

import currencyparsing.currencyurlbuilders.*;
import currencyparsing.currencyurlworker.ExchangeRateLoader;
import currencyparsing.currencyurlworker.Loader;
import dataconverter.formatters.RowToCSV;
import dataconverter.writersandreaders.CustomFileWriter;
import dataconverter.writersandreaders.JsonFileWriter;
import dataconverter.writersandreaders.TextFileWriter;
import datasciencealgorithms.utils.UtilityMethods;
import datasciencealgorithms.utils.point.Point;
import exchangerateclass.ExchangeRate;
import mathlibraries.Statistics;
import mathlibraries.TimeSeriesScienceLibrary;
import model.*;
import org.jfree.data.time.Day;
import studyjson.ResultsInfo;
import view.other.Menu;
import view.other.Plot;
import view.view.AbstractView;
import view.view.ViewEvent;
import view.view.ViewObserver;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Controller {

    Model model;
    CustomTableModel<ResultsTableModel.Row> modelA;
    CustomTableModel<StatisticsTableModel.Row> modelS;
    AbstractView view;

    List<Point> realDataPoints = new ArrayList<>(100);

    public Controller(AbstractView view, Model model,
                      CustomTableModel<ResultsTableModel.Row> modelA,
                      CustomTableModel<StatisticsTableModel.Row> modelS){
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

        HandleSaveToFile handler = new HandleSaveToFile();
        view.getJMenuBar().addPropertyChangeListener(Menu.SAVE_AS_TEXT,handler);
        view.getJMenuBar().addPropertyChangeListener(Menu.SAVE_AS_JSON, handler);
        view.getJMenuBar().addPropertyChangeListener(Menu.CREATE_PLOT, listener);

        view.registerObserver(handler);

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
                // Date in ExchangeRates class is 'Date' type so convert it to LocalDate
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
            } finally {

            }
        }

    }

    public class HandleViewAction implements ModelObserver{

        @Override
        public void update(ModelEvent event) {
            if (event == ModelEvent.DATA_PROCESS_STARTED){
                view.disableActions();
            } else if (event == ModelEvent.DATA_PROCESS_FINISHED){
                view.enableActions();
            }
        }
    }

    public class ListenForModel1 implements ModelObserver{

        @Override
        public void update(ModelEvent event) {

            if (event == ModelEvent.DATA_IN_PROCESS){
                BigDecimal[] absoluteError = TimeSeriesScienceLibrary
                        .calculateAbsoluteError(model.getDataChunk(), realDataPoints);

                BigDecimal[] absolutePercentageError = TimeSeriesScienceLibrary
                        .calculatePercentageError(model.getDataChunk(), realDataPoints);

                int index = UtilityMethods.findIndexOfDate(model.getDataChunk().get(0).getX(),
                        realDataPoints);


                for (int i = 0; i < model.getDataChunk().size(); i++){
                    modelA.addRow(new ResultsTableModel.Row(model.getDataChunk().get(i), realDataPoints.get(index),
                            absoluteError[i], absolutePercentageError[i]));
                    index++;
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
            } else if (event == ModelEvent.DATA_PROCESS_FINISHED){
                predictedData.addAll(model.getDataChunk());

                for (Statistics s : Statistics.values()){
                    modelS.addRow(new StatisticsTableModel.Row(s.toString(),
                            s.apply(predictedData, realDataPoints)));
                }

                predictedData.clear();

            }
        }
    }

    public class HandleSaveToFile implements PropertyChangeListener, ViewObserver {

        private CustomFileWriter<ResultsTableModel.Row> textFileWriter;
        private CustomFileWriter<ResultsInfo> jsonFileWriter;
        private ViewEvent ev;
        private String dir = "results\\";
        private DateTimeFormatter format = DateTimeFormatter.ofPattern("_yyyy_MM_dd_");

        public HandleSaveToFile(){
            this.textFileWriter = new TextFileWriter<>(new RowToCSV());
            jsonFileWriter = new JsonFileWriter();
        }

        public HandleSaveToFile(CustomFileWriter<ResultsTableModel.Row> textFileWriter,
                                CustomFileWriter<ResultsInfo> jsonFileWriter){
            this.textFileWriter = textFileWriter;
            this.jsonFileWriter = jsonFileWriter;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
                try {
                    String path = dir.concat(ev.getCurrencyCode().concat(LocalDate.now().format(format)));
                    if (evt.getPropertyName().equals(Menu.SAVE_AS_TEXT)){
                        textFileWriter.saveToFile(path.concat(".txt"), modelA.getListOfRows());

                    } else if (evt.getPropertyName().equals(Menu.SAVE_AS_JSON)){
                        ResultsInfo info = new ResultsInfo(ev.getChosenAlgorithm().toString(),
                                ev.getCurrencyCode(), modelA.getListOfRows());
                        jsonFileWriter.saveToFile(path.concat(".json"), Collections.singletonList(info));
                    }
                }
                catch (IOException e) {
                    JOptionPane.showMessageDialog(null,
                            "Error Occurred", "Error", JOptionPane.ERROR_MESSAGE);

            }
        }

        @Override
        public void update(ViewEvent e) {
            ev = e;
        }
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

                plot.addSeries("Real", modelA.getListOfRows().stream()
                        .mapToDouble(x -> x.getReal().doubleValue()).toArray());

                plot.addSeries("Predicted", modelA.getListOfRows().stream()
                        .mapToDouble(x -> x.getPredicted().doubleValue()).toArray());
            }
        }

        @Override
        public void update(ViewEvent e) {
            plot.setTitle(e.getCurrencyCode() + ", " + e.getStartDate() + ":" + e.getEndDate());
        }

        private Day[] getDates(){
            Day[] arr = new Day[modelA.getRowCount()];
            for (int i = 0; i < modelA.getRowCount(); i++){
                LocalDate d = modelA.getRow(i).getDate();
                arr[i] = new Day(d.getDayOfMonth(), d.getMonthValue(), d.getYear());
            }
            return arr;
        }

    }

}