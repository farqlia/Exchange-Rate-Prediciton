package plot;

import datasciencealgorithms.utils.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class Plot<E> extends JComponent {

    int xWidth = 100, yWidth = 50;
    int startX = 20, startY = 20, endX, endY, x0, y0;
    int numOfXGrids, numOfYGrids;
    double distanceToNextPoint, scale;
    Color backgroundColor = new Color(234, 234, 234, 136),
            gridColor = new Color(208, 208, 208);
    Random random = new Random();
    List<Path2D.Double> plottedLines;
    Point<E> debuggingPoint;

    public Plot(){
        plottedLines = new ArrayList<>();
    }

    public Path2D.Double drawGrid(){

        System.out.println("Width= " + this.getWidth());
        System.out.println("Height= " + this.getHeight());

        numOfXGrids = this.getWidth() / xWidth - 1;
        numOfYGrids = this.getHeight() / yWidth - 1;
        endX = startX + (xWidth * numOfXGrids);
        endY = startY + (yWidth * numOfYGrids);

        Path2D.Double path = new Path2D.Double();

        for (int i = 0; i <= numOfYGrids; i++){
            path.moveTo(startX, startY + i * yWidth);
            path.lineTo(endX, startY + i * yWidth);
        }

        for (int i = 0; i <= numOfXGrids; i++){
            path.moveTo(startX + i * xWidth, startY );
            path.lineTo(startX + i * xWidth, endY );
        }

        System.out.println("endX=" + endX + ", endY=" + endY);
        System.out.println("numOfXGrids=" + numOfXGrids + "numOfYGrids=" + numOfYGrids);

        return path;
    }

    // TODO : implement this method for x axis
    public void drawXLabels(Graphics2D g2D, Point<E> lastPointOfData){

    }

    public void drawYLabels(Graphics2D g2D, Point<E> lastPointOfData){

        // Width of one grid divided by scale
        BigDecimal weightOfCell = new BigDecimal(yWidth)
                .divide(BigDecimal.valueOf(scale), 2, RoundingMode.HALF_UP);
        BigDecimal currentLabelValue = weightOfCell;

        // Number of grids below Y axis
        int j = - (endY - y0) / yWidth;

        for (int i = j; i < (numOfYGrids + j); i++){
            currentLabelValue = weightOfCell.multiply(new BigDecimal(i));
            g2D.drawString(String.valueOf(currentLabelValue),
                    endX, y0 - (float)(yWidth * i));
        }

        // Just to check that the values on labels are always correct
        assert Math.abs((currentLabelValue).doubleValue())
                <= Math.abs(lastPointOfData.getY().doubleValue());
    }

    public void setScaling(java.util.List<Point<E>> data){

        // For now, we calculate a distance between two next points like this
        distanceToNextPoint = ((double) (endX - startX) / data.size());

        // The biggest positive value or zero
        double biggestValue = data.stream()
                .map(Point::getY)
                .filter(x -> x.compareTo(BigDecimal.ZERO) > 0)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO)
                .doubleValue();

        // The smallest negative value or zero
        double smallestValue = data.stream()
                .map(Point::getY)
                .filter(x -> x.compareTo(BigDecimal.ZERO) < 0)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO)
                .doubleValue();

        double rangeOfValues = Math.abs(biggestValue - smallestValue);
        double height = ((numOfYGrids - 1) * yWidth);      // Area of plot where we want to draw lines
        scale = height / rangeOfValues;

        // This value is calculated in a way that Y axis lays on a grid
        y0 = startY + BigDecimal.valueOf(Math.abs(biggestValue / rangeOfValues) * (numOfYGrids))
                .setScale(0, RoundingMode.DOWN).intValue() * yWidth;
                // * ) ;

        x0 = startX;

        System.out.println("Distance between to next points=" + distanceToNextPoint);
        System.out.println("Height=" + height);
        System.out.println("Range of Values=" + rangeOfValues);
        System.out.println("Scale=" + scale);
        System.out.println("y0=" + y0);
    }

    public void plotData(java.util.List<Point<E>> data){
        drawGrid();
        plottedLines.add(getPathOfPlottedLine(data));
        debuggingPoint = data.get(data.size() - 1);
    }

    private Path2D.Double getPathOfPlottedLine(java.util.List<Point<E>> data){

        Path2D.Double path = new Path2D.Double();

        setScaling(data);

        path.moveTo(x0, y0);

        double thisX = x0;

        for (int i = 0; i < data.size(); i++) {
            thisX += distanceToNextPoint;
            double thisY = y0 - (data.get(i).getY().doubleValue() * scale);
            path.lineTo(thisX, thisY);

            System.out.println("Current point=" + data.get(i));
            System.out.println("Coordinates of current point=" + thisX +", " + thisY);
        }

        return path;
    }

    public void getMainLines(Graphics2D g2D){

        g2D.setPaint(gridColor);
        g2D.setStroke(new BasicStroke(2.0f));

        Path2D.Double mainLines = new Path2D.Double();
        mainLines.moveTo(x0, y0);
        mainLines.lineTo(endX, y0);

        mainLines.moveTo(endX, startY);
        mainLines.lineTo(endX, this.getHeight());

        mainLines.moveTo(startX, endY);
        mainLines.lineTo(this.getWidth(), endY);

        g2D.draw(mainLines);
    }

    public void createPlotBackground(Graphics2D g2D){

        Rectangle2D.Double rect = new Rectangle2D.Double(0, 0, g2D.getClipBounds().width, g2D.getClipBounds().height);

        g2D.setPaint(backgroundColor);
        g2D.draw(rect);
        g2D.fill(rect);

        g2D.setPaint(gridColor);

        float miterLimit = 10.0f;
        float[] dashPatterns = {3.0f};
        // The offset to start dashing pattern
        float dashPhase = 0;
        g2D.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_BEVEL, miterLimit, dashPatterns, dashPhase));

        g2D.draw(drawGrid());

    }

    @Override
    public void paintComponent(Graphics g){

        Graphics2D g2D = (Graphics2D) g;

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                // This should smooth the edges
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Draws the grid
        createPlotBackground(g2D);

        getMainLines(g2D);
        g2D.setStroke(new BasicStroke(2.0f));

        for (Path2D.Double p : plottedLines){
            g2D.setPaint(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g2D.draw(p);
        }

        drawYLabels(g2D, debuggingPoint);

    }

}
