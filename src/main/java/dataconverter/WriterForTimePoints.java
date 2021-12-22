package dataconverter;

import datasciencealgorithms.utils.Point;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WriterForTimePoints extends CustomFileWriter<Point<LocalDate>> {

    private final String delimiter = ",";
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public WriterForTimePoints(){
        super("txt");
    }

    @Override
    void handleObjectWriting(String path, List<Point<LocalDate>> data) throws IOException {

        StringBuilder builder = new StringBuilder();
        // We assume that the fields are separated by a comma
        try (PrintWriter pW = new PrintWriter(new BufferedWriter(new FileWriter(path)))){

            for (Point<LocalDate> point: data){

                builder.append(point.getX().format(dateFormatter))
                        .append(delimiter)
                        .append(point.getY());
                pW.println(builder);
                builder.delete(0, builder.length());

            }
        }
    }

}
