package sciencelibrarytests;

import mathlibraries.TimeSeriesScienceLibrary;
import datagenerator.DataGenerator;
import datasciencealgorithms.utils.point.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TimeSeriesScienceLibraryTest {


    @Test
    void shouldCorrectlyEstimateMeanAbsoluteError(){
        List<Point<LocalDate>> data = DataGenerator.getInstance()
                .generateDataWithTrend(10, BigDecimal.ONE, BigDecimal.ONE);

        List<Point<LocalDate>> predicted = DataGenerator.getInstance().generateDataWithTrend(10,
                BigDecimal.ONE, BigDecimal.ONE, 0.5);

        BigDecimal MAE = TimeSeriesScienceLibrary.getInstance().calculateMeanAbsoluteError(predicted, data,5);
        System.out.println("MAE= " + MAE);
        Assertions.assertTrue(MAE.compareTo(new BigDecimal("1")) < 0);
    }

    @Test
    void shouldCorrectlyEstimateRMSE(){

        List<Point<LocalDate>> data = DataGenerator.getInstance()
                .generateDataWithTrend(10, BigDecimal.ONE, BigDecimal.ONE);

        List<Point<LocalDate>> predicted = DataGenerator.getInstance().generateDataWithTrend(10,
                BigDecimal.ONE, BigDecimal.ONE, 0.5);

        BigDecimal RMSE = TimeSeriesScienceLibrary.getInstance().calculateRootMeanSquareError(predicted, data, 5);
        System.out.println("RMSE= " + RMSE);
        Assertions.assertTrue(RMSE.compareTo(new BigDecimal("1")) < 0);

    }

    @Test
    void shouldCalculateMeanAbsolutePercentageError(){


        List<Point<LocalDate>> data = DataGenerator.getInstance()
                .generateDataWithTrend(10, BigDecimal.ONE, BigDecimal.ONE);
        List<Point<LocalDate>> predicted = DataGenerator.getInstance().generateDataWithTrend(10,
                BigDecimal.ONE, BigDecimal.ONE, 0.5);


        BigDecimal MAPE = TimeSeriesScienceLibrary.getInstance().calculateMeanAbsolutePercentageError(predicted, data, 5);
        System.out.println("MAPE= " + MAPE);
        Assertions.assertTrue(MAPE.compareTo(BigDecimal.ONE) < 0);

    }

}
