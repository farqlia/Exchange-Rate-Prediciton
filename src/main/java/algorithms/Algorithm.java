package algorithms;

import datasciencealgorithms.utils.point.Point;

import java.time.LocalDate;
import java.util.List;

public interface Algorithm {

    List<Point> forecastValuesForDates(List<Point> expectedData,
                                                  LocalDate startDate, LocalDate endDate);

}
