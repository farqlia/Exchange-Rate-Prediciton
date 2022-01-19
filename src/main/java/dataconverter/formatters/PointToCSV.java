package dataconverter.formatters;


import dataconverter.formatters.Formatter;
import datasciencealgorithms.utils.point.Point;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PointToCSV implements Formatter<Point> {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final Pattern objectPattern = Pattern.compile("(?<date>\\d{2}-\\d{2}-\\d{4})"
            + delimiter + "(?<val>\\d+\\.\\d+)");

    @Override
    public String getAsCSVString(Point point) {
        return point.getX().format(dateFormatter) + delimiter + point.getY();
    }

    @Override
    public Point parseFromCSVString(String stringToParse) throws IOException {
        Matcher matcher = objectPattern.matcher(stringToParse);

        if (!matcher.matches()){
            throw new IOException("Data is incorrectly formatted");
        }

        return new Point(LocalDate.parse(matcher.group("date"), dateFormatter),
                new BigDecimal(matcher.group("val")));

    }
}