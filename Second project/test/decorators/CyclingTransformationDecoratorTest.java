package decorators;

import label.Label;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transformations.TextTransformation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CyclingTransformationDecoratorTest {

    private Label mockLabel;
    private TextTransformation mockTransformation1;
    private TextTransformation mockTransformation2;
    private TextTransformation mockTransformation3;
    private CyclingTransformationsDecorator decorator;

    @BeforeEach
    void setUp() {
        mockLabel = mock(Label.class);
        mockTransformation1 = mock(TextTransformation.class);
        mockTransformation2 = mock(TextTransformation.class);
        mockTransformation3 = mock(TextTransformation.class);

        when(mockLabel.getText()).thenReturn("Original Text");
        when(mockTransformation1.transform(anyString())).thenReturn("Transformed Text 1");
        when(mockTransformation2.transform(anyString())).thenReturn("Transformed Text 2");
        when(mockTransformation3.transform(anyString())).thenReturn("Transformed Text 3");

        List<TextTransformation> transformations = Arrays.asList(
            mockTransformation1,
            mockTransformation2,
            mockTransformation3
        );
        decorator = new CyclingTransformationsDecorator(mockLabel, transformations);
    }

    @Test
    void testIfCyclingThroughTransformationsWorksCorrectly() {
        assertEquals("Transformed Text 1", decorator.getText(),
            "GetText should cycle through transformations!");
        assertEquals("Transformed Text 2", decorator.getText(),
            "GetText should cycle through transformations!");
        assertEquals("Transformed Text 3", decorator.getText(),
            "GetText should cycle through transformations!");
        assertEquals("Transformed Text 1", decorator.getText(),
            "GetText should cycle through transformations!");
    }

    @Test
    void testIfSetTextTransformationResetsTheCycling() {
        decorator.getText();
        decorator.getText();

        decorator.setTextTransformation(Arrays.asList(mockTransformation2, mockTransformation3));

        decorator.getText();
        decorator.getText();

        assertEquals("Transformed Text 2", decorator.getText(),
            "SetTextTransformation should reset the cycling");
    }

    @Test
    void testIfCyclingHandlesASingleTransformation() {
        decorator.setTextTransformation(Collections.singletonList(mockTransformation1));
        assertEquals("Transformed Text 1", decorator.getText(),
            "Cycling should handle a single transformation");
        assertEquals("Transformed Text 1", decorator.getText(),
            "Cycling should handle a single transformation");
    }
}
