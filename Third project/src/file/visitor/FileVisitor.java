package file.visitor;

import file.ConcreteFile;
import file.Folder;

public interface FileVisitor {
    public void visit(ConcreteFile concreteFile);
    public void visit(Folder folder);
}
