package model;

import algorithms.Algorithm;
import algorithms.AlgorithmNames;
import datasciencealgorithms.utils.point.Point;
import view.ViewEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Model {

    private Algorithm algorithm;

    public void setAlgorithm(AlgorithmNames algorithmName){

    }

    public List<Point<LocalDate>> predict(List<Point<LocalDate>> data, LocalDate startDate,
                                          LocalDate endDate){
        return new ArrayList<>();
    }

}
