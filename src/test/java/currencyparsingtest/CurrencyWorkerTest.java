package currencyparsingtest;

import currencyparsing.currencyurlworker.CurrencyWorker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class CurrencyWorkerTest {

    CurrencyWorker currencyWorker;

    @BeforeEach
    void setUp(){
        currencyWorker = new CurrencyWorker();
    }

    @Test
    void shouldSendSuccessfulRequest(){
        String exampleRequest = "http://api.nbp.pl/api/exchangerates/rates/a/chf/";

        Optional<String> opt = currencyWorker.send(exampleRequest);
        Assertions.assertTrue(opt.isPresent());
    }

    @Test
    void shouldSendSuccessfulRequestForTables(){
        String exampleRequest = "http://api.nbp.pl/api/exchangerates/tables/a/";

        Optional<String> opt = currencyWorker.send(exampleRequest);
        Assertions.assertTrue(opt.isPresent());
    }

    @Test
    void shouldReturnEmptyOptionalForLackingData(){
        String exampleRequest = "http://api.nbp.pl/api/exchangerates/rates/c/usd/2022-01-01/";

        Optional<String> opt = currencyWorker.send(exampleRequest);
        Assertions.assertTrue(opt.isEmpty());
    }


    @Test
    void shouldReturnEmptyOptionalForTooLargeTimePeriod(){
        String exampleRequest = "http://api.nbp.pl/api/exchangerates/rates/c/usd/2022-01-01/";

        Optional<String> opt = currencyWorker.send(exampleRequest);
        Assertions.assertTrue(opt.isEmpty());
    }

    @Test
    void shouldReturnEmptyOptionalForMalformedRequest(){
        String exampleRequest = "http://api.nbp.pl/api/badrequest";

        Optional<String> opt = currencyWorker.send(exampleRequest);
        Assertions.assertTrue(opt.isEmpty());
    }

}
