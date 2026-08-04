import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

public class Library {

    ArrayList<Book> books = new ArrayList<>();
    ArrayList<Member> members = new ArrayList<>();
    ArrayList<IssueRecord> issues = new ArrayList<>(); // only currently active issues

    // bookId -> how many times it has been issued in total (for reports)
    HashMap<String, Integer> issueCount = new HashMap<>();

    static final int LOAN_DAYS = 14;
    static final double FINE_PER_DAY = 5.0;

    // ---------- basic catalog stuff ----------

    void addBook(Book b) {
        books.add(b);
    }

    Book findBook(String id) {
        for (Book b : books) {
            if (b.getId().equalsIgnoreCase(id)) return b;
        }
        return null;
    }

    void addMember(Member m) {
        members.add(m);
    }

    Member findMember(String id) {
        for (Member m : members) {
            if (m.getId().equalsIgnoreCase(id)) return m;
        }
        return null;
    }

    IssueRecord findIssueByBookId(String bookId) {
        for (IssueRecord r : issues) {
            if (r.getBookId().equalsIgnoreCase(bookId)) return r;
        }
        return null;
    }

    void showAllBooks() {
        if (books.isEmpty()) {
            System.out.println("No books yet.");
            return;
        }
        for (Book b : books) {
            b.display(); // polymorphic call
        }
    }

