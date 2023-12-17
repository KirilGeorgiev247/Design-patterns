package transformations;

import label.Label;
import label.SimpleLabel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReplaceTransformationTest {
    ReplaceTransformation transformation;
    Label label;

    String wordToReplace = "t";

    String replacement = "d";

    @BeforeEach
    void setUp() {
        transformation = new ReplaceTransformation(wordToReplace, replacement);
        label = new SimpleLabel("test testttest test");
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
        String expected = "desd desdddesd desd";

        assertEquals(expected, transformation.transform(label.getText()),
            "Word to be replaced should be replaced everywhere with replacement");
    }

    @Test
    void testIfWordStaysUnchangedWhenInvalidWordToReplace() {
        ReplaceTransformation transformationWithNull = new ReplaceTransformation(null, replacement);

        ReplaceTransformation transformationWithEmpty = new ReplaceTransformation("", replacement);

        ReplaceTransformation transformationWithBlank = new ReplaceTransformation("   ", replacement);

        assertEquals(label.getText(), transformationWithNull.transform(label.getText()),
            "Word should stay the same when word to replace is null!");

        assertEquals(label.getText(), transformationWithBlank.transform(label.getText()),
            "Word should stay the same when word to replace is blank!");

        assertEquals(label.getText(), transformationWithEmpty.transform(label.getText()),
            "Word should stay the same when word to replace is empty!");
    }

    @Test
    void testIfWordStaysUnchangedWhenInvalidReplacement() {
        ReplaceTransformation transformationWithNull = new ReplaceTransformation(wordToReplace, null);

        ReplaceTransformation transformationWithEmpty = new ReplaceTransformation(wordToReplace, "");

        ReplaceTransformation transformationWithBlank = new ReplaceTransformation(wordToReplace, "      ");

        assertEquals(label.getText(), transformationWithNull.transform(label.getText()),
            "Word should stay the same when replacement is null!");

        assertEquals(label.getText(), transformationWithBlank.transform(label.getText()),
            "Word should stay the same when replacement is blank!");

        assertEquals(label.getText(), transformationWithEmpty.transform(label.getText()),
            "Word should stay the same when replacement is empty!");
    }
}
