package observer;

import observer.message.Message;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable {

    protected final List<Observer> observers;

    protected Observable() {
        observers = new ArrayList<>();
    }
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
    public void notifyObservers(Observable source, Message message) {
        for (Observer observer : observers) {
            observer.update(this, message);
        }
    }
}