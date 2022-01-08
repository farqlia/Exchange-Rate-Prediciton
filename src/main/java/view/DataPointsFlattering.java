package view;

import datasciencealgorithms.utils.point.Point;

import java.time.LocalDate;
import java.util.List;
import java.util.Vector;

public class DataPointsFlattering implements DataFlattering<Point<LocalDate>> {

    private static final DataPointsFlattering instance = new DataPointsFlattering();

    private DataPointsFlattering(){}

    public static DataPointsFlattering getInstance(){
        return instance;
    }

    @SafeVarargs
    @Override
    public final Object[][] flatten(List<Point<LocalDate>>... data) {

        assert data.length == 2;
        assert !data[0].isEmpty() && !data[1].isEmpty();

        List<Point<LocalDate>> l1 = data[0];
        List<Point<LocalDate>> l2 = data[1];

        Object[][] flatData = new Object[l1.size()][3];

        for (int i = 0; i < l1.size(); i++){
            flatData[i][0] = l1.get(i).getX();
            flatData[i][1] = l1.get(i).getY();
            flatData[i][2] = l2.get(i).getY();
        }

        return flatData;
    }
}
