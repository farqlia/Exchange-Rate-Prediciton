package algorithms;

import datasciencealgorithms.utils.Point;

import java.time.LocalDate;
import java.util.List;

public interface Algorithm {

    List<Point<LocalDate>> forecastValuesForDates(List<Point<LocalDate>> actualData,
                                                  LocalDate startDate, LocalDate endDate);
}
