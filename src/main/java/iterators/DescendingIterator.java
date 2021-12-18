package iterators;

import datasciencealgorithms.utils.Point;

import java.time.LocalDate;
import java.util.List;

public class DescendingIterator extends CustomIterator{

    // For descending iterator, startDate is the latest date and endDate is the
    // oldest date
    public DescendingIterator(List<Point<LocalDate>> data, LocalDate startDate, LocalDate endDate){
        super(data, startDate, endDate);
        findStartIndex(startDate);
    }

    public DescendingIterator(List<Point<LocalDate>> data, LocalDate startDate){
        this(data, startDate, data.get(0).getX());
    }

    @Override
    public boolean hasNext() {
        return currIndex >= 0 && data.get(currIndex).getX().compareTo(endDate) >= 0;
    }

    // TODO : think about returning some other value than null, so that special cases are already handled
    @Override
    public Point<LocalDate> next() {
        if (hasNext()) return data.get(currIndex--);
        return data.get(0);
    }
}