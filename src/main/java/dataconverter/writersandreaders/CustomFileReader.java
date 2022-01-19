package dataconverter.writersandreaders;


import dataconverter.Parser;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class CustomFileReader<E> {

    private FileHandler handler;
    public CustomFileReader(String fileExtension){
        handler = new FileHandler(fileExtension);
    }

    abstract List<E> handleObjectReading(String path) throws IOException, IllegalArgumentException;

    public List<E> readFromFile(String fileName) throws IOException{

        handler.parseFilePath(fileName);

        File f = new File(handler.getProperty(Parser.Entries.FILEPATH));

        if (!f.exists() || f.length() == 0L)
            throw new IOException("File doesn't exist or is empty");

        return handleObjectReading(f.getPath());
    }

}
