package currencyparsing.currencyurlworker;

import dataconverter.formatters.Formatter;
import dataconverter.writersandreaders.CustomFileWriter;
import dataconverter.writersandreaders.TextFileWriter;
import exchangerateclass.CurrencyName;

import java.io.IOException;

class LoadCurrencyNames {

    public static void main(String[] args) throws IOException {

        Formatter<CurrencyName> printable = new CSVParser();
        CustomFileWriter<CurrencyName> writer = new TextFileWriter<>(printable);

        Loader<CurrencyName> currencyNameLoader = new CurrencyNamesLoader();

        writer.saveToFile("defaultcurrencies\\defaultcurrencies.txt",
                currencyNameLoader.load());


    }

}
