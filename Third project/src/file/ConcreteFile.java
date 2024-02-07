package file;

import file.visitor.FileVisitor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class ConcreteFile implements FileBase {
    private String path;
    private long size;
    public ConcreteFile(String path, long size) {
        this.path = path;
        this.size = size;
    }

    @Override
    public void accept(FileVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        return new FileInputStream(path);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConcreteFile that = (ConcreteFile) o;
        return size == that.size &&
            Objects.equals(path, ((ConcreteFile) o).path);
    }
}
