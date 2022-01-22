package mvc;

import currencyparsing.currencymapper.SingleRateMapper;
import currencyparsing.currencyurlbuilders.*;
import currencyparsing.currencyurlworker.Loader;
import datasciencealgorithms.utils.point.Point;
import exchangerateclass.ExchangeRate;
import model.*;
import view.IO.FileSaveHandler;
import view.IO.FileTypes;
import view.other.*;
import view.view.AbstractView;
import view.view.ViewEvent;
import view.view.ViewObserver;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    Model model;
    CustomTableModel<ResultsTableModel.Row> modelA;
    CustomTableModel<StatisticsTableModel.Row> modelS;
    AbstractView view;
    Plot plot;

    public Controller(AbstractView view, Model model,
                      CustomTableModel<ResultsTableModel.Row> modelA,
                      CustomTableModel<StatisticsTableModel.Row> modelS){

        this.view = view;
        this.model = model;
        this.modelA = modelA;
        this.modelS = modelS;
        plot = new Plot();

        PlotControllerExPost exPostPlotCon = new PlotControllerExPost(plot, modelA);

        this.view.registerObserver(new ListenForView());
        this.view.registerObserver(new HandlePlotTitle());

        model.registerObserver(new HandleViewAction());

        FileSaveHandler handler = new FileSaveHandler(modelA);

        view.getJMenuBar().addPropertyChangeListener(FileTypes.TEXT.toString(),handler);
        view.getJMenuBar().addPropertyChangeListener(FileTypes.JSON.toString(), handler);
        view.getJMenuBar().addPropertyChangeListener(Plot.CREATE_PLOT_ACTION,
                new HandlePlotVisibility());

        view.registerObserver(handler);

    }

    public class ListenForView implements ViewObserver {

        Loader<ExchangeRate> exchangeRateLoader = new Loader<>(new SingleRateMapper());
        ConcreteCurrencyURL.Builder builder = new ConcreteCurrencyURL.Builder(MoneyType.CURRENCY);
        List<Point> realDataPoints = new ArrayList<>(20);

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

    public class HandlePlotVisibility implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            // Clear previous results
            plot.setVisible(true);
        }
    }

    public class HandlePlotTitle implements ViewObserver{

        @Override
        public void update(ViewEvent e) {
            plot.setVisible(false);
            plot.clear();
            plot.setTitle(e.getCurrencyCode() + ", " + e.getStartDate() + ":" + e.getEndDate());
            // real-time update plot

        }
    }

}