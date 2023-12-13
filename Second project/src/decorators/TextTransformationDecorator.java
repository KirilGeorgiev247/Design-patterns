package decorators;

import labels.Label;
import transformations.TextTransformation;

import java.util.List;

// Ask for default c-r with no params
public class TextTransformationDecorator extends LabelDecoratorBase {
    protected TextTransformation textTransformation;
    public TextTransformationDecorator(Label subject, TextTransformation textTransformation) {
        super(subject);
        this.textTransformation = textTransformation;
    }

    @Override
    public String getText() {
        return textTransformation.transform(super.getText());
    }

    @Override
    public void setTextTransformation(List<TextTransformation> textTransformation) {
        this.textTransformation = textTransformation.getFirst();
    }
}
