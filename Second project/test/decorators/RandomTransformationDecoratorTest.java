package decorators;

import label.Label;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transformations.TextTransformation;

import java.util.Arrays;
import java.util.List;
import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RandomTransformationDecoratorTest {

    private RandomGenerator mockRandomGenerator;
    private RandomTransformationDecorator decorator;

    @BeforeEach
    void setUp() {
        Label mockLabel = mock(Label.class);
        TextTransformation mockTransformation1 = mock(TextTransformation.class);
        TextTransformation mockTransformation2 = mock(TextTransformation.class);
        mockRandomGenerator = mock(RandomGenerator.class);

        when(mockLabel.getText()).thenReturn("Original Text");
        when(mockTransformation1.transform(anyString())).thenReturn("Transformed Text 1");
        when(mockTransformation2.transform(anyString())).thenReturn("Transformed Text 2");

        List<TextTransformation> transformations = Arrays.asList(mockTransformation1, mockTransformation2);
        decorator = new RandomTransformationDecorator(mockLabel, transformations, mockRandomGenerator);
    }

    @Test
    void testIfGetTextShouldReturnsRandomlyTransformedText() {
        when(mockRandomGenerator.nextInt(0, 2)).thenReturn(0);
        assertEquals("Transformed Text 1", decorator.getText(),
            "GetText method should return text transformed by a randomly selected transformation!");

        when(mockRandomGenerator.nextInt(0, 2)).thenReturn(1);
        assertEquals("Transformed Text 2", decorator.getText(),
            "GetText method should return text transformed by a randomly selected transformation!");
    }

    @Test
    void testIfSetTextTransformationUpdatesTransformations() {
        TextTransformation mockTransformation3 = mock(TextTransformation.class);
        when(mockTransformation3.transform(anyString())).thenReturn("Transformed Text 3");

        List<TextTransformation> newTransformations = List.of(mockTransformation3);
        decorator.setTextTransformation(newTransformations);

        when(mockRandomGenerator.nextInt(0, 1)).thenReturn(0);
        assertEquals("Transformed Text 3", decorator.getText(),
            "SetTextTransformation method should update transformations!");
    }
}