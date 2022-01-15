package model;

import algorithms.AlgorithmName;
import datasciencealgorithms.utils.point.Point;

import java.time.LocalDate;
import java.util.List;

public abstract class AbstractModel {

    public abstract void predict(List<Point> data, LocalDate startDate,
                                 LocalDate endDate);

    public abstract void setAlgorithm(AlgorithmName algorithmName, int lookbackPeriod);

    public abstract void registerObserver(ModelObserver ob);

    public abstract void notifyObservers(ModelEvent event);

}
