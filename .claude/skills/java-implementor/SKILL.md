---
name: java-implementor
description: >
  Contains specialized Java implementation knowledge: all 90 Effective Java items (Joshua Bloch), 
  SOLID principles with violation/solution examples, and all 23 GoF design patterns with Java code. 
  This skill provides a design-first framework, pattern selection guide, and implementation checklists 
  that produce measurably better code than working without it. ALWAYS use this skill when the user asks 
  to write, create, implement, design, architect, or refactor Java code — including services, classes, 
  APIs, data models, enums, interfaces, or any Java component. Also trigger for: refactoring large or 
  messy Java classes; designing extensible systems (strategy, observer, decorator, factory patterns); 
  converting mutable code to immutable; applying SOLID, clean code, or Effective Java principles; 
  building thread-safe components; choosing between design patterns. Trigger even for seemingly simple 
  requests like "implement X in Java" or "create a class for Y" — the skill's guidance on object 
  creation, API design, and error handling improves every Java implementation.
---

# Java Implementor

You write Java code that is correct, clear, efficient, and maintainable by applying Effective Java 
principles, SOLID design, and proven GoF design patterns. This is not a checklist to mechanically 
follow — it's a thinking framework that helps you make good design decisions for each specific situation.

## Design-First Mindset

Before writing any code, pause and think about the design. The few minutes spent considering 
responsibilities, abstractions, and extension points will save hours of refactoring later.

### The Five Questions (SOLID)

Ask these before creating any class or interface:

1. **What is this class's single reason to change?** (Single Responsibility)
   - If you can describe the class's purpose with "and" in it, it probably does too much
   - A `UserService` that handles registration AND email sending AND password reset has three reasons to change

2. **How will this be extended without modification?** (Open/Closed)
   - Use strategy patterns, plugin architectures, or polymorphism instead of switch/if-else chains
   - New behavior should mean new classes, not editing existing ones

3. **Can subtypes be used interchangeably with their parent?** (Liskov Substitution)
   - If `Square extends Rectangle`, can you use a `Square` everywhere a `Rectangle` is expected without surprises?
   - Violations manifest as `instanceof` checks or unexpected behavior from overridden methods

4. **Am I forcing clients to depend on methods they don't use?** (Interface Segregation)
   - Prefer many small, focused interfaces over one large one
   - A `Printer` interface shouldn't force implementing `scan()` and `fax()`

5. **Am I depending on abstractions or concretions?** (Dependency Inversion)
   - High-level modules should not import low-level modules — both should depend on interfaces
   - Pass dependencies in (constructor injection) rather than creating them internally

### When NOT to Over-Apply

SOLID is a guide, not a religion. Don't create abstractions for things that have exactly one implementation 
and no realistic prospect of a second. Don't split a 30-line class into 5 classes just to satisfy SRP 
on paper. Three similar lines are better than a premature abstraction.

## Effective Java Quick Reference

These are the highest-impact items to consider during implementation. Consult 
`references/effective-java.md` for the complete list when doing deeper design work.

### Object Creation (Items 1-9)

- **Prefer static factory methods** over constructors — they have names, can return subtypes, 
  can cache instances (`of`, `valueOf`, `from`, `create`, `getInstance`)
- **Use builders** when constructors would have 4+ parameters — especially with optional params
- **Enforce singletons** with a private constructor or an enum type
- **Use dependency injection** — don't hardwire resources. Pass collaborators through the constructor
- **Prefer try-with-resources** over try-finally for anything `AutoCloseable`

### Classes and Interfaces (Items 15-25)

- **Minimize accessibility** — make everything as private as possible. Start private, open up only when needed
- **Use accessor methods** for public classes, not public fields
- **Minimize mutability** — make classes immutable where practical. Use `final` fields, no setters, 
  defensive copies. Immutable objects are simple, thread-safe, and shareable
- **Favor composition over inheritance** — inheritance breaks encapsulation. Use a forwarding wrapper 
  instead unless there's a genuine "is-a" relationship
- **Design interfaces for posterity** — think carefully before adding default methods; they can break 
  existing implementations in subtle ways
- **Use interfaces for type definitions only** — not for constants (use a utility class or enum instead)

### Generics, Enums, Lambdas (Items 26-48)

- **Don't use raw types** — use `List<String>`, never just `List`
- **Use bounded wildcards** to increase API flexibility — `<? extends T>` for producers, `<? super T>` for consumers (PECS)
- **Use enums instead of int constants** — they're type-safe, have namespaces, and can have behavior
- **Use `EnumSet`/`EnumMap`** instead of bit fields or ordinal indexing
- **Prefer lambdas to anonymous classes** — but keep them short (1-3 lines)
- **Prefer method references** when they're clearer than lambdas
- **Use streams judiciously** — they're powerful but can harm readability if overused

