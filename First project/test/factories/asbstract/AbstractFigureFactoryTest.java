package factories.asbstract;

import factories.methods.FigureFactoryAPI;
import factories.methods.FileFigureFactory;
import factories.methods.RandomFigureFactory;
import factories.methods.STDINFigureFactory;
import figures.Figure;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.Collection;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AbstractFigureFactoryTest {
    private final int inputForRandomFactoryFigures = 5;
    private final String inputForStdinFactoryFigures = """
        triangle 1 2.01 3
        triangle 2 3.4 4
        rectangle 8 9
        circle 5.5 (3, 8.4)
        """;
    private final String inputForFileFactoryFigures = "./resources/test/validInputTest.txt";
    private final String inputForRandomFactory = "Random" + "\n" + inputForRandomFactoryFigures;
    private final String inputForStdinFactory = "Console" + "\n" + inputForStdinFactoryFigures;

    private final String inputForFileFactory = "File" + "\n" + inputForFileFactoryFigures;

    @Test
    void testIfRandomFactoryIsCreated() {
        StringReader reader = new StringReader(inputForRandomFactory);

        Scanner scanner = new Scanner(reader);

        AbstractFigureFactory abstractFigureFactory = new AbstractFigureFactory(scanner);
        FigureFactoryAPI factory = abstractFigureFactory.create();

        assertEquals(RandomFigureFactory.class.getTypeName(), factory.getClass().getTypeName(),
            "Created factory should be of type RandomFigureFactory!");
    }

    @Test
    void testIfCreatedRandomFactoryReturnsRightAmountOfFigures() {
        StringReader reader = new StringReader(inputForRandomFactory);

        Scanner scanner = new Scanner(reader);

        AbstractFigureFactory abstractFigureFactory = new AbstractFigureFactory(scanner);
        FigureFactoryAPI factory = abstractFigureFactory.create();

        Collection<Figure> figures = factory.getFigures();

        assertEquals(figures.size(), inputForRandomFactoryFigures,
            "Created factory should return right amount of figures!");
    }

    @Test
    void testIfStdinFactoryIsCreated() {
        StringReader reader = new StringReader(inputForStdinFactory);

        Scanner scanner = new Scanner(reader);

        AbstractFigureFactory abstractFigureFactory = new AbstractFigureFactory(scanner);
        FigureFactoryAPI factory = abstractFigureFactory.create();

        assertEquals(STDINFigureFactory.class.getTypeName(), factory.getClass().getTypeName(),
            "Created factory should be of type STDINFigureFactory!");
    }

    @Test
    void testIfCreatedStdinFactoryReturnsRightAmountOfFigures() {
        StringReader reader = new StringReader(inputForStdinFactory);

        Scanner scanner = new Scanner(reader);

        AbstractFigureFactory abstractFigureFactory = new AbstractFigureFactory(scanner);
        FigureFactoryAPI factory = abstractFigureFactory.create();

        Collection<Figure> figures = factory.getFigures();

        String expected = """
            Triangle 3 2.01 1
            Triangle 4 3.4 2
            Rectangle 9 8
            Circle 5.5 (3, 8.4)
                """;

        StringBuilder sb = new StringBuilder();

        for (Figure f : figures) {
            sb.append(f.toString());
            sb.append('\n');
        }

        assertEquals(expected, sb.toString(),
            "Created factory should return right same figures like these in the input!");
    }

    @Test
    void testIfFileFactoryIsCreated() {
        StringReader reader = new StringReader(inputForFileFactory);

        Scanner scanner = new Scanner(reader);

        AbstractFigureFactory abstractFigureFactory = new AbstractFigureFactory(scanner);
        FigureFactoryAPI factory = abstractFigureFactory.create();

        assertEquals(FileFigureFactory.class.getTypeName(), factory.getClass().getTypeName(),
            "Created factory should be of type FileFigureFactory!");
    }

    @Test
    void testIfCreatedFileFactoryReturnsRightAmountOfFigures() {
        StringReader reader = new StringReader(inputForFileFactory);

        Scanner scanner = new Scanner(reader);

        AbstractFigureFactory abstractFigureFactory = new AbstractFigureFactory(scanner);
        FigureFactoryAPI factory = abstractFigureFactory.create();

        Collection<Figure> figures = factory.getFigures();

        String expected = """
            Triangle 3333.3 2.01 1
            Triangle 4 3.4 2
            Rectangle 9 8
            Circle 5.5 (3, 8.4)
                """;

        StringBuilder sb = new StringBuilder();

        for (Figure f : figures) {
            sb.append(f.toString());
            sb.append('\n');
        }

        assertEquals(expected, sb.toString(),
            "Created factory should return right same figures like these in the input file!");
    }
}
