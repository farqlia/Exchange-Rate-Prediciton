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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class NaiveAlgorithmTest {

    List<Point> dataPoints;
    int dataset;

    @BeforeEach
    void setUp(){
        dataset = 20;
        dataPoints = DataGenerator.getInstance().generateDataWithTrend(dataset, BigDecimal.ONE, new BigDecimal(".2"));
    }

    @Test
    void shouldTestForNaiveModelWithTrend() throws InterruptedException {

        BlockingQueue<Point> queue = new ArrayBlockingQueue<>(dataset);
        Algorithm naiveAlgorithmWithTrend =
                new NaiveAlgorithmWithTrend(queue);

        // We start off with some later date because the 1st values are not computed correctly
        LocalDate sD = dataPoints.get(2).getX(),
                eD = LocalDate.now().minusDays(1);

        naiveAlgorithmWithTrend.forecastValuesForDates(dataPoints, sD, eD);
        Iterator<Point> itr = new AscendingIterator(dataPoints, sD, eD);

        Assertions.assertAll(
                queue.stream()
                        .filter(x -> !x.equals(Point.EMPTY_POINT))
                        // At this point of testing, we only test whether an algorithm computes values correctly
                        // We don't test a real data we would receive normally
                        .map(x -> (() -> Assertions.assertEquals(itr.next().getY().doubleValue(), x.getY().doubleValue(), .1))));
    }

    @Test
    void shouldTestForNaiveModelWithTrendAndAverageIncrement() throws InterruptedException {

        BlockingQueue<Point> queue = new ArrayBlockingQueue<>(dataset);
        Algorithm naiveAlgorithmWithTrendAndAverageIncrement =
                new NaiveAlgorithmWithTrendAndAverageIncrement(queue, 2);

        // We start off with some later date because the 1st values are not computed correctly
        LocalDate sD = LocalDate.now().minusDays(10),
                eD = LocalDate.now().minusDays(1);

        naiveAlgorithmWithTrendAndAverageIncrement.forecastValuesForDates(dataPoints, sD, eD);
        Iterator<Point> itr = new AscendingIterator(dataPoints, sD, eD);

        Assertions.assertAll(
                queue.stream()
                        .filter(x -> !x.equals(Point.EMPTY_POINT))
                        // At this point of testing, we only test whether an algorithm computes values correctly
                        // We don't test a real data we would receive normally
                        .map(x -> (() -> Assertions.assertEquals(itr.next().getY().doubleValue(), x.getY().doubleValue(), .5))));



    }


}
