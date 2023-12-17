package transformations;

import label.Label;
import label.SimpleLabel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TrimLeftTransformationTest {
    TrimLeftTransformation transformation;
    Label label;

    @BeforeEach
    void setUp() {
        transformation = new TrimLeftTransformation();
        label = new SimpleLabel(" test");
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
        String expected = "test";

        assertEquals(expected, transformation.transform(label.getText()), "Left space should be removed!");
    }

    @Test
    void testIfTransformationRemovesOnlyOneSpace() {
        String currTest = "    test";
        String expected = "   test";

        assertEquals(expected, transformation.transform(currTest), "Transform should remove only one left space!");
    }

    @Test
    void testIfTransformationRemovesOnlyWhenStartingWithSpace() {
        String expected = "test";

        assertEquals(expected, transformation.transform(expected),
            "Transformation should not change anything when word does not start with space!");
    }
}
