package builder;

import decorators.DecoratorType;
import decorators.TextTransformationDecorator;
import label.Label;
import label.RichLabel;
import label.SimpleLabel;
import label.help.HelpLabel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transformations.TextTransformation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class LabelBuilderTest {

    private LabelBuilder labelBuilder;
    private TextTransformation mockTransformation;

    @BeforeEach
    void setUp() {
        labelBuilder = new LabelBuilder();
        mockTransformation = mock(TextTransformation.class);
        when(mockTransformation.transform(anyString())).thenAnswer(invocation -> invocation.getArgument(0) + " transformed");
    }

    @Test
    void testIfBuilderCreatesSimpleLabelWithTextCorrectly() {
        String text = "Sample text";
        Label label = labelBuilder.setText(text).build();
        assertTrue(label instanceof SimpleLabel);
        assertEquals(text, label.getText(),
            "Should build simple label with text!");
    }

    @Test
    void testIfBuilderCreatesRichLabelWithProperties() {
        String text = "Sample text";
        String color = "Blue";
        int fontSize = 14;
        String fontName = "Arial";

        Label label = labelBuilder
            .setText(text)
            .setColor(color)
            .setFontSize(fontSize)
            .setFontName(fontName)
            .build();

        assertTrue(label instanceof RichLabel, "Should build rich label!");
        assertEquals(text, label.getText(), "Should set text correctly!");
        assertEquals(color, ((RichLabel) label).getColor(), "Should set color correctly!");
        assertEquals(fontSize, ((RichLabel) label).getFontSize(), "Should set fontSize correctly!");
        assertEquals(fontName, ((RichLabel) label).getFontName(), "Should set fontName correctly!");
    }

    @Test
    void testIfBuilderAddsTextTransformationDecorator() {
        String text = "Sample text";
        labelBuilder.addTransformation(mockTransformation);
        Label label = labelBuilder.setText(text).setDecoratorType(DecoratorType.SINGLE).build();

        assertTrue(label instanceof TextTransformationDecorator);
        assertEquals(text + " transformed", label.getText(),
            "Should apply TextTransformationDecorator!");
    }

    @Test
    void testIfBuilderAddsCompositeTransformationDecorator() {
        TextTransformation mockTransformation1 = mock(TextTransformation.class);
        TextTransformation mockTransformation2 = mock(TextTransformation.class);
        when(mockTransformation1.transform(anyString())).thenAnswer(invocation -> invocation.getArgument(0) + " transformed1");
        when(mockTransformation2.transform(anyString())).thenAnswer(invocation -> invocation.getArgument(0) + " transformed2");

        labelBuilder
            .setText("Test")
            .setDecoratorType(DecoratorType.COMPOSITE)
            .addTransformation(mockTransformation1)
            .addTransformation(mockTransformation2);

        Label label = labelBuilder.build();

        assertEquals("Test transformed1 transformed2", label.getText(),
            "Should build label with composite transformation correctly!");
    }

    @Test
    void testIfBuilderCreatesComplexLabel() {
        String text = "Complex Label";
        String helpText = "Helpful information";
        String color = "Blue";
        int fontSize = 16;
        String fontName = "Arial";

        labelBuilder
            .setText(text)
            .setHelpText(helpText)
            .setColor(color)
            .setFontSize(fontSize)
            .setFontName(fontName)
            .setDecoratorType(DecoratorType.COMPOSITE)
            .addTransformation(mockTransformation);

        Label label = labelBuilder.build();

        assertEquals(text + " transformed", label.getText(),
            "Should build complex label with all features!");

        assertTrue(label instanceof HelpLabel,
            "Should create label of type help label!");

        assertEquals(helpText, ((HelpLabel)label).getHelpText(),
            "Should return help text correctly!");
    }
}
