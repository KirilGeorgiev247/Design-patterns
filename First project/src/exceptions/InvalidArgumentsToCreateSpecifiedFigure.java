package exceptions;

public class InvalidArgumentsToCreateSpecifiedFigure extends RuntimeException {
    public InvalidArgumentsToCreateSpecifiedFigure(String figureType) {
        super(figureType);
    }
}
