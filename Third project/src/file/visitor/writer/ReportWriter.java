package file.visitor.writer;

import file.ConcreteFile;
import file.FileBase;
import file.Folder;
import file.visitor.FileVisitor;

import java.io.OutputStream;
import java.io.PrintWriter;

public class ReportWriter implements FileVisitor {
    private PrintWriter writer;

    public ReportWriter(OutputStream outputStream) {
        this.writer = new PrintWriter(outputStream, true);
    }

    @Override
    public void walk(ConcreteFile concreteFile) {
        writer.println(concreteFile.getPath() + " - Size: " + concreteFile.getSize() + " bytes");
    }

    @Override
    public void walk(Folder folder) {
        // Folder-specific reporting, if needed
        writer.println("Entering directory: " + folder.getPath());
        for (FileBase child : folder.getChildren()) {
            child.accept(this);
        }
    }
}
