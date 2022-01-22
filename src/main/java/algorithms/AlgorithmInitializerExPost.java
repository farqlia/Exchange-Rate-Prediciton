package algorithms;

import algorithms.algorithmsparameters.*;
import datasciencealgorithms.utils.point.Point;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.function.BiFunction;

import static algorithms.algorithmsparameters.AlgorithmArguments.Names;

public enum AlgorithmInitializerExPost{

    LINEARLY_WEIGHTED_MOVING_AVERAGE_ALGORITHM(DialogStrategy.STRATEGY_LBP,
            (q, m) -> new LinearlyWeightedMovingAverage(q, m.get(Names.LOOK_BACK_PERIOD))),

    MOVING_AVERAGE_MEAN_ALGORITHM(DialogStrategy.STRATEGY_LBP,
            (q, m) -> new MovingAverageMeanAlgorithm(q, m.get(Names.LOOK_BACK_PERIOD))),

    NAIVE_ALGORITHM_WITH_TREND(DialogStrategy.STRATEGY_DEFAULT,
            (q, m) -> new NaiveAlgorithmWithTrend(q)),

    NAIVE_ALGORITHM_WITH_TREND_AND_AVERAGE_INCREMENT(DialogStrategy.STRATEGY_LBP,
            (q, m) -> new NaiveAlgorithmWithTrendAndAverageIncrement(q, m.get(Names.LOOK_BACK_PERIOD))),

    BROWN_EXPONENTIAL_SMOOTHING_MODEL(DialogStrategy.STRATEGY_A,
            (q, m) -> new BrownExponentialSmoothingModel(q, (BigDecimal) m.get(Names.ALPHA))),

    HOLT_EXPONENTIAL_SMOOTHING_MODEL(DialogStrategy.STRATEGY_AB,
            (q, m) -> new HoltExponentialSmoothingModel(q, (BigDecimal) m.get(Names.ALPHA), (BigDecimal) m.get(Names.BETA)));

    // Some algorithms are initialized the same so they can share a common instance (JDialog in this case)
    enum DialogStrategy{

        STRATEGY_LBP(new AlgorithmArgumentsPanelLBP()),
        STRATEGY_A(new AlgorithmArgumentsPanelA()),
        STRATEGY_AB(new AlgorithmArgumentsPanelAB()),
        STRATEGY_DEFAULT(new AlgorithmArgumentsPanelDefault());

        private final AlgorithmArguments algorithmArguments;
        DialogStrategy(AlgorithmArguments algorithmArguments){
            this.algorithmArguments = algorithmArguments;
        }

        AlgorithmArguments getStrategy(){
            return algorithmArguments;
        }
    }

    private final String description;
    private final BiFunction<BlockingQueue<Point>, Map<Names, ? extends Number>, Algorithm> createAlgorithmExpression;
    private final DialogStrategy displayStrategy;

    AlgorithmInitializerExPost(DialogStrategy displayStrategy,
                               BiFunction<BlockingQueue<Point>, Map<Names, ? extends Number>, Algorithm> createAlgorithmExpression){

        this.displayStrategy = displayStrategy;
        this.createAlgorithmExpression = createAlgorithmExpression;
        description = parseToReadableForm(this.name());
    }

    @Override
    public String toString(){
        return description;
    }

    public Algorithm createAlgorithm(BlockingQueue<Point> queue, Map<Names, ? extends Number> arg){
        return createAlgorithmExpression.apply(queue, arg);
    };

    public AlgorithmArguments getAlgorithmArguments(){
        return displayStrategy.getStrategy();
    }

    private String parseToReadableForm(String word){

        StringBuilder builder = new StringBuilder(word.length());

        for (String subStr : word.split("_")){
            builder.append(subStr.charAt(0));
            builder.append(subStr.substring(1).toLowerCase(Locale.ROOT));
            builder.append(" ");
        }
        return builder.toString().trim();
    }

}
