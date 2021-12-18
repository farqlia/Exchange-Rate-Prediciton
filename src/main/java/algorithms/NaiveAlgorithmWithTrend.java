package algorithms;

import datasciencealgorithms.utils.Point;
import iterators.AscendingIterator;
import iterators.BiDirectionalIterator;
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

        // Improvement: we have one iterator instead of two, and the binarySearch
        // internally used by CustomIterator to find startDate is used only once (comparing to
        // the previous version where it was used with each iteration in loop)
        List<Point<LocalDate>> generatedData = new ArrayList<>();
        Iterator<Point<LocalDate>> outerItr = new BiDirectionalIterator(actualData, startDate, endDate, 2);

        while (outerItr.hasNext()) {
            Point<LocalDate> currPoint = outerItr.next();
            BigDecimal previousValue = outerItr.next().getY();

            // Computes a predicted value
            generatedData.add(new Point<>(currPoint.getX(),
                    previousValue
                    .multiply(new BigDecimal("2"))
                    .subtract(outerItr.next().getY())));
        } ;

        return generatedData;
    }


}
