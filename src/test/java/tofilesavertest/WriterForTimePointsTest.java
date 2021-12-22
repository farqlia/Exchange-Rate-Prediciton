package tofilesavertest;

import dataconverter.CustomFileWriter;
import dataconverter.WriterForTimePoints;
import datagenerator.DataGenerator;
import datasciencealgorithms.utils.Parser;
import datasciencealgorithms.utils.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.List;

public class WriterForTimePointsTest {

    CustomFileWriter<Point<LocalDate>> dataConverter;
    List<Point<LocalDate>> testData;
    String DELIMITER = "\\";
    String dir = "src" + DELIMITER + "test" + DELIMITER + "resources" + DELIMITER;
    String name = "testfile";
    String extension = "txt";
    String fileName = dir + name + "." + extension;


    @BeforeEach
    void setUp(){
        dataConverter = new WriterForTimePoints();
        testData = new DataGenerator()
                .generateDataWithTrend(10, BigDecimal.ONE, new BigDecimal(".2"));
    }

    @Test
    void shouldNtParseFileName(){
        Exception e = Assertions.assertThrows(IOException.class,
            () -> dataConverter.parseFilePath("somefile"));
        Assertions.assertEquals(e.getMessage(), "Incorrect File Name");
    }

    @Test
    void shouldParseWithTheCorrectExtension() {
        Assertions.assertDoesNotThrow(() -> dataConverter.parseFilePath(fileName));

    }

    @Test
    void shouldNtParseWithTheWrongExtension(){
        String path = "some" + DELIMITER + "random.pdf";

        Exception e = Assertions.assertThrows(IOException.class,
                () -> dataConverter.parseFilePath(path));
        Assertions.assertEquals(e.getMessage(), "Unsupported Extension; Supported Extensions are: txt");
    }

    @Test
    void shouldCreateDirectory() {
        // It should built the path and create all necessary directories and files
        Assertions.assertDoesNotThrow(() -> dataConverter.saveToFile(fileName, testData));
        File file = new File(dataConverter.getProperty(Parser.Entries.DIRECTORY));
        Assertions.assertTrue(file.isDirectory());

    }

    @Test
    void shouldCreateFile() throws IOException {
        // It should built the path and create all necessary directories and files
        dataConverter.saveToFile(fileName, testData);
        File file = new File(dataConverter.getProperty(Parser.Entries.FILEPATH));
        Assertions.assertTrue(file.exists());
    }

    @Test
    void shouldSaveData(){
        Assertions.assertDoesNotThrow(
                () -> dataConverter.saveToFile(fileName, testData));
    }


}
