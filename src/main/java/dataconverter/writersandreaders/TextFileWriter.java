package dataconverter.writersandreaders;

import dataconverter.PrintableAsCSV;
import dataconverter.writersandreaders.CustomFileWriter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class TextFileWriter<E> extends CustomFileWriter<E> {

    PrintableAsCSV<E> p;

    public TextFileWriter(PrintableAsCSV<E> printer){
        super("txt");
        p = printer;
    }


    @Override
    void handleObjectWriting(String path, List<E> data) throws IOException {

        // We assume that the fields are separated by a comma
        try (PrintWriter pW = new PrintWriter(new BufferedWriter(new FileWriter(path)))){

            for (E e: data){
                pW.println(p.getAsCSVString(e));
            }
        }
    }

}
