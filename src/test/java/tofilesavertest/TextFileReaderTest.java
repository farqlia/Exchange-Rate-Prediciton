package tofilesavertest;

import dataconverter.writersandreaders.CustomFileReader;
import dataconverter.writersandreaders.TextFileReader;
import dataconverter.writersandreaders.TextFileWriter;
import datagenerator.DataGenerator;
import dataconverter.writersandreaders.PointToCSV;
import datasciencealgorithms.utils.point.Point;
import iterators.AscendingIterator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

public class TextFileReaderTest {

    CustomFileReader<Point> dataConverter;
    List<Point> testData;
    String DELIMITER = "\\";
    String dir = "src" + DELIMITER + "test" + DELIMITER + "resources" + DELIMITER;
    String name = "testfiledonotdelete";
    String extension = "txt";
    String fileName = dir + name + "." + extension;

    @BeforeEach
    void setUp(){
        dataConverter = new TextFileReader<>(new PointToCSV());
        testData = DataGenerator.getInstance()
                .generateDataWithTrend(10, BigDecimal.ONE, new BigDecimal(".2"));
    }

    @Test
    void shouldParseLine(){
        String line = "01-12-2002,1.50000";
        Matcher m = PointToCSV.objectPattern.matcher(line);
        Assertions.assertTrue(m.matches());
        Assertions.assertEquals("01-12-2002", m.group(1));
        Assertions.assertEquals("1.50000", m.group(2));
    }

    @Test
    void shouldNtParseLine() {
        String line = "01-12-02,1.50000";
        Matcher m = PointToCSV.objectPattern.matcher(line);
        Assertions.assertFalse(m.matches());
    }

    @Test
    void shouldNtParseLine2() {
        String line = "01-12-2002,adsf";
        Matcher m = PointToCSV.objectPattern.matcher(line);
        Assertions.assertFalse(m.matches());
    }

    @Test
    void shouldReadData(){
        Assertions.assertDoesNotThrow(() -> dataConverter.readFromFile(fileName));
    }

    @Test
    void shouldNtReadDataFromNonExistingFile(){
        Exception e = Assertions.assertThrows(IOException.class,
                () -> dataConverter.readFromFile("dir" + DELIMITER + "somefile.txt"));
        Assertions.assertEquals("File doesn't exist or is empty", e.getMessage());
    }

    @Test
    void shouldNtReadIncorrectlyFormattedData(){
        String badFilePath = dir + "badfile." + extension;
        Exception e = Assertions.assertThrows(IOException.class,
                () -> dataConverter.readFromFile(badFilePath));
        Assertions.assertEquals("Data is incorrectly formatted", e.getMessage());
    }

    @Test
    void shouldCorrectlyReadData() throws IOException {
        new TextFileWriter<>(new PointToCSV()).saveToFile(fileName,
                testData);
        List<Point> readData = dataConverter.readFromFile(fileName);

        Iterator<Point> itr = new AscendingIterator(testData);
        Assertions.assertAll(
                readData.stream().map(x -> (() -> Assertions.assertEquals(itr.next(), x))));
    }

}
