package utilstests;

import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class LocalDateTest {

    @Test
    void testDifferenceInDays(){
        LocalDate st = LocalDate.now().minusDays(60);
        LocalDate ed = LocalDate.now();
        System.out.println(ChronoUnit.DAYS.between(st, ed));
    }


    @Test
    void shouldConvertedFromDateToLocalDate(){

        Date d = new Date();
        LocalDate lD = LocalDate.ofInstant(d.toInstant(), ZoneId.of("CET"));

    }
}
