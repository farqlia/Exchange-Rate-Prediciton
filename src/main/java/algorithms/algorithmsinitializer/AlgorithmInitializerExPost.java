package algorithms.algorithmsinitializer;

import algorithms.*;
import algorithms.algorithmsparameters.AlgorithmArguments;
import datasciencealgorithms.utils.point.Point;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.function.BiFunction;

public class AlgorithmInitializerExPost extends AlgorithmInitializer{

    public static AlgorithmInitializerExPost LWMAA = new AlgorithmInitializerExPost(DialogStrategy.STRATEGY_LBP,
            (q, m) -> new LinearlyWeightedMovingAverage(q, m.get(AlgorithmArguments.Names.LOOK_BACK_PERIOD)),
            "LINEARLY_WEIGHTED_MOVING_AVERAGE_ALGORITHM");

    public static AlgorithmInitializerExPost MAMA = new AlgorithmInitializerExPost(DialogStrategy.STRATEGY_LBP,
            (q, m) -> new MovingAverageMeanAlgorithm(q, m.get(AlgorithmArguments.Names.LOOK_BACK_PERIOD)),
            "MOVING_AVERAGE_MEAN_ALGORITHM");

    public static AlgorithmInitializerExPost NAWT = new AlgorithmInitializerExPost(DialogStrategy.STRATEGY_DEFAULT,
            (q, m) -> new NaiveAlgorithmWithTrend(q), "NAIVE_ALGORITHM_WITH_TREND");

    public static AlgorithmInitializerExPost NAWTAAI = new AlgorithmInitializerExPost(DialogStrategy.STRATEGY_LBP,
            (q, m) -> new NaiveAlgorithmWithTrendAndAverageIncrement(q, m.get(AlgorithmArguments.Names.LOOK_BACK_PERIOD)),
            "NAIVE_ALGORITHM_WITH_TREND_AND_AVERAGE_INCREMENT");

    public static AlgorithmInitializerExPost BESM = new AlgorithmInitializerExPost(DialogStrategy.STRATEGY_A,
            (q, m) -> new BrownExponentialSmoothingModel(q, (BigDecimal) m.get(AlgorithmArguments.Names.ALPHA)),
            "BROWN_EXPONENTIAL_SMOOTHING_MODEL");

    public static AlgorithmInitializerExPost HESM = new AlgorithmInitializerExPost(DialogStrategy.STRATEGY_AB,
            (q, m) -> new HoltExponentialSmoothingModel(q, (BigDecimal) m.get(AlgorithmArguments.Names.ALPHA), (BigDecimal) m.get(AlgorithmArguments.Names.BETA)),
            "HOLT_EXPONENTIAL_SMOOTHING_MODEL");
    public static AlgorithmInitializer[] values = new AlgorithmInitializer[]{LWMAA, MAMA, NAWT, NAWTAAI, BESM, HESM};

    private AlgorithmInitializerExPost(DialogStrategy displayStrategy, BiFunction<BlockingQueue<Point>, Map<AlgorithmArguments.Names, ? extends Number>, Algorithm> createAlgorithmExpression, String name) {
        super(displayStrategy, createAlgorithmExpression, name);
    }

    public static AlgorithmInitializer[] values(){
        return values;
    }
}
