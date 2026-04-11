# SOLID Principles — Java Reference

## Table of Contents
1. [Single Responsibility Principle (SRP)](#single-responsibility-principle)
2. [Open/Closed Principle (OCP)](#openclosed-principle)
3. [Liskov Substitution Principle (LSP)](#liskov-substitution-principle)
4. [Interface Segregation Principle (ISP)](#interface-segregation-principle)
5. [Dependency Inversion Principle (DIP)](#dependency-inversion-principle)

---

## Single Responsibility Principle

**A class should have only one reason to change.**

Each class should focus on a single concern. When a class handles multiple responsibilities, changes to 
one responsibility risk breaking the others.

### Violation

```java
// This class has THREE reasons to change:
// 1. Order business rules change
// 2. Persistence mechanism changes  
// 3. Notification format changes
public class OrderService {
    
    public Order createOrder(Customer customer, List<Item> items) {
        // Business logic: validate, calculate totals, apply discounts
        Order order = new Order(customer, items);
        order.setTotal(calculateTotal(items));
        order.applyDiscount(customer.getLoyaltyLevel());
        
        // Persistence: save to database
        Connection conn = DriverManager.getConnection(DB_URL);
        PreparedStatement stmt = conn.prepareStatement(
            "INSERT INTO orders (customer_id, total) VALUES (?, ?)");
        stmt.setLong(1, customer.getId());
        stmt.setBigDecimal(2, order.getTotal());
        stmt.executeUpdate();
        
        // Notification: send confirmation email
        String emailBody = "<html><body><h1>Order Confirmed</h1>" +
            "<p>Total: $" + order.getTotal() + "</p></body></html>";
        sendEmail(customer.getEmail(), "Order Confirmation", emailBody);
        
        return order;
    }
}
```

### Correct

```java
// Each class has exactly one reason to change

public class OrderService {
    private final OrderRepository repository;
    private final OrderNotifier notifier;
    
    public OrderService(OrderRepository repository, OrderNotifier notifier) {
        this.repository = repository;
        this.notifier = notifier;
    }
    
    public Order createOrder(Customer customer, List<Item> items) {
        Order order = new Order(customer, items);
        order.setTotal(calculateTotal(items));
        order.applyDiscount(customer.getLoyaltyLevel());
        
        repository.save(order);
        notifier.sendConfirmation(order);
        
        return order;
    }
}

public class OrderRepository {
    public void save(Order order) { /* persistence logic */ }
    public Optional<Order> findById(long id) { /* query logic */ }
}

public class OrderNotifier {
    public void sendConfirmation(Order order) { /* notification logic */ }
}
```

---

## Open/Closed Principle

**Software entities should be open for extension, but closed for modification.**

You should be able to add new behavior without changing existing code. Use polymorphism and abstractions 
instead of conditionals that must be edited every time a new case appears.

### Violation

```java
// Every new payment method requires editing this class
public class PaymentProcessor {
    
    public void processPayment(Payment payment) {
        switch (payment.getType()) {
            case CREDIT_CARD:
                processCreditCard(payment);
                break;
            case PAYPAL:
                processPayPal(payment);
                break;
            case BANK_TRANSFER:
                processBankTransfer(payment);
                break;
            // Adding Bitcoin? Edit this class again...
            default:
                throw new IllegalArgumentException("Unknown payment type");
        }
    }
    
    private void processCreditCard(Payment payment) { /* ... */ }
    private void processPayPal(Payment payment) { /* ... */ }
    private void processBankTransfer(Payment payment) { /* ... */ }
}
```

### Correct

```java
// New payment methods = new classes, no existing code changes

public interface PaymentHandler {
    boolean supports(PaymentType type);
    PaymentResult process(Payment payment);
}

public class CreditCardHandler implements PaymentHandler {
    @Override
    public boolean supports(PaymentType type) {
        return type == PaymentType.CREDIT_CARD;
    }
    
    @Override
    public PaymentResult process(Payment payment) {
        // Credit card processing logic
        return new PaymentResult(true, "Charged card ending in " + last4Digits);
    }
}

public class PayPalHandler implements PaymentHandler {
    @Override
    public boolean supports(PaymentType type) {
        return type == PaymentType.PAYPAL;
    }
    
    @Override
    public PaymentResult process(Payment payment) {
        // PayPal processing logic
        return new PaymentResult(true, "PayPal payment confirmed");
    }
}

// Adding Bitcoin? Just create a new BitcoinHandler — nothing else changes

public class PaymentProcessor {
    private final List<PaymentHandler> handlers;
    
    public PaymentProcessor(List<PaymentHandler> handlers) {
        this.handlers = handlers;
    }
    
    public PaymentResult processPayment(Payment payment) {
        return handlers.stream()
            .filter(h -> h.supports(payment.getType()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "No handler for " + payment.getType()))
            .process(payment);
    }
}
```

---

## Liskov Substitution Principle

**Subtypes must be substitutable for their base types without altering correctness.**

If code works with a base type, it must also work correctly with any subtype. Violations occur when 
subclasses break the expectations (preconditions, postconditions, invariants) established by the parent.

### Violation

```java
// Classic Rectangle-Square problem
public class Rectangle {
    protected int width;
    protected int height;
    
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public int getArea() { return width * height; }
}

public class Square extends Rectangle {
    // Square overrides to maintain the square invariant
    // but this BREAKS code that expects Rectangle behavior
    @Override
    public void setWidth(int width) {
        this.width = width;
        this.height = width; // Surprise! Also changes height
    }
    
    @Override
    public void setHeight(int height) {
        this.width = height; // Surprise! Also changes width
        this.height = height;
    }
}

// This test passes for Rectangle but FAILS for Square
void testArea(Rectangle rect) {
    rect.setWidth(5);
    rect.setHeight(4);
    assert rect.getArea() == 20; // Square returns 16!
}
```

### Correct

```java
// Model by behavior, not by real-world taxonomy

public interface Shape {
    int getArea();
}

public final class Rectangle implements Shape {
    private final int width;
    private final int height;
    
    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public int getArea() { return width * height; }
    
    public Rectangle withWidth(int width) { return new Rectangle(width, this.height); }
    public Rectangle withHeight(int height) { return new Rectangle(this.width, height); }
}

public final class Square implements Shape {
    private final int side;
    
    public Square(int side) { this.side = side; }
    
    @Override
    public int getArea() { return side * side; }
    
    public Square withSide(int side) { return new Square(side); }
}
```

### Another common violation: throwing from inherited methods

```java
// Violation: subclass refuses to do what parent promised
public class ReadOnlyList<E> extends ArrayList<E> {
    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException(); // Breaks the contract!
    }
}

// Correct: use composition and a restricted interface
public final class ReadOnlyList<E> implements Iterable<E> {
    private final List<E> delegate;
    
    public ReadOnlyList(List<E> source) {
        this.delegate = List.copyOf(source);
    }
    
    public E get(int index) { return delegate.get(index); }
    public int size() { return delegate.size(); }
    
    @Override
    public Iterator<E> iterator() { return delegate.iterator(); }
}
```

---

## Interface Segregation Principle

**No client should be forced to depend on methods it does not use.**

Split large interfaces into smaller, more specific ones so that implementing classes only need to 
know about the methods they actually use.

### Violation

```java
// Forced to implement methods that make no sense
public interface MultiFunctionDevice {
    void print(Document doc);
    void scan(Document doc);
    void fax(Document doc);
    void staple(Document doc);
}

// A simple printer is forced to implement scan, fax, and staple
public class SimplePrinter implements MultiFunctionDevice {
    @Override
    public void print(Document doc) { /* works */ }
    
    @Override
    public void scan(Document doc) {
        throw new UnsupportedOperationException("Can't scan"); // LSP violation too!
    }
    
    @Override
    public void fax(Document doc) {
        throw new UnsupportedOperationException("Can't fax");
    }
    
    @Override
    public void staple(Document doc) {
        throw new UnsupportedOperationException("Can't staple");
    }
}
```

### Correct

```java
// Small, focused interfaces — implement only what you support

public interface Printer {
    void print(Document doc);
}

public interface Scanner {
    Document scan();
}

public interface Fax {
    void fax(Document doc, String number);
}

// Simple printer only implements what it can do
public class SimplePrinter implements Printer {
    @Override
    public void print(Document doc) { /* printing logic */ }
}

// A multifunction device composes the interfaces it supports
public class OfficePrinter implements Printer, Scanner, Fax {
    @Override
    public void print(Document doc) { /* ... */ }
    
    @Override
    public Document scan() { /* ... */ }
    
    @Override
    public void fax(Document doc, String number) { /* ... */ }
}
```

---

## Dependency Inversion Principle

**High-level modules should not depend on low-level modules. Both should depend on abstractions.**

The business logic should define the interfaces it needs, and the infrastructure layer should 
implement those interfaces. This inverts the traditional dependency direction.

### Violation

```java
// High-level OrderService directly depends on low-level MySqlOrderDao
public class OrderService {
    // Tightly coupled to MySQL — can't switch to Postgres, MongoDB, or test with a fake
    private final MySqlOrderDao dao = new MySqlOrderDao();
    
    public void placeOrder(Order order) {
        // Business logic...
        dao.save(order);
    }
}

public class MySqlOrderDao {
    public void save(Order order) {
        // MySQL-specific SQL
    }
}
```

### Correct

```java
// The high-level module defines the abstraction it needs

public interface OrderRepository {
    void save(Order order);
    Optional<Order> findById(long id);
    List<Order> findByCustomer(long customerId);
}

// High-level module depends on the abstraction
public class OrderService {
    private final OrderRepository repository;
    
    // Dependency is injected — OrderService doesn't know or care about the implementation
    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }
    
    public void placeOrder(Order order) {
        // Business logic...
        repository.save(order);
    }
}

// Low-level module implements the abstraction
public class JpaOrderRepository implements OrderRepository {
    private final EntityManager em;
    
    public JpaOrderRepository(EntityManager em) {
        this.em = em;
    }
    
    @Override
    public void save(Order order) { em.persist(order); }
    
    @Override
    public Optional<Order> findById(long id) {
        return Optional.ofNullable(em.find(Order.class, id));
    }
    
    @Override
    public List<Order> findByCustomer(long customerId) {
        return em.createQuery(
            "SELECT o FROM Order o WHERE o.customer.id = :cid", Order.class)
            .setParameter("cid", customerId)
            .getResultList();
    }
}

// For testing: a simple in-memory implementation
public class InMemoryOrderRepository implements OrderRepository {
    private final Map<Long, Order> store = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong();
    
    @Override
    public void save(Order order) {
        if (order.getId() == 0) order.setId(idGen.incrementAndGet());
        store.put(order.getId(), order);
    }
    
    @Override
    public Optional<Order> findById(long id) {
        return Optional.ofNullable(store.get(id));
    }
    
    @Override
    public List<Order> findByCustomer(long customerId) {
        return store.values().stream()
            .filter(o -> o.getCustomer().getId() == customerId)
            .toList();
    }
}
```

---

## Principles Working Together

In well-designed code, the SOLID principles reinforce each other:

- **SRP + DIP**: Each class has one responsibility AND takes its dependencies through abstractions
- **OCP + LSP**: New behavior through new subtypes that are fully substitutable
- **ISP + DIP**: Small focused interfaces that both high-level and low-level modules depend on
- **SRP + OCP**: Single-responsibility classes are easier to extend without modification

The goal is not to satisfy each principle in isolation, but to write code where these principles 
naturally emerge from thoughtful design.
