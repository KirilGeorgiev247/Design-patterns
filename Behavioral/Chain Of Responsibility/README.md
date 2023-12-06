# Chain Of Responsibility

**Avoid coupling the sender of a request to its receiver by giving more than one object a chance to handle the request**

+ differences between decorator and this design pattern are:

 -> that in decorator we go through every step, when some of the handlers in the chain can throw or do something else ruining the flow (handler can change the chain)

 -> linear construction (decorator) vs tree construction (chain of responsibility)

+ collaborating with Composite design pattern (example 03 and 04)

*pros*

+ decouples the sender of a request from its receivers by allowing multiple objects to handle the request without knowing which one will process it

+ handlers in the chain can be dynamically changed or rearranged in runtime

+ each handler is processing for its own part (Single Responsibility)

+ handlers are independant of each other

+ separates the code in smaller parts

*cons*

+ passing the request along a long chain of handlers can produce performance issues

+ risk of request not being completely handled at the end of the chain

+ a request may be handled multiple times when chain is not well - designed

+ handling a very long sequence of steps can become tricky, especially if the order or setup of these steps changes often

# Example 01

```java
public interface HelpHandler {
    void handleHelp();
}

public class Application implements HelpHandler {
    public void handleHelp() {
        System.out.println("Application: Displaying general help.");
    }
}

public abstract class Widget implements HelpHandler {
    private HelpHandler parent;

    public Widget(HelpHandler handler) {
        this.parent = handler;
    }

    public void handleHelp() {
        if (parent != null) {
            parent.handleHelp();
        }
    }
}

public class Dialog extends Widget {
    public Dialog(HelpHandler handler) {
        super(handler);
    }

    public void handleHelp() {
        System.out.println("Dialog: Displaying dialog help.");
    }
}

public class Button extends Widget {
    public Button(HelpHandler handler) {
        super(handler);
    }

    public void handleHelp() {
        System.out.println("Button: Users can click me to perform an action.");
        super.handleHelp();
    }
}

public class Main {
    public static void main(String[] args) {
        HelpHandler app = new Application();
        HelpHandler dialog = new Dialog(app);
        HelpHandler button = new Button(dialog);

        // This will trigger the chain for handling help
        button.handleHelp(); 
    }
}
```

# Example 02

```c++
class Handler {
public:
    virtual void setNext(Handler* handler) = 0;
    virtual void handleRequest(const std::string& request) = 0;
    virtual ~Handler() {}
};

class BaseHandler : public Handler {
protected:
    Handler* nextHandler;

public:
    BaseHandler() : nextHandler(nullptr) {}

    void setNext(Handler* handler) override {
        nextHandler = handler;
    }

    void handleRequest(const std::string& request) override {
        if (nextHandler != nullptr) {
            nextHandler->handleRequest(request);
        }
    }
};

class HandlerA : public BaseHandler {
public:
    void handleRequest(const std::string& request) override {
        if (canHandle(request)) {
            std::cout << "Handler A is handling request: " << request << std::endl;
        }
        else {
            std::cout << "Handler A passed request to next handler." << std::endl;
            BaseHandler::handleRequest(request);
        }
    }

private:
    bool canHandle(const std::string& request) {
        // Determine if this handler can handle the request
        return request == "A";
    }
};

// HandlerB, HandlerC...

int main() {
    HandlerA* handlerA = new HandlerA();
    HandlerB* handlerB = new HandlerB();
    HandlerC* handlerC = new HandlerC();

    handlerA->setNext(handlerB);
    handlerB->setNext(handlerC);

    handlerA->handleRequest("A"); // Should be handled by HandlerA
    handlerA->handleRequest("B"); // Should be handled by HandlerB
    handlerA->handleRequest("C"); // Should be handled by HandlerC
    handlerA->handleRequest("D"); // Won't be handled by any handler

    delete handlerA;
    delete handlerB;
    delete handlerC;

    return 0;
}
```

# Example 03 (with Composite)

```java
// Component interface
public interface Component {
    void setParent(Component parent);
    void handleEvent(String event);
    void add(Component child);
    void remove(Component child);
}

// Composite class for container components
public abstract class ContainerComponent implements Component {
    protected Component parent;
    protected List<Component> children = new ArrayList<>();

    public void setParent(Component parent) {
        this.parent = parent;
    }

    public void add(Component child) {
        children.add(child);
        child.setParent(this);
    }

    public void remove(Component child) {
        children.remove(child);
    }

    public void handleEvent(String event) {
        children.forEach(child->child.handleEvent(event));
        if (parent != null) {
            parent.handleEvent(event);
        }
    }
}

// Concrete component classes
public class Button implements Component {
    private Component parent;

    public void setParent(Component parent) {
        this.parent = parent;
    }

    public void handleEvent(String event) {
        if ("click".equals(event)) {
            System.out.println("Button handles click event.");
        }
        else if (parent != null) {
            parent.handleEvent(event);
        }
    }

    public void add(Component child) {
        // Not applicable for Button.
    }

    public void remove(Component child) {
        // Not applicable for Button.
    }
}

public class Panel extends ContainerComponent {
    public void handleEvent(String event) {
        if ("mousemove".equals(event)) {
            System.out.println("Panel handles mousemove event.");
        }
        else {
            super.handleEvent(event);
        }
    }
}

public class GUI {
    public static void main(String[] args) {
        Component panel = new Panel();
        Component button = new Button();

        panel.add(button);

        button.handleEvent("click");
        panel.handleEvent("mousemove");
        button.handleEvent("keypress"); // This will be passed to the panel
    }
}
```

# Example 04 (with Composite)

```c++
// Component class
class Component {
public:
    virtual void handleEvent(const std::string& event) = 0;
    virtual void add(Component* component) = 0;
    virtual void remove(Component* component) = 0;
    virtual void setParent(Component* parent) = 0;
    virtual ~Component() {}
};

// Composite class for container components
class ContainerComponent : public Component {
protected:
    std::vector<Component*> children;
    Component* parent;

public:
    ContainerComponent() : parent(nullptr) {}

    void setParent(Component* p) override {
        parent = p;
    }

    void add(Component* component) override {
        children.push_back(component);
        component->setParent(this);
    }

    void remove(Component* component) override {
        children.erase(std::remove(children.begin(), children.end(), component), children.end());
    }

    void handleEvent(const std::string& event) override {
        for (Component* child : children) {
            child->handleEvent(event);
        }
        if (parent) {
            parent->handleEvent(event);
        }
    }
};

// Leaf component
class Button : public Component {
private:
    Component* parent;

public:
    Button() : parent(nullptr) {}

    void setParent(Component* p) override {
        parent = p;
    }

    void handleEvent(const std::string& event) override {
        if (event == "click") {
            std::cout << "Button handles click event." << std::endl;
        }
        else if (parent) {
            parent->handleEvent(event);
        }
    }

    void add(Component* component) override {
        throw std::runtime_error("Not supported");
    }

    void remove(Component* component) override {
        throw std::runtime_error("Not supported");
    }
};

int main() {
    ContainerComponent* window = new ContainerComponent();
    ContainerComponent* panel = new ContainerComponent();
    Component* button = new Button();

    window->add(panel);
    panel->add(button);

    button->handleEvent("click"); // Handled by the button
    button->handleEvent("close"); // Propagated up to the window

    delete window; // In a real scenario, use smart pointers instead of raw pointers
    delete panel;
    delete button;

    return 0;
}
```