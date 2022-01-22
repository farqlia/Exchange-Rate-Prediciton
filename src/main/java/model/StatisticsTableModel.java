package model;

import datasciencealgorithms.utils.point.Point;
import mathlibraries.Statistics;
import mvc.Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StatisticsTableModel extends CustomTableModel<StatisticsTableModel.Row>
        implements ModelObserver{

    private List<Point> predictedData = new ArrayList<>();
    private Model dataModel;
    public StatisticsTableModel(List<String> columnNames, String name, Model dataModel){
        super(columnNames, name);
        this.dataModel = dataModel;
        dataModel.registerObserver(this);
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

    @Override
    public void update(ModelEvent event) {
        if (event == ModelEvent.DATA_IN_PROCESS){
            predictedData.addAll(dataModel.getDataChunk());
        } else if (event == ModelEvent.DATA_PROCESS_FINISHED){
            predictedData.addAll(dataModel.getDataChunk());

            for (Statistics s : Statistics.values()){
                addRow(new StatisticsTableModel.Row(s.toString(),
                        s.apply(predictedData, dataModel.getRealData())));
            }

            predictedData.clear();

        }
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
