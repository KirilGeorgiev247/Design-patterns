### Prototype ###

**Specify the kinds of objects to create using a prototypical instance, and create new
objects by copying this prototype.**

+ when we need to avoid a matching set of factory layers for every layer of product types.

+ if a class has only a few possible combinations of states, it might be easier to copy a set of sample models than to create new ones from scratch each time.

*pros*
 
+ adding and removing products at run-time.

+ cloning a prototype is similar to instantiating a class. 

+ lets you clone a prototype instead of asking a factory method to make a new
object.

*cons* 

+ hard part is to implement the Clone operation correctly. It's particularly tricky when
object structures contain circular references. (we need deep copy not just copy in most cases)

*Extra*

+ used in polymorphic hierarchies.

+ used when we don't know the type of the class we are copying.

+ in C# and Java we have already implemented interface for this design pattern.

+ problems when unit testing: 

	--> if only one clone of the children has no safety garantee everything down the hierchy will fail. That's why we use try and catch blocks with this pattern.

	--> different functions for equation check may aquire.

# Example #

```c++
class Figure {
	int x;

	virtual Figure* clone() = 0;
};

class A : public Figure {

public:
	Figure* clone() override {
		return new A(*this);
	}
};

class B : public Figure {

public:
	Figure* clone() override {
		return new B(*this);
	}
};
```
