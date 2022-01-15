package algorithmstests;

import algorithms.*;
import dataconverter.writersandreaders.PointToCSV;
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

        LocalDate sD = LocalDate.now().minusDays(20);
        LocalDate eD = LocalDate.now();

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

        LocalDate sD = LocalDate.now().minusDays(20);
        LocalDate eD = LocalDate.now();

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

        LocalDate sD = LocalDate.now().minusDays(20);
        LocalDate eD = LocalDate.now();

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

}
