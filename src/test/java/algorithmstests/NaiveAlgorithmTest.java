package algorithmstests;

import algorithms.Algorithm;
import algorithms.NaiveAlgorithmWithTrend;
import algorithms.NaiveAlgorithmWithTrendAndAverageIncrement;
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

public class NaiveAlgorithmTest {

    List<Point> dataPoints;

    @BeforeEach
    void setUp(){
        dataPoints = DataGenerator.getInstance().generateDataWithTrend(10, BigDecimal.ONE, new BigDecimal(".2"));
    }

    @Test
    void shouldTestForNaiveModelWithTrend(){

        Algorithm naiveAlgorithmWithTrend =
                new NaiveAlgorithmWithTrend();

        // We start off with some later date because the 1st values are not computed correctly
        LocalDate sD = dataPoints.get(2).getX(),
                eD = LocalDate.now().minusDays(1);

        List<Point> list = naiveAlgorithmWithTrend.forecastValuesForDates(dataPoints, sD, eD);
        Iterator<Point> itr = new AscendingIterator(dataPoints, sD, eD);

        Assertions.assertAll(
                list.stream()
                        // At this point of testing, we only test whether an algorithm computes values correctly
                        // We don't test a real data we would receive normally
                        .map(x -> (() -> Assertions.assertEquals(itr.next().getY().doubleValue(), x.getY().doubleValue(), .1))));
    }

    @Test
    void shouldTestForNaiveModelWithTrendAndAverageIncrement(){

        Algorithm naiveAlgorithmWithTrendAndAverageIncrement =
                new NaiveAlgorithmWithTrendAndAverageIncrement();

        // We start off with some later date because the 1st values are not computed correctly
        LocalDate sD = dataPoints.get(3).getX(),
                eD = LocalDate.now().minusDays(1);

        List<Point> list = naiveAlgorithmWithTrendAndAverageIncrement.forecastValuesForDates(dataPoints, sD, eD);
        Iterator<Point> itr = new AscendingIterator(dataPoints, sD, eD);

        Assertions.assertAll(
                list.stream()
                        // At this point of testing, we only test whether an algorithm computes values correctly
                        // We don't test a real data we would receive normally
                        .map(x -> (() -> Assertions.assertEquals(itr.next().getY().doubleValue(), x.getY().doubleValue(), .5))));



    }


}
