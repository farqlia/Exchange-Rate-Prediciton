package datageneratortest;

import datagenerator.DataGenerator;
import datasciencealgorithms.utils.Point;
import iterators.AscendingIterator;
import iterators.DescendingIterator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

public class CustomIteratorTest {

    Iterator<Point<LocalDate>> itr;
    List<Point<LocalDate>> data;
    int dataset = 7;

    @BeforeEach
    void setUp(){
        DataGenerator dG = new DataGenerator();
        data = dG.generateDataWithTrend(dataset, new BigDecimal(".2"));
    }

    @Test
    void shouldMoveToNextElementAscendingIterator(){

        itr = new AscendingIterator(data, LocalDate.now().minusDays(7));
        Point<LocalDate> nextPoint = itr.next();
        Assertions.assertEquals(data.get(0).getX(), nextPoint.getX());

    }

    @Test
    void shouldReturnNotNullWhenOutOfListAscendingIterator(){

        itr = new AscendingIterator(data, LocalDate.now().plusDays(1));
        Assertions.assertEquals(itr.next(), data.get(dataset - 1));

    }

    @Test
    void shouldNtMoveToNextElementAscendingIterator(){

        itr = new AscendingIterator(data, LocalDate.now().minusDays(dataset), LocalDate.now().minusDays(dataset - 1));
        itr.next();  // returns the next element
        itr.next();
        Assertions.assertFalse(itr.hasNext());

    }

    @Test
    void shouldReturnNotNullWhenOutOfListDescendingIterator(){

        itr = new DescendingIterator(data, LocalDate.now().minusDays(dataset + 1));
        Assertions.assertEquals(itr.next(), data.get(0));

    }

    @Test
    void shouldNtHaveNextElementDescendingIterator(){

        itr = new DescendingIterator(data, LocalDate.now().minusDays(dataset));
        itr.next();
        Assertions.assertFalse(itr.hasNext());

    }

    @Test
    void shouldNtMoveToNextElementDescendingIterator(){

        itr = new DescendingIterator(data, LocalDate.now().minusDays(7), LocalDate.now().minusDays(6));
        itr.next();  // returns the next element
        itr.next();
        Assertions.assertFalse(itr.hasNext());

    }

}
