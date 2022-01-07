package apitests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exchangerateclass.CurrencyName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.util.List;

public class CurrencyNameTest {

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void shouldParseCurrencyAndCodeNames() throws JsonProcessingException {
        String json = "{\"currency\":\"dolar amerykański\",\"code\":\"USD\",\"mid\":4.0631}";

        CurrencyName exR = mapper.readValue(json, CurrencyName.class);
        Assertions.assertAll(() -> Assertions.assertEquals("dolar amerykański", exR.getCurrency()),
                () -> Assertions.assertEquals("USD", exR.getCode()));

    }

    @Test
    void shouldParseCurrencyAndCodeNamesFromResponse(){

        String json = "[{\"currency\":\"bat (Tajlandia)\",\"code\":\"THB\",\"mid\":0.1216}," +
                "{\"currency\":\"dolar amerykański\",\"code\":\"USD\",\"mid\":4.0631}," +
                "{\"currency\":\"dolar australijski\",\"code\":\"AUD\",\"mid\":2.9483}," +
                "{\"currency\":\"euro\",\"code\":\"EUR\",\"mid\":4.5915}]";

        Assertions.assertDoesNotThrow(() -> mapper.readValue(json, new TypeReference<List<CurrencyName>>() {}));;
    }
}
