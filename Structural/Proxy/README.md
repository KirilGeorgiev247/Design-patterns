### Proxy ###

**Provides a stand - in or a substitute for something, managing who can use it**

+ controlls the accessibility of data

+ when we want part of the class, because most of the time we don't use some of the resources, which could be very expensive (reffering to memory)

+ other case is when data is not available at the moment of object creation

+ smart pointers are example of proxy pattern(they check if they have information and return it if so)

+ we use it when we know from the beggining what the flow will be (most of the times but not always)

+ when we have complicated collections, we can use only part of it with this pattern

+ although decorators can have similar implementations as proxies, decorators have a different purpose. A decorator adds one or more responsibilities to an object, whereas a proxy controls access to an object.

*pros*

+ controls access to the real object, making it useful for security and protection purposes

+ lazy loading

+ caching

+ keeps the client code simple as the client interacts with the Proxy in the same way he does with the real object

*cons*

+ additional response time due to the additional layer

+ more complex implementation 

+ risk of overuse

## Example 01 ##

```java
interface DatabaseAccess {
    void provideAccess();
}

class RealDatabaseAccess implements DatabaseAccess {
    private String employeeName;

    public RealDatabaseAccess(String employeeName) {
        this.employeeName = employeeName;
    }

    @Override
    public void provideAccess() {
        System.out.println("Access granted to " + employeeName);
    }
}

class ProxyDatabaseAccess implements DatabaseAccess {
    private RealDatabaseAccess realAccess;
    private String employeeName;

    public ProxyDatabaseAccess(String employeeName) {
        this.employeeName = employeeName;
    }

    @Override
    public void provideAccess() {
        if (this.employeeName.equals("Admin")) {
            realAccess = new RealDatabaseAccess(this.employeeName);
            realAccess.provideAccess();
        } else {
            System.out.println("Access denied for " + employeeName);
        }
    }
}

public class ProxyDemo {
    public static void main(String[] args) {
        DatabaseAccess databaseAccess = new ProxyDatabaseAccess("Admin");
        databaseAccess.provideAccess();
    }
}
```

## Example 02 ##

```c++
#include <iostream>

class Image {
public:
    virtual void display() = 0;
    virtual ~Image() = default;
};

class RealImage : public Image {
private:
    std::string fileName;

public:
    RealImage(std::string fileName) : fileName(fileName) {
        loadFromDisk(fileName);
    }

    void display() override {
        std::cout << "Displaying " << fileName << std::endl;
    }

    void loadFromDisk(std::string fileName) {
        std::cout << "Loading " << fileName << std::endl;
    }
};

class ProxyImage : public Image {
private:
    RealImage* realImage;
    std::string fileName;

public:
    ProxyImage(std::string fileName) : fileName(fileName), realImage(nullptr) {}

    void display() override {
        if (!realImage) {
            realImage = new RealImage(fileName);
        }
        realImage->display();
    }

    ~ProxyImage() {
        delete realImage;
    }
};

int main() {
    Image* image = new ProxyImage("test_image.jpg");
    // Image will be loaded here
    image->display();

    // Image will not be loaded here, as it is already loaded
    image->display();
    delete image;
    return 0;
}
```

## Example 03 ##

```c++
template<typename DataType>
class SortedArrayProxy {
	friend class SortedArray;
public:
	operator const DataType& () const;
	operator DataType& ();

	// same logic with operator= , cctor and so on

private:
	SortedArrayProxy(SortedArray& owner, size_t index) :
		_owner(owner),
		_index(index) {}

	SortedArray& _owner;
	size_t _index;
};

template <typename DataType>
class SortedArray {
	// typical functions

	SortedArrayProxy<DataType> operator[](size_t index) return SortedArrayProxy<DataType>(*this, index);

private:
	DynArray<DataType> array;
};
```

## Example 04 ##

```c++
class DB {};
class FS {};

class StudentRecordProxy {
public:
	string getName();
	string* getPicture() {
		if (!picture) {
			picture = fs.loadPicture();
		}
		return picture;
	}
private:

	DB* db;
	FS* fs;
	string* picture;
};
```
