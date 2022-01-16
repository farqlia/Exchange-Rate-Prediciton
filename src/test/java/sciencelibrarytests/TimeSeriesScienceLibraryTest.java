package sciencelibrarytests;

import datasciencealgorithms.utils.UtilityMethods;
import mathlibraries.TimeSeriesScienceLibrary;
import datagenerator.DataGenerator;
import datasciencealgorithms.utils.point.Point;
import org.junit.jupiter.api.*;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TimeSeriesScienceLibraryTest {

    List<Point> data;
    List<Point> predicted;

    @BeforeEach
    void setUp(){
        data = DataGenerator.getInstance()
                .generateDataWithTrend(11, BigDecimal.ONE, BigDecimal.ONE);

        predicted = DataGenerator.getInstance().generateDataWithTrend(10,
                BigDecimal.ONE, BigDecimal.ONE, 0.5);
    }

    @Test
    void shouldCorrectlyEstimateMeanAbsoluteError(){

        BigDecimal MAE = TimeSeriesScienceLibrary.calculateMeanAbsoluteErrorK(predicted, data,5);
        System.out.println("MAE= " + MAE);
        Assertions.assertTrue(MAE.compareTo(new BigDecimal("3")) < 0);
    }

    @Test
    void shouldReturn0AsMeanAbsoluteError2(){

        BigDecimal MAE = TimeSeriesScienceLibrary.calculateMeanAbsoluteErrorK(data, data,5);
        System.out.println("MAE= " + MAE);
        Assertions.assertTrue(MAE.compareTo(BigDecimal.ZERO) == 0);
    }

    @Test
    void shouldCorrectlyEstimateRMSE(){

        BigDecimal RMSE = TimeSeriesScienceLibrary.calculateRootMeanSquareErrorK(predicted, data, 5);
        System.out.println("RMSE= " + RMSE);
        Assertions.assertTrue(RMSE.compareTo(new BigDecimal("3")) < 0);

    }

    @Test
    void shouldCorrectlyEstimateRMSPE(){

        BigDecimal RMSPE = TimeSeriesScienceLibrary.calculateRootMeanSquarePercentageErrorK(predicted, data, 5);
        System.out.println("RMSPE= " + RMSPE);
        Assertions.assertTrue(RMSPE.compareTo(new BigDecimal("1")) < 0);

    }

    @Test
    void shouldCalculateMeanAbsolutePercentageError(){


        BigDecimal MAPE = TimeSeriesScienceLibrary.calculateMeanAbsolutePercentageErrorK(predicted, data, 5);
        System.out.println("MAPE= " + MAPE);
        Assertions.assertTrue(MAPE.compareTo(BigDecimal.ONE) < 0);

    }

    @Test
    void shouldCalculateAverageOfThreePoints(){

        List<Point> dataPoints = DataGenerator.getInstance().generateDataWithTrend(3, BigDecimal.ONE, BigDecimal.ONE);

        BigDecimal average = TimeSeriesScienceLibrary.calculateAverage(dataPoints.get(0), dataPoints.get(1),
                dataPoints.get(2));
        Assertions.assertEquals(new BigDecimal("2.000000"), average);

    }

    // Important note: DynamicTests are executed differently and therefore @BeforeEach and @AfterEach
    // will not be called
    @TestFactory
    Stream<DynamicTest> shouldReturnArrayOfBigDecimalsAsAbsoluteError(){

        data = DataGenerator.getInstance()
                .generateDataWithTrend(20, BigDecimal.ONE, BigDecimal.ONE);

        predicted = DataGenerator.getInstance().generateDataWithTrend(10,
                BigDecimal.ONE, BigDecimal.ONE, 0.5);

        BigDecimal[] errors = TimeSeriesScienceLibrary
                .calculateAbsoluteError(data, predicted);

        int dataStartIndex = UtilityMethods.findIndexOfDate(predicted.get(0).getX(), data);

        return IntStream.rangeClosed(0, errors.length - 1)
                .mapToObj(i -> DynamicTest.dynamicTest("Resolving: " + i,
                        () -> {BigDecimal diff = data.get(dataStartIndex + i).getY().subtract(predicted.get(i).getY());
                    Assertions.assertEquals(diff, errors[i]);
                        }));
    }

    @TestFactory
    Stream<DynamicTest> shouldReturnArrayOfBigDecimalsAsPercentageError(){

        data = DataGenerator.getInstance()
                .generateDataWithTrend(20, BigDecimal.ONE, BigDecimal.ONE);

        predicted = DataGenerator.getInstance().generateDataWithTrend(10,
                BigDecimal.ONE, BigDecimal.ONE, 0.5);

        BigDecimal[] errors = TimeSeriesScienceLibrary
                .calculatePercentageError(data, predicted);

        int dataStartIndex = UtilityMethods.findIndexOfDate(predicted.get(0).getX(), data);

        return IntStream.rangeClosed(0, errors.length - 1)
                .mapToObj(i -> DynamicTest.dynamicTest("Resolving: " + i,
                        () -> {BigDecimal diff = (BigDecimal.ONE.subtract(predicted.get(i).getY().divide(data.get(dataStartIndex + i).getY(),
                                TimeSeriesScienceLibrary.ROUNDING_MODE))).multiply(new BigDecimal("100")).abs();
                            Assertions.assertEquals(diff, errors[i]);
                        }));
    }

}
