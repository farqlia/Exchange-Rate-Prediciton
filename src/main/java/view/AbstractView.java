package view;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.*;

public abstract class AbstractView extends JFrame {

    List<ViewObserver> observers;

    public AbstractView(){
        observers = new ArrayList<>();
    }

    public void registerObserver(ViewObserver o){
        observers.add(o);
    }

    public void notifyObservers(ViewEvent data) {
        for (ViewObserver observer : observers){
            observer.update(data);
        }
    }
}
