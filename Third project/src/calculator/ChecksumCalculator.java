package calculator;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

public interface ChecksumCalculator {
    public String calculate(InputStream is) throws NoSuchAlgorithmException, IOException;
}