package file.builder;

import file.FileBase;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SymbolLinksBuilder extends SymLinkShortcutBuilder {
    @Override
    public FileBase createFile(Path path) {
        try {
            if (!visitedPaths.add(path.toRealPath())) {
                throw new IllegalStateException("Cycle detected in symbolic links for: " + path);
            }

            Path targetPath = Files.readSymbolicLink(path);

            return regularBuilder.createFile(targetPath);
        } catch (IOException e) {
            throw new UncheckedIOException(e.getMessage(), e);
        }
    }
}