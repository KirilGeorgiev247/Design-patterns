# Mediator

**A central authority that handles communication and coordination between different objects in a system**

+ objects communicate with each other through the mediator, instead of interacting directly

+ every object knows only about the mediator

+ real - world analogy is a tower communicating with airplanes about where and when to land

+ absract mediator is needed when we want to change the mediators and when we want the colleagues to know about the mediator

+ colaborates well with Facade (examples 3 and 4)

*pros*

+ promotes loose coupling by keeping objects from referring to each other explicitly, and so lets you have reduced complexity, increased reusability and easier maintenance

+ limits subclassing

+ one - to - many instead of many - to - many

+ having a centralized point of control can make it easier to manage and monitor interactions

+ abstracts how objects cooperate

*cons*

+ can become a god object

+ if the mediator has an issue, it can impact the entire system's communication

+ increased complexity for the mediator class as can become a monolith that's hard to maintain

# Example 01

```java
public interface ChatMediator {
    void sendMessage(String msg, User user);
    void addUser(User user);
}

public class ChatRoom implements ChatMediator {
    private List<User> users;

    public ChatRoom() {
        this.users = new ArrayList<>();
    }

    @Override
        public void addUser(User user) {
        this.users.add(user);
    }

    @Override
        public void sendMessage(String msg, User user) {
        for (User u : this.users) {
            // message should not be received by the user sending it
            if (u != user) {
                u.receive(msg);
            }
        }
    }
}

public abstract class User {
    protected ChatMediator mediator;
    protected String name;

    public User(ChatMediator med, String name) {
        this.mediator = med;
        this.name = name;
    }

    public abstract void send(String msg);
    public abstract void receive(String msg);
}

public class ConcreteUser extends User {
    public ConcreteUser(ChatMediator med, String name) {
        super(med, name);
    }

    @Override
        public void send(String msg) {
        System.out.println(this.name + ": Sending Message=" + msg);
        mediator.sendMessage(msg, this);
    }

    @Override
        public void receive(String msg) {
        System.out.println(this.name + ": Received Message:" + msg);
    }
}

public class MediatorPatternDemo {
    public static void main(String[] args) {
        ChatMediator mediator = new ChatRoom();

        User user1 = new ConcreteUser(mediator, "John");
        User user2 = new ConcreteUser(mediator, "Lisa");
        User user3 = new ConcreteUser(mediator, "Sara");
        User user4 = new ConcreteUser(mediator, "David");

        mediator.addUser(user1);
        mediator.addUser(user2);
        mediator.addUser(user3);
        mediator.addUser(user4);

        user1.send("Hi All");
    }
}
```

# Example 02

```c++
class Airplane;

class ControlTower {
public:
    void registerAirplane(Airplane* airplane) {
        airplanes.push_back(airplane);
    }

    void sendLandingPermission(const std::string& message, Airplane* airplane);

private:
    std::vector<Airplane*> airplanes;
};

class Airplane {
public:
    Airplane(ControlTower* tower, std::string id) : controlTower(tower), airplaneID(id) {
        controlTower->registerAirplane(this);
    }

    void requestLanding() {
        controlTower->sendLandingPermission("Requesting landing permission.", this);
    }

    void receiveMessage(const std::string& message) {
        std::cout << "Airplane " << airplaneID << " received message: " << message << std::endl;
    }

private:
    ControlTower* controlTower;
    std::string airplaneID;
};

void ControlTower::sendLandingPermission(const std::string& message, Airplane* requestingAirplane) {
    for (auto* airplane : airplanes) {
        if (airplane != requestingAirplane) {
            airplane->receiveMessage(message);
        }
    }
}

int main() {
    ControlTower controlTower;

    Airplane airplane1(&controlTower, "AP123");
    Airplane airplane2(&controlTower, "AP456");

    airplane1.requestLanding();

    return 0;
}
```

# Example 03 (with Facade)

```java
public interface SmartHomeMediator {
    void sendCommand(String command, SmartDevice device);
}

public class SmartHomeController implements SmartHomeMediator {
    private Light light;
    private Thermostat thermostat;
    private MusicSystem musicSystem;

    // Setters for devices

    @Override
        public void sendCommand(String command, SmartDevice device) {
        if (device.equals(light)) {
            // Command processing for light
        }
        else if (device.equals(thermostat)) {
            // Command processing for thermostat
        }
        else if (device.equals(musicSystem)) {
            // Command processing for music system
        }
    }
}

public interface SmartDevice {
    void operateDevice(String command, SmartHomeMediator mediator);
}

public class Light implements SmartDevice {
    @Override
        public void operateDevice(String command, SmartHomeMediator mediator) {
        mediator.sendCommand(command, this);
        // Additional operation logic
    }
}

// Similar implementations for Thermostat and MusicSystem

public class SmartHomeFacade {
    private SmartHomeController controller;

    public SmartHomeFacade(SmartHomeController controller) {
        this.controller = controller;
    }

    public void eveningMode() {
        controller.sendCommand("DIM", controller.getLight());
        controller.sendCommand("COOL", controller.getThermostat());
        controller.sendCommand("PLAY_SOFT_MUSIC", controller.getMusicSystem());
    }

    // Other methods like morningMode, awayMode, etc.
}

public class HomeAutomationTest {
    public static void main(String[] args) {
        SmartHomeController controller = new SmartHomeController();
        SmartHomeFacade facade = new SmartHomeFacade(controller);

        facade.eveningMode();
        // Other operations
    }
}
```

# Example 04 (with Facade)

```c++
class TradingComponent;

class TradingMediator {
public:
    virtual void executeTrade(const std::string& trade, TradingComponent* component) = 0;
};

class TradingComponent {
public:
    TradingComponent(TradingMediator* mediator) : mediator(mediator) {}

    virtual void trade(const std::string& trade) {
        mediator->executeTrade(trade, this);
    }

protected:
    TradingMediator* mediator;
};

class StockExchange : public TradingComponent {
    // Implementation specific to stock exchange
};

class CommodityExchange : public TradingComponent {
    // Implementation specific to commodity exchange
};

// Other components like ForexExchange, etc.

class TradingSystemMediator : public TradingMediator {
public:
    virtual void executeTrade(const std::string& trade, TradingComponent* component) override {
        // Logic to execute trade
        std::cout << "Executing trade: " << trade << std::endl;
    }
};

class TradingFacade {
private:
    TradingSystemMediator* mediator;
    StockExchange* stockExchange;
    CommodityExchange* commodityExchange;

public:
    TradingFacade() {
        mediator = new TradingSystemMediator();
        stockExchange = new StockExchange(mediator);
        commodityExchange = new CommodityExchange(mediator);
    }

    void executeStockTrade(const std::string& trade) {
        stockExchange->trade(trade);
    }

    void executeCommodityTrade(const std::string& trade) {
        commodityExchange->trade(trade);
    }

    // Destructor to clean up
};

int main() {
    TradingFacade tradingFacade;

    tradingFacade.executeStockTrade("Buy 100 shares of XYZ");
    tradingFacade.executeCommodityTrade("Sell 50 units of Gold");

    return 0;
}
```