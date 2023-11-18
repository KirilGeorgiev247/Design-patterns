package exceptions;

public class InvalidCreationMethod extends RuntimeException {
    public InvalidCreationMethod() {
        super("The creation type you have provided is not valid!");
    }
}
