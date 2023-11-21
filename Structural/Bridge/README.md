### Bridge ###

**Decouples an abstraction from its implementation, so that these two will vary independantly**

+ we have abstraction for the abstraction, then we have more concrete abstractions

+ collaborating well with adapter (Example 02, Example 04)

*pros*

+ separating hierarchy of abstractions from hierarchy of implementations

+ extending of both happens independantly for each of them

+ flexible changes

+ Single Responsibility Principle

+ implementations can be changed dynamically in runtime

*cons*

+ introduces additional layers of abstraction and indirection

+ design complexity

+ overuse -> we don't need it when we have only one implementator

+ creating the right implementator object when there's more than one

## Example 01 ##

```java
interface Remote {
	void power();
	void volumeUp();
	void volumeDown();
}

// Refined Abstraction
class BasicRemote implements Remote {
	protected Device device;

	BasicRemote(Device device) {
		this.device = device;
	}

	public void power() {
		System.out.println("Toggle power");
		if (device.isEnabled()) {
			device.disable();
		}
		else {
			device.enable();
		}
	}

	// Other methods like volumeUp, volumeDown...
}

interface Device {
	boolean isEnabled();
	void enable();
	void disable();
	// Other device-specific methods...
}

class TV implements Device {
	private boolean on = false;

	public boolean isEnabled() { return on; }
	public void enable() { on = true; }
	public void disable() { on = false; }
	// Other TV specific methods...
}

class Radio implements Device {
	private boolean on = false;

	// Implementations for Radio specific methods
}

// sample code
public class Client {
	public static void main(String[] args) {
		Device tv = new TV();
		Remote remote = new BasicRemote(tv);
		remote.power(); // Toggles the power of the TV
	}
}
```

## Example 02 ##

```java
abstract class MediaPlayer {
	protected MediaPlayerImplementation implementation;

	MediaPlayer(MediaPlayerImplementation implementation) {
		this.implementation = implementation;
	}

	abstract void play(String file);
}

interface MediaPlayerImplementation {
	void playFile(String file);
}

class VLCMediaPlayer implements MediaPlayerImplementation {
	public void playFile(String file) {
		System.out.println("Playing " + file + " using VLC Media Player");
	}
}

class QuickTimeMediaPlayer implements MediaPlayerImplementation {
	public void playFile(String file) {
		System.out.println("Playing " + file + " using QuickTime Media Player");
	}
}

// External library
class ExternalAdvancedMediaPlayer {
	void playAdvancedMedia(String file) {
		// Advanced media playing algorithms
	}
}

// Adapter
class AdvancedMediaPlayerAdapter implements MediaPlayerImplementation {
	private ExternalAdvancedMediaPlayer advancedMediaPlayer;

	AdvancedMediaPlayerAdapter(ExternalAdvancedMediaPlayer advancedMediaPlayer) {
		this.advancedMediaPlayer = advancedMediaPlayer;
	}

	public void playFile(String file) {
		advancedMediaPlayer.playAdvancedMedia(file);
	}
}

class StandardMediaPlayer extends MediaPlayer {
	StandardMediaPlayer(MediaPlayerImplementation implementation) {
		super(implementation);
	}

	void play(String file) {
		implementation.playFile(file);
	}
}

// Client Code
public class MediaApplication {
	public static void main(String[] args) {
		MediaPlayer vlcPlayer = new StandardMediaPlayer(new VLCMediaPlayer());
		vlcPlayer.play("movie.mp4");

		MediaPlayer advancedPlayer = new StandardMediaPlayer(new AdvancedMediaPlayerAdapter(new ExternalAdvancedMediaPlayer()));
		advancedPlayer.play("advanced_movie.mp4");
	}
}
```

## Example 03 ##

