package writer;

import calculator.ChecksumCalculator;
import file.FileBase;
import memento.Memento;
import observer.Observable;
import observer.message.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import progress.ProgressReporter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HashStreamWriterTest {

    private static final String TEST_FILE_NAME = "test.txt";

    private static final String SECOND_TEST_FILE_NAME = "second_test.txt";

    private static final String SECOND_TEST_FILE_HASH = "second test hash";
    private static final String TEST_FILE_HASH = "test hash";

    private static final String TEST_FILE_CONTENT = "test content";

    private static final long TEST_FILE_SIZE = 100L;

    @Mock
    private ChecksumCalculator calc;
    @Mock
    private ProgressReporter progressReporter;

    @Test
    void testIfHashStreamWriterSavesFileSuccessfully()
        throws IOException, NoSuchAlgorithmException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        FileBase file = mock(FileBase.class);
        when(file.getPath()).thenReturn(TEST_FILE_NAME);
        when(file.getSize()).thenReturn(TEST_FILE_SIZE);
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_FILE_CONTENT.getBytes());
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            HashStreamWriter writer = new HashStreamWriter(calc, outputStream, latch);
            writer.registerObserver(progressReporter);
            when(file.getInputStream()).thenReturn(inputStream);
            when(calc.calculate(inputStream)).thenReturn(TEST_FILE_HASH);

            writer.addToProcess(file);
            Thread writerThread = new Thread(writer);
            writerThread.start();

            assertTrue(latch.await(100, TimeUnit.SECONDS), "HashStreamWriter's latch should have counted down!");

            writer.stop();
            writerThread.join(1000);

            String output = outputStream.toString();
            String expectedOutput = TEST_FILE_HASH + " " + TEST_FILE_NAME + System.lineSeparator();

            assertEquals(output, expectedOutput,
                "HashStreamWriter should save file correctly to output stream containing the file path and the file hash!");

            verify(progressReporter, times(1)).update(eq(writer), argThat(message ->
                message.getType() == Message.MessageType.NEW_FILE &&
                    TEST_FILE_NAME.equals(message.getMessage())
            ));

            verify(progressReporter, times(1)).update(eq(writer), argThat(message ->
                message.getType() == Message.MessageType.COMPLETE &&
                    "Completed".equals(message.getMessage())
            ));

            verify(progressReporter, times(1)).update(eq(writer), argThat(message ->
                message.getType() == Message.MessageType.RESUMED &&
                    "Resuming".equals(message.getMessage())
            ));

            verify(progressReporter, atLeast(1)).update(eq(writer), any());
        }
    }

    @Test
    void testIfHashStreamWriterSavesMultipleFilesSuccessfully() throws Exception {
        CountDownLatch latch = new CountDownLatch(2);

        FileBase firstFile = mock(FileBase.class);
        when(firstFile.getPath()).thenReturn(TEST_FILE_NAME);
        when(firstFile.getSize()).thenReturn(TEST_FILE_SIZE);

        FileBase secondFile = mock(FileBase.class);
        when(secondFile.getPath()).thenReturn(SECOND_TEST_FILE_NAME);
        when(secondFile.getSize()).thenReturn(TEST_FILE_SIZE);

        try (ByteArrayInputStream firstInputStream = new ByteArrayInputStream(TEST_FILE_CONTENT.getBytes());
             ByteArrayInputStream secondInputStream = new ByteArrayInputStream(TEST_FILE_CONTENT.getBytes());
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            HashStreamWriter writer = new HashStreamWriter(calc, outputStream, latch);
            writer.registerObserver(progressReporter);
            when(firstFile.getInputStream()).thenReturn(firstInputStream);
            when(secondFile.getInputStream()).thenReturn(secondInputStream);
            when(calc.calculate(any(InputStream.class)))
                .thenReturn(TEST_FILE_HASH).thenReturn(SECOND_TEST_FILE_HASH);

            writer.addToProcess(firstFile);
            writer.addToProcess(secondFile);
            Thread writerThread = new Thread(writer);
            writerThread.start();

            assertTrue(latch.await(100, TimeUnit.SECONDS),
                "HashStreamWriter's latch should have counted down for both files!");

            writer.stop();
            writerThread.join(1000);

            String output = outputStream.toString();
            String expectedOutput =
                TEST_FILE_HASH + " " + TEST_FILE_NAME + System.lineSeparator() + SECOND_TEST_FILE_HASH + " " +
                    SECOND_TEST_FILE_NAME + System.lineSeparator();

            assertEquals(output, expectedOutput,
                "HashStreamWriter should save multiple files correctly to output stream containing the file paths and the file hashes!");

            verify(progressReporter, times(2)).update(eq(writer), argThat(message ->
                message.getType() == Message.MessageType.NEW_FILE &&
                    (TEST_FILE_NAME.equals(message.getMessage()) || SECOND_TEST_FILE_NAME.equals(message.getMessage()))
            ));

            verify(progressReporter, times(1)).update(eq(writer), argThat(message ->
                message.getType() == Message.MessageType.COMPLETE &&
                    "Completed".equals(message.getMessage())
            ));

            verify(progressReporter, times(1)).update(eq(writer), argThat(message ->
                message.getType() == Message.MessageType.RESUMED &&
                    "Resuming".equals(message.getMessage())
            ));

            verify(progressReporter, atLeast(2)).update(eq(writer), any());
        }
    }

    @Test
    void testIfHashStreamWriterPauseAndResumeBehaviourWorksCorrectly()
        throws InterruptedException, NoSuchAlgorithmException, IOException {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(1);

        FileBase file = mock(FileBase.class);
        when(file.getPath()).thenReturn(TEST_FILE_NAME);
        when(file.getSize()).thenReturn(TEST_FILE_SIZE);

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_FILE_CONTENT.getBytes());
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            HashStreamWriter writer = new HashStreamWriter(calc, outputStream, finishLatch);
            writer.registerObserver(progressReporter);
            when(file.getInputStream()).thenReturn(inputStream);
            when(calc.calculate(any(InputStream.class))).thenReturn(TEST_FILE_HASH);

            writer.addToProcess(file);
            Thread writerThread = new Thread(writer);
            writerThread.start();
            writer.pause();

            assertFalse(startLatch.await(2, TimeUnit.SECONDS), "HashStreamWriter should process no file during pause!");

            writer.resume();
            assertTrue(finishLatch.await(100, TimeUnit.SECONDS), "HashStreamWriter should process file after resume!");

            writer.stop();
            writerThread.join(1000);

            verify(progressReporter, times(1)).update(eq(writer), argThat(message ->
                message.getType() == Message.MessageType.NEW_FILE &&
                    TEST_FILE_NAME.equals(message.getMessage())
            ));

            verify(progressReporter, times(1)).update(eq(writer), argThat(message ->
                message.getType() == Message.MessageType.COMPLETE &&
                    "Completed".equals(message.getMessage())
            ));

            verify(progressReporter, times(2)).update(eq(writer), argThat(message ->
                message.getType() == Message.MessageType.RESUMED &&
                    "Resuming".equals(message.getMessage())
            ));

            verify(progressReporter, times(1)).update(eq(writer), argThat(message ->
                message.getType() == Message.MessageType.PAUSED &&
                    "Paused".equals(message.getMessage())
            ));

            verify(progressReporter, atLeast(1)).update(eq(writer), any());
        }
    }

    @Test
    void testIfHashStreamWriterReactsCorrectlyWhenCalculatorThrows()
        throws IOException, NoSuchAlgorithmException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        FileBase file = mock(FileBase.class);
        when(file.getPath()).thenReturn(TEST_FILE_NAME);
        when(file.getSize()).thenReturn(TEST_FILE_SIZE);

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_FILE_CONTENT.getBytes());
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            HashStreamWriter writer = new HashStreamWriter(calc, outputStream, latch);
            writer.registerObserver(progressReporter);
            when(file.getInputStream()).thenReturn(inputStream);
            when(calc.calculate(any(InputStream.class))).thenThrow(
                new NoSuchAlgorithmException("Error computing hash"));

            writer.addToProcess(file);
            Thread writerThread = new Thread(writer);
            writerThread.start();

            assertTrue(latch.await(5, TimeUnit.SECONDS),
                "HashStreamWriter's latch should have counted down even on error!");

            writer.stop();
            writerThread.join(1000);

            verify(progressReporter, times(1)).update(eq(writer), argThat(message ->
                message.getType() == Message.MessageType.ERROR &&
                    "Error occurred!".equals(message.getMessage())
            ));

            String output = outputStream.toString();
            assertTrue(output.isEmpty(),
                "HashStreamWriter should not have written anything to output stream after error has occurred!");
        }
    }

    @Test
    void testIfHashStreamWriterSavesAndRestoresStateCorrectly() throws IOException, NoSuchAlgorithmException {
        CountDownLatch latch = new CountDownLatch(1);

        FileBase firstFile = mock(FileBase.class);
        FileBase secondFile = mock(FileBase.class);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            HashStreamWriter writer = new HashStreamWriter(calc, outputStream, latch);
            writer.registerObserver(progressReporter);

            writer.addToProcess(firstFile);
            writer.addToProcess(secondFile);

            Memento savedState = writer.save();

            writer.restore(new Memento(new LinkedBlockingDeque<>()));
            assertEquals(0, writer.filesToProcess.size(),
                "HashStreamWriter should have zero files to process after restoring empty state!");

            writer.restore(savedState);

            assertEquals(2, writer.filesToProcess.size(),
                "HashStreamWriter should have two files to process after restoring saved state!");
            assertTrue(writer.filesToProcess.contains(firstFile) && writer.filesToProcess.contains(secondFile),
                "HashStreamWriter should have the original files to process after restoring!");
        }
    }

    @Test
    void testIfHashStreamWriterChangesCalculatorCorrectly()
        throws InterruptedException, NoSuchAlgorithmException, IOException {
        CountDownLatch latch = new CountDownLatch(1);

        FileBase file = mock(FileBase.class);
        when(file.getPath()).thenReturn(TEST_FILE_NAME);
        when(file.getSize()).thenReturn(TEST_FILE_SIZE);

        try (ByteArrayInputStream firstInputStream = new ByteArrayInputStream(TEST_FILE_CONTENT.getBytes());
             ByteArrayInputStream secondInputStream = new ByteArrayInputStream(TEST_FILE_CONTENT.getBytes());
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            HashStreamWriter writer = new HashStreamWriter(calc, outputStream, latch);
            writer.registerObserver(progressReporter);
            when(file.getInputStream()).thenReturn(firstInputStream).thenReturn(secondInputStream);
            ChecksumCalculatorStub newCalc = new ChecksumCalculatorStub();
            when(calc.calculate(any(InputStream.class))).thenReturn(TEST_FILE_HASH);

            writer.addToProcess(file);
            Thread writerThread = new Thread(writer);
            writerThread.start();
            latch.await(2, TimeUnit.SECONDS);

            writer.changeCalculator(newCalc);
            latch = new CountDownLatch(1);
            writer.addToProcess(file);

            writer.stop();
            writerThread.join(1000);

            String output = outputStream.toString();
            String expectedOutput = SECOND_TEST_FILE_HASH + " " + TEST_FILE_NAME + System.lineSeparator();
            assertTrue(output.contains(expectedOutput),
                "HashStreamWriter should change the calculator correctly. The hash should be different if the calculator is different!");

            verify(progressReporter, atLeast(1)).update(eq(writer), any(Message.class));
        }
    }

    private static class ChecksumCalculatorStub extends Observable implements ChecksumCalculator {
        @Override
        public String calculate(InputStream inputStream) {
            return SECOND_TEST_FILE_HASH;
        }
    }
}

