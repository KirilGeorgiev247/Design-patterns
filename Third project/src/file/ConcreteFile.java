package file;

import file.visitor.DFSFileVisitor;

public class ConcreteFile extends FileBase {
    public ConcreteFile(String name, long size) {
        super(name, size);
    }

    @Override
    public void accept(DFSFileVisitor visitor) {
        visitor.walk(this);
    }
}
