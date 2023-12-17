package transformations;

import label.Label;
import label.SimpleLabel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TrimRightTransformationTest {

    TrimRightTransformation transformation;
    Label label;

    @BeforeEach
    void setUp() {
        transformation = new TrimRightTransformation();
        label = new SimpleLabel("test ");
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

        assertEquals(expected, transformation.transform(label.getText()), "Right space should be removed!");
    }

    @Test
    void testIfTransformRemovesOnlyOneSpace() {
        String currTest = "test    ";
        String expected = "test   ";

        assertEquals(expected, transformation.transform(currTest), "Transform should remove only one right space!");
    }

    @Test
    void testIfTransformationRemovesOnlyWhenEndingWithSpace() {
        String expected = "test";

        assertEquals(expected, transformation.transform(expected),
            "Transformation should not change anything when word does not end with space!");
    }
}
