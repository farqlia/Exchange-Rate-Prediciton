package algorithms;

import datasciencealgorithms.utils.Point;
import iterators.AscendingIterator;
import iterators.DescendingIterator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NaiveAlgorithmWithTrend implements Algorithm{

    private List<Point<LocalDate>> actualData;

    public NaiveAlgorithmWithTrend(List<Point<LocalDate>> actualData){
        this.actualData = actualData;
    }

    // TODO: the start date should be shifted so that the 1st values don't break the overall result
    @Override
    public List<Point<LocalDate>> forecastValuesForDates(LocalDate startDate, LocalDate endDate) {

        List<Point<LocalDate>> generatedData = new ArrayList<>();
        // TODO: make two-directional iterator, that iterates a given steps backwards,
        //  and then returns to the latest point and steps forward
        Iterator<Point<LocalDate>> outerItr = new AscendingIterator(actualData, startDate, endDate);
        Iterator<Point<LocalDate>> innerItr;

        while (outerItr.hasNext()){

            Point<LocalDate> currPoint = outerItr.next();

            // Creates an iterator for the current point that can move backwards
            innerItr = new DescendingIterator(actualData, currPoint.getX().minusDays(1));

            BigDecimal previousValue = innerItr.next().getY();

            // Computes a predicted value
            generatedData.add(new Point<>(currPoint.getX(),
                    previousValue
                    .multiply(new BigDecimal("2"))
                    .subtract(innerItr.next().getY())));

        }

        return generatedData;
    }


}