    void searchByTitle(String keyword) {
        boolean found = false;
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                b.display();
                found = true;
            }
        }
        if (!found) System.out.println("No matching books.");
    }

    void searchByAuthor(String keyword) {
        boolean found = false;
        for (Book b : books) {
            if (b.getAuthor().toLowerCase().contains(keyword.toLowerCase())) {
                b.display();
                found = true;
            }
        }
        if (!found) System.out.println("No matching books.");
    }

    // ---------- issue / return ----------

    void issueBook(String bookId, String memberId) {
        Book b = findBook(bookId);
        Member m = findMember(memberId);

        if (b == null) {
            System.out.println("No such book.");
            return;
        }
        if (m == null) {
            System.out.println("No such member.");
            return;
        }
        if (b.isIssued()) {
            System.out.println("Book already issued.");
            return;
        }

        b.setIssued(true);
        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(LOAN_DAYS);
        issues.add(new IssueRecord(bookId, memberId, issueDate, dueDate));
        issueCount.put(bookId, issueCount.getOrDefault(bookId, 0) + 1);

        System.out.println("Issued \"" + b.getTitle() + "\" to " + m.getName() + ". Due on " + dueDate);
    }

    void returnBook(String bookId) {
        Book b = findBook(bookId);
        IssueRecord record = findIssueByBookId(bookId);

        if (b == null || record == null || !b.isIssued()) {
            System.out.println("This book is not currently issued.");
            return;
        }

        long lateDays = ChronoUnit.DAYS.between(record.getDueDate(), LocalDate.now());

        b.setIssued(false);
        issues.remove(record);

        if (lateDays > 0) {
            double fine = lateDays * FINE_PER_DAY;
            System.out.println("Returned \"" + b.getTitle() + "\". Late by " + lateDays
                    + " day(s). Fine = Rs " + fine);
        } else {
            System.out.println("Returned \"" + b.getTitle() + "\" on time. No fine.");
        }
    }

    void showIssuedBooks() {
        if (issues.isEmpty()) {
            System.out.println("No books are issued right now.");
            return;
        }
        for (IssueRecord r : issues) {
            Book b = findBook(r.getBookId());
            Member m = findMember(r.getMemberId());
            System.out.println(b.getTitle() + " -> " + m.getName() + " | Due: " + r.getDueDate());
        }
    }

    // ---------- reports ----------

    void mostIssuedReport() {
        if (books.isEmpty()) {
            System.out.println("No books yet.");
            return;
        }
        // simple sort by copying into a list and using a comparator
        ArrayList<Book> sorted = new ArrayList<>(books);
        sorted.sort((b1, b2) -> issueCount.getOrDefault(b2.getId(), 0) - issueCount.getOrDefault(b1.getId(), 0));

        System.out.println("Book Title -> Times Issued");
        for (Book b : sorted) {
            System.out.println(b.getTitle() + " -> " + issueCount.getOrDefault(b.getId(), 0));
        }
    }

    void overdueReport() {
        ArrayList<IssueRecord> overdue = new ArrayList<>();
        for (IssueRecord r : issues) {
            long lateDays = ChronoUnit.DAYS.between(r.getDueDate(), LocalDate.now());
            if (lateDays > 0) overdue.add(r);
        }

        if (overdue.isEmpty()) {
            System.out.println("No overdue books right now.");
            return;
        }

        try {
            new File("reports").mkdirs();
            PrintWriter pw = new PrintWriter(new FileWriter("reports/overdue_report.csv"));
            pw.println("BookId,Title,Member,DueDate,DaysLate,Fine");

            for (IssueRecord r : overdue) {
                Book b = findBook(r.getBookId());
                Member m = findMember(r.getMemberId());
                long lateDays = ChronoUnit.DAYS.between(r.getDueDate(), LocalDate.now());
                double fine = lateDays * FINE_PER_DAY;

                System.out.println(b.getTitle() + " -> " + m.getName() + " | " + lateDays + " day(s) late | Fine Rs " + fine);
                pw.println(r.getBookId() + "," + b.getTitle() + "," + m.getName() + "," + r.getDueDate() + "," + lateDays + "," + fine);
            }
            pw.close();
            System.out.println("Report saved to reports/overdue_report.csv");
        } catch (IOException e) {
            System.out.println("Could not write report: " + e.getMessage());
        }
    }

    // ---------- save / load from files ----------

    void saveData() {
        try {
            new File("data").mkdirs();

            PrintWriter bw = new PrintWriter(new FileWriter("data/books.txt"));
            for (Book b : books) bw.println(b.toLine());
            bw.close();

            PrintWriter mw = new PrintWriter(new FileWriter("data/members.txt"));
            for (Member m : members) mw.println(m.toLine());
            mw.close();

            PrintWriter iw = new PrintWriter(new FileWriter("data/issues.txt"));
            for (IssueRecord r : issues) iw.println(r.toLine());
            iw.close();

            PrintWriter cw = new PrintWriter(new FileWriter("data/counts.txt"));
            for (String bookId : issueCount.keySet()) {
                cw.println(bookId + "," + issueCount.get(bookId));
            }
            cw.close();

        } catch (IOException e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }

    void loadData() {
        readFile("data/books.txt", line -> books.add(Book.fromLine(line)));
        readFile("data/members.txt", line -> members.add(Member.fromLine(line)));
        readFile("data/issues.txt", line -> issues.add(IssueRecord.fromLine(line)));
        readFile("data/counts.txt", line -> {
            String[] p = line.split(",");
            issueCount.put(p[0], Integer.parseInt(p[1]));
        });
    }

    // small helper to avoid repeating file-reading code 4 times
    interface LineHandler {
        void handle(String line);
    }

    void readFile(String path, LineHandler handler) {
        File f = new File(path);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isBlank()) handler.handle(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading " + path + ": " + e.getMessage());
        }
    }

    void loadSampleDataIfEmpty() {
        if (books.isEmpty()) {
            addBook(new Book("B001", "Introduction to Algorithms", "Cormen", "9780262033848"));
            addBook(new Book("B002", "Clean Code", "Robert C. Martin", "9780132350884"));
            addBook(new Book("B003", "Effective Java", "Joshua Bloch", "9780134685991"));
        }
        if (members.isEmpty()) {
            addMember(new Member("M001", "Sumit Kumar Rai"));
            addMember(new Member("M002", "Aditi Sharma"));
        }
    }
}
