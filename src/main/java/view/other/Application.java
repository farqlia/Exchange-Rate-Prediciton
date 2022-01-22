package view.other;

import mvc.Controller;
import currencyparsing.currencymapper.CurrencyNameMapper;
import currencyparsing.currencyurlbuilders.AllCurrenciesURL;
import currencyparsing.currencyurlbuilders.MoneyType;
import currencyparsing.currencyurlbuilders.Table;
import currencyparsing.currencyurlworker.Loader;
import exchangerateclass.CurrencyName;
import model.*;
import mvc.Model;
import mvc.View;

import javax.swing.*;
import java.util.List;

public class Application {

    public static void main(String[] args) {

        Loader<CurrencyName> currencyNameLoader =
                new Loader<>(new CurrencyNameMapper());

        currencyNameLoader.setCurrencyURL(new AllCurrenciesURL.Builder(MoneyType.CURRENCY)
                .addTable(Table.A)
                .build());

        Model model = new Model();

        CustomTableModel<ResultsTableModel.Row> modelA =
                new ResultsTableModel(List.of("Date", "Real", "Predicted", "Error", "Percentage Error") ,"Results",
                        model);

        CustomTableModel<StatisticsTableModel.Row> modelS =
                new StatisticsTableModel(List.of("Name", "Value"),"Statistics", model);


        View view = new View(currencyNameLoader.load(), modelA, modelS);

        Controller c = new Controller(view, model, modelA, modelS);

        SwingUtilities.invokeLater(() -> view.setVisible(true));

    }

}
