package file.visitor;

import file.ConcreteFile;
import file.FileBase;
import file.Folder;
import writer.HashStreamWriter;

public class ConcreteFileVisitor implements FileVisitor {

    HashStreamWriter hashStreamWriter;
    public ConcreteFileVisitor(HashStreamWriter hashStreamWriter) {
        this.hashStreamWriter = hashStreamWriter;
    }
    @Override
    public void visit(ConcreteFile concreteFile) {
        hashStreamWriter.addToProcess(concreteFile);
    }

    @Override
    public void visit(Folder folder) {
        for (FileBase file : folder.getChildren()) {
            file.accept(this);
        }
    }
}
