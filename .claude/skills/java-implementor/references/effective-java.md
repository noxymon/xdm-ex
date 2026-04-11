# Effective Java — Complete Item Reference

Based on *Effective Java, 3rd Edition* by Joshua Bloch (2018). All 90 items organized by chapter.
Consult this reference when designing Java APIs, classes, and components.

## Table of Contents
1. [Creating and Destroying Objects (Items 1–9)](#chapter-2-creating-and-destroying-objects)
2. [Methods Common to All Objects (Items 10–14)](#chapter-3-methods-common-to-all-objects)
3. [Classes and Interfaces (Items 15–25)](#chapter-4-classes-and-interfaces)
4. [Generics (Items 26–33)](#chapter-5-generics)
5. [Enums and Annotations (Items 34–41)](#chapter-6-enums-and-annotations)
6. [Lambdas and Streams (Items 42–48)](#chapter-7-lambdas-and-streams)
7. [Methods (Items 49–56)](#chapter-8-methods)
8. [General Programming (Items 57–68)](#chapter-9-general-programming)
9. [Exceptions (Items 69–77)](#chapter-10-exceptions)
10. [Concurrency (Items 78–84)](#chapter-11-concurrency)
11. [Serialization (Items 85–90)](#chapter-12-serialization)

---

## Chapter 2: Creating and Destroying Objects

**Item 1: Consider static factory methods instead of constructors**
- Named factories reveal intent: `from`, `of`, `valueOf`, `instance`, `create`, `getType`, `newType`
- Can return subtypes, cached instances, or different implementations per input
- Drawback: classes without public constructors can't be subclassed (but this encourages composition)
- *Apply when*: you need descriptive creation, caching, or returning interface types

**Item 2: Consider a builder when faced with many constructor parameters**
- Telescoping constructors are hard to read; JavaBeans pattern (setters) allows inconsistent state
- Builders combine the safety of telescoping with the readability of JavaBeans
- Use `Builder` as a static inner class; each setter returns `this` for chaining
- *Apply when*: 4+ parameters, especially with optional ones

**Item 3: Enforce the singleton property with a private constructor or an enum type**
- Single-element enum is the best way for most singletons: concise, serialization-safe, thread-safe
- Alternative: private constructor + public static final field or static factory
- *Apply when*: exactly one instance should exist (service facades, stateless utilities)

**Item 4: Enforce noninstantiability with a private constructor**
- For utility classes (static methods only): private constructor + throw AssertionError
- *Apply when*: grouping static methods like `Collections`, `Math`, or project utilities

**Item 5: Prefer dependency injection to hardwiring resources**
- Pass resources into the constructor instead of having the class create them
- This enables testing, flexibility, and reuse
- For complex dependency graphs, use a DI framework (Spring, Guice, Dagger)
- *Apply when*: a class depends on an underlying resource whose behavior could vary

**Item 6: Avoid creating unnecessary objects**
- Reuse immutable objects (`Boolean.valueOf` over `new Boolean`)
- Watch for auto-boxing: `Long sum` in a tight loop creates billions of objects — use `long`
- Prefer `String` literals over `new String("...")`
- *Apply when*: performance-sensitive code or tight loops

**Item 7: Eliminate obsolete object references**
- Nulling out references is needed when a class manages its own memory (stacks, caches, pools)
- Use `WeakHashMap` for caches, `LinkedHashMap.removeEldestEntry` for bounded caches
- Don't forget listeners/callbacks — use weak references or explicit deregistration
- *Apply when*: custom data structures, caches, callback registries

**Item 8: Avoid finalizers and cleaners**
- They are unpredictable, slow, and dangerous. Never do anything time-critical in them
- Use `AutoCloseable` and try-with-resources instead
- *Apply*: essentially always avoid them. Use cleaners only as a safety net, never as primary cleanup

**Item 9: Prefer try-with-resources to try-finally**
- Cleaner, shorter, and preserves the original exception (try-finally can swallow it)
- Works with any `AutoCloseable`: streams, connections, readers
- *Apply when*: working with any resource that must be closed

---

## Chapter 3: Methods Common to All Objects

**Item 10: Obey the general contract when overriding equals**
- Must be reflexive, symmetric, transitive, consistent, and non-null
- Don't override if: each instance is unique, superclass equals works, or the class is private
- Use `instanceof` check, not `getClass()` — allows subtype comparison
- *Apply when*: value classes that need logical equality (not identity)

**Item 11: Always override hashCode when you override equals**
- Equal objects must have equal hash codes
- Use `Objects.hash()` for convenience or manual computation for performance
- *Apply*: always, whenever you override `equals`

**Item 12: Always override toString**
- Return a useful, human-readable representation
- Include all interesting information; document the format (or explicitly state it's unspecified)
- *Apply when*: any class that will be logged, debugged, or displayed

**Item 13: Override clone judiciously**
- `Cloneable` is broken by design. Prefer copy constructors or copy factory methods
- If you must implement `clone`, call `super.clone()` and deep-copy mutable fields
- *Apply*: rarely. Prefer `public Foo(Foo original)` or `public static Foo from(Foo original)`

**Item 14: Consider implementing Comparable**
- Enables natural ordering for sorted collections and arrays
- Use `Comparator.comparingInt()` and chaining for clean multi-field comparisons
- *Apply when*: the class has a natural ordering (dates, names, versions, priorities)

---

## Chapter 4: Classes and Interfaces

**Item 15: Minimize the accessibility of classes and members**
- Make everything as private as possible. Package-private is the default for top-level classes
- Public API is a commitment — keep it small and intentional
- *Apply*: always. Start private, open up only when genuinely needed

**Item 16: In public classes, use accessor methods, not public fields**
- Public fields lock you into a representation. Accessors allow changing internals
- Exception: package-private or private nested classes can expose fields directly
- *Apply when*: the class is public

**Item 17: Minimize mutability**
- Five rules: no mutators, no overridable methods, all fields final, all fields private, exclusive access to mutable components
- Immutable objects are simple, thread-safe, freely shareable, and great map keys
- If full immutability isn't practical, limit mutability as much as possible
- *Apply*: default to immutable. Only add mutability when you have a concrete reason

**Item 18: Favor composition over inheritance**
- Inheritance breaks encapsulation — subclass is coupled to superclass implementation details
- Use a forwarding class (wrapper/decorator) that delegates to a contained instance
- *Apply when*: you're tempted to extend a class from a different package for code reuse

**Item 19: Design and document for inheritance, or else prohibit it**
- Document self-use patterns (which overridable methods call which)
- Test your design by writing subclasses
- If not designed for inheritance, make the class final or give it only private constructors
- *Apply when*: you're creating a class that others will extend

**Item 20: Prefer interfaces to abstract classes**
- Interfaces allow multiple implementations, mixins, and skeletal implementations
- Combine interface + abstract skeletal implementation (AbstractX) for the best of both
- *Apply when*: defining a type that multiple classes should implement

**Item 21: Design interfaces for posterity**
- Default methods can break existing implementations in subtle ways
- Test new default methods against many existing implementations
- *Apply when*: adding methods to existing interfaces

**Item 22: Use interfaces only to define types**
- Don't use "constant interfaces" — they pollute the implementing class's API
- Put constants in a utility class or relevant enum
- *Apply when*: tempted to put constants in an interface

**Item 23: Prefer class hierarchies to tagged classes**
- Tagged classes (a `type` field with switch/case) are verbose and error-prone
- Replace with an abstract class and concrete subclasses for each tag
- *Apply when*: you see a class with a "type" or "kind" field controlling behavior

**Item 24: Favor static member classes over nonstatic**
- Nonstatic inner classes hold a reference to the enclosing instance — potential memory leak
- Use static unless you genuinely need access to the enclosing instance
- *Apply*: always use `static` for nested classes unless there's a reason not to

**Item 25: Limit source files to a single top-level class**
- Multiple top-level classes per file cause confusing compilation behavior
- *Apply*: always. One top-level class per .java file

---

## Chapter 5: Generics

**Item 26: Don't use raw types**
- Raw types lose type safety. Use `List<Object>` if you want any type, `List<?>` if unknown
- *Apply*: always use parameterized types

**Item 27: Eliminate unchecked warnings**
- Every unchecked warning is a potential ClassCastException at runtime
- If you can prove it's safe, suppress with `@SuppressWarnings("unchecked")` on the narrowest scope
- *Apply*: treat unchecked warnings as errors

**Item 28: Prefer lists to arrays**
- Arrays are covariant and reified; generics are invariant and erased — they don't mix well
- `List<String>` is safer than `String[]` in generic code
- *Apply when*: writing generic APIs

**Item 29: Favor generic types**
- Design your own classes with type parameters instead of requiring casts
- *Apply when*: building container or utility types

**Item 30: Favor generic methods**
- Type inference makes generic methods convenient: `Collections.<String>emptyList()` → `Collections.emptyList()`
- *Apply when*: writing utility methods that operate on parameterized types

**Item 31: Use bounded wildcards to increase API flexibility**
- PECS: Producer-Extends, Consumer-Super
- `<? extends T>` for reading, `<? super T>` for writing
- Don't use wildcards for return types — that forces callers to deal with wildcards
- *Apply when*: designing public APIs that take collections/iterables

**Item 32: Combine generics and varargs judiciously**
- Generic varargs are technically unsafe (heap pollution) but often practical
- Use `@SafeVarargs` when you can prove the method is safe
- *Apply when*: writing generic varargs methods

**Item 33: Consider typesafe heterogeneous containers**
- Use `Class<T>` as the key to get type-safe access to a map of different types
- *Apply when*: you need a container that holds values of many different types

---

## Chapter 6: Enums and Annotations

**Item 34: Use enums instead of int constants**
- Type-safe, namespace-safe, can have fields, methods, and implement interfaces
- Use constant-specific method implementations for behavior that varies per constant
- *Apply*: always use enums over int/String constants

**Item 35: Use instance fields instead of ordinals**
- Never derive a value from `ordinal()` — use an explicit field
- *Apply*: never use `ordinal()` for anything

**Item 36: Use EnumSet instead of bit fields**
- `EnumSet.of(Style.BOLD, Style.ITALIC)` is cleaner and type-safe vs. `BOLD | ITALIC`
- *Apply when*: replacing int-based bit flags

**Item 37: Use EnumMap instead of ordinal indexing**
- `EnumMap<Planet, List<Plant>>` instead of `List<Plant>[] plantsByPlanet`
- *Apply when*: mapping from enum keys

**Item 38: Emulate extensible enums with interfaces**
- Enums can't be extended, but they can implement interfaces — callers work with the interface
- *Apply when*: you need enum-like types that are extensible (e.g., operation codes)

**Item 39: Prefer annotations to naming patterns**
- `@Test` beats `testMethodName` naming conventions — compiler-checked, parameterizable
- *Apply*: use annotations over naming conventions

**Item 40: Consistently use the Override annotation**
- `@Override` catches errors at compile time (typos, wrong signatures)
- *Apply*: on every method that overrides a superclass method

**Item 41: Use marker interfaces to define types**
- Marker interface (e.g., `Serializable`) allows compile-time type checking
- Marker annotation is better when it applies to more than just classes
- *Apply when*: the marker should define a type for compile-time checks

---

## Chapter 7: Lambdas and Streams

**Item 42: Prefer lambdas to anonymous classes**
- Lambdas are concise and readable for functional interfaces
- Keep lambdas to 1-3 lines; extract to a method if longer
- *Apply when*: implementing single-method interfaces

**Item 43: Prefer method references to lambdas**
- `String::toLowerCase` is clearer than `s -> s.toLowerCase()`
- Five kinds: static, bound instance, unbound instance, constructor, array constructor
- *Apply when*: the method reference is at least as clear as the lambda

**Item 44: Favor the use of standard functional interfaces**
- Use `Function`, `Predicate`, `Supplier`, `Consumer`, `UnaryOperator`, `BinaryOperator`
- Primitive specializations avoid boxing: `IntFunction`, `LongPredicate`, etc.
- *Apply when*: designing APIs that accept behavioral parameters

**Item 45: Use streams judiciously**
- Streams shine for: transforming, filtering, combining, searching, grouping collections
- Avoid for: mutating local state, complex control flow (break/continue/return), checked exceptions
- *Apply*: use when they make code clearer, not just because they're available

**Item 46: Prefer side-effect-free functions in streams**
- The `forEach` terminal operation should only be used for reporting, not computation
- Use collectors: `toList()`, `toMap()`, `groupingBy()`, `joining()`
- *Apply*: avoid doing work in `forEach` that should be in the pipeline

**Item 47: Prefer Collection to Stream as a return type**
- `Collection` supports both iteration and streaming; `Stream` doesn't support iteration
- *Apply when*: returning a sequence of elements from a public API

**Item 48: Use caution when making streams parallel**
- Parallel streams help with `ArrayList`, arrays, `IntRange` — not `LinkedList`, `Stream.iterate`
- Incorrectly parallelized code can be slower or produce wrong results
- *Apply*: only parallelize when you have measured a performance benefit

---

## Chapter 8: Methods

**Item 49: Check parameters for validity**
- Public methods: `Objects.requireNonNull`, `checkArgument`, throw `IllegalArgumentException`
- Private methods: use assertions
- *Apply*: validate at the start of every public/protected method

**Item 50: Make defensive copies when needed**
- Copy mutable parameters on input and mutable fields on output
- Copy before validation to avoid TOCTOU attacks
- *Apply when*: a public API receives or returns mutable objects

**Item 51: Design method signatures carefully**
- Choose names carefully; avoid long parameter lists (max 4); prefer interfaces as parameter types
- Prefer two-element enums to boolean parameters: `TemperatureUnit.CELSIUS` over `true`
- *Apply*: every method you design

**Item 52: Use overloading judiciously**
- Overloading is resolved at compile time, not runtime — can be confusing
- Avoid overloading with same number of parameters; use different names instead
- *Apply when*: tempted to overload — usually better to use distinct method names

**Item 53: Use varargs judiciously**
- Validate that at least one argument is present if required
- For performance-sensitive code, provide fixed-arity overloads for common cases (0-3 args)
- *Apply when*: a method takes a variable number of same-type arguments

**Item 54: Return empty collections or arrays, not null**
- Never force callers to null-check. Return `Collections.emptyList()`, `new String[0]`
- *Apply*: always return empty over null

**Item 55: Return optionals judiciously**
- Use `Optional<T>` for returns where "no result" is a valid possibility
- Never use `Optional` for collections (return empty collection instead), fields, or map values
- Never use `Optional` as method parameters
- *Apply when*: a single value result might legitimately be absent

**Item 56: Write doc comments for all exposed API elements**
- Document preconditions, postconditions, side effects, thread safety
- Use `@param`, `@return`, `@throws`, `{@code}`, `{@literal}`
- *Apply when*: writing public API

---

## Chapter 9: General Programming

**Item 57: Minimize the scope of local variables**
- Declare at first use, not at the top of the block
- Prefer `for` loops to `while` when the loop variable isn't needed afterward
- *Apply*: always keep variable scope as tight as possible

**Item 58: Prefer for-each loops to traditional for loops**
- Cleaner syntax, no index errors, works with any `Iterable`
- Can't use for-each when: destructive filtering, transforming in-place, parallel iteration
- *Apply*: default to for-each unless you need the index or iterator

**Item 59: Know and use the libraries**
- Don't reinvent `Random`, string utils, collection algorithms, I/O
- Key libraries: `java.util`, `java.util.concurrent`, `java.io`, `java.time`
- *Apply*: always check if a standard library solution exists first

**Item 60: Avoid float and double if exact answers are required**
- Use `BigDecimal` or `long` (in cents) for monetary calculations
- *Apply when*: dealing with money or precision-sensitive calculations

**Item 61: Prefer primitive types to boxed primitives**
- Boxed primitives: identity comparisons (`==`), null dangers, performance overhead
- Unintentional auto-boxing in loops is a performance killer
- *Apply*: use primitives unless you need nullability or collections

**Item 62: Avoid strings where other types are more appropriate**
- Strings are poor substitutes for enums, aggregate types, capabilities
- *Apply when*: tempted to use a String as a type-unsafe stand-in

**Item 63: Beware the performance of string concatenation**
- Use `StringBuilder` in loops, not `+` operator
- *Apply when*: building strings in loops

**Item 64: Refer to objects by their interfaces**
- `List<String>` not `ArrayList<String>`; `Map<K,V>` not `HashMap<K,V>`
- *Apply*: always use the most general applicable type

**Item 65: Prefer interfaces to reflection**
- Reflection loses compile-time checking, is verbose, and is slow
- If you must use reflection, access objects through interfaces/superclasses
- *Apply*: avoid reflection unless you're building a framework or plugin system

**Item 66: Use native methods judiciously**
- Rarely needed for performance. Acceptable for accessing platform-specific facilities
- *Apply*: avoid unless accessing OS-specific features (JNA is usually cleaner than JNI)

**Item 67: Optimize judiciously**
- Don't optimize prematurely. Design clean APIs that don't preclude optimization later
- Profile before optimizing — your intuition about bottlenecks is usually wrong
- *Apply*: always write clean code first; optimize only with measurements

**Item 68: Adhere to generally accepted naming conventions**
- `TypeName`, `methodName`, `CONSTANT_NAME`, `localVariable`, `T` for type parameter
- *Apply*: always follow Java naming conventions

---

## Chapter 10: Exceptions

**Item 69: Use exceptions only for exceptional conditions**
- Never use exceptions for control flow. Use state-testing methods or `Optional` instead
- *Apply*: if the exceptional case is expected in normal operation, it's not exceptional

**Item 70: Use checked exceptions for recoverable conditions and runtime exceptions for programming errors**
- Checked: caller can reasonably recover (file not found, network error)
- Unchecked: programming bug (null pointer, array out of bounds)
- *Apply*: choose the exception type based on whether recovery is expected

**Item 71: Avoid unnecessary use of checked exceptions**
- If the caller can't recover meaningfully, use an unchecked exception
- Consider returning `Optional<T>` instead of throwing a checked exception
- *Apply when*: a checked exception adds burden without recovery benefit

**Item 72: Favor the use of standard exceptions**
- `IllegalArgumentException` — invalid parameter value
- `IllegalStateException` — object in wrong state for the operation
- `NullPointerException` — null where prohibited
- `IndexOutOfBoundsException` — index out of range
- `UnsupportedOperationException` — operation not supported
- `ConcurrentModificationException` — concurrent modification detected
- *Apply*: use these before creating custom exceptions

**Item 73: Throw exceptions appropriate to the abstraction**
- Translate low-level exceptions to match the abstraction: `catch (SQLException e) → throw new RepositoryException(e)`
- *Apply when*: a low-level exception would confuse callers of a high-level API

**Item 74: Document all exceptions thrown by each method**
- Use `@throws` for each checked exception and important unchecked ones
- *Apply*: document exceptions in public API Javadoc

**Item 75: Include failure-capture information in detail messages**
- Include the values that caused the failure: "Index 5 out of bounds for length 3"
- *Apply*: always include diagnostic information in exception messages

**Item 76: Strive for failure atomicity**
- After an exception, the object should be in the state it was before the operation
- Validate parameters before modifying state; use copy-on-write for complex operations
- *Apply when*: an operation could fail partway through

**Item 77: Don't ignore exceptions**
- Empty catch blocks hide bugs. At minimum, log and explain why it's acceptable
- *Apply*: never write an empty catch block without a comment explaining why

---

## Chapter 11: Concurrency

**Item 78: Synchronize access to shared mutable data**
- Both reading and writing shared mutable data must be synchronized (or use `volatile`/atomics)
- Best approach: don't share mutable data — confine it to one thread or make it immutable
- *Apply when*: multiple threads access the same mutable state

**Item 79: Avoid excessive synchronization**
- Never call an alien method (callback, listener) from a synchronized block — risks deadlock
- Do as little as possible inside synchronized regions
- *Apply when*: designing thread-safe classes

**Item 80: Prefer executors, tasks, and streams to threads**
- Use `ExecutorService` instead of creating threads manually
- Choose the right executor: single, fixed pool, cached, scheduled, work-stealing
- *Apply*: always use the executor framework instead of raw `Thread`

**Item 81: Prefer concurrency utilities to wait and notify**
- Use `ConcurrentHashMap`, `BlockingQueue`, `CountDownLatch`, `Semaphore`, `Phaser`
- If you must use `wait`/`notify`, always use `wait` in a loop, always notify
- *Apply*: always prefer `java.util.concurrent` over low-level primitives

**Item 82: Document thread safety**
- State the thread-safety level: immutable, unconditionally thread-safe, conditionally thread-safe, not thread-safe
- Use `@ThreadSafe`, `@NotThreadSafe`, `@GuardedBy` annotations
- *Apply*: document thread safety for every public class

**Item 83: Use lazy initialization judiciously**
- For static fields: lazy initialization holder class idiom
- For instance fields: double-check idiom
- Usually, normal initialization is better — lazy init adds complexity for little benefit
- *Apply*: only when initialization is expensive AND the field may not be used

**Item 84: Don't depend on the thread scheduler**
- Don't use `Thread.yield()` or `Thread.sleep()` for correctness
- Busy-wait is almost never appropriate — use proper synchronization primitives
- *Apply*: design so correctness doesn't depend on thread scheduling behavior

---

## Chapter 12: Serialization

**Item 85: Prefer alternatives to Java serialization**
- Java serialization is fundamentally dangerous (remote code execution attacks)
- Use JSON (Jackson, Gson) or Protocol Buffers instead
- If you must accept serialized data, use `ObjectInputFilter`
- *Apply*: avoid `java.io.Serializable` for new code

**Item 86: Implement Serializable with great caution**
- Serializable reduces flexibility (the serialized form becomes part of the public API)
- Increases security and testing burden
- *Apply*: don't implement unless absolutely necessary (e.g., the class must integrate with a legacy serialization framework)

**Item 87: Consider using a custom serialized form**
- If you must be Serializable, define explicit `readObject`/`writeObject` methods
- Default serialized form is only appropriate when the physical representation matches the logical content
- *Apply when*: forced to implement Serializable

**Item 88: Write readObject methods defensively**
- Validate all fields, make defensive copies of mutable components
- Don't invoke overridable methods during deserialization
- *Apply when*: implementing custom deserialization

**Item 89: For instance control, prefer enum types to readResolve**
- Enum singletons are automatically serialization-safe
- `readResolve` is error-prone — any non-transient field can be exploited
- *Apply when*: you need a singleton that is also Serializable

**Item 90: Consider serialization proxies instead of serialized instances**
- Serialization proxy pattern: private static nested class as the serialized form
- Most robust approach if you must use Java serialization
- *Apply when*: Serializable is unavoidable and you need maximum safety
