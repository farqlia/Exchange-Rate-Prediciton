package viewtest;

import algorithms.AlgorithmName;
import dataconverter.writersandreaders.PointToCSV;
import dataconverter.writersandreaders.TextFileReader;
import datasciencealgorithms.utils.point.Point;
import model.Model;
import model.ModelEvent;
import model.ModelObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ModelTest {

    Model model;
    List<Point> exampleDataPoints;
    @Mock
    ModelObserver observer;

    @BeforeEach
    void setUp() throws IOException {
        model = new Model();
        exampleDataPoints = new TextFileReader<Point>(new PointToCSV())
                .readFromFile("exampledata\\dp2.txt");
        model.registerObserver(observer);
    }

    @Test
    void shouldNotifyObserverAboutStart(){
        model.setAlgorithm(AlgorithmName.LINEARLY_WEIGHTED_MOVING_AVERAGE_ALGORITHM, 5);

        LocalDate startDate = LocalDate.of(2021, 12, 23);

        model.predict(exampleDataPoints, startDate, LocalDate.now());

        verify(observer).update(ModelEvent.DATA_PROCESS_STARTED);
    }

    @Test
    void shouldNotifyObserver(){

        model.setAlgorithm(AlgorithmName.LINEARLY_WEIGHTED_MOVING_AVERAGE_ALGORITHM, 5);

        LocalDate startDate = LocalDate.of(2021, 12, 23);

        model.predict(exampleDataPoints, startDate, LocalDate.now());

        verify(observer, timeout(Duration.ofSeconds(2).toMillis())).update(ModelEvent.DATA_IN_PROCESS);

    }

    @Test
    void shouldReturnNonNullList(){

        model.setAlgorithm(AlgorithmName.LINEARLY_WEIGHTED_MOVING_AVERAGE_ALGORITHM, 5);

        LocalDate startDate = LocalDate.of(2021, 12, 23);

        model.predict(exampleDataPoints, startDate, LocalDate.now());

        verify(observer, timeout(Duration.ofSeconds(2).toMillis())).update(ModelEvent.DATA_IN_PROCESS);

    }
}
