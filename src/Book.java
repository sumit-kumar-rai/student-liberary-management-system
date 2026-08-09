
public class Book extends LibraryItem {
    private String author;
    private String isbn;

    public Book(String id, String title, String author, String isbn) {
        super(id, title);
        this.author = author;
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    // overriding parent method -> polymorphism
    @Override
    public void display() {
        String status = issued ? "ISSUED" : "AVAILABLE";
        System.out.println(id + " | " + title + " | " + author + " | " + isbn + " | " + status);
    }

    // convert to a line so it can be saved in a text file
    public String toLine() {
        return id + "," + title + "," + author + "," + isbn + "," + issued;
    }

    public static Book fromLine(String line) {
        String[] p = line.split(",");
        Book b = new Book(p[0], p[1], p[2], p[3]);
        b.issued = Boolean.parseBoolean(p[4]);
        return b;
    }
}
