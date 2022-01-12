package mathlibraries;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

public class ScienceLibrary {
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    public static BigDecimal calculateMeanError(List<BigDecimal> predictedData, List<BigDecimal> expectedData){

        BigDecimal sum = BigDecimal.ZERO;
        int n = predictedData.size();
        for (int i = 0; i < n; i++){
            sum = sum.add(predictedData.get(i)
                    .subtract(expectedData.get(i)));
        }
        return sum.divide(new BigDecimal(n - 2), RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateMeanSquaredError(List<BigDecimal> predictedData, List<BigDecimal> expectedData){

        BigDecimal sum = BigDecimal.ZERO;
        int n = predictedData.size();
        for (int i = 0; i < n; i++){
            sum = sum.add(predictedData.get(i)
                    .subtract(expectedData.get(i))
                    .pow(2));
        }
        return sum.divide(new BigDecimal(n - 2), RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateRootMeanSquaredPercentageError(List<BigDecimal> predictedData, List<BigDecimal> expectedData){

        BigDecimal sum = BigDecimal.ZERO;
        for (int i = 0; i < predictedData.size(); i++){
            sum = sum.add(BigDecimal.ONE
                    .subtract(expectedData.get(i).divide(predictedData.get(i), ROUNDING_MODE))
                    .pow(2));
        }
        return sum.divide(new BigDecimal(predictedData.size() - 2), ROUNDING_MODE)
                .sqrt(new MathContext(MathContext.DECIMAL32.getPrecision(), ROUNDING_MODE));

    }

    public static BigDecimal calculateMeanAbsolutePercentageError(List<BigDecimal> predictedData, List<BigDecimal> expectedData){

        BigDecimal sum = BigDecimal.ZERO;
        for (int i = 0; i < predictedData.size(); i++){
            sum = sum.add(BigDecimal.ONE
                    .subtract(expectedData.get(i).divide(predictedData.get(i), ROUNDING_MODE)).abs());
        }
        return sum.divide(new BigDecimal(predictedData.size() - 2), ROUNDING_MODE);

    }


}
