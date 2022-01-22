package view.IO;

import java.io.IOException;

public interface SaveToFileStrategy {

    void saveToFile(String path) throws IOException;

}
