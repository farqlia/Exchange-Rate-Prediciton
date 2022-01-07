package viewtest;

import algorithms.AlgorithmNames;
import controller.Controller;
import model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import view.View;
import view.ViewEvent;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ControllerTest {

    Controller controller;

    @Mock
    View view;
    @Mock
    Model model;
    ViewEvent viewEvent;

    @BeforeEach
    void setUp(){
        controller = new Controller(view, model);
        viewEvent = new ViewEvent(LocalDate.now().minusDays(10),
                LocalDate.now(), AlgorithmNames.MOVING_AVERAGE_MEAN_ALGORITHM,
                "EUR");
    }

    @Test
    void shouldInvokePredictMethodOnModel(){

        controller.new ListenForView().update(viewEvent);

        verify(model).setAlgorithm(any(AlgorithmNames.class));
        verify(model).predict(any(List.class), any(LocalDate.class),
                any(LocalDate.class));
    }

    @Test
    void shouldNtPredictMethodOnModel(){

        // Currency is invalid, so we can't be provided with response body
        ViewEvent viewEvent = new ViewEvent(LocalDate.now().minusDays(10),
                LocalDate.now(), AlgorithmNames.MOVING_AVERAGE_MEAN_ALGORITHM,
                "INVALID");

        controller.new ListenForView().update(viewEvent);

        verify(model, never()).setAlgorithm(any(AlgorithmNames.class));
        verify(model, never()).predict(any(List.class), any(LocalDate.class),
                any(LocalDate.class));
    }

    @Test
    void shouldInvokePredictMethodOnModelWithExpectedArguments(){

        controller.new ListenForView().update(viewEvent);

        verify(model).setAlgorithm(AlgorithmNames.MOVING_AVERAGE_MEAN_ALGORITHM);

        // If we use argument matchers, all arguments have to be provided by matchers
        verify(model).predict(any(List.class), eq(viewEvent.getStartDate()),
                eq(viewEvent.getEndDate()));
    }

    @Test
    void shouldInvokeUpdateTableOnView(){

        controller.new ListenForView().update(viewEvent);

        verify(view).updateTable(any(Object[][].class));

    }

}
