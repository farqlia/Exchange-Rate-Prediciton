package model;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class TableModel extends DefaultTableModel {

    String name;
    public TableModel(Vector columnNames, int rows, String name){
        super(columnNames, rows);
        this.name = name;
    }
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public String toString(){
        return name;
    }
}
