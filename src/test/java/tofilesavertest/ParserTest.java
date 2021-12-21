package tofilesavertest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;

public class ParserTest {

    Parser fileParser;
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

        Matcher m = PATH_REGEX.matcher(dir + name + "." + extension);

        Assertions.assertTrue(m.matches());
        Assertions.assertEquals(dir, m.group("dir"));
        Assertions.assertEquals(name, m.group("name"));
        Assertions.assertEquals(extension, m.group("ext"));
    }

    @Test
    void shouldParseAndReturnMapping(){

        Map<String, String> mapping = fileParser.parse(fileName);
        Assertions.assertAll(
                () -> Assertions.assertEquals(dir, mapping.get(Parse.DIRECTORY)),
                () -> Assertions.assertEquals(name, mapping.get(Parse.FILENAME)),
                () -> Assertions.assertEquals(extension, mapping.get(Parse.EXTENSION))
        );
    }

    @Test
    void shouldNtParseDirectoryPathWithNoFileName(){
        Map<String, String> mapping = fileParser.parse("some/dir/with/no/file");
        Assertions.assertNull(mapping);
    }

    @Test
    void shouldNtParseNull(){
        Map<String, String> mapping = fileParser.parse(null);
        Assertions.assertNull(mapping);
    }

}
