package dataconverter.writersandreaders;

import dataconverter.Formatter;

import java.util.Vector;

public class VectorToCSV implements Formatter<Vector> {

    @Override
    public String getAsCSVString(Vector v) {
        StringBuilder sB = new StringBuilder();
        for (Object o : v){
            sB.append(o).append(delimiter);
        }
        return sB.substring(0, sB.length() - 1);
    }

    @Override
    public Vector parseFromCSVString(String stringToParse) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
