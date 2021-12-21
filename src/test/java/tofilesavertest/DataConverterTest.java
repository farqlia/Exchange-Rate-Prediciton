package tofilesavertest;

import dataconverter.DataConverter;
import dataconverter.TextDataConverterForTimePoints;
import datagenerator.DataGenerator;
import datasciencealgorithms.utils.Point;
import iterators.AscendingIterator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

public class DataConverterTest {
    DataConverter<Point<LocalDate>> dataConverter;
    List<Point<LocalDate>> testData;

    @BeforeEach
    void setUp(){
        dataConverter = new TextDataConverterForTimePoints();
        testData = new DataGenerator()
                .generateDataWithTrend(10, BigDecimal.ONE, new BigDecimal(".2"));
    }


    @Test
    void shouldParseDirectoryNames(){
        String dir = "some" + DELIMITER + "random" + DELIMITER + "dir" + DELIMITER;
        String name = "file";
        String extension = "txt";

        Assertions.assertTrue(dataConverter.parseFilePath(dir + name + "." + extension));
    }

    @Test
    void shouldNtParseFileName(){
        Exception e = Assertions.assertThrows(IOException.class,
            () -> dataConverter.parseFilePath("somefile"));
        Assertions.assertEquals(e.getMessage(), "Incorrect File Name");
    }

    @Test
    void shouldParseWithTheCorrectExtension(){
        String path = "some" + DELIMITER + "random.txt";
        Assertions.assertTrue(dataConverter.parseFilePath(path));
    }

    @Test
    void shouldNtParseWithTheWrongExtension(){
        String path = "some" + DELIMITER + "random.pdf";

        Exception e = Assertions.assertThrows(IOException.class,
                () -> dataConverter.parseFilePath(path));
        Assertions.assertEquals(e.getMessage(), "Unsupported Extension; Supported Extensions are: txt");
    }



    @Test
    void shouldCreateDirectory() throws IOException {
        // It should built the path and create all necessary directories and files
        Assertions.assertTrue(dataConverter.saveToFile("src/test/resources/testfile.txt", testData));
        File file = new File(dataConverter.getDirectoryPath());
        Assertions.assertTrue(file.isDirectory());

    }

    @Test
    void shouldCreateFile() throws IOException {
        // It should built the path and create all necessary directories and files
        dataConverter.saveToFile("src/test/resources/testfile.txt", testData);
        File file = new File(dataConverter.getCurrentFilePath());
        Assertions.assertTrue(file.exists());
    }
/*
    @Test
    void shouldSaveDataWithDefaultDelimiterAndNoHeaders() throws IOException {

        Assertions.assertDoesNotThrow(
                () -> dataConverter.saveToFile("src/test/resources/testfile.txt", testData));
    }


    @Test
    void shouldSaveDataWithDefaultDelimiterAndHeaders(){
        String[] headers = {"Header1", "Header2"};
        boolean isSaved = dataConverter.saveToFile("src/test/resources/testfile2.txt", testData, headers);

        Assertions.assertDoesNotThrow(
                () -> dataConverter.saveToFile("src/test/resources/testfile2.txt", testData, headers));
    }

    @Test
    void shouldReadDataWithDefaultDelimiterAndNoHeaders(){
        Assertions.assertDoesNotThrow(() -> dataConverter.readFromFile("src/test/resources/testfile.txt"));
    }

    @Test
    void shouldCorrectlyReadDataWithDefaultDelimiterAndNoHeaders() throws IOException {
        List<Point<LocalDate>> readData = dataConverter.readFromFile("src/test/resources/testfile.txt");

        Iterator<Point<LocalDate>> itr = new AscendingIterator(testData);
        Assertions.assertAll(
                readData.stream().map(x -> (() -> Assertions.assertEquals(itr.next(), x))));
    }

     */

}
