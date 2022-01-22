package algorithms.algorithmsinitializer;

import algorithms.Algorithm;
import algorithms.algorithmsparameters.*;
import datasciencealgorithms.utils.point.Point;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.function.BiFunction;

public abstract class AlgorithmInitializer {

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
    private final BiFunction<BlockingQueue<Point>, Map<AlgorithmArguments.Names, ? extends Number>, Algorithm> createAlgorithmExpression;
    private final DialogStrategy displayStrategy;

    AlgorithmInitializer(DialogStrategy displayStrategy,
                               BiFunction<BlockingQueue<Point>, Map<AlgorithmArguments.Names, ? extends Number>,
                                       Algorithm> createAlgorithmExpression, String name){

        this.displayStrategy = displayStrategy;
        this.createAlgorithmExpression = createAlgorithmExpression;
        description = parseToReadableForm(name);
    }

    @Override
    public String toString(){
        return description;
    }

    public Algorithm createAlgorithm(BlockingQueue<Point> queue, Map<AlgorithmArguments.Names, ? extends Number> arg){
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
