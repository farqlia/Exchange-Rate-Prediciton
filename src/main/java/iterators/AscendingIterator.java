package iterators;

import datasciencealgorithms.utils.Point;

import java.time.LocalDate;
import java.util.List;

public class AscendingIterator extends CustomIterator {

    public AscendingIterator(List<Point<LocalDate>> data, LocalDate startDate, LocalDate endDate){
        super(data, startDate, endDate);
        findStartIndex(startDate);
    }

    public AscendingIterator(List<Point<LocalDate>> data, LocalDate startDate){
        this(data, startDate, LocalDate.now());
    }

    public AscendingIterator(List<Point<LocalDate>> data){
        super(data);
    }

    @Override
    public boolean hasNext() {
        return currIndex < data.size()
                && data.get(currIndex).getX().compareTo(endDate) <= 0;
    }

    // TODO : think about returning some other value than null, so that special cases are already handled
    @Override
    public Point<LocalDate> next() {
        if (hasNext()) return data.get(currIndex++);
        return data.get(data.size() - 1);
    }

}
