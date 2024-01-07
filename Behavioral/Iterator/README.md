# Iterator

**Provides a standardized way of accessing elements in a collection, such as an array or a list, sequentially without exposing the underlying representation of the collection**

***

+ sometimes is way easier to use f.e. bfs or dfs to go through a tree instead of making complicated class for iterator

+ methods -> next(), hasNext(), remove()

+ can be inside or outside class, but outside is better (f.e. if we want two iterators at the same time)

+ sometimes is okay if iterator knows about the collection, but this to be done with friend classes(in C++) or packages (in Java and .NET). This communication manipulates the process of knowing the state of each other

+ in examples 1 and 2 we get abstraction in cost of performance

+ collaborating well in specific cases with:

	--> composite trees and factory method (examples 3 and 4)
	--> flyweight for the leaf values

*pros*

+ abstracts the details of the underlying data structure of a collection

+ a standard way of iterating over different types of collections

*cons*

+ a standard way of iterating over different types of collections

***

# Example 01

```c++
template <typename T>
class BST {
    struct Node {
        T data;
        Node* left;
        Node* right;
        Node(T val) : data(val), left(nullptr), right(nullptr) {}
    };

    Node* root;

public:
    BST() : root(nullptr) {}

    void insert(T value) {
        root = insertRec(root, value);
    }

    class BFSIterator {
    private:
        std::queue<Node*> nodeQueue;

        BFSIterator(Node* root) {
            if (root) {
                nodeQueue.push(root);
            }
        }

        friend class BST<T>;

    public:
        bool hasNext() {
            return !nodeQueue.empty();
        }

        T next() {
            if (nodeQueue.empty()) {
                throw std::out_of_range("No more elements");
            }
            Node* current = nodeQueue.front();
            nodeQueue.pop();
            if (current->left) nodeQueue.push(current->left);
            if (current->right) nodeQueue.push(current->right);
            return current->data;
        }
    };

    class DFSIterator {
    private:
        std::stack<Node*> nodeStack;

        DFSIterator(Node* root) {
            if (root) {
                nodeStack.push(root);
            }
        }

        friend class BST<T>;

    public:
        bool hasNext() {
            return !nodeStack.empty();
        }

        T next() {
            if (nodeStack.empty()) {
                throw std::out_of_range("No more elements");
            }
            Node* current = nodeStack.top();
            nodeStack.pop();
            if (current->right) nodeStack.push(current->right);
            if (current->left) nodeStack.push(current->left);
            return current->data;
        }
    };

    BFSIterator beginBFS() {
        return BFSIterator(root);
    }

    DFSIterator beginDFS() {
        return DFSIterator(root);
    }

private:
    Node* insertRec(Node* node, T value) {
        if (!node) {
            return new Node(value);
        }
        if (value < node->data) {
            node->left = insertRec(node->left, value);
        }
        else {
            node->right = insertRec(node->right, value);
        }
        return node;
    }
};

int main() {
    BST<int> tree;
    tree.insert(5);
    tree.insert(3);
    tree.insert(7);

    std::cout << "BFS Traversal: ";
    for (auto it = tree.beginBFS(); it.hasNext(); ) {
        std::cout << it.next() << " ";
    }
    std::cout << "\n";

    std::cout << "DFS Traversal: ";
    for (auto it = tree.beginDFS(); it.hasNext(); ) {
        std::cout << it.next() << " ";
    }
    std::cout << "\n";

    return 0;
}
```

# Example 02

```java
public class BST<T extends Comparable<T>> {
    private class Node {
        T data;
        Node left, right;

        Node(T data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    private Node root;

    public BST() {
        this.root = null;
    }

    public void insert(T value) {
        root = insertRec(root, value);
    }

    private Node insertRec(Node node, T value) {
        if (node == null) {
            return new Node(value);
        }
        if (value.compareTo(node.data) < 0) {
            node.left = insertRec(node.left, value);
        }
        else if (value.compareTo(node.data) > 0) {
            node.right = insertRec(node.right, value);
        }
        return node;
    }

    public BFSIterator beginBFS() {
        return new BFSIterator(root);
    }

    public class BFSIterator {
        private Queue<Node> nodeQueue;

        BFSIterator(Node root) {
            nodeQueue = new LinkedList<>();
            if (root != null) {
                nodeQueue.add(root);
            }
        }

        public boolean hasNext() {
            return !nodeQueue.isEmpty();
        }

        public T next() {
            if (!hasNext()) {
                throw new RuntimeException("No more elements");
            }
            Node current = nodeQueue.remove();
            if (current.left != null) nodeQueue.add(current.left);
            if (current.right != null) nodeQueue.add(current.right);
            return current.data;
        }
    }

    public DFSIterator beginDFS() {
        return new DFSIterator(root);
    }

    public class DFSIterator {
        private Stack<Node> nodeStack;

        DFSIterator(Node root) {
            nodeStack = new Stack<>();
            if (root != null) {
                nodeStack.push(root);
            }
        }

        public boolean hasNext() {
            return !nodeStack.isEmpty();
        }

        public T next() {
            if (!hasNext()) {
                throw new RuntimeException("No more elements");
            }
            Node current = nodeStack.pop();
            if (current.right != null) nodeStack.push(current.right);
            if (current.left != null) nodeStack.push(current.left);
            return current.data;
        }
    }

    public static void main(String[] args) {
        BST<Integer> tree = new BST<>();
        tree.insert(5);
        tree.insert(3);
        tree.insert(7);

        System.out.print("BFS Traversal: ");
        for (BFSIterator it = tree.beginBFS(); it.hasNext(); ) {
            System.out.print(it.next() + " ");
        }
        System.out.println();

        System.out.print("DFS Traversal: ");
        for (DFSIterator it = tree.beginDFS(); it.hasNext(); ) {
            System.out.print(it.next() + " ");
        }
        System.out.println();
    }
}
```

