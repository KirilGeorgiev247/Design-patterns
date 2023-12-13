package labels;

public class SimpleLabel implements Label {
    private final String value;

    public SimpleLabel(String value) {
        this.value = value;
    }
    @Override
    public String getText() {
        return this.value;
    }
}
