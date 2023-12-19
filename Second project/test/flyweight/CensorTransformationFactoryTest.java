package flyweight;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transformations.TextTransformation;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CensorTransformationFactoryTest {

    private CensorTransformationFactory factory;

    @BeforeEach
    public void setUp() {
        factory = new CensorTransformationFactory();
    }

    @Test
    public void testUniqueInstancesForLongWords() {
        String longWord = "lengthy";
        TextTransformation transformation1 = factory.getCensorTransformation(longWord);
        TextTransformation transformation2 = factory.getCensorTransformation(longWord);

        assertNotSame(transformation1, transformation2, "Expected different instances for words longer than 4 characters.");
    }

    @Test
    public void testSharedInstancesForShortWords() {
        String shortWord = "test";
        TextTransformation transformation1 = factory.getCensorTransformation(shortWord);
        TextTransformation transformation2 = factory.getCensorTransformation(shortWord);

        assertSame(transformation1, transformation2, "Expected the same instances for words of 4 or fewer characters.");
    }

    @Test
    public void testCensoringWithShortWord() {
        String shortWord = "test";
        TextTransformation transformation = factory.getCensorTransformation(shortWord);
        String censoredText = transformation.transform("This is a test string with the word test.");

        assertTrue(censoredText.contains("****"), "Expected the word 'test' to be censored.");
    }

    @Test
    public void testCensoringWithLongWord() {
        String longWord = "lengthy";
        TextTransformation transformation = factory.getCensorTransformation(longWord);
        String censoredText = transformation.transform("This string contains a lengthy word.");

        assertTrue(censoredText.contains("****"), "Expected the word 'lengthy' to be censored.");
    }
}