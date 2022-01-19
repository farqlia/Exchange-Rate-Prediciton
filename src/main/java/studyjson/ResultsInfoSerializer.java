package studyjson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import model.ResultsTableModel;

import java.io.IOException;

public class ResultsInfoSerializer extends StdSerializer<ResultsInfo> {

    public ResultsInfoSerializer(){
        this(null);
    }

    public ResultsInfoSerializer(Class<ResultsInfo> t){
        super(t);
    }

    @Override
    public void serialize(ResultsInfo value, JsonGenerator gen,
                          SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("algorithm", value.algorithmName);
        gen.writeStringField("code", value.code);
        gen.writeFieldName("Predictions");
        gen.writeStartArray();
        for (ResultsTableModel.Row r : value.rows){
            gen.writeStartObject();
            gen.writeStringField("Date", r.getDate().toString());
            gen.writeFieldName("Real");
            gen.writeNumber(r.getReal());
            gen.writeFieldName("Predicted");
            gen.writeNumber(r.getPredicted());
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
}
