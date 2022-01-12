package sciencelibrarytests;

import algorithms.datacleaning.DataCleanser;
import datagenerator.DataGenerator;
import datasciencealgorithms.utils.point.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class DataCleanerTest {

    DataCleanser dataCleanser;
    List<Point> dataToClean;

    @BeforeEach
    void setUp(){
        dataCleanser = new DataCleanser();
        dataToClean = DataGenerator.getInstance().generateDataWithTrend(
                10, BigDecimal.ONE, BigDecimal.ONE);
    }

    @Test
    void shouldAddNonExistingValuesCopiedFromPrevious(){

        LocalDate d1 = dataToClean.remove(1).getX();
        // Indexes are shifted after 1st deletion so here
        // we really delete te 5th element
        LocalDate d2 = dataToClean.remove(4).getX();

        // This should add missing data - in this case, missing dates since
        // we want to keep a period T consistent
        dataCleanser.performCleaning(dataToClean);

        Assertions.assertAll(
                () -> Assertions.assertEquals(d1, dataToClean.get(1).getX()),
                // Y value should be copied from previous, non-missing point
                () -> Assertions.assertEquals(dataToClean.get(0).getY(), dataToClean.get(1).getY()),
                () -> Assertions.assertEquals(d2, dataToClean.get(5).getX()),
                () -> Assertions.assertEquals(dataToClean.get(4).getY(), dataToClean.get(5).getY()),
                () -> Assertions.assertEquals(10, dataToClean.size())
        );

    }

    @Test
    void shouldPassMoreRigorousTest(){

        // Remove two elements in row
        LocalDate d1 = dataToClean.remove(1).getX();
        LocalDate d2 = dataToClean.remove(1).getX();

        dataCleanser.performCleaning(dataToClean);

        Assertions.assertAll(
                () -> Assertions.assertEquals(d1, dataToClean.get(1).getX()),
                // Y value should be copied from previous, non-missing point
                () -> Assertions.assertEquals(dataToClean.get(0).getY(), dataToClean.get(1).getY()),
                () -> Assertions.assertEquals(d2, dataToClean.get(2).getX()),
                // Between indexes 0 - 2 we had a gap
                () -> Assertions.assertEquals(dataToClean.get(0).getY(), dataToClean.get(2).getY()),
                () -> Assertions.assertEquals(10, dataToClean.size())
        );
    }
}
