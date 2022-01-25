package viewtest;

import algorithms.algorithmsinitializer.AlgorithmInitializerExPost;
import algorithms.algorithmsparameters.AlgorithmArguments;
import mvc.Controller;
import dataconverter.writersandreaders.JsonFileWriter;
import dataconverter.writersandreaders.TextFileWriter;
import datagenerator.DataGenerator;
import datasciencealgorithms.utils.point.Point;
import model.*;
import mvc.Model;
import mvc.View;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import studyjson.ResultsInfo;
import view.IO.FileSaveHandler;
import view.IO.FileTypes;
import view.other.*;
import view.view.*;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
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
    LocalDate endDate = LocalDate.now().minusDays(2);

    @Mock
    AbstractView view;
    @Mock
    JTable table;
    @Mock
    Model model;
    @Mock
    ResultsTableModel modelA;
    @Mock
    StatisticsTableModel modelS;

    ViewEvent viewEvent;
    List<Point> exampleData;
    Map<AlgorithmArguments.Names, BigDecimal> argMap;

    @BeforeEach
    void setUp(){
        argMap = new HashMap<>();
        argMap.put(AlgorithmArguments.Names.LOOK_BACK_PERIOD, new BigDecimal(5));
        viewEvent = new ViewEvent(startDate, endDate, AlgorithmInitializerExPost.MAMA,
                "EUR");
        exampleData = DataGenerator.getInstance().generateDataWithTrend(10, BigDecimal.ONE, BigDecimal.ONE);
        when(view.getJMenuBar()).thenReturn(new JMenuBar());

        controller = new Controller(view, table, model, model, modelA, modelS, modelA);
    }

    @Test
    void shouldInvokePredictMethodOnModel(){

        new ListenForView(model).update(viewEvent);

        verify(model).setAlgorithm(AlgorithmInitializerExPost.MAMA);

        verify(model).predict(any(List.class), eq(startDate),
                eq(endDate));
    }

    @Test
    void shouldNtPredictMethodOnModel(){

        // Currency is invalid, so we can't be provided with response body
        ViewEvent viewEvent = new ViewEvent(LocalDate.now().minusDays(10),
                LocalDate.now(), AlgorithmInitializerExPost.MAMA,
                "INVALID");

        new ListenForView(model).update(viewEvent);

        verify(model, never()).setAlgorithm(AlgorithmInitializerExPost.MAMA);

        verify(model, never()).predict(any(List.class), eq(startDate),
                (eq(endDate)));
    }

    @Mock
    Plot plot;
    @Mock
    TableModelEvent event;

    @Test
    void shouldCreatePlot() {
        TableModelListener listener = new PlotController(plot, modelA, "Predicted");

        listener.tableChanged(event);

        // Sets domain range
        verify(plot).setDomainRange(any());
        verify(plot).addSeries(eq("Real"), any());
        verify(plot).addSeries(eq("Predicted"), any());
    }


    @Test
    void shouldClearList(){

        ViewObserver ob = new ListenForView(model);

        // List of real data points should be cleared each time we make a new prediction
        ob.update(viewEvent);
        ob.update(viewEvent);

        int length = (int) ChronoUnit.DAYS.between(startDate.minusMonths(1), endDate);
        verify(model, atLeastOnce()).predict(argThat(x -> x.size() < length),
                eq(startDate), eq(endDate));
    }

    @Test
    void shouldDisableAndEnableViewAction(){

        when(modelS.getRow(2)).thenReturn(new StatisticsTableModel.Row("", BigDecimal.ONE));
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

        FileSaveHandler handler = new FileSaveHandler(modelA, textWriter, jsonWriter);

        handler.update(viewEvent);
        handler.propertyChange(new PropertyChangeEvent(view,
                FileTypes.TEXT.name(), null,null));

        verify(textWriter).saveToFile(argThat(x -> x.endsWith(".txt")),
                eq(Collections.singletonList(r)));
    }

    @Test
    void shouldSaveToJsonFile() throws IOException {

        when(modelA.getListOfRows()).thenReturn(Collections.singletonList(r));
        ResultsInfo info = new ResultsInfo(viewEvent.getChosenAlgorithm().toString(),
                viewEvent.getCurrencyCode(),  Collections.singletonList(r));

        FileSaveHandler handler = new FileSaveHandler(modelA, textWriter, jsonWriter);

        handler.update(viewEvent);
        handler.propertyChange(new PropertyChangeEvent(view,
                FileTypes.JSON.name(), null,null));

        verify(jsonWriter).saveToFile(argThat(x -> x.endsWith(".json")),
                eq(Collections.singletonList(info)));
    }

    @Test
    void shouldReactOnActions(){

        ViewObserver observer = controller.new HandleExPostPredictions();

        observer.update(viewEvent);

        verify(model).predict(any(), eq(viewEvent.getStartDate()), eq(viewEvent.getEndDate()));

    }

    @Test
    void shouldReactOnActionsFromView(){

        JTable table = new JTable();
        View view = new View(Collections.emptyList(), table);
        controller = new Controller(view, table, model, model, modelA, modelS, modelA);
        view.notifyObservers(ViewEventType.EX_POST, viewEvent);


        verify(model).predict(any(), eq(viewEvent.getStartDate()), eq(viewEvent.getEndDate()));

    }

}
