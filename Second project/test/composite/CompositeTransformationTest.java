package composite;

import decorators.CyclingTransformationsDecorator;
import decorators.LabelDecoratorBase;
import decorators.RandomTransformationDecorator;
import decorators.TextTransformationDecorator;
import labels.Label;
import labels.SimpleLabel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transformations.CapitalizeTransformation;
import transformations.CensorTransformation;
import transformations.DecorateTransformation;
import transformations.ReplaceTransformation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CompositeTransformationTest {

    private Label testSubject;

    // TODO: probably won't be necessary
    @BeforeEach
    void setUp() {
        testSubject = new SimpleLabel("test");
    }

    @Test
    void testIfOneTransformationIsAppliedCorrectly() {
        CapitalizeTransformation capitalizeTransformation = new CapitalizeTransformation();

        CompositeTransformation compositeTransformationDecorator =
            new CompositeTransformation(testSubject);

        compositeTransformationDecorator.add(capitalizeTransformation);

        assertEquals(capitalizeTransformation.transform(testSubject.getText()),
            capitalizeTransformation.transform(testSubject.getText()),
            "One composite transformation should be applied to the label!");
    }

    @Test
    void testIfMoreTransformationsAreAppliedCorrectly() {
        CapitalizeTransformation capitalizeTransformation = new CapitalizeTransformation();
        CensorTransformation censorTransformation = new CensorTransformation("t");
        DecorateTransformation decorateTransformation = new DecorateTransformation();

        CompositeTransformation compositeTransformation = new CompositeTransformation(testSubject);

        compositeTransformation.add(capitalizeTransformation);
        compositeTransformation.add(censorTransformation);
        compositeTransformation.add(decorateTransformation);

        String expected = decorateTransformation.transform(
            censorTransformation.transform(
                capitalizeTransformation.transform(testSubject.getText())));

        assertEquals(expected, compositeTransformation.getText(),
            "Composited styles should be applied correctly!");
    }

    @Test
    void testIfDecoratorIsRemovedSuccessfully() {
        CapitalizeTransformation capitalizeTransformation = new CapitalizeTransformation();

        LabelDecoratorBase compositeTransformation =
            new CompositeTransformation(testSubject);

        compositeTransformation.setTextTransformation(List.of(capitalizeTransformation));

        Label prevValue =
            compositeTransformation.removeDecorator(CompositeTransformation.class);

        Label prevValueStatic =
            LabelDecoratorBase.removeDecoratorFrom(compositeTransformation, CompositeTransformation.class);

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

        LabelDecoratorBase compositeTransformation = new CompositeTransformation(testSubject);

        compositeTransformation.setTextTransformation(List.of(capitalizeTransformation, censorTransformation, decorateTransformation));

        String expected = testSubject.getText();

        Label prevValue =
            compositeTransformation.removeDecorator(CompositeTransformation.class);

        Label prevValueStatic =
            LabelDecoratorBase.removeDecoratorFrom(compositeTransformation, CompositeTransformation.class);

        assertEquals(expected, prevValue.getText(),
            "Composited styles should be removed correctly!");

        assertEquals(expected, prevValueStatic.getText(),
            "Composited styles should be removed correctly!");
    }

    @Test
    void testIfRemoveDecoratorThrowsWhenInvalidDecoratorIsPassed() {
        CapitalizeTransformation capitalizeTransformation = new CapitalizeTransformation();

        LabelDecoratorBase compositeTransformation =
            new CompositeTransformation(testSubject);

        compositeTransformation.setTextTransformation(List.of(capitalizeTransformation));

        assertThrows(IllegalArgumentException.class,
            () -> compositeTransformation.removeDecorator(RandomTransformationDecorator.class),
            "Instance remove decorator method should throw when invalid decorator type is provided!");

        assertThrows(IllegalArgumentException.class,
            () -> LabelDecoratorBase.removeDecoratorFrom(compositeTransformation,
                CyclingTransformationsDecorator.class),
            "Static remove decorator method should throw when invalid decorator type is provided!");
    }

    @Test
    void testIfTextTransformationChangingWorksCorrectly() {
        CapitalizeTransformation capitalizeTransformation = new CapitalizeTransformation();
        DecorateTransformation decorateTransformation = new DecorateTransformation();

        CompositeTransformation compositeTransformation =
            new CompositeTransformation(testSubject);

        compositeTransformation.add(capitalizeTransformation);

        compositeTransformation.setTextTransformation(List.of(decorateTransformation));

        assertEquals(decorateTransformation.transform(testSubject.getText()),
            compositeTransformation.getText(),
            "Changed transformation should be applied to the label!");
    }

    @Test
    void testIfDifferentOrderGivesDifferentResult() {
        CapitalizeTransformation capitalizeTransformation = new CapitalizeTransformation();
        DecorateTransformation decorateTransformation = new DecorateTransformation();
        ReplaceTransformation replaceTransformation = new ReplaceTransformation("t", "d");

        CompositeTransformation compositeTransformation = new CompositeTransformation(testSubject);
        compositeTransformation.setTextTransformation(List.of(capitalizeTransformation, decorateTransformation, replaceTransformation));
        String first = compositeTransformation.getText();
        compositeTransformation.setTextTransformation(List.of(replaceTransformation, decorateTransformation, capitalizeTransformation));
        String second = compositeTransformation.getText();

        assertNotEquals(first, second,
            "Different order of composition should give different result!");
    }
}
