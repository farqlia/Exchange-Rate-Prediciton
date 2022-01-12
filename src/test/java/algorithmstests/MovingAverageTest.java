package algorithmstests;

import algorithms.*;
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

public class MovingAverageTest {

    List<Point> dataPoints;

    @BeforeEach
    void setUp(){
        dataPoints = DataGenerator.getInstance().generateDataWithTrend(10, BigDecimal.ONE, new BigDecimal(".2"));
    }


    @Test
    void shouldTestForMovingAverageMean(){

        // Initial value for k
        Algorithm movingAverageMean =
                new MovingAverageMeanAlgorithm();

        int dataset = 30;
        dataPoints = DataGenerator.getInstance().generateDataWithTrend(dataset,
                BigDecimal.ONE, new BigDecimal(".05"));

        LocalDate sD = LocalDate.now().minusDays(20);
        LocalDate eD = LocalDate.now();

        List<Point> predicted = movingAverageMean.forecastValuesForDates(dataPoints, sD, eD);

        Assertions.assertFalse(predicted.isEmpty());
        Iterator<Point> itr = new AscendingIterator(dataPoints, sD, eD);
        Assertions.assertAll(
                predicted.stream()
                        .map(x -> (() -> Assertions.assertEquals(itr.next().getY().doubleValue(), x.getY().doubleValue(), .5))));

    }

    @Test
    void shouldTestForLinearlyWeightedAverageMean(){

        Algorithm linearlyWeightedMovingAverage =
                new LinearlyWeightedMovingAverage();

        int dataset = 30;
        dataPoints = DataGenerator.getInstance().generateDataWithTrend(dataset,
                BigDecimal.ONE, new BigDecimal(".05"));

        LocalDate sD = LocalDate.now().minusDays(20);
        LocalDate eD = LocalDate.now();

        List<Point> predicted = linearlyWeightedMovingAverage.forecastValuesForDates(dataPoints, sD, eD);

        Assertions.assertFalse(predicted.isEmpty());
        Iterator<Point> itr = new AscendingIterator(dataPoints, sD, eD);
        Assertions.assertAll(
                predicted.stream()
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
