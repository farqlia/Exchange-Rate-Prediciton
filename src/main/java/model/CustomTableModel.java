package model;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public abstract class CustomTableModel<E> extends AbstractTableModel {

    private final List<E> listOfRows;
    private final List<String> columns;
    private final String name;

    public CustomTableModel(List<String> columns, String name){
        this.columns = columns;
        listOfRows = new ArrayList<>();
        this.name = name;
    }

    @Override
    public int getRowCount() {
        return listOfRows.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public String getColumnName(int columnIndex){
        return columns.get(columnIndex);
    }

    public void addRow(E row){
        listOfRows.add(row);
        fireTableDataChanged();
    }

    public void deleteRows(){
        listOfRows.clear();
        fireTableDataChanged();
    }

    public E getRow(int rowIndex){
        return listOfRows.get(rowIndex);
    }

    public List<E> getListOfRows(){
        return new ArrayList<>(listOfRows);
    }

    public List<String> getColumnNames(){
        return new ArrayList<>(columns);
    }

    @Override
    public String toString(){
        return name;
    }
}
