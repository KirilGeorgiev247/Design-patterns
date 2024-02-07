### Facade ###

**Provides a unified interface to a set of interfaces in a subsystem. Facade defines a
higher - level interface that makes the subsystem easier to use**

+ makes strong coupling between interfaces weaker

+ hides the complicated interface system with strong coupling, hides the logic inside and provides simple logic (abstraction) to use the system from outside

+ sometimes it provides the base logic to the outside world, for example some getters(at all constant functions)

+ clients that use the facade don't have to access its subsystem objects directly

+ abstract factory can be used with Facade to provide an interface for creating
subsystem objects in a subsystem - independent way (Example 03)

*pros*

+ offers a simpler, user - friendly interface to a complex subsystem, making it easier for clients to use

+ reduces dependencies on the subsystem

+ by encapsulating complex code, Facade improves code readability and usability

+ segregation makes maintenance and expansion easier

*cons*

+ risk of becoming a **God Object**

+ it may limit the flexibility and customization that the complex system offers, although it simplifies the interface

+ in some cases it is not needed and lead to an extra layer of abstaction which is unnecessery and it's inefficient

## Example 01 ##

```java
// Complex subsystem components
class Amplifier {
    void on() { System.out.println("Amplifier on"); }
    // ... other methods ...
}

class Tuner {
    void setFrequency(double frequency) { /* ... */ }
    // ... other methods ...
}

class DvdPlayer {
    void play(String movie) { /* ... */ }
    // ... other methods ...
}

// Facade
class HomeTheaterFacade {
    private Amplifier amp;
    private Tuner tuner;
    private DvdPlayer dvd;

    HomeTheaterFacade(Amplifier amp, Tuner tuner, DvdPlayer dvd) {
        this.amp = amp;
        this.tuner = tuner;
        this.dvd = dvd;
    }

    void watchMovie(String movie) {
        System.out.println("Get ready to watch a movie...");
        amp.on();
        dvd.play(movie);
        // Other complex setup details...
    }

    // Other facade methods...
}

// Client Code
public class HomeTheaterTestDrive {
    public static void main(String[] args) {
        HomeTheaterFacade homeTheater = new HomeTheaterFacade(new Amplifier(), new Tuner(), new DvdPlayer());
        homeTheater.watchMovie("Raiders of the Lost Ark");
        // More operations...
    }
}
```

## Example 02 ##

```c++
#include <iostream>

// Complex subsystem components
class CPU {
public:
    void start() { std::cout << "CPU is starting." << std::endl; }
    // Other methods...
};

class Memory {
public:
    void load() { std::cout << "Memory is loading." << std::endl; }
    // Other methods...
};

class HardDrive {
public:
    void read() { std::cout << "Reading from hard drive." << std::endl; }
    // Other methods...
};

// Facade
class ComputerFacade {
    CPU cpu;
    Memory memory;
    HardDrive hardDrive;
public:
    void startComputer() {
        cpu.start();
        memory.load();
        hardDrive.read();
        std::cout << "Computer started." << std::endl;
    }
    // Other facade methods...
};

// Client Code
int main() {
    ComputerFacade computer;
    computer.startComputer();
    // More operations...
    return 0;
}
```

## Example 03

```java
// Abstract Factory Interfaces
interface CarFactory {
    Engine createEngine();
    Wheel createWheel();
}

interface Engine {
    void assemble();
}

interface Wheel {
    void assemble();
}

// Concrete Classes for Sedan
class SedanFactory implements CarFactory {
    public Engine createEngine() {
        return new SedanEngine();
    }

    public Wheel createWheel() {
        return new SedanWheel();
    }
}

class SedanEngine implements Engine {
    public void assemble() {
        System.out.println("Assembling Sedan Engine");
    }
}

class SedanWheel implements Wheel {
    public void assemble() {
        System.out.println("Assembling Sedan Wheel");
    }
}

// Concrete Classes for SUV
class SuvFactory implements CarFactory {
    public Engine createEngine() {
        return new SuvEngine();
    }

    public Wheel createWheel() {
        return new SuvWheel();
    }
}

class SuvEngine implements Engine {
    public void assemble() {
        System.out.println("Assembling SUV Engine");
    }
}

class SuvWheel implements Wheel {
    public void assemble() {
        System.out.println("Assembling SUV Wheel");
    }
}

// --- //

// Car Assembly Facade
class CarAssemblyFacade {
    private CarFactory carFactory;

    CarAssemblyFacade(CarFactory carFactory) {
        this.carFactory = carFactory;
    }

    void assembleCar() {
        Engine engine = carFactory.createEngine();
        Wheel wheel = carFactory.createWheel();

        System.out.println("Starting car assembly");
        engine.assemble();
        wheel.assemble();
        System.out.println("Car assembly completed");
    }
}

// Client Code
public class CarManufacturingClient {
    public static void main(String[] args) {
        CarFactory sedanFactory = new SedanFactory();
        CarAssemblyFacade sedanAssembly = new CarAssemblyFacade(sedanFactory);
        sedanAssembly.assembleCar();

        CarFactory suvFactory = new SuvFactory();
        CarAssemblyFacade suvAssembly = new CarAssemblyFacade(suvFactory);
        suvAssembly.assembleCar();
    }
}
```
