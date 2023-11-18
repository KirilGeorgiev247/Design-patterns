package factories.methods;

import figures.Figure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class STDINFigureFactoryTest {

    static STDINFigureFactory validFactory;
    static STDINFigureFactory invalidArgumentsCountFactory;
    static STDINFigureFactory invalidArgumentsTypeFactory;
    static STDINFigureFactory invalidFigureTypeFactory;

    static String validInput = """
        triangle 1 2.01 3
        triangle 2 3.4 4
        rectangle 8 9
        circle 5.5 (3, 8.4)
        """;
    static String invalidArgumentsCountInput = """
        triangle 1 2.01 3
        triangle 2 3.4 4
        rectangle 9
        circle 5.5 (3, 8.4)
        """;
    static String invalidArgumentsTypeInput = """
        triangle 1 2.01 3
        triangle 2 3.4 4
        rectangle 9 eight
        circle 5.5 (3, 8.4)
        """;
    static String invalidFigureTypeInput = """
        traingle 1 2.01 3
        triangle 2 3.4 4
        rectangle 9 8
        circle 5.5 (3, 8.4)
        """;

    @Mock
    static InputStream validStreamMock = new ByteArrayInputStream(validInput.getBytes());
    @Mock
    static InputStream invalidArgumentsCountStreamMock =
        new ByteArrayInputStream(invalidArgumentsCountInput.getBytes());
    @Mock
    static InputStream invalidArgumentsTypeStreamMock = new ByteArrayInputStream(invalidArgumentsTypeInput.getBytes());
    @Mock
    static InputStream invalidFigureTypeStreamMock = new ByteArrayInputStream(invalidFigureTypeInput.getBytes());

    @BeforeEach
    void setUp() {
        validFactory = new STDINFigureFactory(validStreamMock);
        invalidArgumentsCountFactory = new STDINFigureFactory(invalidArgumentsCountStreamMock);
        invalidArgumentsTypeFactory = new STDINFigureFactory(invalidArgumentsTypeStreamMock);
        invalidFigureTypeFactory = new STDINFigureFactory(invalidFigureTypeStreamMock);
    }

    @Test
    void testIfCreatedFiguresMatchInput() {
        Collection<Figure> figures = validFactory.getFigures();
        StringBuilder sb = new StringBuilder();

        String expected = """
            Triangle 3 2.01 1
            Triangle 4 3.4 2
            Rectangle 9 8
            Circle 5.5 (3, 8.4)
                """;

        for (Figure f : figures) {
            sb.append(f.toString());
            sb.append('\n');
        }

        assertEquals(expected, sb.toString(), "Created figures stringification should be the same as input");
    }

//    @Test
//    void testIfInvalidArgumentsCountLogsError() {
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
