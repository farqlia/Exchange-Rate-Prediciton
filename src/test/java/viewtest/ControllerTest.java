package viewtest;

import algorithms.AlgorithmName;
import com.sun.jdi.event.Event;
import controller.Controller;
import controller.TableModel;
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
import view.*;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ControllerTest {

    Controller controller;

    @Mock
    AbstractView view;
    @Mock
    Model model;
    @Mock
    TableModel modelA;
    @Mock
    TableModel modelS;

    ViewEvent viewEvent;
    List<Point> exampleData;

    @BeforeEach
    void setUp(){
        viewEvent = new ViewEvent(LocalDate.now().minusDays(10),
                LocalDate.now(), AlgorithmName.MOVING_AVERAGE_MEAN_ALGORITHM,
                "EUR");
        exampleData = DataGenerator.getInstance().generateDataWithTrend(10, BigDecimal.ONE, BigDecimal.ONE);
    }

    @Test
    void shouldInvokePredictMethodOnModel(){

        when(view.getJMenuBar()).thenReturn(new JMenuBar());

        controller = new Controller(view, model, modelA, modelS);

        controller.new ListenForView().update(viewEvent);

        verify(model).setAlgorithm(any(AlgorithmName.class), eq(5));

        verify(model).predict(any(List.class), any(LocalDate.class),
                any(LocalDate.class));
    }

    @Test
    void shouldNtPredictMethodOnModel(){
        when(view.getJMenuBar()).thenReturn(new JMenuBar());

        controller = new Controller(view, model, modelA, modelS);

        // Currency is invalid, so we can't be provided with response body
        ViewEvent viewEvent = new ViewEvent(LocalDate.now().minusDays(10),
                LocalDate.now(), AlgorithmName.MOVING_AVERAGE_MEAN_ALGORITHM,
                "INVALID");

        controller.new ListenForView().update(viewEvent);

        verify(model, never()).setAlgorithm(any(AlgorithmName.class), eq(5));

        verify(model, never()).predict(any(List.class), any(LocalDate.class),
                any(LocalDate.class));
    }

    @Test
    void shouldInvokeUpdateAlgorithmTableOnView(){

        View realView = new View(Collections.emptyList(), modelA, modelS);
        controller = new Controller(realView, model, modelA, modelS);
        realView.notifyObservers(viewEvent);

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

        verify(modelS).setDataVector(any(Vector.class));

    }

    @Mock
    TextFileWriter<Vector> writer;


    @Test
    void shouldSaveToFile() throws IOException {
        when(view.getJMenuBar()).thenReturn(new JMenuBar());
        controller = new Controller(view, model, modelA, modelS);

        PropertyChangeListener listener = controller.new ListenForFileSave(writer);

        when(modelA.getDataVector())
                .thenReturn(DataPointsFlattering.flatten(exampleData, exampleData));

        listener.propertyChange(new PropertyChangeEvent(view,
                Menu.SAVE_TO_FILE, null, "src\\test\\java\\viewtest\\random.txt"));

        verify(modelA).getDataVector();
        verify(writer).saveToFile(eq("src\\test\\java\\viewtest\\random.txt"),
                any(Vector.class));

    }

    @Mock
    Plot plot;

    @Test
    void shouldCreatePlot() {
        when(view.getJMenuBar()).thenReturn(new JMenuBar());
        controller = new Controller(view, model, modelA, modelS);

        ModelObserver listener = controller.new ListenForCreatePlot(plot);
        //new PropertyChangeEvent(view, Menu.CREATE_PLOT, null, null)
        listener.update(ModelEvent.DATA_IN_PROCESS);

        // Sets domain range
        verify(plot).setDomainRange(any());
        verify(plot).addSeries(eq("Real"), any());
        verify(plot).addSeries(eq("Predicted"), any());
    }

}
