package tofilesavertest;

import dataconverter.writersandreaders.CustomFileWriter;
import dataconverter.writersandreaders.FileHandler;
import dataconverter.writersandreaders.TextFileWriter;
import datagenerator.DataGenerator;
import dataconverter.Parser;
import dataconverter.formatters.PointToCSV;
import datasciencealgorithms.utils.point.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import java.util.List;

public class TextFileWriterTest {

    CustomFileWriter<Point> dataConverter;
    FileHandler handler;
    List<Point> testData;
    String DELIMITER = "\\";
    String dir = "src" + DELIMITER + "test" + DELIMITER + "resources" + DELIMITER;
    String name;
    String extension = "txt";
    String fileName;


    @BeforeEach
    void setUp(){
        dataConverter = new TextFileWriter<>(new PointToCSV());
        name = "testfile" + (int)(Math.random() * 10);
        fileName = dir + name + "." + extension;
        handler = new FileHandler(extension);
        testData = DataGenerator.getInstance()
                .generateDataWithTrend(10, BigDecimal.ONE, new BigDecimal(".2"));
    }

    @Test
    void shouldNtParseFileName(){
        Exception e = Assertions.assertThrows(IOException.class,
            () -> handler.parseFilePath("somefile"));
        Assertions.assertEquals(e.getMessage(), "Incorrect File Name");
    }

    @Test
    void shouldParseWithTheCorrectExtension() {
        Assertions.assertDoesNotThrow(() -> handler.parseFilePath(fileName));

    }

    @Test
    void shouldNtParseWithTheWrongExtension(){
        String path = "some" + DELIMITER + "random.pdf";

        Exception e = Assertions.assertThrows(IOException.class,
                () -> handler.parseFilePath(path));
        Assertions.assertEquals(e.getMessage(), "Unsupported Extension; Supported Extensions are: txt");
    }

    @Test
    void shouldCreateDirectory() {
        // It should built the path and create all necessary directories and files
        Assertions.assertDoesNotThrow(() -> dataConverter.saveToFile(fileName, testData));
        Assertions.assertDoesNotThrow(() -> handler.parseFilePath(fileName));
        File file = new File(handler.getProperty(Parser.Entries.DIRECTORY));
        Assertions.assertTrue(file.isDirectory());

    }

    @Test
    void shouldCreateFile() throws IOException {
        // It should built the path and create all necessary directories and files
        dataConverter.saveToFile(fileName, testData);
        handler.parseFilePath(fileName);
        File file = new File(handler.getProperty(Parser.Entries.FILEPATH));
        Assertions.assertTrue(file.exists());
    }

    @Test
    void shouldSaveData(){
        Assertions.assertDoesNotThrow(
                () -> dataConverter.saveToFile(fileName, testData));
    }


}
