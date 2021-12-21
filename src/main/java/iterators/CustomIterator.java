package iterators;

import datasciencealgorithms.utils.Point;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public abstract class CustomIterator implements Iterator<Point<LocalDate>> {

    protected List<Point<LocalDate>> data;
    protected LocalDate startDate,
                      endDate;
    protected int currIndex;

    public CustomIterator(List<Point<LocalDate>> data, LocalDate startDate, LocalDate endDate) {
        this.data = data;
        this.startDate = startDate;
        this.endDate = endDate;
        findStartIndex(startDate);
    }

    public CustomIterator(List<Point<LocalDate>> data, LocalDate startDate){
        this(data, startDate, LocalDate.now());
    }

    public CustomIterator(List<Point<LocalDate>> data){
        this(data, data.get(0).getX(), LocalDate.now());
    }

    public void findStartIndex(LocalDate dateToStartFrom){
        currIndex = Collections.binarySearch(
                // We look for an index based on a date: if it's not found, then based on return
                // value we can compute the index of the next biggest value
                data.stream().map(Point::getX).collect(Collectors.toList()),
                dateToStartFrom);

        if (currIndex < 0){
            currIndex = -currIndex - 1;
        }
    }

}
