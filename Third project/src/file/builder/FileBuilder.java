package file.builder;

import file.FileBase;

import java.io.IOException;
import java.nio.file.Path;

public interface FileBuilder {
    public FileBase createFile(Path path) throws IOException;
}