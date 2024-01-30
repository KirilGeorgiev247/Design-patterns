package file.visitor;

import file.ConcreteFile;
import file.Folder;

public interface FileVisitor {
    public void walk(ConcreteFile concreteFile);
    public void walk(Folder folder);
}
