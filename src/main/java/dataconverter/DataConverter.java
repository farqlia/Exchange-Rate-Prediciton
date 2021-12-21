package dataconverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class DataConverter<E> {

    private String currentFilePath;
    private String directoryFilePath;
    private static String[] fileFormats;
    // We will delegate a job of parsing a file name to this class,
    // but we have to handle further logic
    private Parser parser = new Parser();


    DataConverter(String ... fileExtensions){
        fileFormats = fileExtensions;
    }

    abstract void handleObjectWriting(String path, List<E> data) throws IOException;

    abstract List<E> handleObjectReading(String path) throws IOException;

    public boolean saveToFile(String fileName, List<E> data) throws IOException {

        parseFilePath(fileName);

        try {

            createFile();
            handleObjectWriting(currentFilePath, data);

        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public List<E> readFromFile(String fileName) throws IOException {

        if (!parseFilePath(fileName)){
            return null;
        }

        File f = new File(currentFilePath);

        if (!f.exists() || f.length() == 0L)
            throw new IOException("File doesn't exist or is empty");

        return handleObjectReading(currentFilePath);
    }

    public void createDirectory(){
        File f = new File(directoryFilePath);
        f.mkdirs();
    }

    public void createFile() throws IOException {
        createDirectory();
        File f = new File(currentFilePath);
        f.createNewFile();
    }

    public String getDirectoryPath(){
        return directoryFilePath;
    }

    public String getCurrentFilePath(){
        return currentFilePath;
    }

}
