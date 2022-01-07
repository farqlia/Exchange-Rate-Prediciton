package currencyparsingtest;

import currencyparsing.currencymapper.SingleRateMapper;
import currencyparsing.currencyurlbuilders.ConcreteCurrencyURL;
import currencyparsing.currencyurlbuilders.MoneyType;
import currencyparsing.currencyurlbuilders.Table;
import currencyparsing.currencyurlworker.CurrencyWorker;
import exchangerateclass.ExchangeRate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class CurrencyParsingTest {


    @Test
    void shouldParse(){
        CurrencyWorker cW = new CurrencyWorker();

        Optional<String> response = cW.send(new ConcreteCurrencyURL.Builder(MoneyType.CURRENCY)
        .addDate(LocalDate.now().minusDays(10), LocalDate.now())
        .addTable(Table.C)
        .addCurrencyCode("EUR")
        .build()
        .getURL());

        Assertions.assertTrue(response.isPresent());

        List<ExchangeRate> rates = SingleRateMapper.getInstance().parse(response.get());
        Assertions.assertFalse(rates.isEmpty());
    }

}
