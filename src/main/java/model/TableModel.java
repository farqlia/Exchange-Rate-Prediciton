package model;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class TableModel extends DefaultTableModel {

    public TableModel(Vector columnNames, int rows){
        super(columnNames, rows);
    }
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }
}
