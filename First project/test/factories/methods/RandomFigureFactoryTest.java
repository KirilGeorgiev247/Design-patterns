package factories.methods;

import figures.Circle;
import figures.Figure;
import figures.Rectangle;
import figures.Triangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static utilities.MyFileParser.getMaxFigureValue;

public class RandomFigureFactoryTest {

    final double tolerance = 0.95;
    final int figuresCount = 100000;

    final double averageTypeCount = 1 / 3;
    final double minimumTypeCountExpected = figuresCount * averageTypeCount * tolerance;

    final int maxFigureValue = getMaxFigureValue("./resources/config/random.txt");
    final int rangesCount = 10;
    final double averageValuesCount = tolerance * figuresCount * (3 + 3 + 2) / 3;
    final double rangeValue = maxFigureValue / rangesCount;
    final double minimumValueRangeCountExpected = averageValuesCount / rangesCount;

    RandomFigureFactory figureFactory;
    Collection<Figure> figures;

    private int roundValue(Double value) {
        value /= rangeValue;

        return value.intValue();
    }

    private Collection<Integer> getExtractedSides(String[] values) {
        List<Integer> roundedValues = new ArrayList<>();

        for (String value : values) {
            roundedValues.add(roundValue(Double.parseDouble(value)));
        }

        return roundedValues;
    }

    private Collection<Integer> getExtractedRadiusAndCoordinates(String[] values) {
        List<Integer> roundedValues = new ArrayList<>();

        roundedValues.add(roundValue(Double.parseDouble(values[0])));

        roundedValues.add(roundValue(Double.parseDouble(values[1].substring(1, values[1].length() - 1))));
        roundedValues.add(roundValue(Double.parseDouble(values[2].substring(0, values[2].length() - 1))));

        return roundedValues;
    }

    private Collection<Integer> extractValues(Figure figure) {
        String[] strArr = figure.toString().split(" ");

        return switch (strArr[0]) {
            case "Triangle", "Rectangle" -> getExtractedSides(Arrays.copyOfRange(strArr, 1, strArr.length));
            case "Circle" -> getExtractedRadiusAndCoordinates(Arrays.copyOfRange(strArr, 1, strArr.length));
            default -> null;
        };
    }

    private Map<Integer, Double> getIntegerDoubleMap() {
        Map<Integer, Double> figuresValueRangesCount = new HashMap<>();

        for (int i = 0; i < rangesCount; i++) {
            figuresValueRangesCount.put(i, 0.0);
        }

        for (Figure figure :
            figures) {
            Collection<Integer> extractedValues = extractValues(figure);

            for (Integer value :
                extractedValues) {
                Double count = figuresValueRangesCount.get(value);
                figuresValueRangesCount.put(value, count + 1);
            }
        }
        return figuresValueRangesCount;
    }

    @BeforeEach
    void setUp() {
        figureFactory = new RandomFigureFactory(figuresCount, maxFigureValue);
        figures = figureFactory.getFigures();
    }

    @RepeatedTest(10)
        // it works almost every time and with 1m (not recommended for the device)
    void testIfRandomFigureFactoryCreatesAppropriatelyAmountOfEachTypeOfFigure() {
        Map<String, Integer> figuresTypeCount = new HashMap<>();

        figuresTypeCount.put(Triangle.class.getTypeName(), 0);
        figuresTypeCount.put(Rectangle.class.getTypeName(), 0);
        figuresTypeCount.put(Circle.class.getTypeName(), 0);

        for (Figure figure :
            figures) {
            String key = figure.getClass().getTypeName();
            Integer value = figuresTypeCount.get(key);

            figuresTypeCount.put(key, value + 1);
        }

        assertTrue(Double.compare(minimumTypeCountExpected, figuresTypeCount.get(Triangle.class.getTypeName())) < 0,
            "Triangles count should be at almost 1/3 of all created figures!");

        assertTrue(Double.compare(minimumTypeCountExpected, figuresTypeCount.get(Rectangle.class.getTypeName())) < 0,
            "Rectangles count should be at almost 1/3 of all created figures!");

        assertTrue(Double.compare(minimumTypeCountExpected, figuresTypeCount.get(Circle.class.getTypeName())) < 0,
            "Circles count should be at almost 1/3 of all created figures!");
    }

    @RepeatedTest(10)
    void testIfRandomFigureFactoryCreatesRandomFigureValues() {
        Map<Integer, Double> figuresValueRangesCount = getIntegerDoubleMap();

        boolean rangesCountIsInBound = true;

        for (Map.Entry<Integer, Double> entry :
            figuresValueRangesCount.entrySet()) {

            Double test = minimumValueRangeCountExpected;

            if (Double.compare(entry.getValue(), minimumValueRangeCountExpected) < 0) {
                rangesCountIsInBound = false;
                break;
            }
        }

        assertTrue(rangesCountIsInBound,
            "All figures values should be evenly generated in equal ranges of values!");
    }
}
