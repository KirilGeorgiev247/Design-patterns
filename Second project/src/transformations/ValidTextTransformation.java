package transformations;

public abstract class ValidTextTransformation implements TextTransformation {
    protected void validateText(String text) {
        if (text == null || text.isEmpty() || text.isBlank()) {
            throw new IllegalArgumentException("Invalid text");
        }
    }
}
