package exceptions;

public class NegativeSideOrZeroNotAllowed extends RuntimeException {

    public NegativeSideOrZeroNotAllowed() {
        super("Negative side or side with value 0 is not allowed!");
    }
}
