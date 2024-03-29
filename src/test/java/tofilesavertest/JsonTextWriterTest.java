package tofilesavertest;

import algorithms.algorithmsinitializer.AlgorithmInitializerExPost;
import dataconverter.writersandreaders.CustomFileWriter;
import dataconverter.writersandreaders.JsonFileWriter;
import datasciencealgorithms.utils.point.Point;
import model.ResultsTableModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import studyjson.ResultsInfo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

public class JsonTextWriterTest {

    CustomFileWriter<ResultsInfo> writer;
    @Test
    void shouldSaveToFile() {
        writer = new JsonFileWriter();
        Point p = new Point(LocalDate.now(), BigDecimal.ZERO);
        ResultsTableModel.Row row = new ResultsTableModel.Row(p, p, BigDecimal.ONE, BigDecimal.TEN);
        ResultsInfo info = new ResultsInfo(AlgorithmInitializerExPost.MAMA.toString(),
                "EUR",  Collections.singletonList(row));
        String fileName = "results\\file.json";
        Assertions.assertDoesNotThrow(
                (() -> writer.saveToFile(fileName, Collections.singletonList(info))));
    }

}
