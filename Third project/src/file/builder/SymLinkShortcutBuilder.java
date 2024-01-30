package file.builder;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public abstract class SymLinkShortcutBuilder implements FileBuilder {
    protected final Set<Path> visitedPaths;
    protected FileBuilder regularBuilder;

    public SymLinkShortcutBuilder() {
        this.visitedPaths = new HashSet<>();
        regularBuilder = new RegularFileBuilder();
    }
}
