package viewtest;

import algorithms.AlgorithmInitializerExPost;
import algorithms.algorithmsparameters.AlgorithmArguments;
import dataconverter.formatters.PointToCSV;
import dataconverter.writersandreaders.TextFileReader;
import datasciencealgorithms.utils.point.Point;
import mvc.Model;
import model.ModelEvent;
import model.ModelObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        model.setAlgorithm(algorithms.algorithmsinitializer.AlgorithmInitializerExPost.LWMAA);

        LocalDate startDate = LocalDate.of(2021, 12, 23);

        model.predict(exampleDataPoints, startDate, LocalDate.now());

        verify(observer, timeout(Duration.ofSeconds(2).toMillis())).update(ModelEvent.DATA_PROCESS_STARTED);
    }

    @Test
    void shouldNotifyObserver(){

        model.setAlgorithm(algorithms.algorithmsinitializer.AlgorithmInitializerExPost.LWMAA);

        LocalDate startDate = LocalDate.of(2021, 12, 23);

        model.predict(exampleDataPoints, startDate, LocalDate.now());

        verify(observer, timeout(Duration.ofSeconds(5).toMillis()).atLeastOnce()).update(ModelEvent.DATA_IN_PROCESS);

    }


}
