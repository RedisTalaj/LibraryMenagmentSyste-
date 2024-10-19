package com.sda;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.sql.Date;
import java.time.LocalDate;

public class Book {
    public static final String JDBC_URL = "jdbc:mysql://localhost:3308/library";
    public static final String USERNAME = "user";
    public static final String PASSWORD = "user123";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("\nMirsevini, zgjidhni nje nga opsionet me poshte: ");
        while (running) {
            System.out.println("1. Shto nje liber!");
            System.out.println("2. Huazo nje liber!");
            System.out.println("3. Fshi nje liber!");
            System.out.println("4. Shiko listen e librave te huazuar!");
            System.out.println("0. Dil nga programi!");

            int opsion = scanner.nextInt();
            scanner.nextLine();

            switch (opsion) {
                case 1:
                    addBook(scanner);
                    break;

                case 2:
                    borrowBook(scanner);
                    break;

                case 3:
                    deleteBook(scanner);
                    break;

                case 4:
                    displayLoanedBooks(scanner);
                    break;

                case 0:
                    System.out.println("Duke dalë nga programi. Mirupafshim!");
                    running = false;
                    break;

                default:
                    System.out.println("Opsion i pavlefshem! Ju lutemi zgjidhni perseri.");
                    break;
            }
        }
    }

    public static void addBook(Scanner scanner) {
        Connection connection = null;
        try {
            System.out.println("Vendosni informacionet e librit qe doni te shtoni: ");

            System.out.print("Titulli i librit: ");
            String title = scanner.nextLine();

            System.out.print("Autori i librit: ");
            String author = scanner.nextLine();

            System.out.print("Sasia e librit: ");
            int sasia = scanner.nextInt();

            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            String sql = "INSERT INTO book (tittle, author, sasia) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, author);
            preparedStatement.setInt(3, sasia);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Libri u shtua me sukses!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void borrowBook(Scanner scanner) {
        Connection connection = null;
        try {
            System.out.println("Vendosni titullin e librit qe doni te huazoni: ");
            String title = scanner.nextLine();

            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            connection.setAutoCommit(false);  // Start transaction

            String checkAvailabilityQuery = "SELECT book_id, sasia FROM book WHERE tittle = ?";
            PreparedStatement checkAvailabilityStmt = connection.prepareStatement(checkAvailabilityQuery);
            checkAvailabilityStmt.setString(1, title);
            ResultSet resultSet = checkAvailabilityStmt.executeQuery();

            if (resultSet.next()) {
                int sasia = resultSet.getInt("sasia");
                int bookId = resultSet.getInt("book_id");

                if (sasia > 0) {
                    String borrowBookQuery = "UPDATE book SET sasia = sasia - 1 WHERE tittle = ?";
                    PreparedStatement borrowBookStmt = connection.prepareStatement(borrowBookQuery);
                    borrowBookStmt.setString(1, title);
                    int rowsUpdated = borrowBookStmt.executeUpdate();

                    String loanedBookQuery = "INSERT INTO loanedBook (book_id, loanedDate, returnDate) VALUES (?, ?, ?)";
                    PreparedStatement loanedBookStmt = connection.prepareStatement(loanedBookQuery);
                    loanedBookStmt.setInt(1, bookId);
                    loanedBookStmt.setDate(2, Date.valueOf(LocalDate.now()));
                    loanedBookStmt.setDate(3, Date.valueOf(LocalDate.now().plusDays(7)));
                    int rowsInserted = loanedBookStmt.executeUpdate();

                    if (rowsUpdated > 0 && rowsInserted > 0) {
                        System.out.println("Libri '" + title + "' u huazua me sukses!");
                    }

                    connection.commit();

                } else {
                    System.out.println("Libri '" + title + "' eshte i huazuar tashme ose nuk ka kopje te lira.");
                }
            } else {
                System.out.println("Libri me titull '" + title + "' nuk u gjet.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback();
                    System.out.println("Transaksioni deshtoi. Te dhenat u kthyen.");
                }
            } catch (Exception rollbackException) {
                rollbackException.printStackTrace();
            }
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void deleteBook(Scanner scanner) {
        Connection connection = null;
        try {
            System.out.println("Vendosni titullin e librit qe doni te fshini: ");
            String title = scanner.nextLine();

            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            String checkBookQuery = "SELECT * FROM book WHERE tittle = ?";
            PreparedStatement checkBookStmt = connection.prepareStatement(checkBookQuery);
            checkBookStmt.setString(1, title);
            ResultSet resultSet = checkBookStmt.executeQuery();

            if (resultSet.next()) {
                String deleteBookQuery = "DELETE FROM book WHERE tittle = ?";
                PreparedStatement deleteBookStmt = connection.prepareStatement(deleteBookQuery);
                deleteBookStmt.setString(1, title);

                int rowsDeleted = deleteBookStmt.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Libri '" + title + "' u fshi me sukses!");
                }
            } else {
                System.out.println("Libri me titull '" + title + "' nuk u gjet.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void displayLoanedBooks(Scanner scanner) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            String query = "SELECT b.tittle, b.author, lb.loanedDate, lb.returnDate " +
                    "FROM loanedBook lb " +
                    "JOIN book b ON lb.book_id = b.book_id";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Check if there are any loaned books
            if (!resultSet.isBeforeFirst()) {
                System.out.println("Nuk ka libra te huazuar aktualisht.");
                return;
            }

            System.out.println("Lista e librave te huazuar:");

            while (resultSet.next()) {
                String title = resultSet.getString("tittle");
                String author = resultSet.getString("author");
                Date loanedDate = resultSet.getDate("loanedDate");
                Date returnDate = resultSet.getDate("returnDate");

                System.out.println("Titulli: " + title);
                System.out.println("Autori: " + author);
                System.out.println("Data e huazimit: " + loanedDate);
                System.out.println("Afati i kthimit: " + returnDate);
                System.out.println("--------------------------------------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
