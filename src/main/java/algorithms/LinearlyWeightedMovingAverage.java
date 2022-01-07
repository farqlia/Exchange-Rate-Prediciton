package algorithms;

import datasciencealgorithms.utils.UtilityMethods;
import datasciencealgorithms.utils.point.Point;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class LinearlyWeightedMovingAverage implements Algorithm{

    static RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    @Override
    public List<Point<LocalDate>> forecastValuesForDates(List<Point<LocalDate>> expectedData, LocalDate startDate,
                                                         LocalDate endDate) {

        int startIndex = UtilityMethods.findIndexOfDate(startDate, expectedData);
        int endIndex = UtilityMethods.findIndexOfDate(endDate, expectedData);

        int period = (int) ChronoUnit.DAYS.between(expectedData.get(0).getX(), startDate);
        Weight weight = new Weight(period);
        List<Point<LocalDate>> actualData = new ArrayList<>();

        for (int i = startIndex; i <= endIndex; i++){

            BigDecimal sum = BigDecimal.ZERO;
            for (int j = i; j > 0 && j > (i - period); j--){
                // Sum the previous values * their corresponding weights
                sum = sum.add(expectedData.get(j).getY().multiply(weight.nextWeight()));
            }

            // Predicted value is sum / sum of weights
            actualData.add(new Point<>(expectedData.get(i).getX(),
                    sum.divide(weight.sumOfWeights(), ROUNDING_MODE)));

            // Set weight to the original state for the next point of data
            weight.resetWeight();

        }
        return actualData;
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
