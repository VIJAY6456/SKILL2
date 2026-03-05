package com.inventory.main;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.inventory.entity.Product;
import com.inventory.util.HibernateUtil;

public class HQLDemo {

    public static void main(String[] args) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Transaction tx = session.beginTransaction();

            session.save(new Product("Mouse", "Wireless Mouse", 800, 25));
            session.save(new Product("Monitor", "24 inch Monitor", 12000, 8));
            session.save(new Product("USB Cable", "Type C Cable", 250, 50));
            session.save(new Product("Chair", "Office Chair", 5000, 10));
            session.save(new Product("Desk", "Wooden Desk", 7000, 3));
            session.save(new Product("Keyboard", "Mechanical Keyboard", 3500, 15));
            session.save(new Product("Webcam", "HD Webcam", 4000, 5));

            tx.commit();

            System.out.println("\n--- 3a. Products Ascending by Price ---");
            List<Product> productsAsc = session.createQuery("FROM Product ORDER BY price ASC", Product.class).list();
            productsAsc.forEach(p -> System.out.println(p));

            System.out.println("\n--- 3b. Products Descending by Price ---");
            List<Product> productsDesc = session.createQuery("FROM Product ORDER BY price DESC", Product.class).list();
            productsDesc.forEach(p -> System.out.println(p));

            System.out.println("\n--- 4. Products Sorted by Quantity (Highest first) ---");
            List<Product> productsByQuantity = session.createQuery("FROM Product ORDER BY quantity DESC", Product.class).list();
            productsByQuantity.forEach(p -> System.out.println(p));

            System.out.println("\n--- 5a. Pagination - First 3 Products ---");
            List<Product> firstThree = session.createQuery("FROM Product ORDER BY id", Product.class)
                    .setFirstResult(0).setMaxResults(3).list();
            firstThree.forEach(p -> System.out.println(p));

            System.out.println("\n--- 5b. Pagination - Next 3 Products ---");
            List<Product> nextThree = session.createQuery("FROM Product ORDER BY id", Product.class)
                    .setFirstResult(3).setMaxResults(3).list();
            nextThree.forEach(p -> System.out.println(p));

            System.out.println("\n--- 6a. Count Total Products ---");
            Long totalProducts = session.createQuery("SELECT COUNT(*) FROM Product", Long.class).uniqueResult();
            System.out.println("Total products: " + totalProducts);

            System.out.println("\n--- 6b. Count Products with Quantity > 0 ---");
            Long countQuantityGT0 = session.createQuery("SELECT COUNT(*) FROM Product WHERE quantity > 0", Long.class)
                    .uniqueResult();
            System.out.println("Products with quantity > 0: " + countQuantityGT0);

            System.out.println("\n--- 6c. Count Products Grouped by Description ---");
            List<Object[]> countByDesc = session.createQuery("SELECT description, COUNT(*) FROM Product GROUP BY description").list();
            for (Object[] row : countByDesc) {
                System.out.println("Description: " + row[0] + ", Count: " + row[1]);
            }

            System.out.println("\n--- 6d. Minimum and Maximum Price ---");
            List<Object[]> minMaxList = session.createQuery("SELECT MIN(price), MAX(price) FROM Product").list();
            Object[] minMax = minMaxList.get(0);
            System.out.println("Min price: " + minMax[0] + ", Max price: " + minMax[1]);

            System.out.println("\n--- 7. GROUP BY Description (Same as 6c) ---");
            List<Object[]> groupedProducts = session.createQuery("SELECT description, COUNT(*) FROM Product GROUP BY description").list();
            for (Object[] row : groupedProducts) {
                System.out.println("Description: " + row[0] + ", Count: " + row[1]);
            }

            System.out.println("\n--- 8. Filter Products with Price BETWEEN 1000 AND 10000 ---");
            List<Product> productsInRange = session.createQuery("FROM Product WHERE price BETWEEN :min AND :max", Product.class)
                    .setParameter("min", 1000.0).setParameter("max", 10000.0).list();
            productsInRange.forEach(p -> System.out.println(p));

            System.out.println("\n--- 9a. Names starting with 'M' ---");
            List<Product> namesStartM = session.createQuery("FROM Product WHERE name LIKE 'M%'", Product.class).list();
            namesStartM.forEach(p -> System.out.println(p));

            System.out.println("\n--- 9b. Names ending with 'r' ---");
            List<Product> namesEndR = session.createQuery("FROM Product WHERE name LIKE '%r'", Product.class).list();
            namesEndR.forEach(p -> System.out.println(p));

            System.out.println("\n--- 9c. Names containing 'top' ---");
            List<Product> namesContainTop = session.createQuery("FROM Product WHERE name LIKE '%top%'", Product.class).list();
            namesContainTop.forEach(p -> System.out.println(p));

            System.out.println("\n--- 9d. Names with exact length 5 ---");
            List<Product> namesLength5 = session.createQuery("FROM Product WHERE LENGTH(name) = 5", Product.class).list();
            namesLength5.forEach(p -> System.out.println(p));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
            HibernateUtil.shutdown();
        }
    }
}