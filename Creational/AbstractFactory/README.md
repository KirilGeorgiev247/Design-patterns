### Abstract Factory

**Provides an interface for creating families of related or dependent objects without specifying their concrete classes**

+ most often it is singleton because we need just one in our programm.

+ when a system should be configured with one out of multiple families of products.

+ we hide the logic of how the products are created, and reveal their interfaces not their implementations.

*pros*

+ helps you control the classes of objects that an application creates.

+ the class of a concrete factory appears only once in an application and that is, where it's instantiated.

+ when products from the same group are made to be used together, it’s crucial that an app only uses products from one group at once. AbstractFactory ensures that sticking to one product group is simple.

*cons*

+ to add support for new product types, you must update the factory interface, which means altering the AbstractFactory class and every subclass that inherits from it.

+ all products come back to the client looking the same because they share an interface. The client can't tear them apart or guess their type. If the client needs to do special things with a certain type of product, they can't, because the shared interface doesn't allow it.

*Extra*

+ factory for factories

+ when we have different configurations

# Example 01

```c++
class AbstractFigureFactory {
public:
	static FigureFactory* createByName(string name) {
		// how do you want to create your figures
		// with RandomFactory or StdinFactory

		if (name == "figure")
		{
			return new RandomFigureFactory();
		}
		else if (name == "stdin")
		{
			return new StdinFigureFactory();
		}
		else throw invalid_factory_name;
	}
};
```
