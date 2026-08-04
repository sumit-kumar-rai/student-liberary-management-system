// Base class for library items. Used this to show inheritance/polymorphism.
public abstract class LibraryItem {
    protected String id;
    protected String title;
    protected boolean issued;

    public LibraryItem(String id, String title) {
        this.id = id;
        this.title = title;
        this.issued = false;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isIssued() {
        return issued;
    }

    public void setIssued(boolean issued) {
        this.issued = issued;
    }

    // every item type will show its own info differently
    public abstract void display();
}
