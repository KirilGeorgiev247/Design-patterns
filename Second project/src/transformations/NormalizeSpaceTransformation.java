package transformations;

import java.util.Arrays;
import java.util.stream.Collectors;

public class NormalizeSpaceTransformation extends ValidTextTransformation {
    @Override
    public String transform(String text) {
        validateText(text);

        String result = Arrays.stream(text.split(" "))
            .filter(word -> !word.contentEquals(" ") && !word.isEmpty() && !word.isBlank())
            .collect(Collectors.joining(" "));

        boolean isFirstCharASpace = Character.isSpaceChar(text.charAt(0));
        boolean isLastCharASpace = Character.isSpaceChar(text.charAt(text.length() - 1));

        return (isFirstCharASpace ? " " : "") +
            result + (isLastCharASpace ? " " : "");
    }
}
