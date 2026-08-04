import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();
        library.loadData();
        library.loadSampleDataIfEmpty();

        Scanner sc = new Scanner(System.in);
        boolean running = true;

        System.out.println("===== STUDENT LIBRARY MANAGEMENT SYSTEM =====");

        while (running) {
            printMenu();
            System.out.print("Enter choice: ");
            String choice = sc.nextLine().trim();

            if (choice.equals("1")) {
                library.showAllBooks();

            } else if (choice.equals("2")) {
                System.out.print("Enter title keyword: ");
                library.searchByTitle(sc.nextLine().trim());

            } else if (choice.equals("3")) {
                System.out.print("Enter author keyword: ");
                library.searchByAuthor(sc.nextLine().trim());

            } else if (choice.equals("4")) {
                System.out.print("Book ID: ");
                String bookId = sc.nextLine().trim();
                System.out.print("Member ID: ");
                String memberId = sc.nextLine().trim();
                library.issueBook(bookId, memberId);

            } else if (choice.equals("5")) {
                System.out.print("Book ID to return: ");
                library.returnBook(sc.nextLine().trim());

            } else if (choice.equals("6")) {
                System.out.print("New Book ID: ");
                String id = sc.nextLine().trim();
                System.out.print("Title: ");
                String title = sc.nextLine().trim();
                System.out.print("Author: ");
                String author = sc.nextLine().trim();
                System.out.print("ISBN: ");
                String isbn = sc.nextLine().trim();
                library.addBook(new Book(id, title, author, isbn));
                System.out.println("Book added.");

            } else if (choice.equals("7")) {
                System.out.print("New Member ID: ");
                String id = sc.nextLine().trim();
                System.out.print("Name: ");
                String name = sc.nextLine().trim();
                library.addMember(new Member(id, name));
                System.out.println("Member added.");

            } else if (choice.equals("8")) {
                library.showIssuedBooks();

            } else if (choice.equals("9")) {
                library.mostIssuedReport();

            } else if (choice.equals("10")) {
                library.overdueReport();

            } else if (choice.equals("0")) {
                library.saveData();
                System.out.println("Data saved. Bye!");
                running = false;

            } else {
                System.out.println("Invalid choice.");
            }

            System.out.println();
        }
        sc.close();
    }

    static void printMenu() {
        System.out.println("1. Show all books");
        System.out.println("2. Search by title");
        System.out.println("3. Search by author");
        System.out.println("4. Issue book");
        System.out.println("5. Return book");
        System.out.println("6. Add new book");
        System.out.println("7. Add new member");
        System.out.println("8. Show issued books");
        System.out.println("9. Report - most issued books");
        System.out.println("10. Report - export overdue books (csv)");
        System.out.println("0. Save & Exit");
    }
}
