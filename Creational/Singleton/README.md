### Singleton ###

**Ensure a class only has one instance, and provide a global point of access to it.**

+ when we need exactly one instance of a class, and client should be able to access it from a public access point.

+ we define an operation that lets clients access its unique instance if it's created.

+ has the responsibility for creating its own unique instance when it's not instanciated.

+ clients access a Singleton instance through Singleton's instance operation.

*pros*

+ it can have strict control over how and when clients access it. ( The singleton class encapsulates its instance)

+ we can use the same approach to control the number of instances that
the application uses. Only the operation that grants access to the Singleton
instance needs to change.

*cons* 

+ before we create singleton we can't guarantee that only one instance of a static object will ever be declared.

+ we might not have enough information to instantiate every singleton at initialization time. A singleton might require values that are computed later in the program's execution.

+ Dependencies between singletons have undefined behaviour.

+ It's hard to instantiate subclasses. (But not impossible, one way is to have a collection of subclasses in the parent.)

# Example 01 #

```c++
class Singleton {
private:
    static Singleton* instance;

    // Private constructor so that no objects can be created.
    Singleton() {
        // do something
    }

public:
    // accessor
    static Singleton* getInstance() {
        if (instance == nullptr) {
            instance = new Singleton();
        }
        return instance;
    }
};
```
