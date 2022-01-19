package model;

import datasciencealgorithms.utils.point.Point;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ResultsTableModel extends CustomTableModel<ResultsTableModel.Row>{

    public ResultsTableModel(List<String> columnNames, String name){
        super(columnNames, name);
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
