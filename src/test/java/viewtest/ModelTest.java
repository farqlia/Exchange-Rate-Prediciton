package viewtest;

import algorithms.AlgorithmName;
import dataconverter.IncorrectDataFormat;
import dataconverter.writersandreaders.CSVParser;
import dataconverter.writersandreaders.TextFileReader;
import datasciencealgorithms.utils.UtilityMethods;
import datasciencealgorithms.utils.point.Point;
import mathlibraries.ScienceLibrary;
import mathlibraries.Statistics;
import model.Model;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class ModelTest {

    Model model;
    List<Point> exampleDataPoints;

    @BeforeEach
    void setUp() throws IOException, IncorrectDataFormat {
        model = new Model();
        exampleDataPoints = new TextFileReader<Point>(CSVParser.getInstance())
                .readFromFile("exampledata\\dp2.txt");
    }

    @Test
    void shouldChooseCorrectAlgorithm(){
        model.setAlgorithm(AlgorithmName.LINEARLY_WEIGHTED_MOVING_AVERAGE_ALGORITHM, 5);

        LocalDate startDate = LocalDate.of(2021, 12, 23);

        List<Point> predicted = model.predict(exampleDataPoints, startDate,
                LocalDate.now());

        int startIndex = UtilityMethods.findIndexOfDate(startDate, exampleDataPoints);
        Assertions.assertEquals(exampleDataPoints.size() - startIndex, predicted.size());
    }



}
