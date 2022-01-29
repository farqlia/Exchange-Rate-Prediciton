package view.view;

import datasciencealgorithms.utils.UtilityMethods;
import exchangerateclass.ExchangeRate;
import model.CustomTableModel;
import model.ResultsTableModel;
import mvc.Model;

import java.time.LocalDate;
import java.util.List;

public class ListenForViewExAnte extends ListenForView {

    private CustomTableModel<ResultsTableModel.Row> tableModel;

    public ListenForViewExAnte(CustomTableModel<ResultsTableModel.Row> tableModel,
                               Model model){
        super(model);
        this.tableModel = tableModel;
    }

    protected void formatPoints(List<ExchangeRate> exchangeRatesList){

        super.formatPoints(exchangeRatesList);
        System.out.println(realDataPoints.get(0).getX());

        int row = 3;
        // Find penultimate date in table's data
        LocalDate startDate = tableModel.getRow(tableModel.getRowCount() - row).getDate();

        int index = UtilityMethods.findIndexOfDate(startDate, realDataPoints);

        //
        for (int i = 0; i < row; i++){
            realDataPoints.get(index++).setY(tableModel.getRow(tableModel.getRowCount() - row + i).getPredicted());
        }
    }
}
