package datagenerator;

import datasciencealgorithms.utils.Point;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataGenerator {

    public List<Point<LocalDate>> generateDataWithTrend(int dataset, BigDecimal trend){

        List<Point<LocalDate>> points = new ArrayList<>();
        // The first point in our list is the oldest point
        LocalDate firstDay = LocalDate.now().minusDays(dataset - 1);

        points.add(new Point<>(firstDay, BigDecimal.ONE));
        for (int i = 1; i < dataset; i++) {
            // Time interval equals one day
            // General expression is F(t) = 2 * F(t - 1) - F(t - 2)
            points.add(new Point<>(firstDay.plusDays(i), points.get(i - 1).getY().add(trend)));
        }
        return points;
    }

}
