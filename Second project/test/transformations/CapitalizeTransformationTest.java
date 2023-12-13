package transformations;

import labels.Label;
import labels.SimpleLabel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CapitalizeTransformationTest {

    CapitalizeTransformation transformation;
    Label label;

    @BeforeEach
    void setUp() {
        transformation = new CapitalizeTransformation();
        label = new SimpleLabel("test");
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
        String expected = "Test";

        assertEquals(expected, transformation.transform(label.getText()), "First letter should be capitalized!");
    }

    @Test
    void testIfTransformationWorksCorrectlyWhenAlreadyCapitalized() {
        String expected = "Test";

        String actual = transformation.transform(expected);

        assertEquals(expected, actual, "Word should stay the same when first letter is already capitalized!");
    }

    @Test
    void testIfTransformationRemovesOnlyWhenStartingWithLetter() {
        String expected = "5test";

        assertEquals(expected, transformation.transform(expected),
            "Transformation should not change anything when word does not start with a letter!");
    }
}
