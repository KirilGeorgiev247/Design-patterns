package file;

import file.visitor.DFSFileVisitor;

public abstract class FileBase {
    protected String name;
    protected long size;

    public FileBase(String name, long size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }
    public abstract void accept(DFSFileVisitor visitor);
}
