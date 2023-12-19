package decorators;

import label.Label;
import transformations.TextTransformation;

import java.util.List;
import java.util.random.RandomGenerator;

public class RandomTransformationDecorator extends LabelDecoratorBase {
    private final RandomGenerator randomGenerator;
    private List<TextTransformation> textTransformations;

    public RandomTransformationDecorator(Label subject, List<TextTransformation> textTransformations,
                                         RandomGenerator randomGenerator) {
        super(subject);
        this.textTransformations = textTransformations;
        this.randomGenerator = randomGenerator;
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
        return randomGenerator.nextInt(0, bound);
    }
}
