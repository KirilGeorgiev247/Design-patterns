package builder;

import decorators.DecoratorType;
import decorators.TextTransformationDecorator;
import decorators.composite.CompositeTransformationDecorator;
import label.Label;
import label.ProxyLabel;
import label.RichLabel;
import label.SimpleLabel;
import label.help.HelpExtendedLabel;
import transformations.TextTransformation;

import java.util.ArrayList;
import java.util.List;

// TODO: ask if this is counting for dep inj
public class LabelBuilder implements LabelBuilderAPI {
    private final List<TextTransformation> transformations = new ArrayList<>();
    private String text;
    private String helpText;
    private String color;
    private int fontSize;
    private String fontName;

    private DecoratorType decoratorType;

    @Override
    public LabelBuilder setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public LabelBuilder setHelpText(String helpText) {
        this.helpText = helpText;
        return this;
    }

    @Override
    public LabelBuilder setColor(String color) {
        this.color = color;
        return this;
    }

    @Override
    public LabelBuilder setFontSize(int fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    @Override
    public LabelBuilder setFontName(String fontName) {
        this.fontName = fontName;
        return this;
    }

    @Override
    public LabelBuilder addTransformation(TextTransformation transformation) {
        transformations.add(transformation);
        return this;
    }

    @Override
    public LabelBuilder addTransformations(List<TextTransformation> transformations) {
        this.transformations.addAll(transformations);
        return this;
    }

    @Override
    public LabelBuilder setDecoratorType(DecoratorType decoratorType) {
        this.decoratorType = decoratorType;
        return this;
    }

    @Override
    public Label build() {
        Label label = text == null ? new ProxyLabel(System.in) : new SimpleLabel(text);

        if (color != null && fontSize > 0 && fontName != null) {
            label = new RichLabel(label.getText(), color, fontSize, fontName);
        }

        if (decoratorType != null) {
            label = decorate(label);
        }

        if (helpText != null) {
            label = new HelpExtendedLabel(helpText, label);
        }

        return label;
    }

    private Label decorate(Label label) {
        switch (decoratorType) {
            case COMPOSITE -> {
                label = new CompositeTransformationDecorator(label, transformations);
                }
            case CYCLING -> throw new RuntimeException("Not implemented");
            case RANDOM -> throw new RuntimeException("Not implemented");
            case SINGLE -> {
                for (TextTransformation transformation : transformations) {
                    label = new TextTransformationDecorator(label, transformation);
                }
            }
        };

        return label;
    }
}
