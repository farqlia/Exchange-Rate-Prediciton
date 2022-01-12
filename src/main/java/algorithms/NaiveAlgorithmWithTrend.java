package algorithms;

import datasciencealgorithms.utils.point.Point;
import iterators.BiDirectionalIterator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NaiveAlgorithmWithTrend implements Algorithm{

    @Override
    public List<Point> forecastValuesForDates(List<Point> expectedData,
                                                         LocalDate startDate, LocalDate endDate) {

        // Improvement: we have one iterator instead of two, and the binarySearch
        // internally used by CustomIterator to find startDate is used only once (comparing to
        // the previous version where it was used with each iteration in loop)
        List<Point> generatedData = new ArrayList<>();
        Iterator<Point> outerItr = new BiDirectionalIterator(expectedData, startDate, endDate);

        while (outerItr.hasNext()) {
            Point currPoint = outerItr.next();
            BigDecimal previousValue = outerItr.next().getY();

            // Computes a predicted value
            generatedData.add(new Point(currPoint.getX(),
                    previousValue
                    .multiply(new BigDecimal("2"))
                    .subtract(outerItr.next().getY())));
        }

        return generatedData;
    }


}
