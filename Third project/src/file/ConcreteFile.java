package file;

import file.visitor.DFSFileVisitor;

import java.io.IOException;
import java.nio.file.Path;

public class ConcreteFile implements FileBase {

    private String path;
    private long size;
    public ConcreteFile(String path, long size) {
        this.path = path;
        this.size = size;
    }

    @Override
    public void accept(DFSFileVisitor visitor) {
        visitor.walk(this);
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public String getPath() {
        return path;
    }
}
