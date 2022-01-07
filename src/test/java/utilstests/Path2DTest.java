package utilstests;

import org.junit.jupiter.api.Test;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.Arrays;

public class Path2DTest {

    @Test
    void testPath(){
        Path2D.Double p = new Path2D.Double();

        p.moveTo(0, 0);
        for (int i = 0; i < 10; i++){
            p.lineTo(i, i);
        }

        double[] points = new double[6];
        PathIterator itr = p.getPathIterator(null);
        while(!itr.isDone()){
            int type = itr.currentSegment(points);
            System.out.println(type);
            System.out.println(Arrays.toString(points));
            itr.next();
        }
    }

}
