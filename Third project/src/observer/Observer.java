package observer;

import observer.message.Message;

public interface Observer {
    void update(Observable sender, Message message);
}