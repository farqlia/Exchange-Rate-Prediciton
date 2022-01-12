package currencyparsing.currencyurlworker;

import currencyparsing.currencyurlbuilders.AllCurrenciesURL;
import currencyparsing.currencyurlbuilders.MoneyType;
import currencyparsing.currencyurlbuilders.Table;
import dataconverter.PrintableAsCSV;
import dataconverter.writersandreaders.CustomFileWriter;
import dataconverter.writersandreaders.TextFileWriter;
import exchangerateclass.CurrencyName;

import java.io.IOException;
import java.time.LocalDate;

class LoadCurrencyNames {

    public static void main(String[] args) throws IOException {

        PrintableAsCSV<CurrencyName> printable = new CSVParser();
        CustomFileWriter<CurrencyName> writer = new TextFileWriter<>(printable);

        Loader<CurrencyName> currencyNameLoader = new CurrencyNamesLoader();

        writer.saveToFile("defaultcurrencies\\defaultcurrencies.txt",
                currencyNameLoader.load());


    }

}
