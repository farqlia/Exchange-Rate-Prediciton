package currencyparsing.currencyurlworker;

import currencyparsing.currencymapper.CurrencyNameMapper;
import currencyparsing.currencymapper.CurrencyObjectMapper;
import currencyparsing.currencyurlbuilders.AllCurrenciesURL;
import currencyparsing.currencyurlbuilders.CurrencyURL;
import currencyparsing.currencyurlbuilders.MoneyType;
import exchangerateclass.CurrencyName;

import java.util.Collections;
import java.util.List;

public class CurrencyNamesLoader extends Loader<CurrencyName>{

    public CurrencyNamesLoader(){
        super(new AllCurrenciesURL.Builder(MoneyType.CURRENCY).build(),
                CurrencyNameMapper.getInstance());
    }

    public CurrencyNamesLoader(AllCurrenciesURL url, CurrencyNameMapper mapper){
        super(url, mapper);
    }

    @Override
    protected List<CurrencyName> getDefault(){
        return Collections.singletonList(new CurrencyName("euro", "EUR"));
    }

}
