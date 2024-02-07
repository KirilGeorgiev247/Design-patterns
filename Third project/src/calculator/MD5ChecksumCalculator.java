package calculator;

import observer.Observable;
import observer.message.Message;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5ChecksumCalculator extends Observable implements ChecksumCalculator {
    private static final String MD5_NAME = "MD5";
    private static final int BUFF_SIZE = 1024;
    @Override
    public String calculate(InputStream is) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance(MD5_NAME);
        int totalRead = 0;
        try (DigestInputStream dis = new DigestInputStream(is, md)) {
            byte[] buffer = new byte[BUFF_SIZE];
            int read = 0;
            while ((read = dis.read(buffer)) > 0) {
                totalRead += read;
                notifyObservers(this, new Message(Message.MessageType.PROGRESS_UPDATE, totalRead));
            }
        }

        byte[] digest = md.digest();
        BigInteger bigInt = new BigInteger(1, digest);

        return bigInt.toString(16);
    }
}
