package dataconverter.writersandreaders;

import dataconverter.formatters.Formatter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class TextFileWriter<E> extends CustomFileWriter<E> {

    Formatter<E> p;

    public TextFileWriter(Formatter<E> printer){
        super("txt");
        p = printer;
    }


    @Override
    void handleObjectWriting(String path, List<? extends E> data) throws IOException {

        // We assume that the fields are separated by a comma
        try (PrintWriter pW = new PrintWriter(new BufferedWriter(new FileWriter(path)))){

            for (E e: data){
                pW.println(p.getAsCSVString(e));
            }
        }
    }

}
