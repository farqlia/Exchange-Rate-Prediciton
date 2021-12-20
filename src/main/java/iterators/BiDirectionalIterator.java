package iterators;

import datasciencealgorithms.utils.Point;

import java.time.LocalDate;
import java.util.List;

// The idea is that this iterator let's you iterate backwards, where call to hasNext()
// method goes to the next point after the point you started going backwards
public class BiDirectionalIterator extends CustomIterator{

    private int indexMovingBackward;

    // We get rid of step variable, since the number of steps is now number of calls to the
    // 'next()' method between two next call to the 'hasNext()'
    public BiDirectionalIterator(List<Point<LocalDate>> data, LocalDate startDate, LocalDate endDate){
        super(data, startDate, endDate);
        // We start out of actual range because this index
        // is incremented by one with the first call to the hasNext() method
        indexMovingBackward = --currIndex;
    }

    public BiDirectionalIterator(List<Point<LocalDate>> data, LocalDate startDate){
        this(data, startDate, LocalDate.now());
    }

    @Override
    public boolean hasNext() {
        indexMovingBackward = ++currIndex;
        return currIndex < data.size()
                && data.get(currIndex).getX().compareTo(endDate) <= 0;
    }

    @Override
    public Point<LocalDate> next() {

        if (indexMovingBackward == 0){
            return data.get(0);
        }
        return data.get(indexMovingBackward--);
    }

}
