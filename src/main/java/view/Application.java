package view;

import controller.Controller;
import controller.TableModel;
import currencyparsing.currencyurlbuilders.AllCurrenciesURL;
import currencyparsing.currencyurlbuilders.MoneyType;
import currencyparsing.currencyurlworker.CurrencyNamesLoader;
import currencyparsing.currencyurlworker.Loader;
import exchangerateclass.CurrencyName;
import model.Model;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Vector;

public class Application {

    public static void main(String[] args) {

        DefaultTableModel modelA =
                new DefaultTableModel(new Vector<>(List.of("Date", "real", "Predicted", "y - y*", "(y - y*) / y")), 0)
                {
                    @Override
                    public Class<?> getColumnClass(int columnIndex) {
                        return getValueAt(0, columnIndex).getClass();
                    }
                };
        DefaultTableModel modelS =
                new DefaultTableModel(new Vector<>(List.of("Name", "Value")), 0)
                {
                    @Override
                    public Class<?> getColumnClass(int columnIndex) {
                        return getValueAt(0, columnIndex).getClass();
                    }
                };;

        Loader<CurrencyName> currencyNameLoader =
                new CurrencyNamesLoader();

        View view = new View(currencyNameLoader.load(), modelA, modelS);
        Model model = new Model();

        Controller c = new Controller(view, model, modelA, modelS);

        SwingUtilities.invokeLater(() -> view.setVisible(true));



    }

}
