package apitests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertThat;

import java.util.LinkedHashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;

public class DeserializeList {

    class MyDto{
        String str;
        int i;
        boolean b;

        public MyDto(){}

        public MyDto(String str, int i, boolean b) {
            this.str = str;
            this.i = i;
            this.b = b;
        }
    }

    @Test
    public void shouldNotDeserializeCorrectlyListWithNoTypeInfo() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        List<MyDto> listOfStrings =
                List.of(new MyDto("a", 1, true),
                        new MyDto("b", 2, true));
        String jsonList = mapper.writeValueAsString(listOfStrings);

        // Reading JSON into a Java Collection is a bit more difficult -
        // bu default, Jackson will not be able to get the full generic type
        // information
        List<MyDto> asList = mapper.readValue(jsonList, List.class);
        // How to fix this
        // assertThat((Object)asList.get(0), instanceOf(LinkedHashMap.class));
    }

    @Test
    public void shouldDeserializeCorrectlyListWithTypeInfo() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        List<MyDto> listOfStrings =
                List.of(new MyDto("a", 1, true),
                        new MyDto("b", 2, true));
        String jsonList = mapper.writeValueAsString(listOfStrings);

        // We help Jackson figure out type by providing TypeReference
        List<MyDto> asList = mapper.readValue(jsonList,
                new TypeReference<List<MyDto>>(){});

        Assertions.assertTrue(String.class.isInstance(asList.get(0)));
        // How to fix this :(
        // assertThat((Object)asList.get(0), instanceOf(LinkedHashMap.class));
    }


}
