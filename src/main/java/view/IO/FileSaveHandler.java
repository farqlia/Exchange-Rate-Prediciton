package view.IO;

import dataconverter.formatters.RowToCSV;
import dataconverter.writersandreaders.CustomFileWriter;
import dataconverter.writersandreaders.JsonFileWriter;
import dataconverter.writersandreaders.TextFileWriter;
import model.CustomTableModel;
import model.ResultsTableModel;
import studyjson.ResultsInfo;
import view.view.ViewEvent;
import view.view.ViewObserver;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class FileSaveHandler implements PropertyChangeListener, ViewObserver {

    private ViewEvent ev;
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("_yyyy_MM_dd_");
    private JFileChooser chooser;
    private CustomTableModel<ResultsTableModel.Row> model;

    private final Map<FileTypes, SaveToFileStrategy> saveToFilesStrategies =
            new EnumMap<>(FileTypes.class);

    public FileSaveHandler(CustomTableModel<ResultsTableModel.Row> model){
        this(model, new TextFileWriter<>(new RowToCSV()), new JsonFileWriter());
    }

    public FileSaveHandler(CustomTableModel<ResultsTableModel.Row> model,
                           TextFileWriter<ResultsTableModel.Row> textFileWriter,
                           JsonFileWriter jsonFileWriter){
        this.model = model;
        chooser = new JFileChooser();
        saveToFilesStrategies.put(FileTypes.TEXT, new TextSaveToFileStrategy(textFileWriter));
        saveToFilesStrategies.put(FileTypes.JSON, new JsonSaveToFileStrategy(jsonFileWriter));
        // User can only choose directories where to place a file
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setCurrentDirectory(new File("."));
        chooser.setSelectedFile(new File("results/"));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        try {
            FileTypes fileType = FileTypes.valueOf(evt.getPropertyName());
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION){
                Path path = Path.of(chooser.getSelectedFile().toString(),
                        ev.getCurrencyCode().concat(LocalDate.now().format(format)));

                saveToFilesStrategies.get(fileType).saveToFile(path.toString());
            }

            // Thrown when there's no matching fileType, but it just means that we don't
            // support this kind of file types
            } catch (IllegalArgumentException | IOException ex){
            System.out.println("Something went wrong");
            JOptionPane.showMessageDialog(null,
                    "Couldn't save to file", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    @Override
    public void update(ViewEvent e) {
        ev = e;
    }

    public class JsonSaveToFileStrategy implements SaveToFileStrategy {

        private final CustomFileWriter<ResultsInfo> jsonFileWriter;

        public JsonSaveToFileStrategy(CustomFileWriter<ResultsInfo> jsonFileWriter){
            this.jsonFileWriter = jsonFileWriter;
        }

        public void saveToFile(String path) throws IOException {
            ResultsInfo info = new ResultsInfo(ev.getChosenAlgorithm().toString(),
                    ev.getCurrencyCode(), model.getListOfRows());
            jsonFileWriter.saveToFile(path.concat(".json"), Collections.singletonList(info));
        }
    }

    public class TextSaveToFileStrategy implements SaveToFileStrategy {

        private final CustomFileWriter<ResultsTableModel.Row> textFileWriter;

        public TextSaveToFileStrategy(CustomFileWriter<ResultsTableModel.Row> textFileWriter){
            this.textFileWriter = textFileWriter;
        }
        @Override
        public void saveToFile(String path) throws IOException {
            textFileWriter.saveToFile(path.concat(".txt"), model.getListOfRows());
        }
    }

}
