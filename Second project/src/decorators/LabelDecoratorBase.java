package decorators;

import label.Label;
import transformations.TextTransformation;

import java.util.List;

public abstract class LabelDecoratorBase implements Label {
    private Label subject;

    public LabelDecoratorBase(Label subject) {
        this.subject = subject;
    }

    public static Label removeDecoratorFrom(Label label, Class<? extends LabelDecoratorBase> decoratorType) {
        // Check if the label is null or not a decorator, in which case there's nothing to do
        if (!(label instanceof LabelDecoratorBase currentDecorator)) {
            throw new IllegalArgumentException("Label cannot be null or undecorated!");
        }

        Label innerLabel = currentDecorator.subject;

        // If the current decorator is of the type to be removed, return the subject of it
        if (currentDecorator.getClass() == decoratorType) {
            return innerLabel;
        }

        // Recursively try to remove the decorator from the subject
        Label newSubject = removeDecoratorFrom(innerLabel, decoratorType);

        // If the subject has changed (decorator was removed), update the subject
        if (newSubject != innerLabel) {
            currentDecorator.subject = newSubject;
        }

        // Return the current decorator, which now has the subject with the decorator removed
        return currentDecorator;
    }

    public Label removeDecorator(Class<? extends LabelDecoratorBase> decoratorType) {

        if (this.getClass() == decoratorType) {
            // This is the decorator to remove
            return subject;
        } else if (LabelDecoratorBase.class.isAssignableFrom(subject.getClass())) {
            // The subject is itself a decorator.
            // Try to remove decoratorType from it.
            // Note that we may need to reassign subject, because the
            // requested decorator may be on top of the list.
            subject = ((LabelDecoratorBase) subject).removeDecorator(decoratorType);

            // Return the current object to keep it on top of the list
            return this;
        } else {
            // The subject is not a decorator.
            // We have reached the end of the list and have not found decoratorType.
            // We can either throw an exception here,
            // or do nothing and simply return the current object.

            throw new IllegalArgumentException("TODO");
//            return this;
        }
    }

    @Override
    public String getText() {
        return subject.getText();
    }

    public abstract void setTextTransformation(List<TextTransformation> textTransformations);
}
