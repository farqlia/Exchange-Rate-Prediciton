package view.view;

import javax.swing.*;
import java.util.*;

public abstract class AbstractView extends JFrame {

    List<ViewObserver> observers;

    public AbstractView(){
        observers = new ArrayList<>();
    }

    // Should disable button for the time of computations
    public abstract void disableActions();
    public abstract void enableActions();

    public void registerObserver(ViewObserver o){
        observers.add(o);
    }

    public void notifyObservers(ViewEvent data) {
        for (ViewObserver observer : observers){
            observer.update(data);
        }
    }
}
