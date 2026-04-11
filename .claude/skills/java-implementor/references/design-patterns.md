# OOP Design Patterns — Java Reference

All 23 Gang of Four (GoF) design patterns organized by category.
Based on examples from [eduardocruzpalacios/oop-design-patterns-java](https://github.com/eduardocruzpalacios/oop-design-patterns-java).

Consult this reference when deciding which pattern fits a design problem, or when implementing
a pattern to ensure you're following the standard structure.

## Table of Contents
1. [Creational Patterns](#creational-patterns) — Object creation mechanisms
2. [Structural Patterns](#structural-patterns) — Object composition and relationships
3. [Behavioral Patterns](#behavioral-patterns) — Object communication and responsibility
4. [Pattern Selection Guide](#pattern-selection-guide)

---

## Creational Patterns

Control how objects are created — replacing direct `new` calls with patterns that give more
flexibility, decoupling, and control.

### Singleton

**Intent**: Ensure a class has exactly one instance and provide a global access point.

**When to use**: Shared resources (connection pools, caches, configuration), service facades.

```java
// Preferred: enum singleton (Effective Java Item 3)
public enum AppConfig {
    INSTANCE;
    private final Properties props = loadProperties();
    public String get(String key) { return props.getProperty(key); }
}

// Alternative: lazy holder idiom (thread-safe, no synchronization overhead)
public class Registry {
    private Registry() {}
    private static class Holder {
        static final Registry INSTANCE = new Registry();
    }
    public static Registry getInstance() { return Holder.INSTANCE; }
}
```

**Caution**: Singletons make testing harder. Prefer dependency injection when possible (Effective Java Item 5).

---

### Factory Method

**Intent**: Define an interface for creating objects, letting subclasses decide which class to instantiate.

**When to use**: A class can't anticipate the exact type of objects it needs to create; creation logic
should be delegated to subclasses.

```java
// Creator (abstract)
public abstract class DocumentCreator {
    public abstract Document createDocument();

    // Template method using the factory method
    public Document openDocument(String path) {
        Document doc = createDocument();
        doc.load(path);
        return doc;
    }
}

// Concrete creator
public class PdfDocumentCreator extends DocumentCreator {
    @Override
    public Document createDocument() { return new PdfDocument(); }
}

public class SpreadsheetCreator extends DocumentCreator {
    @Override
    public Document createDocument() { return new Spreadsheet(); }
}
```

---

### Abstract Factory

**Intent**: Provide an interface for creating families of related objects without specifying concrete classes.

**When to use**: A system must work with multiple families of products that are designed to be used
together (UI themes, database providers, platform-specific implementations).

```java
// Abstract factory
public interface UIFactory {
    Button createButton();
    TextField createTextField();
    Dialog createDialog();
}

// Concrete factory — produces a consistent family
public class DarkThemeFactory implements UIFactory {
    @Override public Button createButton() { return new DarkButton(); }
    @Override public TextField createTextField() { return new DarkTextField(); }
    @Override public Dialog createDialog() { return new DarkDialog(); }
}

public class LightThemeFactory implements UIFactory {
    @Override public Button createButton() { return new LightButton(); }
    @Override public TextField createTextField() { return new LightTextField(); }
    @Override public Dialog createDialog() { return new LightDialog(); }
}

// Client depends only on the factory interface
public class Application {
    private final UIFactory uiFactory;
    public Application(UIFactory uiFactory) { this.uiFactory = uiFactory; }
}
```

---

### Builder

**Intent**: Separate the construction of a complex object from its representation, allowing the
same construction process to create different representations.

**When to use**: Objects with many optional parameters, step-by-step construction, or when the same
construction process should produce different representations.

```java
// Product
public class HttpRequest {
    private final String method;
    private final String url;
    private final Map<String, String> headers;
    private final String body;
    private final Duration timeout;

    private HttpRequest(Builder builder) {
        this.method = builder.method;
        this.url = builder.url;
        this.headers = Map.copyOf(builder.headers);
        this.body = builder.body;
        this.timeout = builder.timeout;
    }

    public static Builder builder(String method, String url) {
        return new Builder(method, url);
    }

    public static final class Builder {
        private final String method;
        private final String url;
        private final Map<String, String> headers = new LinkedHashMap<>();
        private String body;
        private Duration timeout = Duration.ofSeconds(30);

        private Builder(String method, String url) {
            this.method = Objects.requireNonNull(method);
            this.url = Objects.requireNonNull(url);
        }

        public Builder header(String name, String value) {
            headers.put(name, value); return this;
        }
        public Builder body(String body) { this.body = body; return this; }
        public Builder timeout(Duration timeout) { this.timeout = timeout; return this; }
        public HttpRequest build() { return new HttpRequest(this); }
    }
}

// Usage
HttpRequest request = HttpRequest.builder("POST", "/api/users")
    .header("Content-Type", "application/json")
    .body("{\"name\": \"Alice\"}")
    .timeout(Duration.ofSeconds(10))
    .build();
```

**Note**: Effective Java Item 2 recommends this pattern for 4+ constructor parameters.

---

### Prototype

**Intent**: Create new objects by copying an existing instance (the prototype) rather than constructing from scratch.

**When to use**: Object creation is expensive (DB queries, complex initialization), or you need
copies with small variations from a template.

```java
public interface Prototype<T> {
    T deepCopy();
}

public class DocumentTemplate implements Prototype<DocumentTemplate> {
    private String title;
    private List<Section> sections;
    private Formatting formatting;

    @Override
    public DocumentTemplate deepCopy() {
        DocumentTemplate copy = new DocumentTemplate();
        copy.title = this.title;
        copy.sections = this.sections.stream()
            .map(Section::deepCopy)
            .collect(Collectors.toList());
        copy.formatting = this.formatting.deepCopy();
        return copy;
    }
}
```

**Caution**: Always prefer deep copy over shallow copy to avoid shared mutable state.

---

## Structural Patterns

Describe how objects and classes are composed to form larger structures while keeping them
flexible and efficient.

### Adapter

**Intent**: Convert the interface of a class into another interface clients expect. Lets classes
work together that otherwise couldn't because of incompatible interfaces.

**When to use**: Integrating legacy code, third-party libraries, or systems with different interfaces.

```java
// Target interface (what the client expects)
public interface MediaPlayer {
    void play(String filename);
}

// Adaptee (incompatible interface)
public class VlcLibrary {
    public void playVlc(String path, boolean fullscreen) { /* ... */ }
}

// Adapter
public class VlcAdapter implements MediaPlayer {
    private final VlcLibrary vlc;

    public VlcAdapter(VlcLibrary vlc) { this.vlc = vlc; }

    @Override
    public void play(String filename) {
        vlc.playVlc(filename, false);
    }
}
```

---

### Bridge

**Intent**: Decouple an abstraction from its implementation so the two can vary independently.

**When to use**: When both the abstraction and implementation might have multiple variants, and you
want to avoid a combinatorial explosion of subclasses.

```java
// Implementor
public interface Renderer {
    void renderCircle(float radius);
    void renderRectangle(float width, float height);
}

// Abstraction
public abstract class Shape {
    protected final Renderer renderer;
    protected Shape(Renderer renderer) { this.renderer = renderer; }
    public abstract void draw();
}

// Refined abstraction
public class Circle extends Shape {
    private final float radius;
    public Circle(Renderer renderer, float radius) {
        super(renderer);
        this.radius = radius;
    }
    @Override
    public void draw() { renderer.renderCircle(radius); }
}

// Concrete implementors
public class SvgRenderer implements Renderer { /* SVG output */ }
public class OpenGLRenderer implements Renderer { /* OpenGL output */ }

// Any Shape × any Renderer — no class explosion
```

---

### Composite

**Intent**: Compose objects into tree structures to represent part-whole hierarchies. Clients
treat individual objects and compositions uniformly.

**When to use**: Tree structures (file systems, UI components, org charts, menu systems).

```java
public interface FileSystemNode {
    String getName();
    long getSize();
    void print(String indent);
}

public class File implements FileSystemNode {
    private final String name;
    private final long size;
    // ... simple leaf implementation
}

public class Directory implements FileSystemNode {
    private final String name;
    private final List<FileSystemNode> children = new ArrayList<>();

    public void add(FileSystemNode node) { children.add(node); }

    @Override
    public long getSize() {
        return children.stream().mapToLong(FileSystemNode::getSize).sum();
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + name + "/");
        children.forEach(child -> child.print(indent + "  "));
    }
}
```

---

### Decorator

**Intent**: Attach additional responsibilities to an object dynamically. A flexible alternative
to subclassing for extending functionality.

**When to use**: Adding behavior (logging, caching, validation, compression) without modifying
the original class. Especially powerful when behaviors can be composed.

```java
// Component interface
public interface DataSource {
    void writeData(byte[] data);
    byte[] readData();
}

// Concrete component
public class FileDataSource implements DataSource {
    private final String filename;
    // ... reads/writes to file
}

// Base decorator
public abstract class DataSourceDecorator implements DataSource {
    protected final DataSource wrapped;
    protected DataSourceDecorator(DataSource wrapped) { this.wrapped = wrapped; }
}

// Concrete decorators — composable
public class EncryptionDecorator extends DataSourceDecorator {
    @Override
    public void writeData(byte[] data) { wrapped.writeData(encrypt(data)); }
    @Override
    public byte[] readData() { return decrypt(wrapped.readData()); }
}

public class CompressionDecorator extends DataSourceDecorator {
    @Override
    public void writeData(byte[] data) { wrapped.writeData(compress(data)); }
    @Override
    public byte[] readData() { return decompress(wrapped.readData()); }
}

// Usage: stack decorators
DataSource source = new CompressionDecorator(
    new EncryptionDecorator(
        new FileDataSource("secret.dat")));
```

**Connection to Effective Java**: Item 18 — favor composition over inheritance. Decorators are the textbook example.

---

### Facade

**Intent**: Provide a unified, simplified interface to a set of interfaces in a subsystem.

**When to use**: Hiding complexity of a subsystem, providing a simple entry point for common operations.

```java
public class VideoConverter {
    // Hides the complexity of codec selection, bitrate calculation,
    // audio extraction, muxing, and file writing
    public File convert(String inputPath, String outputFormat) {
        VideoFile file = new VideoFile(inputPath);
        Codec codec = CodecFactory.extract(file);
        AudioMixer audioMixer = new AudioMixer();
        // ... complex subsystem interactions
        return new File(outputPath);
    }
}
```

---

### Flyweight

**Intent**: Use sharing to support large numbers of fine-grained objects efficiently by separating
intrinsic (shared) state from extrinsic (per-instance) state.

**When to use**: Many similar objects consuming too much memory (characters in a text editor, particles
in a game, tree objects in a forest renderer).

```java
// Flyweight — holds only intrinsic (shared) state
public class TreeType {
    private final String name;
    private final String texture;    // heavy — shared across all trees of this type
    private final Color color;

    public void draw(int x, int y) { /* render at position using shared texture */ }
}

// Flyweight factory — ensures sharing
public class TreeFactory {
    private static final Map<String, TreeType> types = new HashMap<>();

    public static TreeType getTreeType(String name, String texture, Color color) {
        return types.computeIfAbsent(name, k -> new TreeType(name, texture, color));
    }
}

// Context — holds extrinsic (per-instance) state
public class Tree {
    private final int x, y;              // unique per tree
    private final TreeType type;         // shared

    public void draw() { type.draw(x, y); }
}
```

---

### Proxy

**Intent**: Provide a surrogate or placeholder for another object to control access to it.

**When to use**: Lazy initialization (virtual proxy), access control (protection proxy), caching,
logging, remote access.

```java
// Subject interface
public interface ImageLoader {
    Image load(String path);
}

// Real subject — expensive
public class DiskImageLoader implements ImageLoader {
    @Override
    public Image load(String path) {
        // Expensive disk I/O
        return ImageIO.read(new File(path));
    }
}

// Proxy — adds caching
public class CachingImageProxy implements ImageLoader {
    private final ImageLoader delegate;
    private final Map<String, Image> cache = new ConcurrentHashMap<>();

    public CachingImageProxy(ImageLoader delegate) { this.delegate = delegate; }

    @Override
    public Image load(String path) {
        return cache.computeIfAbsent(path, delegate::load);
    }
}
```

---

## Behavioral Patterns

Define how objects interact, distribute responsibility, and communicate.

### Strategy

**Intent**: Define a family of algorithms, encapsulate each one, and make them interchangeable.

**When to use**: Multiple algorithms for the same task (sorting, compression, pricing, routing),
runtime algorithm selection, eliminating conditionals.

```java
public interface CompressionStrategy {
    byte[] compress(byte[] data);
    byte[] decompress(byte[] data);
}

public class GzipCompression implements CompressionStrategy { /* ... */ }
public class ZipCompression implements CompressionStrategy { /* ... */ }
public class LZ4Compression implements CompressionStrategy { /* ... */ }

public class FileArchiver {
    private final CompressionStrategy strategy;

    public FileArchiver(CompressionStrategy strategy) {
        this.strategy = strategy;
    }

    public byte[] archive(byte[] data) {
        return strategy.compress(data);
    }
}
```

**Connection to SOLID**: This is the canonical OCP solution — new algorithms = new classes, no modification.

---

### Observer

**Intent**: Define a one-to-many dependency between objects so that when one changes state,
all dependents are notified automatically.

**When to use**: Event systems, UI data binding, pub/sub messaging, reactive programming.

```java
public interface EventListener<T> {
    void onEvent(T event);
}

public class EventBus<T> {
    private final List<EventListener<T>> listeners = new CopyOnWriteArrayList<>();

    public void subscribe(EventListener<T> listener) { listeners.add(listener); }
    public void unsubscribe(EventListener<T> listener) { listeners.remove(listener); }

    public void publish(T event) {
        listeners.forEach(listener -> listener.onEvent(event));
    }
}
```

---

### Command

**Intent**: Encapsulate a request as an object, allowing parameterization, queueing, logging,
and undo/redo support.

**When to use**: Undo/redo, task queues, macro recording, callback mechanisms.

```java
public interface Command {
    void execute();
    void undo();
}

public class MoveCommand implements Command {
    private final Editor editor;
    private final int dx, dy;
    private int prevX, prevY;

    @Override
    public void execute() {
        prevX = editor.getCursorX();
        prevY = editor.getCursorY();
        editor.moveCursor(dx, dy);
    }

    @Override
    public void undo() {
        editor.setCursor(prevX, prevY);
    }
}

public class CommandHistory {
    private final Deque<Command> history = new ArrayDeque<>();

    public void execute(Command cmd) {
        cmd.execute();
        history.push(cmd);
    }

    public void undo() {
        if (!history.isEmpty()) history.pop().undo();
    }
}
```

---

### Template Method

**Intent**: Define the skeleton of an algorithm in a method, deferring some steps to subclasses.

**When to use**: Common algorithm structure with varying steps (data processing pipelines,
game loops, document generation).

```java
public abstract class DataProcessor {
    // Template method — defines the algorithm skeleton
    public final void process() {
        DataSource source = openSource();
        List<Record> raw = readData(source);
        List<Record> cleaned = cleanData(raw);
        List<Record> transformed = transformData(cleaned);
        writeResults(transformed);
        closeSource(source);
    }

    protected abstract DataSource openSource();
    protected abstract List<Record> readData(DataSource source);
    protected List<Record> cleanData(List<Record> data) { return data; }  // hook — optional override
    protected abstract List<Record> transformData(List<Record> data);
    protected abstract void writeResults(List<Record> results);
    protected void closeSource(DataSource source) { source.close(); }     // hook
}
```

---

### State

**Intent**: Allow an object to alter its behavior when its internal state changes. The object
will appear to change its class.

**When to use**: Objects whose behavior depends on state (order status, connection state,
document workflow), replacing complex state-dependent conditionals.

```java
public interface OrderState {
    void next(Order order);
    void cancel(Order order);
    String getStatus();
}

public class PendingState implements OrderState {
    @Override
    public void next(Order order) { order.setState(new ProcessingState()); }
    @Override
    public void cancel(Order order) { order.setState(new CancelledState()); }
    @Override
    public String getStatus() { return "PENDING"; }
}

public class ProcessingState implements OrderState {
    @Override
    public void next(Order order) { order.setState(new ShippedState()); }
    @Override
    public void cancel(Order order) {
        throw new IllegalStateException("Cannot cancel order in processing");
    }
    @Override
    public String getStatus() { return "PROCESSING"; }
}

public class Order {
    private OrderState state = new PendingState();
    public void setState(OrderState state) { this.state = state; }
    public void proceed() { state.next(this); }
    public void cancel() { state.cancel(this); }
}
```

---

### Chain of Responsibility

**Intent**: Avoid coupling the sender of a request to its receiver by giving more than one object
a chance to handle the request. Chain the receiving objects and pass the request along.

**When to use**: Middleware pipelines, validation chains, event handling, logging levels.

```java
public abstract class Handler<T> {
    private Handler<T> next;

    public Handler<T> setNext(Handler<T> next) {
        this.next = next;
        return next;
    }

    public void handle(T request) {
        if (canHandle(request)) {
            doHandle(request);
        } else if (next != null) {
            next.handle(request);
        }
    }

    protected abstract boolean canHandle(T request);
    protected abstract void doHandle(T request);
}
```

---

### Mediator

**Intent**: Define an object that encapsulates how a set of objects interact, promoting loose
coupling by preventing objects from referring to each other directly.

**When to use**: Complex communication between multiple UI components, chat rooms, air traffic control.

```java
public interface ChatMediator {
    void sendMessage(String message, User sender);
    void addUser(User user);
}

public class ChatRoom implements ChatMediator {
    private final List<User> users = new ArrayList<>();

    @Override
    public void sendMessage(String message, User sender) {
        users.stream()
            .filter(u -> u != sender)
            .forEach(u -> u.receive(message, sender.getName()));
    }
}
```

---

### Visitor

**Intent**: Represent an operation to be performed on elements of an object structure. Define
a new operation without changing the classes of the elements it operates on.

**When to use**: Adding operations to a stable class hierarchy (tax calculation, export formats,
syntax tree analysis) without modifying those classes.

```java
public interface ShapeVisitor {
    double visit(Circle circle);
    double visit(Rectangle rectangle);
    double visit(Triangle triangle);
}

public interface Shape {
    double accept(ShapeVisitor visitor);
}

public class AreaCalculator implements ShapeVisitor {
    @Override public double visit(Circle c) { return Math.PI * c.radius() * c.radius(); }
    @Override public double visit(Rectangle r) { return r.width() * r.height(); }
    @Override public double visit(Triangle t) { return 0.5 * t.base() * t.height(); }
}

public class PerimeterCalculator implements ShapeVisitor {
    // Different operation, same element hierarchy — no changes to Shape classes
}
```

---

### Memento

**Intent**: Capture and externalize an object's internal state so it can be restored later,
without violating encapsulation.

**When to use**: Undo/redo, checkpointing, transaction rollback.

```java
public record EditorMemento(String content, int cursorPosition) {}

public class TextEditor {
    private String content;
    private int cursorPosition;

    public EditorMemento save() {
        return new EditorMemento(content, cursorPosition);
    }

    public void restore(EditorMemento memento) {
        this.content = memento.content();
        this.cursorPosition = memento.cursorPosition();
    }
}
```

---

### Iterator

**Intent**: Provide a way to access elements of a collection sequentially without exposing
the underlying representation.

**When to use**: Custom collections, traversing complex structures (trees, graphs) with a
uniform interface.

```java
public interface TreeIterator<T> extends Iterator<T> {
    // Standard Iterator contract: hasNext(), next()
}

// In-order, pre-order, and post-order iterators can be swapped
// without changing client code
public class BinaryTree<T> implements Iterable<T> {
    @Override
    public Iterator<T> iterator() { return new InOrderIterator<>(root); }
    public Iterator<T> preOrder() { return new PreOrderIterator<>(root); }
}
```

---

### Interpreter

**Intent**: Define a grammar for a language and provide an interpreter that uses the grammar
to interpret sentences in the language.

**When to use**: Simple DSLs, expression evaluation, query parsing, rule engines.

---

## Pattern Selection Guide

Use this quick reference to pick the right pattern for common design problems:

| Problem | Consider |
|---------|----------|
| Object creation is complex or has many optional params | **Builder** |
| Need to create families of related objects | **Abstract Factory** |
| Need exactly one instance | **Singleton** (prefer enum or DI) |
| Subclasses should decide what to create | **Factory Method** |
| Need copies of an expensive object | **Prototype** |
| Incompatible interfaces need to work together | **Adapter** |
| Want to add behavior without modifying a class | **Decorator** |
| Complex subsystem needs a simpler API | **Facade** |
| Part-whole tree hierarchies | **Composite** |
| Many similar objects consuming too much memory | **Flyweight** |
| Control access to an object (cache, lazy load, security) | **Proxy** |
| Abstraction and implementation vary independently | **Bridge** |
| Multiple algorithms for the same task, swappable at runtime | **Strategy** |
| Object behavior changes based on state | **State** |
| Notify multiple objects when something changes | **Observer** |
| Encapsulate operations for undo/redo/queuing | **Command** |
| Common algorithm with varying steps | **Template Method** |
| Request should be handled by one of many handlers | **Chain of Responsibility** |
| Add operations to a class hierarchy without modifying it | **Visitor** |
| Complex interactions between many objects | **Mediator** |
| Save/restore object state | **Memento** |
| Sequential access to a collection's elements | **Iterator** |
| Interpret a simple language or expressions | **Interpreter** |

### Patterns and SOLID

| Pattern | Primary SOLID Principle |
|---------|----------------------|
| Strategy, State, Observer | **OCP** — new behavior via new classes |
| Adapter, Decorator, Proxy | **OCP + ISP** — extend without modifying |
| Factory Method, Abstract Factory, Builder | **DIP** — depend on abstractions for creation |
| Template Method | **DIP + OCP** — algorithm skeleton depends on abstract steps |
| Composite | **LSP** — leaf and composite treated uniformly |
| Chain of Responsibility | **SRP** — each handler has one concern |
| Command | **SRP** — each command encapsulates one action |
| Facade | **ISP** — simplified interface hides complexity |
