package algorithms;

import datasciencealgorithms.utils.point.Point;

import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.function.Function;

public enum AlgorithmName {

    LINEARLY_WEIGHTED_MOVING_AVERAGE_ALGORITHM{
        public Algorithm createAlgorithm(BlockingQueue<Point> queue, int lookbackPeriod){
            return new LinearlyWeightedMovingAverage(queue, lookbackPeriod);
        }
    },
    MOVING_AVERAGE_MEAN_ALGORITHM{
        public Algorithm createAlgorithm(BlockingQueue<Point> queue, int lookbackPeriod){
            return new MovingAverageMeanAlgorithm(queue, lookbackPeriod);
        }
    },
    NAIVE_ALGORITHM_WITH_TREND{
        public Algorithm createAlgorithm(BlockingQueue<Point> queue, int lookbackPeriod){
            return new NaiveAlgorithmWithTrend(queue);
        }
    },
    NAIVE_ALGORITHM_WITH_TREND_AND_AVERAGE_INCREMENT{
        public Algorithm createAlgorithm(BlockingQueue<Point> queue, int lookbackPeriod){
            return new NaiveAlgorithmWithTrendAndAverageIncrement(queue, lookbackPeriod);
        }
    };

    private final String description;

    AlgorithmName(){
        description = parseToReadableForm(this.name());
    }

    @Override
    public String toString(){
        return description;
    }

    public abstract Algorithm createAlgorithm(BlockingQueue<Point> queue, int lookbackPeriod);

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
