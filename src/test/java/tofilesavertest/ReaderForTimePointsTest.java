package tofilesavertest;

import dataconverter.CustomFileReader;
import dataconverter.IncorrectDataFormat;
import dataconverter.ReaderForTimePoints;
import datagenerator.DataGenerator;
import datasciencealgorithms.utils.Point;
import iterators.AscendingIterator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

public class ReaderForTimePointsTest {

    CustomFileReader<Point<LocalDate>> dataConverter;
    List<Point<LocalDate>> testData;
    String DELIMITER = "\\";
    String dir = "src" + DELIMITER + "test" + DELIMITER + "resources" + DELIMITER;
    String name = "testfiledonotdelete";
    String extension = "txt";
    String fileName = dir + name + "." + extension;

    @BeforeEach
    void setUp(){
        dataConverter = new ReaderForTimePoints();
        testData = new DataGenerator()
                .generateDataWithTrend(10, BigDecimal.ONE, new BigDecimal(".2"));
    }

    @Test
    void shouldParseLine(){
        String line = "01-12-2002,1.50000";
        Matcher m = ReaderForTimePoints.objectPattern.matcher(line);
        Assertions.assertTrue(m.matches());
        Assertions.assertEquals("01-12-2002", m.group(1));
        Assertions.assertEquals("1.50000", m.group(2));
    }

    @Test
    void shouldNtParseLine() {
        String line = "01-12-02,1.50000";
        Matcher m = ReaderForTimePoints.objectPattern.matcher(line);
        Assertions.assertFalse(m.matches());
    }

    @Test
    void shouldNtParseLine2() {
        String line = "01-12-2002,adsf";
        Matcher m = ReaderForTimePoints.objectPattern.matcher(line);
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
        Exception e = Assertions.assertThrows(IncorrectDataFormat.class,
                () -> dataConverter.readFromFile(badFilePath));
        Assertions.assertEquals("Data is incorrectly formatted", e.getMessage());
    }

    @Test
    void shouldCorrectlyReadData() throws IOException, IncorrectDataFormat {
        List<Point<LocalDate>> readData = dataConverter.readFromFile(fileName);

        Iterator<Point<LocalDate>> itr = new AscendingIterator(testData);
        Assertions.assertAll(
                readData.stream().map(x -> (() -> Assertions.assertEquals(itr.next(), x))));
    }

}
