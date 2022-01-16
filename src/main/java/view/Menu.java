package view;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.nio.file.Path;

public class Menu extends JMenuBar {

    public static final String SAVE_TO_FILE = "SAVE_TO_FILE", CREATE_PLOT = "CREATE_PLOT";
    JMenuItem saveToFileItem, createPlotItem;
    JFileChooser chooser;
    FileFilter textFilter;

    public Menu(){

        JMenu optionsMenu = new JMenu("Options");

        chooser = new JFileChooser();

        this.add(optionsMenu);

        saveToFileItem = new JMenuItem("Save");
        optionsMenu.add(saveToFileItem);
        saveToFileItem.addActionListener(new ListenForSaveButton());

        createPlotItem = new JMenuItem("Generate Plot");
        optionsMenu.add(createPlotItem);
        createPlotItem.addActionListener(ev ->
                firePropertyChange(CREATE_PLOT,null, null));


        textFilter = new FileNameExtensionFilter("Text Files",
                "txt");
        chooser.setFileFilter(textFilter);
        chooser.setCurrentDirectory(Path.of(".").toFile());

    }

    private class ListenForSaveButton extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent e) {

            int result = chooser.showOpenDialog(Menu.this);

            if (result == JFileChooser.APPROVE_OPTION && textFilter.accept(chooser.getSelectedFile())){
                Menu.this.firePropertyChange(Menu.SAVE_TO_FILE, null, chooser.getSelectedFile());
            }

        }
    }
}
