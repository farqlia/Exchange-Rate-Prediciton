package algorithmstests;

import algorithms.AlgorithmName;
import algorithms.LinearlyWeightedMovingAverage;
import algorithms.NaiveAlgorithmWithTrend;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class AlgorithmNamesTest {

    @Test
    void shouldReturnNameOfAlgorithm(){
        Assertions.assertEquals(AlgorithmName.LINEARLY_WEIGHTED_MOVING_AVERAGE_ALGORITHM.toString(),
                "Linearly Weighted Moving Average Algorithm");
    }

    @Test
    void shouldReturnCorrectInstance(){
        Assertions.assertTrue(AlgorithmName.LINEARLY_WEIGHTED_MOVING_AVERAGE_ALGORITHM.createAlgorithm().apply(5)
        instanceof LinearlyWeightedMovingAverage);
    }

    @Test
    void shouldReturnCorrectInstance2(){
        Assertions.assertTrue(AlgorithmName.NAIVE_ALGORITHM_WITH_TREND.createAlgorithm().apply(5)
                instanceof NaiveAlgorithmWithTrend);
    }
}
