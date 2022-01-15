package viewtest;

import datagenerator.DataGenerator;
import datasciencealgorithms.utils.point.Point;
import org.junit.jupiter.api.*;
import view.DataPointsFlattering;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Vector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DataFlatteringTest {

    List<Point> data;

    @BeforeEach
    void setUp(){
        data = DataGenerator.getInstance()
                .generateDataWithTrend(10, BigDecimal.ZERO, BigDecimal.ONE);
    }

    @Test
    void shouldFlattenDataPoints(){

        Vector<Vector> flatData = DataPointsFlattering.flatten(data, data);

        for (int i = 0; i < 10; i++){

            Assertions.assertEquals(data.get(i).getX(), flatData.get(i).get(0));
            Assertions.assertEquals(data.get(i).getY(), flatData.get(i).get(1));
            Assertions.assertEquals(data.get(i).getY(), flatData.get(i).get(2));

        }
    }

    @TestFactory
    Stream<DynamicTest> shouldConcatWithArrays(){

        List<Point> data = DataGenerator.getInstance()
                .generateDataWithTrend(10, BigDecimal.ZERO, BigDecimal.ONE);

        Vector<Vector> flatData = DataPointsFlattering.flatten(data, data);

        Object[] arr1 = IntStream.rangeClosed(1, 10).boxed().toArray();
        Object[] arr2 = IntStream.rangeClosed(2, 11).boxed().toArray();

        DataPointsFlattering.concat(flatData, arr1, arr2);

        return IntStream.rangeClosed(0, flatData.size() - 1)
                .mapToObj(i -> DynamicTest.dynamicTest("Resolving: " + i,
                        () -> {
                            Assertions.assertEquals(data.get(i).getX(), flatData.get(i).get(0));
                            Assertions.assertEquals(data.get(i).getY(), flatData.get(i).get(1));
                            Assertions.assertEquals(arr1[i], flatData.get(i).get(3));
                            Assertions.assertEquals(arr2[i], flatData.get(i).get(4));
                        }));

    }

}
