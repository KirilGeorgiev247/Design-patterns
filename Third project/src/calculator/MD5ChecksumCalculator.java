package calculator;

import file.observer.Observable;
import file.observer.Observer;
import file.observer.message.Message;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class MD5ChecksumCalculator implements ChecksumCalculator, Observable {

    private static final String MD5_NAME = "MD5";
    private static final int BUFF_SIZE = 1024;
    private final List<Observer> observers;

    public MD5ChecksumCalculator() {
        observers = new ArrayList<>();
    }

    @Override
    public String calculate(InputStream is) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance(MD5_NAME);
        int totalRead = 0;
        try (DigestInputStream dis = new DigestInputStream(is, md)) {
            byte[] buffer = new byte[BUFF_SIZE];
            int read = 0;
            while ((read = dis.read(buffer)) > 0) {
                md.update(buffer, 0, read);
                totalRead += read;
                notifyObservers(new Message(Message.MessageType.PROGRESS_UPDATE, totalRead));
            }
        }

        byte[] digest = md.digest();
        BigInteger bigInt = new BigInteger(1, digest);

        return bigInt.toString(16);
    }

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
}
