package mathlibraries;

import datasciencealgorithms.utils.point.Point;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiFunction;

public enum Statistics {

    RMSE(TimeSeriesScienceLibrary::calculateRootMeanSquareError),
    RMSPE(TimeSeriesScienceLibrary::calculateRootMeanSquarePercentageError),
    MAPE(TimeSeriesScienceLibrary::calculateMeanAbsolutePercentageError),
    MASE(TimeSeriesScienceLibrary::calculateMeanAbsoluteScaledError),
    AVERAGE_REAL((a,p) -> TimeSeriesScienceLibrary.calculateAverage(a), "Real Average"),
    AVERAGE_PRED((a,p) -> TimeSeriesScienceLibrary.calculateAverage(p), "Predicted Average");

    private final BiFunction<List<Point>, List<Point>, BigDecimal> method;
    String name;
    Statistics(BiFunction<List<Point>, List<Point>, BigDecimal> method){
        this.method = method;
        name = this.name();
    }

    Statistics(BiFunction<List<Point>, List<Point>, BigDecimal> method, String name){
        this.method = method;
        this.name = name;
    }

    // Extracts from both lists only numeric values and applies statistical function
    public BigDecimal apply(List<Point> predictedData, List<Point> realData){
        return method.apply(predictedData, realData);
    }

    @Override
    public String toString(){
        return name;
    }
}
