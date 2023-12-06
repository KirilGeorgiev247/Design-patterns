# Strategy

**Enables selecting an algorithm's behavior at runtime**

+ example is google maps navigation to be by foot, car, train or bus

+ abstraction of used algorithm behaviors depending on the context

+ context is being set with dependency injection

+ each concrete strategy provides the implementation for an algorithm

+ the context gives the data to the strategy

+  push and pull implementations: 

 -> push - context knows for the strategy

 -> pull - strategy knows for the context

+ in most cases default strategy is a must depending on the logic

 -> unique_ptr for example uses this pattern and has default deleter because the client is not obligated to set the deleter

+ dependency inversion principle 

 -> the context (a high-level module) does not depend on the concrete strategies (low-level modules)

+ can be used with Bridge and Dependency Injection (example 03 and 04)

*pros*

+ allows for easy swapping and reusability of different behaviors or algorithms

+ new strategies can be introduced without changing the context

+ each strategy can be tested independently from the context and other strategies

+ decoupling the implementation details of an algorithm from the code that uses it

+ it helps in avoiding complex conditional logic in choosing algorithms

*cons*

+ increased number of objects -> each new strategy requires a new class

+ overused complexity -> complicates the code too much in a simple scenarios where it might be overkill

+ communication overhead between strategies and context, especially if there are lots of strategies

+ debug complexity -> strategies communicate indirectly with the rest of the application through the context(tracing the flow of the application could be more challenging)

# Example 01

```java
public interface SortingStrategy {
    void sort(List<Integer> list);
}

// Bubble Sort Strategy
public class BubbleSortStrategy implements SortingStrategy {
    public void sort(List<Integer> list) {
        // Implementation of bubble sort
        System.out.println("Sorting using bubble sort");
        // Sorting logic...
    }
}

// Quick Sort Strategy
public class QuickSortStrategy implements SortingStrategy {
    public void sort(List<Integer> list) {
        // Implementation of quick sort
        System.out.println("Sorting using quick sort");
        // Sorting logic...
    }
}

public class Sorter {
    private SortingStrategy strategy;

    public Sorter(SortingStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(SortingStrategy strategy) {
        this.strategy = strategy;
    }

    public void sort(List<Integer> list) {
        strategy.sort(list);
    }
}

public class StrategyPatternDemo {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(5, 2, 9, 1, 5, 6);

        Sorter sorter = new Sorter(new BubbleSortStrategy());
        sorter.sort(list); // Sorts using bubble sort

        sorter.setStrategy(new QuickSortStrategy());
        sorter.sort(list); // Sorts using quick sort
    }
}
```

# Example 02

```c++
class TravelStrategy {
public:
    virtual void travelTo(std::string destination) = 0;
    virtual ~TravelStrategy() {}
};

class CarStrategy : public TravelStrategy {
public:
    void travelTo(std::string destination) override {
        std::cout << "Traveling to " << destination << " by car." << std::endl;
    }
};

class TrainStrategy : public TravelStrategy {
public:
    void travelTo(std::string destination) override {
        std::cout << "Traveling to " << destination << " by train." << std::endl;
    }
};

class TravelContext {
private:
    TravelStrategy* strategy;
public:
    void setStrategy(TravelStrategy* strategy) {
        this->strategy = strategy;
    }

    void travelTo(std::string destination) {
        if (strategy) {
            strategy->travelTo(destination);
        }
        else {
            std::cout << "Travel strategy not set." << std::endl;
        }
    }
};

int main() {
    TravelContext context;
    CarStrategy car;
    TrainStrategy train;

    context.setStrategy(&car);
    context.travelTo("Paris"); // Traveling by car

    context.setStrategy(&train);
    context.travelTo("Berlin"); // Traveling by train

    return 0;
}
```

# Example 03 (with Bridge and Dependency Injection)

```java
public interface PaymentStrategy {
    void pay(int amount);
}

public class CreditCardStrategy implements PaymentStrategy {
    private String cardNumber;

    public CreditCardStrategy(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
        public void pay(int amount) {
        System.out.println(amount + " paid with credit card.");
    }
}

public class PayPalStrategy implements PaymentStrategy {
    private String emailId;

    public PayPalStrategy(String emailId) {
        this.emailId = emailId;
    }

    @Override
        public void pay(int amount) {
        System.out.println(amount + " paid using PayPal.");
    }
}

// Bridge Implementor
public interface PaymentGateway {
    void processPayment(int amount);
}

// Concrete Implementors
public class StripeGateway implements PaymentGateway {
    public void processPayment(int amount) {
        System.out.println("Processing payment through Stripe: " + amount);
    }
}

public class PayPalGateway implements PaymentGateway {
    public void processPayment(int amount) {
        System.out.println("Processing payment through PayPal: " + amount);
    }
}

// Bridge Abstraction
public abstract class Payment {
    protected PaymentGateway paymentGateway;

    public Payment(PaymentGateway gateway) {
        this.paymentGateway = gateway;
    }

    public abstract void makePayment(int amount);
}

// Refined Abstraction
public class SecurePayment extends Payment {
    private PaymentStrategy strategy;

    public SecurePayment(PaymentStrategy strategy, PaymentGateway gateway) {
        super(gateway);
        this.strategy = strategy;
    }

    public void makePayment(int amount) {
        strategy.pay(amount);
        paymentGateway.processPayment(amount);
    }
}

public class PaymentService {
    private SecurePayment payment;

    public PaymentService(SecurePayment payment) {
        this.payment = payment;
    }

    public void processPayment(int amount) {
        payment.makePayment(amount);
    }

    public static void main(String[] args) {
        PaymentStrategy cardStrategy = new CreditCardStrategy("1234567890");
        PaymentGateway stripeGateway = new StripeGateway();

        SecurePayment payment = new SecurePayment(cardStrategy, stripeGateway);
        PaymentService service = new PaymentService(payment);

        service.processPayment(1000);
    }
}
```

# Example 04 (with Bridge and Dependency Injection)

```c++
class LogStrategy {
public:
    virtual void logMessage(const std::string& message) = 0;
    virtual ~LogStrategy() {}
};

class ConsoleLogStrategy : public LogStrategy {
public:
    void logMessage(const std::string& message) override {
        std::cout << "Console Log: " << message << std::endl;
    }
};

class FileLogStrategy : public LogStrategy {
public:
    void logMessage(const std::string& message) override {
        std::ofstream file("log.txt", std::ios::app);
        file << "File Log: " << message << std::endl;
        file.close();
    }
};

// Bridge Implementor
class OutputMedium {
public:
    virtual void send(const std::string& message) = 0;
    virtual ~OutputMedium() {}
};

// Concrete Implementors
class Screen : public OutputMedium {
public:
    void send(const std::string& message) override {
        std::cout << message;
    }
};

class Printer : public OutputMedium {
public:
    void send(const std::string& message) override {
        // Code to send message to printer
    }
};

// Bridge Abstraction
class Logger {
protected:
    OutputMedium* output;
    LogStrategy* strategy;

public:
    Logger(OutputMedium* medium, LogStrategy* strategy)
        : output(medium), strategy(strategy) {}

    void log(const std::string& message) {
        std::string formattedMessage = "Log: " + message;
        strategy->logMessage(formattedMessage);
        output->send(formattedMessage);
    }

    virtual ~Logger() {
        delete output;
        delete strategy;
    }
};

int main() {
    OutputMedium* screen = new Screen();
    LogStrategy* consoleStrategy = new ConsoleLogStrategy();

    Logger* logger = new Logger(screen, consoleStrategy);
    logger->log("Hello, world!");

    delete logger;

    return 0;
}
```