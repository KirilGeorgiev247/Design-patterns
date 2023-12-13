package transformations;

// TODO: how can I combine replace with censor
public class ReplaceTransformation extends ValidTextTransformation {
    private final String wordToReplace;

    private final String replacement;

    public ReplaceTransformation(String wordToReplace, String replacement) {
        this.wordToReplace = wordToReplace;
        this.replacement = replacement;
    }

    @Override
    public String transform(String text) {
        validateText(text);

        if (wordToReplace == null || wordToReplace.isBlank() || wordToReplace.isEmpty() || replacement == null) {
            return text;
        }

        return text.replace(wordToReplace, replacement);
    }
}
