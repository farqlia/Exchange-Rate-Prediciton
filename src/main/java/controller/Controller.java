package controller;

import currencyparsing.currencymapper.SingleRateMapper;
import currencyparsing.currencyurlbuilders.ConcreteCurrencyURL;
import currencyparsing.currencyurlbuilders.MoneyType;
import currencyparsing.currencyurlbuilders.Table;
import currencyparsing.currencyurlworker.CurrencyWorker;
import datagenerator.DataGenerator;
import datasciencealgorithms.utils.point.Point;
import exchangerateclass.ExchangeRate;
import model.Model;
import view.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Controller {

    Model model;
    AbstractView view;

    public Controller(AbstractView view, Model model){
        this.view = view;
        this.model = model;
    }

    public class ListenForView implements ViewObserver{

        List<Point<LocalDate>> actualDataPoints = new ArrayList<>(100);
        CurrencyWorker currencyWorker = new CurrencyWorker();
        ConcreteCurrencyURL.Builder urlBuilder = new ConcreteCurrencyURL.Builder(MoneyType.CURRENCY);

        @Override
        public void update(ViewEvent e) {

            Optional<String> response = currencyWorker.send(urlBuilder.reset()     // Reset the old values
                    .addTable(Table.C)
                    .addDate(e.getStartDate(), e.getEndDate())
                    .addCurrencyCode(e.getCurrencyCode())
                    .build()
                    .getURL());

            if (response.isEmpty()){
                System.out.println("Invalid argument");
                return;
            }

            List<ExchangeRate> exchangeRatesList = SingleRateMapper
                    .getInstance().parse(response.get());

            for (ExchangeRate eR : exchangeRatesList){
                // Date in ExchangeRates class is 'Date' type so we convert it to LocalDate
                actualDataPoints.add(new Point<>(LocalDate.ofInstant(eR.getEffectiveDate().toInstant(),
                        ZoneId.of("CET")),
                        eR.getAsk()));
            }

            model.setAlgorithm(e.getChosenAlgorithm());

            List<Point<LocalDate>> predictedData =
                    model.predict(actualDataPoints, e.getStartDate(), e.getEndDate());

            view.updateTable(DataPointsFlattering.getInstance().flatten(actualDataPoints,
                    predictedData));

            actualDataPoints.clear();
        }
    }


}