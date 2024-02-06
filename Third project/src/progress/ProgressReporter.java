package progress;

import file.observer.Observer;
import file.observer.message.Message;

public class ProgressReporter implements Observer {
    private String currentPath = "(nothing)";
    private long bytesRead = 0;

    @Override
    public void update(Message message) {
        // Check the type of message and update accordingly
        if (message.getType() == Message.MessageType.PROGRESS_UPDATE) {
            // Update progress for the current file
            bytesRead += message.getBytesProcessed();
        } else if (message.getType() == Message.MessageType.NEW_FILE) {
            // A new file processing starts, reset bytesRead and update currentPath
            System.out.print('\n'); // Move to the next line for a new file
            currentPath = message.getMessage();
            bytesRead = 0; // Reset bytesRead for the new file
        }

        // Refresh display after processing any type of message
        refreshDisplay();
    }

    private void refreshDisplay() {
        // Print progress information on the same line using carriage return

        System.out.print("\rProcessing " + currentPath + "... " + bytesRead + " byte(s) read");
    }
}