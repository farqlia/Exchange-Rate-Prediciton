package algorithmstests;

import algorithms.AlgorithmName;
import algorithms.LinearlyWeightedMovingAverage;
import algorithms.NaiveAlgorithmWithTrend;
import datasciencealgorithms.utils.point.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

@ExtendWith(MockitoExtension.class)
public class AlgorithmNamesTest {

    @Mock
    BlockingQueue<Point> queue;

    @Test
    void shouldReturnNameOfAlgorithm(){
        Assertions.assertEquals(AlgorithmName.LINEARLY_WEIGHTED_MOVING_AVERAGE_ALGORITHM.toString(),
                "Linearly Weighted Moving Average Algorithm");
    }

    @Test
    void shouldReturnCorrectInstance(){

        Assertions.assertTrue(AlgorithmName.LINEARLY_WEIGHTED_MOVING_AVERAGE_ALGORITHM.createAlgorithm(queue, 5)
        instanceof LinearlyWeightedMovingAverage);
    }

    @Test
    void shouldReturnCorrectInstance2(){
        Assertions.assertTrue(AlgorithmName.NAIVE_ALGORITHM_WITH_TREND.createAlgorithm(queue, 5)
                instanceof NaiveAlgorithmWithTrend);
    }
}
