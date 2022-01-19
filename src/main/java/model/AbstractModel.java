package model;

import algorithms.AlgorithmInitializer;
import datasciencealgorithms.utils.point.Point;

import java.time.LocalDate;
import java.util.List;

public abstract class AbstractModel {

    public abstract void predict(List<Point> data, LocalDate startDate,
                                 LocalDate endDate);

    public abstract void setAlgorithm(AlgorithmInitializer algorithmInitializer);

    public abstract void registerObserver(ModelObserver ob);

    public abstract void notifyObservers(ModelEvent event);

}
