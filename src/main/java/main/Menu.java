package main;

import algorithms.SleepingThread;
import io.FileTypes;

import javax.swing.*;

public class Menu extends JMenuBar {

    public static final String
            SET_RENDERING = "SET_RENDERING";

    JMenuItem saveAsTextItem, saveAsJsonItem, createPlotItem, setRenderingItem;

    public Menu(){

        JMenu optionsMenu = new JMenu("Options");
        JMenu saveMenu = new JMenu("Save");
        JMenu renderMenu = new JMenu("Render");

        this.add(optionsMenu);

        saveAsTextItem = new JMenuItem("Text");
        saveMenu.add(saveAsTextItem);
        saveAsTextItem.addActionListener(ev -> firePropertyChange(FileTypes.TEXT.name(), null, null));

        saveAsJsonItem = new JMenuItem("Json");
        saveMenu.add(saveAsJsonItem);
        saveAsJsonItem.addActionListener(ev -> firePropertyChange(FileTypes.JSON.name(), null, null));

        optionsMenu.add(saveMenu);

        createPlotItem = new JMenuItem("Generate Plot");
        optionsMenu.add(createPlotItem);
        createPlotItem.addActionListener(ev ->
                firePropertyChange(Plot.CREATE_PLOT_ACTION,null, null));

        setRenderingItem = new JMenuItem("Set rendering");
        setRenderingItem
                .addActionListener(ev -> firePropertyChange(SET_RENDERING, null, null));
        renderMenu.add(setRenderingItem);

        JMenuItem delayItem = new JMenuItem("Delay");
        delayItem.addActionListener(ev -> SleepingThread.DEBUG = !SleepingThread.DEBUG);

        optionsMenu.add(delayItem);

        this.add(renderMenu);

    }
}
