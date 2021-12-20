package studygraphics;

import datasciencealgorithms.utils.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

public class GraphicsComponent<E> extends JComponent {

    int xWidth = 100, yWidth = 50;
    int startX = 0, startY = 0, endX, endY;
    int numOfXGrids, numOfYGrids;
    double distanceToNextPoint, scale;
    Color backgroundColor = new Color(234, 234, 234, 136),
            gridColor = new Color(208, 208, 208);
    Random random = new Random();

    List<Point<E>> data;

    public GraphicsComponent(List<Point<E>> data){
        this.data = data;
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

    public void drawYLabels(Graphics2D g2D, float x, float y){

        // Width of one grid divided by scale
        BigDecimal weightOfCell = new BigDecimal(yWidth)
                .divide(BigDecimal.valueOf(scale), 2, RoundingMode.HALF_UP);
        BigDecimal currentLabelValue = weightOfCell;

        for (int i = 0; i < numOfYGrids; i++){
            currentLabelValue = weightOfCell.multiply(new BigDecimal(i));
            g2D.drawString(String.valueOf(currentLabelValue),
                    x, y - (float)(yWidth * i));
        }

        // Just to check that the values on labels are always correct
        assert Math.abs(((currentLabelValue).doubleValue())
                - data.get(data.size() - 1).getY().doubleValue()) < 1;

    }

    // TODO: the scale and distances should be calculated based on all the
    //  plots, so that all figures fit
    public void setScaling(){

        // For now, we calculate a distance between two next points like this
        distanceToNextPoint = ((double) (endX - xWidth) / data.size());

        // We retrieve the biggest value from the data and set scale
        // by dividing the width by this number
        double biggestValue = (data.stream()
                .map(Point::getY)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO)
                .doubleValue());
        scale = (endY - yWidth) / biggestValue;

        System.out.println("Distance between to next points=" + distanceToNextPoint);
        System.out.println("Scale=" + scale);

    }

    // TODO: make this method take a list an return a plotted line for it's values
    public void plotData(Graphics2D g2D){

        Path2D.Double path = new Path2D.Double();
        setScaling();

        g2D.setPaint(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        g2D.setStroke(new BasicStroke(2.0f));

        // Set the point to start drawing the plot from
        path.moveTo(startX, endY - (data.get(0).getY().doubleValue() * scale));


        double thisX = startX;

        for (int i = 0; i < data.size(); i++) {
            thisX += distanceToNextPoint;
            double thisY = endY - (data.get(i).getY().doubleValue() * scale);
            path.lineTo(thisX, thisY);

            System.out.println("Current point=" + data.get(i));
            System.out.println("Coordinates of current point=" + thisX +", " + thisY);
        }

        g2D.draw(path);
    }

    public void getMainLines(Graphics2D g2D){

        g2D.setPaint(gridColor);
        g2D.setStroke(new BasicStroke(2.0f));

        Path2D.Double mainLines = new Path2D.Double();
        mainLines.moveTo(startX, endY);
        mainLines.lineTo(this.getWidth(), endY);

        mainLines.moveTo(endX, startY);
        mainLines.lineTo(endX, this.getHeight());

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

        createPlotBackground(g2D);

        plotData(g2D);

        /*
        Rectangle2D.Double rect = new Rectangle2D.Double(endX, startY, this.getWidth() - endX, this.getHeight());
        g2D.setPaint(new Color(127, 141, 137));
        g2D.fill(rect);
        g2D.draw(rect);

        rect = new Rectangle2D.Double(startX, endY, this.getWidth(), this.getHeight() - endY);
        g2D.fill(rect);
        g2D.draw(rect);

         */

        getMainLines(g2D);

        drawYLabels(g2D, (float)(endX), (float)(endY));

    }

}
