// Represents a student who can borrow books
public class Member {
    private String id;
    private String name;

    public Member(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toLine() {
        return id + "," + name;
    }

    public static Member fromLine(String line) {
        String[] p = line.split(",");
        return new Member(p[0], p[1]);
    }
}
