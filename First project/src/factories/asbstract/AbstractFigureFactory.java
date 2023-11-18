package factories.asbstract;

import exceptions.InvalidCreationMethod;
import factories.methods.FigureFactoryAPI;
import factories.methods.FileFigureFactory;
import factories.methods.RandomFigureFactory;
import factories.methods.STDINFigureFactory;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import static utilities.MyLogger.log;
import static utilities.MyFileParser.getMaxFigureValue;

public class AbstractFigureFactory implements AbstractFigureFactoryAPI {
    private Scanner sc;

    private String getTypeInput() {
        log("Enter creation method (Console, File, Random): \n");
        String line = sc.nextLine();
        return line;
    }

    private int getRandomInput() {
        log("Enter <figures count>: \n");
        int count = 0;

        try {
            count = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException ex) {
            log("Invalid number as input! \n");
        }

        return count;
    }
    public AbstractFigureFactory(Scanner sc) {
        Locale.setDefault(Locale.ENGLISH);
        this.sc = sc;
    }

    private FileFigureFactory getFileFactory() {
        log("Enter <file name>: \n");

        String fileName = sc.nextLine();
        FileInputStream fileInputStream;

        try {
          fileInputStream  = new FileInputStream("./resources/input/" + fileName);
          return new FileFigureFactory(fileInputStream);
        } catch (FileNotFoundException fileNotFoundException) {
            log("Such file is not found!");
        }

        return null;
    }

    private STDINFigureFactory getStdinFigureFactory() {
        log("Enter <figureType> <params...> and tap new line when you are ready: \n");
        List<String> input = new ArrayList<>();

        while(true) {
            String currLine = sc.nextLine();
            if (currLine.isEmpty() || currLine.isBlank()) {
                break;
            }
            input.add(currLine);
        }

        StringBuilder sb = new StringBuilder();

        for (String str : input) {
            sb.append(str);
            sb.append('\n');
        }

        return new STDINFigureFactory(new ByteArrayInputStream(sb.toString().getBytes()));
    }

    @Override
    public FigureFactoryAPI create() {
        return switch (getTypeInput()) {
            case "Console" -> getStdinFigureFactory();
            case "File" -> getFileFactory();
            case "Random" -> new RandomFigureFactory(getRandomInput(), getMaxFigureValue("./resources/config/random.txt"));
            default -> throw new InvalidCreationMethod();
        };
    }
}
