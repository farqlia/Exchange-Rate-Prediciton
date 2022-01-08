package viewtest;

import datagenerator.DataGenerator;
import datasciencealgorithms.utils.point.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.AbstractView;
import view.DataPointsFlattering;
import view.View;

import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TimeSeriesTableTest {

    DefaultTableModel table;
    AbstractView view;
    List<Point<LocalDate>> data;
    Object[][] flatData;

    @BeforeEach
    void setUp() {
        view = new View();
        table = ((View) view).tableModel;
        data = DataGenerator.getInstance().generateDataWithTrend(10,
                BigDecimal.ZERO, BigDecimal.ONE);
        flatData = DataPointsFlattering.getInstance().flatten(data, data);

    }

    @Test
    void shouldAddColumns(){
        // Initial number of columns should always be three
        Assertions.assertEquals(3, table.getColumnCount());
    }

    @Test
    void shouldAddDataToTable(){
        // Call to this method means we want to update rows
        view.updateTable(flatData);
        Assertions.assertEquals(flatData[0][0], table.getValueAt(0, 0));
        Assertions.assertEquals(flatData[0][1], table.getValueAt(0, 1));
        Assertions.assertEquals(flatData[0][2], table.getValueAt(0, 2));
    }

}
