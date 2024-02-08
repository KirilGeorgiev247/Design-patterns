# Composite

**Allows you to arrange objects into a tree - like structure to show how they're part of a larger whole. It makes it possible to treat single objects and groups of objects in the same way**

+ when you want to represent part - whole hierarchies of objects

+ when you want clients to be able to ignore the difference between compositions of objects and individual objects

+ example is a directory tree (files and folders are treated the same)

+ often used with Decorator design pattern (examples 3 and 4)

*pros*

+ simplifies client code, as it can treat composite structures and individual objects uniformly

+ clients can use polymorphism to interact with all objects in the composite structure uniformly

+ supports adding new kinds of components easily since the client code works with the component interface

*cons*

+ not all objects in a hierarchy might need to have common operations or share interfaces, leading to overgeneralization and design complexity by requiring all components to follow the same interface, even if they don't fit nearly into the pattern

+ traversing composite structures can lead to performance issues if the hierarchy is deep or complex

***

# Example 01

```java
// Component
interface Graphic {
    void draw();
}

class Circle implements Graphic {
    public void draw() {
        System.out.println("Drawing a circle");
    }
}

// Leaf
class Square implements Graphic {
    public void draw() {
        System.out.println("Drawing a square");
    }
}

// Composite
class CompositeGraphic implements Graphic {
    private List<Graphic> childGraphics = new ArrayList<>();

    public void add(Graphic graphic) {
        childGraphics.add(graphic);
    }

    public void draw() {
        for (Graphic graphic : childGraphics) {
            graphic.draw();
        }
    }
}

public class CompositeDemo {
    public static void main(String[] args) {
        Circle circle = new Circle();
        Square square = new Square();

        CompositeGraphic graphic = new CompositeGraphic();
        graphic.add(circle);
        graphic.add(square);

        graphic.draw(); // Draws both the circle and square
    }
}
```

# Example 02

```c++
// Component
class Graphic {
public:
    virtual void draw() = 0;
    virtual ~Graphic() {}
};

// Leaf
class Circle : public Graphic {
public:
    void draw() override {
        cout << "Drawing a circle" << endl;
    }
};

// Leaf
class Square : public Graphic {
public:
    void draw() override {
        cout << "Drawing a square" << endl;
    }
};

// Composite
class CompositeGraphic : public Graphic {
private:
    vector<Graphic*> childGraphics;

public:
    void add(Graphic* graphic) {
        childGraphics.push_back(graphic);
    }

    void draw() override {
        for (Graphic* graphic : childGraphics) {
            graphic->draw();
        }
    }

    ~CompositeGraphic() {
        for (Graphic* graphic : childGraphics) {
            delete graphic;
        }
    }
};

int main() {
    Circle* circle = new Circle();
    Square* square = new Square();

    CompositeGraphic graphic;
    graphic.add(circle);
    graphic.add(square);

    graphic.draw(); // Draws both the circle and square

    return 0;
}
```

# Example 03 (with Decorator)

```java
// Component
interface Graphic {
    void draw();
}

// Leaf
class Circle implements Graphic {
    public void draw() {
        System.out.println("Drawing a circle");
    }
}

// Composite
class CompositeGraphic implements Graphic {
    private List<Graphic> childGraphics = new ArrayList<>();

    public void add(Graphic graphic) {
        childGraphics.add(graphic);
    }

    public void draw() {
        for (Graphic graphic : childGraphics) {
            graphic.draw();
        }
    }
}

// Decorator
abstract class GraphicDecorator implements Graphic {
    protected Graphic decoratedGraphic;

    public GraphicDecorator(Graphic decoratedGraphic) {
        this.decoratedGraphic = decoratedGraphic;
    }

    public void draw() {
        decoratedGraphic.draw();
    }
}

// Concrete Decorator
class ColoredGraphic extends GraphicDecorator {
    private String color;

    public ColoredGraphic(Graphic decoratedGraphic, String color) {
        super(decoratedGraphic);
        this.color = color;
    }

    public void draw() {
        decoratedGraphic.draw();
        System.out.println("Color: " + color);
    }
}

public class DecoratorCompositeDemo {
    public static void main(String[] args) {
        Graphic circle = new Circle();
        Graphic redCircle = new ColoredGraphic(circle, "Red");

        CompositeGraphic graphics = new CompositeGraphic();
        graphics.add(redCircle);

        graphics.draw(); // Draws a circle with red color
    }
}
```

# Example 04 (with Decorator)

```c++
// Component
class Graphic {
public:
    virtual void draw() = 0;
    virtual ~Graphic() {}
};

// Leaf
class Circle : public Graphic {
public:
    void draw() override {
        cout << "Drawing a circle" << endl;
    }
};

// Composite
class CompositeGraphic : public Graphic {
private:
    vector<Graphic*> childGraphics;

public:
    void add(Graphic* graphic) {
        childGraphics.push_back(graphic);
    }

    void draw() override {
        for (Graphic* graphic : childGraphics) {
            graphic->draw();
        }
    }

    ~CompositeGraphic() {
        for (Graphic* graphic : childGraphics) {
            delete graphic;
        }
    }
};

// Decorator
class GraphicDecorator : public Graphic {
protected:
    Graphic* decoratedGraphic;

public:
    GraphicDecorator(Graphic* decoratedGraphic) : decoratedGraphic(decoratedGraphic) {}

    void draw() override {
        decoratedGraphic->draw();
    }
};

// Concrete Decorator
class ColoredGraphic : public GraphicDecorator {
private:
    string color;

public:
    ColoredGraphic(Graphic* decoratedGraphic, string color)
        : GraphicDecorator(decoratedGraphic), color(color) {}

    void draw() override {
        decoratedGraphic->draw();
        cout << "Color: " << color << endl;
    }
};

int main() {
    Graphic* circle = new Circle();
    Graphic* redCircle = new ColoredGraphic(circle, "Red");

    CompositeGraphic graphics;
    graphics.add(redCircle);

    graphics.draw(); // Draws a circle with red color

    delete redCircle;
}
```
