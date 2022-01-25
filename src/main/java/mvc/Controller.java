package mvc;

import model.*;
import view.IO.FileSaveHandler;
import view.IO.FileTypes;
import view.other.*;
import view.view.*;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;

public class Controller {

    Model modelExPost, modelExAnte;
    AbstractView view;
    JTable tableExPost;
    CustomTableModel<ResultsTableModel.Row> tableModelExPost;
    CustomTableModel<StatisticsTableModel.Row> tableModelStatistics;
    CustomTableModel<ResultsTableModel.Row> tableModelAntePost;
    Plot plot;

    public Controller(AbstractView view,
                      JTable tableExPost,
                      Model modelExPost,
                      Model modelExAnte,
                      CustomTableModel<ResultsTableModel.Row> tableModelExPost,
                      CustomTableModel<StatisticsTableModel.Row> tableModelStatistics,
                      CustomTableModel<ResultsTableModel.Row> tableModelAntePost){

        this.view = view;
        this.modelExPost = modelExPost;
        this.modelExAnte = modelExAnte;
        this.tableModelExPost = tableModelExPost;
        this.tableModelAntePost = tableModelAntePost;
        this.tableModelStatistics = tableModelStatistics;
        this.tableExPost = tableExPost;
        plot = new Plot();

        PlotController exPostPlotCon = new PlotController(plot, tableModelExPost, "Predicted");
        PlotController antePostPlotCon = new PlotController(plot, tableModelAntePost, "Future Predictions");

        view.registerObserver(ViewEventType.EX_POST, new HandleExPostPredictions());
        view.registerObserver(ViewEventType.EX_ANTE, new HandleExAntePredictions());
        view.registerObserver(ViewEventType.EX_POST, new HandlePlotTitle());

        ModelObserver observer = new HandleViewAction();
        modelExPost.registerObserver(observer);
        modelExAnte.registerObserver(observer);

        FileSaveHandler handler = new FileSaveHandler(tableModelExPost);

        view.getJMenuBar().addPropertyChangeListener(FileTypes.TEXT.toString(), handler);
        view.getJMenuBar().addPropertyChangeListener(FileTypes.JSON.toString(), handler);

        HandlePlotVisibility listener = new HandlePlotVisibility();

        view.getJMenuBar().addPropertyChangeListener(Plot.CREATE_PLOT_ACTION,
                listener);

        HandleTableRendering tableRenderingHandler = new HandleTableRendering();
        view.getJMenuBar().addPropertyChangeListener(Menu.SET_RENDERING,
                tableRenderingHandler);
        view.registerObserver(ViewEventType.EX_POST, tableRenderingHandler);

        view.registerObserver(ViewEventType.EX_POST, listener);
        view.registerObserver(ViewEventType.EX_ANTE, listener);
        view.registerObserver(ViewEventType.EX_POST, handler);

    }

    public class HandleExPostPredictions implements ViewObserver{

        private ListenForView viewListener = new ListenForView(modelExPost);

        @Override
        public void update(ViewEvent e) {
            tableModelExPost.deleteRows();
            tableModelAntePost.deleteRows();
            tableModelStatistics.deleteRows();

            viewListener.update(e);
        }
    }

    public class HandleExAntePredictions implements ViewObserver{

        private ListenForView viewListener = new ListenForViewExAnte(tableModelExPost,
                modelExAnte);

        @Override
        public void update(ViewEvent e) {
            tableModelAntePost.deleteRows();

            viewListener.update(e);
        }
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

    public class HandleTableRendering implements PropertyChangeListener, ViewObserver{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            tableExPost.setDefaultRenderer(BigDecimal.class,
                    new NumberCellRenderer(tableModelStatistics.getRow(2).getValue(),
                            tableModelExPost));
            tableExPost.repaint();
        }

        @Override
        public void update(ViewEvent e) {
            tableExPost.setDefaultRenderer(BigDecimal.class, null);
        }
    }

    public class HandlePlotVisibility implements PropertyChangeListener, ViewObserver {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            // Clear previous results
            plot.setVisible(true);
        }

        @Override
        public void update(ViewEvent e) {
            plot.setVisible(false);
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