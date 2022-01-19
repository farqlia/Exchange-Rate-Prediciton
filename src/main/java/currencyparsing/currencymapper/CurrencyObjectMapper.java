package currencyparsing.currencymapper;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

public abstract class CurrencyObjectMapper<E> {

    ObjectMapper mapper = new ObjectMapper();
    JsonPointer ratesPointer = JsonPointer.compile("/rates");

    abstract List<E> parse(JsonNode node);

    /***
     * Parses given json-type String into list of E objects
     * @param json
     * @return List of objects converted from String or empty list
     * if conversion was unsuccessful
     * @throws JsonProcessingException if a given String is not json-like
     */
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
        List<E> parsedData;

        if (jsonNodeRoot.isArray()){
            parsedData = parse(jsonNodeRoot.get(0));
        } else {
           parsedData = parse(jsonNodeRoot);
        }

        // If the String is correctly build according to json format,
        // but can't be parsed to given class, then null is returned
        return parsedData == null ? Collections.emptyList() : parsedData;
    }

}
