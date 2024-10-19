package com.sda;
import com.sda.service.BookService;
import java.util.Scanner;

public class AppMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        BookService bookService = new BookService();

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
                    bookService.addBook(scanner);
                    break;

                case 2:
                    bookService.borrowBook(scanner);
                    break;

                case 3:
                    bookService.deleteBook(scanner);
                    break;

                case 4:
                    bookService.displayLoanedBooks(scanner);
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
}
