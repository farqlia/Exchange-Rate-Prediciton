package datagenerator;

import datasciencealgorithms.utils.point.Point;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataGenerator {

    private static final DataGenerator dG = new DataGenerator();

    private DataGenerator(){}

    public static DataGenerator getInstance(){
        return dG;
    }

    public List<Point<LocalDate>> generateDataWithTrend(int dataset, BigDecimal initialValue, BigDecimal trend){
        return generateDataWithTrend(dataset, initialValue, trend, 0);
    }

    public List<BigDecimal> generateSimpleData(int dataset, BigDecimal initialValue, BigDecimal trend, double error){
        return generateDataWithTrend(dataset, initialValue, trend, error)
                .stream().map(Point::getY).collect(Collectors.toList());
    }

    public List<BigDecimal> generateSimpleData(int dataset, BigDecimal initialValue, BigDecimal trend){
        return generateSimpleData(dataset, initialValue, trend, 0);
    }

    public List<Point<LocalDate>> generateDataWithTrend(int dataset, BigDecimal initialValue, BigDecimal trend, double error){

        List<Point<LocalDate>> points = new ArrayList<>();
        // The first point in our list is the oldest point
        LocalDate firstDay = LocalDate.now().minusDays(dataset - 1);

        points.add(new Point<>(firstDay, initialValue));
        for (int i = 1; i < dataset; i++) {
            // Time interval equals one day
            // General expression is F(t) = 2 * F(t - 1) - F(t - 2)
            points.add(new Point<>(firstDay.plusDays(i),
                    points.get(i - 1).getY().add(trend).add(BigDecimal.valueOf((Math.random() - .5) * error)))
            );
        }
        return points;
    }



}
