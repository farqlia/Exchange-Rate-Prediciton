package algorithms;

import datasciencealgorithms.utils.point.Point;
import datasciencealgorithms.utils.UtilityMethods;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NaiveAlgorithmWithTrendAndAverageIncrement implements Algorithm{

    RoundingMode roundingMode = RoundingMode.HALF_UP;
    private int lookbackPeriod;

    public NaiveAlgorithmWithTrendAndAverageIncrement(int lookbackPeriod){
        this.lookbackPeriod = lookbackPeriod;
    }

    public NaiveAlgorithmWithTrendAndAverageIncrement(){
        this.lookbackPeriod = 10;
    }

    @Override
    public List<Point> forecastValuesForDates(List<Point> expectedData, LocalDate startDate, LocalDate endDate) {

        List<Point> generatedData = new ArrayList<>();
        int startIndex = UtilityMethods.findIndexOfDate(startDate, expectedData);
        int endIndex = UtilityMethods.findIndexOfDate(endDate, expectedData);

        for (int i = startIndex; i <= endIndex; i++){

            BigDecimal sumForAverage = BigDecimal.ZERO;

            for (int j = i - 2; j > (i - 2 - lookbackPeriod); j--){
                sumForAverage = sumForAverage.add(
                        // min is not minus :)
                        expectedData.get(j).getY().subtract(expectedData.get(j - 1).getY()));
            }

            generatedData.add(new Point(expectedData.get(i).getX(),
                    expectedData.get(i - 1).getY()
                            .add(sumForAverage.divide(new BigDecimal(lookbackPeriod), roundingMode))));

        }
        return generatedData;
    }
}
