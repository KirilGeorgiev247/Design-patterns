package file.builder;

import file.ConcreteFile;
import file.FileBase;
import file.Folder;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class SymbolLinksBuilder extends SymLinkShortcutBuilder {
    public SymbolLinksBuilder() {
        super();
    }
    @Override
    public FileBase createFile(Path path) {
        try {
            if (!visitedPaths.add(path.toRealPath())) {
                throw new IllegalStateException("Cycle detected in symbolic links for: " + path);
            }

            Path targetPath = Files.readSymbolicLink(path);

            return regularBuilder.createFile(targetPath);
        } catch (IOException e) {
            throw new RuntimeException(e); // TODO: uncheckedexc
        }
    }
}
