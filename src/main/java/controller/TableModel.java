package controller;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class TableModel extends DefaultTableModel {

    Vector<String> columnNames;
    public TableModel(Vector<String> columnNames){
        super(columnNames, 0);
        this.columnNames = columnNames;
    }

    public void clear(){
        for (int i = getRowCount() - 1; i >= 0; i--){
            removeRow(i);
        }
    }

    public void setDataVector(Vector<Vector> data){
        setDataVector(data, columnNames);
    }

}
