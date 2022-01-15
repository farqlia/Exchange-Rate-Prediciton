package algorithms;

import datasciencealgorithms.utils.point.Point;
import iterators.BiDirectionalIterator;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class NaiveAlgorithmWithTrend implements Algorithm{

    private final BlockingQueue<Point> queue;

    public NaiveAlgorithmWithTrend(BlockingQueue<Point> queue){
        this.queue = queue;
    }

    @Override
    public void forecastValuesForDates(List<Point> realData,
                                       LocalDate startDate, LocalDate endDate) throws InterruptedException{

        // Improvement: we have one iterator instead of two, and the binarySearch
        // internally used by CustomIterator to find startDate is used only once (comparing to
        // the previous version where it was used with each iteration in loop)
        Iterator<Point> outerItr = new BiDirectionalIterator(realData, startDate, endDate);

        while (outerItr.hasNext()) {
            Point currPoint = outerItr.next();
            BigDecimal previousValue = outerItr.next().getY();

            // Computes a predicted value
            queue.put(new Point(currPoint.getX(),
                    previousValue
                    .multiply(new BigDecimal("2"))
                    .subtract(outerItr.next().getY())));
            SleepingThread.sleep();
        }
        queue.put(Point.EMPTY_POINT);

    }


}
