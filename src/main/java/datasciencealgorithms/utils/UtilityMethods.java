package datasciencealgorithms.utils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UtilityMethods {

    public static int findIndexOfDate(LocalDate date, List<Point<LocalDate>> data){
        int currIndex = Collections.binarySearch(
                // We look for an index based on a date: if it's not found, then based on return
                // value we can compute the index of the next biggest value
                data.stream().map(Point::getX).collect(Collectors.toList()),
                date);

        if (currIndex < 0){
            currIndex = -currIndex - 1;
        }
        return currIndex;
    }

}
