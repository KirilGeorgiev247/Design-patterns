### Dependency injection ###

**Resolves dependencies in software implementation logic**

+ they are services in most cases

+ we have interface for the dependency injection, which we inject

+ we can have helping funcs, which change the dependency injection

*pros*

+ reduces coupling between classes, and so they become less dependant on each other

+ it is easier to test a class when it doesn't depend on other classes

+ code is easier for maintaining

+ enhanced reusability

*cons*

+ sometimes dependency injections rely on some external configuration files, which lead to configuration overhead

+ when overused for simple tasks, it complicates unnecesserly

+ more challenging debug

## Example 01 ##

```c++
#include <iostream>
#include <fstream>
#include <string>

class Logger {
public:
	virtual void logMessage(const std::string& message) = 0;
	virtual ~Logger() {}
};

class ConsoleLogger : public Logger {
public:
	void logMessage(const std::string& message) override {
		std::cout << "Console Log: " << message << std::endl;
	}
};

class FileLogger : public Logger {
public:
	void logMessage(const std::string& message) override {
		std::ofstream file("log.txt", std::ios::app);
		file << "File Log: " << message << std::endl;
		file.close();
	}
};


class LoggingClient {
private:
	Logger* logger;

public:
	void setLogger(Logger* newLogger) {
		logger = newLogger;
	}

	void doLogging(const std::string& message) {
		if (logger) {
			logger->logMessage(message);
		}
		else {
			std::cout << "No logger defined!" << std::endl;
		}
	}
};

// Main function
int main() {
	LoggingClient client;

	ConsoleLogger consoleLogger;
	FileLogger fileLogger;

	// Using ConsoleLogger
	client.setLogger(&consoleLogger);
	client.doLogging("Test Console Logging");

	// Switching to FileLogger
	client.setLogger(&fileLogger);
	client.doLogging("Test File Logging");

	return 0;
}
```

## Example 02 ##

```java
interface NotificationService {
	void sendNotification(String message);
}

class EmailService implements NotificationService {
	public void sendNotification(String message) {
		System.out.println("Email notification sent: " + message);
	}
}

class SMSService implements NotificationService {
	public void sendNotification(String message) {
		System.out.println("SMS notification sent: " + message);
	}
}


class NotificationClient {
	private NotificationService service;

	// Setter for dependency injection
	public void setNotificationService(NotificationService service) {
		this.service = service;
	}

	public void doNotify(String message) {
		if (service != null) {
			service.sendNotification(message);
		}
		else {
			System.out.println("No notification service set!");
		}
	}
}

// Main class
public class DependencyInjectionDemo {
	public static void main(String[] args) {
		NotificationClient client = new NotificationClient();

		// Injecting EmailService
		client.setNotificationService(new EmailService());
		client.doNotify("Test Email");

		// Changing to SMSService
		client.setNotificationService(new SMSService());
		client.doNotify("Test SMS");
	}
}
```

## Example 03 (with service injector) ##

```java
// Service interface
interface AuthenticationService {
	boolean authenticate(String username, String password);
}

// Concrete Service
class SimpleAuthenticationService implements AuthenticationService {
	public boolean authenticate(String username, String password) {
		// Simple authentication logic (just an example)
		return "admin".equals(username) && "password".equals(password);
	}
}

class AuthenticationClient {
	private AuthenticationService authService;

	// Constructor injection
	AuthenticationClient(AuthenticationService authService) {
		this.authService = authService;
	}

	void execute(String username, String password) {
		if (authService.authenticate(username, password)) {
			System.out.println("User authenticated successfully.");
		}
		else {
			System.out.println("Authentication failed.");
		}
	}
}

interface ServiceInjector {
	AuthenticationClient buildClient();
}

class SimpleAuthServiceInjector implements ServiceInjector {
	public AuthenticationClient buildClient() {
		return new AuthenticationClient(new SimpleAuthenticationService());
	}
}

public class DependencyInjectionDemo {
	public static void main(String[] args) {
		ServiceInjector injector = new SimpleAuthServiceInjector();
		AuthenticationClient client = injector.buildClient();

		// Example usage
		client.execute("admin", "password"); // Successful authentication
		client.execute("user", "password"); // Failed authentication
	}
}
```