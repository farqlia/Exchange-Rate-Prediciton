package model;

import algorithms.*;
import datasciencealgorithms.utils.point.Point;
import java.time.LocalDate;

import java.util.List;

public class Model {

    private Algorithm algorithm;
    private int lookbackPeriod;

    public void setAlgorithm(AlgorithmName algorithmName, int lookbackPeriod){
        this.lookbackPeriod = lookbackPeriod;

        algorithm = algorithmName.createAlgorithm().apply(lookbackPeriod);
    }

    public List<Point> predict(List<Point> data, LocalDate startDate,
                                          LocalDate endDate){

        return algorithm.forecastValuesForDates(data, startDate, endDate);
    }


}
