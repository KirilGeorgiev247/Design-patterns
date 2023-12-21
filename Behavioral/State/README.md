# State

**Allowing altering of object's behaviour when its internal state is changed**

+ state can be changed, but states should know about the state machine

+ example is audio player (when pause button is clicked state is changed and music is not playing and the opposite)

+ difference between state and strategy is that strategies don't know about each other, when states should know about each other

+ collaborating with Flyweight (examples 03 and 04)

*pros*

+ it separates the state - related behaviors into distinct classes, promoting cleaner, more organized code (Single Responsibility)

+ removes complex conditional statements by encapsulating the behavior within state objects

+ adding new states and behaviors is easy (Open - Closed)

+ logic for transitioning between states can be centralized in one place

+ objects can change their behavior at runtime depending on their internal state

*cons*

+ overkill when a state machine has only a few states or rarely changes

+ state objects usually need to use the main object they belong to for their tasks. This can result in a close link between the main object and its various states

+ complicated relationships between states

+ in c++ for example we can execute a code on a deleted state

# Example 01

```java
interface TrafficLightState {
    void change(TrafficLight light);
}

class GreenState implements TrafficLightState {
    public void change(TrafficLight light) {
        System.out.println("Green light - Go!");
        light.setState(new YellowState());
    }
}

class YellowState implements TrafficLightState {
    public void change(TrafficLight light) {
        System.out.println("Yellow light - Slow down!");
        light.setState(new RedState());
    }
}

class RedState implements TrafficLightState {
    public void change(TrafficLight light) {
        System.out.println("Red light - Stop!");
        light.setState(new GreenState());
    }
}

 // Context Class
class TrafficLight {
    private TrafficLightState state;

    public TrafficLight(TrafficLightState state) {
        this.state = state;
    }

    public void setState(TrafficLightState state) {
        this.state = state;
    }

    public void change() {
        state.change(this);
    }
}

public class StatePatternDemo {
    public static void main(String[] args) {
        TrafficLight light = new TrafficLight(new GreenState());

        for (int i = 0; i < 6; i++) {
            light.change();
            try {
                Thread.sleep(2000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
```

# Example 02

```c++
class VendingMachine;

class State {
public:
    virtual void handle(VendingMachine* machine) = 0;
    virtual ~State() {}
};

class ReadyState : public State {
    void handle(VendingMachine* machine) override;
};

class DispensingState : public State {
    void handle(VendingMachine* machine) override;
};

class OutOfStockState : public State {
    void handle(VendingMachine* machine) override;
};

 // Context Class
class VendingMachine {
private:
    State* currentState;
    int stock;

public:
    VendingMachine(int stock) : stock(stock) {
        currentState = new ReadyState();
    }

    void setState(State* state) {
        delete currentState;
        currentState = state;
    }

    void requestProduct() {
        currentState->handle(this);
    }

    void refill(int count) {
        stock += count;
        setState(new ReadyState());
    }

    void dispense() {
        if (stock > 0) {
            stock--;
            setState(new DispensingState());
        }
        else {
            setState(new OutOfStockState());
        }
    }

    int getStock() const {
        return stock;
    }

    ~VendingMachine() {
        delete currentState;
    }
};

void ReadyState::handle(VendingMachine* machine) {
    std::cout << "Ready to take an order." << std::endl;
    machine->dispense();
}

void DispensingState::handle(VendingMachine* machine) {
    std::cout << "Dispensing the product." << std::endl;
    if (machine->getStock() > 0) {
        machine->setState(new ReadyState());
    }
    else {
        machine->setState(new OutOfStockState());
    }
}

void OutOfStockState::handle(VendingMachine* machine) {
    std::cout << "Machine is out of stock." << std::endl;
}

int main() {
    VendingMachine machine(2);

    machine.requestProduct();
    machine.requestProduct();
    machine.requestProduct();

    machine.refill(2);
    machine.requestProduct();

    return 0;
}
```

# Example 03 (with Flyweight)

```java
 // State Interface and Concrete States for Formatting
interface TextState {
    void write(String text);
}

class BoldText implements TextState {
    public void write(String text) {
        System.out.println("Bold: " + text);
    }
}

class ItalicText implements TextState {
    public void write(String text) {
        System.out.println("Italic: " + text);
    }
}

 // Flyweight Factory for Creating Shared State Objects
class TextStateFactory {
    private static final Map<String, TextState> states = new HashMap<>();

    public static TextState getState(String key) {
        TextState state = states.get(key);

        if (state == null) {
            switch (key) {
            case "bold":
                state = new BoldText();
                break;
            case "italic":
                state = new ItalicText();
                break;
            default:
                throw new IllegalArgumentException("Unknown state: " + key);
            }
            states.put(key, state);
        }
        return state;
    }
}

class Character {
    private char value;
    private TextState state;

    public Character(char value, TextState state) {
        this.value = value;
        this.state = state;
    }

    public void write() {
        state.write(String.valueOf(value));
    }
}

public class TextEditor {
    public static void main(String[] args) {
        TextState boldState = TextStateFactory.getState("bold");
        TextState italicState = TextStateFactory.getState("italic");

        Character c1 = new Character('a', boldState);
        Character c2 = new Character('b', italicState);

        c1.write();
        c2.write();
    }
}
```

# Example 04 (with Flyweight)

```c++
 // State Interface and Concrete States for Unit Behavior
class UnitState {
public:
    virtual void handle() = 0;
    virtual ~UnitState() {}
};

class AttackingState : public UnitState {
    void handle() override {
        std::cout << "Unit is attacking." << std::endl;
    }
};

class DefendingState : public UnitState {
    void handle() override {
        std::cout << "Unit is defending." << std::endl;
    }
};

 // Flyweight Factory for State Objects
class StateFactory {
private:
    std::unordered_map<std::string, UnitState*> states;

public:
    UnitState* getState(const std::string& stateType) {
        if (states.find(stateType) == states.end()) {
            if (stateType == "attacking") {
                states[stateType] = new AttackingState();
            }
            else if (stateType == "defending") {
                states[stateType] = new DefendingState();
            }
        }
        return states[stateType];
    }

    ~StateFactory() {
        for (auto& state : states) {
            delete state.second;
        }
    }
};

class GameUnit {
private:
    UnitState* state;

public:
    GameUnit(UnitState* state) : state(state) {}

    void handle() {
        state->handle();
    }
};

int main() {
    StateFactory factory;

    UnitState* attacking = factory.getState("attacking");
    UnitState* defending = factory.getState("defending");

    GameUnit soldier1(attacking);
    GameUnit soldier2(defending);

    soldier1.handle();
    soldier2.handle();

    return 0;
}
```