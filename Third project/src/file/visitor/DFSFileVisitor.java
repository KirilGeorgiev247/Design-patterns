package file.visitor;

import calculator.ChecksumCalculator;
import file.ConcreteFile;
import file.FileBase;
import file.Folder;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

public class DFSFileVisitor implements FileVisitor {

    private ChecksumCalculator checksumCalculator;
    public DFSFileVisitor(ChecksumCalculator checksumCalculator) {
        this.checksumCalculator = checksumCalculator;
    }
    @Override
    public void walk(ConcreteFile concreteFile) {
        try(InputStream is = new BufferedInputStream(new FileInputStream(concreteFile.getName()))) {
            String hexChechsum = checksumCalculator.calculate(is);
            // observer
        } catch (IOException e) {
            throw new RuntimeException(e); // TODO: uncheckedioexc
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e); // TODO: invalidalgexc
        }
    }

    @Override
    public void walk(Folder folder) {
        for (FileBase child : folder.getChildren()) {
            child.accept(this);
        }
    }
}
