package file.observer;

import file.observer.message.Message;

public interface Observer {
    void update(Message message);
}