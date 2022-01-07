package mathlibraries;

import datasciencealgorithms.utils.UtilityMethods;
import datasciencealgorithms.utils.point.Point;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

public class TimeSeriesScienceLibrary {

    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private static TimeSeriesScienceLibrary instance;

    public static TimeSeriesScienceLibrary getInstance(){
        if (instance == null){
            instance = new TimeSeriesScienceLibrary();
        }
        return instance;
    }

    public BigDecimal calculateRootMeanSquareError(List<Point<LocalDate>> actualData, List<Point<LocalDate>> expectedData,
                                                   int k){

        int n = actualData.size();
        BigDecimal sumOfErrors = BigDecimal.ZERO;
        int j = UtilityMethods.findIndexOfDate(actualData.get(n - 1).getX(), expectedData);
        for (int i = n - 1; i > 0 && i >= (n - k); i--, j--){

            sumOfErrors = sumOfErrors.add(actualData.get(i).getY()
                    .subtract(expectedData.get(j).getY())
                    .pow(2));

        }
        return sumOfErrors.divide(new BigDecimal(n - k), RoundingMode.HALF_UP)
                .sqrt(new MathContext(MathContext.DECIMAL32.getPrecision(), RoundingMode.HALF_UP));

    }

    // actualData : predicted data, it has an actual range of dates we should consider in
    // calculations
    public BigDecimal calculateMeanAbsoluteError(List<Point<LocalDate>> actualData, List<Point<LocalDate>> expectedData,
                                                 int k){

        int n = actualData.size();
        BigDecimal sumOfErrors = BigDecimal.ZERO;
        int j = UtilityMethods.findIndexOfDate(actualData.get(n - 1).getX(), expectedData);
        for (int i = n - 1; i > 0 && i >= (n - k); i--, j--){

            // sum of | y - y* |
            sumOfErrors = sumOfErrors.add(actualData.get(i).getY().subtract(expectedData.get(j).getY()).abs());

        }

        return sumOfErrors.divide(new BigDecimal(n - k), ROUNDING_MODE);
    }

    // actualData : predicted data, it has an actual range of dates we should consider in
    // calculations
    public BigDecimal calculateMeanAbsolutePercentageError(List<Point<LocalDate>> actualData, List<Point<LocalDate>> expectedData,
                                                           int k){

        int n = actualData.size();
        BigDecimal sumOfErrors = BigDecimal.ZERO;
        int j = UtilityMethods.findIndexOfDate(actualData.get(n - 1).getX(), expectedData);
        for (int i = n - 1; i > 0 && i >= (n - k); i--, j--){

            sumOfErrors = sumOfErrors.add(BigDecimal.ONE.subtract(actualData.get(i).getY()
                    .divide(expectedData.get(j).getY(), ROUNDING_MODE)).abs());

        }
        return sumOfErrors.divide(new BigDecimal(n - k), ROUNDING_MODE);

    }

}
