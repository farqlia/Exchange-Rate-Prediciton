package model;

import datasciencealgorithms.utils.UtilityMethods;
import datasciencealgorithms.utils.point.Point;
import mathlibraries.TimeSeriesScienceLibrary;
import mvc.Model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ResultsTableModel extends CustomTableModel<ResultsTableModel.Row>
        implements ModelObserver {

    private Model dataModel;
    public ResultsTableModel(List<String> columnNames, String name, Model dataModel){
        super(columnNames, name);
        this.dataModel = dataModel;
        dataModel.registerObserver(this);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex){
        if (columnIndex == 0) {
            return LocalDate.class;
        }
        return BigDecimal.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0 : return getRow(rowIndex).date;
            case 1 : return getRow(rowIndex).real;
            case 2 : return getRow(rowIndex).predicted;
            case 3 : return getRow(rowIndex).error;
            case 4 : return getRow(rowIndex).percentageError;
            default: return new Object();
        }
    }

    @Override
    public void update(ModelEvent event) {
        if (event == ModelEvent.DATA_IN_PROCESS){
            BigDecimal[] absoluteError = TimeSeriesScienceLibrary
                    .calculateAbsoluteError(dataModel.getDataChunk(), dataModel.getRealData());

            BigDecimal[] absolutePercentageError = TimeSeriesScienceLibrary
                    .calculatePercentageError(dataModel.getDataChunk(), dataModel.getRealData());

            int index = UtilityMethods.findIndexOfDate(dataModel.getDataChunk().get(0).getX(),
                    dataModel.getRealData());


            for (int i = 0; i < dataModel.getDataChunk().size() && index >= 0; i++){
                addRow(new ResultsTableModel.Row(dataModel.getRealData().get(index),dataModel.getDataChunk().get(i),
                        absoluteError[i], absolutePercentageError[i]));
                index++;
            }
        }
    }

    public static class Row {
        LocalDate date;
        BigDecimal real, predicted, error, percentageError;

        public Row(Point realPoint, Point predictedPoint, BigDecimal error, BigDecimal percentageError) {
            this.date = realPoint.getX();
            this.real = realPoint.getY();
            this.predicted = predictedPoint.getY();
            this.error = error;
            this.percentageError = percentageError;
        }

        public LocalDate getDate() {
            return date;
        }

        public BigDecimal getReal() {
            return real;
        }

        public BigDecimal getPredicted() {
            return predicted;
        }

        public BigDecimal getError() {
            return error;
        }

        public BigDecimal getPercentageError() {
            return percentageError;
        }
    }
}
