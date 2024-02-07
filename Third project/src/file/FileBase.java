package file;

import file.visitor.FileVisitor;

import java.io.FileNotFoundException;
import java.io.InputStream;

public interface FileBase {
    public void accept(FileVisitor visitor);

    public long getSize();

    public String getPath();

    public InputStream getInputStream() throws FileNotFoundException;
}
