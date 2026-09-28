# JPA/Hibernate Mini Project 🚀

A robust Java application demonstrating foundational **Jakarta Persistence API (JPA) and Hibernate** concepts, featuring entity relationship mappings, persistence configurations, JPQL queries, and H2 in-memory database integration.

---

## 📋 Core Concepts Covered

* **EntityManager & Persistence Context:** Managing the lifecycle of entities (Transient, Managed, Detached).
* **Entity Relationships:** 
  * Bidirectional `@OneToMany` / `@ManyToOne` (Author-Book)
  * `@ManyToOne` (Book-Publisher)
  * `@ManyToMany` (Book-Category)
* **JPQL & Advanced Queries:** Utilizing `JOIN FETCH` to solve the N+1 query problem, handling lazy initialization, and performing aggregate functions (`GROUP BY`, `COUNT`).
* **Transaction Management:** Handling transactions with `EntityTransaction` and understanding `RESOURCE_LOCAL` configurations.
* **Cascading Operations:** Managing entity persistence states with `CascadeType`.

---

## 🗂️ Project Structure

```text
jpa-hibernate-mini-project/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/example/
│   │   │       ├── entity/        # JPA Entities (Author, Book, Category, Publisher)
│   │   │       ├── util/          # JPA EntityManagerFactory utility
│   │   │       └── Main.java      # Application entry point & test operations
│   │   └── resources/
│   │       └── META-INF/
│   │           └── persistence.xml # H2 database and persistence configuration
│   └── test/
│
├── pom.xml                        # Maven configuration file
└── README.md
```
## 🛠️ Prerequisites

* **Java Development Kit (JDK):** Version 17 or 21
* **Build Tool:** Maven 3.8+
* **Database:** H2 In-Memory Database (managed automatically via Hibernate)
* **IDE:** IntelliJ IDEA or Eclipse

---

## 🚀 Getting Started & Running the App

1. **Clone or open the project** in your Java IDE.
2. **Ensure dependencies are loaded** via Maven (`hibernate-core` and `h2`).
3. Run **`Main.java`** to execute entity persistence, JPQL queries, lazy loading behavior tests, and aggregate operations.

---

## 💻 Sample Code Snippet

Initializing the `EntityManager` and persisting an author with books:

```java
try (EntityManager entityManager = JPA.getEntityManagerFactory().createEntityManager()) {
    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();
    
    Author author = new Author();
    author.setName("Omar");

    Book book = new Book();
    book.setTitle("Peaky Blinders");
    author.addBook(book);

    entityManager.persist(author);
    transaction.commit();
}
``