### Methods and General Programming (Items 49-68)

- **Check parameters for validity** at the start of public methods — fail fast with clear exceptions
- **Make defensive copies** of mutable parameters and return values when needed
- **Design method signatures carefully** — avoid long parameter lists, prefer interfaces over classes 
  as parameter types, use two-element enums over booleans
- **Return empty collections/optionals**, not null — `Collections.emptyList()`, `Optional.empty()`
- **Use `Optional`** only for return values, never for fields or method parameters
- **Refer to objects by interfaces** — `List<String>` not `ArrayList<String>`
- **Prefer primitive types** to boxed primitives — avoid unintentional auto-boxing

### Exceptions (Items 69-77)

- **Use exceptions for exceptional conditions only** — not for control flow
- **Use checked exceptions for recoverable conditions** and runtime exceptions for programming errors
- **Favor standard exceptions** — `IllegalArgumentException`, `IllegalStateException`, 
  `NullPointerException`, `UnsupportedOperationException`
- **Include failure-capture information** in exception messages — what values caused the failure

### Concurrency (Items 78-84)

- **Synchronize access to shared mutable data** — or better, avoid shared mutable state entirely
- **Prefer executors and tasks** over raw threads
- **Prefer concurrency utilities** (`ConcurrentHashMap`, `CountDownLatch`) over `wait`/`notify`

## Design Pattern Selection

When the design problem matches a known pattern, use it. Patterns are battle-tested solutions that
communicate intent to other developers. Consult `references/design-patterns.md` for full details
and Java code examples of all 23 GoF patterns.

### Quick Pattern Selector

**Object creation problems:**
- Many optional parameters → **Builder**
- Need to create families of related objects → **Abstract Factory**
- Subclasses decide what to create → **Factory Method**
- Expensive object, need copies → **Prototype**
- Exactly one instance → **Singleton** (prefer enum or DI)

**Structural problems:**
- Incompatible interfaces → **Adapter**
- Add behavior without modifying classes → **Decorator** (also supports Item 18: composition over inheritance)
- Simplify a complex subsystem → **Facade**
- Tree/part-whole hierarchies → **Composite**
- Control access (caching, lazy-load, auth) → **Proxy**
- Too many similar objects consuming memory → **Flyweight**
- Abstraction and implementation vary independently → **Bridge**

**Behavioral problems:**
- Multiple algorithms, swappable at runtime → **Strategy** (canonical OCP solution)
- Behavior changes based on object state → **State**
- Notify many objects of changes → **Observer**
- Undo/redo, queued operations → **Command**
- Common algorithm, varying steps → **Template Method**
- Request handled by one of many handlers → **Chain of Responsibility**
- Add operations without modifying a class hierarchy → **Visitor**
- Complex interactions between many objects → **Mediator**
- Save/restore object state → **Memento**

### When NOT to Use Patterns

Don't force a pattern where a simpler solution works. A three-line `if/else` doesn't need a 
Strategy pattern. A class with two fields doesn't need a Builder. Patterns add indirection — 
they should earn their complexity by solving a real design problem, not be applied for their 
own sake. The presence of a switch statement is not automatically a reason to refactor.

## Implementation Workflow

When implementing Java code, follow this sequence:

### 1. Analyze Requirements
- What is the core responsibility?
- What are the inputs, outputs, and side effects?
- What are the error cases?

### 2. Design the API
- Define interfaces first, then implementations
- Choose the right object creation pattern (constructor, factory, builder)
- Identify if any GoF patterns fit the problem naturally — don't force them, but recognize when they apply
- Decide on mutability — default to immutable unless there's a strong reason not to
- Identify dependencies that should be injected

### 3. Implement
- Start with the public API, then fill in private helpers
- Apply the Effective Java items relevant to the specific code being written
- Use descriptive naming — the code should read like well-written prose

### 4. Self-Review
After writing the code, quickly scan for:
- Unused generality (interfaces with one implementation, unused parameters)
- Mutable state that could be immutable
- `null` returns that should be `Optional` or empty collections
- Resource leaks (unclosed streams, connections)
- Missing input validation on public API boundaries

## Reference Files

- **`references/effective-java.md`** — Complete listing of all 90 Effective Java items with descriptions 
  and guidance. Read this when doing detailed design work or when you need to recall a specific item.
- **`references/solid-principles.md`** — SOLID principles with full Java code examples showing both 
  violations and correct implementations. Read this when designing class hierarchies or refactoring.
- **`references/design-patterns.md`** — All 23 GoF design patterns with Java code examples, when-to-use 
  guidance, and a pattern selection guide. Read this when the design problem matches a known pattern 
  or when the user asks about specific design patterns.
