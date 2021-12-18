package iterators;

import datasciencealgorithms.utils.Point;

import java.time.LocalDate;
import java.util.List;

public class BiDirectionalIterator extends CustomIterator{

    private int indexMovingBackward;
    private int step;

    public BiDirectionalIterator(List<Point<LocalDate>> data, LocalDate startDate, LocalDate endDate, int step){
        super(data, startDate, endDate);
        this.step = step;
        indexMovingBackward = currIndex;
    }

    public BiDirectionalIterator(List<Point<LocalDate>> data, LocalDate startDate, int step){
        this(data, startDate, LocalDate.now(), step);
    }

    @Override
    public boolean hasNext() {
        return currIndex < data.size()
                && data.get(currIndex).getX().compareTo(endDate) <= 0;
    }

    @Override
    public Point<LocalDate> next() {
        // We have two possible scenarios: either we move backwards,
        // if there are some steps we can do, or we jump to the next
        // point that follows the index 'currIndex'
        if (indexMovingBackward >= currIndex - step){
            if (indexMovingBackward <= 0){
                indexMovingBackward--;
                return data.get(0);
            }
            return data.get(indexMovingBackward--);
        }
        indexMovingBackward = currIndex++;

        return data.get(currIndex);
    }
}
