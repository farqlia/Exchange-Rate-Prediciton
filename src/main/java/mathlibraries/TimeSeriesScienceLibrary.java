package mathlibraries;

import datasciencealgorithms.utils.point.Point;
import iterators.AscendingIterator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

public class TimeSeriesScienceLibrary {

    public static final MathContext ROUNDING_MODE =
            new MathContext(MathContext.DECIMAL32.getPrecision(), RoundingMode.HALF_UP);


    public static BigDecimal calculateRootMeanSquareError(List<Point> predictedData, List<Point> expectedData){
        return calculateRootMeanSquareErrorK(predictedData, expectedData, 1);
    }

    public static BigDecimal calculateRootMeanSquareErrorK(List<Point> predictedData, List<Point> expectedData,
                                                           int k){

        int n = predictedData.size();
        BigDecimal sumOfErrors = BigDecimal.ZERO;

        // Iterators for actual and expected data
        AscendingIterator preItr = new AscendingIterator(predictedData);
        AscendingIterator exItr = new AscendingIterator(expectedData, predictedData.get(0).getX(),
                predictedData.get(n - 1).getX());


        while (preItr.hasNext() && exItr.hasNext()) {

            sumOfErrors = sumOfErrors.add(preItr.next().getY()
                    .subtract(exItr.next().getY())
                    .pow(2));

        }
        return sumOfErrors.divide(new BigDecimal(n - k), RoundingMode.HALF_UP)
                .sqrt(ROUNDING_MODE);

    }

    public static BigDecimal calculateRootMeanSquarePercentageError(List<Point> predictedData, List<Point> expectedData){
        return calculateRootMeanSquarePercentageErrorK(predictedData, expectedData, 1);
    }

    public static BigDecimal calculateRootMeanSquarePercentageErrorK(List<Point> predictedData, List<Point> expectedData,
                                                                     int k) {
        int n = predictedData.size();
        BigDecimal sumOfErrors = BigDecimal.ZERO;
        // Iterators for actual and expected data
        AscendingIterator preItr = new AscendingIterator(predictedData);
        AscendingIterator exItr = new AscendingIterator(expectedData, predictedData.get(0).getX(),
                predictedData.get(n - 1).getX());


        while (preItr.hasNext() && exItr.hasNext()) {
            sumOfErrors = sumOfErrors.add(BigDecimal.ONE.subtract(preItr.next().getY()
                    .divide(exItr.next().getY(), ROUNDING_MODE))
                    .pow(2));

        }
        return sumOfErrors.divide(new BigDecimal(n - k), RoundingMode.HALF_UP)
                .sqrt(ROUNDING_MODE);
    }

    public static BigDecimal calculateMeanAbsoluteError(List<Point> predictedData, List<Point> expectedData){
        return calculateMeanAbsoluteErrorK(predictedData, expectedData, 1);
    }

    // predictedData : predicted data, it has an actual range of dates we should consider in
    // calculations
    public static BigDecimal calculateMeanAbsoluteErrorK(List<Point> predictedData, List<Point> expectedData,
                                                         int k){

        int n = predictedData.size();
        BigDecimal sumOfErrors = BigDecimal.ZERO;
        // Iterators for actual and expected data
        AscendingIterator preItr = new AscendingIterator(predictedData);
        AscendingIterator exItr = new AscendingIterator(expectedData, predictedData.get(0).getX(),
                predictedData.get(n - 1).getX());


        while (preItr.hasNext() && exItr.hasNext()) {

            // sum of | y - y* |
            sumOfErrors = sumOfErrors.add(preItr.next().getY()
                    .subtract(exItr.next().getY()).abs());

        }

        return sumOfErrors.divide(new BigDecimal(n - k), ROUNDING_MODE);
    }

    public static BigDecimal calculateMeanAbsolutePercentageError(List<Point> predictedData, List<Point> expectedData){
        return calculateMeanAbsolutePercentageErrorK(predictedData, expectedData, 1);
    }

    // predictedData : predicted data, it has an actual range of dates we should consider in
    // calculations
    public static BigDecimal calculateMeanAbsolutePercentageErrorK(List<Point> predictedData, List<Point> expectedData,
                                                                   int k){

        int n = predictedData.size();
        BigDecimal sumOfErrors = BigDecimal.ZERO;

        // Iterators for actual and expected data
        AscendingIterator preItr = new AscendingIterator(predictedData);
        AscendingIterator exItr = new AscendingIterator(expectedData, predictedData.get(0).getX(),
                predictedData.get(n - 1).getX());


        while (preItr.hasNext() && exItr.hasNext()) {

            sumOfErrors = sumOfErrors.add(BigDecimal.ONE.subtract(preItr.next().getY()
                    .divide(exItr.next().getY(), ROUNDING_MODE)).abs());

        }
        return sumOfErrors.divide(new BigDecimal(n - k), ROUNDING_MODE);

    }

    public static BigDecimal[] calculateAbsoluteError(List<Point> expected, List<Point> predicted){

        BigDecimal[] errorsArr = new BigDecimal[predicted.size()];

        for (int i = 0; i < expected.size(); i++){
            errorsArr[i] = expected.get(i).getY().subtract(predicted.get(i).getY());
        }
        return errorsArr;
    }

    public static BigDecimal[] calculatePercentageError(List<Point> expected, List<Point> predicted){

        BigDecimal[] errorsArr = new BigDecimal[predicted.size()];

        for (int i = 0; i < expected.size(); i++){
            // 1 - (yt*/yt)
            errorsArr[i] = BigDecimal.ONE.subtract(expected.get(i).getY().divide(predicted.get(i).getY(),
                    ROUNDING_MODE));
        }
        return errorsArr;
    }

}
