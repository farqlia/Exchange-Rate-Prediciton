package viewtest;

import currencyparsing.currencymapper.CurrencyNameMapper;
import currencyparsing.currencyurlbuilders.AllCurrenciesURL;
import currencyparsing.currencyurlworker.CurrencyNamesLoader;
import currencyparsing.currencyurlworker.Loader;
import exchangerateclass.CurrencyName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import view.View;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyNamesLoaderTest {

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
    void shouldLoadIconsCallingAPI(){

        // Happy scenario when the call to the API was successful
        when(allCurrenciesURL.getURL())
                .thenReturn("http://api.nbp.pl/api/exchangerates/tables/a/");
        // This command should load (in practise, this makes call
        // to API)
        Assertions.assertFalse(cNL.load().isEmpty());
        verify(allCurrenciesURL).getURL();
    }

    @Test
    void shouldLoadIconsAfterUnsuccessfulCall(){

        when(allCurrenciesURL.getURL())
                .thenReturn("http://api.nbp.pl/api/exchangerates/BADREQUEST");
        // This command should load (in practise, this makes call
        // to API)
        Assertions.assertFalse(cNL.load().isEmpty());
        // It shouldn't be called at all, since the call to API failed
        verify(currencyNameMapper, never()).parse(anyString());
    }


}
