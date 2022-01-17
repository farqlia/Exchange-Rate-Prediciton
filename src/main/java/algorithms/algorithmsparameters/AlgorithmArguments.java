package algorithms.algorithmsparameters;

import java.util.Map;

// Class extending this interface will be responsible for collecting
// arguments that are necessary to create an algorithm
public interface AlgorithmArguments {

    enum Names{
        LOOK_BACK_PERIOD,
        ALPHA,
        BETA;
    }

    Map<Names, Number> getMap();
    void collectArguments();

}
