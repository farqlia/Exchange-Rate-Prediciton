package currencyparsing.currencymapper;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

public abstract class CurrencyObjectMapper<E> {

    protected ObjectMapper mapper = new ObjectMapper();
    protected JsonPointer ratesPointer = JsonPointer.compile("/rates");

    abstract List<E> parse(JsonNode node);

    public List<E> parse(String json){

        JsonNode jsonNodeRoot;
        try {

            jsonNodeRoot = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        if (jsonNodeRoot.isMissingNode() || jsonNodeRoot.isNull()){
            return Collections.emptyList();
        }
        // Returned node is an array, so we extract actual element
        return parse(jsonNodeRoot);
    }

}
