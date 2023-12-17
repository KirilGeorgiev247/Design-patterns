package decorators;

import label.Label;
import transformations.TextTransformation;

import java.util.List;
import java.util.Random;

public class RandomTransformationDecorator extends LabelDecoratorBase {
    private List<TextTransformation> textTransformations;

    public RandomTransformationDecorator(Label subject, List<TextTransformation> textTransformations) {
        super(subject);
        this.textTransformations = textTransformations;
    }

    @Override
    public String getText() {
        return textTransformations.get(getRandomIndex(textTransformations.size()))
            .transform(super.getText());
    }

    @Override
    public void setTextTransformation(List<TextTransformation> textTransformations) {
        this.textTransformations = textTransformations;
    }

    private int getRandomIndex(int bound) {
        return (new Random()).nextInt(0, bound);
    }
}
