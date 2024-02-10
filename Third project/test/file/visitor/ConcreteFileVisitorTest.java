package file.visitor;

import file.ConcreteFile;
import file.Folder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import writer.HashStreamWriter;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class ConcreteFileVisitorTest {
    private static final String DIRECTORY_FILE_NAME = "testDirectory";
    private static final String SECOND_DIRECTORY_FILE_NAME = "secondTestDirectory";
    private static final String TEST_FILE_NAME = "test.txt";
    private static final String SECOND_TEST_FILE_NAME = "second_test.txt";

    private static final String THIRD_TEST_FILE_NAME = "third_test.txt";
    private static final long TEST_FILE_SIZE = 33L;
    @Mock
    private HashStreamWriter hashStreamWriter;

    private ConcreteFileVisitor visitor;

    @BeforeEach
    void setUp() {
        visitor = new ConcreteFileVisitor(hashStreamWriter);
    }

    @Test
    void testIfConcreteFileVisitorAddsTheFileToHashStreamWriterToProcess() {
        ConcreteFile concreteFile = new ConcreteFile(TEST_FILE_NAME, TEST_FILE_SIZE);

        visitor.visit(concreteFile);

        verify(hashStreamWriter).addToProcess(concreteFile);
    }

    @Test
    void testIfConcreteFileVisitorVisitsAllFilesInFolder() {
        Folder folder = new Folder(DIRECTORY_FILE_NAME);
        ConcreteFile firstFile = new ConcreteFile(TEST_FILE_NAME, TEST_FILE_SIZE);
        ConcreteFile secondFile = new ConcreteFile(SECOND_TEST_FILE_NAME, TEST_FILE_SIZE);
        folder.addChild(firstFile);
        folder.addChild(secondFile);

        visitor.visit(folder);

        verify(hashStreamWriter).addToProcess(firstFile);
        verify(hashStreamWriter).addToProcess(secondFile);
    }

    @Test
    void testIfConcreteFileVisitorVisitsAllFilesInFolderWithNesting() {
        Folder rootFolder = new Folder(DIRECTORY_FILE_NAME);
        Folder subFolder = new Folder(SECOND_DIRECTORY_FILE_NAME);
        ConcreteFile firstFile = new ConcreteFile(TEST_FILE_NAME, TEST_FILE_SIZE);
        ConcreteFile secondFile = new ConcreteFile(SECOND_TEST_FILE_NAME, TEST_FILE_SIZE);
        ConcreteFile thirdFile = new ConcreteFile(THIRD_TEST_FILE_NAME, TEST_FILE_SIZE);

        rootFolder.addChild(subFolder);
        rootFolder.addChild(firstFile);
        subFolder.addChild(secondFile);
        subFolder.addChild(thirdFile);

        visitor.visit(rootFolder);

        verify(hashStreamWriter, times(1)).addToProcess(firstFile);
        verify(hashStreamWriter, times(1)).addToProcess(secondFile);
        verify(hashStreamWriter, times(1)).addToProcess(thirdFile);
        verifyNoMoreInteractions(hashStreamWriter);
    }
}