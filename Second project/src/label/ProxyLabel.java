package label;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

// TODO: how to request from inputStream
public class ProxyLabel implements Label {
    private final static int DEFAULT_TIMEOUT = 5;
    private static int timeOutCounter;
    private final BufferedReader reader;
    private String value;
    private final int timeOut;

    public ProxyLabel(InputStream inputStream) {
        this(DEFAULT_TIMEOUT, inputStream);
    }

    public ProxyLabel(int timeOut, InputStream inputStream) {
        value = null;
        timeOutCounter = timeOut;
        this.timeOut = timeOut;
        reader = new BufferedReader(new InputStreamReader(inputStream));
        inputStream.mark(1 << 16); // Mark the stream with a large read-ahead limit
    }

    @Override
    public String getText() {
        if (value == null || timeOutCounter == 0) {
            timeOutCounter = timeOut;
            requestInput();
        }

        timeOutCounter--;
        return value;
    }

    private void requestInput() {
        if (value != null && !value.isEmpty() && !value.isBlank()) {
            System.out.println(
                "Choose if you want to change label value(Press enter for no and type new value for yes):");
        }

        String input = null;
        try {
            input = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e); // TODO
        }

        if (input == null || input.isEmpty() || input.isBlank()) {
            return;
        }

        value = input;
    }

    public void closeReader() throws IOException {
        reader.close();
    }
}
