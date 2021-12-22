package dataconverter;

// Custom exception raised when we want to convert
// text file to objects : checked exception as it's highly
// possible that something can go wrong
public class IncorrectDataFormat extends Exception {

    public IncorrectDataFormat(String message){
        super(message);
    }

    public IncorrectDataFormat(){
        super();
    }

}
