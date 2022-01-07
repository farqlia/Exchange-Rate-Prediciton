package jsontest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import exchangerateclass.ExchangeRate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

public class JsonTest {

    @Test
    void shouldReturnList() throws JsonProcessingException {
        String json = "[{\"table\":\"A\",\"no\":\"253/A/NBP/2021\",\"effectiveDate\":\"2021-12-30\",\"rates\":[{\"currency\":\"bat (Tajlandia)\",\"code\":\"THB\",\"mid\":0.1216}," +
                "{\"currency\":\"dolar amerykański\",\"code\":\"USD\",\"mid\":4.0631}]}]";
        ObjectMapper mapper = new ObjectMapper();

        JsonNode node = mapper.readTree(json);
        for (JsonNode jsonNode : node) {
            System.out.println(jsonNode.at("/effectiveDate"));
        }
        System.out.println(node.get(0).at("/rates"));

        List<ExchangeRate> rates = mapper.convertValue(node.get(0).at("/rates"), new TypeReference<List<ExchangeRate>>(){});
        rates.forEach(System.out::println);

    }

}
