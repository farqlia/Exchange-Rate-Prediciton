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
import java.util.Date;
import java.util.List;

public class ListenForView implements ViewObserver {

    private Loader<ExchangeRate> exchangeRateLoader = new Loader<>(new SingleRateMapper());
    private ConcreteCurrencyURL.Builder builder = new ConcreteCurrencyURL.Builder(MoneyType.CURRENCY);
    protected List<Point> realDataPoints = new ArrayList<>(20);
    private Model model;

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

        } else {

            model.setAlgorithm(e.getChosenAlgorithm());

            try {
                //Subclass can decide here how they want to convert from one type to another
                formatPoints(exchangeRatesList);
                model.predict(realDataPoints, e.getStartDate(), e.getEndDate());

            } catch (IllegalStateException ex){
                JOptionPane.showMessageDialog(null,
                        "Error Occurred", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    protected void formatPoints(List<ExchangeRate> exchangeRatesList){

        for (ExchangeRate eR : exchangeRatesList){
            // Date in ExchangeRates class is 'Date' type so convert it to LocalDate
            realDataPoints.add(new Point(formatDate(eR.getEffectiveDate()),
                    eR.getMid()));
        }
    }

    public LocalDate formatDate(Date date){
        return LocalDate.ofInstant(date.toInstant(),
                ZoneId.systemDefault());
    }

}
