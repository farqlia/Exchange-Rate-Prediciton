package dataconverter.formatters;

import java.io.IOException;

public interface Formatter<E> {

    String delimiter = ",";

    String getAsCSVString(E o);

    E parseFromCSVString(String stringToParse) throws IOException;

}
