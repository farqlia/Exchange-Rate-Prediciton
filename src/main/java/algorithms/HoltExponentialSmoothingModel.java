package algorithms;

import datasciencealgorithms.utils.UtilityMethods;
import datasciencealgorithms.utils.point.Point;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class HoltExponentialSmoothingModel implements Algorithm{

    BlockingQueue<Point> queue;
    BigDecimal alpha;
    BigDecimal beta;

    public HoltExponentialSmoothingModel(BlockingQueue<Point> queue, BigDecimal alpha, BigDecimal beta) {
        this.queue = queue;
        this.alpha = alpha;
        this.beta = beta;
    }

    @Override
    public void forecastValuesForDates(List<Point> realData, LocalDate startDate, LocalDate endDate) throws InterruptedException {

        int startIndex = UtilityMethods.findIndexOfDate(startDate, realData);
        int endIndex = UtilityMethods.findIndexOfDate(endDate, realData);

        // Assumes that passed data is big enough
        BigDecimal currentVal, previousVal = realData.get(startIndex - 1).getY();
        BigDecimal trendIncr = realData.get(startIndex).getY()
                .subtract(realData.get(startIndex - 1).getY());

        for (int i = startIndex; i <= endIndex; i++){

            currentVal = realData.get(i).getY().multiply(alpha)
                    .add(BigDecimal.ONE.subtract(alpha).multiply(previousVal.add(trendIncr)));

            queue.put(new Point(realData.get(i).getX(),
                    previousVal.add(trendIncr)));
            SleepingThread.sleep();

            // S/t/ = beta(F/t/ - F/t-1/) + (1 - beta)S/t-1
            trendIncr = beta.multiply(currentVal.subtract(previousVal))
                    .add(BigDecimal.ONE.subtract(beta).multiply(trendIncr));

            previousVal = currentVal;
        }
        queue.put(Point.EMPTY_POINT);
    }
}
