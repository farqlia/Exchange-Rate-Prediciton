package algorithmstests;

import algorithms.*;
import dataconverter.formatters.PointToCSV;
import dataconverter.writersandreaders.TextFileReader;
import datagenerator.DataGenerator;
import datasciencealgorithms.utils.point.Point;
import iterators.AscendingIterator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class MovingAverageTest {

    List<Point> dataPoints;
    LocalDate sD = LocalDate.now().minusDays(20);
    LocalDate eD = LocalDate.now();
    @BeforeEach
    void setUp(){
        dataPoints = DataGenerator.getInstance().generateDataWithTrend(10, BigDecimal.ONE, new BigDecimal(".2"));
    }


    @Test
    void shouldTestForMovingAverageMean() throws InterruptedException {

        int dataset = 40;
        BlockingQueue<Point> queue = new ArrayBlockingQueue<>(dataset);
        // Initial value for k
        Algorithm movingAverageMean =
                new MovingAverageMeanAlgorithm(queue, 5);

        dataPoints = DataGenerator.getInstance().generateDataWithTrend(dataset,
                BigDecimal.ONE, new BigDecimal(".05"), 1);

        movingAverageMean.forecastValuesForDates(dataPoints, sD, eD);

        Assertions.assertFalse(queue.isEmpty());
        Iterator<Point> itr = new AscendingIterator(dataPoints, sD, eD);

        Assertions.assertAll(
                queue.stream()
                        .filter(x -> !x.equals(Point.EMPTY_POINT))
                        .map(x -> (() -> Assertions.assertEquals(itr.next().getY().doubleValue(),
                                x.getY().doubleValue(), 2))));



    }

    @Test
    void shouldTestForMovingAverageMeanWithRealData() throws IOException, InterruptedException {
        // Initial value for k

        int dataset = 30;
        BlockingQueue<Point> queue = new ArrayBlockingQueue<>(dataset);
        Algorithm movingAverageMean =
                new MovingAverageMeanAlgorithm(queue, 5);

        dataPoints = new TextFileReader<>(new PointToCSV()).readFromFile("exampledata\\dp2.txt");

        movingAverageMean.forecastValuesForDates(dataPoints, sD, eD);

        Assertions.assertFalse(queue.isEmpty());
        Iterator<Point> itr = new AscendingIterator(dataPoints, sD, eD);

        Assertions.assertAll(
                queue.stream()
                        .filter(x -> !x.equals(Point.EMPTY_POINT))
                        .map(x -> (() -> Assertions.assertEquals(itr.next().getY().doubleValue(), x.getY().doubleValue(), .5))));


    }

    @Test
    void shouldTestForLinearlyWeightedAverageMean() throws IOException, InterruptedException {

        int dataset = 30;
        BlockingQueue<Point> queue = new ArrayBlockingQueue<>(dataset);

        Algorithm linearlyWeightedMovingAverage =
                new LinearlyWeightedMovingAverage(queue, 5);

        dataPoints = new TextFileReader<>(new PointToCSV()).readFromFile("exampledata\\dp2.txt");

        linearlyWeightedMovingAverage.forecastValuesForDates(dataPoints, sD, eD);

        Assertions.assertFalse(queue.isEmpty());
        Iterator<Point> itr = new AscendingIterator(dataPoints, sD, eD);
        Assertions.assertAll(
                queue.stream()
                        .filter(x -> !x.equals(Point.EMPTY_POINT))
                        .map(x -> (() -> Assertions.assertEquals(itr.next().getY().doubleValue(), x.getY().doubleValue(), .5))));

    }

    @Test
    void shouldReturnSumOfArithmeticSequence(){

        int period = 10;
        LinearlyWeightedMovingAverage.Weight weight = new LinearlyWeightedMovingAverage.Weight(period);

        for (int i = period; i > 0; i--){
            Assertions.assertEquals(new BigDecimal(i), weight.nextWeight());
        }

        Assertions.assertEquals(new BigDecimal(55), weight.sumOfWeights());

    }

    @Test
    void shouldTestBrownExponentialSmoothingModel() throws InterruptedException {
        BlockingQueue<Point> queue = new ArrayBlockingQueue<>(20);
        BigDecimal a = new BigDecimal("0.7");
        Algorithm algorithm = new BrownExponentialSmoothingModel(queue, a);

        dataPoints = DataGenerator.getInstance().generateDataWithTrend(10, BigDecimal.ONE,
                BigDecimal.ONE);
        sD = LocalDate.now().minusDays(10);
        algorithm.forecastValuesForDates(dataPoints, sD, eD);

        Point firstPoint = queue.take();
        Assertions.assertAll(
                () -> Assertions.assertTrue(new BigDecimal("2").compareTo(firstPoint.getY()) == 0),
                () -> Assertions.assertEquals(dataPoints.get(0).getX(), firstPoint.getX()),
                () -> Assertions.assertTrue(BigDecimal.ONE.multiply(a).add(BigDecimal.ONE.subtract(a).multiply(firstPoint.getY()))
                                .compareTo(queue.take().getY()) == 0),
                () -> Assertions.assertEquals(9, queue.size())      // Empty point also counts
        );


    }

    @Test
    void shouldTestHoltExponentialSmoothingModel() throws InterruptedException{

        BlockingQueue<Point> queue = new ArrayBlockingQueue<>(21);
        BigDecimal a = new BigDecimal("0.4");
        BigDecimal b = new BigDecimal("0.4");
        Algorithm algorithm = new HoltExponentialSmoothingModel(queue, a, b);

        dataPoints = DataGenerator.getInstance().generateDataWithTrend(20, BigDecimal.ONE,
                BigDecimal.ONE);
        sD = LocalDate.now().minusDays(18);
        algorithm.forecastValuesForDates(dataPoints, sD, eD);

        // s1 = y2 - y1 -> 2 - 1 = 1, 0.4 * 2 + (0.6) (1 + 1) = 0.8 + 1.2 = 2
        Assertions.assertAll(
                () -> Assertions.assertTrue(new BigDecimal("2").compareTo(queue.take().getY()) == 0)
        );
    }

}
