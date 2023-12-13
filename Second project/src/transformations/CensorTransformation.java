package transformations;

public class CensorTransformation extends ValidTextTransformation {

    private final String asterisk = "*";

    private final String wordToReplace;

    public CensorTransformation(String wordToReplace) {
        this.wordToReplace = wordToReplace;
    }

    @Override
    public String transform(String text) {
        validateText(text);

        if (wordToReplace == null || wordToReplace.isBlank() || wordToReplace.isEmpty()) {
            return text;
        }

        return text.replace(wordToReplace, asterisk.repeat(wordToReplace.length()));
    }
}
