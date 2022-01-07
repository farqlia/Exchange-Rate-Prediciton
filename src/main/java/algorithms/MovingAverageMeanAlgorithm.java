package algorithms;

import datasciencealgorithms.utils.UtilityMethods;
import datasciencealgorithms.utils.point.Point;
import mathlibraries.TimeSeriesScienceLibrary;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class MovingAverageMeanAlgorithm implements Algorithm{

    @Override
    public List<Point<LocalDate>> forecastValuesForDates(List<Point<LocalDate>> expectedData, LocalDate startDate, LocalDate endDate) {

        // Holds final prediction
        List<Point<LocalDate>> outputList = new ArrayList<>();
        // Stores result of predictions for each value of k
        List<Point<LocalDate>> currentValuesList = new ArrayList<>();
        int initK = (int) ChronoUnit.DAYS.between(expectedData.get(0).getX(), startDate);
        int startIndex = UtilityMethods.findIndexOfDate(startDate, expectedData);
        int endIndex = UtilityMethods.findIndexOfDate(endDate, expectedData);

        BigDecimal minRMSE = expectedData.get(startIndex).getY();

        //
        for (int k = initK; k <= (expectedData.size() - k); k++){

            for (int i = startIndex; i <= endIndex; i++){
                // Computes average of k-recent values
                BigDecimal sumForAverage = BigDecimal.ZERO;
                for (int j = i - 1; (j > 0 && j > (i - initK - 1)); j--){
                    sumForAverage = sumForAverage.add(expectedData.get(j).getY());
                }
                currentValuesList.add(new Point<>(expectedData.get(i).getX(),
                        sumForAverage.divide(new BigDecimal(initK), RoundingMode.HALF_UP)));
            }


            // Calculates root of mean square error
            BigDecimal currentSRME = TimeSeriesScienceLibrary.getInstance()
                    .calculateRootMeanSquareError(currentValuesList, expectedData, k);

            // Smaller error means better fit
            if (currentSRME.compareTo(minRMSE) <= 0){

                minRMSE = currentSRME;
                // Saves presumably best prediction
                outputList = List.copyOf(currentValuesList);
            }
            currentValuesList.clear();

        }
        return outputList;
    }

}
