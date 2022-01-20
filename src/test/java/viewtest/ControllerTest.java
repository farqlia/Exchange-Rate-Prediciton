package viewtest;

import algorithms.AlgorithmInitializer;
import algorithms.algorithmsparameters.AlgorithmArguments;
import controller.Controller;
import dataconverter.writersandreaders.JsonFileWriter;
import dataconverter.writersandreaders.TextFileWriter;
import datagenerator.DataGenerator;
import datasciencealgorithms.utils.point.Point;
import mathlibraries.Statistics;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import studyjson.ResultsInfo;
import view.other.Menu;
import view.other.Plot;
import view.view.*;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ControllerTest {

    Controller controller;
    LocalDate startDate = LocalDate.of(2022, 1, 1);
    LocalDate endDate = LocalDate.now().minusDays(2);

    @Mock
    AbstractView view;
    @Mock
    Model model;
    @Mock
    CustomTableModel<ResultsTableModel.Row> modelA;
    @Mock
    CustomTableModel<StatisticsTableModel.Row> modelS;

    ViewEvent viewEvent;
    List<Point> exampleData;
    Map<AlgorithmArguments.Names, BigDecimal> argMap;

    @BeforeEach
    void setUp(){
        argMap = new HashMap<>();
        argMap.put(AlgorithmArguments.Names.LOOK_BACK_PERIOD, new BigDecimal(5));
        viewEvent = new ViewEvent(startDate, endDate, AlgorithmInitializer.MOVING_AVERAGE_MEAN_ALGORITHM,
                "EUR");
        exampleData = DataGenerator.getInstance().generateDataWithTrend(10, BigDecimal.ONE, BigDecimal.ONE);
        when(view.getJMenuBar()).thenReturn(new JMenuBar());

        controller = new Controller(view, model, modelA, modelS);
    }

    @Test
    void shouldInvokePredictMethodOnModel(){

        controller.new ListenForView().update(viewEvent);

        verify(model).setAlgorithm(AlgorithmInitializer.MOVING_AVERAGE_MEAN_ALGORITHM);

        verify(model).predict(any(List.class), eq(startDate),
                eq(endDate));
    }

    @Test
    void shouldNtPredictMethodOnModel(){

        // Currency is invalid, so we can't be provided with response body
        ViewEvent viewEvent = new ViewEvent(LocalDate.now().minusDays(10),
                LocalDate.now(), AlgorithmInitializer.MOVING_AVERAGE_MEAN_ALGORITHM,
                "INVALID");

        controller.new ListenForView().update(viewEvent);

        verify(model, never()).setAlgorithm(AlgorithmInitializer.MOVING_AVERAGE_MEAN_ALGORITHM);

        verify(model, never()).predict(any(List.class), eq(startDate),
                (eq(endDate)));
    }

    @Test
    void shouldInvokeUpdateAlgorithmTableOnView(){

        View realView = new View(Collections.emptyList(), modelA, modelS);
        controller = new Controller(realView, model, modelA, modelS);
        realView.notifyObservers(viewEvent);

        verify(model).setAlgorithm(AlgorithmInitializer.MOVING_AVERAGE_MEAN_ALGORITHM);
        // This test can broke down because of unequal domains
        exampleData =
                exampleData.stream().map(x -> new Point(x.getX().minusMonths(1),
                        x.getY())).collect(Collectors.toList());

        when(model.getDataChunk())
                .thenReturn(exampleData);

        // We expect that it will get chunk of data from model and put it to the view
        controller.new ListenForModel1().update(ModelEvent.DATA_IN_PROCESS);

        // For table with statistics, we can't add data all at once
        verify(modelA, atLeast(1)).addRow(any(ResultsTableModel.Row.class));

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
        o.update(ModelEvent.DATA_PROCESS_FINISHED);

        verify(modelS, times(Statistics.values().length))
                .addRow(any(StatisticsTableModel.Row.class));

    }
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

        ob.update(ModelEvent.DATA_PROCESS_FINISHED);

        verify(view).enableActions();
    }

    Point p = new Point(LocalDate.now(), BigDecimal.ZERO);
    ResultsTableModel.Row r = new ResultsTableModel.Row(
            p, p, BigDecimal.ZERO, BigDecimal.ONE
    );

    @Mock
    JsonFileWriter jsonWriter;
    @Mock
    TextFileWriter<ResultsTableModel.Row> textWriter;

    @Test
    void shouldSaveToTextFile() throws IOException {

        when(modelA.getListOfRows()).thenReturn(Collections.singletonList(r));

        Controller.HandleSaveToFile handler = controller.new HandleSaveToFile(textWriter, jsonWriter);

        handler.update(viewEvent);
        handler.propertyChange(new PropertyChangeEvent(view, Menu.SAVE_AS_TEXT, null,null));

        verify(textWriter).saveToFile(argThat(x -> x.endsWith(".txt")),
                eq(Collections.singletonList(r)));
    }

    @Test
    void shouldSaveToJsonFile() throws IOException {

        when(modelA.getListOfRows()).thenReturn(Collections.singletonList(r));
        ResultsInfo info = new ResultsInfo(viewEvent.getChosenAlgorithm().toString(),
                viewEvent.getCurrencyCode(),  Collections.singletonList(r));

        Controller.HandleSaveToFile handler = controller.new HandleSaveToFile(textWriter, jsonWriter);

        handler.update(viewEvent);
        handler.propertyChange(new PropertyChangeEvent(view, Menu.SAVE_AS_JSON, null,null));

        verify(jsonWriter).saveToFile(argThat(x -> x.endsWith(".json")),
                eq(Collections.singletonList(info)));
    }

}
