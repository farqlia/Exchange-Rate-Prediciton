package view.view;

import algorithms.algorithmsinitializer.AlgorithmInitializer;
import currencyparsing.currencymapper.SingleRateMapper;
import currencyparsing.currencyurlbuilders.ConcreteCurrencyURL;
import currencyparsing.currencyurlbuilders.MoneyType;
import currencyparsing.currencyurlbuilders.Table;
import currencyparsing.currencyurlworker.Loader;
import datasciencealgorithms.utils.point.Point;
import exchangerateclass.ExchangeRate;
import mvc.Model;

import javax.swing.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ListenForView implements ViewObserver {

    Loader<ExchangeRate> exchangeRateLoader = new Loader<>(new SingleRateMapper());
    ConcreteCurrencyURL.Builder builder = new ConcreteCurrencyURL.Builder(MoneyType.CURRENCY);
    List<Point> realDataPoints = new ArrayList<>(20);
    Model model;

    public ListenForView(Model model){
        this.model = model;
    }

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
            JOptionPane.showMessageDialog(null,
                    "Error Occurred", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
