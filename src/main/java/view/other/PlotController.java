package view.other;

import model.CustomTableModel;
import model.ResultsTableModel;
import org.jfree.data.time.Day;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.time.LocalDate;

public class PlotController implements TableModelListener {

    protected Plot plot;
    protected CustomTableModel<ResultsTableModel.Row> model;
    private String seriesName;
    public PlotController(Plot plot, CustomTableModel<ResultsTableModel.Row> model,
                          String seriesName){
        this.plot = plot;
        this.model = model;
        model.addTableModelListener(this);
        this.seriesName = seriesName;
    }

    protected Day[] getDates(){
        Day[] arr = new Day[model.getRowCount()];
        for (int i = 0; i < model.getRowCount(); i++){
            LocalDate d = model.getRow(i).getDate();
            arr[i] = new Day(d.getDayOfMonth(), d.getMonthValue(), d.getYear());
        }
        return arr;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        updatePlot();
    }

    private void updatePlot() {
        plot.setDomainRange(getDates());

        plot.addSeries("Real", model.getListOfRows().stream()
                .mapToDouble(x -> x.getReal().doubleValue()).toArray());

        plot.addSeries(seriesName, model.getListOfRows().stream()
                .mapToDouble(x -> x.getPredicted().doubleValue()).toArray());
    }

}
