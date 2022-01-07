package dataconverter;

public interface PrintableAsCSV<E> {

    String delimiter = ",";

    String getAsCSVString(E o);

    E parseFromCSVString(String stringToParse) throws IncorrectDataFormat;

}
