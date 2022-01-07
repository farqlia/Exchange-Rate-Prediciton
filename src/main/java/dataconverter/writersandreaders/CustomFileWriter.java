package dataconverter.writersandreaders;

import datasciencealgorithms.utils.Parser;

import java.io.IOException;
import java.util.List;

public abstract class CustomFileWriter<E> extends DataConverter {

    public CustomFileWriter(String ... fileExtensions){
        super(fileExtensions);
    }

    abstract void handleObjectWriting(String path, List<E> data) throws IOException;

    public void saveToFile(String fileName, List<E> data) throws IOException{
        parseFilePath(fileName);
        createFile();
        handleObjectWriting(getProperty(Parser.Entries.FILEPATH), data);
    }

}
