package dataconverter;

import datasciencealgorithms.utils.Point;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class TextDataConverterForTimePoints extends DataConverter<Point<LocalDate>>{

    public TextDataConverterForTimePoints(){
        super("txt");
    }

    @Override
    void handleObjectWriting(String path, List<Point<LocalDate>> data) {



    }

    @Override
    List<Point<LocalDate>> handleObjectReading(String path) {
        return null;
    }
}
