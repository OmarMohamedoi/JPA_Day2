package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.entity.Author;
import org.example.entity.Book;
import org.example.util.JPA;

import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

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

        Author detachedAuthor = null;
        try(EntityManager entityManager = JPA.getEntityManagerFactory().createEntityManager()){
            var query = entityManager.createQuery("Select a from Author a where a.name = :name", Author.class);
            //SOLVING N+1 QUERY PROBLEM USING JOIN FETCH   // var query = entityManager.createQuery("Select a from Author a join fetch a.books where a.name = :name", Author.class);
            query.setParameter("name", "Omar");
            detachedAuthor = query.getSingleResult();
//            var result = query.getResultList();
//            result.stream().forEach(System.out::println);
//            result.stream().flatMap(a ->a.getBooks().stream())
//                    .forEach(System.out::println);

        }
        System.out.println(detachedAuthor.getName());
        System.out.println(detachedAuthor.getBooks()); //LAZY INITIATIAN EXCEPTINO WHEN LAZY FETCH

        try(EntityManager entityManager = JPA.getEntityManagerFactory().createEntityManager()){
            List<Object[]> query = entityManager.createQuery("select a.name, Count(b) from Author a join a.books b group by a.name ", Object[].class).getResultList();
            for (Object[] row : query) {
                String authorName = (String) row[0];   // The first item in SELECT is a.name
                Long bookCount = (Long) row[1];       // The second item in SELECT is COUNT(b)

                System.out.println("Author: " + authorName + " | Total Books Written: " + bookCount);

            }

    }
}}