package transformations;

public class DecorateTransformation extends ValidTextTransformation {
    @Override
    public String transform(String text) {
        validateText(text);

        return "-={ " + text + " }=-";
    }
}
