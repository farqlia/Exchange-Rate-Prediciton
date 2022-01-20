package view.other;

import controller.Controller;
import currencyparsing.currencymapper.CurrencyNameMapper;
import currencyparsing.currencyurlbuilders.AllCurrenciesURL;
import currencyparsing.currencyurlbuilders.MoneyType;
import currencyparsing.currencyurlbuilders.Table;
import currencyparsing.currencyurlworker.Loader;
import exchangerateclass.CurrencyName;
import model.*;
import view.view.View;

import javax.swing.*;
import java.util.List;

public class Application {

    public static void main(String[] args) {

        CustomTableModel<ResultsTableModel.Row> modelA =
                new ResultsTableModel(List.of("Date", "Real", "Predicted", "Error", "Percentage Error") ,"Results");

        CustomTableModel<StatisticsTableModel.Row> modelS =
                new StatisticsTableModel(List.of("Name", "Value"),"Statistics");

        Loader<CurrencyName> currencyNameLoader =
                new Loader<>(new CurrencyNameMapper());

        currencyNameLoader.setCurrencyURL(new AllCurrenciesURL.Builder(MoneyType.CURRENCY)
                .addTable(Table.A)
                .build());

        View view = new View(currencyNameLoader.load(), modelA, modelS);
        Model model = new Model();

        Controller c = new Controller(view, model, modelA, modelS);

        SwingUtilities.invokeLater(() -> view.setVisible(true));

    }

}
