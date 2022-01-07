package plot;
import datasciencealgorithms.utils.point.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.List;


public abstract class Plot<E> extends JComponent {

    // xWidth is 1/12 of 4/5 of total width
    // yHeight is 1/15 of 4/5 of total height
    int xGrids = 12, yGrids = 15;
    int xWidth, yHeight;
    int endX, endY;
    double distanceBetweenNextPointsOnXAxis;

    BigDecimal startValueOnY, endValueOnY, rangeOfValues,
            workingWidth, workingHeight, scale;

    Map<Path2D.Double, Color> plottedLines;

    Color gridColor = new Color(134, 133, 133),
            backgroundColor = new Color(102, 102, 102),
            gridBackgroundColor = new Color(203, 203, 203);

    public Plot(BigDecimal startValueOnY, BigDecimal endValueOnY){
        this.startValueOnY = startValueOnY;
        this.endValueOnY = endValueOnY;
        plottedLines = new HashMap<>();
    }

    // Calculates values for scale and distanceBetweenValues
    public abstract void setXScaling();

    // Subclasses are free to provide it's own representation
    // on X axis
    public abstract void drawXLabels(Graphics2D g2D);

    public abstract double findStartXCoordinate(Point<E> firstValue);

    // As a Y parameter we'll always have a BigDecimal
    public void drawYLabels(Graphics2D g2D){

        int x = endX + (int)(.1 * xWidth);
        BigDecimal currentLabelValue = startValueOnY;

        // Measures how one cell represents values on y axis
        BigDecimal weightOfCell = new BigDecimal(yHeight).divide(scale, RoundingMode.HALF_UP);

        for (int i = 0; i <= yGrids; i++){
            currentLabelValue = startValueOnY.add(weightOfCell.multiply(new BigDecimal(i)));
            g2D.drawString(String.valueOf(currentLabelValue),
                    x, endY - (i * yHeight));
        }

        g2D.setStroke(new BasicStroke(2.0f));
        g2D.setPaint(Color.BLACK);

        g2D.drawLine(x, 0, x, endY);

        // Relative error should be less than 10%
        assert Math.abs(endValueOnY.subtract(currentLabelValue).divide(endValueOnY, RoundingMode.HALF_UP)
                .doubleValue()) < 0.1;

    }

    public Path2D.Double drawGrid() {

        workingWidth = new BigDecimal(".9")
                .multiply(new BigDecimal(getWidth()));

        workingHeight = new BigDecimal(".9")
                .multiply(new BigDecimal(getHeight()));

        xWidth = workingWidth
                .divide(new BigDecimal(xGrids), RoundingMode.HALF_UP)
                .intValue();

        yHeight = workingHeight
                .divide(new BigDecimal(yGrids), RoundingMode.HALF_UP)
                .intValue();

        endX = xWidth * xGrids;
        endY = yHeight * yGrids;

        Path2D.Double path = new Path2D.Double();

        for (int i = 0; i <= yGrids; i++){
            path.moveTo(0, i * yHeight);
            path.lineTo(endX, i * yHeight);
        }

        for (int i = 0; i <= xGrids; i++){
            path.moveTo(i * xWidth, 0);
            path.lineTo( i * xWidth, endY);
        }

        System.out.println("endX=" + endX + ", endY=" + endY);

        return path;

    }

    // Draw grid and labels for both axes
    public void createPlotBackground(Graphics2D g2D){

        g2D.setPaint(gridColor);

        float miterLimit = 10.0f;
        float[] dashPatterns = {3.0f};
        // The offset to start dashing pattern
        float dashPhase = 0;
        g2D.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_BEVEL, miterLimit, dashPatterns, dashPhase));

        Path2D.Double plot = drawGrid();
        g2D.draw(plot);

        // TODO: make font size depend on something else
        g2D.setFont(new Font("Roboto", Font.PLAIN, (int)(xWidth * .2)));
        drawYLabels(g2D);
        drawXLabels(g2D);
    }

    public double findYCoordinateForPoint(Point<E> p){

        return endY - p.getY()
                .subtract(startValueOnY)
                .multiply(scale).doubleValue();
    }

    public void plotLine(List<Point<E>> data){

        plotLine(data.iterator());
    }

    void plotLine(Iterator<Point<E>> itr){

        drawGrid();

        setYScaling();
        setXScaling();

        Path2D.Double path = new Path2D.Double();
        Point<E> point = itr.next();
        double x = findStartXCoordinate(point);
        System.out.println("Start x= " + x);
        path.moveTo(x, findYCoordinateForPoint(point));

        while (itr.hasNext()){
            path.lineTo(x, findYCoordinateForPoint(point));
            point = itr.next();
            x += distanceBetweenNextPointsOnXAxis;
        }

        Random random = new Random();
        int rgb = 256;

        plottedLines.put(path, new Color(random.nextInt(rgb), random.nextInt(rgb), random.nextInt(rgb)));


    }

    public void setYScaling(){
        rangeOfValues = endValueOnY.subtract(startValueOnY).abs();
        scale = new BigDecimal(endY).divide(rangeOfValues, RoundingMode.HALF_UP);
    }

    // Given x and y coordinates, returns a point
    public abstract Point<E> calculateXValueBasedOnCoordinates(double x, double y);

    @Override
    public void paintComponent(Graphics g){

        Graphics2D g2D = (Graphics2D) g;

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                // This should smooth the edges
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2D.setBackground(gridBackgroundColor);

        g2D.translate(20, 20);

        createPlotBackground(g2D);

        g2D.setStroke(new BasicStroke(2.0f));

        g2D.setPaint(Color.blue);

        for (Map.Entry<Path2D.Double, Color> p : plottedLines.entrySet()){
            g2D.setPaint(p.getValue());
            g2D.draw(p.getKey());
        }

    }

}
