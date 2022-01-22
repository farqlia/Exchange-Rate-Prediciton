package algorithms;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

public class SleepingThread {

    static int SLEEP = 3;
    public static boolean DEBUG = false;

    public static void sleep() throws InterruptedException {
        if (DEBUG){
            Thread.sleep(Duration.ofSeconds(ThreadLocalRandom.current().nextInt(SLEEP)).toMillis());
        }
    }

}
