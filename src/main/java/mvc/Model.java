package mvc;

import algorithms.*;
import algorithms.algorithmsinitializer.AlgorithmInitializer;
import datasciencealgorithms.utils.point.Point;
import model.AbstractModel;
import model.ModelEvent;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Model extends AbstractModel {

    private Algorithm algorithm;
    public final int CHUNK_SIZE = 5;
    private final BlockingQueue<Point> queue = new ArrayBlockingQueue<>(CHUNK_SIZE);
    private List<Point> realData;
    private List<Point> chunks;

    public void setAlgorithm(AlgorithmInitializer algorithmInitializer){

        algorithm = algorithmInitializer.createAlgorithm(queue,
                algorithmInitializer.getAlgorithmArguments().getMap());
    }

    @Override
    public List<Point> getRealData() {
        return realData;
    }

    public void predict(List<Point> data, LocalDate startDate, LocalDate endDate)
            throws IllegalStateException{

        Runnable producer = () -> {
            try {
                realData = data;
                algorithm.forecastValuesForDates(getRealData(), startDate, endDate);
            } catch (InterruptedException e) {
                throw new IllegalStateException("Illegal state");
            }
        };

        // Starts new thread that will consume data from queue and send it by chunks to observers (simplified)
        gather();
        Thread t = new Thread(producer);
        t.start();

    }

    private void gather(){

        chunks = new ArrayList<>();

        Runnable consumer = () -> {
            notifyObservers(ModelEvent.DATA_PROCESS_STARTED);
            boolean isDone = false;
            try{
                while(!isDone){
                    Point p = queue.take();
                    System.out.println(p);

                    // Means that computations are completed
                    if (Point.EMPTY_POINT.equals(p)){
                        if (!chunks.isEmpty()){
                            notifyObservers(ModelEvent.DATA_IN_PROCESS);
                        }
                        notifyObservers(ModelEvent.DATA_PROCESS_FINISHED);
                        isDone = true;
                    } else {
                        chunks.add(p);
                        if (chunks.size() == CHUNK_SIZE){
                            notifyObservers(ModelEvent.DATA_IN_PROCESS);
                            chunks.clear();
                        }
                    }

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Thread t = new Thread(consumer);
        t.start();

    }

    public List<Point> getDataChunk(){
        return chunks;
    }

}
