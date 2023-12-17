package label;

public class RichLabel extends SimpleLabel {
    private final String color;
    private final int fontSize;
    private final String fontName;

    public RichLabel(String value, String color, int fontSize, String fontName) {
        super(value);

        this.color = color;
        this.fontSize = fontSize;
        this.fontName = fontName;
    }

    public String getColor() {
        return color;
    }

    public int getFontSize() {
        return fontSize;
    }

    public String getFontName() {
        return fontName;
    }
}
