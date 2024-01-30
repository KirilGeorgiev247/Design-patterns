package file.builder;

import file.ConcreteFile;
import file.FileBase;
import file.Folder;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class RegularFileBuilder implements FileBuilder {

    FileBuilder windowsShortcutBuilder;
    FileBuilder symLinkBuilder;
    public RegularFileBuilder() {
        windowsShortcutBuilder = new WindowsShortcutBuilder();
        symLinkBuilder = new SymbolLinksBuilder();
    }
    @Override
    public FileBase createFile(Path path) throws IOException {
        try {
            if (Files.isDirectory(path)) {
                Folder folder = new Folder(path.getFileName().toString());
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
                    for (Path entry : stream) {
                        if(Files.isSymbolicLink(entry)) {
                            folder.addChild(symLinkBuilder.createFile(entry));
                        } else if (isWindowsShortcut(entry)) {
                            folder.addChild(windowsShortcutBuilder.createFile(entry));
                        } else {
                            folder.addChild(createFile(entry));
                        }
                    }
                }
                return folder;
            }

            return new ConcreteFile(path.getFileName().toString(), Files.size(path));
        } catch (IOException e) {
            throw new RuntimeException(e); // TODO: uncheckedexc
        }
    }

    private boolean isWindowsShortcut(Path path) {
        return path.toString().toLowerCase().endsWith(".lnk");
    }
}
