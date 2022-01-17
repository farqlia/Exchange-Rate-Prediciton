package viewtest;

import algorithms.AlgorithmName;
import algorithms.algorithmsparameters.AlgorithmArguments;
import controller.Controller;
import dataconverter.writersandreaders.TextFileWriter;
import datagenerator.DataGenerator;
import datasciencealgorithms.utils.point.Point;
import model.Model;
import model.ModelEvent;
import model.ModelObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import view.other.Plot;
import view.view.AbstractView;
import view.view.View;
import view.view.ViewEvent;
import view.view.ViewObserver;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ControllerTest {

    Controller controller;
    LocalDate startDate = LocalDate.of(2022, 1, 1);
    LocalDate endDate = LocalDate.of(2022, 1, 15);

    @Mock
    AbstractView view;
    @Mock
    Model model;
    @Mock
    DefaultTableModel modelA;
    @Mock
    DefaultTableModel modelS;

    ViewEvent viewEvent;
    List<Point> exampleData;
    Map<AlgorithmArguments.Names, BigDecimal> argMap;

    @BeforeEach
    void setUp(){
        argMap = new HashMap<>();
        argMap.put(AlgorithmArguments.Names.LOOK_BACK_PERIOD, new BigDecimal(5));
        viewEvent = new ViewEvent(startDate, endDate, AlgorithmName.MOVING_AVERAGE_MEAN_ALGORITHM,
                "EUR");
        exampleData = DataGenerator.getInstance().generateDataWithTrend(10, BigDecimal.ONE, BigDecimal.ONE);
        when(view.getJMenuBar()).thenReturn(new JMenuBar());

        controller = new Controller(view, model, modelA, modelS);

    }

    @Test
    void shouldInvokePredictMethodOnModel(){

        controller.new ListenForView().update(viewEvent);

        verify(model).setAlgorithm(any(AlgorithmName.class));

        verify(model).predict(any(List.class), any(LocalDate.class),
                any(LocalDate.class));
    }

    @Test
    void shouldNtPredictMethodOnModel(){

        // Currency is invalid, so we can't be provided with response body
        ViewEvent viewEvent = new ViewEvent(LocalDate.now().minusDays(10),
                LocalDate.now(), AlgorithmName.MOVING_AVERAGE_MEAN_ALGORITHM,
                "INVALID");

        controller.new ListenForView().update(viewEvent);

        verify(model, never()).setAlgorithm(AlgorithmName.MOVING_AVERAGE_MEAN_ALGORITHM);

        verify(model, never()).predict(any(List.class), eq(startDate),
                (eq(endDate)));
    }

    @Test
    void shouldInvokeUpdateAlgorithmTableOnView(){

        View realView = new View(Collections.emptyList(), modelA, modelS);
        controller = new Controller(realView, model, modelA, modelS);
        realView.notifyObservers(viewEvent);

        verify(model).setAlgorithm(AlgorithmName.MOVING_AVERAGE_MEAN_ALGORITHM);
        when(model.getDataChunk())
                .thenReturn(exampleData);

        // We expect that it will get chunk of data from model and put it to the view
        controller.new ListenForModel1().update(ModelEvent.DATA_IN_PROCESS);

        // For table with statistics, we can't add data all at once
        verify(modelA, atLeast(1)).addRow(any(Vector.class));

    }

    @Test
    void shouldInvokeUpdateStatisticsTableOnView(){

        View realView = new View(Collections.emptyList(), modelA, modelS);
        controller = new Controller(realView, model, modelA, modelS);
        realView.notifyObservers(viewEvent);

        when(model.getDataChunk())
                .thenReturn(exampleData);

        // We expect that it will get chunk of data from model and put it to the view
        ModelObserver o = controller.new ListenForModel2();

        // Should add data to it's internal array
        o.update(ModelEvent.DATA_IN_PROCESS);
        // Now based on gathered data it computes general statistics
        o.update(ModelEvent.DATA_PROCESSED);

        verify(modelS).setDataVector(any(Vector.class), any(Vector.class));

    }

    @Mock
    TextFileWriter<Vector> writer;


    @Mock
    Plot plot;

    @Test
    void shouldCreatePlot() {
        ModelObserver listener = controller.new ListenForCreatePlot(plot);

        listener.update(ModelEvent.DATA_IN_PROCESS);

        // Sets domain range
        verify(plot).setDomainRange(any());
        verify(plot).addSeries(eq("Real"), any());
        verify(plot).addSeries(eq("Predicted"), any());
    }


    @Test
    void shouldClearList(){

        ViewObserver ob = controller.new ListenForView();

        // List of real data points should be cleared each time we make a new prediction
        ob.update(viewEvent);
        ob.update(viewEvent);
        int length = (int) ChronoUnit.DAYS.between(startDate.minusMonths(1), endDate);
        verify(model, atLeastOnce()).predict(argThat(x -> x.size() < length),
                eq(startDate), eq(endDate));
    }

    @Test
    void shouldDisableAndEnableViewAction(){

        ModelObserver ob = controller.new HandleViewAction();
        ob.update(ModelEvent.DATA_PROCESS_STARTED);

        verify(view).disableActions();

        ob.update(ModelEvent.DATA_PROCESSED);

        verify(view).enableActions();
    }

}
