package Exceptions;

/**
 * Created by Matthew on 12/02/2017.
 */
public class InvalidAddressException extends Exception{
    public InvalidAddressException(String error) {
        super(error);
    }
}
