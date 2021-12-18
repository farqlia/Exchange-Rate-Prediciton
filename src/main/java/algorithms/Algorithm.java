package algorithms;

import datasciencealgorithms.utils.Point;

import java.time.LocalDate;
import java.util.List;

public interface Algorithm {
    List<Point<LocalDate>> forecastValuesForDates(LocalDate startDate, LocalDate endDate);


}
