package algorithms;

import datasciencealgorithms.utils.point.Point;

import java.time.LocalDate;
import java.util.List;

public interface Algorithm {

    void forecastValuesForDates(List<Point> realData,
                                                  LocalDate startDate, LocalDate endDate) throws InterruptedException;

}
