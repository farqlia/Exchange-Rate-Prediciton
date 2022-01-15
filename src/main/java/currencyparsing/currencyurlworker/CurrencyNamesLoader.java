package currencyparsing.currencyurlworker;

import currencyparsing.currencymapper.CurrencyNameMapper;
import currencyparsing.currencymapper.CurrencyObjectMapper;
import currencyparsing.currencyurlbuilders.AllCurrenciesURL;
import currencyparsing.currencyurlbuilders.CurrencyURL;
import currencyparsing.currencyurlbuilders.MoneyType;
import currencyparsing.currencyurlbuilders.Table; import dataconverter.writersandreaders.CustomFileReader;
import dataconverter.writersandreaders.TextFileReader;
import exchangerateclass.CurrencyName;


import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class CurrencyNamesLoader extends Loader<CurrencyName>{

    String filePath = "defaultcurrencies\\defaultcurrencies.txt";

    public CurrencyNamesLoader(AllCurrenciesURL url, CurrencyNameMapper mapper){
        super(url, mapper);
    }

    public CurrencyNamesLoader(){
        super(new AllCurrenciesURL.Builder(MoneyType.CURRENCY)
                .addTable(Table.A)
                .build(), CurrencyNameMapper.getInstance());
    }

    @Override
    protected List<CurrencyName> getDefault(){
        CustomFileReader<CurrencyName> reader = new TextFileReader<>(new CSVParser());
        List<CurrencyName> currencyNamesList;
        try {
            currencyNamesList = reader.readFromFile(filePath);
        } catch (IOException | IllegalArgumentException e) {
            return Collections.emptyList();
        }
        return currencyNamesList;
    }

}
