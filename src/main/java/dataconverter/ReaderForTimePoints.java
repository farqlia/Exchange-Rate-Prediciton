package dataconverter;

import datasciencealgorithms.utils.Point;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReaderForTimePoints extends CustomFileReader<Point<LocalDate>> {

    private static final String delimiter = ",";
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final Pattern objectPattern = Pattern.compile("(?<date>\\d{2}-\\d{2}-\\d{4})"
            + delimiter + "(?<val>\\d+\\.\\d+)");

    public ReaderForTimePoints(){
        super("txt");
    }

    @Override
    List<Point<LocalDate>> handleObjectReading(String path) throws IOException, IncorrectDataFormat {

        List<Point<LocalDate>> list;
        Matcher matcher;

        try (BufferedReader bR = new BufferedReader(new FileReader(path))){

            list = new ArrayList<>();

            String line;
            while ((line = bR.readLine()) != null){

                matcher = objectPattern.matcher(line);

                if (!matcher.matches()){
                    throw new IncorrectDataFormat("Data is incorrectly formatted");
                }

                list.add(new Point<>(LocalDate.parse(matcher.group("date"), dateFormatter),
                        new BigDecimal(matcher.group("val"))));

            }

        }
        return list;
    }

}
