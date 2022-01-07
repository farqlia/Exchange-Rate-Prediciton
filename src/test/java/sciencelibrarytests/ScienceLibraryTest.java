package sciencelibrarytests;

import datagenerator.DataGenerator;
import mathlibraries.ScienceLibrary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class ScienceLibraryTest {

    ScienceLibrary library;
    int dataset = 10;

    @BeforeEach
    void setUp(){
        // This will be a singleton class
        library = ScienceLibrary.getInstance();
    }

    @Test
    void shouldReturnZeroAsAMeanError(){

        List<BigDecimal> data = DataGenerator.getInstance().generateSimpleData(dataset, BigDecimal.ONE,
                new BigDecimal(".3"));
        BigDecimal ME = library.calculateMeanError(data, data);
        Assertions.assertEquals(ME.setScale(0, RoundingMode.CEILING), BigDecimal.ZERO);

    }

    @Test
    void shouldReturnRMSPELessThenOne(){

        List<BigDecimal> data = DataGenerator.getInstance().generateSimpleData(dataset, BigDecimal.ONE,
                new BigDecimal(".3"));
        List<BigDecimal> predicted = DataGenerator.getInstance().generateSimpleData(dataset, BigDecimal.ONE,
                new BigDecimal(".3"), 1);
        BigDecimal RMSPE = library.calculateRootMeanSquaredPercentageError(data, predicted);
        Assertions.assertTrue(RMSPE.compareTo(BigDecimal.ONE) < 0);

    }

    @Test
    void shouldReturnMAPSELessThenOne(){

        List<BigDecimal> data = DataGenerator.getInstance().generateSimpleData(dataset, BigDecimal.ONE,
                new BigDecimal(".3"));
        List<BigDecimal> predicted = DataGenerator.getInstance().generateSimpleData(dataset, BigDecimal.ONE,
                new BigDecimal(".3"), 1);
        BigDecimal MAPE = library.calculateMeanAbsolutePercentageError(data, predicted);
        Assertions.assertTrue(MAPE.compareTo(BigDecimal.ONE) < 0);

    }

    @Test
    void shouldReturnZeroAsAMeanSquareError(){

        List<BigDecimal> data = DataGenerator.getInstance().generateSimpleData(dataset, BigDecimal.ONE,
                new BigDecimal(".3"));
        BigDecimal MSE = library.calculateMeanSquaredError(data, data);
        Assertions.assertEquals(MSE.setScale(0, RoundingMode.CEILING), BigDecimal.ZERO);

    }

    @Test
    void shouldReturnNonZeroAsAMeanSquareError(){

        List<BigDecimal> data = DataGenerator.getInstance().generateSimpleData(dataset, BigDecimal.ONE,
                new BigDecimal(".3"));
        List<BigDecimal> predicted = DataGenerator.getInstance().generateSimpleData(dataset, BigDecimal.ONE,
                new BigDecimal(".3"), 1);
        BigDecimal MSE = library.calculateMeanSquaredError(data, predicted);
        Assertions.assertTrue(MSE.compareTo(BigDecimal.ZERO) > 0);

    }

    @Test
    void shouldCorrectlyEstimateMeanSquareError(){

        List<BigDecimal> data = DataGenerator.getInstance().generateSimpleData(dataset, BigDecimal.ONE,
                new BigDecimal(".3"), 1);
        BigDecimal MSE = library.calculateMeanSquaredError(data, data);
        BigDecimal boundaryMSE = new BigDecimal("0.5").pow(2).multiply(new BigDecimal(dataset));
        Assertions.assertTrue(MSE.compareTo(boundaryMSE) <= 0);

    }
}
