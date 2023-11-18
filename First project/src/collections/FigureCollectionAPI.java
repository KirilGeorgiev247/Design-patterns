package collections;

import figures.Figure;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

public interface FigureCollectionAPI {
    Figure get(int index);
    void add(Figure figureToAdd);
    void addRange(Collection<Figure> figuresToAdd);
    void delete(Figure figureToDelete);

    boolean contains(Figure figure);

    int size();

    void duplicate(Figure figureToDuplicate) throws CloneNotSupportedException;

    void storeIntoFile(String path, boolean append) throws FileNotFoundException, IOException;
}
