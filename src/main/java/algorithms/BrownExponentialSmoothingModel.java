package algorithms;

import datasciencealgorithms.utils.UtilityMethods;
import datasciencealgorithms.utils.point.Point;
import mathlibraries.TimeSeriesScienceLibrary;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class BrownExponentialSmoothingModel implements Algorithm{

    private final BlockingQueue<Point> queue;
    private BigDecimal alpha;

    public BrownExponentialSmoothingModel(BlockingQueue<Point> queue, BigDecimal alpha) {
        this.queue = queue;
        this.alpha = alpha;
    }

    @Override
    public void forecastValuesForDates(List<Point> realData, LocalDate startDate, LocalDate endDate) throws InterruptedException {

        int startIndex = UtilityMethods.findIndexOfDate(startDate, realData);
        int endIndex = UtilityMethods.findIndexOfDate(endDate, realData);

        // First prediction is an average of three first points ()
        BigDecimal prevPrediction = TimeSeriesScienceLibrary.calculateAverage(realData.get(startIndex),
                realData.get(startIndex + 1), realData.get(startIndex + 2));

        queue.put(new Point(realData.get(startIndex).getX(),
                prevPrediction));

        SleepingThread.sleep();

        for (int i = startIndex + 1; i <= endIndex; i++){

            BigDecimal currPred = realData.get(i - 1).getY().multiply(alpha)
                    .add((BigDecimal.ONE.subtract(alpha)).multiply(prevPrediction));

            queue.put(new Point(realData.get(i).getX(),
                    currPred));
            SleepingThread.sleep();

            prevPrediction = currPred;
        }

    }
}
