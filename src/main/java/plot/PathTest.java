package plot;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.Arrays;

public class PathTest extends JFrame {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(PathTest::new);

    }

    public PathTest(){

        setSize(1200, 600);

        CustomComponent cC = new CustomComponent();

        add(cC);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);

    }

    private void debugData(Path2D.Double p){
        double[] points = new double[6];
        PathIterator itr = p.getPathIterator(null);
        while(!itr.isDone()){
            int type = itr.currentSegment(points);
            System.out.println(type);
            System.out.println(Arrays.toString(points));
            itr.next();
        }
    }

    class CustomComponent extends JComponent{

        @Override
        public void paintComponent(Graphics g){

            Graphics2D g2D = (Graphics2D) g;

            int width = 100;

            Path2D.Double p1 = new Path2D.Double();
            p1.moveTo(width, width);
            for (int i = 1; i < 5; i++){
                p1.lineTo(width, i * width);
            }

            g2D.draw(p1);

            g2D.setPaint(Color.BLUE);

            AffineTransform aF = new AffineTransform();
            //aF.translate(width, width);
            aF.rotate(Math.toRadians(-90), width, width);

            Path2D.Double p2 = (Path2D.Double)p1.clone();

            p2.transform(aF);

            g2D.draw(p2);

            g2D.setPaint(Color.GREEN);

            width = 50;

            Path2D.Double p3 = new Path2D.Double();
            p3.moveTo(0,0);
            for (int i = 1; i < 5; i++){
                p3.lineTo(i * width, 0);
            }

            g2D.draw(p3);

            g2D.setPaint(Color.MAGENTA);

            AffineTransform aF2 = new AffineTransform();

            aF2.translate(width, width);

            Path2D.Double p4 = (Path2D.Double)p3.clone();

            debugData(p4);

            p4.transform(aF2);

            g2D.draw(p4);

            System.out.println("After applying transformation");
            debugData(p4);

            g2D.setPaint(Color.CYAN);

            aF2.rotate(Math.toRadians(90), width, width);

            Path2D.Double p5 = (Path2D.Double)p3.clone();

            p5.transform(aF2);

            g2D.draw(p5);
        }

    }

}
