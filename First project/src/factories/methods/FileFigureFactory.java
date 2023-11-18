package factories.methods;

import java.io.InputStream;

public class FileFigureFactory extends StreamFigureFactory {
    public FileFigureFactory(InputStream input) {
        super(input);
    }
}
