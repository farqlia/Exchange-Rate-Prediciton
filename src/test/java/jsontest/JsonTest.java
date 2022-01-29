package jsontest;

import algorithms.algorithmsinitializer.AlgorithmInitializerExPost;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import datasciencealgorithms.utils.point.Point;
import exchangerateclass.ExchangeRate;
import model.ResultsTableModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import studyjson.ResultsInfo;
import studyjson.ResultsInfoSerializer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class JsonTest {

    ObjectMapper mapper;
    @BeforeEach
    void setUp(){
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(ResultsInfo.class, new ResultsInfoSerializer());
        mapper.registerModule(module);
    }

    @Test
    void shouldReturnList() throws JsonProcessingException {
        String json = "[{\"algorithmTableModel\":\"A\",\"no\":\"253/A/NBP/2021\",\"effectiveDate\":\"2021-12-30\",\"rates\":[{\"currency\":\"bat (Tajlandia)\",\"code\":\"THB\",\"mid\":0.1216}," +
                "{\"currency\":\"dolar amerykański\",\"code\":\"USD\",\"mid\":4.0631}]}]";

        JsonNode node = mapper.readTree(json);
        for (JsonNode jsonNode : node) {
            System.out.println(jsonNode.at("/effectiveDate"));
        }
        System.out.println(node.get(0).at("/rates"));

        List<ExchangeRate> rates = mapper.convertValue(node.get(0).at("/rates"), new TypeReference<List<ExchangeRate>>(){});
        rates.forEach(System.out::println);

    }

    @Test
    void shouldSerialize() throws JsonProcessingException {
        Point p = new Point(LocalDate.of(2022, 1, 18), BigDecimal.ZERO);
        ResultsTableModel.Row row = new ResultsTableModel.Row(p, p, BigDecimal.ONE, BigDecimal.TEN);
        ResultsInfo info = new ResultsInfo(AlgorithmInitializerExPost.MAMA.toString(),
                "EUR", Collections.singletonList(row));
        String s = mapper.writeValueAsString(info);
        System.out.println(s);
        Assertions.assertEquals("{\"algorithm\":\"Moving Average Mean Algorithm\",\"code\":\"EUR\",\"Predictions\":" +
                        "[{\"Date\":\"2022-01-18\",\"Real\":0E-8,\"Predicted\":0E-8}]}",
                s);
    }

}
