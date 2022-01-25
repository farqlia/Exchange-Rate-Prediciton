package view.other;

import mvc.Controller;
import currencyparsing.currencymapper.CurrencyNameMapper;
import currencyparsing.currencyurlbuilders.AllCurrenciesURL;
import currencyparsing.currencyurlbuilders.MoneyType;
import currencyparsing.currencyurlbuilders.Table;
import currencyparsing.currencyurlworker.Loader;
import exchangerateclass.CurrencyName;
import model.*;
import mvc.JTableView;
import mvc.Model;
import mvc.View;

import javax.swing.*;
import java.util.List;

public class Application {

    public static List<String> cols = List.of("Date", "Real", "Predicted", "Error", "Percentage Error");

    public static void main(String[] args) {

        Loader<CurrencyName> currencyNameLoader =
                new Loader<>(new CurrencyNameMapper());

        currencyNameLoader.setCurrencyURL(new AllCurrenciesURL.Builder(MoneyType.CURRENCY)
                .addTable(Table.A)
                .build());

        Model modelExPost = new Model();
        Model modelExAnte = new Model();

        CustomTableModel<ResultsTableModel.Row> tableModelExPost =
                new ResultsTableModel(cols,"Results",
                        modelExPost);

        CustomTableModel<ResultsTableModel.Row> tableModelExAnte =
                new ResultsTableModel(cols ,"Predictions",
                        modelExAnte);

        CustomTableModel<StatisticsTableModel.Row> tableModelStatistics =
                new StatisticsTableModel(List.of("Name", "Value"),"Statistics", modelExPost);

        JTable tableExPost = new JTableView(tableModelExPost, "Results");
        JTable tableExAnte = new JTableView(tableModelExAnte, "Predictions");
        JTable tableStatistics = new JTableView(tableModelStatistics, "Results");

        View view = new View(currencyNameLoader.load(), tableExPost, tableExAnte, tableStatistics);

        Controller c = new Controller(view, tableExPost,
                modelExPost, modelExAnte,
                tableModelExPost, tableModelStatistics, tableModelExAnte);

        SwingUtilities.invokeLater(() -> view.setVisible(true));

    }

}
