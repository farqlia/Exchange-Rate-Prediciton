package view;

import model.CustomTableModel;
import model.ResultsTableModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberCellRenderer extends JLabel implements TableCellRenderer {

    int RGB = 255;
    private BigDecimal a = new BigDecimal(100);
    private BigDecimal averagePercError;
    private BigDecimal max, min;
    private Color[] colors;
    private CustomTableModel<ResultsTableModel.Row> model;
    private Font font = new Font("Roboto", Font.BOLD, 15);

    public NumberCellRenderer(BigDecimal averagePercError, CustomTableModel<ResultsTableModel.Row> model){
        this.model = model;
        max = findMax();
        min = findMin();
        this.averagePercError = averagePercError;
        colors = new Color[model.getRowCount()];
        // Computes color for a row based on column representing percentage error
        computeColors();
        setOpaque(true);

    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        setBackground(colors[row]);
        setFont(font);
        setText(value.toString());
        return this;
    }


    private void computeColors(){
        for (int row = 0; row < model.getRowCount(); row++){

            BigDecimal percentageError = model.getRow(row).getPercentageError();
            BigDecimal offset;
            int x;
            Color c = Color.WHITE;
            if (percentageError.compareTo(averagePercError) > 0){
                offset = percentageError.subtract(averagePercError).divide(max.subtract(averagePercError),
                        RoundingMode.HALF_UP).abs();
                x = RGB - offset.multiply(a).intValue();
                c = new Color(RGB, x, x);
            } else if (percentageError.compareTo(averagePercError) < 0){
                offset = percentageError.subtract(averagePercError).divide(averagePercError.subtract(min),
                        RoundingMode.HALF_UP).abs();
                x = RGB - offset.multiply(a).intValue();
                c = new Color(x, RGB, x);
            }

            colors[row] = c;
        }
    }

    private BigDecimal findMax(){

        return model.getListOfRows().stream()
                .map(ResultsTableModel.Row::getPercentageError)
                .max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);

    }

    private BigDecimal findMin(){

        return model.getListOfRows().stream()
                .map(ResultsTableModel.Row::getPercentageError)
                .min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);

    }



}
