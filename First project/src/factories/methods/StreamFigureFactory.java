package factories.methods;

import exceptions.FigureTypeDoesNotExist;
import exceptions.InvalidArgumentsToCreateSpecifiedFigure;
import figures.Circle;
import figures.Figure;
import figures.Point;
import figures.Rectangle;
import figures.Triangle;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import static utilities.MyLogger.log;

public class StreamFigureFactory implements FigureFactoryAPI {
    private final Scanner sc;

    public StreamFigureFactory(InputStream input) {
        Locale.setDefault(Locale.ENGLISH);

        sc = new Scanner(input);
    }

    private Point getCircleCenter(String point) {
        String[] coordinates = point.substring(1, point.length() - 1).split(",");

        double x = Double.parseDouble(coordinates[0]);
        double y = Double.parseDouble(coordinates[1]);

        return new Point(x, y);
    }

    private Figure getFigure(String line) {
        String[] args = line.split(" ");

        if (args[0].compareToIgnoreCase("triangle") == 0) {
            if (args.length != 4) {
                throw new InvalidArgumentsToCreateSpecifiedFigure("triangle");
            }

            double sideA = Double.parseDouble(args[1]);
            double sideB = Double.parseDouble(args[2]);
            double sideC = Double.parseDouble(args[3]);

            return new Triangle(sideA, sideB, sideC);
        } else if (args[0].compareToIgnoreCase("rectangle") == 0) {
            if (args.length != 3) {
                throw new InvalidArgumentsToCreateSpecifiedFigure("rectangle");
            }

            double sideA = Double.parseDouble(args[1]);
            double sideB = Double.parseDouble(args[2]);

            return new Rectangle(sideA, sideB);
        } else if (args[0].compareToIgnoreCase("circle") == 0) {
            if (args.length != 4) {
                throw new InvalidArgumentsToCreateSpecifiedFigure("circle");
            }

            double radius = Double.parseDouble(args[1]);
            Point center = getCircleCenter(args[2] + args[3]);

            return new Circle(radius, center);
        } else {
            throw new FigureTypeDoesNotExist();
        }
    }

    // TODO: ask for try catch and what to do
    //  Also format exception could be thrown if side is not a number
    @Override
    public List<Figure> getFigures() {
        List<Figure> figures = new ArrayList<>();

        try {
            while (sc.hasNextLine()) {
                String currLine = sc.nextLine();

                if (currLine.isBlank() || currLine.isEmpty()) {
                    break;
                }

                figures.add(getFigure(currLine));
            }
        } catch (FigureTypeDoesNotExist typeDoesNotExist) {
            log(typeDoesNotExist.getMessage());
        } catch (InvalidArgumentsToCreateSpecifiedFigure invalidArgumentsToCreateSpecifiedFigure) {
            switch (invalidArgumentsToCreateSpecifiedFigure.getMessage()) {
                case "triangle" -> log("Triangle input should look like this: <triangle> <sideA> <sideB> <sideC> \n");
                case "rectangle" -> log("Rectangle input should look like this: <rectangle> <sideA> <sideB>");
                case "circle" -> log("Circle input should look like this: <circle> <radius> <(x, y)>");
            }
        } catch (Exception ex) {
            log(ex.getMessage());
        }

        return figures;
    }
}
