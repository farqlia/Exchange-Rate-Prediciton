package algorithmstests;

import datasciencealgorithms.utils.Point;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AlgorithmsTest {

    List<Point<LocalDate>> dataPoints;
    DataGenerator dataGenerator;

    @BeforeEach
    void setUp(){
        DataGenerator dataGenerator = new DataGenerator();
        dataPoints = dataGenerator.generateDataWithTrend(10, .2);
    }

    @Test
    void shouldTestForNaiveModelWithTrend(){

        Algorithm naiveAlgorithmWithTrend =
                new NaiveAlgorithmWithTrend(dataPoints);

        Assertions.assertEquals(dataPoints.get(dataPoints.size() - 1).getY(),
                naiveAlgorithmWithTrend.forecastValueForDate(dataPoints.get(dataPoints.size() - 1).getX()));

    }

}
