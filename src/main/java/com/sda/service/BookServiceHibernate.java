package com.sda.service;

import com.sda.configuration.HibernateUtil;
import com.sda.entity.Book;
import com.sda.entity.LoanedBook;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class BookServiceHibernate {
    public static void addBook(Scanner scanner) {
        Session session = null;
        Transaction transaction = null;
        try {
            System.out.println("Vendosni informacionet e librit qe doni te shtoni: ");

            System.out.print("Titulli i librit: ");
            String title = scanner.nextLine();

            System.out.print("Autori i librit: ");
            String author = scanner.nextLine();

            System.out.print("Sasia e librit: ");
            int sasia = scanner.nextInt();

            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            Book book = new Book();
            book.setBook_id(null);
            book.setTitle(title);
            book.setAuthor(author);
            book.setSasia(sasia);

            session.save(book);

            transaction.commit();
            System.out.println("Libri u shtua me sukses!");

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    public static void borrowBook(Scanner scanner) {
        Session session = null;
        Transaction transaction = null;
        try {
            System.out.println("Vendosni titullin e librit qe doni te huazoni: ");
            String title = scanner.nextLine();

            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            Book book = (Book) session.createQuery("FROM Book WHERE title = :title")
                    .setParameter("title", title)
                    .uniqueResult();

            if (book != null && book.getSasia() > 0) {
                book.setSasia(book.getSasia() - 1);
                session.update(book);

                LoanedBook loanedBook = new LoanedBook();
                loanedBook.setBook(book);
                loanedBook.setLoanedDate(Date.valueOf(LocalDate.now()));
                loanedBook.setReturnDate(Date.valueOf(LocalDate.now().plusDays(7)));

                session.save(loanedBook);

                transaction.commit();
                System.out.println("Libri '" + title + "' u huazua me sukses!");

            } else {
                System.out.println("Libri '" + title + "' eshte i huazuar tashme ose nuk ka kopje te lira.");
            }

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    public static void deleteBook(Scanner scanner) {
        Session session = null;
        Transaction transaction = null;
        try {
            System.out.println("Vendosni titullin e librit qe doni te fshini: ");
            String title = scanner.nextLine();

            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            Book book = (Book) session.createQuery("FROM Book WHERE title = :title")
                    .setParameter("title", title)
                    .uniqueResult();

            if (book != null) {
                session.delete(book);
                transaction.commit();
                System.out.println("Libri '" + title + "' u fshi me sukses!");
            } else {
                System.out.println("Libri me titull '" + title + "' nuk u gjet.");
            }

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    public static void displayLoanedBooks(Scanner scanner) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            List<LoanedBook> loanedBooks = session.createQuery("FROM LoanedBook", LoanedBook.class).list();

            if (loanedBooks.isEmpty()) {
                System.out.println("Nuk ka libra te huazuar aktualisht.");
            } else {
                System.out.println("Lista e librave te huazuar:");
                for (LoanedBook loanedBook : loanedBooks) {
                    if (loanedBook.getBook() != null) {  // Check if book is not null
                        System.out.println("Titulli: " + loanedBook.getBook().getTitle());
                        System.out.println("Autori: " + loanedBook.getBook().getAuthor());
                        System.out.println("Data e huazimit: " + loanedBook.getLoanedDate());
                        System.out.println("Afati i kthimit: " + loanedBook.getReturnDate());
                        System.out.println("--------------------------------------------------");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }
}
