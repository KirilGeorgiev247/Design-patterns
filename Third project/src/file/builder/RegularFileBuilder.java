package file.builder;

import file.ConcreteFile;
import file.FileBase;
import file.Folder;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class RegularFileBuilder implements FileBuilder {
    private static final String WINDOWS_SHORTCUT_EXT = ".lnk";
    private final FileBuilder windowsShortcutBuilder;
    private final FileBuilder symLinkBuilder;
    private Path baseDirectory = null;
    private final Set<Path> visitedPaths;
    public RegularFileBuilder(FileBuilder windowsShortcutBuilder, FileBuilder symLinkBuilder) {
        this.windowsShortcutBuilder = windowsShortcutBuilder;
        this.symLinkBuilder = symLinkBuilder;
        visitedPaths = new HashSet<>();
    }
    @Override
    public FileBase createFile(Path path) throws IOException {
        if (baseDirectory == null) {
            baseDirectory = path.toAbsolutePath();
        }

        if (!visitedPaths.add(path.toRealPath())) {
            throw new IllegalStateException("Cycle detected in symbolic links for: " + path);
        }

        try {
            if (Files.isDirectory(path)) {
                Folder folder = new Folder(path.getFileName().toString());
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
                    for (Path entry : stream) {
                        if(Files.isSymbolicLink(entry)) {
                            folder.addChild(symLinkBuilder.createFile(entry));
                        } else if (isWindowsShortcut(entry)) {
                            Path relativePath = path.relativize(entry);
                            folder.addChild(windowsShortcutBuilder.createFile(relativePath));
                        } else {
                            folder.addChild(createFile(entry));
                        }
                    }
                }
                return folder;
            }

            String relativePath = baseDirectory != null ?
                baseDirectory.getParent().relativize(path).toString() : path.toString();
            return new ConcreteFile(relativePath, Files.size(path));
        } catch (IOException e) {
            throw new UncheckedIOException(e.getMessage(), e);
        } finally {
            visitedPaths.remove(path.toRealPath());

            if (path.equals(baseDirectory)) {
                baseDirectory = null;
            }
        }
    }

    private boolean isWindowsShortcut(Path path) {
        return path.toString().toLowerCase().endsWith(WINDOWS_SHORTCUT_EXT);
    }
}