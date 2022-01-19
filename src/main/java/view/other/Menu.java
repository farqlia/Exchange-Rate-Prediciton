package view.other;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.nio.file.Path;

public class Menu extends JMenuBar {

    public static final String SAVE_AS_TEXT = "SAVE_AS_TEXT", SAVE_AS_JSON = "SAVE_AS_JSON",
            CREATE_PLOT = "CREATE_PLOT",
            SET_RENDERING = "SET_RENDERING";

    JMenuItem saveAsTextItem, saveAsJsonItem, createPlotItem, setRenderingItem;

    public Menu(){

        JMenu optionsMenu = new JMenu("Options");
        JMenu saveMenu = new JMenu("Save");
        JMenu renderMenu = new JMenu("Render");

        this.add(optionsMenu);

        saveAsTextItem = new JMenuItem("Text");
        saveMenu.add(saveAsTextItem);
        saveAsTextItem.addActionListener(ev -> firePropertyChange(SAVE_AS_TEXT, null, null));

        saveAsJsonItem = new JMenuItem("Json");
        saveMenu.add(saveAsJsonItem);
        saveAsJsonItem.addActionListener(ev -> firePropertyChange(SAVE_AS_JSON, null, null));

        optionsMenu.add(saveMenu);

        createPlotItem = new JMenuItem("Generate Plot");
        optionsMenu.add(createPlotItem);
        createPlotItem.addActionListener(ev ->
                firePropertyChange(CREATE_PLOT,null, null));

        setRenderingItem = new JMenuItem("Set rendering");
        setRenderingItem
                .addActionListener(ev -> firePropertyChange(SET_RENDERING, null, null));
        renderMenu.add(setRenderingItem);

        this.add(renderMenu);

    }
}
