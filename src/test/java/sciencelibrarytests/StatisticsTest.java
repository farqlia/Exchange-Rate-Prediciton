package sciencelibrarytests;

import datagenerator.DataGenerator;
import datasciencealgorithms.utils.point.Point;
import mathlibraries.Statistics;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

}
