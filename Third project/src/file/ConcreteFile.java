package file;

import file.visitor.FileVisitor;

public class ConcreteFile implements FileBase {

    private String path;
    private long size;
    public ConcreteFile(String path, long size) {
        this.path = path;
        this.size = size;
    }

    @Override
    public void accept(FileVisitor visitor) {
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
