package mathlibraries;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

public class ScienceLibrary {

    private static ScienceLibrary instance;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private ScienceLibrary(){
    }

    public static ScienceLibrary getInstance(){
        if (instance == null){
            instance = new ScienceLibrary();
        }
        return instance;
    }

    public BigDecimal calculateMeanError(List<BigDecimal> actualData, List<BigDecimal> expectedData){

        BigDecimal sum = BigDecimal.ZERO;
        int n = actualData.size();
        for (int i = 0; i < n; i++){
            sum = sum.add(actualData.get(i)
                    .subtract(expectedData.get(i)));
        }
        return sum.divide(new BigDecimal(n - 2), RoundingMode.HALF_UP);
    }

    public BigDecimal calculateMeanSquaredError(List<BigDecimal> actualData, List<BigDecimal> expectedData){

        BigDecimal sum = BigDecimal.ZERO;
        int n = actualData.size();
        for (int i = 0; i < n; i++){
            sum = sum.add(actualData.get(i)
                    .subtract(expectedData.get(i))
                    .pow(2));
        }
        return sum.divide(new BigDecimal(n - 2), RoundingMode.HALF_UP);
    }

    public BigDecimal calculateRootMeanSquaredPercentageError(List<BigDecimal> actualData, List<BigDecimal> expectedData){

        BigDecimal sum = BigDecimal.ZERO;
        for (int i = 0; i < actualData.size(); i++){
            sum = sum.add(BigDecimal.ONE
                    .subtract(expectedData.get(i).divide(actualData.get(i), ROUNDING_MODE))
                    .pow(2));
        }
        return sum.divide(new BigDecimal(actualData.size() - 2), ROUNDING_MODE)
                .sqrt(new MathContext(MathContext.DECIMAL32.getPrecision(), ROUNDING_MODE));

    }

    public BigDecimal calculateMeanAbsolutePercentageError(List<BigDecimal> actualData, List<BigDecimal> expectedData){

        BigDecimal sum = BigDecimal.ZERO;
        for (int i = 0; i < actualData.size(); i++){
            sum = sum.add(BigDecimal.ONE
                    .subtract(expectedData.get(i).divide(actualData.get(i), ROUNDING_MODE)).abs());
        }
        return sum.divide(new BigDecimal(actualData.size() - 2), ROUNDING_MODE);

    }


}
