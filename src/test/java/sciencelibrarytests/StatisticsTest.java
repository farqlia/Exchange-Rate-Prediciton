package sciencelibrarytests;

import datagenerator.DataGenerator;
import datasciencealgorithms.utils.point.Point;
import mathlibraries.Statistics;
import mathlibraries.TimeSeriesScienceLibrary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;

import java.math.BigDecimal;
import java.util.List;

public class StatisticsTest {

    List<Point> data;

    @BeforeEach
    void setUp(){
        data = DataGenerator.getInstance().generateDataWithTrend(10, BigDecimal.ONE,
                BigDecimal.ONE);
    }

    @Test
    void shouldCalculateFormulaForRMSE(){
        Assertions.assertNotNull(Statistics.RMSE.apply(data, data));
    }

    @Test
    void shouldCalculateFormulaForRMSPE(){
        Assertions.assertNotNull(Statistics.RMSPE.apply(data, data));
    }

    @Test
    void shouldCalculateFormulaForMAPE(){
        Assertions.assertNotNull(Statistics.MAPE.apply(data, data));
    }
    @Test
    void shouldCalculateAverageValueOfRealPoints(){
        Assertions.assertEquals(TimeSeriesScienceLibrary.calculateAverage(data),
                Statistics.AVERAGE_REAL.apply(data, data));
    }

    @Test
    void shouldCalculateAverageValueOfPredictedPoints(){
        List<Point> pred = DataGenerator.getInstance().generateDataWithTrend(10, BigDecimal.ONE,
                new BigDecimal("2"));
        Assertions.assertEquals(TimeSeriesScienceLibrary.calculateAverage(pred),
                Statistics.AVERAGE_PRED.apply(data, pred));
    }

    @Test
    void shouldReturnName(){
        Assertions.assertEquals("Real Average", Statistics.AVERAGE_REAL.toString());
        Assertions.assertEquals("Predicted Average", Statistics.AVERAGE_PRED.toString());
        Assertions.assertEquals("MAPE", Statistics.MAPE.toString());
    }

}
