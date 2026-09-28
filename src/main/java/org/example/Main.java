package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.Predicate;
import org.example.entity.*;
import org.example.util.JPA;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // 1. Initial setup: Persisting Author and Books with cascade operations
        try(EntityManager entityManager = JPA.getEntityManagerFactory().createEntityManager()){
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            Author author1 = new Author();
            author1.setName("Omar");

            Book book1 = new Book();
            Book book2 = new Book();

            book1.setTitle("PeakyBlinders");
            book2.setTitle("Lacasa De Papel");

            author1.addBook(book1);
            author1.addBook(book2);
            entityManager.persist(author1);
            transaction.commit();
            System.out.println("Author and books added successfully");
        }

        // 2. Testing Lazy Loading and N+1 / JOIN FETCH behavior
        Author detachedAuthor = null;
        try(EntityManager entityManager = JPA.getEntityManagerFactory().createEntityManager()){
            var query = entityManager.createQuery("Select a from Author a where a.name = :name", Author.class);
            //SOLVING N+1 QUERY PROBLEM USING JOIN FETCH:
            // var query = entityManager.createQuery("Select a from Author a join fetch a.books where a.name = :name", Author.class);
            query.setParameter("name", "Omar");
            detachedAuthor = query.getSingleResult();
        }
        System.out.println(detachedAuthor.getName());
        // This will trigger LazyInitializationException if not using JOIN FETCH because the EntityManager is closed
        System.out.println(detachedAuthor.getBooks());

        // 3. Testing Aggregate Query (COUNT and GROUP BY)
        try(EntityManager entityManager = JPA.getEntityManagerFactory().createEntityManager()){
            List<Object[]> query = entityManager.createQuery("select a.name, Count(b) from Author a join a.books b group by a.name ", Object[].class).getResultList();
            for (Object[] row : query) {
                String authorName = (String) row[0];
                Long bookCount = (Long) row[1];
                System.out.println("Author: " + authorName + " | Total Books Written: " + bookCount);
            }
        }

        // 4. Testing Complex Relationships (Publisher & Category)
        try(EntityManager entityManager = JPA.getEntityManagerFactory().createEntityManager()){
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            Author author = new Author();
            author.setName("Ali");
            Book book1 = new Book();
            book1.setTitle("Tales of pirates");
            author.addBook(book1);
            Category category = new Category();
            category.setName("Action");
            HashSet<Book> books = new HashSet<>();
            books.add(book1);
            category.setBooks(books);
            Publisher waltDisney = new Publisher();
            waltDisney.setName("WaltDisney");
            book1.setPublisher(waltDisney);
            entityManager.persist(author);
            entityManager.persist(waltDisney);
            transaction.commit();
        }

        // =========================================================================
        // TASK 2 & 3: NEW SPECIFIC JPQL & CRITERIA API QUERIES
        // =========================================================================

        // 5. JPQL Query: Find all books belonging to a given publisher
        try (EntityManager entityManager = JPA.getEntityManagerFactory().createEntityManager()) {
            var query = entityManager.createQuery("SELECT b FROM Book b WHERE b.publisher.name = :publisherName", Book.class);
            query.setParameter("publisherName", "WaltDisney");
            List<Book> publisherBooks = query.getResultList();

            System.out.println("\n--- [JPQL Test] Books by Publisher (WaltDisney) ---");
            publisherBooks.forEach(b -> System.out.println("Found Book: " + b.getTitle()));
        }

        // 6. JPQL Query: Find a specific Book by ID using a positional parameter (?1)
        try (EntityManager entityManager = JPA.getEntityManagerFactory().createEntityManager()) {
            var query = entityManager.createQuery("SELECT b FROM Book b WHERE b.id = ?1", Book.class);
            query.setParameter(1, 1); // Positional parameter index 1
            Book specificBook = query.getSingleResult();

            System.out.println("\n--- [JPQL Test] Book found by Positional Parameter ID (1) ---");
            System.out.println("Found Book Title: " + specificBook.getTitle());
        }

        // 7. Criteria API Query: Find books strictly by title
        try (EntityManager entityManager = JPA.getEntityManagerFactory().createEntityManager()) {
            var cb = entityManager.getCriteriaBuilder();
            var cq = cb.createQuery(Book.class);
            var root = cq.from(Book.class);

            // Equivalent to: SELECT b FROM Book b WHERE b.title = 'PeakyBlinders'
            cq.select(root).where(cb.equal(root.get("title"), "PeakyBlinders"));

            List<Book> booksByTitle = entityManager.createQuery(cq).getResultList();
            System.out.println("\n--- [Criteria API Test] Books found by static title filter ---");
            booksByTitle.forEach(b -> System.out.println("Match: " + b.getTitle()));
        }

        // 8. Criteria API Query: Dynamic optional filters (Title and Author Name) //I DONT UNDERSTAND THIS
        try (EntityManager entityManager = JPA.getEntityManagerFactory().createEntityManager()) {
            var cb = entityManager.getCriteriaBuilder();
            var cq = cb.createQuery(Book.class);
            var root = cq.from(Book.class);

            List<Predicate> predicates = new ArrayList<>();

            // Simulating optional inputs (e.g., search title is provided, but author filter is null/omitted)
            String searchTitle = "Tales of pirates";
            String searchAuthorName = null; // Optional filter left blank

            if (searchTitle != null && !searchTitle.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("title"), searchTitle));
            }
            if (searchAuthorName != null && !searchAuthorName.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("author").get("name"), searchAuthorName));
            }

            // Apply predicates dynamically using conjunction (AND)
            cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));

            List<Book> dynamicBooks = entityManager.createQuery(cq).getResultList();
            System.out.println("\n--- [Criteria API Test] Dynamic Optional Filters Result ---");
            dynamicBooks.forEach(b -> System.out.println("Dynamic Match: " + b.getTitle()));
        }

        // =========================================================================
        // INHERITANCE TESTING
        // =========================================================================

        // 9. Testing Inheritance tables and records creation (`JOINED` strategy)
        try(EntityManager entityManager = JPA.getEntityManagerFactory().createEntityManager()){
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            Employee employee = new Employee();
            employee.setDepartment("SWE");
            employee.setSalary(80000.0);
            System.out.println("\nCHECKING ID BEFORE PERSISTING (Employee): " + employee.getId());
            entityManager.persist(employee);
            System.out.println("CHECKING ID AFTER PERSISTING (Employee): " + employee.getId());

            Customer customer = new Customer();
            customer.setLoyalPoints(153);
            entityManager.persist(customer);
            System.out.println("CHECKING IF ID IS CORRECTLY INCREMENTING (Customer): " + customer.getId());
            transaction.commit();
        }
    }
}