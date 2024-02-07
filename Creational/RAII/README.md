### RAII ###

**It is used to manage resource allocation and deallocation**

+ abbriviation: "Resource Acquisition Is Initialization".

+ particularly used in languages like C++.

+ example for RAII are smart pointers.

+ common use is wrapping file usability.

+ Autoclosables in java

*pros

+ **Automatic Cleanup**: Objects clean up their own resources automatically when they're no longer used.

+ **Safe from Errors**: It helps prevent errors by freeing resources if something goes wrong.

+ **No Memory Leaks**: It's good at preventing wasted memory since it ensures resources are always freed.

+ **Simpler Code**: The code is usually cleaner because it doesn't need manual cleanup.

*cons

+ **Inflexible**: Sometimes it's too strict for complicated tasks that need flexible resource management.

+ **Ownership Challenges**: It can be tricky to move resources around between different parts of a program.

+ **Limited Use**: It doesn't work well for all types of resources, like those that need to be shared or reused. (We can still make around this problem using shared and weak pointers)

# Example 01 #

```c++
class Foo {
public:
	Foo() {
		if (rand() % 123 == 0)
			throw std::exception;
	}
};

void test() {
	int a = new int[10]; // if it throws first leaks
	int b = new int[20]; // if it throws first and second leak
	int c = new int[30]; // if it throws first & ssecond & third leak
	Foo* d = new Foo[100]; // if it throws first & second & third & forth leak

	FILE* pFile = fopen("asdasdasd.dat", "r+");

	if (rand() % 2 == 0) // if it's true it will leak
		return;

	someFunctionThatMayThrow(); // if it throws it will leak
}

// --> first fix is with try and catch and in the catch block we free the memory and close opened streams
// --> second fix is to replace int* with vector<int> and FILE with ifstream -> this is kinda the idea of RAII
// --> in Java and C# we have Garbage collector so we don't have much need of RAII in most cases
```

# Example 02 #

```c++
class Test {
	int* data; // vector<int> data; (vector will take care of resource managment)
};
```
