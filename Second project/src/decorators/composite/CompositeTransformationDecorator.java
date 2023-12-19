package decorators.composite;

import decorators.LabelDecoratorBase;
import label.Label;
import transformations.TextTransformation;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CompositeTransformationDecorator extends LabelDecoratorBase {

    private Queue<TextTransformation> composedTransformations;
    public CompositeTransformationDecorator(Label subject) {
        super(subject);
        composedTransformations = new LinkedList<>();
    }

    public CompositeTransformationDecorator(Label subject, List<TextTransformation> textTransformations) {
        super(subject);
        composedTransformations = new LinkedList<>(textTransformations);
    }

    public void add(TextTransformation textTransformation) {
        composedTransformations.add(textTransformation);
    }

    @Override
    public String getText() {
        return composedTransformations.stream()
            .reduce(super.getText(),
                (text, transformation) -> transformation.transform(text),
                (text1, text2) -> text2);
    }

    @Override
    public void setTextTransformation(List<TextTransformation> textTransformations) {
        composedTransformations = new LinkedList<>(textTransformations);
    }
}
