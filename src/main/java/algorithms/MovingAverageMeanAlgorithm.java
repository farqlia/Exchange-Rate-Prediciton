package algorithms;

import datasciencealgorithms.utils.UtilityMethods;
import datasciencealgorithms.utils.point.Point;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class MovingAverageMeanAlgorithm implements Algorithm{

    // Initial value of k
    private final int lookbackPeriod;
    // How many k values are considered
    private final BlockingQueue<Point> queue;

    public MovingAverageMeanAlgorithm(BlockingQueue<Point> queue, int lookbackPeriod){
        this.lookbackPeriod = lookbackPeriod;
        this.queue = queue;
    }

    @Override
    public void forecastValuesForDates(List<Point> realData, LocalDate startDate, LocalDate endDate) throws InterruptedException {

        int startIndex = UtilityMethods.findIndexOfDate(startDate, realData);
        int endIndex = UtilityMethods.findIndexOfDate(endDate, realData);

        for (int i = startIndex; i <= endIndex; i++){
            // Computes average of k-recent values
            BigDecimal sumForAverage = BigDecimal.ZERO;
            for (int j = i - 1; (j > 0 && j > (i - lookbackPeriod - 1)); j--){
                sumForAverage = sumForAverage.add(realData.get(j).getY());
            }
            queue.put(new Point(realData.get(i).getX(),
                    sumForAverage.divide(new BigDecimal(lookbackPeriod), RoundingMode.HALF_UP)));
            SleepingThread.sleep();
        }
        queue.put(Point.EMPTY_POINT);
    }

}
