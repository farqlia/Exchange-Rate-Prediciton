package algorithms;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

public class SleepingThread {

    static int SLEEP = 5;
    static boolean DEBUG = true;
    public static void sleep() throws InterruptedException {
        if (DEBUG){
            Thread.sleep(Duration.ofSeconds(ThreadLocalRandom.current().nextInt(SLEEP)).toMillis());
        }
    }

}