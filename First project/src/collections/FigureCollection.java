package collections;

import figures.Figure;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FigureCollection implements FigureCollectionAPI {
    private List<Figure> figures;
    public FigureCollection() {
        figures = new ArrayList<>();
    }

    @Override
    public Figure get(int index) {
        return figures.get(index);
    }

    @Override
    public void add(Figure figureToAdd) {
        figures.add(figureToAdd);
    }

    @Override
    public void addRange(Collection<Figure> figuresToAdd) {
        figures.addAll(figuresToAdd);
    }

    @Override
    public void delete(Figure figureToDelete) {

        figures.remove(figureToDelete);
        var test = figures;
    }

    @Override
    public boolean contains(Figure figure) {
        return figures.contains(figure);
    }

    @Override
    public int size() {
        return figures.size();
    }

    @Override
    public void duplicate(Figure figureToDuplicate) throws CloneNotSupportedException {
        Figure clone = figureToDuplicate.clone();
        figures.add(clone);
    }

    @Override
    public void storeIntoFile(String path, boolean append) throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream("./resources/output/" + path, append);

        fos.write(this.toString().getBytes());

        fos.close();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Figure f :
            figures) {
            sb.append(f.toString());
            sb.append('\n');
        }

        return sb.toString();
    }
}
