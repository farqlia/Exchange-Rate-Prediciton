package viewtest;

import controller.TableModel;
import datagenerator.DataGenerator;
import datasciencealgorithms.utils.point.Point;
import exchangerateclass.CurrencyName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import view.AbstractView;
import view.DataPointsFlattering;
import view.View;

import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.stream.IntStream;

public class TimeSeriesTableTest {

    TableModel table;
    List<Point> data;
    Vector<Vector> flatData;
    Object[] exampleColumn;
    int dataset = 10;

    @BeforeEach
    void setUp() {
        table = new TableModel(new Vector<>(List.of("Column1", "Column2", "Column3")));
        data = DataGenerator.getInstance().generateDataWithTrend(dataset,
                BigDecimal.ZERO, BigDecimal.ONE);
        flatData = DataPointsFlattering.flatten(data, data);
        exampleColumn =  IntStream.rangeClosed(0, 10)
                .boxed().toArray();

    }

    @Test
    void shouldAddInitialColumns(){
        Assertions.assertAll(
                () -> Assertions.assertEquals(3, table.getColumnCount()),
                () -> Assertions.assertEquals("Column1", table.getColumnName(0)),
                () -> Assertions.assertEquals("Column2", table.getColumnName(1)),
                () -> Assertions.assertEquals("Column3", table.getColumnName(2))
        );
    }

    @Test
    void shouldAddDataToTable(){
        // Call to this method means we want to update rows
        table.setDataVector(flatData);
        Assertions.assertAll(
                () -> Assertions.assertEquals(flatData.get(0).get(0), table.getValueAt(0, 0)),
                () -> Assertions.assertEquals(flatData.get(0).get(1), table.getValueAt(0, 1)),
                () -> Assertions.assertEquals(flatData.get(0).get(1), table.getValueAt(0, 2)));
    }

    @RepeatedTest(3)
    void shouldDeleteCurrentRows(){
        table.clear();
        table.setDataVector(flatData);
        Assertions.assertEquals(dataset, table.getRowCount());

    }

}
