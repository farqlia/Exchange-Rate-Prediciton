package view.other;

import model.CustomTableModel;
import model.ResultsTableModel;

public class PlotControllerExAnte extends AbstractPlotController {

    public PlotControllerExAnte(Plot plot, CustomTableModel<ResultsTableModel.Row> model){
        super(plot, model);
    }

    @Override
    public void updatePlot() {
        plot.setDomainRange(getDates());

        plot.addSeries("Real", model.getListOfRows().stream()
                .mapToDouble(x -> x.getReal().doubleValue()).toArray());

        plot.addSeries("Ante Predicted", model.getListOfRows().stream()
                .mapToDouble(x -> x.getPredicted().doubleValue()).toArray());
    }
}
