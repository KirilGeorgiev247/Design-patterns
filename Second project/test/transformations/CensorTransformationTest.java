package transformations;

import label.Label;
import label.SimpleLabel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CensorTransformationTest {

    CensorTransformation transformation;
    Label label;
    String wordToReplace = "t";

    @BeforeEach
    void setUp() {
        transformation = new CensorTransformation(wordToReplace);
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
        String expected = "*es* *es***es* *es*";

        assertEquals(expected, transformation.transform(label.getText()),
            "Word to be replaced should be replaced everywhere with censored character!");
    }

    @Test
    void testIfWordStaysUnchangedWhenInvalidWordToReplace() {
        CensorTransformation transformationWithNull = new CensorTransformation(null);

        CensorTransformation transformationWithEmpty = new CensorTransformation("");

        CensorTransformation transformationWithBlank = new CensorTransformation("   ");

        assertEquals(label.getText(), transformationWithNull.transform(label.getText()),
            "Word should stay the same when word to replace is null!");

        assertEquals(label.getText(), transformationWithBlank.transform(label.getText()),
            "Word should stay the same when word to replace is blank!");

        assertEquals(label.getText(), transformationWithEmpty.transform(label.getText()),
            "Word should stay the same when word to replace is empty!");
    }
}
