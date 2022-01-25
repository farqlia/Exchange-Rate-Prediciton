package algorithms.algorithmsinitializer;

import algorithms.Algorithm;
import algorithms.BrownExponentialSmoothingModel;
import algorithms.FutureForecastAlgortihm;
import algorithms.algorithmsparameters.AlgorithmArguments;
import datasciencealgorithms.utils.point.Point;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.function.BiFunction;

public class AlgorithmInitializerExAnte extends AlgorithmInitializer{

    public static AlgorithmInitializerExAnte FA = new AlgorithmInitializerExAnte(DialogStrategy.STRATEGY_AB,
            (q, m) -> new FutureForecastAlgortihm(q, (BigDecimal) m.get(AlgorithmArguments.Names.ALPHA), (BigDecimal) m.get(AlgorithmArguments.Names.BETA)),
            "FUTURE_ALGORITHM");
    public static AlgorithmInitializer[] values = new AlgorithmInitializer[]{FA};

    private AlgorithmInitializerExAnte(DialogStrategy displayStrategy, BiFunction<BlockingQueue<Point>, Map<AlgorithmArguments.Names, ? extends Number>, Algorithm> createAlgorithmExpression, String name) {
        super(displayStrategy, createAlgorithmExpression, name);
    }

    public static AlgorithmInitializer[] values(){
        return values;
    }

}
