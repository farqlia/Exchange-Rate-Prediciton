package view;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public abstract class AbstractView extends JFrame {

    List<ViewObserver> observers;

    public AbstractView(){
        observers = new ArrayList<>();
    }

    public abstract void insertAlgorithmOutput(Vector<Vector<Object>> data);

    public abstract void insertStatistics(Vector<Vector<Object>> data);

    public void registerObserver(ViewObserver o){
        observers.add(o);
    }

    public void notifyObservers(ViewEvent e){
        for (ViewObserver o : observers){
            o.update(e);
        }
    }
}
