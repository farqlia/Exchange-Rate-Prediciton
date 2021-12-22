package algorithms;

import datasciencealgorithms.utils.Point;
import datasciencealgorithms.utils.UtilityMethods;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NaiveAlgorithmWithTrendAndAverageIncrement implements Algorithm{

    RoundingMode roundingMode = RoundingMode.HALF_EVEN;

    @Override
    public List<Point<LocalDate>> forecastValuesForDates(List<Point<LocalDate>> actualData, LocalDate startDate, LocalDate endDate) {

        List<Point<LocalDate>> generatedData = new ArrayList<>();
        int startIndex = UtilityMethods.findIndexOfDate(startDate, actualData);
        int endIndex = UtilityMethods.findIndexOfDate(endDate, actualData);

        for (int i = startIndex; i <= endIndex; i++){

            BigDecimal sumForAverage = BigDecimal.ZERO;

            for (int j = i - 2; j > 0; j--){
                sumForAverage = sumForAverage.add(
                        // min is not minus :)
                        actualData.get(j).getY().subtract(actualData.get(j - 1).getY()));
            }

            generatedData.add(new Point<>(actualData.get(i).getX(),
                    actualData.get(i - 1).getY()
                            .add(sumForAverage.divide(new BigDecimal(i - 2), roundingMode))));

        }
        return generatedData;
    }
}
