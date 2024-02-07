package writer;

import calculator.ChecksumCalculator;
import file.FileBase;
import memento.Memento;
import observer.Observable;
import observer.Observer;
import observer.message.Message;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;

public class HashStreamWriter extends Observable implements Observer, Runnable {
    private final Object pauseLock = new Object();
    private final OutputStream os;
    private final CountDownLatch latch;
    BlockingQueue<FileBase> filesToProcess;
    private volatile boolean running = true;
    private volatile boolean paused = false;
    private ChecksumCalculator calc;

    public HashStreamWriter(ChecksumCalculator calc, OutputStream os, CountDownLatch latch) {
        this.calc = calc;
        this.os = os;
        filesToProcess = new LinkedBlockingDeque<>();
        this.latch = latch;

        if (calc instanceof Observable) {
            ((Observable) calc).registerObserver(this);
        }
    }

    @Override
    public void run() {
        while (running) {
            synchronized (pauseLock) {
                if (paused) {
                    try {
                        notifyObservers(this, new Message(Message.MessageType.PAUSED, "Paused"));

                        pauseLock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        this.stop();
                    }
                }
            }

            if (!filesToProcess.isEmpty()) {
                FileBase fileToProcess = filesToProcess.poll();

                notifyObservers(this, new Message(Message.MessageType.NEW_FILE, fileToProcess.getPath(),
                    fileToProcess.getSize()));

                try (InputStream is = fileToProcess.getInputStream();
                     OutputStream bos = new BufferedOutputStream(os)) {

                    String hash = calc.calculate(is);
                    String info = hash + " " + fileToProcess.getPath() + System.lineSeparator();
                    bos.write(info.getBytes());
                    bos.flush();

                } catch (IOException | NoSuchAlgorithmException e) {
                    notifyObservers(this, new Message(Message.MessageType.ERROR, "Error occurred!"));
                } finally {
                    latch.countDown();
                }
            }
        }
    }

    public void addToProcess(FileBase file) {
        filesToProcess.add(file);
    }

    public Memento save() {
        return new Memento(filesToProcess);
    }

    public void restore(Memento memento) {
        this.filesToProcess.clear();
        this.filesToProcess.addAll(memento.getState());
    }

    @Override
    public void update(Observable source, Message message) {
        // Forward the message to the observers of DirectoryHashStreamWriter
        notifyObservers(this, message);
    }

    public void changeCalculator(ChecksumCalculator calc) {
        if (this.calc instanceof Observable) {
            ((Observable) this.calc).removeObserver(this);
        }

        this.calc = calc;

        if (calc instanceof Observable) {
            ((Observable) calc).registerObserver(this);
        }
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            notifyObservers(this, new Message(Message.MessageType.RESUMED, "Resuming"));
            pauseLock.notifyAll();
        }
    }

    public void stop() {
        running = false;
        notifyObservers(this, new Message(Message.MessageType.COMPLETE, "Completed"));
        resume();
    }
}
