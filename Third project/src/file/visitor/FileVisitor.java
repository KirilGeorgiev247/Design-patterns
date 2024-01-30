package file.visitor;

import file.ConcreteFile;
import file.Folder;

// TODO: rename: visit
public interface FileVisitor {
    public void walk(ConcreteFile concreteFile);
    public void walk(Folder folder);
}
