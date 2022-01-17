package datageneratortest;

import datagenerator.DataGenerator;
import datasciencealgorithms.utils.point.Point;
import iterators.AscendingIterator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

public class CustomIteratorTest {

    Iterator<Point> itr;
    List<Point> data;
    int dataset = 7;

    @BeforeEach
    void setUp(){
        data = DataGenerator.getInstance().generateDataWithTrend(dataset, BigDecimal.ONE,new BigDecimal(".2"));
    }

    @Test
    void shouldMoveToNextElementAscendingIterator(){

        itr = new AscendingIterator(data, LocalDate.now().minusDays(7));
        Point nextPoint = itr.next();
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



}
