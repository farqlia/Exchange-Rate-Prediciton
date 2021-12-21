package tofilesavertest;

import datagenerator.DataGenerator;
import datasciencealgorithms.utils.Point;
import iterators.AscendingIterator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

public class DataConverterTest {

    DataConverter dataConverter;
    List<Point<LocalDate>> testData;

    @BeforeEach
    void setUp(){
        dataConverter = new DataConverter("src/test/resources");
        testData = new DataGenerator()
                .generateDataWithTrend(10, BigDecimal.ONE, new BigDecimal(".2"));
    }

    @Test
    void shouldCreateDirectory(){
        // It should built the path and create all necessary directories and files
        dataConverter.saveToFile("testfile", testData);

        File file = new File(dataConverter.getDirectoryPath());
        Assertions.assertTrue(file.isDirectory());

    }

    @Test
    void shouldCreateFile(){
        // It should built the path and create all necessary directories and files
        dataConverter.saveToFile("testfile", testData);

        File file = new File(dataConverter.getLatestFilePath());
        Assertions.assertTrue(file.exists());
    }

    @Test
    void shouldSaveDataWithDefaultDelimiterAndNoHeaders(){
        boolean isSaved = dataConverter.saveToFile("testfile", testData);

        Assertions.assertTrue(isSaved);
    }

    @Test
    void shouldSaveDataWithDefaultDelimiterAndHeaders(){
        String[] headers = {"Header1", "Header2"};
        boolean isSaved = dataConverter.saveToFile("testfile2", testData, headers);

        Assertions.assertTrue(isSaved);
    }

    @Test
    void shouldReadDataWithDefaultDelimiterAndNoHeaders(){
        List<Point<LocalDate>> readData = dataConverter.readFromFile("testfile");

        Assertions.assertNotNull(readData);
    }

    @Test
    void shouldCorrectlyReadDataWithDefaultDelimiterAndNoHeaders(){
        List<Point<LocalDate>> readData = dataConverter.readFromFile("testfile");

        Iterator<Point<LocalDate>> itr = new AscendingIterator(testData);
        Assertions.assertAll(
                readData.stream().map(x -> (() -> Assertions.assertEquals(itr.next(), x))));
    }

}
