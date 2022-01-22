package view.other;

import model.*;
import org.jfree.data.time.Day;
import view.view.AbstractView;
import view.view.ViewEvent;
import view.view.ViewObserver;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;

public class PlotControllerExPost extends AbstractPlotController {

    public PlotControllerExPost(Plot plot, CustomTableModel<ResultsTableModel.Row> model){
        super(plot, model);
    }

    public void updatePlot(){

        plot.setDomainRange(getDates());

        plot.addSeries("Real", model.getListOfRows().stream()
                .mapToDouble(x -> x.getReal().doubleValue()).toArray());

        plot.addSeries("Predicted", model.getListOfRows().stream()
                .mapToDouble(x -> x.getPredicted().doubleValue()).toArray());
    }

}
