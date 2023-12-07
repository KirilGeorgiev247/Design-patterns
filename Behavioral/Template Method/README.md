# Template method

**defines the skeleton of an algorithm in the superclass, but lets superclasses redefine certain steps of an algorithm without changing structure of the algorithm**

+ example is dfs and bfs in a tree

+ example is comparator class in initializing a data structure

+ example is building a house is the algorithm and the specific parts an be changed via the subclasses

+ collaborating with Strategy design pattern (examples 03 and 04)

*pros*

+ code reusabillity

+ consistant algorithm structure -> ensures a predefined structure of an algorithm, ensuring that subclasses cannot change the overall structure of the algorithm

+ extention points -> provides	specific points, where subclasses can extend the behaviour

+ changing at only one place -> changing the algorithm structure needs to be done only in the superclass

+ well encapsulated algorithm structure

*cons

+ limited flexibillity -> changing the structure of the algorithm is limited

+ some clients may be limited by the provided skeleton of an algorithm

+ risk of over - generalization -> trying to cover too many similar algorithms in a single template can lead to a complicated design that is hard readable and maintainable

+ misuse in simple scenarios -> can be overkill in simple scenarios

# Example 01

```java
public abstract class DataProcessor {

    // Template method
    public final void processData() {
        readData();
        processData();
        saveData();
    }

    private void readData() {
        System.out.println("Reading data");
        // Common read data implementation
    }

    private void processData() {
        System.out.println("Processing data");
        // Common process data implementation
    }

    // Abstract method to be implemented by subclasses
    protected abstract void saveData();
}

public class DatabaseDataProcessor extends DataProcessor {
    @Override
        protected void saveData() {
        System.out.println("Saving data to a database");
        // Implementation for saving data to a database
    }
}

public class FileDataProcessor extends DataProcessor {
    @Override
        protected void saveData() {
        System.out.println("Saving data to a file");
        // Implementation for saving data to a file
    }
}

public class TemplateMethodDemo {
    public static void main(String[] args) {
        DataProcessor dbProcessor = new DatabaseDataProcessor();
        dbProcessor.processData();

        DataProcessor fileProcessor = new FileDataProcessor();
        fileProcessor.processData();
    }
}
```

# Example 02 

```c++
class Game {
public:
    // Template method
    void playGame() {
        initializeGame();
        startGame();
        endGame();
    }

    virtual ~Game() {}

protected:
    virtual void initializeGame() = 0;
    virtual void startGame() = 0;
    virtual void endGame() = 0;
};

class Chess : public Game {
protected:
    void initializeGame() override {
        std::cout << "Chess Game Initialized!" << std::endl;
        // Initialization specific to Chess
    }

    void startGame() override {
        std::cout << "Chess Game Started!" << std::endl;
        // Start game logic for Chess
    }

    void endGame() override {
        std::cout << "Chess Game Finished!" << std::endl;
        // Ending game logic for Chess
    }
};

class Soccer : public Game {
protected:
    void initializeGame() override {
        std::cout << "Soccer Game Initialized!" << std::endl;
        // Initialization specific to Soccer
    }

    void startGame() override {
        std::cout << "Soccer Game Started!" << std::endl;
        // Start game logic for Soccer
    }

    void endGame() override {
        std::cout << "Soccer Game Finished!" << std::endl;
        // Ending game logic for Soccer
    }
};

int main() {
    Game* game = new Chess();
    game->playGame();
    delete game;

    game = new Soccer();
    game->playGame();
    delete game;

    return 0;
}
```

# Example 03 (with Strategy)

```java
 // Strategy Interface and Concrete Strategies for Seasoning
interface SeasoningStrategy {
    void applySeasoning();
}

class SpicySeasoning implements SeasoningStrategy {
    public void applySeasoning() {
        System.out.println("Applying spicy seasoning");
    }
}

class MildSeasoning implements SeasoningStrategy {
    public void applySeasoning() {
        System.out.println("Applying mild seasoning");
    }
}

 // Abstract Class with the Template Method for Cooking
abstract class CookingRecipe {
    final void cookRecipe() {
        prepareIngredients();
        cook();
        applySeasoning();
        serve();
    }

    void prepareIngredients() {
        System.out.println("Preparing ingredients");
    }

    void cook() {
        System.out.println("Cooking the dish");
    }

    abstract void applySeasoning(); // Delegated to Strategy

    void serve() {
        System.out.println("Serving the dish");
    }
}

 // Concrete Subclass Implementing the Strategy
class PastaRecipe extends CookingRecipe {
    private SeasoningStrategy seasoningStrategy;

    public PastaRecipe(SeasoningStrategy strategy) {
        this.seasoningStrategy = strategy;
    }

    void applySeasoning() {
        seasoningStrategy.applySeasoning();
    }
}

public class CookingDemo {
    public static void main(String[] args) {
        SeasoningStrategy spicy = new SpicySeasoning();
        CookingRecipe pastaWithSpicySeasoning = new PastaRecipe(spicy);
        pastaWithSpicySeasoning.cookRecipe();

        SeasoningStrategy mild = new MildSeasoning();
        CookingRecipe pastaWithMildSeasoning = new PastaRecipe(mild);
        pastaWithMildSeasoning.cookRecipe();
    }
}
```

# Example 04 (with Strategy)

```c++
 // Strategy Interface and Concrete Strategies for Formatting
class FormattingStrategy {
public:
    virtual void formatDocument() = 0;
    virtual ~FormattingStrategy() {}
};

class SimpleFormatting : public FormattingStrategy {
    void formatDocument() override {
        std::cout << "Applying simple formatting" << std::endl;
    }
};

class FancyFormatting : public FormattingStrategy {
    void formatDocument() override {
        std::cout << "Applying fancy formatting" << std::endl;
    }
};

 // Abstract Class with the Template Method for Document Generation
class DocumentGenerator {
public:
    void generateDocument() {
        createDocument();
        formatDocument();
        printDocument();
    }

    virtual ~DocumentGenerator() {}

protected:
    virtual void createDocument() {
        std::cout << "Creating document" << std::endl;
    }

    virtual void formatDocument() = 0; // Delegated to Strategy

    virtual void printDocument() {
        std::cout << "Printing document" << std::endl;
    }
};

 // Concrete Subclass Implementing the Strategy
class ReportGenerator : public DocumentGenerator {
private:
    FormattingStrategy* formattingStrategy;

public:
    ReportGenerator(FormattingStrategy* strategy) : formattingStrategy(strategy) {}

    void formatDocument() override {
        formattingStrategy->formatDocument();
    }

    ~ReportGenerator() {
        delete formattingStrategy;
    }
};

int main() {
    FormattingStrategy* simple = new SimpleFormatting();
    DocumentGenerator* reportWithSimpleFormatting = new ReportGenerator(simple);
    reportWithSimpleFormatting->generateDocument();

    FormattingStrategy* fancy = new FancyFormatting();
    DocumentGenerator* reportWithFancyFormatting = new ReportGenerator(fancy);
    reportWithFancyFormatting->generateDocument();

    delete reportWithSimpleFormatting;
    delete reportWithFancyFormatting;

    return 0;
}
```