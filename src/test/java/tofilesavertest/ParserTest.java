package tofilesavertest;

import datasciencealgorithms.utils.Parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;
import java.util.regex.Matcher;

import static datasciencealgorithms.utils.Parser.PATH_REGEX;

public class ParserTest {

    Parser fileParser;
    String DELIMITER = "\\";
    String dir = "some" + DELIMITER + "random" + DELIMITER + "dir" + DELIMITER;
    String name = "file";
    String extension = "txt";
    String fileName = dir + name + "." + extension;

    @BeforeEach
    void setUp(){
        fileParser = new Parser();
    }

    @Test
    void shouldParseDirectoryNames(){

        Matcher m = PATH_REGEX.matcher(fileName);

        Assertions.assertTrue(m.matches());
        Assertions.assertEquals(dir, m.group("dir"));
        Assertions.assertEquals(name, m.group("name"));
        Assertions.assertEquals(extension, m.group("ext"));
    }

    @Test
    void shouldParseAndReturnMapping(){

        Map<Parser.Entries, String> mapping = fileParser.parseFilePath(fileName);
        Assertions.assertAll(
                () -> Assertions.assertEquals(dir, mapping.get(Parser.Entries.DIRECTORY)),
                () -> Assertions.assertEquals(fileName, mapping.get(Parser.Entries.FILEPATH)),
                () -> Assertions.assertEquals(extension, mapping.get(Parser.Entries.EXTENSION))
        );
    }

    @Test
    void shouldNtParseDirectoryPathWithNoFileName(){
        Map<Parser.Entries, String> mapping = fileParser.parseFilePath("some" + DELIMITER + "dir");
        Assertions.assertNull(mapping);
    }

    @Test
    void shouldParseFileNameFromFileObject() {

        File f = new File(fileName);

        Map<Parser.Entries, String> mapping = fileParser.parseFilePath(f.getPath());
        Assertions.assertAll(
                () -> Assertions.assertEquals(dir, mapping.get(Parser.Entries.DIRECTORY)),
                () -> Assertions.assertEquals(fileName, mapping.get(Parser.Entries.FILEPATH)),
                () -> Assertions.assertEquals(extension, mapping.get(Parser.Entries.EXTENSION))
        );
    }

    @Test
    void shouldNtParseNull(){
        Map<Parser.Entries, String> mapping = fileParser.parseFilePath(null);
        Assertions.assertNull(mapping);
    }


}
