package view.other;

import model.CustomTableModel;
import model.ResultsTableModel;
import org.jfree.data.time.Day;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.time.LocalDate;

public abstract class AbstractPlotController implements TableModelListener {

    protected Plot plot;
    protected CustomTableModel<ResultsTableModel.Row> model;
    public AbstractPlotController(Plot plot, CustomTableModel<ResultsTableModel.Row> model){
        this.plot = plot;
        this.model = model;
        model.addTableModelListener(this);
    }

    public abstract void updatePlot();

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

}
