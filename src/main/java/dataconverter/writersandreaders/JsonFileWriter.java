package dataconverter.writersandreaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import studyjson.ResultsInfo;
import studyjson.ResultsInfoSerializer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JsonFileWriter extends CustomFileWriter<ResultsInfo> {

    ObjectMapper mapper;
    public JsonFileWriter(){
        super("json");
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(ResultsInfo.class, new ResultsInfoSerializer());
        mapper.registerModule(module);
    }

    // Passed list is a singleton list
    @Override
    void handleObjectWriting(String path, List<? extends ResultsInfo> data) throws IOException {

        ResultsInfo info = data.get(0);
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(path))){
            mapper.writerWithDefaultPrettyPrinter().writeValue(bW, info);
        }
    }
}
