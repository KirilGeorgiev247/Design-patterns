package exceptions;

public class FigureTypeDoesNotExist extends RuntimeException {
    public FigureTypeDoesNotExist() {
        super("This type of figure is invalid and does not exist!");
    }
}
