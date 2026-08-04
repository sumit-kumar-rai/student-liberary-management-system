import java.time.LocalDate;

// Represents one issue transaction - which book, which member, and dates
public class IssueRecord {
    private String bookId;
    private String memberId;
    private LocalDate issueDate;
    private LocalDate dueDate;

    public IssueRecord(String bookId, String memberId, LocalDate issueDate, LocalDate dueDate) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
    }

    public String getBookId() {
        return bookId;
    }

    public String getMemberId() {
        return memberId;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String toLine() {
        return bookId + "," + memberId + "," + issueDate + "," + dueDate;
    }

    public static IssueRecord fromLine(String line) {
        String[] p = line.split(",");
        return new IssueRecord(p[0], p[1], LocalDate.parse(p[2]), LocalDate.parse(p[3]));
    }
}
