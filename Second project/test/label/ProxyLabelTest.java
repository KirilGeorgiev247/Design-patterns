package label;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class ProxyLabelTest {

    InputStream inputStream;

    @Test
    void testIfNullValueIsRequestingInitialization() {
        String value = "test";
        inputStream = new ByteArrayInputStream(value.getBytes());
        ProxyLabel label = new ProxyLabel(inputStream);

        assertEquals(value, label.getText(),
            "Proxy label should request initialization of its value when not initialized!");

        try {
            label.closeReader();
        } catch (IOException e) {
            throw new RuntimeException(e); // TODO: ask if it is good to throw exceptions in tests
        }
    }

    @Test
    void testIfValueIsBeingRequestedWhenTimeOutExceeds() {
        String value1 = "test";
        String value2 = "test2";

        String input = value1 + System.lineSeparator() + value2;
        inputStream = new ByteArrayInputStream(input.getBytes());
        ProxyLabel label = new ProxyLabel(1, inputStream);
        String firstRes = label.getText();
        String secondRes = label.getText();

        assertEquals(value2, secondRes,
            "Value should request to be changed if wanted when timeout expires!");

        try {
            label.closeReader();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testIfGetTextReturnsTheSameWhenTimeOutHasNotExpired(){
        String value1 = "test";
        String value2 = "test2";

        String input = value1 + System.lineSeparator() + value2;
        inputStream = new ByteArrayInputStream(input.getBytes());
        ProxyLabel label = new ProxyLabel(5, inputStream);
        String firstRes = label.getText();
        String secondRes = label.getText();
        String thirdRes = label.getText();

        List<String> expected = List.of(value1, value1, value1);
        List<String> actual = List.of(firstRes, secondRes, thirdRes);

        assertIterableEquals(expected, actual,
            "Value should be the same every time if timeout has not expired!");

        try {
            label.closeReader();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testIfTimeoutIsExceedingCorrectly() {
        String value1 = "test";
        String value2 = "test2";

        String input = value1 + System.lineSeparator() + value2;
        inputStream = new ByteArrayInputStream(input.getBytes());
        ProxyLabel label = new ProxyLabel(2, inputStream);
        String firstRes = label.getText();
        String secondRes = label.getText();
        String thirdRes = label.getText();

        assertEquals(value2, thirdRes,
            "Value should have the opportunity to be changed exactly when timeout counter reaches zero!");

        try {
            label.closeReader();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
