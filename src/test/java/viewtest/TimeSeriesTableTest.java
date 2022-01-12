package viewtest;

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
import java.util.stream.IntStream;

public class TimeSeriesTableTest {

    DefaultTableModel table;
    AbstractView view;
    List<Point> data;
    Object[][] flatData;
    Object[] exampleColumn;
    int dataset = 10;

    @BeforeEach
    void setUp() {
        view = new View(Collections.singletonList(new CurrencyName("euro", "EUR")));
        table = ((View) view).algorithmTableModel;
        data = DataGenerator.getInstance().generateDataWithTrend(dataset,
                BigDecimal.ZERO, BigDecimal.ONE);
        flatData = DataPointsFlattering.getInstance().flatten(data, data);
        exampleColumn =  IntStream.rangeClosed(0, 10)
                .boxed().toArray();

    }

    @Test
    void shouldAddInitialColumns(){
        Assertions.assertEquals(5, table.getColumnCount());
    }

    @Test
    void shouldAddDataToTable(){
        // Call to this method means we want to update rows
        view.insertAlgorithmOutput(flatData);
        Assertions.assertEquals(flatData[0][0], table.getValueAt(0, 0));
        Assertions.assertEquals(flatData[0][1], table.getValueAt(0, 1));
        Assertions.assertEquals(flatData[0][2], table.getValueAt(0, 2));
    }

    @RepeatedTest(3)
    void shouldDeleteCurrentRows(){
        view.insertAlgorithmOutput(flatData);
        Assertions.assertEquals(dataset, table.getRowCount());
    }

    /*
    @Test
    void shouldAddColumns(){

        Object[][] cols = new Object[][]{exampleColumn};

        view.insertStatistics(cols, new String[]{"New Column"});
        Assertions.assertEquals(4, table.getColumnCount());
    }

    @Test // ?? Columns are probably deleted from the view,
    // but not from the model
    void shouldRemoveColumnsFromOldDisplay(){
        Object[][] cols = new Object[][]{exampleColumn};
        view.updateTable(cols, new String[]{"New Column"});

        view.insertAlgorithmOutput(flatData);
        Assertions.assertEquals(3, table.getColumnCount());
    }

     */

}
