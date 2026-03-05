package com.inventory.main;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.inventory.entity.Product;
import com.inventory.util.HibernateUtil;

public class MainApp {

    public static void main(String[] args) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Transaction tx = session.beginTransaction();

            // Insert multiple products
            session.save(new Product("Laptop", "Gaming Laptop", 55000, 5));
            session.save(new Product("Mouse", "Wireless Mouse", 800, 25));
            session.save(new Product("Monitor", "24 inch Monitor", 12000, 8));
            session.save(new Product("USB Cable", "Type C Cable", 250, 50));
            session.save(new Product("Chair", "Office Chair", 5000, 10));
            session.save(new Product("Desk", "Wooden Desk", 7000, 3));
            session.save(new Product("Keyboard", "Mechanical Keyboard", 3500, 15));
            session.save(new Product("Webcam", "HD Webcam", 4000, 5));

            tx.commit();

            System.out.println("\n--- Retrieve Product by ID ---");
            Product product = session.get(Product.class, 1);
            if (product != null) {
                System.out.println("Retrieved: " + product.getName());
            }

            // Update price or quantity
            tx = session.beginTransaction();
            Product pToUpdate = session.get(Product.class, 1);
            if (pToUpdate != null) {
                pToUpdate.setPrice(52000);
                pToUpdate.setQuantity(6);
                session.update(pToUpdate);
                System.out.println("Updated: " + pToUpdate);
            }
            tx.commit();

            // Safe delete
            tx = session.beginTransaction();
            int idToDelete = 100; // example ID
            Product pToDelete = session.get(Product.class, idToDelete);
            if (pToDelete != null) {
                session.delete(pToDelete);
                System.out.println("Deleted: " + pToDelete.getName());
            } else {
                System.out.println("Product with ID " + idToDelete + " not found. Nothing deleted.");
            }
            tx.commit();

            // HQL: Sorting
            System.out.println("\n--- Products Ascending by Price ---");
            List<Product> productsAsc = session.createQuery("FROM Product ORDER BY price ASC", Product.class).list();
            productsAsc.forEach(System.out::println);

            System.out.println("\n--- Products Descending by Price ---");
            List<Product> productsDesc = session.createQuery("FROM Product ORDER BY price DESC", Product.class).list();
            productsDesc.forEach(System.out::println);

            // HQL: Pagination (first 3)
            System.out.println("\n--- Pagination: First 3 Products ---");
            List<Product> firstThree = session.createQuery("FROM Product ORDER BY id", Product.class)
                    .setFirstResult(0).setMaxResults(3).list();
            firstThree.forEach(System.out::println);

            // HQL: Aggregates
            List<Object[]> minMaxList = session.createQuery("SELECT MIN(price), MAX(price) FROM Product").list();
            Object[] minMax = minMaxList.get(0);
            System.out.println("\nMin price: " + minMax[0] + ", Max price: " + minMax[1]);

            Long totalProducts = session.createQuery("SELECT COUNT(*) FROM Product", Long.class).uniqueResult();
            System.out.println("Total products: " + totalProducts);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
            HibernateUtil.shutdown();
        }
    }
}