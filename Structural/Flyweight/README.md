### Flyweight ###

**Uses sharing to support large numbers of fine - grained objects efficiently**

+ powerful tool for optimizing memory and performance in systems with many similar objects

+ managing big amount of small objects via sharing

+ the question we must ask ourselves is if we actually need object (should be pay the price for using an object instead of primitive type)

+ objects have intrinsic and extrinsic states

+ we create objects and do not destruct them until the programm terminates, because we want to have them available all the time, not to create them every time we need them

+ collaborating well with abstract factory (Example 03)

+ shared things should be so that noone would want to change them (we rely that they won't be changed or it will be from some central system)

+ allows object that keep everything themselves (unsharing flyweight object)

+ string pool (in JAVA) is an example of the Flyweight Design Pattern (all literal strings and string - valued constant expressions are interned)

*pros*

+ by sharing common parts of the object state among multiple objects, it minimizes memory overhead

+ improve system scalability by reducing resource consumption

+ well - suited for systems that need to handle large numbers of objects, making it practical to create object - intensive simulations or applications (like graphical representations, gaming environments)

+ reducing the number of expensive objects

*cons*

+ runtime costs

+ sharing memory (hard change or deletion), incorrect handling can lead to issues

+ limits on how you can design the logic of your system

+ understanding and maintaining such a system can be challenging due to the separation of intrinsic and extrinsic states

## Example 01 ##

```java
import java.util.HashMap;
import java.util.Map;

// Flyweight Object
class Character {
    private final char value;

    public Character(char c) {
        this.value = c;
    }

    public void display(String formatting) {
        System.out.println(value + " (formatting: " + formatting + ")");
    }
}

// Flyweight Factory
class CharacterFactory {
    private final Map<Character, Character> characterMap = new HashMap<>();

    public Character getCharacter(char c) {
        Character character = characterMap.get(c);
        if (character == null) {
            character = new Character(c);
            characterMap.put(c, character);
        }
        return character;
    }
}

// Client Code
public class TextEditor {
    private final CharacterFactory factory = new CharacterFactory();

    public void displayFormattedText(String text, String formatting) {
        for (char c : text.toCharArray()) {
            Character character = factory.getCharacter(c);
            character.display(formatting);
        }
    }

    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        editor.displayFormattedText("Hello", "Bold");
    }
}
```

## Example 02 ##

```c++
#include <iostream>
#include <string>
#include <map>

// Intrinsic State
class TreeType {
public:
    std::string name;
    std::string color;
    std::string texture;

    TreeType(std::string name, std::string color, std::string texture)
        : name(name), color(color), texture(texture) {}

    void draw(int x, int y) {
        std::cout << "Drawing " << name << " tree at (" << x << ", " << y << "). Color: " << color << std::endl;
    }
};

// Flyweight Factory
class TreeFactory {
private:
    std::map<std::string, TreeType*> treeTypes;

public:
    TreeType* getTreeType(std::string name, std::string color, std::string texture) {
        std::string key = name + color + texture;
        if (treeTypes.find(key) == treeTypes.end()) {
            treeTypes[key] = new TreeType(name, color, texture);
        }
        return treeTypes[key];
    }
};

// Extrinsic State
class Tree {
private:
    int x, y;
    TreeType* type;

public:
    Tree(int x, int y, TreeType* type)
        : x(x), y(y), type(type) {}

    void draw() {
        type->draw(x, y);
    }
};

// Client Code
int main() {
    TreeFactory factory;

    for (int i = 0; i < 5; ++i) {
        TreeType* type = factory.getTreeType("Maple", "Red", "MapleTexture");
        Tree tree(i * 3, i * 4, type);
        tree.draw();
    }

    // More trees can be created with shared TreeType objects
    return 0;
}
```

## Example 03 ##

```c++
#include <iostream>
#include <map>
#include <vector>

using ushort = unsigned short;

class Context {
	std::size_t position;
public:
	Context(size_t position) : position(position) {}

	std::size_t getPosition() const {
		return position;
	}
};

class Token {
public:
	virtual size_t length() const = 0;
	voritual void print(Context const& context) const = 0;
	virtual std::string toString() const = 0;
	virtual ~Token() {}
};

class NumberToken : public Token{
	ushort number;
public:
	NumberToken(ushort number) : number(number) {}

	virtual size_t length() const {
		return std::to_string(number).length();
	}

	void print(Context const& context) const {
		std::cout << "This is number" << number << "on position" << context.getPosition() << '\n';
	}

	std::string toString() const {
		return std::to_string(number);
	}
};

class WordToken : public Token {
	std::string word;
	size_t wordStart;
public:
	WordToken(std::string word, size_t wordStart) : word(word), wordStart(wordStart) {}

	void print(Context const& context) const {
		if (context.getPosition() < wordStart || context.getPosition() >= wordStart + word.length())
			throw std::runtime_error(std::string("Invalid context of word") + toString();
		std::cout << "This is word" << toString() << "on position" << context.getPosition();
	}

	std::string toString() const {
		return word + "(" + std::to_string(wordStart) + ")";
	}
};

class TokenFactory {
	std::map<ushort, std::shared_ptr<NumberToken>>;
public:

	Token* getNumberToken(ushort num) {
		if (numberTokens.count(num) == 0)
		{
			numberTokens[num] = std::make_shared<NumberToken>(num);
			return numberTokens[num];
		}
	}

	Token* getWordToken(std::string word, size_t wordStart) {
		return std::make_shared<WordToken>(word, wordStart);
	}
};

class Sentence {
private:
	size_t length;
	std::vector<std::shared_ptr<Token>> tokens;
	TokenFactory& tokenFactory;
public:
	Sentence(TokenFactory& tokenFactory) : length(0), tokenFactory(tokenFactory) {}

	Sentence& addWord(std::string word) {
		tokens.push_back(tokenFactory.getWordToken(word, length));
		length += word.length() + 1;
		return *this;
	}

	Sentence& addNumber(ushort num) {
		tokens.push_back(tokenFactory.getNumberToken(num));
		length += std::to_string(num).length() + 1;
		return *this;
	}

	~Sentence() {
		// ??
	}

	void print() const {
		for (size_t position = 0, index = 0, currentTokenPos = 0; position < length; position++, currentTokenPos++) {
			if (currentTokenPos >= tokens[index]->length())
			{
				currentTokenPos = 0;
				index++;
			}

			std::cout << "[" << position << "] ";
			Context context(position);
			tokens[index]->print(context);
			std::cout << '\n';
		}
	}
};

int main() {
	TokenFactory* tokenFactory = new TokenFactory; // we make the factory to use it for our purposes
	Sentence sentence(*tokenFactory);

	sentence.addWord("Today").addWord("November").addNumber(21).addWord("at").addNumber(12)
		.addWord("hours").addWord("and").addNumber(21).addWord("minutes");

	sentence.print();

	// and we can delete it and still use already created objects because of shared_ptr, but we can't create more
	delete tokenFactory; 

	sentence.print();
}
```
