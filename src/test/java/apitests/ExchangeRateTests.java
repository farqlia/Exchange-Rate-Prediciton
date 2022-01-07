package apitests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import exchangerateclass.CurrencyName;
import exchangerateclass.ExchangeRate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import studyjson.CustomExchangeRateSerializer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class ExchangeRateTests {

    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp(){
        SimpleModule module =
                new SimpleModule("CustomExchangeRateSerializer",
                        new Version(1, 0, 0, null, null, null));
        module.addSerializer(ExchangeRate.class, new CustomExchangeRateSerializer());
        mapper.registerModule(module);
    }

    @Test
    void shouldSerializeInstance() throws JsonProcessingException {
        ExchangeRate exR = new ExchangeRate("dolar amerykanski", "USD", new Date(),
                BigDecimal.ONE, BigDecimal.ZERO);

        String stringRate = mapper.writeValueAsString(exR);
        System.out.println(stringRate);
    }

    @Test
    void shouldDeserializeInstanceWithMissingProp() throws JsonProcessingException {
        String json = "{\"currency\":\"dolar amerykanski\",\"effectiveDate\":1639077640811,\"bid\":1}";

        ExchangeRate exR = mapper.readValue(json, ExchangeRate.class);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(exR),
                () -> Assertions.assertNull(exR.getCode()),
                () -> Assertions.assertNull(exR.getAsk())
        );

    }

    @Test
    void shouldIgnoreFieldsNonExistingInClass() throws JsonProcessingException {
        String json = "{\"currency\":\"dolar amerykanski\"," +
                "\"code\":\"EUR\"," +
                "\"effectiveDate\":1639077640811," +
                "\"someField\":" + null + "," +
                "\"ask\":0," +
                "\"bid\":1}";

        // Asserts that does not throw any throwable
        Assertions.assertDoesNotThrow(() -> mapper.readValue(json, ExchangeRate.class));
    }

    @Test
    void shouldConvertToList() throws JsonProcessingException {
        // Actually I should create objects, each for this string representation, and then use equals method to compare them
        String jsonList = "[{\"no\":\"229/C/NBP/2021\",\"effectiveDate\":\"2021-11-26\",\"bid\":4.6228,\"ask\":4.7162}," +
                "{\"no\":\"230/C/NBP/2021\",\"effectiveDate\":\"2021-11-29\",\"bid\":4.6671,\"ask\":4.7613}," +
                "{\"no\":\"231/C/NBP/2021\",\"effectiveDate\":\"2021-11-30\",\"bid\":4.6411,\"ask\":4.7349}]";

        List<ExchangeRate> listOfExR = mapper.readValue(jsonList,
                new TypeReference<List<ExchangeRate>>() {});

        listOfExR.stream().forEach(x -> {
            x.setCurrency("");
            x.setCode("");
        });

        System.out.println(listOfExR);
    }



}
