package transformations;

public class TrimLeftTransformation extends ValidTextTransformation {
    @Override
    public String transform(String text) {
        validateText(text);
        if (Character.isWhitespace(text.charAt(0))) {
            return text.substring(1);
        }

        return text;
    }
}
