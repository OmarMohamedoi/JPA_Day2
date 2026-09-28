# 📚 JPA/Hibernate Library Management System

A robust Java application demonstrating foundational **Jakarta Persistence API (JPA) and Hibernate** concepts, featuring entity relationship mappings, persistence configurations, inheritance strategies, JPQL/Criteria API queries, and H2 in-memory database integration.

---

## 🏗️ Architecture & Design Decisions

### 1. Relationship Mappings & Owning Sides
* **Author & Book (`@OneToMany` / `@ManyToOne`):** Implemented as a bidirectional relationship. `Book` acts as the owning side, storing the foreign key (`author_id`). The inverse side on `Author` uses `mappedBy = "author"`.
* **Book & Publisher (`@ManyToOne`):** Connects each book to a single publisher via a foreign key column on the `Book` table.
* **Book & Category (`@ManyToMany`):** Implemented with a dedicated join table to map many books to multiple categories cleanly.

### 2. Fetch Choices
* **Lazy Loading (`LAZY`):** Configured by default for all entity collections to prevent unnecessary database queries and heavy memory overhead.
* **`JOIN FETCH`:** Explicitly utilized in JPQL queries to load associations eagerly within an active persistence context, solving the classic N+1 query problem and avoiding `LazyInitializationException` when accessing collections outside transactions.

### 3. Cascade Choices & Lifecycle
* **`CascadeType.ALL` & `orphanRemoval = true`:** Applied to the Author-Book relationship. Persisting an `Author` automatically cascades persistence down to newly associated `Books`. Furthermore, setting `orphanRemoval = true` ensures that unlinking a book from an author's collection automatically deletes it from the database.

### 4. Inheritance Strategy
* **`InheritanceType.JOINED`:** Selected for the `User` base class and its subclasses (`Employee` and `Customer`). 
  * *Why:* It normalizes the database by creating a parent `users` table for shared fields and separate `employees` / `customers` tables for specific attributes. This avoids data duplication and excessive `NULL` columns (unlike `SINGLE_TABLE`) while preventing expensive SQL `UNION` statements (unlike `TABLE_PER_CLASS`).
  * *Note:* Explicitly mapped with `@Table(name = "users")` to prevent syntax errors caused by reserved keywords in H2.

### 5. Query Decisions
* **JPQL & Aggregate Queries:** Standard object-oriented queries used for fetching records, utilizing dot-notation for nested properties, positional parameters (`?1`), and aggregate functions (`COUNT` combined with `GROUP BY`).
* **Criteria API:** Used for programmatic, type-safe queries. It powers both static lookups (searching by title) and **dynamic optional filtering**, where predicates are built conditionally at runtime only when search criteria are provided.

---

## 🚀 Getting Started & Running the App

1. **Clone or open the project** in your Java IDE (IntelliJ IDEA / Eclipse).
2. **Ensure dependencies are loaded** via Maven (`hibernate-core` and `h2`).
3. Run **`Main.java`** to execute entity persistence, cascade actions, inheritance creation, JPQL/Criteria queries, and lazy loading behaviors.
