package utilstests;

import dataconverter.IncorrectDataFormat;
import dataconverter.writersandreaders.CSVParser;
import datasciencealgorithms.utils.point.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CSVParserTest {

    Point p;

    @BeforeEach
    void setUp(){
        p = new Point(LocalDate.of(2021, 12, 23), new BigDecimal("1.0000000000"));
    }

    @Test
    void shouldConvertToString(){
        // "23-12-2021,0"
        Assertions.assertEquals("23-12-2021,1.0000000000", CSVParser.getInstance().getAsCSVString(p));
    }

    @Test
    void shouldConvertFromString() throws IncorrectDataFormat {
        Assertions.assertEquals(p, CSVParser.getInstance().parseFromCSVString("23-12-2021,1.0000000000"));
    }

    @Test
    void shouldNtConvertFromString(){
        Exception e = Assertions.assertThrows(IncorrectDataFormat.class,
                () -> CSVParser.getInstance().parseFromCSVString("2312-2021,1"));
        Assertions.assertEquals("Data is incorrectly formatted", e.getMessage());
    }

}
