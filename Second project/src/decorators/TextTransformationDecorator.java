package decorators;

import labels.Label;
import transformations.TextTransformation;

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

    public void setTextTransformation(TextTransformation textTransformation) {
        this.textTransformation = textTransformation;
    }
}
