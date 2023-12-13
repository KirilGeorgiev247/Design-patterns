package transformations;

public class TrimRightTransformation extends ValidTextTransformation {
    @Override
    public String transform(String text) {
        validateText(text);
        if (Character.isWhitespace(text.charAt(text.length() - 1))) {
            return text.substring(0, text.length() - 2);
        }

        return text;
    }
}
