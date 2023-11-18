package utilities;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Scanner;

import static utilities.MyLogger.log;

public class MyFileParser {
    public static int getMaxFigureValue(String path) {
        Locale.setDefault(Locale.ENGLISH);
        int maxFigureValue = 0;

        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            Scanner scanner = new Scanner(fileInputStream);
            maxFigureValue = scanner.hasNextLine() ? Integer.parseInt(scanner.nextLine().split("=")[1]) : 0;
            fileInputStream.close();
            scanner.close();
        } catch (FileNotFoundException ex) {
            log("Invalid random config!\n");
        } catch (IOException ex) {
            log(ex.getMessage());
        }

        return maxFigureValue;
    }
}
