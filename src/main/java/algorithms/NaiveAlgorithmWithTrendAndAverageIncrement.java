package algorithms;

import datasciencealgorithms.utils.point.Point;
import datasciencealgorithms.utils.UtilityMethods;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class NaiveAlgorithmWithTrendAndAverageIncrement implements Algorithm{

    RoundingMode roundingMode = RoundingMode.HALF_UP;
    private final int lookbackPeriod;
    private final BlockingQueue<Point> queue;

    public NaiveAlgorithmWithTrendAndAverageIncrement(BlockingQueue<Point> queue, int lookbackPeriod){
        this.lookbackPeriod = lookbackPeriod;
        this.queue = queue;
    }

    @Override
    public void forecastValuesForDates(List<Point> realData, LocalDate startDate, LocalDate endDate) throws InterruptedException{

        int startIndex = UtilityMethods.findIndexOfDate(startDate, realData);
        int endIndex = UtilityMethods.findIndexOfDate(endDate, realData);

        for (int i = startIndex; i <= endIndex; i++){

            BigDecimal sumForAverage = BigDecimal.ZERO;

            for (int j = i - 2; j > (i - 2 - lookbackPeriod); j--){
                sumForAverage = sumForAverage.add(
                        // min is not minus :)
                        realData.get(j).getY().subtract(realData.get(j - 1).getY()));
            }

            queue.put(new Point(realData.get(i).getX(),
                    realData.get(i - 1).getY()
                            .add(sumForAverage.divide(new BigDecimal(lookbackPeriod), roundingMode))));
            SleepingThread.sleep();

        }
        queue.put(Point.EMPTY_POINT);
    }
}
