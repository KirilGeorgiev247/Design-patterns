package file.observer.message;

public class Message {
    private final MessageType type;
    private final String message;
    private final int bytesProcessed;

    // Enum to distinguish between different types of messages
    public enum MessageType {
        NEW_FILE, PROGRESS_UPDATE, COMPLETE, PAUSED, RESUMED
    }

    // Constructor for progress updates
    public Message(MessageType type, int bytesProcessed) {
        this.type = type;
        this.message = null;
        this.bytesProcessed = bytesProcessed;
    }

    // Constructor for other types of messages (e.g., new file, error)
    public Message(MessageType type, String message) {
        this.type = type;
        this.message = message;
        this.bytesProcessed = 0; // Default or irrelevant for non-progress messages
    }

    // Getters
    public MessageType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public int getBytesProcessed() {
        return bytesProcessed;
    }
}
