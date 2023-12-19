package builder;

import decorators.DecoratorType;
import label.Label;
import transformations.TextTransformation;

import java.util.List;

public interface LabelBuilderAPI {

    public LabelBuilder setText(String text);

    public LabelBuilder setHelpText(String helpText);

    public LabelBuilder setColor(String color);

    public LabelBuilder setFontSize(int fontSize);

    public LabelBuilder setFontName(String fontName);

    public LabelBuilder addTransformation(TextTransformation transformation);

    public LabelBuilder addTransformations(List<TextTransformation> transformations);

    public LabelBuilder setDecoratorType(DecoratorType decoratorType);

    public Label build();
}
