package file.visitor.writer;

import calculator.ChecksumCalculator;
import file.ConcreteFile;
import file.Folder;
import file.observer.Observable;
import file.observer.Observer;
import file.observer.message.Message;
import file.visitor.DFSFileVisitor;
import file.visitor.writer.memento.HashWriterMemento;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

// TODO: check
public class HashStreamWriter extends DFSFileVisitor implements Observer, Observable, Runnable {
    private final PrintWriter writer;
    private final List<Observer> observers;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final AtomicBoolean paused = new AtomicBoolean(false);
    private String currentFilePath;
    private long currentPosition;

    public HashStreamWriter(ChecksumCalculator checksumCalculator, OutputStream outputStream) {
        super(checksumCalculator);
        observers = new ArrayList<>();
        this.writer = new PrintWriter(outputStream, true);
        if (checksumCalculator instanceof Observable) {
            ((Observable) checksumCalculator).registerObserver(this);
        }
    }
    @Override
    public void walk(ConcreteFile concreteFile) {
        notifyObservers(new Message(Message.MessageType.NEW_FILE, concreteFile.getPath()));
        super.walk(concreteFile); // Call to parent method to calculate checksum
        try(InputStream is = new BufferedInputStream(new FileInputStream(concreteFile.getPath()))) {
            // Re-calculate checksum for writing purposes. Consider optimizing to avoid double-calculation.
            String hexChecksum = checksumCalculator.calculate(is);
            writer.println(concreteFile.getPath() + ": " + hexChecksum);
        } catch (FileNotFoundException e) {
            writer.println("File not found: " + concreteFile.getPath());
        } catch (IOException | NoSuchAlgorithmException e) {
            writer.println("Error processing file: " + concreteFile.getPath());
        }
    }
    @Override
    public void walk(Folder folder) {
        writer.println("Entering directory: " + folder.getPath());
        super.walk(folder); // Continue traversal for folder contents
    }
    @Override
    public void update(Message message) {
        // Implement how HashStreamWriter should handle the progress updates.
        // This could be simply logging to console or updating a progress bar if you have a GUI.
        // Example implementation could be:
        notifyObservers(message);
        // If you want to forward these updates to another observer (like a ProgressReporter),
        // you would need HashStreamWriter to also implement Observable and notify its observers.
    }
    public String getCurrentFilePath() { return currentFilePath; }
    public void setCurrentFilePath(String path) { this.currentFilePath = path; }

    public long getCurrentPosition() { return currentPosition; }
    public void setCurrentPosition(long position) { this.currentPosition = position; }
    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Message message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    @Override
    public void run() {
        try {
            while (running.get()) {
                checkPaused(); // Check if the process is paused and wait if so

                // Simulate file processing - replace this with your actual file processing logic
                ConcreteFile file = getNextFileToProcess(); // Implement this method to retrieve the next file
                if (file != null) {
                    this.currentFilePath = file.getPath();
                    notifyObservers(new Message(Message.MessageType.PROCESSING_STARTED, file.getPath()));

                    try (InputStream is = new BufferedInputStream(new FileInputStream(file.getPath()))) {
                        String hexChecksum = checksumCalculator.calculate(is);
                        writer.println(file.getPath() + ": " + hexChecksum);
                        notifyObservers(new Message(Message.MessageType.PROCESSING_COMPLETED, file.getPath()));
                    } catch (FileNotFoundException e) {
                        writer.println("File not found: " + file.getPath());
                    } catch (IOException | NoSuchAlgorithmException e) {
                        writer.println("Error processing file: " + file.getPath());
                    }

                    // After processing, you might want to update currentPosition
                    currentPosition += file.getSize(); // Implement getFileSize to determine the size of the processed file
                } else {
                    // Optionally, wait or end the thread if there are no files left to process
                    running.set(false); // Or implement a waiting mechanism
                }
            }
        } catch (Exception e) {
            // Handle any unexpected exceptions
            e.printStackTrace();
        } finally {
            closeWriter();
        }
    }
    public void pause() {
        paused.set(true);
    }

    public void resume() {
        synchronized (paused) {
            paused.set(false);
            paused.notifyAll();
        }
    }

    private void checkPaused() {
        while (paused.get()) {
            synchronized (paused) {
                try {
                    paused.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
    public HashWriterMemento save() {
        return HashWriterMemento.save(this);
    }

    public void restore(HashWriterMemento memento) {
        memento.restore(this);
    }
    public void closeWriter() {
        if (writer != null) {
            writer.close();
        }
    }
}