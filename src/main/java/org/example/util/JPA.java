package org.example.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPA {
    private static final EntityManagerFactory entityManagerFactory;

    static {
        try{
            entityManagerFactory= Persistence.createEntityManagerFactory("empPU");
        }
        catch (Throwable ex){
            System.out.println("Initial EntityManagerFactory initiation has failed. "+ ex.getMessage());
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManagerFactory getEntityManagerFactory(){
        return entityManagerFactory;
    }

    public static void shutdown(){
        if (entityManagerFactory != null && entityManagerFactory.isOpen()){
            entityManagerFactory.close();
        }
    }
}
