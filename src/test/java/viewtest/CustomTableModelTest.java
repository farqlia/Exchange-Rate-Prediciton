package viewtest;

import datagenerator.DataGenerator;
import datasciencealgorithms.utils.point.Point;
import model.*;
import mvc.Model;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomTableModelTest {

    ResultsTableModel modelA;
    StatisticsTableModel modelS;
    @Mock
    Model dataModel;
    List<Point> exampleData;

    @BeforeEach
    void setUp(){

        modelA = new ResultsTableModel(List.of("Date", "Expected", "Predicted", "Error",
                "Percentage Error"), "", dataModel);
        modelS = new StatisticsTableModel(List.of("Name", "Value"),"Statistics", dataModel);
        exampleData = DataGenerator.getInstance().generateDataWithTrend(10,
                BigDecimal.ONE, BigDecimal.ONE);
    }

    @Test
    void shouldAddRow(){
        Point p = new Point(LocalDate.now(), BigDecimal.ZERO);
        modelA.addRow(new ResultsTableModel.Row(
                p, p, BigDecimal.ZERO, BigDecimal.ONE
        ));
        Assertions.assertEquals(1, modelA.getRowCount());
    }
    @Test
    void shouldReturnCorrectValues(){
        Point p = new Point(LocalDate.now(), BigDecimal.ZERO);
        Point p2 = new Point(LocalDate.now(), BigDecimal.ONE);
        modelA.addRow(new ResultsTableModel.Row(
                p, p2, BigDecimal.ZERO, BigDecimal.ONE
        ));
        Assertions.assertAll(
                () -> Assertions.assertEquals(p.getX(), modelA.getValueAt(0, 0)),
                () -> Assertions.assertEquals(p.getY(), modelA.getValueAt(0, 1)),
                () -> Assertions.assertEquals(p2.getY(), modelA.getValueAt(0, 2)),
                () -> Assertions.assertEquals(BigDecimal.ZERO, modelA.getValueAt(0, 3)),
                () -> Assertions.assertEquals(BigDecimal.ONE, modelA.getValueAt(0, 4))
        );
    }

    @Test
    void shouldCountColumns(){
        Assertions.assertEquals(5, modelA.getColumnCount());
    }

    @Test
    void shouldReturnCorrectColumnClass(){
        Point p = new Point(LocalDate.now(), BigDecimal.ZERO);
        modelA.addRow(new ResultsTableModel.Row(
                p, p, BigDecimal.ZERO, BigDecimal.ONE
        ));
        Assertions.assertAll(
                () -> Assertions.assertEquals(LocalDate.class, modelA.getColumnClass(0)),
                () -> Assertions.assertEquals(BigDecimal.class, modelA.getColumnClass(1))
        );
    }

    @Test
    void shouldRemoveRows(){
        Point p = new Point(LocalDate.now(), BigDecimal.ZERO);
        modelA.addRow(new ResultsTableModel.Row(
                p, p, BigDecimal.ZERO, BigDecimal.ONE
        ));
        modelA.deleteRows();
        Assertions.assertEquals(0, modelA.getRowCount());
    }

    @Test
    void shouldReturnCopyOfRealData(){
        List<ResultsTableModel.Row> rows = modelA.getListOfRows();
        Point p = new Point(LocalDate.now(), BigDecimal.ZERO);
        ResultsTableModel.Row r = new ResultsTableModel.Row(
                p, p, BigDecimal.ZERO, BigDecimal.ONE
        );
        rows.add(r);
        rows.add(r);
        Assertions.assertEquals(0, modelA.getRowCount());
    }

    @Mock
    JTable tableView;
    @Test
    void shouldInvokeUpdateAlgorithmTableOnView(){


        modelA.addTableModelListener(tableView);

        when(dataModel.getDataChunk()).thenReturn(exampleData);

        modelA.update(ModelEvent.DATA_IN_PROCESS);

        verify(dataModel, atLeastOnce()).getDataChunk();
    }

    @Test
    void shouldInvokeUpdateStatisticsTableOnView(){

        modelS.addTableModelListener(tableView);

        when(dataModel.getDataChunk()).thenReturn(exampleData);
        when(dataModel.getRealData()).thenReturn(exampleData);

        modelS.update(ModelEvent.DATA_PROCESS_FINISHED);

        verify(dataModel, atLeastOnce()).getDataChunk();
    }

}
