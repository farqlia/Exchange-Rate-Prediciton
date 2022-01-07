package view;

import javax.swing.table.DefaultTableModel;

public class View extends AbstractView {

    Object[][] data;
    String[] columnNames = {"Date", "Actual", "Forecasted"};
    // Just for the time of testing
    public DefaultTableModel table = new DefaultTableModel(data, columnNames);

    @Override
    public void updateTable(Object[][] data) {
        for (Object[] row : data){
            table.addRow(row);
        }
    }

    @Override
    public void updateTable(Object[][] data, Object[] columnNames) {

    }
}
