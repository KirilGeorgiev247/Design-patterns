# Visitor

**Represent an operation to be performed on the elements of an object structure. Visitor lets you define a new operation without changing the classes of the elements on which it operates**

***

+ we have this pattern implemented in most languages (like C#), but in Java and C++ we do not

+ related problems: overload resolution, template instantiation

+ solves the problem with dispatchering

*pros*

+ decouples the algorithm from the object structure

+ if an operation requires interacting with several different types of elements, it can be centralized in a visitor rather than distributed across element classes

*cons*

+ element classes need to have the accept method, which can be seen as intrusive, especially if the elements are part of a third-party library or if their modification is otherwise undesirable

+ creates a tight coupling between the visitors and the elements, as visitors need to be aware of all the concrete element classes they will interact with

***

# Example 01 (in java)

```java
abstract class Image {
    // Method to accept a filter
    public abstract void accept(Filter f);
}

class RgbImage extends Image {
    @Override
        public void accept(Filter f) {
        f.applyTo(this);
    }
    // Other methods specific to RgbImage
}

class GrayscaleImage extends Image {
    @Override
        public void accept(Filter f) {
        f.applyTo(this);
    }
    // Other methods specific to GrayscaleImage
}

// More derived image classes as needed


abstract class Filter {
    // Single abstract method in the base class
    public abstract void applyTo(Image img);
}

class CropFilter extends Filter {
    @Override
        public void applyTo(Image img) {
        // General crop algorithm or delegation to specific methods
    }

    public void applyTo(RgbImage img) {
        // Crop algorithm specific to RgbImage
    }

    // Other overloaded methods as needed
}

class ResizeFilter extends Filter {
    @Override
        public void applyTo(Image img) {
        // General resize algorithm or delegation to specific methods
    }

    public void applyTo(RgbImage img) {
        // Resize algorithm specific to RgbImage
    }

    // Other overloaded methods as needed
}


class ResizeFilter extends Filter {
    @Override
        public void applyTo(Image img) {
        // General resize algorithm or delegation to specific methods
    }

    @Override
        public void applyTo(RgbImage img) {
        // Resize algorithm specific to RgbImage
    }

    // Other overloaded methods as needed
}

public class Main {
    public static void main(String[] args) {
        Image img = new RgbImage();
        Filter filter = new ResizeFilter();

        // Apply filter to image
        img.accept(filter);
    }
}
```

# Example 02 (in c++)

```c++
#include <iostream>

// Forward declarations for all image types
struct Image;
struct MonochromeImage;
struct GrayscaleImage;
struct RgbImage;
struct CmykImage;

// The base class for all filters.
// Contains basic implementations of all operations that do nothing.
// Not how each image-specific overload forwards to the virtual applyTo function.
struct Filter {
	virtual void applyTo(Image* img) {
		std::cout << "Filter::applyTo(Image*) (default implementation, does nothing)\n";
	}
	virtual void applyTo(MonochromeImage* img)  {
		std::cout << "Filter::applyTo(MonochromeImage*), forwarding to applyTo(Image*)\n";
		applyTo((Image*)img);
	}
	virtual void applyTo(GrayscaleImage* img) {
		std::cout << "Filter::applyTo(GrayscaleImage*), forwarding to applyTo(Image*)\n";
		applyTo((Image*)img);
	}
	virtual void applyTo(RgbImage* img) {
		std::cout << "Filter::applyTo(RgbImage*), forwarding to applyTo(Image*)\n";
		applyTo((Image*)img);
	}
	virtual void applyTo(CmykImage* img) {
		std::cout << "Filter::applyTo(CmykImage*), forwarding to applyTo(Image*)\n";
		applyTo((Image*)img);
	}
};

// Definition for the Image class
struct Rgb {
	uint8_t red;
	uint8_t green;
	uint8_t blue;

	Rgb() = default;
};

struct Image {
	// May as well have been pure virtual functions.
	virtual Rgb getPixel(int row, int col) const { return Rgb(); }	
	virtual void setPixel(int row, int col, Rgb color) {};
	virtual size_t getWidth() const { return 0; };
	virtual size_t getHeight() const { return 0; };
	virtual void setWidth(size_t value) {};
	virtual void setHeight(size_t value) {};
	virtual void accept(Filter* f) = 0;
};

// Specific Image types
struct MonochromeImage : public Image {
	virtual void accept(Filter* f) override {
		f->applyTo(this); // this:MonochromeImage
	}
};
struct GrayscaleImage : public Image {
	virtual void accept(Filter* f) override {
		f->applyTo(this); // this:GrayscaleImage
	}
};

struct RgbImage : public Image {
	virtual void accept(Filter* f) {
		f->applyTo(this); // this:RgbImage
	}	
};

struct CmykImage : public Image {
	virtual void accept(Filter* f) {
		f->applyTo(this); // this:CMykImage
	}	
};

// Specific Filter types
struct CropFilter : public Filter {
	// When you DO NOT need to discriminate between the different image types,
	// you can implement the generic version.
	void applyTo(Image* img) override {
		std::cout << "CropFilter::applyTo(Image*)\n";
	}
};

struct ResizeFilter : public Filter {
	// When you need to discriminate between the different image types,
	// you can do so.
	void applyTo(MonochromeImage* img) {
		std::cout << "ResizeFilter::applyTo(MonochromeImage*)\n";
	}
	void applyTo(GrayscaleImage* img) {
		std::cout << "ResizeFilter::applyTo(GrayscaleImage*)\n";
	}
	void applyTo(RgbImage* img) {
		std::cout << "ResizeFilter::applyTo(RgbImage*)\n";
	}
	void applyTo(CmykImage* img) {
		std::cout << "ResizeFilter::applyTo(CmykImage*)\n";
	}
};

struct ConvertToGrayscaleFilter : public Filter {
	// If you need to process only some of the image types and leave
	// the others unmodified, you only override the types ypu need
	void applyTo(RgbImage* img) {
		std::cout << "ConvertToGrayscaleFilter::applyTo(RgbImage*)\n";
	}
	void applyTo(CmykImage* img) {
		std::cout << "ConvertToGrayscaleFilter::applyTo(CmykImage*)\n";
	}
};

struct GrayscaleOnlyFilter : public Filter {
	// It is also possible to define behavior specific to one or more
	// image types and then define a rule that applies to all others.
	void applyTo(GrayscaleImage* img) {
		std::cout << "GrayscaleOnlyFilter::applyTo(GrayscaleOnlyFilter*)\n";
	}
	void applyTo(Image* img) {
		std::cout << "GrayscaleOnlyFilter::applyTo(Image*)\n";
	}
};

int main()
{
	// Images
	GrayscaleImage gi;
	Image& giRef = gi;

	RgbImage ri;
	Image& riRef = ri;
	
	std::cout << "\n1: CropFilter applied to a GrayscaleImage:\n";
	CropFilter cf;
	giRef.accept(&cf);
	
	std::cout << "\n2: ResizeFilter applied to a GrayscaleImage:\n";
	ResizeFilter rf;
	giRef.accept(&rf);

	std::cout << "\n3: ConvertToGrayscaleFilter applied to a GrayscaleImage:\n";
	ConvertToGrayscaleFilter ctgf;
	giRef.accept(&ctgf);

	std::cout << "\n4: ConvertToGrayscaleFilter applied to a RgbImage:\n";
	riRef.accept(&ctgf);

	std::cout << "\n5: GrayscaleOnlyFilter applied to a RgbImage:\n";
	GrayscaleOnlyFilter gof;
	riRef.accept(&gof);

	std::cout << "\n6: GrayscaleOnlyFilter applied to a GrayscaleImage:\n";
	giRef.accept(&gof);
}
```