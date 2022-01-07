package viewtest;

import dataconverter.IncorrectDataFormat;
import dataconverter.PrintableAsCSV;
import dataconverter.writersandreaders.*;
import datagenerator.DataGenerator;
import datasciencealgorithms.utils.point.Point;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Prepare {

    public static void main(String[] args) throws IOException, IncorrectDataFormat {
        new Prepare();
    }

    public Prepare() throws IOException, IncorrectDataFormat {

        CustomFileWriter<Point<LocalDate>> tFW = new TextFileWriter<>(CSVParser.getInstance());

        tFW.saveToFile("exampledata\\dp1.txt", DataGenerator.getInstance().generateDataWithTrend(10, BigDecimal.ONE, BigDecimal.ONE));
        tFW.saveToFile("exampledata\\dp2.txt", DataGenerator.getInstance().generateDataWithTrend(10, BigDecimal.ONE, BigDecimal.ONE, 2));

        CustomFileReader<Point<LocalDate>> cFW = new TextFileReader<>(CSVParser.getInstance());
        cFW.readFromFile("exampledata\\dp1.txt").forEach(System.out::println);
        cFW.readFromFile("exampledata\\dp2.txt").forEach(System.out::println);


    }

    public static class PrintBigDec implements PrintableAsCSV<BigDecimal>{

        @Override
        public String getAsCSVString(BigDecimal o) {
            return o.toString();
        }

        @Override
        public BigDecimal parseFromCSVString(String stringToParse) throws IncorrectDataFormat {
            return new BigDecimal(stringToParse);
        }
    }

    public static class PrintLocDate implements PrintableAsCSV<LocalDate>{

        DateTimeFormatter dF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        @Override
        public String getAsCSVString(LocalDate o) {
            return o.format(dF);
        }

        @Override
        public LocalDate parseFromCSVString(String stringToParse) {
            return LocalDate.parse(stringToParse);
        }
    }

}
