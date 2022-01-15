package view;

import datasciencealgorithms.utils.point.Point;
import iterators.AscendingIterator;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class DataPointsFlattering {

    public static Vector<Vector> flatten(List<Point> realData, List<Point> predictedData) {

        Vector<Vector> flatData = new Vector<>(realData.size());

        Iterator<Point> bItr = new AscendingIterator(realData,
                predictedData.get(0).getX(), predictedData.get(predictedData.size() - 1).getX());

        Iterator<Point> sItr = new AscendingIterator(predictedData);

        for (int i = 0; (bItr.hasNext() && sItr.hasNext()); i++){
            Point p = bItr.next();
            flatData.add(new Vector<>(3));
            flatData.get(i).add(p.getX());
            flatData.get(i).add(p.getY());
            flatData.get(i).add(sItr.next().getY());
        }

        return flatData;
    }

    /**
     * Adds to each vector (row) elements from column Object[] data
     * @param data to be concat
     * @param args arrays to be concatenated, column-wise
     */
    public static void concat (Vector<Vector> data, Object[] ... args){

        for (Object[] arg : args){
            for (int i = 0; (i < arg.length && i < data.size()); i++){
                data.get(i).add(arg[i]);
            }
        }
    }

}
