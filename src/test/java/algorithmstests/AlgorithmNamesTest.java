package algorithmstests;

import algorithms.AlgorithmName;
import algorithms.BrownExponentialSmoothingModel;
import algorithms.LinearlyWeightedMovingAverage;
import algorithms.NaiveAlgorithmWithTrend;
import algorithms.algorithmsparameters.AlgorithmArguments;
import algorithms.algorithmsparameters.AlgorithmArgumentsPanel1;
import datasciencealgorithms.utils.point.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.processor.core.AbstractListProcessor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;

@ExtendWith(MockitoExtension.class)
public class AlgorithmNamesTest {

    @Mock
    BlockingQueue<Point> queue;
    Map<AlgorithmArguments.Names, Number> argMap;

    @BeforeEach
    void setUp(){
        argMap = new HashMap<>();
    }

    @Test
    void shouldReturnNameOfAlgorithm(){
        Assertions.assertEquals(AlgorithmName.LINEARLY_WEIGHTED_MOVING_AVERAGE_ALGORITHM.toString(),
                "Linearly Weighted Moving Average Algorithm");
    }

    @Test
    void shouldReturnCorrectInstance(){
        argMap.put(AlgorithmArguments.Names.LOOK_BACK_PERIOD,5);
        Assertions.assertTrue(AlgorithmName.LINEARLY_WEIGHTED_MOVING_AVERAGE_ALGORITHM.createAlgorithm(queue, argMap)
        instanceof LinearlyWeightedMovingAverage);
    }

    @Test
    void shouldReturnCorrectInstance2(){
        argMap.put(AlgorithmArguments.Names.LOOK_BACK_PERIOD, 5);
        Assertions.assertTrue(AlgorithmName.NAIVE_ALGORITHM_WITH_TREND.createAlgorithm(queue, argMap)
                instanceof NaiveAlgorithmWithTrend);
    }

    @Test
    void shouldReturnCorrectInstance3(){
        argMap.put(AlgorithmArguments.Names.ALPHA, new BigDecimal("0.7"));
        Assertions.assertTrue(AlgorithmName.BROWN_EXPONENTIAL_SMOOTHING_MODEL.createAlgorithm(queue, argMap)
                instanceof BrownExponentialSmoothingModel);
    }

}
