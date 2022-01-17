package viewtest;

import algorithms.AlgorithmName;
import algorithms.algorithmsparameters.AlgorithmArguments;
import dataconverter.writersandreaders.PointToCSV;
import dataconverter.writersandreaders.TextFileReader;
import datasciencealgorithms.utils.point.Point;
import model.Model;
import model.ModelEvent;
import model.ModelObserver;
import model.TableModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ModelTest {

    Model model;
    List<Point> exampleDataPoints;
    @Mock
    ModelObserver observer;
    Map<AlgorithmArguments.Names, BigDecimal> arg;

    @BeforeEach
    void setUp() throws IOException {
        model = new Model();
        exampleDataPoints = new TextFileReader<Point>(new PointToCSV())
                .readFromFile("exampledata\\dp2.txt");
        model.registerObserver(observer);
        arg = new HashMap<>();
        arg.put(AlgorithmArguments.Names.LOOK_BACK_PERIOD, new BigDecimal(5));
    }

    @Test
    void shouldNotifyObserverAboutStart(){
        model.setAlgorithm(AlgorithmName.LINEARLY_WEIGHTED_MOVING_AVERAGE_ALGORITHM);

        LocalDate startDate = LocalDate.of(2021, 12, 23);

        model.predict(exampleDataPoints, startDate, LocalDate.now());

        verify(observer).update(ModelEvent.DATA_PROCESS_STARTED);
    }

    @Test
    void shouldNotifyObserver(){

        model.setAlgorithm(AlgorithmName.LINEARLY_WEIGHTED_MOVING_AVERAGE_ALGORITHM);

        LocalDate startDate = LocalDate.of(2021, 12, 23);

        model.predict(exampleDataPoints, startDate, LocalDate.now());

        verify(observer, timeout(Duration.ofSeconds(5).toMillis()).atLeastOnce()).update(ModelEvent.DATA_IN_PROCESS);

    }

    @Test
    void shouldReturnNonNullList(){

        model.setAlgorithm(AlgorithmName.LINEARLY_WEIGHTED_MOVING_AVERAGE_ALGORITHM);

        LocalDate startDate = LocalDate.of(2021, 12, 23);

        model.predict(exampleDataPoints, startDate, LocalDate.now());

        verify(observer, timeout(Duration.ofSeconds(5).toMillis()).atLeastOnce()).update(ModelEvent.DATA_IN_PROCESS);

    }

    @Test
    void shouldReturnRuntimeClass(){
        DefaultTableModel model = new TableModel(new Vector(List.of("col1", "col2")), 0);

        model.addRow(new Vector<>(List.of(BigDecimal.ONE, LocalDate.now())));
        model.addRow(new Vector<>(List.of(BigDecimal.ONE, LocalDate.now())));
        Assertions.assertTrue(model.getColumnClass(0).isInstance(BigDecimal.ONE));
        Assertions.assertTrue(model.getColumnClass(1).isInstance(LocalDate.now()));
    }
}
