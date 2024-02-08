### Decorator ###

**Attaches additional responsibilities to an object dynamically. Decorators provide a
flexible alternative to subclassing for extending functionality**

+ we can dynamically change the behaviour of a class without changing it

+ we can just detach a class in the hierarhy and everything will be fine

+ if the logic is moving from bottom to top - decorator, otherwise - filter

+ more flexible for the cost of memory

+ ensure dynamically decorating a class by extending it with filters

+ a decorator is different from an adapter in a way that a decorator only
changes an object's responsibilities, not it's interface. An adapter will give an object
a completely new interface

*pros*

+ used to add or remove responsibilities to individual objects dynamically and transparently,
that is, without affecting other objects.

+ when extension by subclassing is impractical. Sometimes a large number of independent extensions are possible and would produce an explosion of subclasses to support every combination

+ new functionalities can be added to objects without altering their structure

+ it supports really good the Single Responsibility Principle

*cons*

+ many steps are being made instead of all at once

+ a decorator acts as a transparent enclosure. But from an object identity point of view, a decorated component is not identical to the component itself

+ a design that uses Decorator often results in systems composed of lots of little objects that all look alike

+ hard to debug

## Example 01 ##

```java
// Component
interface Coffee {
    String getDescription();
    double getCost();
}

// Concrete Component
class SimpleCoffee implements Coffee {
    public String getDescription() {
        return "Simple Coffee";
    }

    public double getCost() {
        return 2.0;
    }
}

// Decorator
abstract class CoffeeDecorator implements Coffee {
    protected final Coffee decoratedCoffee;

    public CoffeeDecorator(Coffee coffee) {
        this.decoratedCoffee = coffee;
    }

    public String getDescription() {
        return decoratedCoffee.getDescription();
    }

    public double getCost() {
        return decoratedCoffee.getCost();
    }
}

// Concrete Decorator
class MilkCoffeeDecorator extends CoffeeDecorator {
    public MilkCoffeeDecorator(Coffee coffee) {
        super(coffee);
    }

    public String getDescription() {
        return super.getDescription() + ", with milk";
    }

    public double getCost() {
        return super.getCost() + 0.5;
    }
}

// Usage
public class DecoratorDemo {
    public static void main(String[] args) {
        Coffee myCoffee = new SimpleCoffee();
        myCoffee = new MilkCoffeeDecorator(myCoffee);

        System.out.println(myCoffee.getDescription() + " costs $" + myCoffee.getCost());
    }
}

```

## Example 02 ##

```c++
#include <iostream>
#include <string>

// Component
class Beverage {
public:
    virtual std::string getDescription() const = 0;
    virtual double cost() const = 0;
    virtual ~Beverage() = default;
};

// Concrete Component
class Coffee : public Beverage {
public:
    std::string getDescription() const override {
        return "Coffee";
    }

    double cost() const override {
        return 2.0;
    }
};

// Decorator
class BeverageDecorator : public Beverage {
protected:
    Beverage* beverage;
public:
    BeverageDecorator(Beverage* b) : beverage(b) {}
};

// Concrete Decorator
class MilkDecorator : public BeverageDecorator {
public:
    MilkDecorator(Beverage* b) : BeverageDecorator(b) {}

    std::string getDescription() const override {
        return beverage->getDescription() + " with Milk";
    }

    double cost() const override {
        return beverage->cost() + 0.5;
    }
};

// Usage
int main() {
    Beverage* myCoffee = new Coffee();
    myCoffee = new MilkDecorator(myCoffee);

    std::cout << "Cost: " << myCoffee->cost() << std::endl;
    std::cout << "Description: " << myCoffee->getDescription() << std::endl;

    delete myCoffee;
    return 0;
}
```

## Example 03 ##

```c++
class Text {
public:
	virtual string getText() const = 0;
	virtual void setText(const string& text) = 0;
	virtual ~Text() {}
	virtual Text* getSource() {
		return this;
	}
};

struct TextLine2 : public Text { ... };

struct TextDecorator : public Text {
	virtual string getText() const = 0 {
		return source->getText();
	}

	virtual void setText(const string& str) = 0 {
		source->setText(str);
	}

	virtual Text* getSource() {
		return source;
	}

	virtual void setSymbol(char ch) = 0;

	// we can pass TextDecorator here and extend, not change
	virtual void setSource(Text* src) {
		source = src;
	}

protected:
	Text* source = nullptr;
};

struct AddQuotesDecorator : public TextDecorator {

	AddQuotesDecorator(Text* source) {
		this->source = source;
	}

	virtual void setSymbol(char ch) { s = ch; }

	virtual string getText() const {
		return string(s, 1) + source->getText() + string(s, 1);
	}

	virtual void setText(const string& str) {
		source->setText(str);
	}

	virtual Text* getSource() {
		return source;
	}

	// we can pass TextDecorator here and extend, not change
	virtual void setSource(Text* src) {
		source = src;
	}

private:
	char s;
};

class TextBox {
public:
	void print() const {
		for (Text* line : text)
		{
			cout << line->getText();
		}
	}

	void setQuotes(int num, char ch) {
		Text* s = text[num];
		AddQuotesDecorator* aqd = nullptr;
		while (s!= s->getSource())
		{
			if (aqd = dynamic_cast<AddQuotesDecorator*>(s))
				break;
			s = s->getSource();
		}

		if (!aqd) aqd = new AddQuotesDecorator(s);

		aqd->setSymbol(ch);

		text[num] = aqd;
	}
private:
	std::vector<Text*> text;
};
```

## Example 04 ##

```c++
class TestLine {};
class UpperCaseTL : public TestLine {};
class CenteredTL : public TestLine {};
class UCTL : public UpperCaseTL, public CenteredTL {};
```

## Example 05 ##

```c++
class TextProcessor {};

class CaseControl : TextProcessor {};

class Centered : CaseControl {};

class TextLine2 : Centered {};
```

