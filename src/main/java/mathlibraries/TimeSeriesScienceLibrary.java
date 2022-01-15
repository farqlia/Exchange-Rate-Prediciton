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


    public static BigDecimal calculateRootMeanSquareError(List<Point> predictedDataData, List<Point> realDataData){
        return calculateRootMeanSquareErrorK(predictedDataData, realDataData, 1);
    }

    public static BigDecimal calculateRootMeanSquareErrorK(List<Point> predictedDataData, List<Point> realDataData,
                                                           int k){

        int n = predictedDataData.size();
        BigDecimal sumOfErrors = BigDecimal.ZERO;

        // Iterators for actual and realData data
        AscendingIterator preItr = new AscendingIterator(predictedDataData);
        AscendingIterator exItr = new AscendingIterator(realDataData, predictedDataData.get(0).getX(),
                predictedDataData.get(n - 1).getX());


        while (preItr.hasNext() && exItr.hasNext()) {

            sumOfErrors = sumOfErrors.add(preItr.next().getY()
                    .subtract(exItr.next().getY())
                    .pow(2));

        }
        return sumOfErrors.divide(new BigDecimal(n - k), RoundingMode.HALF_UP)
                .sqrt(ROUNDING_MODE);

    }

    public static BigDecimal calculateRootMeanSquarePercentageError(List<Point> predictedDataData, List<Point> realDataData){
        return calculateRootMeanSquarePercentageErrorK(predictedDataData, realDataData, 1);
    }

    public static BigDecimal calculateRootMeanSquarePercentageErrorK(List<Point> predictedDataData, List<Point> realDataData,
                                                                     int k) {
        int n = predictedDataData.size();
        BigDecimal sumOfErrors = BigDecimal.ZERO;
        // Iterators for actual and realData data
        AscendingIterator preItr = new AscendingIterator(predictedDataData);
        AscendingIterator exItr = new AscendingIterator(realDataData, predictedDataData.get(0).getX(),
                predictedDataData.get(n - 1).getX());


        while (preItr.hasNext() && exItr.hasNext()) {
            sumOfErrors = sumOfErrors.add(BigDecimal.ONE.subtract(preItr.next().getY()
                    .divide(exItr.next().getY(), ROUNDING_MODE))
                    .pow(2));

        }
        return sumOfErrors.divide(new BigDecimal(n - k), RoundingMode.HALF_UP)
                .sqrt(ROUNDING_MODE);
    }

    public static BigDecimal calculateMeanAbsoluteError(List<Point> predictedDataData, List<Point> realDataData){
        return calculateMeanAbsoluteErrorK(predictedDataData, realDataData, 1);
    }

    // predictedDataData : predictedData data, it has an actual range of dates we should consider in
    // calculations
    public static BigDecimal calculateMeanAbsoluteErrorK(List<Point> predictedDataData, List<Point> realDataData,
                                                         int k){

        int n = predictedDataData.size();
        BigDecimal sumOfErrors = BigDecimal.ZERO;
        // Iterators for actual and realData data
        AscendingIterator preItr = new AscendingIterator(predictedDataData);
        AscendingIterator exItr = new AscendingIterator(realDataData, predictedDataData.get(0).getX(),
                predictedDataData.get(n - 1).getX());


        while (preItr.hasNext() && exItr.hasNext()) {

            // sum of | y - y* |
            sumOfErrors = sumOfErrors.add(preItr.next().getY()
                    .subtract(exItr.next().getY()).abs());

        }

        return sumOfErrors.divide(new BigDecimal(n - k), ROUNDING_MODE);
    }

    public static BigDecimal calculateMeanAbsolutePercentageError(List<Point> predictedDataData, List<Point> realDataData){
        return calculateMeanAbsolutePercentageErrorK(predictedDataData, realDataData, 1);
    }

    // predictedDataData : predictedData data, it has an actual range of dates we should consider in
    // calculations
    public static BigDecimal calculateMeanAbsolutePercentageErrorK(List<Point> predictedDataData, List<Point> realDataData,
                                                                   int k){

        int n = predictedDataData.size();
        BigDecimal sumOfErrors = BigDecimal.ZERO;

        // Iterators for actual and realData data
        AscendingIterator preItr = new AscendingIterator(predictedDataData);
        AscendingIterator exItr = new AscendingIterator(realDataData, predictedDataData.get(0).getX(),
                predictedDataData.get(n - 1).getX());


        while (preItr.hasNext() && exItr.hasNext()) {

            sumOfErrors = sumOfErrors.add(BigDecimal.ONE.subtract(preItr.next().getY()
                    .divide(exItr.next().getY(), ROUNDING_MODE)).abs());

        }
        return sumOfErrors.divide(new BigDecimal(n - k), ROUNDING_MODE);

    }

    public static BigDecimal[] calculateAbsoluteError(List<Point> realData, List<Point> predictedData){

        int n = predictedData.size();
        BigDecimal[] errorsArr = new BigDecimal[n];
        // Iterators for actual and realData data
        AscendingIterator preItr = new AscendingIterator(predictedData);
        AscendingIterator exItr = new AscendingIterator(realData, predictedData.get(0).getX(),
                predictedData.get(n - 1).getX());


        int i = 0;
        while (preItr.hasNext() && exItr.hasNext()) {
            errorsArr[i++] = exItr.next().getY().subtract(preItr.next().getY());
        }
        return errorsArr;
    }

    public static BigDecimal[] calculatePercentageError(List<Point> realData, List<Point> predictedData){

        int n = predictedData.size();
        BigDecimal[] errorsArr = new BigDecimal[n];
        // Iterators for actual and realData data
        AscendingIterator preItr = new AscendingIterator(predictedData);
        AscendingIterator exItr = new AscendingIterator(realData, predictedData.get(0).getX(),
                predictedData.get(n- 1).getX());

        for (int i = 0; exItr.hasNext() && preItr.hasNext(); i++){
            errorsArr[i] = BigDecimal.ONE.subtract(preItr.next().getY().divide(exItr.next().getY(), ROUNDING_MODE))
                    .abs().multiply(new BigDecimal("100"));
        }

        return errorsArr;
    }

}
