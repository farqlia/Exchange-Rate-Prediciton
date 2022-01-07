package viewtest;

import datagenerator.DataGenerator;
import datasciencealgorithms.utils.point.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import view.DataFlattering;
import view.DataPointsFlattering;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class DataFlatteringTest {

    @Test
    void shouldFlattenDataPoints(){

        List<Point<LocalDate>> data = DataGenerator.getInstance()
                .generateDataWithTrend(10, BigDecimal.ZERO, BigDecimal.ONE);

        Object[][] flatData = DataPointsFlattering.getInstance().flatten(data, data);

        for (int i = 0; i < 10; i++){

            Assertions.assertEquals(data.get(i).getX(), flatData[i][0]);
            Assertions.assertEquals(data.get(i).getY(), flatData[i][1]);
            Assertions.assertEquals(data.get(i).getY(), flatData[i][2]);

        }
    }

}
