package dataconverter;

import datasciencealgorithms.utils.Parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public abstract class CustomFileReader<E> extends DataConverter {

    public CustomFileReader(String ... fileExtensions){
        super(fileExtensions);
    }

    abstract List<E> handleObjectReading(String path) throws IOException, IncorrectDataFormat;

    public List<E> readFromFile(String fileName) throws IOException, IncorrectDataFormat{

        parseFilePath(fileName);

        File f = new File(mapping.get(Parser.Entries.FILEPATH));

        if (!f.exists() || f.length() == 0L)
            throw new IOException("File doesn't exist or is empty");

        return handleObjectReading(f.getPath());
    }

}
