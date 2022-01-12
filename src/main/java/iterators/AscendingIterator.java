package iterators;

import datasciencealgorithms.utils.point.Point;

import java.time.LocalDate;
import java.util.List;

public class AscendingIterator extends CustomIterator {

    public AscendingIterator(List<Point> data, LocalDate startDate, LocalDate endDate){
        super(data, startDate, endDate);
        findStartIndex(startDate);
    }

    public AscendingIterator(List<Point> data, LocalDate startDate){
        this(data, startDate, LocalDate.now());
    }

    public AscendingIterator(List<Point> data){
        super(data);
    }

    @Override
    public boolean hasNext() {
        return currIndex < data.size()
                && data.get(currIndex).getX().compareTo(endDate) <= 0;
    }

    @Override
    public Point next() {
        if (hasNext()) return data.get(currIndex++);
        return data.get(data.size() - 1);
    }

}
