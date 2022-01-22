package mvc;

import model.*;
import view.IO.FileSaveHandler;
import view.IO.FileTypes;
import view.other.*;
import view.view.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Controller {

    Model modelExPost, modelExAnte;
    AbstractView view;
    Plot plot;

    public Controller(AbstractView view, Model modelExPost,
                      Model modelExAnte,
                      CustomTableModel<ResultsTableModel.Row> tableModelExPost,
                      CustomTableModel<ResultsTableModel.Row> tableModelAntePost){

        this.view = view;
        this.modelExPost = modelExPost;
        this.modelExAnte = modelExAnte;
        plot = new Plot();

        PlotControllerExPost exPostPlotCon = new PlotControllerExPost(plot, tableModelExPost);
        AbstractPlotController antePostPlotCon = new PlotControllerExAnte(plot, tableModelAntePost);

        this.view.registerObserver(ViewEventType.EX_POST, new ListenForView(modelExPost));
        this.view.registerObserver(ViewEventType.EX_POST, new HandlePlotTitle());

        modelExPost.registerObserver(new HandleViewAction());

        FileSaveHandler handler = new FileSaveHandler(tableModelExPost);

        view.getJMenuBar().addPropertyChangeListener(FileTypes.TEXT.toString(),handler);
        view.getJMenuBar().addPropertyChangeListener(FileTypes.JSON.toString(), handler);
        view.getJMenuBar().addPropertyChangeListener(Plot.CREATE_PLOT_ACTION,
                new HandlePlotVisibility());

        view.registerObserver(ViewEventType.EX_POST, handler);

    }

    public class HandleViewAction implements ModelObserver{

        @Override
        public void update(ModelEvent event) {
            if (event == ModelEvent.DATA_PROCESS_STARTED){
                view.disableActions();
            } else if (event == ModelEvent.DATA_PROCESS_FINISHED){
                view.enableActions();
            }
        }
    }

    public class HandlePlotVisibility implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            // Clear previous results
            plot.setVisible(true);
        }
    }

    public class HandlePlotTitle implements ViewObserver{

        @Override
        public void update(ViewEvent e) {
            plot.setVisible(false);
            plot.clear();
            plot.setTitle(e.getCurrencyCode() + ", " + e.getStartDate() + ":" + e.getEndDate());
            // real-time update plot

        }
    }

}