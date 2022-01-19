package utilstests;

import dataconverter.formatters.Formatter;
import dataconverter.formatters.RowToCSV;
import dataconverter.formatters.PointToCSV;
import dataconverter.formatters.VectorToCSV;
import datasciencealgorithms.utils.point.Point;
import model.ResultsTableModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Vector;

public class CSVPrintableTest {

    Point p;
    Formatter<Point> csvConverter;

    @BeforeEach
    void setUp(){
        csvConverter = new PointToCSV();
        p = new Point(LocalDate.of(2021, 12, 23), new BigDecimal("1.00000000"));
    }

    @Test
    void shouldConvertToString(){
        // "23-12-2021,0"
        Assertions.assertEquals("23-12-2021,1.00000000", csvConverter.getAsCSVString(p));
    }

    @Test
    void shouldConvertFromString() throws IOException {
        Assertions.assertEquals(p, csvConverter.parseFromCSVString("23-12-2021,1.0000000000"));
    }

    @Test
    void shouldNtConvertFromString(){
        Exception e = Assertions.assertThrows(IOException.class,
                () -> csvConverter.parseFromCSVString("2312-2021,1"));
        Assertions.assertEquals("Data is incorrectly formatted", e.getMessage());
    }

    @Test
    void shouldConvertToStringFromVector(){

        Vector v = new Vector<>(List.of(LocalDate.of(2022, 1, 13), BigDecimal.ONE, BigDecimal.ZERO,
                BigDecimal.TEN));

        Assertions.assertEquals("2022-01-13,1,0,10", new VectorToCSV().getAsCSVString(v));

    }

    @Test
    void shouldConvertToStringFromRow(){

        Point p = new Point(LocalDate.of(2022, 1, 13), BigDecimal.ZERO);
        ResultsTableModel.Row r = new ResultsTableModel.Row(
                p, p, BigDecimal.ZERO, BigDecimal.ONE
        );

        Assertions.assertEquals("2022-01-13,0,0,0,1", new RowToCSV().getAsCSVString(r));

    }

}
