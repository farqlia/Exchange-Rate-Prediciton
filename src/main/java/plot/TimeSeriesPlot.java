package plot;

import datasciencealgorithms.utils.point.Point;
import iterators.AscendingIterator;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class TimeSeriesPlot extends Plot<LocalDate>{

    LocalDate startDate, endDate;
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public TimeSeriesPlot(BigDecimal startValueOnY, BigDecimal endValueOnY,
                          LocalDate startDate, LocalDate endDate){
        super(startValueOnY, endValueOnY);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public void drawXLabels(Graphics2D g2D) {

        int y = (int)(endY + (.5 * yHeight));
        int x = 0;
        long daysPeriod = new BigDecimal(xWidth)
                .divideToIntegralValue(new BigDecimal(distanceBetweenNextPointsOnXAxis)).intValue();

        for (int i = 0; i < xGrids; i++){
            g2D.drawString(startDate.plusDays(i * daysPeriod).format(format),
                    x, y);
            x += xWidth;
        }

        g2D.setStroke(new BasicStroke(2.0f));
        g2D.setPaint(Color.BLACK);

        y -= (int)(.5 * yHeight);

        g2D.drawLine(0, y, endX, y);

    }

    @Override
    public double findStartXCoordinate(Point<LocalDate> firstValue) {
        System.out.println("Start Date of data: " + firstValue);
        long days = ChronoUnit.DAYS.between(startDate, firstValue.getX());
        System.out.println("Days Between " + startDate + ", " + firstValue.getX() + "= " + days);
        return days
                * distanceBetweenNextPointsOnXAxis;
    }

    @Override
    public void setXScaling() {

        long totalDays = ChronoUnit.DAYS.between(startDate, endDate);
        distanceBetweenNextPointsOnXAxis = workingWidth.divide(new BigDecimal(totalDays), RoundingMode.HALF_UP)
                .doubleValue();

        System.out.println("Total Days: " + totalDays);
        System.out.println("Distance Between X Points: " + distanceBetweenNextPointsOnXAxis);

    }

    @Override
    public void plotLine(List<Point<LocalDate>> data){
        plotLine(new AscendingIterator(data, startDate, endDate));
    }


    @Override
    public Point<LocalDate> calculateXValueBasedOnCoordinates(double x, double y) {
        return null;
    }

    public class LabelsForXAxis{

    }

}
