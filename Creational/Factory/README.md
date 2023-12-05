### Factory method ###

**Solves the problem of creating product objects without specifying their concrete classes.**

*pros*

+ **Simplicity**: It’s easier to understand and implement compared to more complex patterns like the Abstract Factory.

+ **Flexibility**: Allows for flexibility in creating objects without having to specify their exact classes.

+ **Extensibility**: New product types can be introduced by extending the creator class with minimal changes to existing code.

+ **Loose Coupling**: The client code is loosely coupled with the product implementations, making maintenance easier.

*cons*

+ **Class Increasment**: Can lead to an increase in the number of classes within an application.

+ **Refactoring**: If new products are similar, you might need to refactor the hierarchy to avoid duplicate code.

+ **Complexity for Simple Needs**: For simple applications, using a factory method can be more complex than necessary.

+ **Indirectness**: Clients might deal with a more indirect way of instantiation.

*Extra*

+ we don't know how the object is created and we don't know how to release the memory if needed.
  
	--> That's why we want to return RAII object(unique_pointer)
  
	--> Other fix is to implement release method in the factory or in the object (the factory knows the context)
  
	--> we can attach on the object refference to the factory but we distrupt the Single Responsibility principle and the life cycle of the object must now outlive the factory.

# Example 01 #

```c++
class Vehicle {
public:
    virtual void printVehicle() = 0;
};

class TwoWheeler : public Vehicle {
public:
    void printVehicle()
    {
        cout << "I am two wheeler" << endl;
    }
};

class FourWheeler : public Vehicle {
public:
    void printVehicle()
    {
        cout << "I am four wheeler" << endl;
    }
};
 
// factory
class Client {
public:
    Client(int type)
    {
 
        // Client explicitly creates classes according to type
        if (type == 1)
            pVehicle = new TwoWheeler();
        else if (type == 2)
            pVehicle = new FourWheeler();
        else
            pVehicle = NULL;
    }
 
    ~Client()
    {
        if (pVehicle) {
            delete pVehicle;
            pVehicle = NULL;
        }
    }
 
    Vehicle* getVehicle() { return pVehicle; }
 
private:
    Vehicle* pVehicle;
};
```

# Example 02 #

```c++
class Figure {
	virtual double getPerimeter() = 0;
};

class Circle : public Figure {
	double radius;
public:
	Circle(double radius) : radius(radius) {
		// ..
	}

	virtual double getPerimeter() override {
		return 2 * 3.14 * radius;
	}
};

class Triangle : public Figure {
	double a;
	double b;
	double c;

public:
	Triangle(double a, double b, double c) : a(a), b(b), c(c) {

	}

	virtual double getPerimeter() override {
		return a + b + c;
	}
};

class StdinTriangleFactory {};
class RandomTriangleFactory {};
class StdinFigureFactory {};

class CircleFromStdinFactory {
public:
	Circle* cteate() {
		double radius;
		cin >> radius;
		return new Circle(radius);
	}
};

class RandomCircleFactory {
public:
	Circle* create() {
		double radius = rand() % 100;
		return new Circle(radius);
	}
};


class RandomFigureFactory {
	Figure* create() {
		if (rand() % 2)
		{
			RandomTriangleFactory factory;
			return factory.create();
		}
		else
		{
			RandomCircleFactory factory;
			return factory.create();
		}
	}
};
```

# Example 03 #

```c++
// variable template (if we want it as a double we can get it as a double, if we want it as an int...)
template <typename T>
T pi = T(3.14159265);
```
