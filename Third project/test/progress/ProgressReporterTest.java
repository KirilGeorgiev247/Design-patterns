package progress;

import calculator.MD5ChecksumCalculator;
import observer.Observable;
import observer.message.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import writer.HashStreamWriter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ProgressReporterTest {
    private static final String TEST_FILE_NAME = "test.txt";

    private static final long TEST_FILE_SIZE = 100L;

    private static final long TEST_PROGRESS_SIZE = 20L;

    private static final Long TOTAL_BYTES = 1000L;
    private static final String TEST_ERROR_MESSAGE = "Error";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    @Mock
    private HashStreamWriter hashStreamWriter;
    @Mock
    private MD5ChecksumCalculator calc;
    @Mock
    private Observable observable;

    @Mock
    private Message messageMock;
    private ProgressReporter progressReporter;


    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        progressReporter = new ProgressReporter(hashStreamWriter, TOTAL_BYTES);
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testIfProgressReporterReportsCorrectlyOnNewFile() {
        Message newFileMessage = new Message(Message.MessageType.NEW_FILE, TEST_FILE_NAME, TEST_FILE_SIZE);
        progressReporter.update(hashStreamWriter, newFileMessage);

        String expectedResult = "\rProcessing " + TEST_FILE_NAME;
        String actualResult = outContent.toString();

        String normalizedActual = actualResult.replace("\n", "");

        assertTrue(normalizedActual.startsWith(expectedResult),
            "The output should start with the current expected text!");
    }

    @Test
    void testIfProgressReporterReportsCorrectlyOnProgressUpdate() {
        Message newFileMessage = new Message(Message.MessageType.NEW_FILE, TEST_FILE_NAME, TEST_FILE_SIZE);
        progressReporter.update(hashStreamWriter, newFileMessage);

        int bytesSoFar = 0;

        Message progressUpdateMessage = new Message(Message.MessageType.PROGRESS_UPDATE, TEST_PROGRESS_SIZE);
        progressReporter.update(calc, progressUpdateMessage);

        bytesSoFar += TEST_PROGRESS_SIZE;

        // once
        String expectedResult = "\rProcessing " + TEST_FILE_NAME + getProcessingInfo(bytesSoFar, progressUpdateMessage);
        String actualResult = outContent.toString();

        String normalizedActual = actualResult.replace("\n", "");

        assertTrue(normalizedActual.startsWith(expectedResult),
            "The output should start with the current expected text!");

        progressUpdateMessage = new Message(Message.MessageType.PROGRESS_UPDATE, TEST_PROGRESS_SIZE);
        progressReporter.update(calc, progressUpdateMessage);

        bytesSoFar += TEST_PROGRESS_SIZE;

        // twice
        String expectedResultAfterSecondProcess =
            expectedResult + getProcessingInfo(bytesSoFar, progressUpdateMessage);
        String actualResultAfterSecondProcess = outContent.toString();

        String normalizedActualAfterSecondProcess = actualResultAfterSecondProcess.replace("\n", "");

        assertTrue(normalizedActualAfterSecondProcess.startsWith(expectedResultAfterSecondProcess),
            "The output should start with the current expected text!");
    }

    @Test
    void testIfProgressReporterReportsCorrectlyOnComplete() {
        Message completeMessage = new Message(Message.MessageType.COMPLETE, "Completed");
        progressReporter.update(hashStreamWriter, completeMessage);

        String expectedResult = "\rCompleted";
        String actualResult = outContent.toString();

        String normalizedActual = actualResult.replace("\n", "");

        assertTrue(normalizedActual.startsWith(expectedResult),
            "The output should start with the current expected text!");
    }

    @Test
    void testIfProgressReporterReportsCorrectlyOnPaused() {
        Message pausedMessage = new Message(Message.MessageType.PAUSED, "Paused");
        progressReporter.update(hashStreamWriter, pausedMessage);

        String expectedResult = "\rPaused";
        String actualResult = outContent.toString();

        String normalizedActual = actualResult.replace("\n", "");

        assertTrue(normalizedActual.startsWith(expectedResult),
            "The output should start with the current expected text!");
    }

    @Test
    void testIfProgressReporterReportsCorrectlyOnResumed() {
        Message resumedMessage = new Message(Message.MessageType.RESUMED, "Resumed");
        progressReporter.update(hashStreamWriter, resumedMessage);

        String expectedResult = "\rResumed";
        String actualResult = outContent.toString();

        String normalizedActual = actualResult.replace("\n", "");

        assertTrue(normalizedActual.startsWith(expectedResult),
            "The output should start with the current expected text!");
    }

    @Test
    void testIfProgressReporterReportsCorrectlyOnError() {
        Message errorMessage = new Message(Message.MessageType.ERROR, TEST_ERROR_MESSAGE);
        progressReporter.update(hashStreamWriter, errorMessage);

        String expectedResult = "\r" + TEST_ERROR_MESSAGE;
        String actualResult = outContent.toString();

        String normalizedActual = actualResult.replace("\n", "");

        assertTrue(normalizedActual.startsWith(expectedResult),
            "The output should start with the current expected text!");
    }

    @Test
    void testIfProgressReporterThrowsWhenItReceivesMessageFromUnknownObserver() {
        assertThrows(IllegalArgumentException.class, () -> progressReporter.update(observable, messageMock),
            "Progress reporter should throw when unknown observer sends a message!");
    }

    private String getProcessingInfo(long bytesSoFar, Message currMessage) {
        double currPercentage = ((double) bytesSoFar / TEST_FILE_SIZE) * 100;
        double totalPercentage = ((double) bytesSoFar / TOTAL_BYTES) * 100;

        currPercentage = Math.round(currPercentage * 100.0) / 100.0;
        totalPercentage = Math.round(totalPercentage * 100.0) / 100.0;

        return "\rProcessing " + TEST_FILE_NAME + "... " +
            bytesSoFar + " byte(s) read - " + currPercentage +
            "%. Total: " + totalPercentage + "%";
    }
}
