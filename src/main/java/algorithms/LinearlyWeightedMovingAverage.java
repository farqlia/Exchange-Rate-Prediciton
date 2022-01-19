package algorithms;

import algorithms.algorithmsparameters.AlgorithmArguments;
import datasciencealgorithms.utils.UtilityMethods;
import datasciencealgorithms.utils.point.Point;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class LinearlyWeightedMovingAverage implements Algorithm{

    static RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private final int lookbackPeriod;
    private final BlockingQueue<Point> queue;

    public LinearlyWeightedMovingAverage(BlockingQueue<Point> queue,Number lookbackPeriod){
        this.lookbackPeriod = lookbackPeriod.intValue();
        this.queue = queue;
    }

    @Override
    public void forecastValuesForDates(List<Point> realData, LocalDate startDate,
                                                         LocalDate endDate) throws InterruptedException {

        int startIndex = UtilityMethods.findIndexOfDate(startDate, realData);
        int endIndex = UtilityMethods.findIndexOfDate(endDate, realData);

        //int period = (int) ChronoUnit.DAYS.between(realData.get(0).getX(), startDate);
        Weight weight = new Weight(lookbackPeriod);

        for (int i = startIndex; i <= endIndex; i++){

            BigDecimal sum = BigDecimal.ZERO;
            for (int j = i - 1; j > 0 && j >= (i - lookbackPeriod); j--){
                // Sum the previous values * their corresponding weights
                sum = sum.add(realData.get(j).getY().multiply(weight.nextWeight()));
            }

            // Predicted value is sum / sum of weights
            queue.put(new Point(realData.get(i).getX(),
                    sum.divide(weight.sumOfWeights(), ROUNDING_MODE)));

            SleepingThread.sleep();

            // Set weight to the original state for the next point of data
            weight.resetWeight();

        }
        // Add 'poison' object to signal that no more points will be produced
        queue.put(Point.EMPTY_POINT);

    }

    public static class Weight{

        private BigDecimal i;
        private BigDecimal n;

        public Weight(int n) {
            this.n = new BigDecimal(n);
            i = this.n.add(BigDecimal.ONE);
        }

        public BigDecimal nextWeight(){
            return (i = i.subtract(BigDecimal.ONE));
        }

        // Sum of weight equals to the arithmetic sequence
        public BigDecimal sumOfWeights(){
            return n.add(i).multiply(n.subtract(i).add(BigDecimal.ONE))
                    .divide(new BigDecimal(2), ROUNDING_MODE);
        }

        public void resetWeight(){
            i = n.add(BigDecimal.ONE);
        }

    }

}
