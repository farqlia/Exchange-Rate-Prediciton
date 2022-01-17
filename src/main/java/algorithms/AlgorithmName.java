package algorithms;

import algorithms.algorithmsparameters.AlgorithmArguments;
import algorithms.algorithmsparameters.AlgorithmArgumentsPanel1;
import algorithms.algorithmsparameters.AlgorithmArgumentsPanel2;
import algorithms.algorithmsparameters.AlgorithmArgumentsPanelDefault;
import datasciencealgorithms.utils.point.Point;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import static algorithms.algorithmsparameters.AlgorithmArguments.Names;

public enum AlgorithmName {

    LINEARLY_WEIGHTED_MOVING_AVERAGE_ALGORITHM(DialogStrategy.STRATEGY_1){
        public Algorithm createAlgorithm(BlockingQueue<Point> queue, Map<Names, ? extends Number> arg){
            return new LinearlyWeightedMovingAverage(queue, arg.get(Names.LOOK_BACK_PERIOD));
        }
    },
    MOVING_AVERAGE_MEAN_ALGORITHM(DialogStrategy.STRATEGY_1){
        public Algorithm createAlgorithm(BlockingQueue<Point> queue, Map<Names, ? extends Number> arg){
            return new MovingAverageMeanAlgorithm(queue, arg.get(Names.LOOK_BACK_PERIOD));
        }
    },
    NAIVE_ALGORITHM_WITH_TREND(DialogStrategy.STRATEGY_DEFAULT){
        public Algorithm createAlgorithm(BlockingQueue<Point> queue, Map<Names, ? extends Number> arg){
            return new NaiveAlgorithmWithTrend(queue);
        }
    },
    NAIVE_ALGORITHM_WITH_TREND_AND_AVERAGE_INCREMENT(DialogStrategy.STRATEGY_1){
        public Algorithm createAlgorithm(BlockingQueue<Point> queue, Map<Names, ? extends Number> arg){
            return new NaiveAlgorithmWithTrendAndAverageIncrement(queue, arg.get(Names.LOOK_BACK_PERIOD));
        }
    },
    BROWN_EXPONENTIAL_SMOOTHING_MODEL(DialogStrategy.STRATEGY_2){
        public Algorithm createAlgorithm(BlockingQueue<Point> queue, Map<Names, ? extends Number> arg){
            return new BrownExponentialSmoothingModel(queue, (BigDecimal) arg.get(Names.ALPHA));
        }
    };

    // Some algorithms are initialized the same so they can share a common instance (JDialog in this case)
    enum DialogStrategy{

        STRATEGY_1(new AlgorithmArgumentsPanel1()),
        STRATEGY_2(new AlgorithmArgumentsPanel2()),
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
    private DialogStrategy displayStrategy;

    AlgorithmName(DialogStrategy displayStrategy){
        this.displayStrategy = displayStrategy;
        description = parseToReadableForm(this.name());
    }

    @Override
    public String toString(){
        return description;
    }

    public abstract Algorithm createAlgorithm(BlockingQueue<Point> queue, Map<Names, ? extends Number> arg);

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
