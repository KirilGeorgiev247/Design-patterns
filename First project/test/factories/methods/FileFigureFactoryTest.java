package factories.methods;

import figures.Figure;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileFigureFactoryTest {

    static FileFigureFactory validFactory;
//    static FileFigureFactory invalidArgumentsCountFactory;
//    static FileFigureFactory invalidArgumentsTypeFactory;
//    static FileFigureFactory invalidFigureTypeFactory;

    @Mock
    static FileInputStream validInputTestFile;

//    @Mock
//    static FileInputStream invalidArgumentsCountTestFile;
//
//    @Mock
//    static FileInputStream invalidArgumentsTypeTestFile;
//
//    @Mock
//    static FileInputStream invalidFigureTypeTestFile;
//
//    @BeforeEach
//    void setUp() throws FileNotFoundException {
//
//        invalidArgumentsCountFactory = new FileFigureFactory("./resources/test/invalidArgumentsCountInputTest.txt");
//        invalidArgumentsTypeFactory = new FileFigureFactory("./resources/test/invalidArgumentsTypeInputTest.txt");
//        invalidFigureTypeFactory = new FileFigureFactory("./resources/test/invalidFigureTypeInputTest.txt");
//    }

    @Test
    void testIfCreatedFiguresMatchInput() throws FileNotFoundException {
        validInputTestFile = new FileInputStream("./resources/test/validInputTest.txt");
        validFactory = new FileFigureFactory(validInputTestFile);

        List<Figure> figures = validFactory.getFigures();
        StringBuilder sb = new StringBuilder();

        String expected = """
            Triangle 3333.3 2.01 1
            Triangle 4 3.4 2
            Rectangle 9 8
            Circle 5.5 (3, 8.4)
                """;

        for (Figure f : figures) {
            String test = f.toString();
            sb.append(f.toString());
            sb.append('\n');
        }

        assertEquals(expected, sb.toString(), "Created figures stringification should be the same as input");
    }

//    @Test
//    void testIfInvalidArgumentsCountThrows() {
//        assertThrows(InvalidArgumentsToCreateSpecifiedFigure.class, () -> invalidArgumentsCountFactory.getFigures(),
//            "Invalid arguments count should throw!");
//    }
//
//    @Test
//    void testIfInvalidFormatArgumentsThrows() {
//        assertThrows(NumberFormatException.class, () -> invalidArgumentsTypeFactory.getFigures(),
//            "Wrong argument type should throw!");
//    }
//
//    @Test
//    void testIfInvalidFigureTypeThrows() {
//        assertThrows(FigureTypeDoesNotExist.class, () -> invalidFigureTypeFactory.getFigures(),
//            "Invalid figure type should throw!");
//    }
}
