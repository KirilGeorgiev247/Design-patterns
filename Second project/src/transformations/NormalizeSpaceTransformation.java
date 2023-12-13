package transformations;

import java.util.Arrays;
import java.util.stream.Collectors;

public class NormalizeSpaceTransformation extends ValidTextTransformation {
    @Override
    public String transform(String text) {
        validateText(text);

        return Arrays.stream(text.split(" "))
            .filter(word -> !word.contentEquals(" ") && !word.isEmpty() && !word.isBlank())
            .collect(Collectors.joining(" "));
    }
}
