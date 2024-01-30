package file;

import file.visitor.DFSFileVisitor;

import java.util.List;

public class Folder implements FileBase {

    private List<FileBase> children;

    private String name;

    private long size;

    public Folder(String name) {
        this.name = name;
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
    public void accept(DFSFileVisitor visitor) {
        visitor.walk(this);

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
        return name;
    }
}
