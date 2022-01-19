package dataconverter.writersandreaders;



import dataconverter.formatters.Formatter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextFileReader<E> extends CustomFileReader<E> {

    Formatter<E> p;

    public TextFileReader(Formatter<E> printer){
        super("txt");
        p = printer;
    }

    @Override
    List<E> handleObjectReading(String path) throws IOException {

        List<E> list;

        try (BufferedReader bR = new BufferedReader(new FileReader(path))){
            list = new ArrayList<>();

            String line;
            while ((line = bR.readLine()) != null){
                list.add(p.parseFromCSVString(line));
            }

        }
        return list;
    }

}
