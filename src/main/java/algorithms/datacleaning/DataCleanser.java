package algorithms.datacleaning;

import datasciencealgorithms.utils.point.Point;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ListIterator;

public class DataCleanser {

    public void performCleaning(List<Point> dataToClean){

        assert !dataToClean.isEmpty();

        Point prevPoint = dataToClean.get(0);
        Point currElement;
        ListIterator<Point> itr = dataToClean.listIterator();
        itr.next();

        while (itr.hasNext()){

            currElement = itr.next();
            itr.previous();
            itr.previous();
            prevPoint = itr.next();

            if (ChronoUnit.DAYS.between(prevPoint.getX(), currElement.getX()) > 1) {
                itr.add(new Point(prevPoint.getX().plusDays(1),
                        prevPoint.getY()));

                itr.previous();
            }
            // Goes past element returned by call to previous
            itr.next();

        }
    }

}
