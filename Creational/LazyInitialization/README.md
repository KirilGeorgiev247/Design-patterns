### Lazy initialization ###

**The construction of the object is not possible immediately but depends on some parameter whose value is not yet known.**

+ specific for c++, using the class std::optional will be usually efficient.

+ provides solution to infinite problems

+ classes can have different implementations on how the process of lazy initialization occurs.

*pros*

+ **Save Resources**: It saves system resources by creating objects only when they are needed.

+ **Improved Performance**: Can lead to faster application startup times because less work is done upfront.

+ **Memory Efficient**: It uses memory more efficiently by not holding onto objects that aren't currently needed.

*cons*

+ **Complexity**: Adds complexity to the code because you have to check if the object has been created every time you use it.

+ **Overhead**: Can introduce overhead due to the need for synchronization in multi-threaded environments.

+ **Unexpected Delays**: Might cause delays later in the program when the object is eventually created.

*Extra*

+ memoization and lazy initialization can collaborate and also can be mistaken for one another

+ for example if we have two matrix and we create third for the multiplication, but this third we don't know how much cells we need, we can use this pattern.

# Example 01 #

```c++
class Config
{
    std::optional<File> file{};
public:
    Config()
    {
        std::cout << "Config object created" << std::endl;
    }

    void addOption(std::string_view name, std::string_view value)
    {
        if (!file)
            file.emplace("config.txt");
        file->write(name);
        file->write(" = ");
        file->write(value);
        file->write("\n");
    }
};
```

# Example 02 #

```c++
f(x) := <heavy stuff>

// first case
g(a, b) : = if (false) a else b

g(f(42), 0)

if (false) f(42) else 0
0 // with lazy init we won't calculate f(42) because is not needed

// second case
h(a) : = a + a + a

h(f(42)) // here we face the problem that we will calculate 3 times because of lazy init

[t: f(42)]
t + t + t // we can fix it like this for example but other problems may occur, that's why **it depends**
```