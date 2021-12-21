package algorithms;

import datasciencealgorithms.utils.Point;

import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Algorithm {

    private List<Point<LocalDate>> actualData;
    protected RoundingMode roundingMode = RoundingMode.HALF_EVEN;

    public abstract List<Point<LocalDate>> forecastValuesForDates(List<Point<LocalDate>> actualData,
                                                                  LocalDate startDate, LocalDate endDate);



}
