package file.builder;

import file.ConcreteFile;
import file.FileBase;
import file.Folder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class RegularFileBuilderTest {
    private static final String DIRECTORY_FILE_NAME = "testDirectory";
    private static final String TEST_FILE_NAME = "test.txt";
    private static final String SECOND_TEST_FILE_NAME = "second_test.txt";
    private static final long TEST_FILE_SIZE = 33L;
    @Mock
    private FileBuilder windowsShortcutBuilder;
    @Mock
    private FileBuilder symLinkBuilder;
    @Mock
    private DirectoryStream<Path> directoryStream;
    private RegularFileBuilder regularFileBuilder;

    @BeforeEach
    void setUp() {
        regularFileBuilder = new RegularFileBuilder(windowsShortcutBuilder, symLinkBuilder);
    }

    @Test
    void testIfRegularFileBuilderCreatesConcreteFileSuccessfully(@TempDir Path tempDir) throws IOException {
        Path filePath = tempDir.resolve(TEST_FILE_NAME);
        Files.createFile(filePath);

        byte[] bytes = new byte[(int) TEST_FILE_SIZE];
        Files.write(filePath, bytes);

        FileBase actualResult = regularFileBuilder.createFile(filePath);

        FileBase expectedResult = new ConcreteFile(TEST_FILE_NAME, TEST_FILE_SIZE);

        assertEquals(expectedResult.getPath(), actualResult.getPath(),
                () -> "In memory file representation should have the correct relative path!");
            assertEquals(expectedResult.getSize(), actualResult.getSize(),
                () -> "In memory file representation should have the same size as the real file!");
            assertTrue(actualResult instanceof ConcreteFile, () -> "Concrete file should have been created!");
    }

    @Test
    void testIfRegularFileBuilderCreatesFolderSuccessfully(@TempDir Path tempDir) throws IOException {
        Path directoryPath = tempDir.resolve(DIRECTORY_FILE_NAME);
        Files.createDirectories(directoryPath);
        Path firstFilePath = directoryPath.resolve(TEST_FILE_NAME);
        Files.createFile(firstFilePath);
        Path secondFilePath = directoryPath.resolve(SECOND_TEST_FILE_NAME);
        Files.createFile(secondFilePath);

        byte[] bytes = new byte[(int) TEST_FILE_SIZE];
        Files.write(firstFilePath, bytes);
        Files.write(secondFilePath, bytes);

        regularFileBuilder = new RegularFileBuilder(windowsShortcutBuilder, symLinkBuilder);
        FileBase actualResult = regularFileBuilder.createFile(directoryPath);

        FileBase firstFile =
            new ConcreteFile(DIRECTORY_FILE_NAME + File.separator + TEST_FILE_NAME, TEST_FILE_SIZE);
        FileBase secondFile =
            new ConcreteFile(DIRECTORY_FILE_NAME + File.separator + SECOND_TEST_FILE_NAME, TEST_FILE_SIZE);

        Folder expectedResult = new Folder(DIRECTORY_FILE_NAME);
        expectedResult.addChild(firstFile);
        expectedResult.addChild(secondFile);

        assertEquals(expectedResult.getPath(), actualResult.getPath(),
            () -> "In memory folder representation should have the same name as the real folder!");
        assertEquals(expectedResult.getSize(), actualResult.getSize(),
            () -> "In memory folder representation should have the same size as the real folder!");
        assertTrue(actualResult instanceof Folder, () -> "Folder should have been created!");
    }

    @Test
    void testIfRegularFileBuilderCreatesFilesInFolderSuccessfully(@TempDir Path tempDir) throws IOException {
        Path directoryPath = tempDir.resolve(DIRECTORY_FILE_NAME);
        Files.createDirectories(directoryPath);
        Path firstFilePath = directoryPath.resolve(TEST_FILE_NAME);
        Files.createFile(firstFilePath);
        Path secondFilePath = directoryPath.resolve(SECOND_TEST_FILE_NAME);
        Files.createFile(secondFilePath);

        FileBase actualResult = regularFileBuilder.createFile(directoryPath);

        FileBase firstFile =
            new ConcreteFile(DIRECTORY_FILE_NAME + File.separator + TEST_FILE_NAME, TEST_FILE_SIZE);
        FileBase secondFile =
            new ConcreteFile(DIRECTORY_FILE_NAME + File.separator + SECOND_TEST_FILE_NAME, TEST_FILE_SIZE);

        List<FileBase> expectedChildren = List.of(secondFile, firstFile);
        List<FileBase> actualChildren = ((Folder) actualResult).getChildren();

        assertEquals(expectedChildren.getFirst().getPath(), actualChildren.getFirst().getPath(),
            () -> "In memory folder representation should have relative paths up to the root!");

        assertEquals(expectedChildren.getLast().getPath(), actualChildren.getLast().getPath(),
            () -> "In memory folder representation should have relative paths up to the root!");
    }
}
