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
        data = dG.generateDataWithTrend(dataset, BigDecimal.ONE, new BigDecimal(".2"));
    }

    @Test
    void shouldMoveOneStepBackwards(){

        LocalDate now = LocalDate.now();
        // Start with index 3
        itr = new BiDirectionalIterator(data, now.minusDays(1));
        if (itr.hasNext()) itr.next();    // t
        Assertions.assertEquals(data.get(2), itr.next());

    }

    @Test
    void shouldMoveTwoStepBackwards(){

        LocalDate now = LocalDate.now();
        // Start with 3rd index (t)
        itr = new BiDirectionalIterator(data, now.minusDays(1));
        if (itr.hasNext()) itr.next();    // t
        itr.next();    // t - 1
        Assertions.assertEquals(data.get(1), itr.next());

    }

    @Test
    void shouldMoveForward(){

        LocalDate now = LocalDate.now();
        // The latest point generated has always today's date
        itr = new BiDirectionalIterator(data, now.minusDays(1));
        itr.next();   // t
        itr.next();    // t - 1
        Point<LocalDate> point = null;
        if (itr.hasNext()) point = itr.next();
        // Initially the starting index is behind the point we wanted to start with
        Assertions.assertEquals(data.get(dataset - 2), point);

    }

    @Test
    void shouldNtMoveForward(){

        LocalDate now = LocalDate.now();
        itr = new BiDirectionalIterator(data, now);
        if (itr.hasNext()) itr.next();   // t : shifts the internal index to the position we want to start with
        itr.next();    // t - 1
        // Should return false since there's no more data
        Assertions.assertFalse(itr.hasNext());

    }

}
