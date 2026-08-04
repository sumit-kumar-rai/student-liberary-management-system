# Student Library Management System

Console based Java project for managing books in a college library.

## What it does
- Add books and members
- Search books by title or author
- Issue a book to a member (14 day due period)
- Return a book (charges Rs 5/day fine if late)
- Show currently issued books
- Report: most issued books
- Report: export overdue books to a CSV file
- Saves everything to text files so data doesn't get lost when you close the program

## How to run
```
cd src
javac *.java
java Main
```

First run will auto add 3 sample books and 2 sample members so you have something to test with.

## Files
- `LibraryItem.java` - abstract class, base for Book (inheritance)
- `Book.java` - extends LibraryItem, overrides display() (polymorphism)
- `Member.java` - stores member id and name
- `IssueRecord.java` - stores one issue transaction (which book, which member, issue/due date)
- `Library.java` - main logic: catalog, issue/return, fines, search, reports, file save/load
- `Main.java` - menu driven interface

## Concepts used
- OOP: inheritance (Book extends LibraryItem), polymorphism (display() overridden), encapsulation (private fields + getters)
- Collections: ArrayList for books/members, HashMap for tracking who has which book and issue counts
- File I/O: BufferedReader/PrintWriter to save and load data as plain text files
