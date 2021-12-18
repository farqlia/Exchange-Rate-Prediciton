package datageneratortest;

import datagenerator.DataGenerator;
import datasciencealgorithms.utils.Point;
import iterators.BiDirectionalIterator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

public class BiDirectionalIteratorTest {

    Iterator<Point<LocalDate>> itr;
    List<Point<LocalDate>> data;
    int dataset = 5;

    @BeforeEach
    void setUp(){
        DataGenerator dG = new DataGenerator();
        data = dG.generateDataWithTrend(dataset, new BigDecimal(".2"));
    }

    @Test
    void shouldMoveOneStepBackwards(){

        LocalDate now = LocalDate.now();
        itr = new BiDirectionalIterator(data, now.minusDays(1), 2);
        itr.next();    // t
        Assertions.assertEquals(data.get(2), itr.next());

    }

    @Test
    void shouldMoveTwoStepBackwards(){

        LocalDate now = LocalDate.now();
        itr = new BiDirectionalIterator(data, now.minusDays(1), 2);
        itr.next();    // t
        itr.next();    // t - 1
        Assertions.assertEquals(data.get(1), itr.next());

    }

    @Test
    void shouldMoveForward(){

        LocalDate now = LocalDate.now();
        // The latest point generated has always today's date
        itr = new BiDirectionalIterator(data, now.minusDays(1), 1);
        itr.next();   // t
        itr.next();    // t - 1
        // After the two moves, it should change the direction and proceed towards
        // the point with date t + 1
        Assertions.assertEquals(data.get(dataset - 1), itr.next());

    }

    @Test
    void shouldNtMoveForward(){

        LocalDate now = LocalDate.now();
        itr = new BiDirectionalIterator(data, now, 1);
        itr.next();   // t
        itr.next();    // t - 1
        // Should return null since there's no more data
        Assertions.assertFalse(itr.hasNext());

    }

}
