package dataconverter.writersandreaders;

import dataconverter.Parser;

import java.io.IOException;
import java.util.List;

public abstract class CustomFileWriter<E>  {

    private FileHandler handler;
    public CustomFileWriter(String fileExtension){
        handler = new FileHandler(fileExtension);
    }

    abstract void handleObjectWriting(String path, List<? extends E> data) throws IOException;

    public void saveToFile(String fileName, List<E> data) throws IOException{
        handler.parseFilePath(fileName);
        handler.createFile();
        handleObjectWriting(handler.getProperty(Parser.Entries.FILEPATH), data);
    }

}