# Example 03 (with Composite and factory method)

```c++
// Abstract Component
class ExpressionComponent {
public:
    virtual double evaluate() const = 0;
    virtual ~ExpressionComponent() {}
};

// Literal (Leaf)
class Literal : public ExpressionComponent {
private:
    double value;

public:
    explicit Literal(double val) : value(val) {}

    double evaluate() const override {
        return value;
    }
};

// Forward declaration
class ExpressionIterator;

// Sequence (Composite)
class Sequence : public ExpressionComponent {
private:
    std::vector<std::shared_ptr<ExpressionComponent>> children;
    char operation; // '+' for addition, '*' for multiplication

public:
    explicit Sequence(char op) : operation(op) {}

    void add(const std::shared_ptr<ExpressionComponent>& component) {
        children.push_back(component);
    }

    double evaluate() const override {
        double result = (operation == '+') ? 0 : 1;
        for (const auto& child : children) {
            result = (operation == '+') ? (result + child->evaluate()) : (result * child->evaluate());
        }
        return result;
    }

    ExpressionIterator begin() const;
    ExpressionIterator end() const;

    friend class ExpressionIterator;
};

// Iterator
class ExpressionIterator {
private:
    const Sequence& sequence;
    size_t index;

public:
    ExpressionIterator(const Sequence& seq, size_t idx) : sequence(seq), index(idx) {}

    bool operator!=(const ExpressionIterator& other) const {
        return index != other.index;
    }

    const ExpressionComponent& operator*() const {
        return *(sequence.children[index]);
    }

    ExpressionIterator& operator++() {
        index++;
        return *this;
    }
};

ExpressionIterator Sequence::begin() const {
    return ExpressionIterator(*this, 0);
}

ExpressionIterator Sequence::end() const {
    return ExpressionIterator(*this, children.size());
}

int main() {
    // Building the expression (3 + 4) * 2
    auto sequence = std::make_shared<Sequence>('*');
    auto addition = std::make_shared<Sequence>('+');
    addition->add(std::make_shared<Literal>(3));
    addition->add(std::make_shared<Literal>(4));
    sequence->add(addition);
    sequence->add(std::make_shared<Literal>(2));

    std::cout << "Result: " << sequence->evaluate() << std::endl;

    // Iterating over sequence
    std::cout << "Iterating over sequence: ";
    for (auto it = sequence->begin(); it != sequence->end(); ++it) {
        std::cout << (*it).evaluate() << " ";
    }
    std::cout << std::endl;

    return 0;
}
```

# Example 04 (with Composite and factory method)

```java
// Component
interface FileSystemComponent {
    String getName();
    Iterator<FileSystemComponent> createIterator(); // factory method
}

// Leaf
class File implements FileSystemComponent {
    private String name;

    public File(String name) {
        this.name = name;
    }

    @Override
        public String getName() {
        return name;
    }

    @Override
        public Iterator<FileSystemComponent> createIterator() {
        return new NullIterator();
    }
}

// Composite
class Directory implements FileSystemComponent {
    private String name;
    private List<FileSystemComponent> children = new ArrayList<>();

    public Directory(String name) {
        this.name = name;
    }

    public void add(FileSystemComponent component) {
        children.add(component);
    }

    @Override
        public String getName() {
        return name;
    }

    @Override
        public Iterator<FileSystemComponent> createIterator() {
        return children.iterator();
    }
}

// Null Iterator for Leaves
class NullIterator implements Iterator<FileSystemComponent> {
    @Override
        public boolean hasNext() {
        return false;
    }

    @Override
        public FileSystemComponent next() {
        return null;
    }
}

public class FileSystemDemo {
    public static void main(String[] args) {
        Directory root = new Directory("root");
        Directory folder1 = new Directory("folder1");
        File file1 = new File("file1.txt");
        File file2 = new File("file2.txt");

        root.add(folder1);
        folder1.add(file1);
        folder1.add(file2);

        printFileSystem(root);
    }

    private static void printFileSystem(FileSystemComponent component) {
        System.out.println(component.getName());
        Iterator<FileSystemComponent> iterator = component.createIterator();
        while (iterator.hasNext()) {
            FileSystemComponent child = iterator.next();
            printFileSystem(child);
        }
    }
}
```