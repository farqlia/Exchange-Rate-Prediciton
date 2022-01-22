package model;

import algorithms.AlgorithmInitializerExPost;
import algorithms.algorithmsinitializer.AlgorithmInitializer;
import datasciencealgorithms.utils.point.Point;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractModel {

    private final List<ModelObserver> observers = new ArrayList<>();
    public abstract void predict(List<Point> realData,
                                 LocalDate startDate, LocalDate endDate);

    public abstract void setAlgorithm(AlgorithmInitializer algorithmInitializer);

    public abstract List<Point> getRealData();

    public void registerObserver(ModelObserver ob){
        observers.add(ob);
    }

    public void notifyObservers(ModelEvent event) {
        for (ModelObserver o : observers){
            o.update(event);
        }
    }

}
