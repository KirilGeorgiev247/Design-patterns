package factories.methods;

import java.io.InputStream;

public class STDINFigureFactory extends StreamFigureFactory {
    public STDINFigureFactory(InputStream input) {
        super(input);
    }
}
