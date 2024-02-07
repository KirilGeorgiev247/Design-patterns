package progress;

import calculator.ChecksumCalculator;
import observer.Observable;
import observer.Observer;
import observer.message.Message;
import writer.HashStreamWriter;

public class ProgressReporter implements Observer {
    Message currMessage;
    Long totalBytes;
    Long bytesSoFar = 0L;
    Long bytesRead;
    Long currFileSize;

    String currFile;

    public ProgressReporter(HashStreamWriter hashStreamWriter, Long totalBytes) {
        hashStreamWriter.registerObserver(this);
        this.totalBytes = totalBytes;
        currFile = "";
    }

    public void update(Observable sender, Message message) {
        currMessage = message;
        if(sender instanceof ChecksumCalculator) {
            bytesSoFar += message.getBytesProcessed();
            bytesRead += message.getBytesProcessed();
        }
        else if(sender instanceof HashStreamWriter) {
            System.out.print('\n');
            bytesRead = 0L;
            currFileSize = message.getBytesProcessed();
            currFile = message.getMessage();
        }
        else {
            throw new IllegalArgumentException("Unexpected message");
        }

        refreshDisplay();
    }

    private void refreshDisplay() {
        switch (currMessage.getType()) {
            case NEW_FILE -> {
                System.out.print("\rProcessing " + currFile);
                currFileSize = currMessage.getTotalFileBytes();
            }
            case PROGRESS_UPDATE -> {
                String info = getProcessingInfo();
                System.out.print(info);
            }
            case COMPLETE, PAUSED, RESUMED, ERROR -> System.out.print("\r" + currMessage.getMessage());
        }
    }

    private String getProcessingInfo() {
        double currPercentage = ((double) bytesRead / currFileSize) * 100;
        double totalPercentage = ((double) bytesSoFar / totalBytes) * 100;

        currPercentage = Math.round(currPercentage * 100.0) / 100.0;
        totalPercentage = Math.round(totalPercentage * 100.0) / 100.0;

        return "\rProcessing " + currFile + "... " +
            bytesRead + " byte(s) read - " + currPercentage +
            "%. Total: " + totalPercentage + "%";
    }
}

