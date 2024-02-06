package file.visitor.writer.memento;

import file.visitor.FileVisitor;
import file.visitor.writer.HashStreamWriter;

public class HashWriterMemento {
    private final String filePath;
    private final long position;

    private HashWriterMemento(String filePath, long position) {
        this.filePath = filePath;
        this.position = position;
    }

    public static HashWriterMemento save(HashStreamWriter writer) {
        return new HashWriterMemento(writer.getCurrentFilePath(), writer.getCurrentPosition());
    }

    public void restore(HashStreamWriter writer) {
        writer.setCurrentFilePath(this.filePath);
        writer.setCurrentPosition(this.position);
    }

    public String getFilePath() { return filePath; }
    public long getPosition() { return position; }
}
