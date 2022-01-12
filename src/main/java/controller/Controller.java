package controller;

import currencyparsing.currencyurlbuilders.*;
import currencyparsing.currencyurlworker.ExchangeRateLoader;
import currencyparsing.currencyurlworker.Loader;
import datasciencealgorithms.utils.point.Point;
import exchangerateclass.ExchangeRate;
import mathlibraries.Statistics;
import model.Model;
import view.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    Model model;
    AbstractView view;

    public Controller(AbstractView view, Model model){
        this.view = view;
        this.model = model;
        this.view.registerObserver(new ListenForView());
    }

    public class ListenForView implements ViewObserver{

        List<Point> expectedDataPoints = new ArrayList<>(100);
        Loader<ExchangeRate> exchangeRateLoader = new ExchangeRateLoader();
        ConcreteCurrencyURL.Builder builder = new ConcreteCurrencyURL.Builder(MoneyType.CURRENCY);

        @Override
        public void update(ViewEvent e) {

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
                return;
            }

            for (ExchangeRate eR : exchangeRatesList){
                // Date in ExchangeRates class is 'Date' type so we convert it to LocalDate
                expectedDataPoints.add(new Point(LocalDate.ofInstant(eR.getEffectiveDate().toInstant(),
                        ZoneId.systemDefault()),
                        eR.getMid()));
            }

            model.setAlgorithm(e.getChosenAlgorithm(), e.getLookbackPeriod());

            List<Point> predictedData =
                    model.predict(expectedDataPoints, e.getStartDate(), e.getEndDate());

            // Joins both lists into Object[][] type, where each row consists of date, expected and predicted value
            // flatten() method also 'cuts' data so that the both lists are the same
            // length and have the same time period
            view.insertAlgorithmOutput(DataPointsFlattering.getInstance().flatten(expectedDataPoints,
                    predictedData));

            handleStatistics(expectedDataPoints, predictedData);

            expectedDataPoints.clear();
        }

        private void handleStatistics(List<Point> expectedData, List<Point> predictedData){

            Object[][] data = new Object[Statistics.values().length][2];
            // Creates array of rows, where each row is an array containing name of statistical function and it's output
            for (int i = 0; i < Statistics.values().length; i++){
                data[i][0] = Statistics.values()[i].name();
                data[i][1] = Statistics.values()[i].apply(predictedData, expectedData);
            }

            view.insertStatistics(data);

        }
    }

}