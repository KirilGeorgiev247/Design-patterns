# Observer

**Allows an object, known as the subject, to maintain a list of its dependents, called observers, and automatically notify them of any state changes, usually by calling one of their methods. It's a foundational element for achieving a loose coupling between these objects**

***

+ in GUI frameworks, for instance, where an action on a user interface element (like a button click) notifies other parts of the program (addEventListener in JS)

+ such as in model-view-controller (MVC) architectures where model changes need to be reflected in the view

+ like in a messaging or notification system where subscribers need to be informed about events or data changes

+ std::observer_ptr(c++)

*pros*

+ the subject doesn't need to know details about the observers. This allows for easy modification and addition of observers without changing the subject

+ dynamically add or remove observers without altering the subject's code

+ observers are notified immediately about any changes in the subject, allowing for real-time response

*cons*

+ keeping a list of observers for each subject can increase memory usage, especially with a large number of observers

***

# Example 01 (in java)

```java
// Observer interface
interface Observer {
    void update(Observable observable, String message);
}

// Concrete Observer
class User implements Observer {
    private String name;

    public User(String name) {
        this.name = name;
    }

    @Override
        public void update(Observable observable, String message) {
        System.out.println(name + " received message from " + observable + ": " + message);
    }
}

// Observable interface
interface Observable {
    void addObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers(String message);
}

// Concrete Observable
class MessagePublisher implements Observable {
    private List<Observer> observers = new ArrayList<>();

    public void postMessage(String message) {
        notifyObservers(message);
    }

    @Override
        public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
        public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
        public void notifyObservers(String message) {
        for (Observer o : observers) {
            o.update(this, message);
        }
    }

    @Override
        public String toString() {
        return "MessagePublisher";
    }
}

public class ObserverPatternDemo {
    public static void main(String[] args) {
        MessagePublisher publisher1 = new MessagePublisher();
        MessagePublisher publisher2 = new MessagePublisher();

        Observer user1 = new User("User1");
        Observer user2 = new User("User2");

        publisher1.addObserver(user1);
        publisher2.addObserver(user1);
        publisher2.addObserver(user2);

        publisher1.postMessage("Message from Publisher 1");
        publisher2.postMessage("Message from Publisher 2");
    }
}
```

# Example 02 (in c++)

```c++
class Observable;

class Observer {
public:
    virtual ~Observer() {}
    virtual void update(Observable* observable, const std::string& message) = 0;
};

// Concrete Observer
class User : public Observer {
private:
    std::string name;

public:
    User(const std::string& name) : name(name) {}

    void update(Observable* observable, const std::string& message) override {
        std::cout << name << " received message from " << observable << ": " << message << std::endl;
    }
};

// Observable interface
class Observable {
public:
    virtual ~Observable() {}
    virtual void addObserver(Observer* o) = 0;
    virtual void removeObserver(Observer* o) = 0;
    virtual void notifyObservers(const std::string& message) = 0;
};

// Concrete Observable
class MessagePublisher : public Observable {
private:
    std::vector<Observer*> observers;

public:
    void postMessage(const std::string& message) {
        notifyObservers(message);
    }

    void addObserver(Observer* o) override {
        observers.push_back(o);
    }

    void removeObserver(Observer* o) override {
        observers.erase(std::remove(observers.begin(), observers.end(), o), observers.end());
    }

    void notifyObservers(const std::string& message) override {
        for (Observer* o : observers) {
            o->update(this, message);
        }
    }
};

int main() {
    MessagePublisher publisher1;
    MessagePublisher publisher2;

    User user1("User1");
    User user2("User2");

    publisher1.addObserver(&user1);
    publisher2.addObserver(&user1);
    publisher2.addObserver(&user2);

    publisher1.postMessage("Message from Publisher 1");
    publisher2.postMessage("Message from Publisher 2");

    return 0;
}
```

# Example 03 (in java)

```java
// Event classes
class Event {
    // Basic event structure
}

class DataEvent extends Event {
    public final String data;

    DataEvent(String data) {
        this.data = data;
    }
}

class ErrorEvent extends Event {
    public final String error;

    ErrorEvent(String error) {
        this.error = error;
    }
}


// Observable interface
interface Observable<T> {
    void addObserver(Observer<T> o);
    void removeObserver(Observer<T> o);
    void notifyObservers(T event);
}

// Observer interface
interface Observer<T> {
    void onEvent(T event);
}


// Concrete Observable
class EventPublisher implements Observable<Event> {
    private List<Observer<Event>> observers = new ArrayList<>();
    private ExecutorService executor = Executors.newCachedThreadPool(); // For asynchronous notification

    public void publishEvent(Event event) {
        executor.submit(()->notifyObservers(event));
    }

    @Override
        public void addObserver(Observer<Event> o) {
        observers.add(o);
    }

    @Override
        public void removeObserver(Observer<Event> o) {
        observers.remove(o);
    }

    @Override
        public void notifyObservers(Event event) {
        observers.forEach(observer->observer.onEvent(event));
    }
}

// Concrete Observers
class DataObserver implements Observer<Event> {
    @Override
        public void onEvent(Event event) {
        if (event instanceof DataEvent) {
            System.out.println("Data Received: " + ((DataEvent)event).data);
        }
    }
}

class ErrorObserver implements Observer<Event> {
    @Override
        public void onEvent(Event event) {
        if (event instanceof ErrorEvent) {
            System.err.println("Error Occurred: " + ((ErrorEvent)event).error);
        }
    }
}


public class ObserverPatternDemo {
    public static void main(String[] args) {
        EventPublisher publisher = new EventPublisher();

        Observer<Event> dataObserver = new DataObserver();
        Observer<Event> errorObserver = new ErrorObserver();

        publisher.addObserver(dataObserver);
        publisher.addObserver(errorObserver);

        publisher.publishEvent(new DataEvent("Sample Data"));
        publisher.publishEvent(new ErrorEvent("Sample Error"));

        publisher.removeObserver(dataObserver);
        publisher.removeObserver(errorObserver);

        publisher.executor.shutdown(); // shutdown the executor
    }
}
```