package view.view;

import javax.swing.*;
import java.util.*;

public abstract class AbstractView extends JFrame {

    Map<ViewEventType, List<ViewObserver>> observers;

    public AbstractView(){
        observers = new EnumMap<ViewEventType, List<ViewObserver>>(ViewEventType.class);
    }

    // Should disable button for the time of computations
    public abstract void disableActions();
    public abstract void enableActions();

    public void registerObserver(ViewEventType eventType, ViewObserver o){
        observers.computeIfAbsent(eventType, k -> List.of(o));
    }

    public void notifyObservers(ViewEventType eventType, ViewEvent e) {
        for (ViewObserver observer : observers.getOrDefault(eventType, Collections.emptyList())){
            observer.update(e);
        }
    }
}
