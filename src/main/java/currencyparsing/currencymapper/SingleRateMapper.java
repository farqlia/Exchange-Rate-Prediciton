package currencyparsing.currencymapper;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import exchangerateclass.ExchangeRate;

import java.util.List;

public class SingleRateMapper extends CurrencyObjectMapper<ExchangeRate>{

    private static final SingleRateMapper instance = new SingleRateMapper();

    JsonPointer currencyPointer = JsonPointer.compile("/currency");
    JsonPointer codePointer = JsonPointer.compile("/code");

    private SingleRateMapper(){}

    public static SingleRateMapper getInstance(){
        return instance;
    }

    @Override
    protected List<ExchangeRate> parse(JsonNode root) {
        assert root != null;
        List<ExchangeRate> list = mapper.convertValue(root.at(ratesPointer),
                new TypeReference<List<ExchangeRate>>(){});
        String currency = mapper.convertValue(root.at(currencyPointer), String.class);
        String code = mapper.convertValue(root.at(codePointer), String.class);

        list.forEach(x -> {
            x.setCode(code);
            x.setCurrency(currency);
        });

        return list;
    }
}
