package file;

import file.visitor.DFSFileVisitor;

import java.util.List;

public class Folder extends FileBase {

    private List<FileBase> children;

    public Folder(String name, long size) {
        super(name, size);
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
}