```c++
class WindowImplementation;

class Window {
protected:
	WindowImplementation* implementation;

public:
	Window(WindowImplementation* impl) : implementation(impl) {}
	virtual void open() = 0;
	virtual ~Window() {}
};

class IconWindow : public Window {
public:
	IconWindow(WindowImplementation* impl) : Window(impl) {}
	void open() override;
};

// Other window types...

class WindowImplementation {
public:
	virtual void openWindow() = 0;
	virtual ~WindowImplementation() {}
};

class XWindowImplementation : public WindowImplementation {
public:
	void openWindow() override {
		// X-Windows specific opening mechanism
	}
};

class MacOSWindowImplementation : public WindowImplementation {
public:
	void openWindow() override {
		// MacOS specific opening mechanism
	}
};

int main() {
	WindowImplementation* xWindowImpl = new XWindowImplementation();
	Window* window = new IconWindow(xWindowImpl);
	window->open(); // Opens an icon window using X-Windows system

	delete window;
	delete xWindowImpl;
	return 0;
}
```

## Example 04 ##

```c++
class DrawingAPI {
public:
	virtual void drawCircle(double x, double y, double radius) = 0;
	virtual ~DrawingAPI() {}
};

class Shape {
protected:
	DrawingAPI* drawingAPI;

	Shape(DrawingAPI* drawingAPI) : drawingAPI(drawingAPI) {}

public:
	virtual void draw() = 0;
	virtual void resize(double percent) = 0;
	virtual ~Shape() {}
};

class DrawingAPI1 : public DrawingAPI {
public:
	void drawCircle(double x, double y, double radius) override {
		std::cout << "API1.circle at " << x << ':' << y << ' ' << radius << std::endl;
	}
};

class DrawingAPI2 : public DrawingAPI {
public:
	void drawCircle(double x, double y, double radius) override {
		std::cout << "API2.circle at " << x << ':' << y << ' ' << radius << std::endl;
	}
};

// Third-party graphics library
class FancyDrawing {
public:
	void fancyDraw(double x, double y, double radius) {
		// Advanced drawing code
	}
};

// Adapter
class FancyDrawingAdapter : public DrawingAPI {
	FancyDrawing fancyDrawing;

public:
	void drawCircle(double x, double y, double radius) override {
		fancyDrawing.fancyDraw(x, y, radius);
	}
};

class CircleShape : public Shape {
private:
	double x, y, radius;

public:
	CircleShape(double x, double y, double radius, DrawingAPI* drawingAPI)
		: Shape(drawingAPI), x(x), y(y), radius(radius) {}

	void draw() override {
		drawingAPI->drawCircle(x, y, radius);
	}

	void resize(double percent) override {
		radius *= percent;
	}
};

// Client Code
int main() {
	CircleShape circle1(1, 2, 3, new DrawingAPI1());
	CircleShape circle2(5, 7, 11, new FancyDrawingAdapter());

	circle1.draw();
	circle2.draw(); // This will use the adapted FancyDrawing

	delete circle1.drawingAPI;
	delete circle2.drawingAPI;
	return 0;
}
```

## Example 05 (with composition)##

```java
class Engine {
	void start() {
		System.out.println("Engine started");
	}

	void stop() {
		System.out.println("Engine stopped");
	}
}

class Wheels {
	void roll() {
		System.out.println("Wheels rolling");
	}

	void stop() {
		System.out.println("Wheels stopped");
	}
}

class Car {
	private Engine engine;
	private Wheels wheels;

	Car() {
		engine = new Engine();
		wheels = new Wheels();
	}

	void start() {
		engine.start();
		wheels.roll();
		System.out.println("Car started");
	}

	void stop() {
		engine.stop();
		wheels.stop();
		System.out.println("Car stopped");
	}
}

// Client Code
public class Main {
	public static void main(String[] args) {
		Car car = new Car();
		car.start();
		car.stop();
	}
}
```

## Example 06 (with composition)##

```c++
class Processor {
public:
	void performComputation() {
		std::cout << "Processor is computing." << std::endl;
	}
};

class Memory {
public:
	void loadMemory() {
		std::cout << "Memory is loading data." << std::endl;
	}
};

class Computer {
private:
	Processor processor;
	Memory memory;

public:
	void start() {
		processor.performComputation();
		memory.loadMemory();
		std::cout << "Computer started." << std::endl;
	}
};

// Client Code
int main() {
	Computer myComputer;
	myComputer.start();
	return 0;
}
```