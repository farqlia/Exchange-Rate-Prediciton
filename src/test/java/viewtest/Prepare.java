package viewtest;

import currencyparsing.currencyurlbuilders.ConcreteCurrencyURL;
import currencyparsing.currencyurlbuilders.MoneyType;
import currencyparsing.currencyurlbuilders.Table;
import currencyparsing.currencyurlworker.ExchangeRateLoader;
import currencyparsing.currencyurlworker.Loader;
import dataconverter.Formatter;
import dataconverter.writersandreaders.*;
import datasciencealgorithms.utils.point.Point;
import exchangerateclass.ExchangeRate;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Prepare {

    public static void main(String[] args) throws IOException, IllegalArgumentException {
        new Prepare();
    }

    public Prepare() throws IOException, IllegalArgumentException {

        CustomFileWriter<Point> tFW = new TextFileWriter<>(new PointToCSV());

        Loader<ExchangeRate> loader = new ExchangeRateLoader();
        loader.setCurrencyURL(new ConcreteCurrencyURL.Builder(MoneyType.CURRENCY)
                .addCurrencyCode("EUR")
                .addTable(Table.A)
                .addDate(30).build());

        List<Point> dataPoints = new ArrayList<>(10);

        for (ExchangeRate eR : loader.load()){
            dataPoints.add(new Point(LocalDate.ofInstant(eR.getEffectiveDate().toInstant(), ZoneId.systemDefault()),
                    eR.getMid()));
        }

        //tFW.saveToFile("exampledata\\dp1.txt", DataGenerator.getInstance().generateDataWithTrend(10, BigDecimal.ONE, BigDecimal.ONE));
        tFW.saveToFile("exampledata\\dp2.txt", dataPoints);

        CustomFileReader<Point> cFW = new TextFileReader<>(new PointToCSV());
        //cFW.readFromFile("exampledata\\dp1.txt").forEach(System.out::println);
        cFW.readFromFile("exampledata\\dp2.txt").forEach(System.out::println);


    }

    public static class PrintBigDec implements Formatter<BigDecimal> {

        @Override
        public String getAsCSVString(BigDecimal o) {
            return o.toString();
        }

        @Override
        public BigDecimal parseFromCSVString(String stringToParse) throws IOException {
            return new BigDecimal(stringToParse);
        }
    }

    public static class PrintLocDate implements Formatter<LocalDate> {

        DateTimeFormatter dF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        @Override
        public String getAsCSVString(LocalDate o) {
            return o.format(dF);
        }

        @Override
        public LocalDate parseFromCSVString(String stringToParse) throws IOException {
            return LocalDate.parse(stringToParse);
        }
    }

}
