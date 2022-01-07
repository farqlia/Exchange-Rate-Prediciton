package currencyparsing.currencymapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import exchangerateclass.CurrencyName;

import java.util.List;

public class CurrencyNameMapper extends CurrencyObjectMapper<CurrencyName>{

    private static final CurrencyNameMapper instance = new CurrencyNameMapper();

    private CurrencyNameMapper(){
    }

    public static CurrencyNameMapper getInstance(){
        return instance;
    }

    @Override
    protected List<CurrencyName> parse(JsonNode root) {
        return mapper.convertValue(root.at(ratesPointer),
                new TypeReference<List<CurrencyName>>(){});
    }
}
