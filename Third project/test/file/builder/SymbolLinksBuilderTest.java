package file.builder;

import file.ConcreteFile;
import file.FileBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
public class SymbolLinksBuilderTest {
    private static final String TEST_FILE_NAME = "test.txt";

    private static final String SYM_LINK_NAME = "testSymLink.txt";
    private static final long TEST_FILE_SIZE = 33L;
    private SymbolLinksBuilder symbolLinksBuilder;
    @Mock
    private RegularFileBuilder regularFileBuilder;

    @BeforeEach
    void setUp() {
        symbolLinksBuilder = new SymbolLinksBuilder();
        symbolLinksBuilder.regularBuilder = regularFileBuilder;
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testIfSymbolicLinkCreatesFileSuccessfully(@TempDir Path tempDir) throws IOException {
        Path targetFile = tempDir.resolve(TEST_FILE_NAME);
        Files.createFile(targetFile);

        byte[] bytes = new byte[(int) TEST_FILE_SIZE];
        Files.write(targetFile, bytes);

        Path symLink = tempDir.resolve(SYM_LINK_NAME);
        try {
            Files.createSymbolicLink(symLink, targetFile);
        } catch (UnsupportedOperationException | FileSystemException e) {
            fail("Skipping symbolic link creation due to OS limitations or missing privileges!");
        }

        Path filePath = tempDir.resolve(TEST_FILE_NAME);
        Files.createFile(filePath);

        FileBase actualResult = symbolLinksBuilder.createFile(filePath);

        FileBase expectedResult = new ConcreteFile(TEST_FILE_NAME, TEST_FILE_SIZE);

        assertEquals(expectedResult.getPath(), actualResult.getPath(),
            () -> "In memory file representation should have the correct relative path!");
        assertEquals(expectedResult.getSize(), actualResult.getSize(),
            () -> "In memory file representation should have the same size as the real file!");
        assertTrue(actualResult instanceof ConcreteFile, () -> "Concrete file should have been created!");
    }
}