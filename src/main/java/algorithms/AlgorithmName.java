package algorithms;

import java.util.Locale;
import java.util.function.Function;

public enum AlgorithmName {

    LINEARLY_WEIGHTED_MOVING_AVERAGE_ALGORITHM(LinearlyWeightedMovingAverage::new),
    MOVING_AVERAGE_MEAN_ALGORITHM(MovingAverageMeanAlgorithm::new),
    NAIVE_ALGORITHM_WITH_TREND((x) -> new NaiveAlgorithmWithTrend()),
    NAIVE_ALGORITHM_WITH_TREND_AND_AVERAGE_INCREMENT(NaiveAlgorithmWithTrendAndAverageIncrement::new);

    private final String description;

    private final Function<Integer, Algorithm> construct;

    AlgorithmName(Function<Integer, Algorithm> construct){
        description = parseToReadableForm(this.name());
        this.construct = construct;
    }

    @Override
    public String toString(){
        return description;
    }

    public Function<Integer, Algorithm> createAlgorithm(){
        return construct;
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
