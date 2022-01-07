package currencyparsingtest;

import currencyparsing.currencymapper.CurrencyNameMapper;
import currencyparsing.currencymapper.SingleRateMapper;
import exchangerateclass.CurrencyName;
import exchangerateclass.ExchangeRate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;

public class CurrencyObjectMapperTest {


    @Test
    void shouldNtParseForCurrencyNames(){
        // ? Why null and not an exception
        Assertions.assertNull(CurrencyNameMapper.getInstance().parse("{\"badjson\":\"AAA\"}"));
    }

    @Test
    void shouldNtParseForSingleRate(){
        Assertions.assertTrue(SingleRateMapper.getInstance().parse("{\"badjson\":\"AAA\"}").isEmpty());
    }


    @Test
    void shouldParseFullJsonForCurrencyNames(){
        String exampleJson = "{\"table\":\"A\",\"no\":\"253/A/NBP/2021\",\"effectiveDate\":\"2021-12-30\",\"rates\":[{\"currency\":\"bat (Tajlandia)\",\"code\":\"THB\",\"mid\":0.1216}," +
                "{\"currency\":\"dolar amerykański\",\"code\":\"USD\",\"mid\":4.0631}]}";

        List<CurrencyName> rates = CurrencyNameMapper.getInstance().parse(exampleJson);

        // If something went wrong with sending/receiving a request, then we return an empty list
        Assertions.assertFalse(rates.isEmpty());
        Assertions.assertAll(
                () -> Assertions.assertEquals("dolar amerykański", rates.get(1).getCurrency()),
                () -> Assertions.assertEquals("USD", rates.get(1).getCode())
        );

    }

    @Test
    void shouldParseFullJsonForSingleRate(){
        String json = "{\"table\":\"C\",\"currency\":\"euro\",\"code\":\"EUR\",\"rates\":[{\"no\":\"245/C/NBP/2021\",\"effectiveDate\":\"2021-12-20\",\"bid\":4.5901,\"ask\":4.6829}," +
                "{\"no\":\"246/C/NBP/2021\",\"effectiveDate\":\"2021-12-21\",\"bid\":4.5870,\"ask\":4.6796}]}";

        List<ExchangeRate> rates = SingleRateMapper.getInstance().parse(json);

        Assertions.assertAll(
                () -> Assertions.assertEquals("euro", rates.get(0).getCurrency()),
                () -> Assertions.assertEquals("EUR",rates.get(0).getCode()),
                () -> Assertions.assertEquals(rates.get(0).getBid(), new BigDecimal("4.5901"))
                );
    }
}
