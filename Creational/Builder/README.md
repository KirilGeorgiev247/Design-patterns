### Builder ###

**Allows cunstructing objects step by step. (chaining functions is allowed)**

+ the algorithm for creating a complex object should be independent of the
parts that make up the object and how they're assembled.

+ the construction process must allow different representations for the object
that's constructed.

*pros*

+ provides the client with an abstract interface for constructing the product.

+ improves modularity by encapsulating the way a complex object is constructed and represented. (Like other creational patterns, the Builder pattern encapsulates how objects get created)

*cons*

+ unlike creational patterns that construct products in one shot, the Builder pattern constructs the product step by step under the client's control. And we can't be sure what parts have been already constructed.

#Example 01#

```c++
// Parts -->

class Wheel
{
    public:
        int size;
};

class Engine
{
    public:
        int horsepower;
};

class Body
{
    public:
        std::string shape;
};

// Parts <--

// Final Product
class Car
{
    public:
        Wheel*   wheels[4];
        Engine*  engine;
        Body* body;
    
        void specifications()
        {
            std::cout << "body:" << body->shape << std::endl;
            std::cout << "engine horsepower:" << engine->horsepower << std::endl;
            std::cout << "tire size:" << wheels[0]->size << "'" << std::endl;
        }
};

// Responsible for creating the smaller parts
class Builder
{
    public:
        virtual Wheel* getWheel() = 0;
        virtual Engine* getEngine() = 0;
        virtual Body* getBody() = 0;
};

// Responsible for the whole process
class Client
{
    Builder* builder;

    public:
        void setBuilder(Builder* newBuilder)
        {
            builder = newBuilder;
        }

        Car* getCar()
        {
            Car* car = new Car();

            car->body = builder->getBody();

            car->engine = builder->getEngine();

            car->wheels[0] = builder->getWheel();
            car->wheels[1] = builder->getWheel();
            car->wheels[2] = builder->getWheel();
            car->wheels[3] = builder->getWheel();

            return car;
        }
};

// Concrete builder 01
class JeepBuilder : public Builder
{
    public:
        Wheel* getWheel()
        {
            Wheel* wheel = new Wheel();
            wheel->size = 22;
            return wheel;
        }

        Engine* getEngine()
        {
            Engine* engine = new Engine();
            engine->horsepower = 400;
            return engine;
        }

        Body* getBody()
        {
            Body* body = new Body();
            body->shape = "SUV";
	    return body;
        }
};

// Concrete builder 02
class NissanBuilder : public Builder
{
    public:
        Wheel* getWheel()
        {
            Wheel* wheel = new Wheel();
            wheel->size = 16;
            return wheel;
        }

        Engine* getEngine()
        {
            Engine* engine = new Engine();
            engine->horsepower = 85;
            return engine;
        }

        Body* getBody()
        {
            Body* body = new Body();
            body->shape = "hatchback";
        }
};


int main()
{
    Car* car; // Final product

    Client client; // Director

    // Concrete builders
    JeepBuilder jeepBuilder;
    NissanBuilder nissanBuilder;

    // build a jeep
    std::cout << "Jeep" << std::endl;
    client.setBuilder(&jeepBuilder);
    car = client.getCar();
    car->specifications();

    std::cout << std::endl;

    // build a nissan
    std::cout << "Nissan" << std::endl;
    client.setBuilder(&nissanBuilder);
    car = client.getCar();
    car->specifications();

    return 0;
}
```

# Example 02 #

```c++
class X {
private:
	int a;
	int b;
	int c;

	X(int a, int b = 0, int c = 0) {

	}

	X(int a, int b) {

	}

	X(int a, int b, int c) {

	}

public:
	X& a(int value) { a = value; return *this; }
	X& b(int value) { b = value; return *this; }
	X& c(int value) { c = value; return *this; }

	X build() {
		// creates the object;
	}
};

X x;

x.a(5).c(10).b(1000).build();
```

