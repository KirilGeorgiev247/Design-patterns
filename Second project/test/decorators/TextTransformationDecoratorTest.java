package decorators;

import labels.Label;
import labels.SimpleLabel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transformations.CapitalizeTransformation;
import transformations.CensorTransformation;
import transformations.DecorateTransformation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TextTransformationDecoratorTest {

    private Label testSubject;

    // TODO: probably won't be necessary
    @BeforeEach
    void setUp() {
        testSubject = new SimpleLabel("test");
    }

    @Test
    void testIfOneTransformationIsAppliedCorrectly() {
        CapitalizeTransformation capitalizeTransformation = new CapitalizeTransformation();

        Label textTransformationDecorator =
            new TextTransformationDecorator(testSubject, capitalizeTransformation);

        assertEquals(capitalizeTransformation.transform(testSubject.getText()),
            textTransformationDecorator.getText(),
            "Transformation should be applied to the label!");
    }

    @Test
    void testIfMoreTransformationsAreAppliedCorrectly() {
        CapitalizeTransformation capitalizeTransformation = new CapitalizeTransformation();
        CensorTransformation censorTransformation = new CensorTransformation("t");
        DecorateTransformation decorateTransformation = new DecorateTransformation();

        Label capitalize =
            new TextTransformationDecorator(testSubject, capitalizeTransformation);
        Label censor =
            new TextTransformationDecorator(capitalize, censorTransformation);
        Label decorate =
            new TextTransformationDecorator(censor, decorateTransformation);

        String expected = decorateTransformation.transform(
            censorTransformation.transform(
                capitalizeTransformation.transform(testSubject.getText())));

        assertEquals(expected, decorate.getText(),
            "Composited styles should be applied correctly!");
    }

    @Test
    void testIfDecoratorIsRemovedSuccessfully() {
        CapitalizeTransformation capitalizeTransformation = new CapitalizeTransformation();

        LabelDecoratorBase textTransformationDecorator =
            new TextTransformationDecorator(testSubject, capitalizeTransformation);

        Label prevValue =
            textTransformationDecorator.removeDecorator(TextTransformationDecorator.class);

        Label prevValueStatic =
            LabelDecoratorBase.removeDecoratorFrom(textTransformationDecorator, TextTransformationDecorator.class);

        assertEquals(testSubject.getText(),
            prevValue.getText(),
            "Transformation should be removed from the label and it should return the original value!");

        assertEquals(testSubject.getText(),
            prevValueStatic.getText(),
            "Transformation should be removed from the label and it should return the original value!");
    }

    @Test
    void testIfMoreDecoratorsAreRemovedSuccessfully() {
        CapitalizeTransformation capitalizeTransformation = new CapitalizeTransformation();
        CensorTransformation censorTransformation = new CensorTransformation("t");
        DecorateTransformation decorateTransformation = new DecorateTransformation();

        Label capitalize =
            new TextTransformationDecorator(testSubject, capitalizeTransformation);
        Label censor =
            new TextTransformationDecorator(capitalize, censorTransformation);
        LabelDecoratorBase decorate =
            new TextTransformationDecorator(censor, decorateTransformation);

        String expected = capitalizeTransformation.transform(testSubject.getText());

        Label prevValue = ((LabelDecoratorBase) decorate.removeDecorator(TextTransformationDecorator.class))
            .removeDecorator(TextTransformationDecorator.class);

        LabelDecoratorBase decorateReversed =
            (LabelDecoratorBase) LabelDecoratorBase.removeDecoratorFrom(decorate, TextTransformationDecorator.class);

        Label prevValueStatic =
            LabelDecoratorBase.removeDecoratorFrom(decorateReversed, TextTransformationDecorator.class);

        assertEquals(expected, prevValue.getText(),
            "Composited styles should be applied correctly!");

        assertEquals(expected, prevValueStatic.getText(),
            "Composited styles should be applied correctly!");
    }

    @Test
    void testIfRemoveDecoratorThrowsWhenInvalidDecoratorIsPassed() {
        CapitalizeTransformation capitalizeTransformation = new CapitalizeTransformation();

        LabelDecoratorBase textTransformationDecorator =
            new TextTransformationDecorator(testSubject, capitalizeTransformation);

        Label prevValue =
            textTransformationDecorator.removeDecorator(TextTransformationDecorator.class);

        assertThrows(IllegalArgumentException.class,
            () -> textTransformationDecorator.removeDecorator(RandomTransformationDecorator.class),
            "Instance remove decorator method should throw when invalid decorator type is provided!");

        assertThrows(IllegalArgumentException.class,
            () -> LabelDecoratorBase.removeDecoratorFrom(textTransformationDecorator,
                CyclingTransformationsDecorator.class),
            "Static remove decorator method should throw when invalid decorator type is provided!");
    }

    @Test
    void testIfTextTransformationChangingWorksCorrectly() {
        CapitalizeTransformation capitalizeTransformation = new CapitalizeTransformation();
        DecorateTransformation decorateTransformation = new DecorateTransformation();

        LabelDecoratorBase textTransformationDecorator =
            new TextTransformationDecorator(testSubject, capitalizeTransformation);

        textTransformationDecorator.setTextTransformation(List.of(decorateTransformation));

        assertEquals(decorateTransformation.transform(testSubject.getText()),
            textTransformationDecorator.getText(),
            "Changed transformation should be applied to the label!");
    }


}
