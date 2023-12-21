# Memento

**uUsed to capture and externalize an object's internal state so that the object can be restored to this state later**

+ useful in scenarios where you need to provide an undo mechanism or keep a history of an object's states

+ also knows as Token

+ collaborating well with the Command design pattern (examples 3 and 4)

*pros*

+ saves the internal state of an object without exposing its internal structure

+ history management

+ undo mechanism

*cons*

+ memory usage

+ even though the pattern aims to preserve encapsulation, it can still lead to inadvertent exposure of state details if not implemented carefully

+ most dynamic languages cannot guarantee that the state within the memento stays untouched(Javascript, Python, ...)

+ caretakers should track the originator's lifecycle to be able to destroy obsolute mementos

# Example 01

```java
// Memento
class EditorMemento {
    private final String state;

    public EditorMemento(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}

// Originator
class TextEditor {
    private StringBuilder currentText;

    public TextEditor() {
        this.currentText = new StringBuilder();
    }

    public void addText(String text) {
        currentText.append(text);
    }

    public String getText() {
        return currentText.toString();
    }

    public EditorMemento save() {
        return new EditorMemento(currentText.toString());
    }

    public void restore(EditorMemento memento) {
        this.currentText = new StringBuilder(memento.getState());
    }
}

// Caretaker
class UndoManager {
    private Stack<EditorMemento> history = new Stack<>();

    public void save(TextEditor editor) {
        history.push(editor.save());
    }

    public void undo(TextEditor editor) {
        if (!history.isEmpty()) {
            editor.restore(history.pop());
        }
    }
}

public class Main {
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        UndoManager undoManager = new UndoManager();

        editor.addText("Hello");
        undoManager.save(editor);

        editor.addText(" World");
        undoManager.save(editor);

        System.out.println(editor.getText()); // Output: Hello World

        undoManager.undo(editor);
        System.out.println(editor.getText()); // Output: Hello
    }
}
```

# Example 02

```c++
// Memento
class GameState {
public:
    int health;
    int level;

    GameState(int health, int level) : health(health), level(level) {}
};

// Originator
class GameCharacter {
public:
    int health;
    int level;

    GameCharacter(int health, int level) : health(health), level(level) {}

    std::shared_ptr<GameState> save() {
        return std::make_shared<GameState>(health, level);
    }

    void restore(std::shared_ptr<GameState> memento) {
        health = memento->health;
        level = memento->level;
    }
};

// Caretaker
class GameCaretaker {
private:
    std::vector<std::shared_ptr<GameState>> history;

public:
    void saveState(const GameCharacter& character) {
        history.push_back(character.save());
    }

    void restoreState(GameCharacter& character, size_t index) {
        if (index < history.size()) {
            character.restore(history[index]);
        }
    }
};

int main() {
    GameCharacter hero(100, 1);
    GameCaretaker caretaker;

    caretaker.saveState(hero); // Save initial state

    // Game progresses
    hero.health -= 30;
    hero.level += 1;
    caretaker.saveState(hero); // Save new state

    // Restore to initial state
    caretaker.restoreState(hero, 0);
    std::cout << "Health: " << hero.health << ", Level: " << hero.level << std::endl; // Output: Health: 100, Level: 1

    return 0;
}
```

# Example 03 (with Command)

```java
// Memento
class DrawingSnapshot {
    private final List<String> shapes;

    public DrawingSnapshot(List<String> shapes) {
        this.shapes = new ArrayList<>(shapes); // deep copy
    }

    public List<String> getShapes() {
        return shapes;
    }
}

// Originator
class Drawing {
    private List<String> shapes = new ArrayList<>();

    public void addShape(String shape) {
        shapes.add(shape);
    }

    public DrawingSnapshot save() {
        return new DrawingSnapshot(shapes);
    }

    public void restore(DrawingSnapshot snapshot) {
        shapes = snapshot.getShapes();
    }

    public void display() {
        System.out.println("Drawing: " + shapes);
    }
}

// Command
interface Command {
    void execute();
    void undo();
}

class DrawCommand implements Command {
    private final Drawing drawing;
    private final String shape;
    private DrawingSnapshot snapshot;

    public DrawCommand(Drawing drawing, String shape) {
        this.drawing = drawing;
        this.shape = shape;
    }

    @Override
    public void execute() {
        snapshot = drawing.save();
        drawing.addShape(shape);
        drawing.display();
    }

    @Override
    public void undo() {
        drawing.restore(snapshot);
        drawing.display();
    }
}

public class DrawingApp {
    public static void main(String[] args) {
        Drawing drawing = new Drawing();
        Command drawCircle = new DrawCommand(drawing, "Circle");
        Command drawSquare = new DrawCommand(drawing, "Square");

        drawCircle.execute(); // Draw a circle
        drawSquare.execute(); // Draw a square

        drawSquare.undo(); // Undo square
        drawCircle.undo(); // Undo circle
    }
}
```

# Example 04 (with Command)

```c++
// Memento
class CalculatorMemento {
public:
    int value;

    CalculatorMemento(int value) : value(value) {}
};

// Originator
class Calculator {
public:
    int currentValue = 0;

    shared_ptr<CalculatorMemento> save() {
        return make_shared<CalculatorMemento>(currentValue);
    }

    void restore(shared_ptr<CalculatorMemento> memento) {
        currentValue = memento->value;
    }

    void display() {
       cout << "Current Value: " << currentValue << endl;
    }
};

// Command
class Command {
public:
    virtual void execute() = 0;
    virtual void undo() = 0;
    virtual ~Command() {}
};

class AddCommand : public Command {
private:
    Calculator& calculator;
    int value;
    shared_ptr<CalculatorMemento> memento;

public:
    AddCommand(Calculator& calculator, int value) : calculator(calculator), value(value) {}

    void execute() override {
        memento = calculator.save();
        calculator.currentValue += value;
        calculator.display();
    }

    void undo() override {
        calculator.restore(memento);
        calculator.display();
    }
};

int main() {
    Calculator calculator;
    AddCommand addFive(calculator, 5);

    addFive.execute(); // Add 5
    addFive.undo();    // Undo addition

    return 0;
}
```