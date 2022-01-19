package mathlibraries;

import datasciencealgorithms.utils.UtilityMethods;
import datasciencealgorithms.utils.point.Point;
import iterators.AscendingIterator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

public class TimeSeriesScienceLibrary {

    public static final MathContext ROUNDING_MODE =
            new MathContext(MathContext.DECIMAL32.getPrecision(), RoundingMode.HALF_UP);
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    public static BigDecimal calculateRootMeanSquareError(List<Point> predictedData, List<Point> realDataData){
        return calculateRootMeanSquareErrorK(predictedData, realDataData, 1);
    }

    public static BigDecimal calculateRootMeanSquareErrorK(List<Point> predictedData, List<Point> realDataData,
                                                           int k){

        int n = predictedData.size();
        if (n <= 0) return BigDecimal.ZERO;
        BigDecimal sumOfErrors = BigDecimal.ZERO;

        // Iterators for actual and realData data
        AscendingIterator preItr = new AscendingIterator(predictedData);
        AscendingIterator exItr = new AscendingIterator(realDataData, predictedData.get(0).getX(),
                predictedData.get(n - 1).getX());


        while (preItr.hasNext() && exItr.hasNext()) {

            sumOfErrors = sumOfErrors.add(preItr.next().getY()
                    .subtract(exItr.next().getY())
                    .pow(2));

        }
        return sumOfErrors.divide(new BigDecimal(n - k), RoundingMode.HALF_UP)
                .sqrt(ROUNDING_MODE);

    }

    public static BigDecimal calculateRootMeanSquarePercentageError(List<Point> predictedData, List<Point> realData){
        return calculateRootMeanSquarePercentageErrorK(predictedData, realData, 1);
    }

    public static BigDecimal calculateRootMeanSquarePercentageErrorK(List<Point> predictedData, List<Point> realData,
                                                                     int k) {
        int n = predictedData.size();
        if (n <= 0) return BigDecimal.ZERO;
        BigDecimal sumOfErrors = BigDecimal.ZERO;
        // Iterators for actual and realData data
        AscendingIterator preItr = new AscendingIterator(predictedData);
        AscendingIterator exItr = new AscendingIterator(realData, predictedData.get(0).getX(),
                predictedData.get(n - 1).getX());


        while (preItr.hasNext() && exItr.hasNext()) {
            sumOfErrors = sumOfErrors.add(BigDecimal.ONE.subtract(preItr.next().getY()
                    .divide(exItr.next().getY(), ROUNDING_MODE))
                    .pow(2));

        }
        return sumOfErrors.divide(new BigDecimal(n - k), RoundingMode.HALF_UP)
                .sqrt(ROUNDING_MODE).multiply(ONE_HUNDRED);
    }

   public static BigDecimal calculateAverage(Point ... points){
        BigDecimal sum = BigDecimal.ZERO;
        for (Point p : points){
            sum = sum.add(p.getY());
        }
        return sum.divide(new BigDecimal(points.length), ROUNDING_MODE);
    }

    public static BigDecimal calculateAverage(List<Point> points){
        Point[] pointsArr = new Point[points.size()];
        return calculateAverage(points.toArray(pointsArr));
    }

    // predictedData : predictedData data, it has an actual range of dates we should consider in
    // calculations
    public static BigDecimal calculateMeanAbsoluteErrorK(List<Point> predictedData, List<Point> realData,
                                                         int k){

        int n = predictedData.size();
        if (n <= 0) return BigDecimal.ZERO;
        BigDecimal sumOfErrors = BigDecimal.ZERO;
        // Iterators for actual and realData data
        AscendingIterator preItr = new AscendingIterator(predictedData);
        AscendingIterator exItr = new AscendingIterator(realData, predictedData.get(0).getX(),
                predictedData.get(n - 1).getX());


        while (preItr.hasNext() && exItr.hasNext()) {

            // sum of | y - y* |
            sumOfErrors = sumOfErrors.add(preItr.next().getY()
                    .subtract(exItr.next().getY()).abs());

        }

        return sumOfErrors.divide(new BigDecimal(n - k), ROUNDING_MODE);
    }

    public static BigDecimal calculateMeanAbsoluteError(List<Point> predictedData, List<Point> realData){
        return calculateMeanAbsoluteErrorK(predictedData, realData, 0);
    }

    public static BigDecimal calculateMeanAbsolutePercentageError(List<Point> predictedDataData, List<Point> realDataData){
        return calculateMeanAbsolutePercentageErrorK(predictedDataData, realDataData, 0);
    }

    // predictedData : predictedData data, it has an actual range of dates we should consider in
    // calculations
    public static BigDecimal calculateMeanAbsolutePercentageErrorK(List<Point> predictedData, List<Point> realData,
                                                                   int k){

        int n = predictedData.size();
        if (n <= 0) return BigDecimal.ZERO;
        BigDecimal sumOfErrors = BigDecimal.ZERO;

        // Iterators for actual and real data
        AscendingIterator preItr = new AscendingIterator(predictedData);
        AscendingIterator exItr = new AscendingIterator(realData, predictedData.get(0).getX(),
                predictedData.get(n - 1).getX());


        while (preItr.hasNext() && exItr.hasNext()) {

            sumOfErrors = sumOfErrors.add(BigDecimal.ONE.subtract(preItr.next().getY()
                    .divide(exItr.next().getY(), ROUNDING_MODE)).abs());

        }
        return sumOfErrors.divide(new BigDecimal(n - k), ROUNDING_MODE).multiply(ONE_HUNDRED);

    }

    public static BigDecimal calculateMeanAbsoluteScaledError(List<Point> predictedData, List<Point> realData){

        int n = predictedData.size();
        if (n <= 0) return BigDecimal.ZERO;
        int startIndex = UtilityMethods.findIndexOfDate(predictedData.get(0).getX(), realData);
        int endIndex = UtilityMethods.findIndexOfDate(predictedData.get(n-1).getX(), realData);

        BigDecimal sumOfAbsOneStepError = BigDecimal.ZERO;
        for (int i = startIndex + 1; i <= endIndex; i++){
            // Denominator is the mean absolute error of the one-step "naive forecast method" which uses
            // value from the prior period as the forecast
            sumOfAbsOneStepError = sumOfAbsOneStepError.add(realData.get(i).getY().subtract(realData.get(i - 1).getY()).abs());
        }

        return calculateMeanAbsoluteError(predictedData, realData)
                .divide(sumOfAbsOneStepError.divide(new BigDecimal(n - 1), ROUNDING_MODE), ROUNDING_MODE);
    }

    public static BigDecimal[] calculateAbsoluteError(List<Point> predictedData, List<Point> realData){

        int n = predictedData.size();

        BigDecimal[] errorsArr = new BigDecimal[n];
        if (n <= 0) return errorsArr;
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

    public static BigDecimal[] calculatePercentageError(List<Point> predictedData, List<Point> realData){

        int n = predictedData.size();
        BigDecimal[] errorsArr = new BigDecimal[n];
        if (n <= 0) return errorsArr;
        // Iterators for actual and realData data
        AscendingIterator preItr = new AscendingIterator(predictedData);
        AscendingIterator exItr = new AscendingIterator(realData, predictedData.get(0).getX(),
                predictedData.get(n - 1).getX());

        for (int i = 0; exItr.hasNext() && preItr.hasNext(); i++){
            errorsArr[i] = BigDecimal.ONE.subtract(preItr.next().getY().divide(exItr.next().getY(), ROUNDING_MODE))
                    .abs().multiply(ONE_HUNDRED);
        }

        return errorsArr;
    }

}
