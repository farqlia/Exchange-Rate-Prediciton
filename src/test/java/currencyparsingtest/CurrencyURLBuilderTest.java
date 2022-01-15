package currencyparsingtest;

import currencyparsing.currencyurlbuilders.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

// NOTE : tests shouldn't be run all at once, because builders are static classes, so they are
// shared between the instances and the old values affect new instances
public class CurrencyURLBuilderTest {

    CurrencyURL currencyURL;
    String baseAddress = "http://api.nbp.pl/api/";

    @Test
    void shouldCorrectlyCreateInstanceOfTable(){

        currencyURL = new AllCurrenciesURL.Builder(MoneyType.CURRENCY).build();
        Assertions.assertEquals(baseAddress + "exchangerates/tables/", currencyURL.getURL());

    }

    @Test
    void shouldCorrectlyCreateInstanceOfConcreteCurrency(){

        currencyURL = new ConcreteCurrencyURL.Builder(MoneyType.CURRENCY).build();
        Assertions.assertEquals(baseAddress + "exchangerates/rates/", currencyURL.getURL());

    }

    @Test
    void shouldCreateRequestForConcreteCurrency(){

        currencyURL = new ConcreteCurrencyURL.Builder(MoneyType.CURRENCY)
                .addCurrencyCode("EUR")
                .addTable(Table.A)
                .build();

        Assertions.assertEquals(baseAddress + "exchangerates/rates/A/EUR/", currencyURL.getURL());
    }

    @Test
    void shouldCreateRequestForTable(){

        currencyURL = new AllCurrenciesURL.Builder(MoneyType.CURRENCY)
                .addTable(Table.C)
                .build();

        Assertions.assertEquals(baseAddress + "exchangerates/tables/C/", currencyURL.getURL());

    }

    @Test
    void shouldCreateRequestForTableAndTopCount(){

        currencyURL = new AllCurrenciesURL.Builder(MoneyType.CURRENCY)
                .addDate(10)
                .addTable(Table.C)
                .build();

        Assertions.assertEquals(baseAddress + "exchangerates/tables/C/last/10/", currencyURL.getURL());

    }

    @Test
    void shouldCreateRequestForTableForDate(){

        currencyURL = new AllCurrenciesURL.Builder(MoneyType.CURRENCY)
                .addDate(LocalDate.now())
                .addTable(Table.C)
                .build();

        Assertions.assertEquals(baseAddress + "exchangerates/tables/C/today/", currencyURL.getURL());

    }

    @Test
    void shouldCreateRequestForTableForBetweenDates(){

        currencyURL = new AllCurrenciesURL.Builder(MoneyType.CURRENCY)
                .addDate(LocalDate.of(2021, 12, 30), LocalDate.of(2022, 1, 1))
                .addTable(Table.C)
                .build();

        Assertions.assertEquals(baseAddress + "exchangerates/tables/C/2021-12-30/2022-01-01/", currencyURL.getURL());

    }

    @Test
    void shouldCreateRequestForConcreteCurrencyFromBetweenDates(){

        // There is also an option to make request for 'cenyzlota'
        currencyURL = new ConcreteCurrencyURL.Builder(MoneyType.CURRENCY)
                .addDate(LocalDate.of(2021, 12, 30), LocalDate.of(2022, 1, 1))
                .addTable(Table.C)
                .addCurrencyCode("EUR")
                .build();

        Assertions.assertEquals(baseAddress + "exchangerates/rates/C/EUR/2021-12-30/2022-01-01/", currencyURL.getURL());

    }

    @Test
    void shouldResetAllFields(){
        ConcreteCurrencyURL.Builder b = new ConcreteCurrencyURL.Builder(MoneyType.CURRENCY)
                .addDate(LocalDate.of(2021, 12, 30), LocalDate.of(2022, 1, 1))
                .addTable(Table.C)
                .addCurrencyCode("EUR");

        // ALl fields should be set to default
        b.reset();

        currencyURL = b.build();
        Assertions.assertEquals(baseAddress + "exchangerates/rates/", currencyURL.getURL());

    }

    ConcreteCurrencyURL.Builder builder;

    @BeforeEach
    void setUp2(){
        builder = new ConcreteCurrencyURL.Builder(MoneyType.CURRENCY);
    }

    @RepeatedTest(3)
    void shouldResetAllFieldsMultipleTimes(){
        CurrencyURL url = builder.reset().addTable(Table.A).addCurrencyCode("EUR").addDate(2).build();
        Assertions.assertEquals("http://api.nbp.pl/api/exchangerates/rates/A/EUR/last/2/",
                url.getURL());
    }

}
