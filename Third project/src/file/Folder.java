package file;

import file.visitor.FileVisitor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Folder implements FileBase {

    private final List<FileBase> children;

    private final String path;

    private long size;

    public Folder(String path) {
        children = new ArrayList<>();
        this.path = path;
        size = 0;
    }

    public void addChild(FileBase child) {
        children.add(child);
        size += child.getSize();
    }

    public List<FileBase> getChildren() {
        return children;
    }

    @Override
    public void accept(FileVisitor visitor) {
        for (FileBase child : children) {
            child.accept(visitor);
        }
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
}
