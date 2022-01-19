package viewtest;

import currencyparsing.currencymapper.CurrencyNameMapper;
import currencyparsing.currencymapper.SingleRateMapper;
import currencyparsing.currencyurlbuilders.*;
import currencyparsing.currencyurlworker.CurrencyNamesLoader;
import currencyparsing.currencyurlworker.ExchangeRateLoader;
import currencyparsing.currencyurlworker.Loader;
import exchangerateclass.CurrencyName;
import exchangerateclass.ExchangeRate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyLoaderTest {

    Loader<CurrencyName> cNL;
    @Mock
    AllCurrenciesURL allCurrenciesURL;
    @Mock
    CurrencyNameMapper currencyNameMapper;

    @BeforeEach
    void setUp(){
        cNL = new CurrencyNamesLoader(allCurrenciesURL, currencyNameMapper);
    }

    @Test
    void shouldLoadNamesCallingAPI(){

        // Happy scenario when the call to the API was successful
        when(allCurrenciesURL.getURL())
                .thenReturn("http://api.nbp.pl/api/exchangerates/tables/a/");
        // This command should load (in practise, this makes call
        // to API)
        cNL.load();
        // We can't verify whether the mapper has parsed successfully since we can't
        // call real methods on mocks. This is a job for another testing class, so just check
        // whether the 1st 'if' statement wasn't executed
        verify(currencyNameMapper).parse(anyString());
    }

    @Test
    void shouldLoadNamesAfterUnsuccessfulCall(){

        when(allCurrenciesURL.getURL())
                .thenReturn("http://api.nbp.pl/api/exchangerates/BADREQUEST");
        // This command should load (in practise, this makes call
        // to API)
        Assertions.assertFalse(cNL.load().isEmpty());
        // It shouldn't be called at all, since the call to API failed
        verify(currencyNameMapper, never()).parse(anyString());
    }

    @Mock
    SingleRateMapper singleRateMapper;
    @Mock
    ConcreteCurrencyURL url;


    @Test
    void shouldLoadSingleExchangeRates(){

        Loader<ExchangeRate> loader = new ExchangeRateLoader(url, singleRateMapper);

        when(url.getURL()).thenReturn("http://api.nbp.pl/api/exchangerates/rates/a/gbp/2012-01-02/");

        loader.setCurrencyURL(url);
        loader.load();

        verify(singleRateMapper).parse(anyString());
        verify(url).getURL();
    }


}
