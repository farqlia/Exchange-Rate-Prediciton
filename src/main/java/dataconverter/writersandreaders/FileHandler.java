package dataconverter.writersandreaders;

import datasciencealgorithms.utils.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;

public class FileHandler {

    private final Parser parser;
    private Map<Parser.Entries, String> mapping;
    private final String[] fileExtensions;
    // We will delegate a job of parsing a file name to this class,
    // but we have to handle further logic

    // It's a default constructor in the same time
    public FileHandler(String ... fileExtensions){
        parser = new Parser();
        this.fileExtensions = fileExtensions;
    }

    public void parseFilePath(String filePath) throws IOException{
        mapping = parser.parseFilePath(filePath);
        if (mapping == null){
            // I decided to throw an exception to have a better insight on what went wrong
            throw new IOException("Incorrect File Name");
        }
        if (Arrays.stream(fileExtensions).noneMatch(x -> x.equals(mapping.get(Parser.Entries.EXTENSION)))){
            // Delimiter is only put between words, not at the both edges
            throw new IOException("Unsupported Extension; Supported Extensions are: "
                    + String.join(", ", fileExtensions));
        }

    }

    public void createFile() throws IOException {
        Path p = Path.of(mapping.get(Parser.Entries.DIRECTORY));
        // Creates all subdirectories
        Files.createDirectories(p);
        p = Path.of(mapping.get(Parser.Entries.FILEPATH));
        p.toFile().createNewFile();
    }

    public String getProperty(Parser.Entries property) {
        return mapping.getOrDefault(property, "");
    }

}
