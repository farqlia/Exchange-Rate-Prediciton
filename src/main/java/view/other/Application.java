package view.other;

import controller.Controller;
import currencyparsing.currencyurlworker.CurrencyNamesLoader;
import currencyparsing.currencyurlworker.Loader;
import exchangerateclass.CurrencyName;
import model.Model;
import model.TableModel;
import view.view.View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Vector;

public class Application {

    public static void main(String[] args) {

        DefaultTableModel modelA =
                new TableModel(new Vector<>(List.of("Date", "Real", "Predicted", "y - y*", "(y - y*) / y")), 0, "Results");

        DefaultTableModel modelS =
                new TableModel(new Vector<>(List.of("Name", "Value")), 0, "Statistics");

        Loader<CurrencyName> currencyNameLoader =
                new CurrencyNamesLoader();

        View view = new View(currencyNameLoader.load(), modelA, modelS);
        Model model = new Model();

        Controller c = new Controller(view, model, modelA, modelS);

        SwingUtilities.invokeLater(() -> view.setVisible(true));



    }

}
