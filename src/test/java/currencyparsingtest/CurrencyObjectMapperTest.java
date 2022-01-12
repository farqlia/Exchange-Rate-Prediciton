package currencyparsingtest;

import currencyparsing.currencymapper.AllRatesMapper;
import currencyparsing.currencymapper.CurrencyNameMapper;
import currencyparsing.currencymapper.SingleRateMapper;
import exchangerateclass.CurrencyName;
import exchangerateclass.ExchangeRate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class CurrencyObjectMapperTest {


    @Test
    void shouldNtParseForCurrencyNames(){
        // ? Why null and not an exception
        Assertions.assertTrue(CurrencyNameMapper.getInstance().parse("{\"badjson\":\"AAA\"}").isEmpty());
    }


    @Test
    void shouldNtParseForSingleRate(){
        Assertions.assertTrue(SingleRateMapper.getInstance().parse("badjson").isEmpty());
    }


    @Test
    void shouldParseFullJsonForCurrencyNames(){
        // Json for all currencies call is in form of a list
        String exampleJson = "[{\"algorithmTableModel\":\"A\",\"no\":\"253/A/NBP/2021\",\"effectiveDate\":\"2021-12-30\",\"rates\":[{\"currency\":\"bat (Tajlandia)\",\"code\":\"THB\",\"mid\":0.1216}," +
                "{\"currency\":\"dolar amerykański\",\"code\":\"USD\",\"mid\":4.0631}]}]";

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
        String json = "{\"algorithmTableModel\":\"C\",\"currency\":\"euro\",\"code\":\"EUR\",\"rates\":" +
                "[{\"no\":\"245/C/NBP/2021\",\"effectiveDate\":\"2021-12-20\",\"bid\":4.5901,\"ask\":4.6829}," +
                "{\"no\":\"246/C/NBP/2021\",\"effectiveDate\":\"2021-12-21\",\"bid\":4.5870,\"ask\":4.6796}]}";

        List<ExchangeRate> rates = SingleRateMapper.getInstance().parse(json);

        Assertions.assertAll(
                () -> Assertions.assertEquals("euro", rates.get(0).getCurrency()),
                () -> Assertions.assertEquals("EUR",rates.get(0).getCode()),
                () -> Assertions.assertEquals(rates.get(0).getBid(), new BigDecimal("4.5901"))
                );
    }

    @Test
    void shouldParseFullJsonForSingleRateAndMid(){
        String json = "{\"table\":\"A\",\"currency\":\"euro\",\"code\":\"EUR\",\"rates\":" +
                "[{\"no\":\"001/A/NBP/2021\",\"effectiveDate\":\"2021-01-04\",\"mid\":4.5485}," +
                "{\"no\":\"002/A/NBP/2021\",\"effectiveDate\":\"2021-01-05\",\"mid\":4.5446}]}";

        List<ExchangeRate> listOfExR =  SingleRateMapper.getInstance().parse(json);

        Assertions.assertAll(
                () -> Assertions.assertEquals(LocalDate.parse("2021-01-04"), LocalDate.ofInstant(listOfExR.get(0).getEffectiveDate().toInstant(), ZoneId.of("CET"))),
                () -> Assertions.assertEquals(new BigDecimal("4.5485"), listOfExR.get(0).getMid()),
                () -> Assertions.assertEquals("EUR", listOfExR.get(0).getCode())
        );


    }
}
