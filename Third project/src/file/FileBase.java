package file;

import file.builder.FileBuilder;
import file.visitor.DFSFileVisitor;

// TODO: Store the file path and the size of the file in bytes.
public interface FileBase {
    public void accept(DFSFileVisitor visitor);

    public long getSize();

    public String getPath();
}
