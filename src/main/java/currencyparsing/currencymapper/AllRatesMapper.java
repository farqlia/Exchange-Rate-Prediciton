package currencyparsing.currencymapper;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import exchangerateclass.ExchangeRate;

import java.util.Date;
import java.util.List;

public class AllRatesMapper extends CurrencyObjectMapper<ExchangeRate> {

    JsonPointer datePointer = JsonPointer.compile("/effectiveDate");

    @Override
    List<ExchangeRate> parse(JsonNode root) {
        List<ExchangeRate> list = mapper.convertValue(root.at(ratesPointer),
                new TypeReference<List<ExchangeRate>>(){});
        Date date = mapper.convertValue(root.at(datePointer), Date.class);

        list.forEach(x -> x.setEffectiveDate(date));

        return list;
    }
}
