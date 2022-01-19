package viewtest;

import datasciencealgorithms.utils.point.Point;
import model.CustomTableModel;
import model.ResultsTableModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class CustomTableModelTest {

    CustomTableModel<ResultsTableModel.Row> model;
    @BeforeEach
    void setUp(){
        model = new ResultsTableModel(List.of("Date", "Expected", "Predicted", "Error",
                "Percentage Error"), "");
    }

    @Test
    void shouldAddRow(){
        Point p = new Point(LocalDate.now(), BigDecimal.ZERO);
        model.addRow(new ResultsTableModel.Row(
                p, p, BigDecimal.ZERO, BigDecimal.ONE
        ));
        Assertions.assertEquals(1, model.getRowCount());
    }
    @Test
    void shouldReturnCorrectValues(){
        Point p = new Point(LocalDate.now(), BigDecimal.ZERO);
        Point p2 = new Point(LocalDate.now(), BigDecimal.ONE);
        model.addRow(new ResultsTableModel.Row(
                p, p2, BigDecimal.ZERO, BigDecimal.ONE
        ));
        Assertions.assertAll(
                () -> Assertions.assertEquals(p.getX(), model.getValueAt(0, 0)),
                () -> Assertions.assertEquals(p.getY(), model.getValueAt(0, 1)),
                () -> Assertions.assertEquals(p2.getY(), model.getValueAt(0, 2)),
                () -> Assertions.assertEquals(BigDecimal.ZERO, model.getValueAt(0, 3)),
                () -> Assertions.assertEquals(BigDecimal.ONE, model.getValueAt(0, 4))
        );
    }

    @Test
    void shouldCountColumns(){
        Assertions.assertEquals(5, model.getColumnCount());
    }

    @Test
    void shouldReturnCorrectColumnClass(){
        Point p = new Point(LocalDate.now(), BigDecimal.ZERO);
        model.addRow(new ResultsTableModel.Row(
                p, p, BigDecimal.ZERO, BigDecimal.ONE
        ));
        Assertions.assertAll(
                () -> Assertions.assertEquals(LocalDate.class, model.getColumnClass(0)),
                () -> Assertions.assertEquals(BigDecimal.class, model.getColumnClass(1))
        );
    }

    @Test
    void shouldRemoveRows(){
        Point p = new Point(LocalDate.now(), BigDecimal.ZERO);
        model.addRow(new ResultsTableModel.Row(
                p, p, BigDecimal.ZERO, BigDecimal.ONE
        ));
        model.deleteRows();
        Assertions.assertEquals(0, model.getRowCount());
    }

    @Test
    void shouldReturnCopyOfRealData(){
        List<ResultsTableModel.Row> rows = model.getListOfRows();
        Point p = new Point(LocalDate.now(), BigDecimal.ZERO);
        ResultsTableModel.Row r = new ResultsTableModel.Row(
                p, p, BigDecimal.ZERO, BigDecimal.ONE
        );
        rows.add(r);
        rows.add(r);
        Assertions.assertEquals(0, model.getRowCount());
    }

}
