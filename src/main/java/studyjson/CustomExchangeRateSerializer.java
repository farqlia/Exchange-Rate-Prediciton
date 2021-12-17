package studyjson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import exchangerateclass.ExchangeRate;

import java.io.IOException;

public class CustomExchangeRateSerializer extends StdSerializer<ExchangeRate> {

    public CustomExchangeRateSerializer(){
        this(null);
    }

    public CustomExchangeRateSerializer(Class<ExchangeRate> e){
        super(e);
    }

    @Override
    public void serialize(ExchangeRate value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("currency", value.getCurrency());
        gen.writeObjectField("effectiveDate", value.getEffectiveDate());
        gen.writeFieldName("bid");
        gen.writeNumber(value.getBid());
        gen.writeEndObject();
    }
}
