package label.help;

import composite.CompositeTransformation;
import label.Label;
import label.SimpleLabel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transformations.CapitalizeTransformation;
import transformations.CensorTransformation;
import transformations.DecorateTransformation;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelpExtendedLabelTest {

    Label testSubject;

    @BeforeEach
    void setUp() {
        testSubject = new SimpleLabel("test");
    }

    @Test
    void testIfLabelContentIsValid() {
        CapitalizeTransformation capitalizeTransformation = new CapitalizeTransformation();
        CensorTransformation censorTransformation = new CensorTransformation("t");
        DecorateTransformation decorateTransformation = new DecorateTransformation();

        CompositeTransformation compositeTransformation = new CompositeTransformation(testSubject);

        compositeTransformation.add(capitalizeTransformation);
        compositeTransformation.add(censorTransformation);
        compositeTransformation.add(decorateTransformation);

        String expected = decorateTransformation.transform(
            censorTransformation.transform(
                capitalizeTransformation.transform(testSubject.getText())));

        Label helpExtendedLabel = new HelpExtendedLabel("help text", compositeTransformation);

        assertEquals(expected, helpExtendedLabel.getText(),
            "Help label should have the same text as the label which is referring.");
    }
}
