package Exceptions;

public class CalculateBoundsException extends Exception {
    public CalculateBoundsException(){
        super("Users's boundaries are not in function boundaries");
    }
}
