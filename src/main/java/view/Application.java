package view;

import controller.Controller;
import currencyparsing.currencyurlbuilders.AllCurrenciesURL;
import currencyparsing.currencyurlbuilders.MoneyType;
import currencyparsing.currencyurlworker.CurrencyNamesLoader;
import currencyparsing.currencyurlworker.Loader;
import exchangerateclass.CurrencyName;
import model.Model;

import javax.swing.*;

public class Application {

    public static void main(String[] args) {

        View view = prepareView();
        Model model = new Model();

        Controller c = new Controller(view, model);

        SwingUtilities.invokeLater(() -> view.setVisible(true));

    }

    public static View prepareView(){
        Loader<CurrencyName> currencyNameLoader =
                new CurrencyNamesLoader();

        return new View(currencyNameLoader.load());
    }

}
