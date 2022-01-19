package dataconverter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    // Because Window's path delimiter is a special character in Java, we
    // can't really tie it to a variable
    public static final String DELIMITER = "\\\\";
    // Added : to make it work with absolute files
    public static final Pattern PATH_REGEX = Pattern.compile("(?<dir>([a-zA-Z0-9_:\\-]+"
            + DELIMITER + ")+)(?<name>[a-zA-Z0-9_-]+)\\.(?<ext>[a-z]{3,4})");

    public enum Entries{
        DIRECTORY,
        FILEPATH,
        EXTENSION
    }

    public Map<Entries, String> parseFilePath(String filePath) {

        if (filePath == null){
            return null;
        }

        Matcher matcher = PATH_REGEX.matcher(filePath);
        Map<Entries, String> mapping = null;

        if (matcher.matches()){
            mapping = new HashMap<>();
            mapping.put(Entries.DIRECTORY, matcher.group("dir"));
            mapping.put(Entries.EXTENSION, matcher.group("ext"));

            String currentFilePath = mapping.get(Entries.DIRECTORY) +
                    matcher.group("name") + "." +
                    mapping.get(Entries.EXTENSION);

            mapping.put(Entries.FILEPATH, currentFilePath);
        }
        return mapping;
    }
}
