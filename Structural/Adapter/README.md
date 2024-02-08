### Adapter ###

**It translates or adapts the interface of one class into an interface another class can understand and use**

+ it simply arranges the calls being made

+ adaptee don't have much logic init

+ adapts one interface to another

+ using multi - inheritance, two - way can provide transparency. They're useful when two different clients need to view an object differently (Example 03)

*pros*

+ when you're working with multiple existing subclasses and find it impractical to modify each one's interface through further subclassing, an object adapter can be a valuable solution. This approach involves using an object adapter to alter the interface of the parent class

+  loose coupling by keeping the client code separate from the detailed underlying interface of the adapted class

+ changes to the adapted class do not impact the client code, making maintenance easier

+ helpful for integrating libraries or APIs where modification of source code is not possible

*cons*

+ adding adapters can complicate the code architecture

+ additional layer of abstraction can sometimes lead to a slight decrease in performance

+ code is a bit harder for understanding

## Example 01 ###

```java
// Target Interface
interface MicroUsbPhone {
    void recharge();
    void useMicroUsb();
}

// Adaptee Interface
interface LightningPhone {
    void recharge();
    void useLightning();
}

// Adapter Class
class LightningToMicroUsbAdapter implements MicroUsbPhone {
    private final LightningPhone lightningPhone;

    public LightningToMicroUsbAdapter(LightningPhone lightningPhone) {
        this.lightningPhone = lightningPhone;
    }

    public void recharge() {
        lightningPhone.recharge();
    }

    public void useMicroUsb() {
        System.out.println("MicroUsb connected");
        lightningPhone.useLightning();
    }
}

// Usage
public class AdapterDemo {
    public static void main(String[] args) {
        LightningPhone iphone = new Iphone();
        MicroUsbPhone androidAdapter = new LightningToMicroUsbAdapter(iphone);

        androidAdapter.useMicroUsb();
        androidAdapter.recharge();
    }
}
```

## Example 02 ###

```c++
template<typename T>
class Stack : private list<T> {
public:
	void push(const T& x) {
		push_back(x);
	}

	T pop() {
		T result = back();
		pop_back();
		return result;
	}

	using bool std::list<T>::empty() const;

	bool empty() const {
		return list<T>::empty();
	}
};

struct Node {};

// we don't want to change this type of code, rather we want to adapt 
// the list so we can pass it to the function
template<class Stack>
bool searchPath(Node start, Node end) {
	Stack<Node> front;

	front.push(start);
	while (!front.empty())
	{
		Node curr = front.pop();
		if (isOk(cur)) return true;

		for (auto& sc : cur.successors)
		{
			front.push(sc);
		}
	}
	return false;
}
```

## Example 03 ##

```c++
// specified for languages with multi - inheritance

struct Stack {
	virtual int pop() = 0;
	virtual void push() = 0;
	virtual bool empty() const = 0;
};

// adapter class
class ListAdapter : public Stack, private list<int> {
	...
};
```
