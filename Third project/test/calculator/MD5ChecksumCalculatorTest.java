package calculator;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MD5ChecksumCalculatorTest {
    @Test
    void testIfMD5ChecksumCalculatorReturnsCorrectlyForABC() throws NoSuchAlgorithmException, IOException {
        String input = "abc";
        String expectedResult = "900150983cd24fb0d6963f7d28e17f72";

        MD5ChecksumCalculator calculator = new MD5ChecksumCalculator();
        String actualResult = calculator.calculate(new ByteArrayInputStream(input.getBytes()));

        assertEquals(expectedResult, actualResult, "MD5 checksum does not match for input: " + input);
    }

    @Test
    void testIfMD5ChecksumCalculatorReturnsCorrectlyForHelloWorld() throws NoSuchAlgorithmException, IOException {
        String input = "Hello world!";
        String expectedResult = "86fb269d190d2c85f6e0468ceca42a20";

        MD5ChecksumCalculator calculator = new MD5ChecksumCalculator();
        String actualResult = calculator.calculate(new ByteArrayInputStream(input.getBytes()));

        assertEquals(expectedResult, actualResult, "MD5 checksum does not match for input: " + input);
    }
}
