package mathlibraries;

import datasciencealgorithms.utils.point.Point;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiFunction;

public enum Statistics {

    RMSE(TimeSeriesScienceLibrary::calculateRootMeanSquareError),
    RMSPE(TimeSeriesScienceLibrary::calculateRootMeanSquarePercentageError),
    MAPE(TimeSeriesScienceLibrary::calculateMeanAbsolutePercentageError);

    private BiFunction<List<Point>, List<Point>, BigDecimal> method;

    Statistics(BiFunction<List<Point>, List<Point>, BigDecimal> method){
        this.method = method;
    }

    // Extracts from both lists only numeric values and applies statistical function
    public BigDecimal apply(List<Point> expected, List<Point> actual){
        return method.apply(expected, actual);
    }
}
