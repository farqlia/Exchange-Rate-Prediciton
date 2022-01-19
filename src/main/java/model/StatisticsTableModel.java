package model;

import java.math.BigDecimal;
import java.util.List;

public class StatisticsTableModel extends CustomTableModel<StatisticsTableModel.Row>{

    public StatisticsTableModel(List<String> columnNames, String name){
        super(columnNames, name);
    }
    @Override
    public Class<?> getColumnClass(int columnIndex){
        if (columnIndex == 0) {
            return String.class;
        } else if (columnIndex == 1){
            return BigDecimal.class;
        }
        return Object.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0){
            return getRow(rowIndex).name;
        } else if (columnIndex == 1){
            return getRow(rowIndex).value;
        }
        return new Object();
    }

    public static class Row{
        String name;
        BigDecimal value;

        public Row(String name, BigDecimal value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public BigDecimal getValue() {
            return value;
        }
    }

}
