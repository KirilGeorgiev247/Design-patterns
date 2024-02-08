# Command

**Turns a request into an object. This allows clients to be set up with various requests, to put requests in a queue or record them, and to make operations that can be undone**

+ commands are an object-oriented replacement for callbacks

+ support undo

+ support logging changes, giving them the possibility to be reapplied

+ real - world analogy is a customer giving his order to the waiter who gives the order to the chef and the chef cooks(executes) the order

+ must be a small class, which don't actually knows how to execute the concrete request

+ commands can be gathered in a macro command, which can execute all of them at once

+ useful when many objects execute the same thing (example is graphic objects being drawn or documents being opened, closed, copied, ...)

+ installation wizards ask the user to choose from many steps, which are executed at the end when finish is clicked,

 -> when we cancel or click uninstall, undo mechanism is invoked (like transactions)

+ collaborates well with Memento and Prototype (examples 3 and 4)

+ combination of Command and Memento is needed is some situations(examples 5 and 6)

*pros*

+ commands are first - class objects. They can be manipulated and extended like any other object

+ command decouples the object that invokes the operation from the one that knows how to perform it

+ it's easy to add new Commands

+ undo mechanism with small cost of memory (but the operation must be reversible)

*cons*

+ potential for numerous command classes

# Example 01

```java
public interface TextFileOperation {
    String execute();
}

// concrete commands
public class OpenTextFileOperation implements TextFileOperation {
    private TextFile textFile;

    public OpenTextFileOperation(TextFile textFile) {
        this.textFile = textFile;
    }

    @Override
        public String execute() {
        return textFile.open();
    }
}

public class SaveTextFileOperation implements TextFileOperation {
    private TextFile textFile;

    public SaveTextFileOperation(TextFile textFile) {
        this.textFile = textFile;
    }

    @Override
        public String execute() {
        return textFile.save();
    }
}

// Similar implementation for WriteTextFileOperation

// receiver
public class TextFile {
    private String name;

    public TextFile(String name) {
        this.name = name;
    }

    public String open() {
        return "Opening file " + name;
    }

    public String save() {
        return "Saving file " + name;
    }

    // Method for write operation
}

// invoker
public class TextFileOperationExecutor {
    public String executeOperation(TextFileOperation textFileOperation) {
        return textFileOperation.execute();
    }
}

public class CommandPatternDemo {
    public static void main(String[] args) {
        TextFile file = new TextFile("example.txt");

        TextFileOperation openOp = new OpenTextFileOperation(file);
        TextFileOperation saveOp = new SaveTextFileOperation(file);

        TextFileOperationExecutor executor = new TextFileOperationExecutor();

        System.out.println(executor.executeOperation(openOp));
        System.out.println(executor.executeOperation(saveOp));
    }
}
```

# Example 02 

```c++
class Command {
public:
    virtual void execute() = 0;
    virtual ~Command() {}
};

// concrete commands
class LightOnCommand : public Command {
public:
    void execute() override {
        std::cout << "Light is on" << std::endl;
    }
};

class ThermostatUpCommand : public Command {
public:
    void execute() override {
        std::cout << "Thermostat set to higher temperature" << std::endl;
    }
};

// Similar implementation for LightOffCommand, ThermostatDownCommand

// invoker
class RemoteControl {
private:
    Command* button;

public:
    void setCommand(Command* command) {
        button = command;
    }

    void pressButton() {
        button->execute();
    }
};

int main() {
    RemoteControl remote;
    LightOnCommand lightOn;
    ThermostatUpCommand thermostatUp;

    remote.setCommand(&lightOn);
    remote.pressButton();

    remote.setCommand(&thermostatUp);
    remote.pressButton();

    return 0;
}
```

# Example 03 (with Memento and Prototype)

