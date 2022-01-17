package model;

import algorithms.AlgorithmName;
import datasciencealgorithms.utils.point.Point;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import static algorithms.algorithmsparameters.AlgorithmArguments.Names;

public abstract class AbstractModel {

    public abstract void predict(List<Point> data, LocalDate startDate,
                                 LocalDate endDate);

    public abstract void setAlgorithm(AlgorithmName algorithmName);

    public abstract void registerObserver(ModelObserver ob);

    public abstract void notifyObservers(ModelEvent event);

}
