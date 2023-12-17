package transformations;

import label.Label;
import label.SimpleLabel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NormalizeSpaceTransformationTest {

    NormalizeSpaceTransformation transformation;
    Label label;

    @BeforeEach
    void setUp() {
        transformation = new NormalizeSpaceTransformation();
        label = new SimpleLabel("   te   st   ");
    }

    @Test
    void testIfTransformThrowsWhenInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> transformation.transform(null),
            "Transform should throw when text is null!");

        assertThrows(IllegalArgumentException.class, () -> transformation.transform("   "),
            "Transform should throw when text is blank!");

        assertThrows(IllegalArgumentException.class, () -> transformation.transform(""),
            "Transform should throw when text is empty!");
    }

    @Test
    void testIfTransformationWorksCorrectly() {
        String expected = " te st ";

        assertEquals(expected, transformation.transform(label.getText()), "Word should get rid of unnecessary spaces!");
    }

    @Test
    void testIfTransformationWorksCorrectlyWhenNoSpaces() {
        String expected = "test";
        assertEquals(expected, transformation.transform(expected), "Word should stay the same when no spaces!");
    }

    @Test
    void testIfTransformationKeepsTheCorrectIntervals() {
        String expected = " a b c d e f g h u m ";

        assertEquals(expected, transformation.transform(expected),
            "Word should keep the spaces when there is only one consequently!");
    }
}
