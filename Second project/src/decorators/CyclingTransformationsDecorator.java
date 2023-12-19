package decorators;

import label.Label;
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
        String transformedText = textTransformations.get(cyclingIt).transform(super.getText());
        cyclingIt = (++cyclingIt) % textTransformations.size();
        return transformedText;
    }

    @Override
    public void setTextTransformation(List<TextTransformation> textTransformations) {
        this.textTransformations = textTransformations;
        cyclingIt = 0;
    }
}
