package currencyparsing.currencyurlworker;

import currencyparsing.currencymapper.CurrencyObjectMapper;
import currencyparsing.currencymapper.SingleRateMapper;
import currencyparsing.currencyurlbuilders.AllCurrenciesURL;
import currencyparsing.currencyurlbuilders.ConcreteCurrencyURL;
import currencyparsing.currencyurlbuilders.CurrencyURL;
import currencyparsing.currencyurlbuilders.MoneyType;
import exchangerateclass.ExchangeRate;

public class ExchangeRateLoader extends Loader<ExchangeRate> {

    public ExchangeRateLoader(ConcreteCurrencyURL url, CurrencyObjectMapper<ExchangeRate> mapper){
        super(url, mapper);
    }

    public ExchangeRateLoader(){
        super(new ConcreteCurrencyURL.Builder(MoneyType.CURRENCY).build(),
                SingleRateMapper.getInstance());
    }

}
