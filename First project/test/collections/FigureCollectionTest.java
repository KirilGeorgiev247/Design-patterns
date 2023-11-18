package collections;

import figures.Circle;
import figures.Figure;
import figures.Point;
import figures.Rectangle;
import figures.Triangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FigureCollectionTest {

    private String testFilePath = "./resources/output/testStoring.txt";
    FigureCollection figureCollection;

    Triangle triangle;

    Rectangle rectangle;

    Circle circle;

    @BeforeEach
    void setUp() {
        triangle = new Triangle(1, 2, 3);
        rectangle = new Rectangle(1, 2);
        circle = new Circle(1, new Point(2, 3));
    }

    @Test
    void testIfFigureIsAddedSuccessfully() {
        figureCollection = new FigureCollection();

        figureCollection.add(triangle);
        figureCollection.add(rectangle);
        figureCollection.add(circle);

        assertTrue(figureCollection.contains(triangle) &&
                figureCollection.contains(rectangle) &&
                figureCollection.contains(circle),
            "Collection should contain all added items!");

        assertEquals(3, figureCollection.size(),
            "Size should be equal to added figures!");
    }

    @Test
    void testIfFiguresAreAddedSuccessfully() {
        figureCollection = new FigureCollection();

        Collection<Figure> collection = new ArrayList<>();

        collection.add(triangle);
        collection.add(rectangle);
        collection.add(circle);

        figureCollection.addRange(collection);

        assertTrue(figureCollection.contains(triangle) &&
                figureCollection.contains(rectangle) &&
                figureCollection.contains(circle),
            "Collection should contain all added items!");

        assertEquals(3, figureCollection.size(),
            "Size should be equal to added figures!");
    }

    @Test
    void testIfFigureIsDuplicatedAndAdded() throws CloneNotSupportedException {
        figureCollection = new FigureCollection();

        figureCollection.add(triangle);
        figureCollection.duplicate(triangle);

        figureCollection.delete(triangle);

        Figure duplicated = figureCollection.get(0);

        assertEquals(triangle, duplicated,
            "Duplicated item should be equal to the source item!");
    }

    @Test
    void testIfGetMethodReturnsCorrectly() {
        figureCollection = new FigureCollection();

        figureCollection.add(triangle);
        figureCollection.add(rectangle);
        figureCollection.add(circle);

        assertEquals(triangle, figureCollection.get(0),
            "Added figure should be at the added position!");

        assertEquals(rectangle, figureCollection.get(1),
            "Added figure should be at the added position!");

        assertEquals(circle, figureCollection.get(2),
            "Added figure should be at the added position!");
    }

    @Test
    void testIfFigureIsDeletedSuccessfully() {
        figureCollection = new FigureCollection();

        figureCollection.add(triangle);
        figureCollection.add(rectangle);
        figureCollection.add(circle);

        figureCollection.delete(triangle);

        assertFalse(figureCollection.contains(triangle),
            "Removed figure should not be in the collection anymore!");

        assertEquals(2, figureCollection.size(),
            "Figure collection size should be updated after deletion!");
    }

    @Test
    void testIfSizeIsUpdatingCorrectly() {
        figureCollection = new FigureCollection();

        figureCollection.add(triangle);
        figureCollection.add(rectangle);
        figureCollection.add(circle);

        assertEquals(3, figureCollection.size(),
            "Figure collection size should be updated after adding!" );

        figureCollection.delete(triangle);

        assertEquals(2, figureCollection.size(),
            "Figure collection size should be updated after deletion!");
    }

    @Test
    void testIfFiguresAreStoredInFileSuccessfully() throws IOException {
        figureCollection = new FigureCollection();

        figureCollection.add(triangle);
        figureCollection.add(rectangle);
        figureCollection.add(circle);
        figureCollection.add(triangle);

        figureCollection.storeIntoFile(testFilePath, false);

        File file = new File(testFilePath);

        assertTrue(file.exists(), "File should exists after method is called either if it exists before or not!");

        FileInputStream fis = new FileInputStream(file);

        Scanner sc = new Scanner(fis);

        StringBuilder sbForFile = new StringBuilder();

        while(sc.hasNextLine()) {
            sbForFile.append(sc.nextLine());
            sbForFile.append("\n");
        }

        assertEquals(figureCollection.toString(), sbForFile.toString(),
            "Figure collection should have the same content as file when append if false!");
    }

    @Test
    void testIfStoringAppendingIsWorkingCorrectly() throws IOException {
        figureCollection = new FigureCollection();

        figureCollection.add(triangle);
        figureCollection.add(rectangle);
        figureCollection.add(circle);
        figureCollection.add(triangle);

        figureCollection.storeIntoFile(testFilePath, false);

        File file = new File(testFilePath);

        FileInputStream fis1 = new FileInputStream(file);

        Scanner sc1 = new Scanner(fis1);

        StringBuilder sbForFile = new StringBuilder();

        while(sc1.hasNextLine()) {
            sbForFile.append(sc1.nextLine());
            sbForFile.append("\n");
        }

        sc1.close();
        fis1.close();

        assertEquals(figureCollection.toString(), sbForFile.toString(),
            "Figure collection should have the same content as file when append if false even if the file is not empty!");

        figureCollection.add(circle);

        figureCollection.storeIntoFile(testFilePath, true);

        FileInputStream fis2 = new FileInputStream(file);

        Scanner sc2 = new Scanner(fis2);

        StringBuilder sbForFile2 = new StringBuilder();

        while(sc2.hasNextLine()) {
            sbForFile2.append(sc2.nextLine());
            sbForFile2.append("\n");
        }

        sbForFile.append(figureCollection.toString());

        sc2.close();
        fis2.close();

        assertEquals(sbForFile.toString(), sbForFile2.toString(),
            "Figure collection should have the previous and present content stored in file when append is true!");
    }

    @Test
    void testIfStringificationOfFiguresIsRight() {
        figureCollection = new FigureCollection();

        figureCollection.add(triangle);
        figureCollection.add(rectangle);
        figureCollection.add(circle);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < figureCollection.size(); i++) {
            sb.append(figureCollection.get(i).toString());
            sb.append("\n");
        }

        assertEquals(sb.toString(), figureCollection.toString(),
            "Stringification should be all of the figures stringification in collection!");
    }

    @Test
    void testIfContainsIsWorkingCorrectly() {
        figureCollection = new FigureCollection();

        figureCollection.add(triangle);
        figureCollection.add(rectangle);

        assertTrue(figureCollection.contains(triangle),
            "Added figure should be contained in collection!");

        assertFalse(figureCollection.contains(circle),
            "Not added figures should not be contained in collection!");
    }
}
