package decorators;

import labels.Label;
import transformations.TextTransformation;

import java.util.List;

public class CyclingTransformationsDecorator extends LabelDecoratorBase {
    private List<TextTransformation> textTransformations;
    private int cyclingIt;

    public CyclingTransformationsDecorator(Label subject, List<TextTransformation> textTransformations) {
        super(subject);
        this.textTransformations = textTransformations;
        cyclingIt = 0;
    }

    @Override
    public String getText() {
        cyclingIt = (++cyclingIt) % (textTransformations.size() - 1);
        return textTransformations.get(cyclingIt)
            .transform(super.getText());
    }

    public void setTextTransformations(List<TextTransformation> textTransformations) {
        this.textTransformations = textTransformations;
        cyclingIt = 0;
    }
}
