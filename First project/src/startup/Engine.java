package startup;

import collections.FigureCollection;
import collections.FigureCollectionAPI;
import factories.asbstract.AbstractFigureFactory;
import factories.methods.FigureFactoryAPI;

import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

import static utilities.MyLogger.log;

public class Engine {

    private int parseIndex(String line) {
        try {
            return Integer.parseInt(line.split(" ")[1]);
        } catch (NumberFormatException numberFormatException) {
            log("Invalid input!");
        }
        return -1;
    }

    private String parseFileName(String line) {
        return line.split(" ")[1];
    }

    private boolean parseAppendInfo(String line) {
        try {
            return Objects.equals(line.split(" ")[2], "1");
        } catch (NumberFormatException numberFormatException) {
            log("Invalid input!");
        }
        return false;
    }

    void start() {
        Locale.setDefault(Locale.ENGLISH);

        FigureCollectionAPI figureCollection = new FigureCollection();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                AbstractFigureFactory abstractFigureFactory = new AbstractFigureFactory(scanner);
                FigureFactoryAPI figureFactory = abstractFigureFactory.create();
                figureCollection.addRange(figureFactory.getFigures());
                break;
            } catch (Exception ex) {

            }
        }

        log("Enter command(<Print>, <Delete [index]>, " + "<Duplicate [index]> " +
            "<Store [filename] [appendValue](0 for no append, 1 for append)>: ");
        String command = scanner.nextLine();

        try {
            while (!command.isBlank() && !command.isEmpty()) {
                switch (command.split(" ")[0].toLowerCase()) {
                    case "print" -> System.out.printf(figureCollection.toString());
                    case "delete" -> {
                        int parsedIndex = parseIndex(command);
                        if (parsedIndex >= 0) {
                            figureCollection.delete(figureCollection.get(parseIndex(command)));
                        }
                    }
                    case "duplicate" -> {
                        int parsedIndex = parseIndex(command);
                        if (parsedIndex >= 0) {
                            figureCollection.duplicate(figureCollection.get(parseIndex(command)));
                        }
                    }
                    case "store" -> figureCollection.storeIntoFile(parseFileName(command), parseAppendInfo(command));
                }
                log("Enter command(<Print>, <Delete [index]>, " + "<Duplicate [index] " +
                    "<Store [filename]> [appendValue](0 for no append, 1 for append)>): ");
                command = scanner.nextLine();
            }
        } catch (Exception ex) {
            log("Invalid command!");
        }
        log("Ended.");
    }
}
