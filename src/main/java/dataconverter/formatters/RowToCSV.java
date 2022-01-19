package dataconverter.formatters;

import dataconverter.formatters.Formatter;
import model.ResultsTableModel;

import java.io.IOException;

public class RowToCSV implements Formatter<ResultsTableModel.Row> {
    @Override
    public String getAsCSVString(ResultsTableModel.Row o) {
        StringBuilder builder = new StringBuilder();
        builder.append(o.getDate()).append(delimiter)
                .append(o.getReal()).append(delimiter)
                .append(o.getPredicted()).append(delimiter)
                .append(o.getError()).append(delimiter)
                .append(o.getPercentageError());
        return builder.toString();
    }

    @Override
    public ResultsTableModel.Row parseFromCSVString(String stringToParse) throws IOException {
        return null;
    }
}
