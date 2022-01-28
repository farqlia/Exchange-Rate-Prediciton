package algorithms;

import datasciencealgorithms.utils.UtilityMethods;
import datasciencealgorithms.utils.point.Point;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class FutureForecastAlgorithm implements Algorithm {

    BlockingQueue<Point> queue;
    BigDecimal alpha;
    BigDecimal beta;

    public FutureForecastAlgorithm(BlockingQueue<Point> queue, BigDecimal alpha, BigDecimal beta) {
        this.queue = queue;
        this.alpha = new BigDecimal("0.4");
        this.beta = new BigDecimal("0.4");
    }

    @Override
    public void forecastValuesForDates(List<Point> realData, LocalDate startDate, LocalDate endDate) throws InterruptedException {

        int startIndex = UtilityMethods.findIndexOfDate(startDate, realData);
        int endIndex = UtilityMethods.findIndexOfDate(endDate, realData);

        // Assumes that passed data is big enough
        BigDecimal currentVal, previousVal = realData.get(startIndex - 1).getY()
                , postPreviousVal = realData.get(startIndex - 2).getY();
        BigDecimal trendIncr = realData.get(startIndex).getY().subtract(realData.get(startIndex - 1).getY());

        for (int i = startIndex; i <= endIndex; i++){

            queue.put(new Point(realData.get(i).getX(),
                    previousVal.add(trendIncr)));
            SleepingThread.sleep();

            currentVal = previousVal.multiply(alpha)
                    .add(BigDecimal.ONE.subtract(alpha).multiply(postPreviousVal.add(trendIncr)));

            // S/t/ = beta(F/t/ - F/t-1/) + (1 - beta)S/t-1
            //trendIncr = beta.multiply(currentVal.subtract(previousVal))
            //        .add(BigDecimal.ONE.subtract(beta).multiply(trendIncr));

            postPreviousVal = previousVal;
            previousVal = currentVal;
        }
        queue.put(Point.EMPTY_POINT);

    }
}