```java
// memento components
public class Canvas {
    private String content;

    public void addContent(String newContent) {
        content += newContent;
    }

    public CanvasMemento save() {
        return new CanvasMemento(content);
    }

    public void restore(CanvasMemento memento) {
        content = memento.getContent();
    }

    public static class CanvasMemento {
        private final String content;

        private CanvasMemento(String content) {
            this.content = content;
        }

        private String getContent() {
            return content;
        }
    }
}

public class CanvasCaretaker {
    private final Stack<Canvas.CanvasMemento> history = new Stack<>();

    public void save(Canvas canvas) {
        history.push(canvas.save());
    }

    public void undo(Canvas canvas) {
        if (!history.isEmpty()) {
            canvas.restore(history.pop());
        }
    }
}

// prototype components
public interface Shape extends Cloneable {
    void draw();
    Shape clone() throws CloneNotSupportedException;
}

public class Circle implements Shape {
    public void draw() {
        System.out.println("Draw Circle");
    }

    @Override
        public Circle clone() throws CloneNotSupportedException {
        return (Circle)super.clone();
    }
}

// Similar implementations for Rectangle, Triangle...

// command pattern components
public interface Command {
    void execute();
}

public class DrawCommand implements Command {
    private Shape shape;

    public DrawCommand(Shape shape) {
        this.shape = shape;
    }

    @Override
        public void execute() {
        shape.draw();
    }
}


public class DrawingApp {
    public static void main(String[] args) throws CloneNotSupportedException {
        Canvas canvas = new Canvas();
        CanvasCaretaker caretaker = new CanvasCaretaker();

        Shape circle = new Circle();
        Command drawCircle = new DrawCommand(circle.clone());

        caretaker.save(canvas); // Saving state before drawing
        drawCircle.execute();   // Drawing a circle

        // More drawing commands and undo operations
    }
}
```

# Example 04 (with Memento and Prototype)

```c++
// memento components
class DocumentState {
public:
    std::string content;

    DocumentState(const std::string& content) : content(content) {}
};

class Document {
public:
    std::string content;

    DocumentState createMemento() {
        return DocumentState(content);
    }

    void restore(const DocumentState& memento) {
        content = memento.content;
    }
};

// prototype components
class DocumentElement {
public:
    virtual void addContent(const std::string& extraContent) = 0;
    virtual DocumentElement* clone() = 0;
    virtual ~DocumentElement() {}
};

class TextElement : public DocumentElement {
    std::string content;

public:
    void addContent(const std::string& extraContent) override {
        content += extraContent;
    }

    TextElement* clone() override {
        return new TextElement(*this);
    }
};

// command components
class Command {
public:
    virtual void execute() = 0;
    virtual ~Command() {}
};

class EditCommand : public Command {
    Document& doc;
    DocumentElement* element;

public:
    EditCommand(Document& doc, DocumentElement* element) : doc(doc), element(element) {}

    void execute() override {
        doc.content += element->clone()->content;
    }
};

int main() {
    Document doc;
    TextElement* text = new TextElement();
    text->addContent("Hello, world!");

    EditCommand editCommand(doc, text);
    editCommand.execute();

    delete text;
    return 0;
}
```

# Example 05 (combining with Memento)

```java
// Command Interface
public interface Command {
    void execute();
    void undo();
}

// Concrete Command for Inserting Text
public class InsertTextCommand implements Command {
    // implementation details
}

// Concrete Command for Deleting Text
public class DeleteTextCommand implements Command {
    // implementation details
}

// Concrete Command for Formatting Text
public class FormatTextCommand implements Command {
    // implementation details
}

// Memento Class
public class TextState {
    private String text;

    public TextState(String text) {
        this.text = text;
    }

    // Getter and Setter
}

// TextEditor Class
public class TextEditor {
    private String text;

    public TextState save() {
        return new TextState(text);
    }

    public void restore(TextState state) {
        this.text = state.getText();
    }

    // Methods to modify text
}

// History Manager
public class EditorHistory {
    private Stack<Command> commands = new Stack<>();
    private Stack<TextState> states = new Stack<>();

    public void executeCommand(Command command, TextEditor editor) {
        commands.push(command);
        states.push(editor.save());
        command.execute();
    }

    public void undo(TextEditor editor) {
        if (!commands.isEmpty()) {
            Command command = commands.pop();
            command.undo();
            editor.restore(states.pop());
        }
    }
}
```

# Example 06 (combining with Memento)

```c++
// Command Class
class Command {
public:
    virtual void execute() = 0;
    virtual void undo() = 0;
    virtual ~Command() {}
};

// Concrete Command for Moving an Object
class MoveCommand : public Command {
    // implementation details
};

// Concrete Command for Transforming an Object
class TransformCommand : public Command {
    // implementation details
};

// Memento Class
class ObjectState {
    // State details
};

// GraphicObject Class
class GraphicObject {
public:
    ObjectState saveState() {
        // Save current state
    }

    void restoreState(const ObjectState& state) {
        // Restore state
    }

    // Methods to manipulate the object
};

// History Manager
class HistoryManager {
    std::stack<Command*> commands;
    std::stack<ObjectState> states;

public:
    void executeCommand(Command* command, GraphicObject& object) {
        commands.push(command);
        states.push(object.saveState());
        command->execute();
    }

    void undo(GraphicObject& object) {
        if (!commands.empty()) {
            Command* command = commands.top();
            commands.pop();
            command->undo();
            object.restoreState(states.top());
            states.pop();
            delete command;  // Assuming ownership is transferred
        }
    }
};
```
