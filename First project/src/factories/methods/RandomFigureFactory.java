package factories.methods;

import figures.Circle;
import figures.Figure;
import figures.Point;
import figures.Rectangle;
import figures.Triangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// TODO -> random interface generator
public class RandomFigureFactory implements FigureFactoryAPI {

    private Random figureTypeGenerator;

    private Random valuesGenerator;

    private int count;

    private int figureMaxValue;

    public RandomFigureFactory(int count, int figureMaxValue) {
        this.count = Math.max(count, 0);
        figureTypeGenerator = new Random();
        valuesGenerator = new Random();
        this.figureMaxValue = figureMaxValue;
    }

    private Figure getFigure() {
        int figureType = figureTypeGenerator.nextInt(1, 4);

        switch (figureType) {
            case 1 -> {
                double sideA = valuesGenerator.nextDouble(1, figureMaxValue);
                double sideB = valuesGenerator.nextDouble(1, figureMaxValue);
                double sideC = valuesGenerator.nextDouble(1, figureMaxValue);

                return new Triangle(sideA, sideB, sideC);
            }
            case 2 -> {
                double sideA = valuesGenerator.nextDouble(1, figureMaxValue);
                double sideB = valuesGenerator.nextDouble(1, figureMaxValue);

                return new Rectangle(sideA, sideB);
            }
            case 3 -> {
                double radius = valuesGenerator.nextDouble(1, figureMaxValue);
                double x = valuesGenerator.nextDouble(1, figureMaxValue);
                double y = valuesGenerator.nextDouble(1, figureMaxValue);
                Point center = new Point(x, y);

                return new Circle(radius, center);
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public List<Figure> getFigures() {
        List<Figure> figures = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Figure figure = getFigure();
            figures.add(figure);
        }

        return figures;
    }
}
