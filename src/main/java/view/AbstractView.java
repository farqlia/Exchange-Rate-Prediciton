package view;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractView{

    List<ViewObserver> observers;

    public AbstractView(){
        observers = new ArrayList<>();
    }

    public abstract void updateTable(Object[][] data);

    public abstract void updateTable(Object[][] data, Object[] columnNames);

    public void registerObserver(ViewObserver o){
        observers.add(o);
    }

    public void notifyObservers(ViewEvent e){
        for (ViewObserver o : observers){
            o.update(e);
        }
    }
}
