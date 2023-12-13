package transformations;

public class CapitalizeTransformation extends ValidTextTransformation {
    @Override
    public String transform(String text) {
        validateText(text);
        if (Character.isLetter(text.charAt(0))) {
            return Character.toUpperCase(text.charAt(0)) + text.substring(1);
        }

        return text;
    }
}
