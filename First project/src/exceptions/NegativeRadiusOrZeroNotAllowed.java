package exceptions;

public class NegativeRadiusOrZeroNotAllowed extends RuntimeException {

    public NegativeRadiusOrZeroNotAllowed() {
        super("Negative radius or only one point do not belong to valid circle!");
    }
}
