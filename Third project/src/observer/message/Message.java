package observer.message;

public class Message {
    private final MessageType type;
    private final String message;
    private final long bytesProcessed;

    private final long totalFileBytes;

    public Message(MessageType type, long bytesProcessed) {
        this.type = type;
        this.message = null;
        this.bytesProcessed = bytesProcessed;
        totalFileBytes = 0;
    }

    public Message(MessageType type, String message) {
        this.type = type;
        this.message = message;
        bytesProcessed = 0;
        totalFileBytes = 0;
    }

    public Message(MessageType type, String message, long totalFileBytes) {
        this.type = type;
        this.message = message;
        this.totalFileBytes = totalFileBytes;
        bytesProcessed = 0;
    }

    public MessageType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public long getBytesProcessed() {
        return bytesProcessed;
    }

    public long getTotalFileBytes() {
        return totalFileBytes;
    }

    public enum MessageType {
        NEW_FILE, PROGRESS_UPDATE, COMPLETE, PAUSED, RESUMED, ERROR
    }
}
