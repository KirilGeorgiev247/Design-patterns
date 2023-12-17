package transformations;

import label.Label;
import label.SimpleLabel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DecorateTransformationTest {

    DecorateTransformation transformation;
    Label label;

    @BeforeEach
    void setUp() {
        transformation = new DecorateTransformation();
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
        String expected = "-={ test }=-";

        assertEquals(expected, transformation.transform(label.getText()), "Word should be decorated");
    }
}
